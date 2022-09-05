package com.jatpack.socialmediahub.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rajeev on 23/08/18.
 */


public class AppUtils {
    public static final long View_3 = 3 * 1000;
    public static final long View_5 = 5 * 1000;
    public static final long View_10 = 10 * 1000;
    public static final String FILTTER_ALL = "all";
    public static final String WHATSAPP_STATUS_DIR_GALLERY = "/storage/emulated/0/my_gallery/WhatsApp story";
    public static String WHATSAPP_STATUS_DIR_GALLERY_STORY;
    public static String STATUS_GALLERY_PATH;
    public static String STATUS_TRANDING_GALLERY_PATH;
    public static String WHATSAPP_STATUS_DIR = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
    private Context context;
    public static final String MAIN_FOLDER_NAME = "Status Downloader/.WhatsDelete/";
    //Created bcoz of new storage changes...
    public static String MAIN_DIR_ABOVE_PIE;
    public static final String WHATSAPP_PACKAGE = "/Android/media/com.whatsapp/";
    public static final String WHATSAPP_STATUS_DIR_WITHIN_APP = Environment.getExternalStorageDirectory().toString() + WHATSAPP_PACKAGE +"WhatsApp/Media/.Statuses";



    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static long getCurrentDate(String string_date) {
        long milliseconds = 0;

        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public void init(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            WHATSAPP_STATUS_DIR_GALLERY_STORY = context.getExternalFilesDir("Quantum NoCrop For DP").getPath();
            STATUS_GALLERY_PATH = context.getExternalFilesDir("WA Status Gallery").getPath();
            STATUS_TRANDING_GALLERY_PATH = context.getExternalFilesDir("WA Status Tranding Gallery").getPath();

            MAIN_DIR_ABOVE_PIE = context.getExternalFilesDir(MAIN_FOLDER_NAME  + "reserved/").getPath();

            Log.d("TAG", "init: testPath: 000012 " + WHATSAPP_STATUS_DIR);

        } else {

            WHATSAPP_STATUS_DIR = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
            WHATSAPP_STATUS_DIR_GALLERY_STORY = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "Quantum NoCrop For DP";
            STATUS_GALLERY_PATH = context.getExternalFilesDir("WA Status Gallery").getPath();
            STATUS_TRANDING_GALLERY_PATH = context.getExternalFilesDir("WA Status Tranding Gallery").getPath();


            MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + MAIN_FOLDER_NAME+ "reserved/";

            Log.d("TAG", "init: testPath: 000013 " + WHATSAPP_STATUS_DIR.toString());

        }
    }


    public static String getAppMainDir() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            return MAIN_DIR_ABOVE_PIE;
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MAIN_FOLDER_NAME";
        }
    }

    public static String createAppDir(Context context) {
        String APP_DIR = context.getExternalFilesDir("WA Status Gallery").getAbsolutePath();
        File file = new File(APP_DIR);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        } else {
            file.mkdir();
        }

        return APP_DIR;
    }


    public static String getWhatsAppStatusPath(){
        File file = new File(WHATSAPP_STATUS_DIR);
        if(file.exists()){
            return WHATSAPP_STATUS_DIR;
        }else {
            return WHATSAPP_STATUS_DIR_WITHIN_APP;
        }
    }


    public static boolean check_hasExtra(Activity activity, String putExtraValue){
        try {
            if(activity.getIntent().hasExtra(putExtraValue)){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static String getPutExtraValue(Activity activity, String putExtraName) {
        String value;
        try{
            value = activity.getIntent().getStringExtra(putExtraName);
            return value;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static int getDeviceOrientation(Context context)
    {
        // 1 -> portrait 2 -> landscape
        int orientation = -1;
        orientation = ((Activity)context).getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            // Log.e("#orientation", String.valueOf(orientation));
        } else {
            // code for landscape mode
            // Log.e("#orientation1", String.valueOf(orientation));
        }
        return orientation;
    }

    //time conversion
    public static String timeConversion1(long value) {
        String videoTime= "";
        try {
            int dur = (int) value;
            int hrs = (dur / 3600000);
            int mns = (dur / 60000) % 60000;
            int scs = dur % 60000 / 1000;

            if (hrs > 0) {
                videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
            } else {
                videoTime = String.format("%02d:%02d", mns, scs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Log.e("videoTime::",videoTime);
        return videoTime;
    }


}