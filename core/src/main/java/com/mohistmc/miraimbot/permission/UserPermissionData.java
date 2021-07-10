package com.mohistmc.miraimbot.permission;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class UserPermissionData {
    @JSONField(serialize = false)
    public static final UserPermissionData DEFAULT = new UserPermissionData();


    public String[] permissions;
    public String group;
}
