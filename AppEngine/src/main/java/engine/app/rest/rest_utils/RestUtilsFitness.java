package engine.app.rest.rest_utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import engine.app.adshandler.AHandler;
import engine.app.adshandler.PromptHander;
import engine.app.fcm.GCMPreferences;
import engine.app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 23/04/18.
 */

public class RestUtilsFitness {
    private static RestUtilsFitness instance;
    private String countryCode ="IN";
    private String screenDimen ="XHDPI";
    private String appLaunchCount ="1";
    private String appVersion ="1";
    private String osVersion= "1";
    private String uniqueId ="fitnessapp";
    private String dVersion = "Samsung";
    private String applicationName = "second_fitness";

    private RestUtilsFitness() {
    }

    public static RestUtilsFitness getInstance() {
        if (instance == null) {
            synchronized (RestUtilsFitness.class) {
                if (instance == null) {
                    instance = new RestUtilsFitness();
                }
            }
        }
        return instance;
    }

    public void onSetRequestData(String countryCode, String screenDimen, String appLaunchCount, String appVersion, String osVersion,String dVersion, String uniqueId){
        this.countryCode = countryCode;
        this.screenDimen = screenDimen;
        this.appLaunchCount = appLaunchCount;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
        this.uniqueId =uniqueId;
        this.dVersion =dVersion;
    }

    public  String getCountryCode() {
        return countryCode;
    }

    public  String getScreenDimens() {
        return screenDimen;
    }

    public  String getAppLaunchCount() {
        return appLaunchCount;
    }

    public  String getVersion() {
        return appVersion;
    }

    public  String getOSVersion() {
        return osVersion;
    }

    public  String generateUniqueId() {
        return uniqueId;
    }
    public  String getDVersion() {
        return dVersion;
    }
    public String getApplicationName(){
        return applicationName;
    }

    private static int getRandomNo() {
        Random r = new Random();
        int low = 10000;
        int high = 99000;
        return r.nextInt(high - low) + low;
    }
}
