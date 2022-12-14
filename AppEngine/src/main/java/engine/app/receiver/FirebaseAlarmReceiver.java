package engine.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import engine.app.fcm.FCMController;
import engine.app.fcm.GCMPreferences;
import engine.app.fcm.NotificationUIResponse;
import engine.app.rest.request.DataRequest;
import engine.app.server.v2.DataHubHandler;
import engine.app.socket.EngineApiController;
import engine.app.socket.Response;


/**
 * Created by Meenu Singh on 2019-09-16.
 */
public class FirebaseAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("152 get FirebaseAlarmReceiver.onReceive ");
        GCMPreferences gcmPreferences = new GCMPreferences(context);
        doNotificationRequest(context, gcmPreferences.getFCMNotificationId());
    }

    private void doNotificationRequest(final Context context, String value) {
        DataRequest request = new DataRequest();
        EngineApiController mApiController = new EngineApiController(context,
                new Response() {
                    @Override
                    public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                        new DataHubHandler().parseNotificationData(response.toString(), new DataHubHandler.NotificationListener() {
                            @Override
                            public void pushFCMNotification(String json) {
                                if (json != null)
                                    showFCMNotification(context, json);
                            }
                        });
                    }

                    @Override
                    public void onErrorObtained(String errormsg, int responseType) {
                        System.out.println("response on notification ERROR" + " " + errormsg);
                    }
                }, EngineApiController.NOTIFICATION_ID_CODE);
        mApiController.setNotificationID(value);
        mApiController.getNotificationIDRequest(request);
    }

    private void showFCMNotification(Context context, String response) {
        Gson gson = new Gson();
        NotificationUIResponse notiResponse = gson.fromJson(response, NotificationUIResponse.class);

        if (notiResponse.status.equalsIgnoreCase("0") && notiResponse.type != null) {
            new FCMController(context, notiResponse);
        }
    }

}
