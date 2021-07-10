package com.mohistmc.miraimbot.config;


import com.mohistmc.miraimbot.plugin.MohistPlugin;
import com.mohistmc.yaml.file.YamlConfiguration;
import com.mohistmc.yaml.util.Charsets;
import lombok.Getter;
import lombok.SneakyThrows;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ConfigManager {

    private static final File configFile = new File("config.yml");
    private static final Logger log = LogManager.getLogger("ConfigManager");
    @Getter
    public static YamlConfiguration config;
    public static final String path_login_account = "login.account";// 登入账户
    public static final String path_login_password = "login.password";// 登入密码
    public static final String path_command_prefix = "command.prefix";// 指令前缀
    public static final String path_command_enable = "command.enable";// 指令开关
    public static final String path_command_unknown = "command.unknown";// 未知指令消息
    public static final String path_permission_enable = "permission.enable";// 权限开关
    public static final String path_log_network = "log.network";// 网络日志开关
    public static final String path_log_bot = "log.bot";// mirai消息开关
    public static final String path_protocol = "protocol";// mirai协议

    /**
     * 初始化配置文件
     */
    @SneakyThrows
    public static void init() {
        if (!configFile.exists()) {
            saveDefault();
        }
        load();
    }

    /**
     * 加载配置文件
     */
    @SneakyThrows
    public static void load() {
        if (config == null) config = YamlConfiguration.loadConfiguration(configFile);
        config.loadFromString(parseNotes2Key(configFile));
    }

    /**
     * 保存配置文件
     */
    @SneakyThrows
    public static void save() {
        config.save(configFile);
        parseKey2Notes(configFile);
    }

    /**
     * 保存默认配置文件
     */
    @SneakyThrows
    public static void saveDefault() {
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        load();
        config.set(createNote("登录设置"), "");
        config.set("login." + createNote("要登录的账号"), "");
        config.set(path_login_account, 0);
        config.set("login." + createNote("要登录账号的密码"), "");
        config.set(path_login_password, "");
        config.set(createNote("指令设置"), "");
        config.set("command." + createNote("是否开启默认指令系统(true为开false为关)"), "");
        config.set(path_command_enable, false);
        config.set("command." + createNote("指令前缀"), "");
        config.set(path_command_prefix, "#");
        config.set("command." + createNote("未知指令消息"), "");
        config.set(path_command_unknown, "未知指令。请使用\"#help\"来获得帮助");
        config.set(createNote("权限设置"), "");
        config.set("permission." + createNote("是否开启默认权限系统(true为开false为关)"), "");
        config.set(path_permission_enable, false);
        config.set("log." + createNote("网络日志开关(true为开false为关)"), "");
        config.set(path_log_network, true);
        config.set("log." + createNote("mirai日志开关(true为开false为关)"), "");
        config.set(path_log_bot, true);
        config.set(createNote("mirai协议(手机为ANDROID_PHONE 平板为ANDROID_PAD 手表为ANDROID_WATCH)"), "");
        config.set(path_protocol, BotConfiguration.MiraiProtocol.ANDROID_PHONE.toString());
        save();
    }

    /**
     * 将注释转换为键
     *
     * @param file 配置文件
     */
    @SneakyThrows
    public static String parseNotes2Key(File file) {
        List<String> lines = FileUtils.readLines(file, Charsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String s = line.replaceAll(" ", "");
            if (s.startsWith("#")) {
                sb.append(line.replaceFirst("# ", "¤") + ": ''");
            } else sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 将特定键还原为注释
     *
     * @param file 配置文件
     */
    @SneakyThrows
    public static void parseKey2Notes(File file) {
        List<String> lines = FileUtils.readLines(file, Charsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String s = line.replaceAll(" ", "");
            if (s.startsWith("¤")) {
                sb.append(line.split(":")[0].replaceFirst("¤", "# "));
            } else if (s.startsWith("'¤")) {
                sb.append(line.split(":")[0].replaceFirst("'¤", "# "));
            } else sb.append(line);
            sb.append("\n");
        }
        FileUtils.writeStringToFile(file, sb.toString(), Charsets.UTF_8);
    }

    /**
     * 创建注释
     *
     * @param note 注释
     * @return ¤ + 注释
     */
    public static String createNote(CharSequence note) {
        return "¤" + note;
    }

    @SneakyThrows
    public static YamlConfiguration getConfig(MohistPlugin plugin) {
        File dir = plugin.getDataDir();
        if (!dir.exists()) dir.mkdirs();
        File configF = new File(dir, "config.yml");
        if (!configF.exists()) saveDefault(plugin);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configF);
        config.loadFromString(parseNotes2Key(configF));
        return config;
    }

    @SneakyThrows
    public static void saveDefault(MohistPlugin plugin) {
        File dir = plugin.getDataDir();
        if (!dir.exists()) dir.mkdirs();
        File configF = new File(dir, "config.yml");
        JarFile jarFile = plugin.getSourceFile();
        ZipEntry ze = jarFile.getEntry("config.yml");
        InputStream is = ze != null ? jarFile.getInputStream(ze) : null;
        if (is != null) {
            if (!configF.exists()) configF.createNewFile();
            IOUtils.copyLarge(is, new FileOutputStream(configF));
            is.close();
        } else {
            log.error("插件 {} 文件内无 config.yml", plugin.getName());
        }
    }

    @SneakyThrows
    public static void save(MohistPlugin plugin) {
        File dir = plugin.getDataDir();
        File configF = new File(dir, "config.yml");
        if (!configF.exists()) saveDefault(plugin);
        plugin.getConfig().save(configF);
        parseKey2Notes(configF);
    }
}
