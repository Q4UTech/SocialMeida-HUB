package engine.app.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import engine.app.adshandler.AHandler;
import engine.app.fcm.GCMPreferences;
import engine.app.fcm.MapperUtils;


/**
 * Created by Meenu Singh on 13-12-2017.
 */
public class MapperActivity extends Activity {
    private String splashNameMain, dashboardNameMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        GCMPreferences gcmPreferences = new GCMPreferences(this);
        splashNameMain = gcmPreferences.getSplashName();
        dashboardNameMain = gcmPreferences.getDashboardName();

        Intent intent = getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);

        System.out.println("0643 key value" + " " + value);

        if (type != null && value != null) {
            if (type.equalsIgnoreCase("url")) {
                launchAppWithMapper(type, value);

            } else if (type.equalsIgnoreCase("deeplink")) {
                handleValue(type, value);
            } else {
                this.finish();
            }
        } else {
            this.finish();
        }
    }


    private void handleValue(String type, String value) {
        switch (value) {
            case MapperUtils.gcmAppLaunch:
                //startActivity(new Intent(MapperActivity.this, MainActivity.class));
                if (dashboardNameMain != null) {
                    AHandler.getInstance().v2CallOnBGLaunch(this);
                    Intent intent = new Intent();
                    intent.setClassName(this, dashboardNameMain);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case MapperUtils.LAUNCH_SPLASH:
                //startActivity(new Intent(this, SplashActivity.class));
                if (splashNameMain != null) {
                    Intent intent = new Intent();
                    intent.setClassName(this, splashNameMain);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case MapperUtils.gcmMoreApp:
            case MapperUtils.gcmShareApp:
            case MapperUtils.gcmFeedbackApp:
            case MapperUtils.gcmRateApp:
            case MapperUtils.gcmForceAppUpdate:
            case MapperUtils.DL_ACTIVITY_PAGE:
            case MapperUtils.DL_PROFILE_PAGE:
            case MapperUtils.DL_PEDOMETER_HOME:
            case MapperUtils.DL_SEARCH_PAGE:
            case MapperUtils.DL_DOWNLOAD_PAGE:
            case MapperUtils.DL_CHAT_PAGE:
            case MapperUtils.DL_RESTORE:
                launchAppWithMapper(type, value);
                break;


            default:
                this.finish();
                break;

        }
    }


    private void launchAppWithMapper(String type, String value) {
        if (splashNameMain != null) {
            Intent intent = new Intent();
            intent.setClassName(this, splashNameMain);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MapperUtils.keyType, type);
            intent.putExtra(MapperUtils.keyValue, value);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        }


        finish();
    }
}
