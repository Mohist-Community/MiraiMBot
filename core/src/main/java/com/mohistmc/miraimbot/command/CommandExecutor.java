package com.mohistmc.miraimbot.command;

import com.mohistmc.miraimbot.annotations.Command;
import lombok.Data;


@Data
public abstract class CommandExecutor {

    public String label;
    public String usage = "";
    public String description = "";
    public boolean noshow = false;
    public boolean opCan = true;
    public boolean onlyOp = false;
    public boolean permissionEnable = false;
    public String permission = "";
    public Command.Type type = Command.Type.ALL;

    public abstract boolean onCommand(CommandResult result);
}
