package com.universal.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.downloader.PRDownloader;
import com.m24apps.socialvideo.R;
import utils.SplashActivityV3;
import com.universal.gallery.AppPreference;
import com.universal.gallery.AppUtils;
import com.universal.helper.ClipBoardService;
import com.developer.whatsdelete.utils.Const;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import version2.retrofit.Api;
import version2.retrofit.PinterestRequest;

import static com.developer.whatsdelete.utils.Const.NOTIFICATION_CHANNEL_ID;


/**
 * Created by qunatum4u2 on 14/01/19.
 */

public class BackgroundRunningService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {
    // public static final int NOTIFICATION_ID = 11;
    private ClipboardManager mClipboardManager;
    private String paste;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("BackgroundRunngService", "Hello onCreate 01");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannels();
//            startNotificationForeGround(createRecordingNotificationPrimary().build(), Const.NOTIFICATION_ID);
            super.startForeground(Const.NOTIFICATION_ID, newRunningNotification());

        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            super.startForeground(NOTIFICATION_ID, new Notification());
//            startMyOwnForeground(NOTIFICATION_ID);
//        }
        PRDownloader.initialize(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BackgroundRunngService", "Hello onCreate 02" + " " + intent);
//        if (intent == null) {
//            stopSelf();
//            Log.d("BackgroundRunngService", "Hello onCreate 03");
//            return START_NOT_STICKY;
//        }

        if (mClipboardManager == null) {
            mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (mClipboardManager != null) {
                mClipboardManager.addPrimaryClipChangedListener((ClipboardManager.OnPrimaryClipChangedListener) this);


            }
        }
        return START_STICKY;
    }

    @Override
    public void onPrimaryClipChanged() {

        System.out.println("BackgroundRunningService.onPrimaryClipChanged gfhjszdgjfasgfjahg");

        try {
            if (!new AppPreference(getApplicationContext()).getdisableDownload()) {
                return;
            }


            if (mClipboardManager == null) {
                return;
            }

            ClipData clip = mClipboardManager.getPrimaryClip();
            if (clip != null) {
                paste = clip.getItemAt(0).getText().toString();
            }


            if(paste != null && paste.contains("https://pin.it")
                    || paste.contains("https://in.pinterest")
                    || paste.contains("https://www.pinterest")){

                new PinterestAsynTask(getApplicationContext(),paste).execute();


            }

            if (paste != null && paste.contains("https://www.instagram.com") || paste.contains("https://www.facebook.com") ||
                    paste.contains("https://m.facebook.com")||
                    paste.contains("https://like.video") || paste.contains("https://l.likee.video")|| paste.contains("https://share.like.video") || paste.contains("https://mobile.like-video")
                    || paste.contains("https://like-video") ||
                    paste.contains("tumblr.com/post") || paste.contains("tiktok.com")) {
                Log.d("BackgroundRService", "Test onPrimaryClipChanged..." + paste + "  ");
                startDownloadingLink(getApplicationContext(), paste, false);
            }

        } catch (Exception e) {

        }


    }

    public static void startDownloadingLink(Context context, String copyData, boolean goButtonClick) {

        Intent downloadService = new Intent(context, ClipBoardService.class);
        downloadService.putExtra(AppUtils.PASTE_MEDIA_URL, copyData)
                .putExtra(AppUtils.GO_BUTTON_CLICK, goButtonClick);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(downloadService);
//        } else {
        context.startService(downloadService);
        // }

    }

