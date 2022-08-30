package com.jatpack.socialmediahub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jatpack.socialmediahub.R;

public class SocialMediaHubService extends Service {
    private Handler handler;
    private int ID = 1;

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            super.startForeground(ID, new Notification());
            startMyOwnForeground();
            registerPowerConnectionReceiver();
        }
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    //private static final int DIFFERENCE_3 = -3;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//        getContentResolver().unregisterContentObserver(imageObserver);
//        if (_timer != null)

//            System.out.println("my timer is here" + " " + _timer);
//        _timer.cancel();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "quantum4u_notification_channel_whatsapp";
        String channelName = "quantum4u";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_camera_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void registerPowerConnectionReceiver() {
      /*  CallBlockReceiver powerConnectionReceiver =
                new CallBlockReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        ifilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(powerConnectionReceiver, ifilter);*/
    }
}
