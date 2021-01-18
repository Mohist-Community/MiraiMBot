package com.mohistmc.miraimbot.plugin;

import com.mohistmc.miraimbot.config.ConfigManager;
import com.mohistmc.miraimbot.utils.Utils;
import com.mohistmc.yaml.file.YamlConfiguration;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.jar.JarFile;

public class MohistPlugin extends Utils {

    @Getter
    public String name;
    @Getter
    public String version;
    @Getter
    public String description;
    @Getter
    public ClassLoader classLoader;
    @Getter
    public JarFile sourceFile;
    @Getter
    public File dataDir;
    public YamlConfiguration config;
    @Getter
    public List<String> authors;

    public void onLoad() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Logger getLogger() {
        return LogManager.getLogger(getName());
    }

    public YamlConfiguration getConfig() {
        if (config == null) config = ConfigManager.getConfig(this);
        return config;
    }

    public void saveDefaultConfig() {
        ConfigManager.saveDefault(this);
    }

    public void saveConfig() {
        if (config == null) getConfig();
        ConfigManager.save(this);
    }
}
