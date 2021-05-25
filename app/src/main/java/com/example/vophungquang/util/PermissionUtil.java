package com.example.vophungquang.util;
/**
 * Created by vophungquang
 */
import android.content.Context;

import com.example.vophungquang.R;

public final class
PermissionUtil {
    public static final void checkPermission(Context context, PermissionListener permissionListener, String[] permissions, String msg) {
        new TedPermission(context)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(msg)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText(context.getString(R.string.setting))
                .setPermissions(permissions)
                .check();
    }
}
