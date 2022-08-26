package com.jatpack.socialmediahub.ui.status

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.GalleryAdapter
import com.jatpack.socialmediahub.helper.MediaPreferences
import com.jatpack.socialmediahub.model.ImagesDetails
import com.jatpack.socialmediahub.util.ItemOffsetView
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MyDownloadsFragment : AppCompatActivity() {
    private val savedImages: ArrayList<ImagesDetails> = ArrayList<ImagesDetails>()
    private var recyclerView: RecyclerView? = null
    private val toolbar: Toolbar? = null
    private var executorService: ExecutorService? = null
    private var mediaPreferences: MediaPreferences? = null
    private var total_downloads: TextView? = null
    private var rl_no_data_found: RelativeLayout? = null
    var adapterList: GalleryAdapter? = null
    private val hideEmpty = false
    private var showFooters: Boolean? = true
    private var itemOffsetView: ItemOffsetView? = null
    private var share: ImageView? = null
    private var imgActive: TextView? = null
    private var ll_nodata: LinearLayout? = null
    private var imgInactive: TextView? = null
    private var videoActive: TextView? = null
    private var videoInactive: TextView? = null
    private var delete: android.widget.ImageView? = null
    private var back: android.widget.ImageView? = null

    // private var asynTask: AsycnTask? = null
    var path: String? = null
    var tempList: Array<File>? = null
    var imageList: ArrayList<File>? = null
    var videoList: ArrayList<File>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_my_download)
        path = this.getExternalFilesDir("WA Status Gallery")?.getAbsolutePath();
        tempList = getExternalFilesDir("WA Status Gallery")!!.listFiles()
        executorService = Executors.newSingleThreadExecutor()
        videoList = ArrayList()
        imageList = ArrayList()
        loadData()
        ll_nodata = findViewById(R.id.ll_nodata)
        imgActive = findViewById(R.id.img_active)
        imgInactive = findViewById(R.id.img_inactive)
        videoActive = findViewById(R.id.video_active)
        videoInactive = findViewById(R.id.video_inactive)
        imgActive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.setVisibility(View.VISIBLE)
            videoInactive?.setVisibility(View.GONE)
            videoActive?.setVisibility(View.VISIBLE)
        })
        imgInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgActive?.setTextColor(resources.getColor(R.color.white, null))
            imgActive?.setVisibility(View.VISIBLE)
            videoInactive?.setVisibility(View.VISIBLE)
            videoActive?.setVisibility(View.GONE)
            fetchFor10AndAbove("images")
        })
        videoActive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.setVisibility(View.GONE)
            videoInactive?.setVisibility(View.VISIBLE)
            imgActive?.setVisibility(View.VISIBLE)
        })
        videoInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.setVisibility(View.VISIBLE)
            imgActive?.setVisibility(View.GONE)
            videoActive?.setVisibility(View.VISIBLE)
            videoActive?.setTextColor(resources.getColor(R.color.white, null))
            fetchFor10AndAbove("video")
        })
        recyclerView = findViewById(R.id.recycler_View)
        itemOffsetView = ItemOffsetView(this, com.intuit.sdp.R.dimen._5sdp)
        recyclerView?.addItemDecoration(itemOffsetView!!)


        fetchFor10AndAbove("images")
    }

    private fun fetchFor10AndAbove(criteria: String) {

        fetchForAndroid10andBelow(criteria)

    }


    private fun fetchForAndroid10andBelow(criteria: String) {

        if (criteria.equals("images")) {
            if (imageList != null && imageList?.size!! > 0) {
                ll_nodata?.visibility = View.GONE
                adapterList = GalleryAdapter(this, imageList!!)
                val gridLayoutManager = GridLayoutManager(this, 3)
                total_downloads?.setText("Total Downloads" + imageList?.size)
                recyclerView?.layoutManager = gridLayoutManager
                recyclerView?.adapter = adapterList
            } else {
                ll_nodata?.visibility = View.VISIBLE
            }
        } else if (criteria.equals("video")) {
            if (videoList != null && videoList?.size!! > 0) {
                ll_nodata?.visibility = View.GONE
                adapterList = GalleryAdapter(this, videoList!!)
                val gridLayoutManager = GridLayoutManager(this, 3)
                total_downloads?.setText("Total Downloads" + videoList?.size)
                recyclerView?.layoutManager = gridLayoutManager
                recyclerView?.adapter = adapterList
            } else {
                ll_nodata?.visibility = View.VISIBLE
            }
        }

    }


    fun visibleToolBar() {
        toolbar!!.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
//        new AsycnTask(this).execute();
    }

    fun loadData() {

        executorService?.execute(Runnable {

            getExternalFilesDir("WA Status Gallery")?.let { file ->
                if (file.exists()) {
                    file.listFiles()?.let { fileArray ->

                        fileArray.forEach { file ->
                            if (file.path.endsWith(".mp4")) {
                                videoList?.add(file)
                            } else if (file.path.endsWith(".jpg") || file.path.endsWith(".jpeg")) {
                                imageList?.add(file)
                            }
                        }
                    }
                }

            }
        })

        /*for (i in tempList.indices){
            if(tempList[i].path.endsWith(".Mp4"))
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        //asynTask!!.cancel(true)
    }
}