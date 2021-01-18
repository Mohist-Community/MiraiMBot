package com.mohistmc.miraimbot.command;

import com.mohistmc.miraimbot.MiraiMBot;
import com.mohistmc.miraimbot.annotations.Command;
import com.mohistmc.miraimbot.config.ConfigManager;
import com.mohistmc.miraimbot.plugin.PluginLoader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mohistmc.miraimbot.permission.Permission;
import com.mohistmc.miraimbot.plugin.MohistPlugin;
import com.mohistmc.miraimbot.utils.Utils;
import com.mohistmc.yaml.util.Charsets;
import lombok.SneakyThrows;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class CommandManager {

    private static final Logger log = LogManager.getLogger("CommandManager");
    private static final Map<String, CommandExecutor> executors = Maps.newHashMap();
    private static final ExecutorService tasks = Utils.createThreadPool(30, 100, "Command-%d");
    public static boolean console = false;


    public static ConcurrentMap<String, CommandExecutor> getExecutors() {
        ConcurrentMap<String, CommandExecutor> m = Maps.newConcurrentMap();
        m.putAll(executors);
        return m;
    }

    @SneakyThrows
    public static void register(MohistPlugin plugin) {
        JarFile jarFile = plugin.getSourceFile();
        ZipEntry ze = jarFile.getEntry("commands.json");
        InputStream is = ze != null ? jarFile.getInputStream(ze) : null;
        if (is != null) {
            log.debug("从 {} 找到了commands.json", jarFile.getName());
            String jstr = IOUtils.toString(is, Charsets.UTF_8);
            log.debug("从 {} 读取到的commands.json: {}", jarFile.getName(), jstr);
            if (JSON.isValid(jstr)) {
                JSONArray ja = JSON.parseArray(jstr);
                for (JSONObject json : ja.toArray(new JSONObject[0])) {
                    Class<?> executorClass = PluginLoader.INSTANCE.loadClass(json.getString("class"));
                    if (executorClass.getSuperclass() != CommandExecutor.class) {
                        log.error("指令处理器没有继承CommandExecutor ({})", json.getString("class"));
                        return;
                    }
                    CommandExecutor executor = (CommandExecutor) executorClass.newInstance();
                    executor.label = json.getString("label");
                    executor.usage = json.getString("usage");
                    executor.description = json.getString("description");
                    executor.noshow = json.getJSONObject("noshow").getBooleanValue("enable");
                    executor.opCan = json.getJSONObject("noshow").getBooleanValue("opCan");
                    executor.onlyOp = json.getBooleanValue("onlyOp");
                    executor.permissionEnable = json.getJSONObject("permission").getBooleanValue("enable");
                    executor.permission = json.getJSONObject("permission").getString("permission");
                    executor.type = Command.Type.valueOf(json.getString("type"));
                    register(executor, json.getJSONArray("alias").toArray(new String[0]));
                }
            } else {
                log.error("插件 {} 损坏", plugin.getName());
            }
            is.close();
        } else {
            log.debug("从 {} 没有找到commands.json", jarFile.getName());
        }
    }

    public static void register(CommandExecutor executor, String... aliases) {
        executors.put(executor.getLabel(), executor);
        log.info("注册了指令 {}", executor.label);
        for (String alia : aliases) {
            executors.put(alia, executor);
            log.info("注册了别称 {}", alia);
        }
    }

    public static void registers(CommandExecutor... executors) {
        for (CommandExecutor executor : executors) register(executor);
    }

    public static void parseAndCall(MessageEvent event) {
        log.debug("处理并触发指令 {}", event.getMessage().contentToString());
        call(parse(event));
    }

    public static void parseAndCall(MessageChain msgs, UserOrBot sender) {
        log.debug("处理并触发指令 {}", msgs.contentToString());
        call(parse(msgs, sender));
    }

    private static long last = System.currentTimeMillis() + 10000;

    public static void call(CommandResult result) {
        if (System.currentTimeMillis() - last < 3000) {
            last = System.currentTimeMillis();
            return;
        }
        log.debug("开始处理指令 {}", result.getLabel());
        if (executors.containsKey(result.getLabel())) {
            log.debug("指令 {} 存在", result.getLabel());
            CommandExecutor executor = executors.get(result.getLabel());
            log.debug("指令 {} 状态: opOnly: {},permission: {}", result.getLabel(), executor.isOnlyOp(), executor.permissionEnable);
            if (executor.type != Command.Type.ALL) {
                if (executor.type == Command.Type.GROUP && !Utils.isGroup(result.getSender())) {
                    Utils.sendMessageOrGroup(result, "抱歉，当前指令只可以在群组使用。");
                    last = System.currentTimeMillis();
                    return;
                } else if (executor.type == Command.Type.FRIEND && !Utils.isFriend(result.getSender())) {
                    Utils.sendMessageOrGroup(result, "抱歉，当前指令只可以在私聊使用。");
                    last = System.currentTimeMillis();
                    return;
                } else if (executor.type == Command.Type.CONSOLE && !Utils.isConsole(result.getSender())) {
                    Utils.sendMessageOrGroup(result, "抱歉，当前指令只可以在控制台使用。");
                    last = System.currentTimeMillis();
                    return;
                }
            }
            if (executor.isOnlyOp()) {
                if (!Permission.isOp(result.getSender())) {
                    Utils.sendMessageOrGroup(result, "抱歉，当前指令只可以机器人管理员使用。");
                    last = System.currentTimeMillis();
                    return;
                }
                log.debug("指令 {} 触发", result.getLabel());
                tasks.execute(() -> executor.onCommand(result));
                last = System.currentTimeMillis();
                return;
            } else if (executor.permissionEnable) {
                if (!Permission.hasPermission(result.getSender(), executor.permission)) {
                    Utils.sendMessageOrGroup(result, "抱歉，您的权限不足。");
                    last = System.currentTimeMillis();
                    return;
                }
                log.debug("指令 {} 触发", result.getLabel());
                tasks.execute(() -> executor.onCommand(result));
                last = System.currentTimeMillis();
                return;
            }
            log.debug("指令 {} 触发: {}", result.getLabel(), executor.getClass());
            tasks.execute(() -> executor.onCommand(result));
            last = System.currentTimeMillis();
        } else {
            log.debug("指令 {} 不存在", result.getLabel());
            Utils.sendMessageOrGroup(result, ConfigManager.getConfig().getString("path_command_unknown", "未知指令。请使用\"#help\"来获得帮助"));
        }
    }

    public static CommandResult parse(MessageEvent event) {
        List<String> args = Lists.newArrayList();
        String label = "";
        String msg = event.getMessage().contentToString();
        String[] waitParse = msg.split(" ");
        label = waitParse[0].replaceFirst("#", "");
        log.debug("转换指令 {}", label);
        args.addAll(Arrays.asList(waitParse).subList(1, waitParse.length));
        return CommandResult.builder()
                .args(args)
                .label(label)
                .source(event.getMessage())
                .sender(event.getSender())
                .bot(event.getBot())
                .build();
    }

    public static CommandResult parse(MessageChain msgs, UserOrBot sender) {
        List<String> args = Lists.newArrayList();
        String label = "";
        String msg = msgs.contentToString();
        String[] waitParse = msg.split(" ");
        label = waitParse[0].replaceFirst("#", "");
        log.debug("转换指令 {}", label);
        args.addAll(Arrays.asList(waitParse).subList(1, waitParse.length));
        return CommandResult.builder()
                .args(args)
                .label(label)
                .source(msgs)
                .sender(sender)
                .bot(MiraiMBot.instance)
                .build();
    }

    public static void init() {
        tasks.execute(() -> new CommandConsole().start());
    }
}
