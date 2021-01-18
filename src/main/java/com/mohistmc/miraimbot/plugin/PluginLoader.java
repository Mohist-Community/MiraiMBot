package com.mohistmc.miraimbot.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class PluginLoader extends URLClassLoader {
    public static final PluginLoader INSTANCE = new PluginLoader();

    public PluginLoader() {
        super(new URL[0], PluginClassLoader.INSTANCE);
    }

    public MohistPlugin loadPlugin(File file) throws IOException, ClassNotFoundException {
        String url = "jar:file:///" + file.getAbsolutePath() + "!/";
        this.addURL(new URL(url));
        JarFile jarFile = new JarFile(file, false);

        return PluginManager.createPluginInstance(jarFile);
    }
}
