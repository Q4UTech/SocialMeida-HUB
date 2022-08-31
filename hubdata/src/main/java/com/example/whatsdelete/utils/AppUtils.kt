package com.example.whatsdelete.utils

import android.R.attr.data
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppUtils {

    companion object {

        fun showToast(msg: String?, ctx: Context?) {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        }

        fun getCountryCode(context: Context): String? {
            val tm = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            return tm.simCountryIso
            //return "fr";
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
    }



}