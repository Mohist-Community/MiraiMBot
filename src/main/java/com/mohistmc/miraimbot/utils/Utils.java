package com.mohistmc.miraimbot.utils;

import com.mohistmc.miraimbot.MiraiMBot;
import com.mohistmc.miraimbot.command.CommandResult;
import com.mohistmc.miraimbot.command.ConsoleSender;
import com.mohistmc.miraimbot.config.ConfigManager;
import com.mohistmc.miraimbot.console.log4j.MiraiBotLogger;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utils {

    /**
     * 给sender发送消息
     *
     * @param sender 接受者
     * @param msg    消息
     */
    public static void sendMessage(UserOrBot sender, CharSequence msg) {
        sendMessage(sender, new PlainText(msg));
    }

    /**
     * 给sender发送消息
     *
     * @param sender 接受者
     * @param msg    消息
     */
    public static void sendMessage(UserOrBot sender, Message msg) {
        sendMessage(sender, MessageUtils.newChain(msg));
    }

    /**
     * 给sender发送消息
     *
     * @param sender 接受者
     * @param msg    消息
     */
    public static void sendMessage(UserOrBot sender, MessageChain msg) {
        if (sender instanceof ConsoleSender)
            ((ConsoleSender) sender).sendMessage(msg);
        else
            ((User) sender).sendMessage(msg);
    }

    /**
     * 给result中的sender发送消息
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessage(CommandResult result, CharSequence msg) {
        sendMessage(result, new PlainText(msg));
    }

    /**
     * 给result中的sender发送消息
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessage(CommandResult result, Message msg) {
        sendMessage(result, MessageUtils.newChain(msg));
    }

    /**
     * 给result中的sender发送消息
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessage(CommandResult result, MessageChain msg) {
        sendMessage(result.getSender(), msg);
    }

    /**
     * 如果sender是群员，则将消息发送到群，否则发送给sender
     *
     * @param sender 接收者
     * @param msg    消息
     */
    public static void sendMessageOrGroup(UserOrBot sender, CharSequence msg) {
        sendMessageOrGroup(sender, new PlainText(msg));
    }

    /**
     * 如果sender是群员，则将消息发送到群，否则发送给sender
     *
     * @param sender 接收者
     * @param msg    消息
     */
    public static void sendMessageOrGroup(UserOrBot sender, Message msg) {
        sendMessageOrGroup(sender, MessageUtils.newChain(msg));
    }

    /**
     * 如果sender是群员，则将消息发送到群，否则发送给sender
     *
     * @param sender 接收者
     * @param msg    消息
     */
    public static void sendMessageOrGroup(UserOrBot sender, MessageChain msg) {
        if (isGroup(sender)) {
            ((Member) sender).getGroup().sendMessage(msg);
        } else {
            Utils.sendMessage(sender, msg);
        }
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则发送给result中的sender
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageOrGroup(CommandResult result, CharSequence msg) {
        sendMessageOrGroup(result, new PlainText(msg));
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则发送给result中的sender
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageOrGroup(CommandResult result, MessageChain msg) {
        sendMessageOrGroup(result.getSender(), msg);
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则发送给result中的sender
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageOrGroup(CommandResult result, Message msg) {
        sendMessageOrGroup(result, MessageUtils.newChain(msg));
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(Group group, CharSequence msg) {
        sendMessageToGroup(group, new PlainText(msg));
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(Group group, Message msg) {
        sendMessageToGroup(group, MessageUtils.newChain(msg));
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(Group group, MessageChain msg) {
        group.sendMessage(msg);
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToGroup(CommandResult result, CharSequence msg) {
        sendMessageToGroup(result, new PlainText(msg));
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToGroup(CommandResult result, Message msg) {
        sendMessageToGroup(result, MessageUtils.newChain(msg));
    }

    /**
     * 如果result中的sender是群员，则将消息发送到群，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToGroup(CommandResult result, MessageChain msg) {
        if (result.getSender() instanceof Member)
            sendMessageToGroup(((Member) result.getSender()).getGroup(), msg);
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(long group, CharSequence msg) {
        sendMessageToGroup(group, new PlainText(msg));
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(long group, MessageChain msg) {
        Group g = MiraiMBot.instance.getGroup(group);
        if (g == null) return;
        sendMessageToGroup(g, msg);
    }

    /**
     * 给group发送一条消息
     *
     * @param group 群
     * @param msg   消息
     */
    public static void sendMessageToGroup(long group, Message msg) {
        sendMessageToGroup(group, MessageUtils.newChain(msg));
    }

    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(Friend friend, CharSequence msg) {
        sendMessageToFriend(friend, new PlainText(msg));
    }


    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(Friend friend, Message msg) {
        sendMessageToFriend(friend, MessageUtils.newChain(msg));
    }


    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(Friend friend, MessageChain msg) {
        friend.sendMessage(msg);
    }

    /**
     * 如果result中的sender是好友，则将消息发送到好友，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToFriend(CommandResult result, CharSequence msg) {
        sendMessageToFriend(result, new PlainText(msg));
    }

    /**
     * 如果result中的sender是好友，则将消息发送到好友，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToFriend(CommandResult result, Message msg) {
        sendMessageToFriend(result, MessageUtils.newChain(msg));
    }

    /**
     * 如果result中的sender是好友，则将消息发送到好友，否则忽略
     *
     * @param result 指令结果
     * @param msg    消息
     */
    public static void sendMessageToFriend(CommandResult result, MessageChain msg) {
        if (result.getSender() instanceof Friend)
            sendMessageToFriend((Friend) result.getSender(), msg);
    }

    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(long friend, CharSequence msg) {
        sendMessageToFriend(friend, new PlainText(msg));
    }

    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(long friend, MessageChain msg) {
        Friend g = MiraiMBot.instance.getFriend(friend);
        if (g == null) return;
        sendMessageToFriend(g, msg);
    }

    /**
     * 给好友发送一条消息
     *
     * @param friend 好友
     * @param msg    消息
     */
    public static void sendMessageToFriend(long friend, Message msg) {
        sendMessageToFriend(friend, MessageUtils.newChain(msg));
    }


    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param sender 接收者
     * @param file   要发送的图片
     */
    public static void sendImage(UserOrBot sender, File file) {
        if (sender instanceof ConsoleSender)
            sendMessage(sender, "[图片]");
        else
            ExternalResource.sendAsImage(ExternalResource.create(file), (User) sender);
    }

    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param sender 接收者
     * @param url    要发送的图片
     */
    @SneakyThrows
    public static void sendImage(UserOrBot sender, URL url) {
        sendImage(sender, url.openConnection().getInputStream());

    }

    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param sender 接收者
     * @param is     要发送的图片
     */
    @SneakyThrows
    public static void sendImage(UserOrBot sender, InputStream is) {
        if (sender instanceof ConsoleSender)
            sendMessage(sender, "[图片]");
        else
            ExternalResource.sendAsImage(ExternalResource.create(is), (User) sender);
    }

    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param result 指令结果
     * @param file   要发送的图片
     */
    public static void sendImage(CommandResult result, File file) {
        sendImage(result.getSender(), file);
    }

    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param result 指令结果
     * @param url    要发送的图片
     */
    public static void sendImage(CommandResult result, URL url) {
        sendImage(result.getSender(), url);
    }

    /**
     * 给好友/群员私聊发送一张图片
     *
     * @param result 指令结果
     * @param is     要发送的图片
     */
    public static void sendImage(CommandResult result, InputStream is) {
        sendImage(result.getSender(), is);
    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param file  要发送的图片
     */
    public static void sendImageToGroup(Group group, File file) {
        ExternalResource.sendAsImage(ExternalResource.create(file), group);
    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param url   要发送的图片
     */
    @SneakyThrows
    public static void sendImageToGroup(Group group, URL url) {
        sendImageToGroup(group, url.openConnection().getInputStream());

    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param is    要发送的图片
     */
    @SneakyThrows
    public static void sendImageToGroup(Group group, InputStream is) {
        ExternalResource.sendAsImage(ExternalResource.create(is), group);
    }

    /**
     * 给群发送一张图片
     *
     * @param result 指令结果
     * @param file   要发送的图片
     */
    public static void sendImageToGroup(CommandResult result, File file) {
        if (result.getSender() instanceof Member)
            sendImageToGroup(((Member) result.getSender()).getGroup(), file);
    }

    /**
     * 给群发送一张图片
     *
     * @param result 指令结果
     * @param url    要发送的图片
     */
    public static void sendImageToGroup(CommandResult result, URL url) {
        if (result.getSender() instanceof Member)
            sendImageToGroup(((Member) result.getSender()).getGroup(), url);
    }

    /**
     * 给群发送一张图片
     *
     * @param result 指令结果
     * @param is     要发送的图片
     */
    public static void sendImageToGroup(CommandResult result, InputStream is) {
        if (result.getSender() instanceof Member)
            sendImageToGroup(((Member) result.getSender()).getGroup(), is);
    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param file  要发送的图片
     */
    public static void sendImageToGroup(long group, File file) {
        Group g = MiraiMBot.instance.getGroup(group);
        if (g == null) return;
        sendImageToGroup(g, file);
    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param url   要发送的图片
     */
    public static void sendImageToGroup(long group, URL url) {
        Group g = MiraiMBot.instance.getGroup(group);
        if (g == null) return;
        sendImageToGroup(g, url);
    }

    /**
     * 给群发送一张图片
     *
     * @param group 群
     * @param is    要发送的图片
     */
    public static void sendImageToGroup(long group, InputStream is) {
        Group g = MiraiMBot.instance.getGroup(group);
        if (g == null) return;
        sendImageToGroup(g, is);
    }

    /**
     * 如果sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param sender 接收者
     * @param file   要发送的图片
     */
    @SneakyThrows
    public static void sendImageOrGroup(UserOrBot sender, File file) {
        if (sender instanceof ConsoleSender)
            sendMessage(sender, "[图片]");
        else if (sender instanceof Member)
            ExternalResource.sendAsImage(ExternalResource.create(file), ((Member) sender).getGroup());
        else if (sender instanceof User)
            sendImage(sender, file);
    }

    /**
     * 如果sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param sender 接收者
     * @param url    要发送的图片
     */
    @SneakyThrows
    public static void sendImageOrGroup(UserOrBot sender, URL url) {
        sendImageOrGroup(sender, url.openConnection().getInputStream());
    }

    /**
     * 如果sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param sender 接收者
     * @param is     要发送的图片
     */
    @SneakyThrows
    public static void sendImageOrGroup(UserOrBot sender, InputStream is) {
        if (sender instanceof ConsoleSender)
            sendMessage(sender, "[图片]");
        else if (sender instanceof Member)
            ExternalResource.sendAsImage(ExternalResource.create(is), ((Member) sender).getGroup());
        else if (sender instanceof User)
            sendImage(sender, is);
    }


    /**
     * 如果result中的sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param result 指令结果
     * @param file   要发送的图片
     */
    public static void sendImageOrGroup(CommandResult result, File file) {
        sendImageOrGroup(result.getSender(), file);
    }

    /**
     * 如果result中的sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param result 指令结果
     * @param url    要发送的图片
     */
    public static void sendImageOrGroup(CommandResult result, URL url) {
        sendImageOrGroup(result.getSender(), url);
    }

    /**
     * 如果result中的sender是群成员，则给群发送一张图片反之私聊发送
     *
     * @param result 指令结果
     * @param is     要发送的图片
     */
    public static void sendImageOrGroup(CommandResult result, InputStream is) {
        sendImageOrGroup(result.getSender(), is);
    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param file   要发送的图片
     */
    public static void sendImageToFriend(Friend friend, File file) {
        ExternalResource.sendAsImage(ExternalResource.create(file), friend);
    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param url    要发送的图片
     */
    @SneakyThrows
    public static void sendImageToFriend(Friend friend, URL url) {
        sendImageToFriend(friend, url.openConnection().getInputStream());

    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param is     要发送的图片
     */
    @SneakyThrows
    public static void sendImageToFriend(Friend friend, InputStream is) {
        ExternalResource.sendAsImage(ExternalResource.create(is), friend);
    }

    /**
     * 给好友发送一张图片
     *
     * @param result 指令结果
     * @param file   要发送的图片
     */
    public static void sendImageToFriend(CommandResult result, File file) {
        if (result.getSender() instanceof Friend)
            sendImageToFriend((Friend) result.getSender(), file);
    }

    /**
     * 给好友发送一张图片
     *
     * @param result 指令结果
     * @param url    要发送的图片
     */
    public static void sendImageToFriend(CommandResult result, URL url) {
        if (result.getSender() instanceof Friend)
            sendImageToFriend((Friend) result.getSender(), url);
    }

    /**
     * 给好友发送一张图片
     *
     * @param result 指令结果
     * @param is     要发送的图片
     */
    public static void sendImageToFriend(CommandResult result, InputStream is) {
        if (result.getSender() instanceof Friend)
            sendImageToFriend((Friend) result.getSender(), is);
    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param file   要发送的图片
     */
    public static void sendImageToFriend(long friend, File file) {
        Friend g = MiraiMBot.instance.getFriend(friend);
        if (g == null) return;
        sendImageToFriend(g, file);
    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param url    要发送的图片
     */
    public static void sendImageToFriend(long friend, URL url) {
        Friend g = MiraiMBot.instance.getFriend(friend);
        if (g == null) return;
        sendImageToFriend(g, url);
    }

    /**
     * 给好友发送一张图片
     *
     * @param friend 好友
     * @param is     要发送的图片
     */
    public static void sendImageToFriend(long friend, InputStream is) {
        Friend g = MiraiMBot.instance.getFriend(friend);
        if (g == null) return;
        sendImageToFriend(g, is);
    }


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

    /**
     * 判断sender是否在群里
     *
     * @param sender sender
     * @return sender是否在群里
     */
    public static boolean isGroup(UserOrBot sender) {
        return (sender instanceof Member);
    }

    /**
     * 判断sender是否为正常群员
     *
     * @param sender sender
     * @return sender是否为正常群员
     */
    public static boolean isNormalMember(UserOrBot sender) {
        return (sender instanceof NormalMember);
    }

    /**
     * 判断sender是否为匿名群员
     *
     * @param sender sender
     * @return sender是否为匿名群员
     */
    public static boolean isAnonymousMember(UserOrBot sender) {
        return (sender instanceof AnonymousMember);
    }

    /**
     * 判断sender是否为好友
     *
     * @param sender sender
     * @return sender是否为好友
     */
    public static boolean isFriend(UserOrBot sender) {
        return !isGroup(sender) && !isConsole(sender);
    }

    /**
     * 判断sender是否为控制台
     *
     * @param sender sender
     * @return sender是否为控制台
     */
    public static boolean isConsole(UserOrBot sender) {
        return (sender instanceof ConsoleSender);
    }
}
