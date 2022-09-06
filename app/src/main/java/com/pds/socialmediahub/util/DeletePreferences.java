package com.pds.socialmediahub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Anon on 06,August,2019
 */
public class DeletePreferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DeletePreferences(Context context) {
        Log.d("DeletePreferences", "Test DeletePreferences..."+context+"  ");
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


    // will not save media
    public boolean isWhatsAppMediaBackUp() {
        return getPreferences().getBoolean("allow_backup", true);
    }

    // will not save media
    public void allowWhatsAppMediaBackUp(boolean flag) {
        editor.putBoolean("allow_backup", flag);
        editor.commit();
    }

    // will not save chats
    public boolean isChatDeleteServiceEnabled() {
        return getPreferences().getBoolean("_chat_delete", true);
    }

    // will not save chats
    public void enableChatDeleteService(boolean flag) {
        editor.putBoolean("_chat_delete", flag);
        editor.commit();
    }

    // will not notify on message delete
    public boolean isNotifyOnChatDelete() {
        return getPreferences().getBoolean("_delete_notification_chat", true);
    }

    // will not notify on message delete
    public void allowNotificationOnChatDelete(boolean flag) {
        editor.putBoolean("_delete_notification_chat", flag);
        editor.commit();
    }

    public boolean isNotifyOnMediaDelete() {
        return getPreferences().getBoolean("_delete_notification_media", true);
    }

    public void allowNotificationOnMediaDelete(boolean flag) {
        editor.putBoolean("_delete_notification_media", flag);
        editor.commit();
    }

    public boolean isNewMessage() {
        return getPreferences().getBoolean("_new_message_alert", false);
    }

    public void setNewMessageAlert(boolean flag) {
        editor.putBoolean("_new_message_alert", flag);
        editor.commit();
    }

    public void setEnable(boolean value) {
        editor.putBoolean("KEY_IS_ONCE_ENABLED", value);
        editor.apply();
    }

    public boolean isOnceEnabled() {
        return preferences.getBoolean("KEY_IS_ONCE_ENABLED", false);
    }
}
