package com.mohistmc.miraimbot.processors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mohistmc.miraimbot.annotations.Command;
import com.mohistmc.miraimbot.annotations.NoShow;
import com.mohistmc.miraimbot.annotations.OnlyOp;
import com.mohistmc.miraimbot.annotations.Permission;
import com.mohistmc.yaml.util.Charsets;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

@SupportedAnnotationTypes("com.mohistmc.miraimbot.annotations.Command")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CommandProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println(">>>>>>>CommandProcessor Init<<<<<<<");
        super.init(processingEnv);
    }

    @SneakyThrows
    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> clazz = roundEnv.getElementsAnnotatedWith(Command.class);
        File dir = new File("./build/repack/");
        JSONArray ja = new JSONArray();
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "commands.json");
        if (!file.exists()) {
            file.createNewFile();
            clazz.forEach(element -> {
                if (element.getKind() != ElementKind.CLASS) return;
                System.out.println(">>> Processing command " + element);
                Command command = element.getAnnotation(Command.class);
                NoShow noShow = element.getAnnotation(NoShow.class);
                OnlyOp onlyOp = element.getAnnotation(OnlyOp.class);
                Permission permission = element.getAnnotation(Permission.class);
                if (command == null) {
                    System.out.println(">>> Has not annotation at " + element);
                } else {
                    System.out.println(">> Executor: " + element.toString());
                    System.out.println(">> Label: " + command.value());
                    System.out.println(">> Usage: " + command.usage());
                    System.out.println(">> Description: " + command.description());
                    System.out.println(">> NoShow <<");
                    System.out.println("> Enable: " + (noShow != null));
                    System.out.println("> OpCan: " + ((noShow != null) && noShow.opCan()));
                    System.out.println(">> OnlyOp: " + (onlyOp != null));
                    System.out.println(">> Permission <<");
                    System.out.println("> Enable: " + (permission != null));
                    System.out.println("> Permission: " + (permission != null ? permission.value() : "none"));
                    System.out.println(">> Alias: " + Arrays.toString(command.alias()));
                    System.out.println(">> Type: " + command.type().toString());
                    ja.add(new JSONObject(new HashMap<String, Object>() {{
                        put("class", element.toString());
                        put("label", command.value());
                        put("usage", command.usage());
                        put("description", command.description());
                        put("noshow", new JSONObject(new HashMap<String, Object>() {{
                            put("enable", noShow != null);
                            put("opCan", noShow != null && noShow.opCan());
                        }}));
                        put("onlyOp", onlyOp != null);
                        put("permission", new JSONObject(new HashMap<String, Object>() {{
                            put("enable", permission != null);
                            put("permission", permission != null ? permission.value() : "");
                        }}));
                        put("alias", new JSONArray(new ArrayList<Object>() {{
                            addAll(Arrays.asList(command.alias()));
                        }}));
                        put("type", command.type().toString());
                    }}));
                }
            });
            try {
                FileUtils.writeStringToFile(file, ja.toJSONString(), Charsets.UTF_8);
            } catch (Throwable e) {
                System.out.println(e);
            }
            System.out.println(">>> Success <<<");
        }
        return true;
    }
}
