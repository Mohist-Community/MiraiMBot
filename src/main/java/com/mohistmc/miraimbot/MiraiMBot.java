package com.mohistmc.miraimbot;

import com.mohistmc.miraimbot.config.ConfigManager;
import com.google.common.base.Strings;
import com.mohistmc.miraimbot.command.CommandManager;
import com.mohistmc.miraimbot.command.executors.AddOpCommand;
import com.mohistmc.miraimbot.command.executors.HelpCommand;
import com.mohistmc.miraimbot.listeners.ListenerManager;
import com.mohistmc.miraimbot.listeners.MainListener;
import com.mohistmc.miraimbot.permission.Permission;
import com.mohistmc.miraimbot.plugin.PluginManager;
import com.mohistmc.miraimbot.utils.Utils;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiraiMBot {
    @Getter
    public static final boolean debug = true;
    private static final Logger log = LogManager.getLogger("MiraiMBot");
    public static Bot instance;
    public static boolean command_enable;
    public static boolean permission_enable;


    public static void main(String[] args) {
        System.setProperty("terminal.ansi", "true");
        log.info("初始化...");
        ConfigManager.init();
        long account = ConfigManager.getConfig().getLong(ConfigManager.path_login_account, 0L);
        String password = ConfigManager.getConfig().getString(ConfigManager.path_login_password, "");
        MiraiMBot.command_enable = ConfigManager.getConfig().getBoolean(ConfigManager.path_command_enable, false);
        MiraiMBot.permission_enable = ConfigManager.getConfig().getBoolean(ConfigManager.path_permission_enable, false);
        if (account == 0L || Strings.isNullOrEmpty(password)) {
            if (command_enable) {
                log.warn("您没有设置账户和密码。您可以通过\"login [账户] [密码]\"尝试登入。");
            } else {
                log.error("您没有设置账户和密码。请在配置文件设置后重新启动。");
                System.exit(0);
            }
        } else {
            instance = BotFactory.INSTANCE.newBot(account, password, Utils.defaultConfig());
            log.info("尝试登入...");
            instance.login();
            if (command_enable) {
                log.info("初始化事件系统...");
                ListenerManager.register(MainListener.INSTANCE);
                log.info("初始化指令系统...");
                CommandManager.init();
                CommandManager.register(new HelpCommand(), "?", "菜单", "帮助");
                CommandManager.register(new AddOpCommand());
            } else {
                log.warn("您关闭了默认的指令系统，将不会注册插件指令。");
            }
            if (permission_enable) {
                log.info("初始化权限系统...");
                Permission.init();
            }
            log.info("初始化插件系统...");
            PluginManager.init();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Utils.stopAllThread();
            }));
            instance.join();
        }
    }
}
