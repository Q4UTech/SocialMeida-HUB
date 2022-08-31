package com.jatpack.socialmediahub;

import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import java.io.File;


/**
 * Created by Anon on 13,August,2018
 */
public class MainApplication extends MultiDexApplication {

    public static final String MAIN_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SocialMediaHub";

//    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.q4u.whatsappstatus" + File.separator + "/hello";


    public static String BASE_APP_DIR;


    //Created bcoz of new storage changes...
    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.m24apps.socialvideo" + File.separator + "SocialMediaHub";







    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TAG", "onCreate: chheckk000000");


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
