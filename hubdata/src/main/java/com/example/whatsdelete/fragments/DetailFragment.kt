package com.example.whatsdelete.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.whatsdelete.utils.AppUtils
import com.pds.wastatustranding.R
import com.squareup.picasso.Picasso
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths


class DetailFragment : AppCompatActivity(), View.OnClickListener {
    private var detailImageView: ImageView? = null
    private var share: ImageView? = null
    private var shareOnFacebook: ImageView? = null
    private var shareOnInsta: ImageView? = null
    private var shareOnWhatsApp: ImageView? = null
    private var downloadManager: DownloadManager? = null
    private var imageUrl: String? = null
    private var imageId: String? = null
    private var downloadTag: String? = null
    private var download: LinearLayout? = null
    private var adsbanner: LinearLayout? = null

    var progressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_detail)


        imageId = intent.getStringExtra("id")
        imageUrl = intent.getStringExtra("img_url")
        detailImageView = findViewById(R.id.detail_img)
        adsbanner = findViewById(R.id.adsbanner)
        Picasso.get().load(imageUrl).into(detailImageView)
        download=findViewById(R.id.ll_download)
        download?.setOnClickListener(this)
        share = findViewById(R.id.share);
        share?.setOnClickListener(this)
        shareOnFacebook = findViewById(R.id.share_facebook)
        shareOnFacebook?.setOnClickListener(this)
        shareOnInsta = findViewById(R.id.share_insta)
        shareOnInsta?.setOnClickListener(this)
        shareOnWhatsApp = findViewById(R.id.share_whats_app)
        shareOnWhatsApp?.setOnClickListener(this)
        progressBar=findViewById(R.id.progress_circular)


    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_download->{
                startDownload(imageUrl, "")
            }
            R.id.share -> {
                downloadTag = "share"
                startDownload(imageUrl, downloadTag!!)
            }
            R.id.share_facebook -> {
                downloadTag = "share_facebook"
                startDownload(imageUrl, downloadTag!!)
                ////AppUtils.shareOnFacebook(requireActivity())
            }
            R.id.share_whats_app -> {
                downloadTag = "share_whats_app"
                startDownload(imageUrl, downloadTag!!)
                // AppUtils.shareOnWhatsApp(requireActivity())
            }
            R.id.share_insta -> {
                downloadTag = "share_insta"
                startDownload(imageUrl, downloadTag!!)
                // AppUtils.shareOnInsta(requireActivity())
            }
        }
    }

    private fun startDownload(imageUrl: String?, downloadTag: String) {
        Log.d("TAG", "startDownload: " + imageUrl)
        DownloadTask(this, downloadTag).execute(stringToURL(imageUrl));

    }

    private class DownloadTask(activity: DetailFragment, val downloadTag: String) :
        AsyncTask<URL?, Void?, Bitmap?>() {
        private val weakReference: WeakReference<DetailFragment> =
            WeakReference(activity)
       private var name:String?=null
        override fun onPreExecute() {
             weakReference.get()?.progressBar?.visibility=View.VISIBLE
        }

        override fun doInBackground(vararg urls: URL?): Bitmap? {
            val url: URL? = urls[0]
            Log.d("TAG", "doInBackground: $url")
            name = AppUtils.getImageName(url)
            println("name"+name)
            var connection: HttpURLConnection? = null
            try {
                connection = url?.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                return BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }


        @SuppressLint("WrongThread")
        override fun onPostExecute(result: Bitmap?) {
            var file: File? = null
            if (result != null) {
                weakReference.get()?.progressBar?.visibility=View.GONE
                Toast.makeText(weakReference.get()?.applicationContext,"Download Complete",Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onPostExecute: $result")
                Log.d("TAG", "getDataFilePathWithFolder12: ")
                val directory: File = File(
                    weakReference.get()?.applicationContext?.getExternalFilesDir("/whats_status")?.getPath()
                )
                if (!directory.exists())
                    directory.mkdirs()
                file = File(directory, name)
                Log.d("TAG", "onPostExecute12: "+file)
                try {
                    val out = FileOutputStream(file)
                    result.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                weakReference.get()?.progressBar?.visibility=View.GONE
                Toast.makeText(weakReference.get()?.applicationContext,"Download Failed",Toast.LENGTH_SHORT).show()
            }
            when (downloadTag) {
                "share_insta" -> {
                    weakReference?.get()?.applicationContext?.let {
                        if (file != null) {
                            val uri = FileProvider.getUriForFile(
                                it,
                                weakReference.get()?.applicationContext?.packageName + ".provider",
                                file
                            )
                            AppUtils.shareOnInsta(it, uri)
                        }
                    }

                }
                "share"->{
                    Log.d("TAG", "onPostExecute: "+file)
                    weakReference?.get()?.applicationContext?.let {
                        if (file != null) {
                            val uri = FileProvider.getUriForFile(
                                it,
                                weakReference.get()?.applicationContext?.packageName + ".provider",
                                file
                            )
                            Log.d("TAG", "onPostExecute1: $uri")
                            AppUtils.share(it, uri)
                        }
                    }
                }
                "share_whats_app"->{
                    weakReference?.get()?.applicationContext?.let {
                        if (file != null) {
                            val uri = FileProvider.getUriForFile(
                                it,
                                weakReference.get()?.applicationContext?.packageName + ".provider",
                                file
                            )
                            AppUtils.shareOnWhatsApp(it, uri)
                        }
                    }
                }
                "share_facebook"->{
                    weakReference?.get()?.applicationContext?.let {
                        if (file != null) {
                            val uri = FileProvider.getUriForFile(
                                it,
                                weakReference.get()?.applicationContext?.packageName + ".provider",
                                file
                            )
                            AppUtils.shareOnFacebook(it, uri)
                        }
                    }
                }else->{
                    Toast.makeText(weakReference.get()?.applicationContext,"Download Complete",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    protected fun stringToURL(imageUrl: String?): URL? {

        try {
            val url = URL(imageUrl)
            return url
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

}
