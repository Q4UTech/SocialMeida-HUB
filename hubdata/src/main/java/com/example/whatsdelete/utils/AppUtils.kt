package com.example.whatsdelete.utils

import android.R.attr.data
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.example.whatsdelete.constants.Constants
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppUtils {

    companion object {

        val MAIN_DIR =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "SocialMediaHub"

//    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.q4u.whatsappstatus" + File.separator + "/hello";


        //    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.q4u.whatsappstatus" + File.separator + "/hello";
        var BASE_APP_DIR: String? = null


        //Created bcoz of new storage changes...
        val MAIN_DIR_ABOVE_PIE =
            Environment.getExternalStorageDirectory().absolutePath + "/Android/media/com.m24apps.socialvideo" + File.separator + "SocialMediaHub"


        fun showToast(msg: String?, ctx: Context?) {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        }

        fun getCountryCode(context: Context): String? {
            val tm = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            return tm.simCountryIso
//            return "IN";
        }


        private fun whatsappInstalledOrNot(context: Context, uri: String): Boolean {
            val pm: PackageManager = context.packageManager
            var app_installed = false
            app_installed = try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return app_installed
        }

        fun shareOnInsta(context: Context, file: Uri) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.type = "image/*"
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_STREAM, file)
            sendIntent.setPackage("com.instagram.android")
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(Intent.createChooser(sendIntent, "Share images..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Please Install Instagram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun getDataFilePathWithFolder(fileName: String?, context: Context): String? {

            val directory = File(context.getExternalFilesDir("/bks")!!.path)
            if (!directory.exists()) directory.mkdirs()
            val file = File(directory, fileName)
            return file.absolutePath
        }

        fun share(context: Context, result: Uri) {
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val sendIntent = Intent()

            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_STREAM, result)
            sendIntent.type = "*/*"

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

        fun shareOnFacebook(context: Context, file: Uri) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.type = "*/*"
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_STREAM, file)
            sendIntent.setPackage("com.facebook.katana")
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(Intent.createChooser(sendIntent, "Share images..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Please Install Instagram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun stringToURL(imageUrl: String?): URL? {

            try {
                val url = URL(imageUrl)
                return url
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return null
        }

        fun shareOnWhatsApp(context: Context, uri: Uri) {
            if (context != null) {
                val isWhatsappInstalled = whatsappInstalledOrNot(context, "com.whatsapp")
                Log.d("TAG", "shareOnWhatsApp: " + isWhatsappInstalled)
                if (isWhatsappInstalled) {

                    val whatsappIntent = Intent(Intent.ACTION_SEND)
                    whatsappIntent.type = "*/*"
                    whatsappIntent.setPackage("com.whatsapp")
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    try {
                        context.startActivity(whatsappIntent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "whats app not installed" + ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    Toast.makeText(
                        context, "WhatsApp not Installed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fun getImageName(url: URL?): String {
            var name: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                name = Paths.get(url.toString()).fileName.toString()
            } else {
                val regex =
                    "http(s?)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./]*)+\\.(?:[gG][iI][fF]|[jJ][pP][gG]|[jJ][pP][eE][gG]|[pP][nN][gG]|[bB][mM][pP])"

                val m: Matcher = Pattern.compile(regex).matcher(data.toString())

                if (m.find())
                    name = m.group(0)

            }
            return name!!
        }

        fun shareMutliple(it: Context, imgList: ArrayList<Uri>) {
            Log.d("TAG", "shareMutliple: ")
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.d("TAG", "shareMutliple1: ")
            val sendIntent = Intent()

            sendIntent.action = Intent.ACTION_SEND_MULTIPLE
            sendIntent.putExtra(Intent.EXTRA_STREAM, imgList)
            sendIntent.type = "*/*"

            val shareIntent = Intent.createChooser(sendIntent, null)
            it.startActivity(shareIntent)
        }

        fun shareOnWhatsAppMultiple(context: Context, list: ArrayList<Uri>) {
            if (context != null) {
                val isWhatsappInstalled = whatsappInstalledOrNot(context, "com.whatsapp")
                Log.d("TAG", "shareOnWhatsApp: $isWhatsappInstalled")
                if (isWhatsappInstalled) {

                    val whatsappIntent = Intent(Intent.ACTION_SEND)
                    whatsappIntent.type = "*/*"
                    whatsappIntent.action = Intent.ACTION_SEND_MULTIPLE
                    whatsappIntent.setPackage("com.whatsapp")
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, list)
                    try {
                        context.startActivity(whatsappIntent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "whats app not installed" + ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    Toast.makeText(
                        context, "WhatsApp not Installed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fun shareOnInstaMultiple(context: Context, list: ArrayList<Uri>) {
            Log.d("TAG", "shareOnInstaMultiple: " + list[0])
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.type = "*/*"
            sendIntent.action = Intent.ACTION_SEND_MULTIPLE
            sendIntent.setPackage("com.instagram.android")
            sendIntent.putExtra(Intent.EXTRA_STREAM, list)
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(Intent.createChooser(sendIntent, "Share images..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Please Install Instagram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun shareOnFacebookMultiple(context: Context, list: ArrayList<Uri>) {
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.type = "*/*"
            sendIntent.action = Intent.ACTION_SEND_MULTIPLE
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list)
            sendIntent.setPackage("com.facebook.katana")
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(Intent.createChooser(sendIntent, "Share images..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Please Install Facebook",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun isSameDay(context: Context, currentDay: String): Boolean {
            if (currentDay.equals(Prefs(context).getSameDay())) {
                return true
            } else {
                Prefs(context).setSameDay(currentDay)
                return false
            }
        }


        fun openCustomTab(
            activity: Activity,
             uri: Uri?,toolcolor:String
        ) {
            val  toolcolorTemp:String
            // package name is the default package
            // for our custom chrome tab
            try {

                // img_home.setVisibility(View.VISIBLE);
                val customTabsIntent = CustomTabsIntent.Builder()



                if(toolcolor!=null && !toolcolor.equals("")){
                    toolcolorTemp=toolcolor
                }else{
                    toolcolorTemp="#024282"
                }
                customTabsIntent.setToolbarColor(
                        Color.parseColor(toolcolorTemp)!!
                )
                val build = customTabsIntent.build()

                val packageName = "com.android.chrome"
                if (packageName != null) {
                    // we are checking if the package name is not null
                    // if package name is not null then we are calling
                    // that custom chrome tab with intent by passing its
                    // package name.
                    build.intent.setPackage(packageName)

                    // in that custom tab intent we are passing
                    // our url which we have to browse.
                    build.launchUrl(activity, uri!!)
                } else {
                    // if the custom tabs fails to load then we are simply
                    // redirecting our user to users device default browser.
                    activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }

                /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AHandler.getInstance().showFullAds(activity,false);
                }
            },450);*/
            } catch (e: java.lang.Exception) {
                println("AppUtils.openCustomTab sdgsddjghas"+" "+e.message)
            }
        }


        fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
            return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }


        fun openInstalledApp(context: Context, appPackageName: String) {
            var intent = Intent(Intent.ACTION_MAIN)
            val managerclock: PackageManager = context.getPackageManager()
            intent = managerclock.getLaunchIntentForPackage(appPackageName)!!
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            context.startActivity(intent)
        }


        //Find the installed application Name.
        fun getInstalledApps(context: Context): ArrayList<ApplicationInfo>? {
            val res = ArrayList<ApplicationInfo>()
            val packs = context.packageManager.getInstalledPackages(0) as ArrayList<PackageInfo>
            var pkgArr: ArrayList<String>? = ArrayList<String>()
            var verArr: ArrayList<String>? = ArrayList<String>()

            for (i in packs.indices) {
                val p = packs[i]
                if (!isSystemPackage(p)) {
                    //appname = p.applicationInfo.loadLabel(packageManager).toString();


                    val verName = p.versionName
                    val pkgName = p.packageName
                    println("AppUtils.getInstalledApps hi test app" + " " + pkgName)
                    pkgArr?.add(pkgName)
                    verArr?.add(verName)
                    res.add(ApplicationInfo())
                }
            }
            return res
        }



        fun hideSoftKeyboard(activity: Activity) {
            try {
                val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                //Find the currently focused view, so we can grab the correct window token from it.
                var view = activity.currentFocus
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                // inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun getFileSize(size: Long): String? {
            if (size <= 0) return "0"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / Math.pow(
                    1024.0,
                    digitGroups.toDouble()
                )
            ) + " " + units[digitGroups]
        }




        fun createAppDir(context: Context): String? {
            val APP_DIR = context.getExternalFilesDir("WA Status Gallery")!!.absolutePath
            val file = File(APP_DIR)
            if (!file.exists()) {
                file.parentFile.mkdirs()
            } else {
                file.mkdir()
            }
            return APP_DIR
        }

    }




}