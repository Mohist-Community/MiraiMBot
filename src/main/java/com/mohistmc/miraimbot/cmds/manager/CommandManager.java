package com.mohistmc.miraimbot.cmds.manager;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mohistmc.miraimbot.cmds.manager.annotations.Command;
import com.mohistmc.miraimbot.console.log4j.MiraiMBotLog;
import com.mohistmc.miraimbot.plugin.PluginClassLoader;
import com.mohistmc.miraimbot.utils.JarUtils;
import com.mohistmc.miraimbot.utils.LogUtil;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.MessageChain;

public class CommandManager {

    private static ExecutorService cmds = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder().setNameFormat("CommandTask-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    public static Map<String, CommandExecutor> executors = Maps.newHashMap();
    public static Map<String, String> usages = Maps.newHashMap();

    @SneakyThrows
    public static void init() {
        long start = System.currentTimeMillis();
        int load = 0;
        Set<Class<?>> classes = JarUtils.getAllLoadClasses();
        for (Class<?> loadClass : classes) {
            if (Arrays.asList(loadClass.getInterfaces()).contains(CommandExecutor.class)) {
                register((CommandExecutor) loadClass.newInstance());
                load++;
            }
        }
        MiraiMBotLog.LOGGER.info("加载了 " + load + " 个指令，耗时 " + (System.currentTimeMillis() - start) + "(ms).");
    }

    public static void register(CommandExecutor newInstance) {
        Class<?> clazz = newInstance.getClass();
        if (clazz.getAnnotation(Command.class) != null) {
            Command c = clazz.getAnnotation(Command.class);
            if (!executors.containsKey(c.name())) {
                executors.put(c.name(), newInstance);// 注册主要指令
                MiraiMBotLog.LOGGER.info("注册指令 " + clazz.getName() + "(" + c.name() + ")");
                if (!usages.containsKey(c.name())) usages.put(c.name(), c.usage());
                for (String alia : c.alias()) {
                    if (!executors.containsKey(alia)) executors.put(alia, newInstance);
                    MiraiMBotLog.LOGGER.info("注册别称 " + clazz.getName() + "(" + alia + ")");
                    if (!usages.containsKey(alia)) usages.put(alia, c.usage());
                }
            }
        } else {
            MiraiMBotLog.LOGGER.info("指令解析器 " + clazz.getName() + " 无效(无@Command注解)");
        }
    }

    public static void registers(CommandExecutor... cmds) {
        for (CommandExecutor cmd : cmds) {
            register(cmd);
        }
    }

    public static void registers(String pack) {
        try {
            long start = System.currentTimeMillis();
            int load = 0;
            Enumeration<URL> c = PluginClassLoader.INSTANCE.getResources(pack.replace(".", "/"));
            while (c.hasMoreElements()) {
                URL u = c.nextElement();
                MiraiMBotLog.LOGGER.info(u.getPath());
                Class<?> clazz = PluginClassLoader.INSTANCE.loadClass(u.getPath().replace("\\", "."));
                if (Arrays.asList(clazz.getInterfaces()).contains(CommandExecutor.class)) {
                    register((CommandExecutor) clazz.newInstance());
                    load++;
                }
            }
            MiraiMBotLog.LOGGER.info("加载了 " + load + " 个指令，耗时 " + (System.currentTimeMillis() - start) + "(ms).");
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            MiraiMBotLog.LOGGER.info("[ERROR] 自动注册指令失败，请手动注册");
            e.printStackTrace();
        }
    }

    static long last = System.currentTimeMillis();

    public static void call(MessageChain messages, User sender) {
        if (System.currentTimeMillis() - last < 3000) {
            sender.sendMessage("指令发送太快了哦");
        } else {
            //cmds.execute(() -> callA(messages, sender));
            callA(messages, sender);
        }
        last = System.currentTimeMillis();
    }

    @SneakyThrows
    private static void callA(MessageChain messages, User sender) {
        String msg = messages.contentToString().replaceFirst(LogUtil.command, "");// 获得带有mirai码的字符串，让用户自己解析。
        while (true) {
            if (!msg.contains("  ")) {
                break;
            }
            msg.replace("  ", " ");// 吧两个空格替换为一个
        }
        String[] sourceCmd = msg.split(" ");// 通过空格来切割
        if (sourceCmd.length > 0) {// 保证不为空
            String label = sourceCmd[0];
            //LogUtil.getLogger().info("触发指令: " + label);
            //LogUtil.getLogger().info("是否包含: " + executors.containsKey(label));
            if (executors.containsKey(label)) {
                String[] args = new String[sourceCmd.length - 1];
                System.arraycopy(sourceCmd, 1, args, 0, sourceCmd.length - 1);
                CommandExecutor executor = executors.get(label);
                CommandResult cr = new CommandResult();
                cr.setArgs(Arrays.asList(args));
                cr.setSender(sender);
                cr.setSource(messages);
                if (!executor.onCommand(cr)) {
                    if (sender instanceof Member) {
                        ((Member) sender).getGroup().sendMessage("指令执行失败。用法：" + usages.get(executor));
                    } else {
                        sender.sendMessage("指令执行失败。用法：" + usages.get(executor));
                    }
                }
            } else {
                if (sender instanceof Member) {
                    ((Member) sender).getGroup().sendMessage("未知的指令.请使用 " + LogUtil.command + "cmdlist 来获得指令列表");
                } else {
                    sender.sendMessage("未知的指令.请使用 " + LogUtil.command + "cmdlist 来获得指令列表");
                }
            }
        }
    }
}