package com.mohistmc.miraimbot.console.log4j;

import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MiraiBotLogger implements MiraiLogger {
    private static final Logger log = LogManager.getLogger("BotMessage");
    private MiraiLogger logger;

    @Nullable
    @Override
    public MiraiLogger getFollower() {
        return logger;
    }

    @Override
    public void setFollower(@Nullable MiraiLogger miraiLogger) {
        this.logger = miraiLogger;
    }

    @Nullable
    @Override
    public String getIdentity() {
        return "BotMessage";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void debug(@Nullable String s) {
        log.debug(s);
    }

    @Override
    public void debug(@Nullable String s, @Nullable Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public void error(@Nullable String s) {
        log.error(s);
    }

    @Override
    public void error(@Nullable String s, @Nullable Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    public void info(@Nullable String s) {
        log.info(s);
    }

    @Override
    public void info(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s, throwable);
    }

    @NotNull
    @Override
    public <T extends MiraiLogger> T plus(@NotNull T t) {
        return t;
    }

    @Override
    public void verbose(@Nullable String s) {
        log.info(s);
    }

    @Override
    public void verbose(@Nullable String s, @Nullable Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public void warning(@Nullable String s) {
        log.warn(s);
    }

    @Override
    public void warning(@Nullable String s, @Nullable Throwable throwable) {
        log.warn(s,throwable);
    }
}
