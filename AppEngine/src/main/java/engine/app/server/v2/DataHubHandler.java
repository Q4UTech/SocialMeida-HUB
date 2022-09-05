package engine.app.server.v2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import engine.app.PrintLog;
import engine.app.ecrypt.MCrypt;
import engine.app.fcm.GCMPreferences;
import engine.app.fcm.ServerResponse;
import engine.app.listener.onParseDefaultValueListener;
import engine.app.rest.response.DataResponse;
import engine.app.rest.rest_utils.RestUtils;
import engine.app.server.v4.AdsProviders;


/**
 * Created by hp on 9/20/2017.
 */
public final class DataHubHandler {

    private final Gson gson;
    private final MCrypt mCrypt;
   // private boolean isFromDefaultvalue=false;
    public DataHubHandler() {
        gson = new Gson();
        mCrypt = new MCrypt();
    }


    /**
     * for parsing the actual encrypted inHouse service response
     *
     * @param context  context of the application
     * @param response encrypted response contained inside "data" tag
     */
    public void parseInHouseService(Context context, String response, InHouseCallBack l) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                parseDecryptInHouse(context, dResponse, l);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void parseDecryptInHouse(Context context, String response, InHouseCallBack l) {
        InHouseResponse vResp;
        try {
            if (response != null && !response.equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                vResp = gson.fromJson(response, InHouseResponse.class);

                if (vResp != null) {
                    if (vResp.message.equalsIgnoreCase(DataHubConstant.KEY_SUCESS)) {
                        if (vResp.inhouseresponse != null) {
                            l.onInhouseDownload(vResp.inhouseresponse);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * for parsing the actual encrypted version service response
     *
     * @param context  context of the application
     * @param response encrypted response contained inside "data" tag
     * @param mRl      a custom listener(call by value to func()-parseDecryptVersionData)
     */
    public void parseVersionData(Context context, String response, MasterRequestListener mRl) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print("parsing Version data encrypt" + " " + dataResponse.data);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing Version data decrypt value" + " " + dResponse);

                parseDecryptVersionData(context, dResponse, mRl);

            } catch (Exception e) {
                PrintLog.print("exception version response" + " " + e);
                parseMasterData(context, new DataHubPreference(context).getAdsResponse(),null);
            }


        } else {
            parseMasterData(context, new DataHubPreference(context).getAdsResponse(),null);
        }
    }


    /**
     * for parsing the actual json obtained from decrypting the data tag of version service
     *
     * @param context  context of the application
     * @param response actual json response of version service.
     * @param mRL      a custom listener for calling master service in EngineHandler.java class
     */
    private void parseDecryptVersionData(Context context, String response, MasterRequestListener mRL) {

        VersionResponse vResp;
        DataHubConstant mConstant = new DataHubConstant(context);
        DataHubPreference preference = new DataHubPreference(context);
        try {
            if (response != null && !response.equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                vResp = gson.fromJson(response, VersionResponse.class);

                if (vResp != null) {
                    if (vResp.message.equalsIgnoreCase(DataHubConstant.KEY_SUCESS)) {
                        if (preference.getDataHubVersion().equalsIgnoreCase(vResp.app_status)) {
                            if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                                parseMasterData(context, preference.getAdsResponse(),null);
                            } else {
                                mRL.callMasterService();
                            }
                        } else {
                            // call master service
                            mRL.callMasterService();
                        }

                    } else {
                        if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                            parseMasterData(context, preference.getAdsResponse(),null);
                        } else {
                            parseMasterData(context, mConstant.parseAssetData(),null);
                        }
                    }
                } else {
                    if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                        parseMasterData(context, preference.getAdsResponse(),null);
                    } else {
                        parseMasterData(context, mConstant.parseAssetData(),null);
                    }
                }

            }
        } catch (Exception e) {
            PrintLog.print("Exception version parsing decrypt" + " " + e);
            parseMasterData(context, preference.getAdsResponse(),null);
        }
    }

