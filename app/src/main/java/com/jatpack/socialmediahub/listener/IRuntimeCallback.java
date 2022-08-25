package com.jatpack.socialmediahub.listener;

import androidx.annotation.NonNull;

/**
 * Created by Anon on 03,June,2019
 */
public interface IRuntimeCallback {

    void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
