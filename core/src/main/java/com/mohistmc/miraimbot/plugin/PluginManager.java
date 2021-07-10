package com.mohistmc.miraimbot.plugin;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mohistmc.miraimbot.MiraiMBot;
import com.mohistmc.miraimbot.command.CommandManager;
import com.mohistmc.miraimbot.listeners.ListenerManager;
import com.mohistmc.miraimbot.utils.Utils;
import com.mohistmc.yaml.util.Charsets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginManager {

    public static final File pluginDir = new File("./plugins/");
    private static final Logger log = LogManager.getLogger("PluginManager");
    private static final ExecutorService tasks = Utils.createThreadPool(1, 100, "Plugins-%d");
    @Getter
    public static final List<MohistPlugin> plugins = Lists.newArrayList();
    private static boolean inited = false;

    public static void init() {
        if (!pluginDir.exists()) pluginDir.mkdirs();
        findAndLoadAll();
        enableAll();
    }

    public static void enableAll() {
        if (inited) return;
        plugins.forEach(plugin -> tasks.execute(() -> {
            plugin.onEnable();
            if (MiraiMBot.command_enable) CommandManager.register(plugin);
            ListenerManager.register(plugin);
        }));
        inited = true;

    }

    public static void disableAll() {
        plugins.forEach(MohistPlugin::onDisable);

    }

    @SneakyThrows
    private static void findAndLoadAll() {
        for (File pluginFile : pluginDir.listFiles()) {
            if (pluginFile.isFile() && (pluginFile.getName().endsWith(".jar") || pluginFile.getName().endsWith(".zip"))) {
                try {
                    plugins.add(PluginLoader.INSTANCE.loadPlugin(pluginFile));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SneakyThrows
    public static MohistPlugin createPluginInstance(JarFile jarFile) {
        MohistPlugin plugin = null;
        ZipEntry ze = jarFile.getEntry("plugin.json");
        InputStream is = ze != null ? jarFile.getInputStream(ze) : null;
        if (is != null) {
            log.debug("从插件 {} 读取plugins.json", jarFile.getName());
            JSONObject json = JSONObject.parseObject(IOUtils.toString(is, Charsets.UTF_8));
            String main = json.getString("main");
            List<String> authors = json.getJSONArray("author").toJavaList(String.class);
            String version = json.getString("version");
            String name = json.getString("name");
            String description = json.getString("description");
            if (main != null && !Strings.isNullOrEmpty(name)) {
                log.info("加载插件 {} (版本: {})", name, version);
                final Class<?> mainClass = PluginLoader.INSTANCE.loadClass(main);
                if (mainClass.getSuperclass() != MohistPlugin.class) {
                    log.warn("插件 {} 加载失败: 主类没有继承MohistPlugin", jarFile.getName());
                } else {
                    plugin = (MohistPlugin) mainClass.newInstance();
                    plugin.name = name;
                    plugin.version = version;
                    plugin.authors = authors;
                    plugin.description = description;
                    plugin.classLoader = PluginLoader.INSTANCE;
                    plugin.dataDir = new File(PluginManager.pluginDir, plugin.getName());
                    plugin.sourceFile = jarFile;
                    plugin.onLoad();
                }
            } else {
                log.warn("插件 {} 加载失败: 无效的plugin.yml", jarFile.getName());
            }
            is.close();
        } else {
            log.warn("插件 {} 加载失败: 没有有效的plugin.json", jarFile.getName());
        }
        return plugin;
    }
}