    public static void startDownloadingLink(Context context, String copyData, boolean goButtonClick,String pinteresturl,String isvideo) {

        Intent downloadService = new Intent(context, ClipBoardService.class);
        downloadService.putExtra(AppUtils.PASTE_MEDIA_URL, copyData)
                .putExtra(AppUtils.GO_BUTTON_CLICK, goButtonClick).putExtra(AppUtils.PINTEREST_URL,pinteresturl)
        .putExtra(AppUtils.PINTEREST_VIDEO_IMAGE,isvideo);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(downloadService);
//        } else {
        context.startService(downloadService);
        // }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy services background!");
        stopForeground(true);
        if (mClipboardManager != null) {
            if (mNotificationManager != null)
                mNotificationManager.cancel(Const.NOTIFICATION_ID);
        }


    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Log.d("BackgroundRunningServe", "Hello onTaskRemoved "+" "+this.getClass());
//        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//
//        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(),
//                1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(
//                AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + 1000,
//                restartServicePendingIntent);
//
//        super.onTaskRemoved(rootIntent);
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startMyOwnForeground(int channelId) {
//        String NOTIFICATION_CHANNEL_ID = "M24Apps";
//        String channelName = "Video Downloader";
//        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//        chan.setLightColor(Color.BLUE);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//        manager.createNotificationChannel(chan);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        Notification notification = notificationBuilder.setOngoing(true)
//                .setSmallIcon(R.drawable.status_app_icon)
//                .setContentTitle("Auto Downloading Service Enabled")
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//        startForeground(channelId, notification);
//    }

    private NotificationManager mNotificationManager;

    private NotificationManager getManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @TargetApi(26)
    private void createNotificationChannels() {
        List<NotificationChannel> notificationChannels = new ArrayList<>();
        NotificationChannel recordingNotificationChannel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                Const.channelName,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        recordingNotificationChannel.enableLights(true);
        recordingNotificationChannel.setLightColor(Color.BLUE);
        recordingNotificationChannel.setShowBadge(true);
        recordingNotificationChannel.enableVibration(true);
        recordingNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannels.add(recordingNotificationChannel);

        getManager().createNotificationChannels(notificationChannels);
    }

    private NotificationCompat.Builder createRecordingNotificationPrimary() {

        Intent intent = new Intent(this, SplashActivityV3.class);
        PendingIntent pcloseIntent = PendingIntent.getActivity(this, Const.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification1 = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.status_app_icon)
                .setAutoCancel(true)
                .setContentTitle("Auto Downloading Service Enabled")
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT);

        notification1.setContentIntent(pcloseIntent);

        return notification1;
    }

    //Start service as a foreground service. We dont want the service to be killed in case of low memory
    private void startNotificationForeGround(Notification notification, int ID) {
        startForeground(ID, notification);
    }


    private Notification newRunningNotification() {
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);


        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_app);

        Intent intent = new Intent(this, SplashActivityV3.class);
        intent.addCategory(this.getPackageName());
        PendingIntent pSplashLaunchIntent = PendingIntent.getActivity(this, Const.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent(this, SplashActivityV3.class);
        intent1.putExtra(AppUtils.onDefaultNotificationSetting, true);
        intent.addCategory(this.getPackageName());
        PendingIntent pSettingLaunchIntent = PendingIntent.getActivity(this, Const.NOTIFICATION_ID, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);


        contentView.setOnClickPendingIntent(R.id.linear, pSplashLaunchIntent);
        contentView.setOnClickPendingIntent(R.id.setting_button, pSettingLaunchIntent);

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(this.getResources().getString(R.string.fcm_defaultSenderId),
                    Const.channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(this,
                    this.getResources().getString(R.string.fcm_defaultSenderId));
            // .setContentTitle("Auto Download Service Enabled");

            //    builder.setContentIntent(pcloseIntent);
            builder.setCustomContentView(contentView);
            builder.setSmallIcon(R.drawable.status_app_icon);
            notification = builder.build();

        } else {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this,
                            this.getResources().getString(R.string.fcm_defaultSenderId));
            //  .setContentTitle("Auto Download Service Enabled");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.status_app_icon);

            } else {
                mBuilder.setSmallIcon(R.drawable.app_icon);
            }

            mBuilder.setCustomContentView(contentView);

            notification = mBuilder.build();
        }

        // notification.contentIntent = pcloseIntent;

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(Const.NOTIFICATION_ID, notification);
        return notification;
    }

    class PinterestAsynTask extends AsyncTask<Void, Integer, String> {
        private String pastedata;
        private Context mContext;

        PinterestAsynTask(Context context, String link){
            this.pastedata=link;
            this.mContext=context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                Document document = Jsoup.connect(pastedata).get();


                Log.d("HomeFragmentV2", "Hello onClick gsdjfgsjddg"+" "+document.baseUri());
                Api.getClient().registration(document.baseUri()).enqueue(new Callback<PinterestRequest>() {
                    @Override
                    public void onResponse(Call<PinterestRequest> call, Response<PinterestRequest> response) {

                        Log.d("HomeFragmentV2", "Hello onResponse pipipipipi ok"+" "+response.body().getUrl()+" "+
                                response.body().getMedia());

                        if(response.body().getUrl()!=null && !response.body().getUrl().equalsIgnoreCase("")){
                            startDownloadingLink(mContext, document.baseUri(), false,response.body().getUrl(),
                                    response.body().getMedia());
                        }
                    }

                    @Override
                    public void onFailure(Call<PinterestRequest> call, Throwable t) {
                        Log.d("HomeFragmentV2", "Hello onResponse pipipipipi failed"+" "+t.getStackTrace().toString());



                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
