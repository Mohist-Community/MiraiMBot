package com.mohistmc.miraimbot.permission;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class GroupPermissionData {
    @JSONField(serialize = false)
    public static final GroupPermissionData DEFAULT = new GroupPermissionData();

    public String[] permissions;
    public String group;
}
