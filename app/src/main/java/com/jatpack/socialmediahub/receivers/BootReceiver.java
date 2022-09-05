package com.jatpack.socialmediahub.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.jatpack.socialmediahub.helper.Pref;
import com.jatpack.socialmediahub.service.SocialMediaHubService;


public class BootReceiver extends BroadcastReceiver {
    Pref pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        pref = new Pref(context);
        System.out.println("Boot Receiver called");
        if (pref != null && pref.getAutoNotificationEnable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, SocialMediaHubService.class));
            } else {
                context.startService(new Intent(context, SocialMediaHubService.class));
            }
        }
    }



}
