package com.frame.core.util.PermissionHelper.manufacturer;

import android.content.Intent;

/**
 * Created by joker on 2017/8/4.
 */

public interface PermissionsPage {
    String PACK_TAG = "package";

    // normally, ActivityNotFoundException
    Intent settingIntent() throws Exception;
}
