package com.mohistmc.miraimbot.permission;

import com.mohistmc.miraimbot.command.ConsoleSender;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Sets;
import com.mohistmc.yaml.util.Charsets;
import lombok.SneakyThrows;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.UserOrBot;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Permission {
    public static final File permissionDir = new File("permission");
    public static final File userPermissionDir = new File(permissionDir, "users");
    public static final File groupPermissionDir = new File(permissionDir, "groups");
    private static final File permissionFile = new File(permissionDir, "permission.json");
    private static final File opsFile = new File(permissionDir, "ops.json");
    private static final Logger log = LogManager.getLogger("Permission");
    private static final Set<Long> ops = Sets.newHashSet();


    @SneakyThrows
    public static void init() {
        if (!permissionDir.exists()) permissionDir.mkdirs();
        if (!userPermissionDir.exists()) userPermissionDir.mkdirs();
        if (!groupPermissionDir.exists()) groupPermissionDir.mkdirs();
        if (!permissionFile.exists()) permissionFile.createNewFile();

        if (!opsFile.exists()) {
            opsFile.createNewFile();
            FileUtils.writeStringToFile(opsFile, new JSONArray(new ArrayList<Object>() {{
                add(123456789L);
            }}).toJSONString(), Charsets.UTF_8);
        }
        load();
    }

    @SneakyThrows
    public static void load() {
        String jstr = FileUtils.readFileToString(opsFile, Charsets.UTF_8);
        if (!JSON.isValid(jstr)) {
            log.error("ops.json文件损坏");
            return;
        }
        ops.clear();
        ops.addAll(JSON.parseArray(jstr).toJavaList(Long.class));
    }

    /**
     * 判断sender是否有permission权限
     *
     * @param sender     用户
     * @param permission 权限
     * @return sender是否有permission权限
     */
    public static boolean hasPermission(UserOrBot sender, String permission) {
        if (isOp(sender)) return true;
        if (sender instanceof NormalMember) {
            return ((NormalMember) sender).getPermission() == MemberPermission.ADMINISTRATOR || ((NormalMember) sender).getPermission() == MemberPermission.OWNER;
        }
        String[] node = permission.split("\\.");
        UserPermissionData data = getUserPermissionData(sender);
        for (String per : data.getPermissions()) {
            String[] perNode = per.split("\\.");
            if (node.length == perNode.length && perNode[perNode.length - 1].equalsIgnoreCase("*")) return true;
            if (permission.equalsIgnoreCase(per)) return true;
            if (PermissionGroup.hasPermission(data.getGroup(), permission)) return true;
        }
        return false;
    }

    @SneakyThrows
    public static UserPermissionData getUserPermissionData(UserOrBot sender) {
        File file = new File(userPermissionDir, sender.getId() + ".json");
        if (!file.exists()) {
            return saveUserPermissionData(sender, UserPermissionData.DEFAULT);
        }
        String jstr = FileUtils.readFileToString(file, Charsets.UTF_8);
        return JSON.isValid(jstr) ? JSON.parseObject(jstr, UserPermissionData.class) : UserPermissionData.DEFAULT;
    }

    @SneakyThrows
    public static UserPermissionData saveUserPermissionData(UserOrBot sender, UserPermissionData data) {
        File file = new File(userPermissionDir, sender.getId() + ".json");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileUtils.writeStringToFile(file, JSON.toJSONString(data), Charsets.UTF_8);
        return data;
    }

    /**
     * 判断sender是否为op
     *
     * @param sender 用户
     * @return sender是否有permission权限
     */
    public static boolean isOp(UserOrBot sender) {
        if (sender instanceof ConsoleSender) return true;
        return getAllOps().contains(sender.getId());
    }

    /**
     * 判断sender是否为op
     *
     * @param sender 用户
     * @return sender是否有permission权限
     */
    public static boolean isOp(long sender) {
        return getAllOps().contains(sender);
    }

    /**
     * 判断sender是否为op
     *
     * @param sender 用户
     * @return sender是否有permission权限
     */
    public static void addOp(UserOrBot sender) {
        if (sender instanceof ConsoleSender) return;
        addOp(sender.getId());
    }

    public static void addOp(long sender) {
        if (isOp(sender)) return;
        ops.add(sender);
        saveOpsData();
    }

    public static void addPermission(UserOrBot sender, String permission) {
        if (sender instanceof ConsoleSender) return;
        UserPermissionData data = getUserPermissionData(sender);
        data.setPermissions(new ArrayList<String>() {{
            addAll(Arrays.asList(data.getPermissions()));
            add(permission);
        }}.toArray(new String[0]));
        saveUserPermissionData(sender, data);
    }

    public static void removePermission(UserOrBot sender, String permission) {
        if (sender instanceof ConsoleSender) return;
        UserPermissionData data = getUserPermissionData(sender);
        data.setPermissions(new ArrayList<String>() {{
            addAll(Arrays.asList(data.getPermissions()));
            remove(permission);
        }}.toArray(new String[0]));
        saveUserPermissionData(sender, data);
    }

    @SneakyThrows
    public static void saveOpsData() {
        JSONArray ja = new JSONArray();
        for (long op : ops) ja.add(op);
        FileUtils.writeStringToFile(opsFile, ja.toJSONString(), Charsets.UTF_8);
    }

    public static Set<Long> getAllOps() {
        return ops;
    }
}
