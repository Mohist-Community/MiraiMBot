package com.mohistmc.miraimbot.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mohistmc.miraimbot.command.CommandExecutor;
import com.mohistmc.miraimbot.plugin.MohistPlugin;
import com.mohistmc.miraimbot.plugin.PluginClassLoader;
import com.mohistmc.miraimbot.plugin.PluginLoader;
import com.mohistmc.yaml.util.Charsets;
import lombok.SneakyThrows;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.SimpleListenerHost;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ListenerManager {

    private static final Logger log = LogManager.getLogger("ListenerManager");

    @SneakyThrows
    public static void register(MohistPlugin plugin) {
        JarFile jarFile = plugin.getSourceFile();
        ZipEntry ze = jarFile.getEntry("listeners.json");
        InputStream is = ze != null ? jarFile.getInputStream(ze) : null;
        if (is != null) {
            log.debug("从 {} 找到了listeners.json", jarFile.getName());
            String jstr = IOUtils.toString(is, Charsets.UTF_8);
            log.debug("从 {} 读取到的listeners.json: {}", jarFile.getName(), jstr);
            if (JSON.isValid(jstr)) {
                JSONArray ja = JSON.parseArray(jstr);
                for (String clazzN : ja.toArray(new String[0])) {
                    Class<?> clazz = PluginLoader.INSTANCE.loadClass(clazzN);
                    if (clazz.getSuperclass() != SimpleListenerHost.class) {
                        log.error("事件监听器没有继承SimpleListenerHost ({})", clazzN);
                        return;
                    }
                    register((SimpleListenerHost) clazz.newInstance());
                }
            } else {
                log.error("插件 {} 损坏", plugin.getName());
            }
            is.close();
        }
    }

    public static void register(SimpleListenerHost instance) {
        GlobalEventChannel.INSTANCE.registerListenerHost(instance);
        log.info("注册了监听器 {}", instance.getClass());
    }
}
