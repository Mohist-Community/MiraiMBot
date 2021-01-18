package com.mohistmc.miraimbot.annotations.processors;

import com.mohistmc.miraimbot.annotations.Listener;
import com.alibaba.fastjson.JSONArray;
import com.mohistmc.yaml.util.Charsets;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("com.mohistmc.miraimbot.annotations.Listener")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ListenerProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println("Init...");
        super.init(processingEnv);
    }


    @SneakyThrows
    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> clazz = roundEnv.getElementsAnnotatedWith(Listener.class);
        JSONArray ja = new JSONArray();
        File dir = new File("./build/repack/");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "listeners.json");
        if (!file.exists()) {
            file.createNewFile();
            clazz.forEach(element -> {
                if (element.getKind() != ElementKind.CLASS) {
                    return;
                }
                System.out.println("Processing listener " + element);
                ja.add(element.toString());
            });
            System.out.println(ja.toJSONString());
            try {
                FileUtils.writeStringToFile(file, ja.toJSONString(), Charsets.UTF_8);
            } catch (IOException e) {

            }
        }
        return true;
    }
}
