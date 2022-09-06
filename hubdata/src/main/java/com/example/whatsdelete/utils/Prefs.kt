package com.example.whatsdelete.utils


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.whatsdelete.constants.Constants.Companion.FILTTER_ALL
import com.example.whatsdelete.modal.CategoryDetailItem
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.responce.CategoryListData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


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
        const val PREF = "prefs_social_media_downloader"
        const val PROGRESS_KEY_PREF = "progress_key_pref"
         const val SET_UPDATED_KEY = "SET_UPDATED_KEY"



        private const val KEY_CAMERA = "camera_pack"
        private const val KEY_SHARE_PATH = "share_path"
        private const val KEY_DOWNLOADED_MEDIA = "downloaded_media"
        private const val KEY_FILTER_NAME = "filtername"
        private const val KEY_BOOL = "activity_boolean"
        private const val KEY_DEL_BOOL = "delete_boolean"
        private const val KEY_DEL_BOOL_SHARE = "delete_boolean_share"
        private const val KEY_AUTO_DOWNLOAD = "auto_download"
        private const val KEY_DISABLE_DOWNLOAD = "disable_download"
        private const val KEY_GO_BUTTON_CLICK = "go_button_click"
        private const val SCOPE_PERMISSION_GRANTED = "ScopePermissionGranted"
        private const val SCOPE_STORAGE_URI = "scope_storage_path"

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


    fun setCategoryList(list: List<CategoryListData>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(KEY_SET_cat_list, json)
        editor.commit()
    }

    fun getCategoryList(): List<CategoryListData>? {
        val gson = Gson()
        val json: String? = preferences.getString(KEY_SET_cat_list, null)
        val type: Type = object : TypeToken<List<CategoryListData>?>() {}.type
        return gson.fromJson(json, type)
    }

    fun setSubCategoryList(list: List<CategoryDetailItem>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(KEY_SET_SUB_cat_list, json)
        editor.commit()


    }

    fun getSubCategoryList(): List<CategoryDetailItem>? {
        val gson = Gson()
        val json: String? = preferences.getString(KEY_SET_SUB_cat_list, null)
        val type: Type = object : TypeToken<List<CategoryDetailItem>?>() {}.type
        return gson.fromJson(json, type)
    }


