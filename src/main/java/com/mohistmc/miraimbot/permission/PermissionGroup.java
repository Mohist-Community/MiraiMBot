package com.mohistmc.miraimbot.permission;

import com.alibaba.fastjson.JSON;
import com.mohistmc.miraimbot.command.ConsoleSender;
import com.mohistmc.yaml.util.Charsets;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import net.mamoe.mirai.contact.UserOrBot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@Data
@Builder
public class PermissionGroup {
    private static final File groupPermissionDir = Permission.groupPermissionDir;

    public static boolean hasPermission(String group, String permission) {
        GroupPermissionData data = getGroupPermissionData(group);
        String[] node = permission.split("\\.");
        for (String per : data.getPermissions()) {
            String[] perNode = per.split("\\.");
            if (node.length == perNode.length && perNode[perNode.length - 1].equalsIgnoreCase("*")) return true;
            if (permission.equalsIgnoreCase(per)) return true;
        }
        return false;
    }

    @SneakyThrows
    public static GroupPermissionData getGroupPermissionData(String group) {
        File file = new File(groupPermissionDir, group + ".json");
        if (!file.exists()) {
            return saveGroupPermissionData(group, GroupPermissionData.DEFAULT);
        }
        String jstr = FileUtils.readFileToString(file, Charsets.UTF_8);
        return JSON.isValid(jstr) ? JSON.parseObject(jstr, GroupPermissionData.class) : GroupPermissionData.DEFAULT;
    }

    @SneakyThrows
    public static GroupPermissionData saveGroupPermissionData(String group, GroupPermissionData data) {
        File file = new File(groupPermissionDir, group + ".json");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileUtils.writeStringToFile(file, JSON.toJSONString(data), Charsets.UTF_8);
        return data;
    }

    public static void addPermission(String group, String permission) {
        GroupPermissionData data = getGroupPermissionData(group);
        data.setPermissions(new ArrayList<String>() {{
            addAll(Arrays.asList(data.getPermissions()));
            add(permission);
        }}.toArray(new String[0]));
        saveGroupPermissionData(group, data);
    }

    public static void removePermission(String group, String permission) {
        GroupPermissionData data = getGroupPermissionData(group);
        data.setPermissions(new ArrayList<String>() {{
            addAll(Arrays.asList(data.getPermissions()));
            remove(permission);
        }}.toArray(new String[0]));
        saveGroupPermissionData(group, data);
    }

    public static void addToGroup(UserOrBot sender, String group) {
        if (sender instanceof ConsoleSender) return;
    }

    public static void removeFromGroup(UserOrBot sender, String group) {
        if (sender instanceof ConsoleSender) return;
    }

    public static void changeToGroup(UserOrBot sender, String group, String group1) {
        removeFromGroup(sender, group);
        addToGroup(sender, group1);
    }
}
