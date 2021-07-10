package com.mohistmc.miraimbot.listeners;

import com.mohistmc.miraimbot.command.CommandManager;
import com.mohistmc.miraimbot.config.ConfigManager;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainListener extends SimpleListenerHost {
    public static final MainListener INSTANCE = new MainListener();
    private static final Logger log = LogManager.getLogger("MainListener");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessage(MessageEvent event) {
        String prefix = ConfigManager.getConfig().getString(ConfigManager.path_command_prefix);
        if (event.getMessage().contentToString().startsWith(prefix)) {
            log.debug(event.getMessage().contentToString());
            CommandManager.parseAndCall(event);
            event.intercept();
        }
    }
}
