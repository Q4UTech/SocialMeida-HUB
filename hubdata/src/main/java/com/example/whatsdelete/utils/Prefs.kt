package com.example.whatsdelete.utils


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.whatsdelete.modal.Category
import com.example.whatsdelete.modal.CategoryDetailItem
import com.example.whatsdelete.modal.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList


/**
 * Created by Rakesh Rajput on 09/08/2021.
 */
class Prefs(con: Context) {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var context: Context? = con


    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()
    }

    companion object {
        var KEY_SET_SAME_DAY = "KEY_SET_SAME_DAY"
        var KEY_SET_cat_list = "KEY_SET_cat_list"
        var KEY_SET_SUB_cat_list = "KEY_SET_SUB_cat_list"

    }




    //save the number
    fun setSameDay(position: String) {
        editor.putString(KEY_SET_SAME_DAY, position)
        editor.commit()
    }

    fun getSameDay(): String? {
        return preferences.getString(KEY_SET_SAME_DAY, "00")
    }

    fun clearAll(){
        editor.clear()
        editor.commit()
    }


    fun setCategoryList(list: List<Data>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(KEY_SET_cat_list, json)
        editor.commit()
    }

//    fun getCategoryList(): List<Data>? {
//        val gson = Gson()
//        val json: String? = preferences.getString(KEY_SET_cat_list, null)
//        val type: Type = object : TypeToken<List<Data>?>() {}.type
//        return gson.fromJson(json, type)
//    }
//
//    fun setSubCategoryList(list: List<CategoryDetailItem>?) {
//        val gson = Gson()
//        val json: String = gson.toJson(list)
//        editor.putString(KEY_SET_SUB_cat_list, json)
//        editor.commit()
//    }

    fun getSubCategoryList(): List<CategoryDetailItem>? {
        val gson = Gson()
        val json: String? = preferences.getString(KEY_SET_SUB_cat_list, null)
        val type: Type = object : TypeToken<List<CategoryDetailItem>?>() {}.type
        return gson.fromJson(json, type)
    }



}