//    fun setApplicationResponceList(modal: ApplicationListResponce?, key: String?) {
//        /**** storing object in preferences   */
//        val gson = Gson()
//        val jsonObject = gson.toJson(modal)
//        editor.putString(key, jsonObject)
//        editor.commit()
//    }
//
//    fun getApplicationResponceList(key: String?): ApplicationListResponce? {
//        /**** get user data     */
//        val json = preferences.getString(key, "")
//        val gson = Gson()
//        return gson.fromJson(json, ApplicationListResponce::class.java)
//    }


     fun setApplicationList(con: Context, jsonMap: HashMap<String, List<ApplicationListData>>) {
        val jsonString = Gson().toJson(jsonMap)

         println("Prefs.setApplicationList jhjhj"+" "+jsonMap.toString())
        val sharedPreferences: SharedPreferences = con.getSharedPreferences("HashMap", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("map", jsonString)
        editor.apply()
    }

    fun getApplicationLsit(con: Context): HashMap<String, List<ApplicationListData>> {
        val sharedPreferences: SharedPreferences =
            con.getSharedPreferences("HashMap", MODE_PRIVATE)
        val defValue =
            Gson().toJson(HashMap<String, List<ApplicationListData>>())
        val json = sharedPreferences.getString("map", defValue)
        val token: TypeToken<HashMap<String, List<ApplicationListData>>> =
            object : TypeToken<HashMap<String, List<ApplicationListData>>>() {}
        return Gson().fromJson(json, token.type)
    }




    fun saveArrayList(list: List<ApplicationListData>?, key: String?) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String?): List<ApplicationListData>? {
        val gson = Gson()
        val json = preferences.getString(key, null)
        val type = object : TypeToken<List<ApplicationListData>?>() {}.type
        return gson.fromJson(json, type)
    }




    fun getFilterName(): String? {
        return preferences.getString(
            KEY_FILTER_NAME,
            FILTTER_ALL
        )
    }

    fun setFilterName(value: String?) {
        editor.putString(KEY_FILTER_NAME, value)
        editor.commit()
    }


    fun getCameraName(): String? {
        return preferences.getString(KEY_CAMERA, " ")
    }

    fun setCameraName(value: String?) {
        editor.putString(KEY_CAMERA, value)
        editor.commit()
    }


    fun getActivityBoolean(): Boolean? {
        return preferences.getBoolean(KEY_BOOL, true)
    }

    fun setActivityBoolean(value: Boolean?) {
        editor.putBoolean(KEY_BOOL, value!!)
        editor.commit()
    }

    //AUTO DOWNLOAD
    fun setKeyAutoDownload(value: Boolean) {
        editor.putBoolean(KEY_AUTO_DOWNLOAD, value)
        editor.commit()
    }

    fun getKeyAutoDownload(): Boolean {
        return preferences.getBoolean(KEY_AUTO_DOWNLOAD, true)
    }


    //disable download

    //disable download
    //AUTO DOWNLOAD
    fun setdisableDownload(value: Boolean) {
        editor.putBoolean(KEY_DISABLE_DOWNLOAD, value)
        editor.commit()
    }

    fun getdisableDownload(): Boolean {
        return preferences.getBoolean(
            KEY_DISABLE_DOWNLOAD,
            false
        )
    }

    fun getDelete(): Boolean? {
        return preferences.getBoolean(KEY_DEL_BOOL, false)
    }

    fun setDelete(value: Boolean?) {
        editor.putBoolean(KEY_DEL_BOOL, value!!)
        editor.commit()
    }

    fun getDeleteaftershare(): Boolean? {
        return preferences.getBoolean(KEY_DEL_BOOL_SHARE, false)
    }

    fun setDeleteaftershare(value: Boolean?) {
        editor.putBoolean(KEY_DEL_BOOL_SHARE, value!!)
        editor.commit()
    }

    fun getlaunchwith_autodownload(): Boolean? {
        return preferences.getBoolean("auto_download_launch", false)
    }

    fun setlaunchwith_autodownload(value: Boolean?) {
        editor.putBoolean("auto_download_launch", value!!)
        editor.commit()
    }

    fun getGoButtonClick(): Boolean? {
        return preferences.getBoolean(
            KEY_GO_BUTTON_CLICK,
            false
        )
    }

    fun setGoButtonClick(value: Boolean?) {
        editor.putBoolean(KEY_GO_BUTTON_CLICK, value!!)
        editor.commit()
    }


    fun getSharepathname(): String? {
        return preferences.getString(KEY_SHARE_PATH, " ")
    }

    fun setSharepathname(value: String?) {
        editor.putString(KEY_SHARE_PATH, value)
        editor.commit()
    }



    private class ListParameterizeType(private val type: Type) :
        ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(type)
        }

        override fun getRawType(): Type {
            return ArrayList::class.java
        }

        override fun getOwnerType(): Type {
            return null!!
        }
    }

    fun saveDownloadList(context: Context, key: String, arrayList: List<MediaUrl?>?) {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(arrayList)
        editor.putString(key, json)
        editor.apply()
    }

    fun getDownloadList(
        context: Context,
        key: String
    ): List<MediaUrl>? {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPrefs.getString(key, null)
        return Gson().fromJson<List<MediaUrl>>(json, object : TypeToken<List<MediaUrl?>?>() {}.type)
    }

    fun saveFavoriteList(
        context: Context,
        key: String,
        hasMap: java.util.HashMap<String?, Boolean?>?
    ) {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(hasMap)
        editor.putString(key, json)
        editor.apply()
    }

    fun getFavoriteList(
        context: Context,
        key: String
    ): java.util.HashMap<String, Boolean>? {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPrefs.getString(key, null)
        return Gson().fromJson(
            json,
            object : TypeToken<java.util.HashMap<String?, Boolean?>?>() {}.type
        )
    }


    fun setFirstTimeSeen(
        context: Context,
        key: String,
        hasMap: java.util.HashMap<String?, Boolean?>?
    ) {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(hasMap)
        editor.putString(key, json)
        editor.apply()
    }

    fun getFirstTimeSeen(
        context: Context,
        key: String
    ): java.util.HashMap<String, Boolean>? {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPrefs.getString(key, null)
        return Gson().fromJson(
            json,
            object : TypeToken<java.util.HashMap<String?, Boolean?>?>() {}.type
        )
    }

    fun getIsScopePermissionGranted(): Boolean? {
        return preferences.getBoolean(
            SCOPE_PERMISSION_GRANTED,
            false
        )
    }

    fun setIsScopePermissionGranted(value: Boolean?) {
        editor.putBoolean(SCOPE_PERMISSION_GRANTED, value!!)
        editor.commit()
    }

    fun getScopeStorageUri(): String? {
        return preferences.getString(SCOPE_STORAGE_URI, null)
    }

    fun setScopeStorageUri(value: String?) {
        editor.putString(SCOPE_STORAGE_URI, value)
        editor.commit()
    }

    fun <T> saveList(context: Context, key: String, arrayList: List<T>?) {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(arrayList)
        editor.putString(key, json)
        editor.apply()
    }

    fun <T> getList(context: Context, key: String, clazz: Class<T>?): List<T>? {
        val sharedPrefs = context.getSharedPreferences(
            PREF + "" + key,
            MODE_PRIVATE
        )
        val gson = Gson()
        val json = sharedPrefs.getString(key, null)
        val type: Type = ListParameterizeType(clazz!!)
        return gson.fromJson(json, type)
    }

    fun setUpdatedKey(updatedKey: String) {
        editor.putString(SET_UPDATED_KEY, updatedKey)
        editor.commit()
    }

    fun getUpdatedKey(): String? {
        return preferences?.getString(SET_UPDATED_KEY, "NA")
    }

}


