package com.mohistmc.miraimbot.command.executors;

import com.mohistmc.miraimbot.command.CommandExecutor;
import com.mohistmc.miraimbot.command.CommandManager;
import com.mohistmc.miraimbot.command.CommandResult;
import com.mohistmc.miraimbot.permission.Permission;
import com.mohistmc.miraimbot.utils.Utils;

import java.util.concurrent.ConcurrentMap;

public class HelpCommand extends CommandExecutor {

    public HelpCommand() {
        super.label = "help";
        super.usage = "help";
        super.description = "";
        super.noshow = false;
        super.opCan = false;
        super.onlyOp = false;
        super.permissionEnable = false;
        super.permission = "";
    }

    @Override
    public boolean onCommand(CommandResult result) {
        ConcurrentMap<String, CommandExecutor> executors = CommandManager.getExecutors();
        StringBuilder sb = new StringBuilder();
        sb.append("==[帮助列表]==\n");
        executors.keySet().forEach(e -> {
            CommandExecutor executor = executors.get(e);
            if (!executor.noshow && (!executor.opCan || Permission.isOp(result.getSender()))) {
                sb.append(e)
                        .append(" 简介: ")
                        .append(executor.getDescription())
                        .append(" 用法: ")
                        .append(executor.getUsage())
                        .append("\n");
            }
        });
        Utils.sendMessageOrGroup(result, sb.toString());
        return true;
    }
}
