package com.mohistmc.miraimbot.command.executors;

import com.mohistmc.miraimbot.MiraiMBot;
import com.mohistmc.miraimbot.command.CommandExecutor;
import com.mohistmc.miraimbot.command.CommandResult;

public class VersionCommand extends CommandExecutor {

    public VersionCommand() {
        super.label = "version";
        super.usage = "version";
        super.description = "";
        super.noshow = false;
        super.opCan = false;
        super.onlyOp = false;
        super.permissionEnable = false;
        super.permission = "";
    }

    @Override
    public boolean onCommand(CommandResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("====[MiraiMBot]====\n");
        sb.append("version: " + MiraiMBot.getVersion() + "\n");
        sb.append("mirai-core: 2.7-M1\n");
        sb.append("java: " + System.getProperty("java.version"));
        result.sendMessageOrGroup(sb.toString());
        return true;
    }
}
