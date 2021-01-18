package com.mohistmc.miraimbot.command.executors;

import com.mohistmc.miraimbot.command.CommandExecutor;
import com.google.common.collect.Lists;
import com.mohistmc.miraimbot.command.CommandResult;
import com.mohistmc.miraimbot.permission.Permission;
import com.mohistmc.miraimbot.utils.Utils;
import net.mamoe.mirai.message.data.*;

import java.util.List;

public class AddOpCommand extends CommandExecutor {

    public AddOpCommand() {
        super.label = "addop";
        super.usage = "addop";
        super.description = "";
        super.noshow = true;
        super.opCan = true;
        super.onlyOp = true;
        super.permissionEnable = false;
        super.permission = "";
    }

    @Override
    public boolean onCommand(CommandResult result) {
        if (result.getArgs().size() < 1) {
            Utils.sendMessageOrGroup(result, "用法: #addop xxx xxx...或 #addop @xxx @xxx...");
            return true;
        }
        List<Long> ops = Lists.newArrayList();
        for (String op : result.getArgs()) {
            if (op.startsWith("@")) {
                for (Message msg : result.getSource()) {
                    if (msg instanceof At) {
                        ops.add(((At) msg).getTarget());
                        Permission.addOp(((At) msg).getTarget());
                    }
                }
                break;
            }
            if (Utils.isNumeric(op)) {
                ops.add(Long.parseLong(op));
                Permission.addOp(Long.parseLong(op));
            }
        }
        MessageChain m = MessageUtils.newChain(new PlainText("已添加 "));
        for (long o : ops)
            m.plus(new At(o));
        Utils.sendMessageOrGroup(result, m);
        return true;
    }
}
