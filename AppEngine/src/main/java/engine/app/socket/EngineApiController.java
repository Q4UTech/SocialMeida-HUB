package engine.app.socket;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import engine.app.server.v2.DataHubConstant;

public class EngineApiController implements Response {
    private WeakReference<Context> contxt;
    private Response response;
    private EngineClient client;


    public static String BASE_URL = "https://phonedatashare.com/engine/";

    /*
     * new service url using for fcm id request and for fcm push notification.
     */
    private static String NEW_BASE_URL = "https://phonedatashare.com/engine/";

    private static String ADS_SERVICE_URL = "adservicevfour/";


    public static String ENGINE_VERSION = "6";

    private String MASTER_SERVICE_URL;
    private String GCM_ID_SERVICE_URL;
    private String NOTIFICATION_ID_SERVICE_URL;
    private String VERSION_SERVICE_URl;
    private String REFERRAL_URL;
    private String INHOUSE_URL;
    private String FCM_TOPIC_URL;
    private String INAPP_URL;
    public static final int MASTER_SERVICE_CODE = 1;
    public static final int GCM_SERVICE_CODE = 2;
    public static final int NOTIFICATION_ID_CODE = 3;
    public static final int VERSION_ID_CODE = 4;
    public static final int REFERRAL_ID_CODE = 5;
    public static final int INHOUSE_CODE = 6;
    public static final int FCM_TOPIC_CODE = 7;
    public static final int INAPP_CODE = 8;

    private int responseType;
    private ProgressDialog dialog;
    private boolean isProgressShow;
//    private boolean isDataOnline = true;

    public EngineApiController(Context context, Response response,
                               int responseType, boolean isProgressShow) {
        this.contxt = new WeakReference<>(context);
        this.response = response;
        this.responseType = responseType;
        this.isProgressShow = isProgressShow;
        client = new EngineClient(contxt.get(), this);

        if (DataHubConstant.IS_LIVE) {
            MASTER_SERVICE_URL = BASE_URL +ADS_SERVICE_URL+ "adsresponse?engv=" + ENGINE_VERSION; //new engineV4 using link .
            VERSION_SERVICE_URl = BASE_URL+ADS_SERVICE_URL + "checkappstatus?engv=" + ENGINE_VERSION;
            REFERRAL_URL = BASE_URL + "gcm/requestreff?engv=" + ENGINE_VERSION;
            INHOUSE_URL = BASE_URL + ADS_SERVICE_URL+"inhousbanner?engv=" + ENGINE_VERSION;
            INAPP_URL = BASE_URL + "inappreporting/successInapp?engv=" + ENGINE_VERSION;

            //New one
            GCM_ID_SERVICE_URL = NEW_BASE_URL + "Gcm/requestgcm?engv=" + ENGINE_VERSION;
            NOTIFICATION_ID_SERVICE_URL = NEW_BASE_URL + "Gcm/requestnotification?engv=" + ENGINE_VERSION;
            FCM_TOPIC_URL = NEW_BASE_URL + "Gcm/requestgcmv4?engv=" + ENGINE_VERSION;

        }

    }

    public EngineApiController(Context context, Response response,
                               int responseType) {
        this(context, response, responseType, true);
    }

    public void setFCMTokens(String mToken) {
        client.setFCMTokens(mToken);
    }

    public void setInHouseType(String type) {
        client.setInHouseType(type);
    }

    public void setNotificationID(String _id) {
        client.setNotificationID(_id);
    }

    public void setAllTopics(ArrayList<String> list) {
        client.setAllTopics(list);
    }

    public void setInAppData(String productID) {
        client.setInAppData(productID);
    }

    @Override
    public void onResponseObtained(Object response, int responseType,
                                   boolean isCachedData) {
        this.response.onResponseObtained(response, responseType, isCachedData);
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public void getMasterData(Object mMasterRequest) {
        if (checkConnection()) {
            client.Communicate(MASTER_SERVICE_URL, mMasterRequest, responseType);
        }
    }

    public void getGCMIDRequest(Object mGCMIdRequest) {
        if (checkConnection()) {
            client.Communicate(GCM_ID_SERVICE_URL, mGCMIdRequest, responseType);
        }
    }

    public void getNotificationIDRequest(Object mNotificationRequest) {
        if (checkConnection()) {
            client.Communicate(NOTIFICATION_ID_SERVICE_URL, mNotificationRequest, responseType);
        }
    }

    public void getReferralRequest(Object obj) {
        if (checkConnection()) {
            client.Communicate(REFERRAL_URL, obj, responseType);
        }
    }

    public void getVersionRequest(Object mRequest) {
        if (checkConnection()) {
            client.Communicate(VERSION_SERVICE_URl, mRequest, responseType);
        }
    }


    public void getInHouseData(Object mInHouseRequest) {
        System.out.println("here is the INHOUSE_URL" + " " + INHOUSE_URL);
        if (checkConnection()) {
            client.Communicate(INHOUSE_URL, mInHouseRequest, responseType);
        }
    }

    public void getFCMTopicData(Object mInHouseRequest) {
        if (checkConnection()) {
            client.Communicate(FCM_TOPIC_URL, mInHouseRequest, responseType);
        }
    }

    public void getInAppSuccessData(Object mInAppRequest) {
        if (checkConnection()) {
            client.Communicate(INAPP_URL, mInAppRequest, responseType);
        }
    }

    @Override
    public void onErrorObtained(String errormsg, int responseType) {
        this.response.onErrorObtained(errormsg, responseType);
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    /*public void showProgressDialog() {
        if (isProgressShow) {
            dialog = ProgressDialog.show(contxt.get(), "", "");
        }
    }*/


    /*public String loadJSONFromAsset(Context context, String pathName) {
        PrintLog.print("json return is here" + pathName);
        String json = null;
        try {
            InputStream is = context.getAssets().open(pathName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            PrintLog.print("json retun is here" + json.length());
        } catch (IOException ex) {
            ex.printStackTrace();
            PrintLog.print("json retun is here" + ex);
            return null;
        }
        return json;
    }*/

    private boolean checkConnection() {
        final ConnectivityManager connMgr = (ConnectivityManager) contxt.get().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }

        return false;
    }

}