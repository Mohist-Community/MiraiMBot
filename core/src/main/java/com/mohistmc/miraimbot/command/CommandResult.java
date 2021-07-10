package com.mohistmc.miraimbot.command;

import com.mohistmc.miraimbot.utils.Utils;
import lombok.Builder;
import lombok.Data;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

import java.util.List;

@Data
@Builder
public class CommandResult {
    public UserOrBot sender;
    public Bot bot;
    public List<String> args;
    public MessageChain source;
    public CharSequence label;


    /**
     * 判断当前指令是否来自群组
     *
     * @return
     */
    public boolean isGroup() {
        return Utils.isGroup(sender);
    }

    public void sendMessage(CharSequence msg) {
        sendMessage(new PlainText(msg));
    }

    public void sendMessage(Message msg) {
        sendMessage(MessageUtils.newChain(msg));
    }

    public void sendMessage(MessageChain msg) {
        Utils.sendMessage(this.getSender(), msg);
    }

    public void sendMessageOrGroup(CharSequence msg) {
        sendMessageOrGroup(new PlainText(msg));
    }

    public void sendMessageOrGroup(Message msg) {
        sendMessageOrGroup(MessageUtils.newChain(msg));
    }

    public void sendMessageOrGroup(MessageChain msg) {
        Utils.sendMessageOrGroup(this.getSender(), msg);
    }

    public Group getGroupOrNull() {
        return isGroup() ? ((Member) sender).getGroup() : null;
    }

}
