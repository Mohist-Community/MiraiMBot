package com.mohistmc.miraimbot.command;

import com.mohistmc.miraimbot.MiraiMBot;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.event.events.EventCancelledException;
import net.mamoe.mirai.message.action.UserNudge;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ConsoleSender implements UserOrBot {
    private static final Logger log = LogManager.getLogger("Console");
    public static final ConsoleSender INSTANCE = new ConsoleSender();

    @Override
    public long getId() {
        return 666666;
    }


    @NotNull
    @Override
    public Bot getBot() {
        return MiraiMBot.instance;
    }

    @NotNull
    @Override
    public CoroutineContext getCoroutineContext() {
        return MiraiMBot.instance.getCoroutineContext();
    }


    public void sendMessage(@NotNull String message) throws EventCancelledException, IllegalStateException {
        sendMessage(MessageUtils.newChain(new PlainText(message)));
    }


    @NotNull
    @Override
    public String getAvatarUrl() {
        return "";
    }


    @NotNull
    @Override
    public UserNudge nudge() {
        return null;
    }

    @NotNull
    public String getNick() {
        return "Console";
    }

    @NotNull
    public String getRemark() {
        return "Console";
    }


    public void sendMessage(@NotNull Message message) throws EventCancelledException, IllegalStateException {
        log.info(message.contentToString());
        // return super.sendMessage(message);
    }


}
