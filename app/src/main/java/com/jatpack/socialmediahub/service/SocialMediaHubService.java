package com.jatpack.socialmediahub.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.jatpack.socialmediahub.MainActivity;
import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.activities.SettingActivity;
import com.jatpack.socialmediahub.helper.Pref;

public class SocialMediaHubService extends Service {
    private Handler handler;
    private Context mContext;
    private int ID = 1;
    Pref pref;

    @Override
    public void onCreate() {
        super.onCreate();
        pref = new Pref(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMyOwnForeground();
        handleNotification(intent);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "quantum4u_notification_channel_whatsapp";
        String channelName = "quantum4u";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.socialhub_custom_notification);

        setChannelAction(remoteViews);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.icon_bid)
                .setCustomContentView(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        startForeground(2, notification);
    }

    private void setChannelAction(RemoteViews remoteViews) {
        String strtitle = getString(R.string.all_social);
        if (pref.getSearchPref()) {
            remoteViews.setViewVisibility(R.id.ll_search, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_search, View.GONE);
        }
        if (pref.getCameraPref()) {
            remoteViews.setViewVisibility(R.id.ll_camera, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_camera, View.GONE);
        }
        if (pref.getWhatsAppPref()) {
            remoteViews.setViewVisibility(R.id.ll_whatsapp, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_whatsapp, View.GONE);
        }
        if (pref.getMessagePref()) {
            remoteViews.setViewVisibility(R.id.ll_msg, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_msg, View.GONE);
        }
        if (pref.getMessengerPref()) {
            remoteViews.setViewVisibility(R.id.ll_messanger, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_messanger, View.GONE);
        }
        if (pref.getFacebookPref()) {
            remoteViews.setViewVisibility(R.id.ll_facebook, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_facebook, View.GONE);
        }

        if (pref.getPostion() == 0) {
            remoteViews.setViewVisibility(R.id.ll_status, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_status, View.GONE);
        }

        if (pref.getPostion() == 1) {
            remoteViews.setViewVisibility(R.id.ll_video_downloader, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_video_downloader, View.GONE);
        }

        if (pref.getPostion() == 2) {
            remoteViews.setViewVisibility(R.id.ll_direct_chat, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.ll_direct_chat, View.GONE);
        }

        Intent searchIntent = new Intent(this, SocialMediaHubService.class).setAction("search_action");
        PendingIntent searchPendingIntent = PendingIntent.getService(this, 0, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_search, searchPendingIntent);

        Intent cameraIntent = new Intent(this, SocialMediaHubService.class).setAction("camera_action");
        PendingIntent cameraPendingIntent = PendingIntent.getService(this, 0, cameraIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_camera, cameraPendingIntent);

        Intent whatAppIntent = new Intent(this, SocialMediaHubService.class).setAction("whatsapp_action");
        PendingIntent whatsPendingIntent = PendingIntent.getService(this, 0, whatAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_whatsapp, whatsPendingIntent);

        Intent msgIntent = new Intent(this, SocialMediaHubService.class).setAction("msg_action");
        PendingIntent msgPendingIntent = PendingIntent.getService(this, 0, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_msg, msgPendingIntent);

        Intent messengerIntent = new Intent(this, SocialMediaHubService.class).setAction("messenger_action");
        PendingIntent messengerPendingIntent = PendingIntent.getService(this, 0, messengerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_messanger, messengerPendingIntent);

        Intent facebookIntent = new Intent(this, SocialMediaHubService.class).setAction("fb_action");
        PendingIntent facebookPendingIntent = PendingIntent.getService(this, 0, facebookIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_facebook, facebookPendingIntent);

        Intent settingIntenet = new Intent(this, SocialMediaHubService.class).setAction("setting_action");
        PendingIntent settingPendingIntent = PendingIntent.getService(this, 0, settingIntenet, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivSetting, settingPendingIntent);

        Intent waStatusIntent = new Intent(this, SocialMediaHubService.class).setAction("wa_status_action");
        PendingIntent waStatusPendingIntent = PendingIntent.getService(this, 0, waStatusIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivSetting, waStatusPendingIntent);

        Intent downloadIntent = new Intent(this, SocialMediaHubService.class).setAction("download_action");
        PendingIntent downloadPendingIntent = PendingIntent.getService(this, 0, downloadIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivSetting, downloadPendingIntent);

        Intent chatIntent = new Intent(this, SocialMediaHubService.class).setAction("chat_action");
        PendingIntent chatPendingIntent = PendingIntent.getService(this, 0, chatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivSetting, chatPendingIntent);
    }

    private void handleNotification(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "search_action":
                    startActivity(new Intent(this, MainActivity.class));
                    closingBroadcast();
                    break;
                case "camera_action":
                    try {
                        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cameraIntent);
                        closingBroadcast();
                    } catch (Exception e) {
                        Log.d("TAG", "handleNotification: " + e.getMessage());
                    }

                    break;
                case "whatsapp_action":
                    try {
                        Intent waPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                        startActivity(waPackage);
                        closingBroadcast();
                    } catch (Exception e) {
                        Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    break;
                case "msg_action":
                    try {
                        Intent msg = getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(this));
                        startActivity(msg);
                        closingBroadcast();
                    } catch (Exception e) {
                        Toast.makeText(this, "Message app not installed in your phone", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    break;
                case "messenger_action":
                    try {
                        Intent messengerPackage = getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
                        startActivity(messengerPackage);
                        closingBroadcast();
                    } catch (Exception e) {
                        Toast.makeText(this, "Messenger app not installed in your phone", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    break;
                case "fb_action":
                    try {
                        Intent fbPackage = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                        startActivity(fbPackage);
                        closingBroadcast();
                    } catch (Exception e) {
                        Toast.makeText(this, "Facebook app not installed in your phone", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    break;
                case "setting_action":
                    Intent setting = new Intent(this, SettingActivity.class);
                    setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(setting);
                    closingBroadcast();
                    break;
                case "wa_status_action":
                    Intent waStatus = new Intent(this, MainActivity.class);
                    waStatus.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(waStatus);
                    closingBroadcast();
                    break;
                case "download_action":
                    Intent downloadIntent = new Intent(this, MainActivity.class);
                    downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(downloadIntent);
                    closingBroadcast();
                    break;
                case "chat_action":
                    Intent chatIntent = new Intent(this, MainActivity.class);
                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(chatIntent);
                    closingBroadcast();
                    break;


            }
        }
    }

    private void closingBroadcast() {
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public boolean isStoragePermissionGrantedonly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getBaseContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


}
