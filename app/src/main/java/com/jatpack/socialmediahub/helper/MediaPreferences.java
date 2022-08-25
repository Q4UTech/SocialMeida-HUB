package com.jatpack.socialmediahub.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jatpack.socialmediahub.model.MediaModel;
import com.jatpack.socialmediahub.util.AppUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MediaPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String SELECTED_POSITION = "_selected_pos";
    private static final String setting_image = "enable_image";
    private static final String media_path = "media_path";
    private static final String KEY_CHECKED = "key_checked";
    private static final String KEY_SELECTED = "key_selected";
    private static final String KEY_SELECTED_NOTIFICATION = "key_selected_notification";
    private static final String TIME_KEY = "time_key";

    private static final String STATUS_KEY = "status_key";
    private static final String STATUS_KEY1 = "status_key1";
    private static final String FILE_URI_KEY = "file_uri_key1";
    private static final String IMAGE_FILE_URI_KEY = "image_file_uri_key";
    private static final String IMAGE_FILE_VALUE = "file_uri_value";
    private static final String DOC_FILE_PATH = "_doc_file_path";
    private static final String IMAGE_FILE_REFRESH = "IMAGE_FILE_REFRESH";
    private static final String DOC_FILE_REFRESH = "_doc_file_refresh";
    private Context context;

    public MediaPreferences(Context context) {
        setPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        editor = getPreferences().edit();
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return preferences;
    }

    private void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public void setgallerycount(int pos) {
        editor.putInt(SELECTED_POSITION, pos);
        editor.commit();
    }

    public int getgallerycount() {
        return getPreferences().getInt(SELECTED_POSITION, 0);
    }

    public void setdownloadedboolean(boolean flag) {
        editor.putBoolean("downloadbool", flag);
        editor.commit();
    }

    public boolean getdownloadedboolean() {
        return getPreferences().getBoolean("downloadbool", false);
    }


    public void setSettingImage(boolean flag) {
        editor.putBoolean(setting_image, flag);
        editor.commit();
    }

    public boolean getSettingImage() {
        return getPreferences().getBoolean(setting_image, true);
    }

    public void setMediaPath(String path) {
        editor.putString(media_path, path);
        editor.commit();
    }

    public String getMediaPath() {
        return getPreferences().getString(media_path, "NA");
    }


    public void setRadioSelected(int value) {
        editor.putInt(KEY_SELECTED, value);
        editor.commit();
    }

    public int getRadioSelected() {
        int value = preferences.getInt(KEY_SELECTED, 1);
        return value;
    }

    public void setRadioAleartpost(int value) {
        editor.putInt(KEY_SELECTED_NOTIFICATION, value);
        editor.commit();
    }

    public int getRadioAleartpost() {
        int value = preferences.getInt(KEY_SELECTED_NOTIFICATION, 0);
        return value;
    }

    public void setNotificationtiming(long value) {
        editor.putLong("notificationtime", value);
        editor.commit();
    }

   /* public long getNotificationtiming() {
        long value = preferences.getLong("notificationtime", NotificationValue.TIMER_3_HOUR);
        return value;
    }*/

    public long getServiceTime() {
        return preferences.getLong(TIME_KEY, AppUtils.View_5);
    }

    public void setServiceTime(long value) {
        editor.putLong(TIME_KEY, value);
        editor.commit();
    }

    public int getStatusFileCount() {
        return preferences.getInt(STATUS_KEY, 0);
    }

    public void setStatusFileCount(int value) {
        editor.putInt(STATUS_KEY, value);
        editor.commit();
    }

    public int getAppNewMediaCount() {
        return preferences.getInt(STATUS_KEY1, 0);
    }

    public void setAppNewMediaCount(int value) {
        editor.putInt(STATUS_KEY1, value);
        editor.commit();
    }


    public int getnewstoryvideo() {
        return preferences.getInt("newstatus", 0);
    }

    public void setnewstoryvideo(int value) {
        editor.putInt("newstatus", value);
        editor.commit();
    }


    public int getnewstoryimage() {
        return preferences.getInt("newstatus_image", 0);
    }

    public void setnewstoryimage(int value) {
        editor.putInt("newstatus_image", value);
        editor.commit();
    }

    public int getallstories() {
        return preferences.getInt("allstatus", 0);
    }



    public void setallstories(int value) {
        editor.putInt("allstatus", value);
        editor.commit();
    }

    public void setLastNotificationTime(long value) {
        editor.putLong("last_notificationtime", value);
        editor.commit();
    }

    public long getLastNotificationTime() {
        long value = preferences.getLong("last_notificationtime", 0);
        return value;
    }


    public void setSelectedRadioPosition(int i){
        editor.putInt("_position_",i);
        editor.commit();
    }
    public int getSelectedRadionPosition(){
        return preferences.getInt("_position_",0);
    }



    public void setVideoplayDelete(int i){
        editor.putInt("_position_delete",i);
        editor.commit();
    }
    public int getVideoplayDelete(){
        return preferences.getInt("_position_delete",0);
    }

    public void setgalleryvideocountbool(boolean flag) {
        editor.putBoolean("gallerybool", flag);
        editor.commit();
    }

    public boolean getgalleryvideocountbool() {
        return getPreferences().getBoolean("gallerybool", false);
    }

    public void setgalleryimagecountbool(boolean flag) {
        editor.putBoolean("gallerybool_image", flag);
        editor.commit();
    }

    public boolean getgalleryimagecountbool() {
        return getPreferences().getBoolean("gallerybool_image", false);
    }


    // for 11.
    public void setFileUri(String f_uri){
        editor.putString(IMAGE_FILE_URI_KEY, f_uri);
        System.out.println("Uri of file: "+f_uri);
        editor.commit();
    }

    public String getFileUri(){
        return getPreferences().getString(IMAGE_FILE_URI_KEY, "NA");
    }

    public void setFileImage(boolean isFirst) {
        editor.putBoolean(IMAGE_FILE_VALUE, isFirst);
        Log.d("TAG", "setFileImage: ksjDKLJKLd"+isFirst);
        editor.commit();
    }

    public boolean getFileImage() {
        return getPreferences().getBoolean(IMAGE_FILE_VALUE, false);
    }
    public void setoverlaypermission(boolean flag) {
        editor.putBoolean("overlaypermission", flag);
        editor.commit();
    }

    public boolean isOverlayPermission() {
        return preferences.getBoolean("OverlayPermission", false);
    }

    public boolean isSkipPermission() {
        return preferences.getBoolean("isSkipPermission", false);
    }

    public void setSkipPermission(boolean flag) {
        editor.putBoolean("isSkipPermission", flag);
        editor.commit();
    }


    public String getDocumetFilePath() {
        return getPreferences().getString(DOC_FILE_PATH, "NA");
    }

    public void setDocumetFilePath(String path) {
        editor.putString(DOC_FILE_PATH, path);
        editor.commit();
    }



    public void setStatusArraylist(ArrayList<MediaModel> list) {
        Gson gson = new Gson();
         String json= gson.toJson(list);
        editor.putString("KEY_ADDRESS_LIST", json);
        editor.commit();
    }

    public ArrayList<MediaModel> getStatusArraylist() {
        Gson gson = new Gson();
         String json = preferences.getString("KEY_ADDRESS_LIST", null);
          Type type=  new TypeToken<ArrayList<MediaModel>> () {}.getType();
        return gson.fromJson(json, type);
    }

    public boolean getRefresh() {
        return getPreferences().getBoolean(DOC_FILE_REFRESH, false);
    }

    public void setRefresh(boolean isRefresh) {
        editor.putBoolean(DOC_FILE_REFRESH, isRefresh);
        editor.commit();
    }


    public boolean getRefreshImages() {
        return getPreferences().getBoolean(IMAGE_FILE_REFRESH, false);
    }

    public void setRefreshImages(boolean isRefresh) {
        editor.putBoolean(IMAGE_FILE_REFRESH, isRefresh);
        editor.commit();
    }
}

