package com.mohistmc.miraimbot.plugin;

public class PluginClassLoader extends ClassLoader {
    public static final PluginClassLoader INSTANCE = new PluginClassLoader();

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}