    /**
     * for parsing the encrypted master service response
     *
     * @param context  context of the application
     * @param response encrypted response contained inside "data" object
     */
    public void parseMasterData(Context context, String response, onParseDefaultValueListener onParseDefaultValueListener) {

        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print(" NewEngine parsing Master data encrypt" + " " + dataResponse.data);


            try {

                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing Master data decrypt value" + " " + dResponse);

                parseDecryptMasterData(context, dResponse, response , onParseDefaultValueListener);

            } catch (Exception e) {
                PrintLog.print("exception decryption" + " " + e);
                e.printStackTrace();
                parseMasterData(context, new DataHubPreference(context).getAdsResponse(), onParseDefaultValueListener);
            }
        } else {
            parseMasterData(context, new DataHubPreference(context).getAdsResponse(), onParseDefaultValueListener);
        }
    }


    /**
     * for parsing the actual json obtained from decrypting the data tag of master service
     *
     * @param context    context of the application
     * @param response   actual json response of master service, to be used to parsing and initializing Slave class
     * @param encryptKey encrypted response from server, to be stored in preferences for exception handling
     */
    private void parseDecryptMasterData(Context context, String response, String encryptKey, onParseDefaultValueListener onParseDefaultValueListener ) {

        DataHubPreference preference = new DataHubPreference(context);

        try {
            if (response != null && response.length() > 100) {
                DataHubResponse hubResponse;

                if (!response.equalsIgnoreCase(DataHubConstant.KEY_NA)) {

                    hubResponse = gson.fromJson(response, DataHubResponse.class);

                    if (hubResponse != null && hubResponse.message.equalsIgnoreCase(DataHubConstant.KEY_SUCESS)) {
                        if (hubResponse.adsresponse != null && hubResponse.adsresponse.size() > 0) {

                            ArrayList<AdsResponse> adsResponses = new ArrayList<>(hubResponse.adsresponse);

                            if (adsResponses.size() > 0) {
                                loadSlaveAdResponse(adsResponses,onParseDefaultValueListener);

                                //InAppHandler handler = new InAppHandler(context, Slave.INAPP_PUBLIC_KEY);
                                //handler.initializeBilling();
                            }

                        }


                        if (hubResponse.moreFeatures != null && hubResponse.moreFeatures.size() > 0) {
                            ArrayList<MoreFeature> moreFeatures = new ArrayList<>(hubResponse.moreFeatures);
                            if (moreFeatures.size() > 0) {
                                loadMoreFeatureData(moreFeatures);
                            }
                        }

                        if (hubResponse.gameProvidersResponceArrayList != null && hubResponse.gameProvidersResponceArrayList.size() > 0) {

                            Log.d("DataHubHandler", "Hello parseDecryptMasterData 0012 game provider aa" + " " + hubResponse.gameProvidersResponceArrayList.size());

                            ArrayList<GameProvidersResponce> gameresponce = new ArrayList<>(hubResponse.gameProvidersResponceArrayList);
                            if (gameresponce.size() > 0) {
                                loadGameFeatureV2(gameresponce);
                            }

                        }

//                        if (hubResponse.gameservice != null) {
//                            PrintLog.print("0555 checking game");
//                            loadSlaveGame(hubResponse.gameservice);
//                        }

                        if (hubResponse.cp != null) {
                            loadSlaveCP(hubResponse.cp);
                        }

                        if (hubResponse.cp != null) {
                            loadSlaveCP(hubResponse.cp);
                        }
                        Log.d("hello test ads load", "Hello onparsingDefault navigation 009898"+" "+hubResponse.update_key);

                        preference.setDataHubVersion(hubResponse.update_key);
                        preference.setAdsResponse(encryptKey);
                        /*
                         *  preference.setJson(response) : is called just to save the response json to display it on print activity.
                         *  No other purpose of this function.
                         */
                        preference.setJSON(response);

                        if(onParseDefaultValueListener!=null) {

                            Log.d("DataHubHandler", "Hello parseDecryptMasterData parseDefaultValueListener");

                         //   if(!isFromDefaultvalue){
                          //      isFromDefaultvalue=true;
                                onParseDefaultValueListener.onParsingCompleted();
                                Log.d("DataHubHandler", "Hello parseDecryptMasterData parseDefaultValueListener");
                         //   }
                        }else {
                            Log.d("DataHubHandler", "Hello parseDecryptMasterData parseDefaultValueListener .,l "+onParseDefaultValueListener);

                        }
                    } else {
                        parseMasterData(context, preference.getAdsResponse(),null);
                    }
                } else {
                    parseMasterData(context, preference.getAdsResponse(),null);
                }
            } else {
                parseMasterData(context, preference.getAdsResponse(),null);
            }


        } catch (Exception e) {
            PrintLog.print(" Enginev2 Exception get ad data" + " " + e);
            parseMasterData(context, preference.getAdsResponse(),null);
        }
    }

    public void parseFCMData(Context context, String response) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print("parsing FCM data encrypt " + dataResponse.data);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing FCM data decrypt value " + dResponse);

                parseDecryptFCMData(context, dResponse);

            } catch (Exception e) {
                new GCMPreferences(context).setGCMRegister(false);
            }
        }
    }

    private void parseDecryptFCMData(Context context, String response) {

        if (response != null) {
            GCMPreferences preferences = new GCMPreferences(context);
            ServerResponse gcmResponse = gson.fromJson(response, ServerResponse.class);
            PrintLog.print("response GCM OK softwareupdate.dailyuseapps.receiver" + " " + gcmResponse.status + " " + gcmResponse.message + " " + gcmResponse.reqvalue);

            if (gcmResponse.status.equals("0")) {
                RestUtils.saveAllGCMValue(context);
                preferences.setGCMRegister(true);
                preferences.setVirtualGCMID(gcmResponse.reqvalue);
                preferences.setGCMID("NA");
            } else {
                preferences.setGCMRegister(false);
            }

        }

    }


    public void parseNotificationData(String response, NotificationListener l) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print("parsing Notification data encrypt" + " " + dataResponse.data);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing Notification data decrypt value" + " " + dResponse);

                l.pushFCMNotification(dResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void parseReferalData(Context context, String response) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print("parsing FCM data encrypt " + dataResponse.data);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing FCM data decrypt value " + dResponse);

                parseDecryptReferalData(context, dResponse);

            } catch (Exception e) {
                new GCMPreferences(context).setReferalRegister(false);
            }
        }
    }

    private void parseDecryptReferalData(Context context, String response) {


        if (response != null) {
            GCMPreferences preferences = new GCMPreferences(context);

            ServerResponse gcmResponse = gson.fromJson(response, ServerResponse.class);
            PrintLog.print("response referal OK app launch" + " " + gcmResponse.status + " " + gcmResponse.status + " " + gcmResponse.reqvalue);
            if (gcmResponse.status.equals("0")) {
                preferences.setReferalRegister(true);
            } else {
                preferences.setReferalRegister(false);
            }
        }

    }

    public void parseFCMTopicData(String response, NotificationListener l) {
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);

            PrintLog.print("parsing FCMTopicData data encrypt" + " " + dataResponse.data);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing FCMTopicData data decrypt value" + " " + dResponse);

                l.pushFCMNotification(dResponse);
            } catch (Exception e) {
                PrintLog.print("parsing FCMTopicData Exception " + " " + e.getMessage());
            }
        }
    }

    private void loadSlaveAdResponse(List<AdsResponse> list,onParseDefaultValueListener onParseDefaultValueListener) {

        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_TOP_BANNER)) {
                    PrintLog.print("0555 checking Type Top Banner");
                    Slave.TOP_BANNER_clicklink = list.get(i).clicklink;
                    Slave.TOP_BANNER_start_date = list.get(i).start_date;
                    Slave.TOP_BANNER_nevigation = list.get(i).navigation;
                    Slave.TOP_BANNER_call_native = list.get(i).call_native;
                    Slave.TOP_BANNER_rateapptext = list.get(i).rateapptext;
                    Slave.TOP_BANNER_rateurl = list.get(i).rateurl;
                    Slave.TOP_BANNER_email = list.get(i).email;
                    Slave.TOP_BANNER_updateTYPE = list.get(i).updatetype;
                    Slave.TOP_BANNER_appurl = list.get(i).appurl;
                    Slave.TOP_BANNER_prompttext = list.get(i).prompttext;
                    Slave.TOP_BANNER_version = list.get(i).version;
                    Slave.TOP_BANNER_moreurl = list.get(i).moreurl;
                    Slave.TOP_BANNER_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.TOP_BANNER_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_BOTTOM_BANNER)) {
                    Slave.BOTTOM_BANNER_clicklink = list.get(i).clicklink;
                    Slave.BOTTOM_BANNER_start_date = list.get(i).start_date;
                    Slave.BOTTOM_BANNER_nevigation = list.get(i).navigation;
                    Slave.BOTTOM_BANNER_call_native = list.get(i).call_native;
                    Slave.BOTTOM_BANNER_rateapptext = list.get(i).rateapptext;
                    Slave.BOTTOM_BANNER_rateurl = list.get(i).rateurl;
                    Slave.BOTTOM_BANNER_email = list.get(i).email;
                    Slave.BOTTOM_BANNER_updateTYPE = list.get(i).updatetype;
                    Slave.BOTTOM_BANNER_appurl = list.get(i).appurl;
                    Slave.BOTTOM_BANNER_prompttext = list.get(i).prompttext;
                    Slave.BOTTOM_BANNER_version = list.get(i).version;
                    Slave.BOTTOM_BANNER_moreurl = list.get(i).moreurl;
                    Slave.BOTTOM_BANNER_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.BOTTOM_BANNER_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_BANNER_LARGE)) {
                    Slave.LARGE_BANNER_clicklink = list.get(i).clicklink;
                    Slave.LARGE_BANNER_start_date = list.get(i).start_date;
                    Slave.LARGE_BANNER_nevigation = list.get(i).navigation;
                    Slave.LARGE_BANNER_call_native = list.get(i).call_native;
                    Slave.LARGE_BANNER_rateapptext = list.get(i).rateapptext;
                    Slave.LARGE_BANNER_rateurl = list.get(i).rateurl;
                    Slave.LARGE_BANNER_email = list.get(i).email;
                    Slave.LARGE_BANNER_updateTYPE = list.get(i).updatetype;
                    Slave.LARGE_BANNER_appurl = list.get(i).appurl;
                    Slave.LARGE_BANNER_prompttext = list.get(i).prompttext;
                    Slave.LARGE_BANNER_version = list.get(i).version;
                    Slave.LARGE_BANNER_moreurl = list.get(i).moreurl;
                    Slave.LARGE_BANNER_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.LARGE_BANNER_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_BANNER_RECTANGLE)) {
                    Slave.RECTANGLE_BANNER_clicklink = list.get(i).clicklink;
                    Slave.RECTANGLE_BANNER_start_date = list.get(i).start_date;
                    Slave.RECTANGLE_BANNER_nevigation = list.get(i).navigation;
                    Slave.RECTANGLE_BANNER_call_native = list.get(i).call_native;
                    Slave.RECTANGLE_BANNER_rateapptext = list.get(i).rateapptext;
                    Slave.RECTANGLE_BANNER_rateurl = list.get(i).rateurl;
                    Slave.RECTANGLE_BANNER_email = list.get(i).email;
                    Slave.RECTANGLE_BANNER_updateTYPE = list.get(i).updatetype;
                    Slave.RECTANGLE_BANNER_appurl = list.get(i).appurl;
                    Slave.RECTANGLE_BANNER_prompttext = list.get(i).prompttext;
                    Slave.RECTANGLE_BANNER_version = list.get(i).version;
                    Slave.RECTANGLE_BANNER_moreurl = list.get(i).moreurl;
                    Slave.RECTANGLE_BANNER_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.RECTANGLE_BANNER_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_FULL_ADS)) {
                    PrintLog.print("0555 checking Type Full Ads");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_FULL_ADS value is " + list.get(i));
                    Slave.FULL_ADS_clicklink = list.get(i).clicklink;
                    Slave.FULL_ADS_start_date = list.get(i).start_date;
                    Slave.FULL_ADS_nevigation = list.get(i).navigation;
                    Slave.FULL_ADS_call_native = list.get(i).call_native;
                    Slave.FULL_ADS_rateapptext = list.get(i).rateapptext;
                    Slave.FULL_ADS_rateurl = list.get(i).rateurl;
                    Slave.FULL_ADS_email = list.get(i).email;
                    Slave.FULL_ADS_updateTYPE = list.get(i).updatetype;
                    Slave.FULL_ADS_appurl = list.get(i).appurl;
                    Slave.FULL_ADS_prompttext = list.get(i).prompttext;
                    Slave.FULL_ADS_version = list.get(i).version;
                    Slave.FULL_ADS_moreurl = list.get(i).moreurl;
                    Slave.FULL_ADS_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.FULL_ADS_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_LAUNCH_FULL_ADS)) {
                    PrintLog.print("NewEngine 0555 checking Type Top Launch Full Ads"+onParseDefaultValueListener);

                    if(onParseDefaultValueListener!=null){
                        PrintLog.print(" Enginev2 Parging tag Slave.TYPE_LAUNCH_FULL_ADS value is " + list.get(i).src);
                        Slave.LAUNCH_FULL_ADS_clicklink = list.get(i).clicklink;
                        Slave.LAUNCH_FULL_ADS_start_date = list.get(i).start_date;
                        Slave.LAUNCH_FULL_ADS_nevigation = list.get(i).navigation;
                        Slave.LAUNCH_FULL_ADS_call_native = list.get(i).call_native;
                        Slave.LAUNCH_FULL_ADS_rateapptext = list.get(i).rateapptext;
                        Slave.LAUNCH_FULL_ADS_rateurl = list.get(i).rateurl;
                        Slave.LAUNCH_FULL_ADS_email = list.get(i).email;
                        Slave.LAUNCH_FULL_ADS_updateTYPE = list.get(i).updatetype;
                        Slave.LAUNCH_FULL_ADS_appurl = list.get(i).appurl;
                        Slave.LAUNCH_FULL_ADS_prompttext = list.get(i).prompttext;
                        Slave.LAUNCH_FULL_ADS_version = list.get(i).version;
                        Slave.LAUNCH_FULL_ADS_moreurl = list.get(i).moreurl;
                        Slave.LAUNCH_FULL_ADS_src = list.get(i).src;
                        Slave.LAUNCH_FULL_ADS_show_after = list.get(i).show_after;

                        if (list.get(i).providers != null) {
                            List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                            if (providers.size() > 0) {
                                Slave.LAUNCH_FULL_ADS_providers = providers;
                            }
                        }
                    }


                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_EXIT_FULL_ADS)) {
                    PrintLog.print("0555 checking Type FULL ADS");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_EXIT_FULL_ADS value is " + list.get(i));
                    Slave.EXIT_FULL_ADS_clicklink = list.get(i).clicklink;
                    Slave.EXIT_FULL_ADS_start_date = list.get(i).start_date;
                    Slave.EXIT_FULL_ADS_nevigation = list.get(i).navigation;
                    Slave.EXIT_FULL_ADS_call_native = list.get(i).call_native;
                    Slave.EXIT_FULL_ADS_rateapptext = list.get(i).rateapptext;
                    Slave.EXIT_FULL_ADS_rateurl = list.get(i).rateurl;
                    Slave.EXIT_FULL_ADS_email = list.get(i).email;
                    Slave.EXIT_FULL_ADS_updateTYPE = list.get(i).updatetype;
                    Slave.EXIT_FULL_ADS_appurl = list.get(i).appurl;
                    Slave.EXIT_FULL_ADS_prompttext = list.get(i).prompttext;
                    Slave.EXIT_FULL_ADS_version = list.get(i).version;
                    Slave.EXIT_FULL_ADS_moreurl = list.get(i).moreurl;
                    Slave.EXIT_FULL_ADS_src = list.get(i).src;
                    Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT = list.get(i).show_ad_on_exit_prompt;
                    Slave.EXIT_SHOW_NATIVE_AD_ON_EXIT_PROMPT = list.get(i).show_native_ad_on_exit_prompt;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.EXIT_FULL_ADS_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_NATIVE_MEDIUM)) {
                    PrintLog.print("0555 checking Type NATIVE MEDIUM");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_NATIVE_MEDIUM value is " + list.get(i).ad_id);
                    Slave.NATIVE_MEDIUM_clicklink = list.get(i).clicklink;
                    Slave.NATIVE_MEDIUM_start_date = list.get(i).start_date;
                    Slave.NATIVE_MEDIUM_nevigation = list.get(i).navigation;
                    Slave.NATIVE_MEDIUM_call_native = list.get(i).call_native;
                    Slave.NATIVE_MEDIUM_rateapptext = list.get(i).rateapptext;
                    Slave.NATIVE_MEDIUM_rateurl = list.get(i).rateurl;
                    Slave.NATIVE_MEDIUM_email = list.get(i).email;
                    Slave.NATIVE_MEDIUM_updateTYPE = list.get(i).updatetype;
                    Slave.NATIVE_MEDIUM_appurl = list.get(i).appurl;
                    Slave.NATIVE_MEDIUM_prompttext = list.get(i).prompttext;
                    Slave.NATIVE_MEDIUM_version = list.get(i).version;
                    Slave.NATIVE_MEDIUM_moreurl = list.get(i).moreurl;
                    Slave.NATIVE_MEDIUM_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.NATIVE_MEDIUM_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_NATIVE_LARGE)) {
                    PrintLog.print("0555 checking Type NATIVE LARGE");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_NATIVE_LARGE value is " + list.get(i));
                    Slave.NATIVE_LARGE_clicklink = list.get(i).clicklink;
                    Slave.NATIVE_LARGE_start_date = list.get(i).start_date;
                    Slave.NATIVE_LARGE_nevigation = list.get(i).navigation;
                    Slave.NATIVE_LARGE_call_native = list.get(i).call_native;
                    Slave.NATIVE_LARGE_rateapptext = list.get(i).rateapptext;
                    Slave.NATIVE_LARGE_rateurl = list.get(i).rateurl;
                    Slave.NATIVE_LARGE_email = list.get(i).email;
                    Slave.NATIVE_LARGE_updateTYPE = list.get(i).updatetype;
                    Slave.NATIVE_LARGE_appurl = list.get(i).appurl;
                    Slave.NATIVE_LARGE_prompttext = list.get(i).prompttext;
                    Slave.NATIVE_LARGE_version = list.get(i).version;
                    Slave.NATIVE_LARGE_moreurl = list.get(i).moreurl;
                    Slave.NATIVE_LARGE_src = list.get(i).src;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.NATIVE_LARGE_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_REWARDED_VIDEO)) {
                    Slave.REWARDED_VIDEO_status = list.get(i).ads_status;
                    Slave.REWARDED_VIDEO_start_date = list.get(i).start_date;
                    Slave.REWARDED_VIDEO_nevigation = list.get(i).navigation;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.REWARDED_VIDEO_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_SUGGESTED_ADS)) {
                    Slave.SUGGESTED_ADS_call_native = list.get(i).call_native;
                    Slave.SUGGESTED_ADS_start_date = list.get(i).start_date;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.SUGGESTED_ADS_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_APP_OPEN_ADS)) {
                    Slave.APP_OPEN_ADS_status = list.get(i).ads_status;
                    Slave.APP_OPEN_ADS_start_date = list.get(i).start_date;
                    Slave.APP_OPEN_ADS_nevigation = list.get(i).navigation;

                    if (list.get(i).providers != null) {
                        List<AdsProviders> providers = new ArrayList<>(list.get(i).providers);
                        if (providers.size() > 0) {
                            Slave.APP_OPEN_ADS_providers = providers;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_RATE_APP)) {
                    PrintLog.print("0555 checking Type RATE APP");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_RATE_APP value is " + list.get(i).rateapptext);
                    Slave.RATE_APP_ad_id = list.get(i).ad_id;
                    Slave.RATE_APP_provider_id = list.get(i).provider_id;
                    Slave.RATE_APP_clicklink = list.get(i).clicklink;
                    Slave.RATE_APP_start_date = list.get(i).start_date;
                    Slave.RATE_APP_nevigation = list.get(i).navigation;
                    Slave.RATE_APP_call_native = list.get(i).call_native;
                    Slave.RATE_APP_rateapptext = list.get(i).rateapptext;
                    Slave.RATE_APP_rateurl = list.get(i).rateurl;
                    Slave.RATE_APP_email = list.get(i).email;
                    Slave.RATE_APP_updateTYPE = list.get(i).updatetype;
                    Slave.RATE_APP_appurl = list.get(i).appurl;
                    Slave.RATE_APP_prompttext = list.get(i).prompttext;
                    Slave.RATE_APP_version = list.get(i).version;
                    Slave.RATE_APP_moreurl = list.get(i).moreurl;
                    Slave.RATE_APP_src = list.get(i).src;

                    Slave.RATE_APP_BG_COLOR = list.get(i).bgcolor;
                    Slave.RATE_APP_HEADER_TEXT = list.get(i).headertext;
                    Slave.RATE_APP_TEXT_COLOR = list.get(i).textcolor;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_FEEDBACK)) {
                    PrintLog.print("0555 checking Type FEEDBACK");
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_FEEDBACK value is " + list.get(i));
                    Slave.FEEDBACK_ad_id = list.get(i).ad_id;
                    Slave.FEEDBACK_provider_id = list.get(i).provider_id;
                    Slave.FEEDBACK_clicklink = list.get(i).clicklink;
                    Slave.FEEDBACK_start_date = list.get(i).start_date;
                    Slave.FEEDBACK_nevigation = list.get(i).navigation;
                    Slave.FEEDBACK_call_native = list.get(i).call_native;
                    Slave.FEEDBACK_rateapptext = list.get(i).rateapptext;
                    Slave.FEEDBACK_rateurl = list.get(i).rateurl;
                    Slave.FEEDBACK_email = list.get(i).email;
                    Slave.FEEDBACK_updateTYPE = list.get(i).updatetype;
                    Slave.FEEDBACK_appurl = list.get(i).appurl;
                    Slave.FEEDBACK_prompttext = list.get(i).prompttext;
                    Slave.FEEDBACK_version = list.get(i).version;
                    Slave.FEEDBACK_moreurl = list.get(i).moreurl;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_UPDATES)) {
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_UPDATES value is " + list.get(i).updatetype
                            + " " + list.get(i).appurl);
                    Slave.UPDATES_ad_id = list.get(i).ad_id;
                    Slave.UPDATES_provider_id = list.get(i).provider_id;
                    Slave.UPDATES_clicklink = list.get(i).clicklink;
                    Slave.UPDATES_start_date = list.get(i).start_date;
                    Slave.UPDATES_nevigation = list.get(i).navigation;
                    Slave.UPDATES_call_native = list.get(i).call_native;
                    Slave.UPDATES_rateapptext = list.get(i).rateapptext;
                    Slave.UPDATES_rateurl = list.get(i).rateurl;
                    Slave.UPDATES_email = list.get(i).email;
                    Slave.UPDATES_updateTYPE = list.get(i).updatetype;
                    Slave.UPDATES_appurl = list.get(i).appurl;
                    Slave.UPDATES_prompttext = list.get(i).prompttext;
                    Slave.UPDATES_version = list.get(i).version;
                    Slave.UPDATES_moreurl = list.get(i).moreurl;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_MORE_APPS)) {
                    PrintLog.print(" Enginev2 Parging tag Slave.TYPE_MORE_APPS value is " + list.get(i));
                    Slave.MOREAPP_ad_id = list.get(i).ad_id;
                    Slave.MOREAPP_provider_id = list.get(i).provider_id;
                    Slave.MOREAPP_clicklink = list.get(i).clicklink;
                    Slave.MOREAPP_start_date = list.get(i).start_date;
                    Slave.MOREAPP_nevigation = list.get(i).navigation;
                    Slave.MOREAPP_call_native = list.get(i).call_native;
                    Slave.MOREAPP_rateapptext = list.get(i).rateapptext;
                    Slave.MOREAPP_rateurl = list.get(i).rateurl;
                    Slave.MOREAPP_email = list.get(i).email;
                    Slave.MOREAPP_updateTYPE = list.get(i).updatetype;
                    Slave.MOREAPP_appurl = list.get(i).appurl;
                    Slave.MOREAPP_prompttext = list.get(i).prompttext;
                    Slave.MOREAPP_version = list.get(i).version;
                    Slave.MOREAPP_moreurl = list.get(i).moreurl;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_ETC)) {
                    PrintLog.print("0555 checking Type ETC");
                    Slave.ETC_1 = list.get(i).etc1;
                    Slave.ETC_2 = list.get(i).etc2;
                    Slave.ETC_3 = list.get(i).etc3;
                    Slave.ETC_4 = list.get(i).etc4;
                    Slave.ETC_5 = list.get(i).etc5;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_SHARE)) {
                    PrintLog.print("0555 checking Type SHARE");
                    Slave.SHARE_TEXT = list.get(i).sharetext;
                    Slave.SHARE_URL = list.get(i).shareurl;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_ADMOB_STATIC)) {
                    PrintLog.print("0555 checking Type ADMOB STATIC ");
                    PrintLog.print("1110 here is am ");
                    Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC = list.get(i).admob_native_medium_id;
                    Slave.ADMOB_BANNER_ID_STATIC = list.get(i).admob_banner_id;
                    Slave.ADMOB_FULL_ID_STATIC = list.get(i).admob_full_id;
                    Slave.ADMOB_NATIVE_LARGE_ID_STATIC = list.get(i).admob_native_large_id;
                    Slave.ADMOB_BANNER_ID_LARGE_STATIC = list.get(i).admob_bannerlarge_id;
                    Slave.ADMOB_BANNER_ID_RECTANGLE_STATIC = list.get(i).admob_bannerrect_id;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_REMOVE_ADS)) {
                    Slave.REMOVE_ADS_DESCRIPTION = list.get(i).description;
                    Slave.REMOVE_ADS_BGCOLOR = list.get(i).bgcolor;
                    Slave.REMOVE_ADS_TEXTCOLOR = list.get(i).textcolor;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_ABOUT_DETAILS)) {
                    Slave.ABOUTDETAIL_DESCRIPTION = list.get(i).description;
                    Slave.ABOUTDETAIL_OURAPP = list.get(i).ourapp;
                    Slave.ABOUTDETAIL_WEBSITELINK = list.get(i).websitelink;
                    Slave.ABOUTDETAIL_PRIVACYPOLICY = list.get(i).ppolicy;
                    Slave.ABOUTDETAIL_TERM_AND_COND = list.get(i).tandc;
                    Slave.ABOUTDETAIL_FACEBOOK = list.get(i).facebook;
                    Slave.ABOUTDETAIL_INSTA = list.get(i).instagram;
                    Slave.ABOUTDETAIL_TWITTER = list.get(i).twitter;
                    Slave.ABOUTDETAIL_FAQ = list.get(i).faq;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_EXIT_NON_REPEAT)) {

                    if (list.get(i).counts != null) {
                        ArrayList<NonRepeatCount> counts = new ArrayList<>(list.get(i).counts);
                        if (counts.size() > 0) {
                            Slave.EXIT_NON_REPEAT_COUNT = counts;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_EXIT_REPEAT)) {
                    Slave.EXIT_REPEAT_RATE = list.get(i).rate;
                    Slave.EXIT_REPEAT_EXIT = list.get(i).exit;
                    Slave.EXIT_REPEAT_FULL_ADS = list.get(i).full;
                    Slave.EXIT_REPEAT_REMOVEADS = list.get(i).removeads;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_LAUNCH_NON_REPEAT)) {

                    if (list.get(i).launch_counts != null) {
                        ArrayList<LaunchNonRepeatCount> launch_counts = new ArrayList<>(list.get(i).launch_counts);
                        if (launch_counts.size() > 0) {
                            Slave.LAUNCH_NON_REPEAT_COUNT = launch_counts;
                        }
                    }

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_LAUNCH_REPEAT)) {
                    Slave.LAUNCH_REPEAT_RATE = list.get(i).launch_rate;
                    Slave.LAUNCH_REPEAT_EXIT = list.get(i).launch_exit;
                    Slave.LAUNCH_REPEAT_FULL_ADS = list.get(i).launch_full;
                    Slave.LAUNCH_REPEAT_REMOVEADS = list.get(i).launch_removeads;

                } else if (list.get(i).type.equalsIgnoreCase(Slave.TYPE_INAPP_BILLING)) {
                    Slave.INAPP_PUBLIC_KEY = list.get(i).public_key;

                    if (list.get(i).billing != null) {
                        ArrayList<Billing> billing = new ArrayList<>(list.get(i).billing);
                        if (billing.size() > 0) {
                            BillingResponseHandler.getInstance().setBillingResponse(list.get(i).billing);
                        }
                    }
                }
            }



        }
        //  new AHandler().manageAppLaunch(context);

    }



    private void loadSlaveCP(DataHubCP cp) {
        if (cp != null) {
            Slave.CP_cpname = cp.cpname;
            Slave.CP_navigation_count = cp.navigation_count;
            Slave.CP_is_start = cp.is_start;
            Slave.CP_is_exit = cp.is_exit;
            Slave.CP_startday = cp.startday;
            Slave.CP_package_name = cp.package_name;
            Slave.CP_camp_img = cp.camp_img;
            Slave.CP_camp_click_link = cp.camp_click_link;
            PrintLog.print(" Enginev2 Parging tag Slave.CP  cp.cpname " + cp.cpname + " Slave.CP_camp_click_link " + Slave.CP_camp_click_link);

        }
    }

    private void loadMoreFeatureData(ArrayList<MoreFeature> moreFeatures) {
        MoreFeatureResponseHandler.getInstance().setMoreFeaturesListResponse(moreFeatures);
    }

    public interface InHouseCallBack {
        void onInhouseDownload(InHouse inHouse);
    }


    public interface MasterRequestListener {
        void callMasterService();
    }

    public interface NotificationListener {
        void pushFCMNotification(String json);
    }

    private void loadGameFeatureV2(ArrayList<GameProvidersResponce> gameProvidersResponces) {
        GameServiceV2ResponseHandler.getInstance().setGameV2FeaturesListResponse(gameProvidersResponces);
    }

}
