package com.jatpack.socialmediahub;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;


import com.jatpack.socialmediahub.util.AppUtils;
import com.jatpack.socialmediahub.util.Constants;


import java.io.File;



/**
 * Created by Anon on 13,August,2018
 */
public class MainApplication extends Application {

    public static final String MAIN_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MAIN_FOLDER_NAME";

//    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.quantum.whatsappstatus" + File.separator + "/hello";


    public static String BASE_APP_DIR;






    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TAG", "onCreate: chheckk000000");


        new AppUtils().init(getApplicationContext());
//        new Constants().init(getApplicationContext());


        new Constants().init(getApplicationContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Calldorado.start(this);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  MultiDex.install(base);
    }
}
