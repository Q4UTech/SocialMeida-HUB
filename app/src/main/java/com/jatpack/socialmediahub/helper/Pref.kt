package com.jatpack.socialmediahub.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jatpack.socialmediahub.model.PersonNumber
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

    }

    init {
        setPreferences(PreferenceManager.getDefaultSharedPreferences(context))
        editor = preferences!!.edit()
        this.context = context
    }
    fun setNumberList(list: ArrayList<PersonNumber>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(NUMBER_LIST, json)
        editor.commit()
    }

    fun getNumberList(): ArrayList<PersonNumber>? {
        val gson = Gson()
        val json: String? = preferences?.getString(NUMBER_LIST, null)
        val type: Type = object : TypeToken<List<PersonNumber>?>() {}.type
        return gson.fromJson(json, type)
    }
}
