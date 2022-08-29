package com.jatpack.socialmediahub.ui.status

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.GalleryAdapter
import com.jatpack.socialmediahub.helper.MediaPreferences
import com.jatpack.socialmediahub.model.ImagesDetails
import com.jatpack.socialmediahub.util.AppUtils
import com.jatpack.socialmediahub.util.ItemOffsetView
import com.jatpack.socialmediahub.util.SetClick
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MyDownloadsFragment : AppCompatActivity(), SetClick {
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
    private var rl_saved_options: LinearLayout? = null
    private var ll_save: LinearLayout? = null
    private var ll_share: LinearLayout? = null
    private var ll_select_all: LinearLayout? = null
    private var ll_card_selection: CardView? = null
    private var share: ImageView? = null
    private var imgActive: TextView? = null
    private var ll_nodata: LinearLayout? = null
    private var imgInactive: TextView? = null
    private var videoActive: TextView? = null
    private var videoInactive: TextView? = null
    private var delete: android.widget.ImageView? = null
    private var back: android.widget.ImageView? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null
    private var statusFileList: List<File>? = null

    // private var asynTask: AsycnTask? = null
    var path: String? = null
    var tempList: Array<File>? = null
    var imageList: ArrayList<File>? = null
    var videoList: ArrayList<File>? = null


    @RequiresApi(Build.VERSION_CODES.M)
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
        total_downloads = findViewById(R.id.total_downloads)
        imgActive = findViewById(R.id.img_active)
        imgInactive = findViewById(R.id.img_inactive)
        videoActive = findViewById(R.id.video_active)
        videoInactive = findViewById(R.id.video_inactive)
        rl_saved_options = findViewById(R.id.rl_saved_options)
        ll_share = findViewById(R.id.ll_share)
        ll_save = findViewById(R.id.ll_save)
        ll_select_all = findViewById(R.id.ll_select_all)
        ll_card_selection = findViewById(R.id.ll_card_selection)
        imgActive?.setOnClickListener(View.OnClickListener { v: View? ->

            imgInactive?.visibility = View.VISIBLE
            imgActive?.visibility = View.GONE
            videoInactive?.visibility = View.GONE
            videoActive?.visibility = View.VISIBLE
        })
        imgInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.visibility = View.GONE
            imgActive?.visibility = View.VISIBLE
            imgActive?.setTextColor(resources.getColor(R.color.white, null))
            videoInactive?.visibility = View.VISIBLE
            videoActive?.visibility = View.GONE
            fetchFor10AndAbove("images")
        })
        videoActive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.visibility = View.GONE
            videoInactive?.visibility = View.VISIBLE
            videoActive?.visibility = View.GONE
            imgActive?.visibility = View.VISIBLE

        })
        videoInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            videoInactive?.visibility = View.GONE
            imgInactive?.visibility = View.VISIBLE
            imgActive?.visibility = View.GONE
            videoActive?.visibility = View.VISIBLE
            videoActive?.setTextColor(resources.getColor(R.color.white, null))
            fetchFor10AndAbove("video")
        })
        recyclerView = findViewById(R.id.recycler_View)
        itemOffsetView = ItemOffsetView(this, com.intuit.sdp.R.dimen._5sdp)
        recyclerView?.addItemDecoration(itemOffsetView!!)


    }

    private fun fetchFor10AndAbove(criteria: String) {

        fetchForAndroid10andBelow(criteria)

    }


    private fun fetchForAndroid10andBelow(criteria: String) {


        if (criteria.equals("images") && imageList != null && imageList?.size!! > 0) {

            adapterList = GalleryAdapter(this, imageList!!, this)
            val gridLayoutManager = GridLayoutManager(this, 3)
            total_downloads?.setText("Total Downloads : " + imageList?.size)
            recyclerView?.layoutManager = gridLayoutManager
            recyclerView?.adapter = adapterList
            adapterList?.setCheckedListener(object : GalleryAdapter.CounterSlection {
                override fun selectItems(itemSlectionCount: Int) {
                    setPageTitle(itemSlectionCount)
                }
            })
        } else if (criteria.equals("video") && videoList != null && videoList?.size!! > 0) {

            adapterList = GalleryAdapter(this, videoList!!, this)
            val gridLayoutManager = GridLayoutManager(this, 3)
            total_downloads?.setText("Total Downloads : " + videoList?.size)
            recyclerView?.layoutManager = gridLayoutManager
            recyclerView?.adapter = adapterList

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

        executorService?.execute {

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
                runOnUiThread {
                    if (imageList?.size!! > 0 || videoList?.size!! > 0) {
                        ll_nodata?.visibility = View.GONE
                    } else {
                        ll_nodata?.visibility = View.VISIBLE
                    }
                    fetchFor10AndAbove("images")
                }

            }
        }


        /*for (i in tempList.indices){
            if(tempList[i].path.endsWith(".Mp4"))
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        //asynTask!!.cancel(true)
    }

    override fun onClick(view: View, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClcik(view: View, position: Int) {
        if (adapterList != null) {
            getFilePathData()
            actionModeCallback = ActionModeCallback(this, R.menu.download_menu, true)
            actionMode = startActionMode(actionModeCallback)
            ll_card_selection?.visibility = View.GONE
            rl_saved_options?.visibility = View.VISIBLE

            ll_save?.setOnClickListener {
                //  downloadMultipleImage()
                deleteMultipleImage()
                actionMode?.finish()
            }
            ll_share?.setOnClickListener {
                shareMultipleImage()
                actionMode?.finish()
            }
            ll_select_all?.setOnClickListener {
                adapterList?.selectAll()
            }


            setPageTitle(1)
        }
    }

    private fun deleteMultipleImage() {
        if (statusFileList != null && statusFileList?.size!! > 0) {
            val tempDeleteList = ArrayList<File>()
            tempDeleteList.addAll(statusFileList!!)
            for (i in statusFileList?.indices!!) {

                if (adapterList?.checkStatus?.get(i)!!) {
                    File(statusFileList!![i].absolutePath).delete()
                    tempDeleteList.remove(statusFileList!![i])
                }
            }
            adapterList?.updateList(tempDeleteList)
        }
    }

    private fun setPageTitle(i: Int) {
        if (actionMode != null) {
            actionMode!!.title = "Selected : $i"
        }
    }

    private fun downloadMultipleImage() {
        //List<File> videoList = adapter.getList();
        if (statusFileList != null && statusFileList?.size!! > 0) {
            for (i in statusFileList?.indices!!) {

                if (adapterList?.checkStatus?.get(i)!!) {
                    copyFileOrDirectory(
                        statusFileList?.get(i)?.getAbsolutePath()!!,
                        AppUtils.createAppDir(this)
                    )
                }
            }
        }
    }

    private fun shareMultipleImage() {
        // List<File> list = adapterList.getList();
        val uriArrayList = java.util.ArrayList<Uri>()
        Log.d("TAG", "shareMultipleImage: " + statusFileList?.size)
        for (i in statusFileList?.indices!!) {

            if (adapterList?.checkStatus?.get(i)!!) {
                val uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    File(statusFileList?.get(i)?.getPath())
                )
                uriArrayList.add(uri)
            }
        }
        shareFileImage(uriArrayList)
    }

    private fun getFilePathData() {

        statusFileList = adapterList?.getList()

    }

    fun shareFileImage(path: java.util.ArrayList<Uri>) {
        Log.d("TAG", "shareMutliple1: ")
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND_MULTIPLE
        sendIntent.putExtra(Intent.EXTRA_STREAM, path)
        sendIntent.type = "*/*"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private class ActionModeCallback(
        var myDownloadFragment: MyDownloadsFragment,
        menu_lauout: Int,
        from: Boolean
    ) :
        ActionMode.Callback {
        var checkBox: CheckBox? = null
        var flag = false
        val menu_lauout: Int
        var from: Boolean
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // inflate contextual menu
            mode.menuInflater.inflate(menu_lauout, menu)

            // checkBox = (CheckBox) menu.findItem(R.id.select_all).getActionView();
//            checkBox.setChecked(true);
            /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!flag) {
                        recordedFragment.selectAll(isChecked);
                    }
                    flag = false;
                }
            });*/return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            // retrieve selected items and print them out
            /*  return when (item.itemId) {
                  R.id.share_multiple -> {
                      //Toast.makeText(MainActivity.this, "Option 1 selected", Toast.LENGTH_SHORT).show();

                      myDownloadFragment.shareMultipleImage()

                      myDownloadFragment.actionMode?.finish()
                      true
                  }
                  R.id.multiple_download -> {

                      myDownloadFragment.downloadMultipleImage()

                      myDownloadFragment.actionMode?.finish()
                      true
                  }
                  R.id.select_all -> {
                      if (from) {
                          myDownloadFragment.adapterList?.selectAll()
                      }
                      // mode.finish();
                      true
                  }
                  else -> false
              }*/
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            // remove selection

            //  waStatusFragment.actionMode.finish();
            if (myDownloadFragment.adapterList != null) {
                myDownloadFragment.adapterList!!.removeAllSelected()
            }
            myDownloadFragment.rl_saved_options?.visibility = View.GONE
            myDownloadFragment.ll_card_selection?.visibility = View.VISIBLE
            myDownloadFragment.actionMode = null
        }

        init {
            flag = false
            this.menu_lauout = menu_lauout
            this.from = from
        }
    }


    fun copyFileOrDirectory(srcDir: String, dstDir: String) {
        try {
            Log.d(
                "ImageDetailActivity", "Hello copyFileOrDirectory hi test path" + " " +
                        srcDir + " " + ">>>>>> " + dstDir
            )
            val src = File(srcDir)
            val dst = File(dstDir, src.name)
            if (src.isDirectory) {
                val files = src.list()
                val filesLength = files.size
                for (i in 0 until filesLength) {
                    val src1 = File(src, files[i]).path
                    val dst1 = dst.path
                    copyFileOrDirectory(src1, dst1)
                    println("my video dir if $src1 $dst1")
                }
            } else {
                copyFile(src, dst)
                println("my video dir else" + " " + src.path + " " + dst.path)
            }
        } catch (e: Exception) {
            println("qsdafqhakj $e")
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
        println("MY LOG CHECK 01")
        if (!destFile.exists()) {
            destFile.createNewFile()
            println("MY LOG CHECK 02 ggfahsdgfahj" + " " + destFile.path)

            //  mediaPreferences.setgallerycount(mediaPreferences.getgallerycount() + 1);
            Toast.makeText(
                this,
                resources.getString(R.string.save_image_toast),
                Toast.LENGTH_LONG
            ).show()
        } else {
            println("MY LOG CHECK override")
            Toast.makeText(this, "This Image is already saved ", Toast.LENGTH_LONG)
                .show()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            println("MY LOG CHECK 03")
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            if (source != null) {
                source.close()
                println("MY LOG CHECK 04")
            }
            if (destination != null) {
                println("MY LOG CHECK 05")
                destination.close()
            }
        }
    }
}