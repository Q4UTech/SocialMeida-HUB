package com.jatpack.socialmediahub.helper;


import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


/**
 * Created by qunatum4u2 on 24/09/18.
 */

public class NotificationValue {
    public static int LONG_CLICK = 1121;
    private static String CHANNEL_NAME = "WhatsAppStatus Downloader";
    private static String NOTIFICATION_CHANNEL_DISCRIPTION = "WhatsAppStatus Downloader Notification";

    //test time
//    public static final long TIMER_3_HOUR = 1000 * 60 * 2;//2 min
//    public static final long TIMER_6_HOUR = 1000 * 60 * 3;//3 min
//    public static final long TIMER_12_HOUR = 1000 * 60 * 4;//4 min
//    public static final long TIMER_24_HOUR = 1000 * 60 * 10;//5 min

    //real time
    public static final long TIMER_3_HOUR = 1000 * 60 * 60 * 3;//3 hour
    public static final long TIMER_6_HOUR = 1000 * 60 * 60 * 6;//6 hour
    public static final long TIMER_12_HOUR = 1000 * 60 * 60 * 12;//12 hour
    public static final long TIMER_24_HOUR = 1000 * 60 * 60 * 24;//24 hour


    //Real
    public static final int DIFFERENCE_3 = -3;
    public static final int DIFFERENCE_6 = -6;
    public static final int DIFFERENCE_12 = -12;
    public static final int DIFFERENCE_24 = -24;

    //testing
//    public static final int DIFFERENCE_3 = -2;
//    public static final int DIFFERENCE_6 = -3;
//    public static final int DIFFERENCE_12 = -4;
//    public static final int DIFFERENCE_24 = -5;

   /* public static void show_new_Notification(Context context, Bitmap bitmap, String path, int difference) {
        System.out.println("LocalHost 00003 ");
        Intent intent = new Intent(context, NotificationStoryActivity.class);
        System.out.println("LocalHost 00004 ");

        //intent.addCategory(String.valueOf(context));
        intent.putExtra("bucketName", path);
        intent.putExtra("isNotification", true);
        intent.putExtra("latestFiles", difference);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.contentTitle, difference + " new status found");

        RemoteViews contentViewBig = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        contentViewBig.setTextViewText(R.id.contentTitle, difference + " new status found");
        contentViewBig.setImageViewBitmap(R.id.image, bitmap);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.fcm_defaultSenderId),
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DISCRIPTION);

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,
                        context.getResources().getString(R.string.fcm_defaultSenderId))
                        .setAutoCancel(true)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(contentViewBig);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(app.pnd.adshandler.R.drawable.status_app_icon);

        } else {
            mBuilder.setSmallIcon(app.pnd.adshandler.R.drawable.app_icon);
        }

        Notification notification = mBuilder.build();
        notification.contentIntent = pendingIntent;
        manager.notify(0, notification);

    }*/

}
