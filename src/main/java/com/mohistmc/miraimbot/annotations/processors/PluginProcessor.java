package com.mohistmc.miraimbot.annotations.processors;

import com.mohistmc.miraimbot.annotations.Plugin;
import com.alibaba.fastjson.JSONObject;
import com.mohistmc.yaml.util.Charsets;
import org.apache.commons.io.FileUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes("com.mohistmc.miraimbot.annotations.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PluginProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println("Init...");
        super.init(processingEnv);
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> clazz = roundEnv.getElementsAnnotatedWith(Plugin.class);
        if (clazz.size() > 1) {
            System.out.println("More than one plugin");
            return true;
        }
        clazz.forEach(element -> {
            if (element.getKind() != ElementKind.CLASS) return;
            System.out.println("Processing plugin " + element);
            Plugin plugin = element.getAnnotation(Plugin.class);
            if (plugin == null) {
                System.out.println("Has not annotation at " + element);
            } else {
                System.out.println("Authors: " + Arrays.toString(plugin.authors()));
                System.out.println("Name: " + plugin.value());
                System.out.println("Version: " + plugin.version());
                System.out.println("Description: " + plugin.description());
                File dir = new File("./build/repack/");
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, "plugin.json");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {

                    }
                }
                JSONObject json = new JSONObject();
                json.put("main", element.toString());
                json.put("name", plugin.value());
                json.put("author", plugin.authors());
                json.put("version", plugin.version());
                json.put("description", plugin.description());
                System.out.println("Creating plugin.json...");
                try {
                    FileUtils.writeStringToFile(file, json.toJSONString(), Charsets.UTF_8);
                } catch (Throwable e) {
                    System.out.println(e);
                }
                System.out.println("Success!");
            }

        });
        return true;
    }
}
