package engine.app.server.v2;

import static engine.app.utils.EngineConstant.dailyuseapps;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import app.pnd.adshandler.BuildConfig;
import engine.app.PrintLog;
import engine.app.ecrypt.MCrypt;
import engine.app.rest.request.DataRequest;

/**
 * Created by hp on 9/20/2017.
 */
public class DataHubConstant {

    public static boolean IS_LIVE = true;

    public static int APP_LAUNCH_COUNT = 1;
    public static String APP_ID = BuildConfig.APP_ID;

    public static String CUSTOM_ACTION = BuildConfig.MAPPER_ACTION;

    private Context mContext;

    static String KEY_SUCESS = "success";

    public static String KEY_NA = "NA";

    private static String mPackageName;

    public DataHubConstant(Context c) {
        this.mContext = c;
        mPackageName = c.getPackageName();
    }


    String readFromAssets(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = null;
        try {
            assert reader != null;
            mLine = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (mLine != null) {
            sb.append(mLine); // process line
            try {
                mLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintLog.print("check for logs 01");
        return sb.toString();
    }

    public String parseAssetData() {
        /*
         *Here we encrypt assets data bcoz
         * our parsing is working in encrypt and decrypt from.
         */
//        return enCryptData(readFromAssets("master_link.txt"));
        return readFromAssets("master_link.txt");
    }

    private String enCryptData(String response) {
        Gson gson = new Gson();
        DataRequest mMasterRequest = new DataRequest();
        DataHubResponse dataHubResponse = gson.fromJson(response, DataHubResponse.class);

        String jsonStr = gson.toJson(dataHubResponse);

        mMasterRequest.data = getEncryptString(jsonStr);

        return gson.toJson(mMasterRequest);

    }

    private String getEncryptString(String jsonStr) {
        String value = "";
        MCrypt mcrypt = new MCrypt();
        try {
            value = MCrypt.bytesToHex(mcrypt.encrypt(jsonStr));
        } catch (Exception e) {
            PrintLog.print("exception encryption" + " " + e);
            e.printStackTrace();
        }
        return value;
    }


    public String notificationChannelName() {

        if (mPackageName.contains(dailyuseapps)) {
            return dailyuseapps;
        }
        return dailyuseapps;
    }


}
