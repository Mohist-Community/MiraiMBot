package com.mohistmc.miraimbot.utils;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mohistmc.miraimbot.command.CommandResult;
import com.mohistmc.miraimbot.command.ConsoleSender;
import com.mohistmc.miraimbot.config.ConfigManager;
import com.mohistmc.miraimbot.console.log4j.MiraiBotLogger;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static BotConfiguration defaultConfig() {
        BotConfiguration botConfiguration = new BotConfiguration() {
            {
                fileBasedDeviceInfo("device.json");
                setBotLoggerSupplier(bot -> new MiraiBotLogger());
                setNetworkLoggerSupplier(bot -> new MiraiBotLogger());
            }
        };
        if (!ConfigManager.getConfig().getBoolean(ConfigManager.path_log_network, true))
            botConfiguration.noNetworkLog();
        if (!ConfigManager.getConfig().getBoolean(ConfigManager.path_log_bot, true))
            botConfiguration.noBotLog();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(ConfigManager.getConfig().getString(ConfigManager.path_protocol, "ANDROID_PHONE")));
        return botConfiguration;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?[0-9]*");
    }

    private static final List<ExecutorService> tasks = Lists.newArrayList();

    public static ExecutorService createThreadPool(int core, int max, String name) {
        ExecutorService s = new ThreadPoolExecutor(core, max,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(512), new ThreadFactoryBuilder().setNameFormat(name).build(), new ThreadPoolExecutor.AbortPolicy());
        tasks.add(s);
        return s;
    }

    public static void stopAllThread() {
        tasks.forEach(ExecutorService::shutdownNow);
    }


    public static void sendMessage(UserOrBot sender, String s) {
        if (sender instanceof ConsoleSender)
            ((ConsoleSender) sender).sendMessage(s);
        else
            ((User) sender).sendMessage(s);
    }

    public static void sendMessage(UserOrBot sender, MessageChain s) {
        if (sender instanceof ConsoleSender)
            ((ConsoleSender) sender).sendMessage(s);
        else
            ((User) sender).sendMessage(s);
    }

    public static void sendMessageOrGroup(CommandResult result, String msg) {
        if (result.isGroup()) {
            result.getGroupOrNull().sendMessage(msg);
        } else {
            Utils.sendMessage(result.getSender(), msg);
        }
    }

    public static void sendMessageOrGroup(CommandResult result, MessageChain msg) {
        if (result.isGroup()) {
            result.getGroupOrNull().sendMessage(msg);
        } else {
            Utils.sendMessage(result.getSender(), msg);
        }
    }

    public static void sendMessageOrGroup(UserOrBot sender, String msg) {
        if (isGroup(sender)) {
            ((Member) sender).getGroup().sendMessage(msg);
        } else {
            Utils.sendMessage(sender, msg);
        }
    }

    public static void sendMessageOrGroup(UserOrBot sender, MessageChain msg) {
        if (isGroup(sender)) {
            ((Member) sender).getGroup().sendMessage(msg);
        } else {
            Utils.sendMessage(sender, msg);
        }
    }

    public static boolean isGroup(UserOrBot sender) {
        return (sender instanceof Member);
    }
}
