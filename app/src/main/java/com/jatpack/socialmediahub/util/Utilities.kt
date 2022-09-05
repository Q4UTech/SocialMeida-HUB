package com.jatpack.socialmediahub.util

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

class Utilities {
    companion object {
        fun getDuration(mContext: Context, parse: Uri): kotlin.String {
            val mp: MediaPlayer = MediaPlayer.create(mContext, Uri.parse(parse.toString()))
            val duration = mp.duration
            mp.release()
            var videoTime = ""
            try {
                val dur: Int = duration.toInt()
                val hrs = dur / 3600000
                val mns = dur / 60000 % 60000
                val scs = dur % 60000 / 1000
                videoTime = if (hrs > 0) {
                    kotlin.String.format("%02d:%02d:%02d", hrs, mns, scs)
                } else {
                    kotlin.String.format("%02d:%02d", mns, scs)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // Log.e("videoTime::",videoTime);
            // Log.e("videoTime::",videoTime);
            return videoTime
        }


        fun shareFileImage(context: Context, path: ArrayList<Uri>) {
            Log.d("TAG", "shareMutliple1: ")
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND_MULTIPLE
            sendIntent.putExtra(Intent.EXTRA_STREAM, path)
            sendIntent.type = "*/*"
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

    }


}