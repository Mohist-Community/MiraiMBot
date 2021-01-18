package com.mohistmc.miraimbot.utils;

import com.mohistmc.miraimbot.cmds.manager.CommandResult;
import com.mohistmc.miraimbot.cmds.manager.ConsoleSender;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.BotConfiguration;

public class Utils {
    public static void sendMessage(UserOrBot sender, MessageChain messages) {
        if (sender instanceof Member) {
            ((Member) sender).getGroup().sendMessage(messages);
        } else if (sender instanceof ConsoleSender) {
            ((ConsoleSender) sender).sendMessage(messages);
        } else if (sender instanceof Friend) {
            ((Friend) sender).sendMessage(messages);
        }
    }

    public static void sendMessage(UserOrBot sender, String message) {
        sendMessage(sender, MessageUtils.newChain(new PlainText(message)));
    }

    public static BotConfiguration defaultConfig() {
        BotConfiguration botConfiguration = new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
            }
        };
       // botConfiguration.noNetworkLog();
        //botConfiguration.noBotLog();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        return botConfiguration;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?[0-9]*");
    }

    public static long getId(String arg1, CommandResult result) {
        long id = 0L;
        if (arg1.startsWith("@")) {
            if (result.isGroup()) {
                Message msg = result.getSource().get(At.Key);
                if (msg != null) {
                    id = ((At) msg).getTarget();
                }

            }
        } else {
            if (Utils.isNumeric(arg1)) {
                id = Long.parseLong(arg1);
            }
        }
        return id;
    }
}
