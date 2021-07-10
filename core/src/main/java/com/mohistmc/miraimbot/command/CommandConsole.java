package com.mohistmc.miraimbot.command;

import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandConsole extends SimpleTerminalConsole {
    private static final Logger log = LogManager.getLogger("CommandConsole");

    @Override
    public void start() {
        CommandManager.console = true;
        super.start();
        log.info("控制台启动完毕");
    }

    @Override
    protected boolean isRunning() {
        return CommandManager.console;
    }

    @Override
    protected void runCommand(String command) {
        CommandManager.parseAndCall(MessageUtils.newChain(new PlainText(command)), ConsoleSender.INSTANCE);
    }

    @Override
    protected void shutdown() {
        System.exit(0);
    }
}
