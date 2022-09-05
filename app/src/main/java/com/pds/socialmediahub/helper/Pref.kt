package com.pds.socialmediahub.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pds.socialmediahub.model.PersonNumber
import java.lang.reflect.Type

class Pref(context: Context) {
    private var preferences: SharedPreferences? = null
    private val editor: SharedPreferences.Editor
    private val context: Context
    private fun setPreferences(preferences: SharedPreferences) {
        this.preferences = preferences
    }

    companion object {
        private const val NUMBER_LIST = "NUMBER_LIST"
        private const val SAVED_POSITION = "SAVED_POSITION"
        private const val CAMERA_PREF = "CAMERA_PREF"
        private const val SEARCH_PREF = "SEARCH_PREF"
        private const val WHATSAPP_PREF = "WHATSAPP_PREF"
        private const val MESSAGE_PREF = "MESSAGE_PREF"
        private const val MESSENGER_PREF = "MESSENGER_PREF"
        private const val FACEBOOK_PREF = "FACEBOOK_PREF"
        private const val BOTTOM_POSITION = "BOTTOM_POSITION"
        private const val NOTIFICATION_ENABLE = "NOTIFICATION_ENABLE"


    }

    init {
        setPreferences(PreferenceManager.getDefaultSharedPreferences(context))
        editor = preferences!!.edit()
        this.context = context
    }

    fun getAutoNotificationEnable(): Boolean {
        return preferences!!.getBoolean(NOTIFICATION_ENABLE, false)
    }

    fun setAutoNotificationEnable(flag: Boolean) {
        editor.putBoolean(NOTIFICATION_ENABLE, flag)
        editor.commit()
    }

    fun setNumberList(list: ArrayList<PersonNumber>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(NUMBER_LIST, json)
        editor.commit()
    }

    fun setPositon(position: Int) {
        editor.putInt(SAVED_POSITION, position)
        editor.commit()
    }

    fun getPostion(): Int? {
        return preferences?.getInt(SAVED_POSITION, 0)
    }

    fun setBottomPosition(position: Int) {
        editor.putInt(BOTTOM_POSITION, position)
        editor.commit()
    }

    fun getBottomPosition(): Int? {
        return preferences?.getInt(BOTTOM_POSITION, 0)
    }

    fun setSearchPref(status: Boolean) {
        editor.putBoolean(SEARCH_PREF, status)
        editor.commit()
    }

    fun getSearchPref(): Boolean? {
        return preferences?.getBoolean(SEARCH_PREF, true)
    }

    fun setCameraPref(status: Boolean) {
        editor.putBoolean(CAMERA_PREF, status)
        editor.commit()
    }

    fun getCameraPref(): Boolean? {
        return preferences?.getBoolean(CAMERA_PREF, true)
    }

    fun setWhatsAppPref(status: Boolean) {
        editor.putBoolean(WHATSAPP_PREF, status)
        editor.commit()
    }

    fun getWhatsAppPref(): Boolean? {
        return preferences?.getBoolean(WHATSAPP_PREF, true)
    }

    fun setMessagePref(status: Boolean) {
        editor.putBoolean(MESSAGE_PREF, status)
        editor.commit()
    }

    fun getMessagePref(): Boolean? {
        return preferences?.getBoolean(MESSAGE_PREF, false)
    }

    fun setMessengerPref(status: Boolean) {
        editor.putBoolean(MESSENGER_PREF, status)
        editor.commit()
    }

    fun getMessengerPref(): Boolean? {
        return preferences?.getBoolean(MESSENGER_PREF, false)
    }

    fun setFacebookPref(status: Boolean) {
        editor.putBoolean(FACEBOOK_PREF, status)
        editor.commit()
    }

    fun getFacebookPref(): Boolean? {
        return preferences?.getBoolean(FACEBOOK_PREF, false)
    }

    fun getNumberList(): ArrayList<PersonNumber>? {
        val gson = Gson()
        val json: String? = preferences?.getString(NUMBER_LIST, null)
        val type: Type = object : TypeToken<List<PersonNumber>?>() {}.type
        return gson.fromJson(json, type)
    }
}
