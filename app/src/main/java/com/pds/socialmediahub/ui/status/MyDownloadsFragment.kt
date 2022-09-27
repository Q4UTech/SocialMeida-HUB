package com.pds.socialmediahub.ui.status

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pds.socialmediahub.R
import com.pds.socialmediahub.adapter.GalleryAdapter
import com.pds.socialmediahub.helper.MediaPreferences
import com.pds.socialmediahub.model.ImagesDetails
import com.pds.socialmediahub.util.*
import engine.app.adshandler.AHandler
import kotlinx.android.synthetic.main.fragment_my_download.*
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
    private var videoRecyclerView: RecyclerView? = null
    private val toolbar: Toolbar? = null
    private var executorService: ExecutorService? = null
    private var mediaPreferences: MediaPreferences? = null
    private var total_downloads: TextView? = null
    private var rl_no_data_found: RelativeLayout? = null
    var adapterList: GalleryAdapter? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
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
    private var ll_nodata_video: LinearLayout? = null
    private var imgInactive: TextView? = null
    private var videoActive: TextView? = null
    private var videoInactive: TextView? = null
    private var delete: android.widget.ImageView? = null
    private var back: android.widget.ImageView? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionModeCallback? = null
    private var statusFileList: List<File>? = null
    private var selectAll: Boolean? = false

    // private var asynTask: AsycnTask? = null
    var path: String? = null
    var tempList: Array<File>? = null
    var imageList: ArrayList<File>? = null
    var videoList: ArrayList<File>? = null
    var tvSelectAll: TextView? = null
    var ivBack: ImageView? = null
    var save: TextView? = null
    var rlTop: RelativeLayout? = null
    var adsbanner: LinearLayout? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_my_download)
        mediaPreferences= MediaPreferences(this)
        supportActionBar?.hide()
        tvSelectAll = findViewById(R.id.tvSelectAll)

        adsbanner=findViewById(R.id.adsbanner)
        adsbanner?.addView(AHandler.getInstance().getBannerHeader(this))

        path = this.getExternalFilesDir(Constants.WA_Status_Gallery)?.absolutePath
        tempList = getExternalFilesDir(Constants.WA_Status_Gallery)!!.listFiles()
        rlTop = findViewById(R.id.topbar)
        ivBack = findViewById(R.id.ivBack)
        ivBack?.setOnClickListener {
            finish()
        }
        save = findViewById(R.id.tvSave)
        save?.setOnClickListener {
            Toast.makeText(this, "Setting Saved", Toast.LENGTH_SHORT).show()
        }
        executorService = Executors.newSingleThreadExecutor()


        ll_nodata = findViewById(R.id.ll_nodata)
        ll_nodata_video = findViewById(R.id.ll_nodata_video)
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


//
//        imgActive?.setOnClickListener(View.OnClickListener { v: View? ->
//
//            imgInactive?.visibility = View.VISIBLE
//            imgActive?.visibility = View.GONE
//            videoInactive?.visibility = View.GONE
//            videoActive?.visibility = View.VISIBLE
//        })


        imgInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            imgInactive?.visibility = View.GONE
            imgActive?.visibility = View.VISIBLE
            imgActive?.setTextColor(resources.getColor(R.color.white, null))
            videoInactive?.visibility = View.VISIBLE
            videoActive?.visibility = View.GONE
            fetchFor10AndAbove("images")
            videoRecyclerView?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE

        })
//        videoActive?.setOnClickListener(View.OnClickListener { v: View? ->
//            imgInactive?.visibility = View.GONE
//            videoInactive?.visibility = View.VISIBLE
//            videoActive?.visibility = View.GONE
//            imgActive?.visibility = View.VISIBLE
//
//        })
        videoInactive?.setOnClickListener(View.OnClickListener { v: View? ->
            videoInactive?.visibility = View.GONE
            imgInactive?.visibility = View.VISIBLE
            imgActive?.visibility = View.GONE
            videoActive?.visibility = View.VISIBLE
            videoActive?.setTextColor(resources.getColor(R.color.white, null))
            fetchFor10AndAbove("video")
            videoRecyclerView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE

        })
        recyclerView = findViewById(R.id.recycler_View)
        videoRecyclerView = findViewById(R.id.video_recycler_View)
        itemOffsetView = ItemOffsetView(this, com.intuit.sdp.R.dimen._5sdp)
        recyclerView?.addItemDecoration(itemOffsetView!!)
        videoRecyclerView?.addItemDecoration(itemOffsetView!!)

        videoList = ArrayList()
        imageList = ArrayList()
        loadData()

    }

    private fun fetchFor10AndAbove(criteria: String) {
        fetchForAndroid10andBelow(criteria)

    }


    private fun fetchForAndroid10andBelow(criteria: String) {

        when (criteria) {
            "images" -> {

                total_downloads?.visibility=View.VISIBLE
                videoRecyclerView?.visibility=View.GONE

                ll_nodata_video?.visibility=View.GONE
                if (imageList != null && imageList?.size!! > 0) {
                    recyclerView?.visibility=View.VISIBLE
                    adapterList = GalleryAdapter(this, imageList!!, this)
                    ll_nodata?.visibility=View.GONE
                    val gridLayoutManager = GridLayoutManager(this, 3)
                    total_downloads?.text = getFontColorSize(imageList?.size!!)
                    recyclerView?.layoutManager = gridLayoutManager
                    recyclerView?.adapter = adapterList
                    adapterList?.setCheckedListener(object : GalleryAdapter.CounterSlection {
                        override fun selectItems(itemSlectionCount: Int) {
                            setPageTitle(itemSlectionCount)
                        }
                    })
                } else {
                    println("MyDownloadsFragment.fetchForAndroid10andBelow no data dfgmdajbga")

                    ll_nodata?.visibility=View.VISIBLE
                    recyclerView?.visibility=View.INVISIBLE
                    videoRecyclerView?.visibility=View.INVISIBLE
                    total_downloads?.visibility=View.GONE

                }
            }

             "video" -> {
                 recyclerView?.visibility=View.GONE
                 ll_nodata?.visibility=View.GONE
                 total_downloads?.visibility=View.VISIBLE

                 if (videoList != null && videoList?.size!! > 0) {
                     ll_nodata_video?.visibility=View.GONE
                     videoRecyclerView?.visibility=View.VISIBLE
                     total_downloads?.text = getFontColorSize(videoList?.size!!)
                     adapterList = GalleryAdapter(this, videoList!!, this)
                     val gridLayoutManager = GridLayoutManager(this, 3)
                     videoRecyclerView?.layoutManager = gridLayoutManager
                     videoRecyclerView?.adapter = adapterList

                 } else {

                     println("MyDownloadsFragment.fetchForAndroid10andBelow no data")
                     ll_nodata_video?.visibility=View.VISIBLE
                     total_downloads?.visibility=View.GONE
                     recyclerView?.visibility=View.INVISIBLE
                     videoRecyclerView?.visibility=View.INVISIBLE

                 }
            }

            }

    }


    fun visibleToolBar() {
        toolbar!!.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (mediaPreferences != null && mediaPreferences!!.refresh) {
            mediaPreferences!!.refresh = false
            try {
                path = this.getExternalFilesDir(Constants.WA_Status_Gallery)?.absolutePath
                tempList = getExternalFilesDir(Constants.WA_Status_Gallery)!!.listFiles()
                executorService = Executors.newSingleThreadExecutor()
                println("MyDownloadsFragment.onResume fsadhfjagsjs")
                videoList = ArrayList()
                imageList = ArrayList()
                videoList?.clear()
                imageList?.clear()
                imgInactive?.visibility = View.GONE
                imgActive?.visibility = View.VISIBLE
                imgActive?.setTextColor(resources.getColor(R.color.white, null))
                videoInactive?.visibility = View.VISIBLE
                videoActive?.visibility = View.GONE
//                fetchFor10AndAbove("images")
                videoRecyclerView?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE

                loadData()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

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
//                    if (videoList != null && videoList?.size!! > 0) {
//                        ll_nodata_video?.visibility = View.GONE
//                    } else {
//                        ll_nodata_video?.visibility = View.VISIBLE
//                    }
//                    if (imageList != null && imageList?.size!! > 0) {
//                        ll_nodata?.visibility = View.GONE
//                    } else {
//                        ll_nodata?.visibility = View.VISIBLE
//                    }
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
            rlTop?.visibility = View.GONE
            total_downloads?.visibility = View.GONE
            getFilePathData()
            actionModeCallback = ActionModeCallback(this, R.menu.download_menu, true)
            actionMode = startActionMode(actionModeCallback)
            ll_card_selection?.visibility = View.GONE
            rl_saved_options?.visibility = View.VISIBLE

            ll_save?.setOnClickListener {
                showBottomSheetDialog()

            }
            ll_share?.setOnClickListener {
                shareMultipleImage()
                actionMode?.finish()
            }
            ll_select_all?.setOnClickListener {
                if (!selectAll!!) {
                    selectAll = true
                    adapterList?.selectAll()
                    tvSelectAll?.text = "Unselect All"
                } else {
                    selectAll = false
                    adapterList?.unSelectAll()
                    tvSelectAll?.text = "select All"
                }
            }


            setPageTitle(1)
        }
    }

    fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        bottomSheetDialog?.setContentView(R.layout.delete_bottom_sheet)

        val delete = bottomSheetDialog?.findViewById<TextView>(R.id.delete)
        val close = bottomSheetDialog?.findViewById<ImageView>(R.id.close)
        close?.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        delete?.setOnClickListener {
            Log.d("TAG", "showBottomSheetDialog: ")
            deleteMultipleImage()
            actionMode?.finish()
            bottomSheetDialog?.dismiss()
        }


        bottomSheetDialog?.show()
    }

    private fun deleteMultipleImage() {
        if (statusFileList != null && statusFileList?.size!! > 0) {
            val tempDeleteList = ArrayList<File>()
            tempDeleteList.addAll(statusFileList!!)
            for (i in statusFileList?.indices!!) {
                if (adapterList?.checkStatus?.get(i)!!) {
                    File(statusFileList!![i].path).delete()
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
        val uriArrayList = ArrayList<Uri>()
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
        //  shareFileImage(uriArrayList)
        Utilities.shareFileImage(this, uriArrayList)
    }

    private fun getFilePathData() {

        statusFileList = adapterList?.getList()

    }

//    fun shareFileImage(path: ArrayList<Uri>) {
//        Log.d("TAG", "shareMutliple1: ")
//        val sendIntent = Intent()
//        sendIntent.action = Intent.ACTION_SEND_MULTIPLE
//        sendIntent.putExtra(Intent.EXTRA_STREAM, path)
//        sendIntent.type = "*/*"
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        startActivity(shareIntent)
//    }

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
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {

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
            myDownloadFragment.rlTop?.visibility = View.VISIBLE
            myDownloadFragment.total_downloads?.visibility = View.VISIBLE
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

    private fun getFontColorSize(size: Int): CharSequence? {
        return Html.fromHtml(
            "Total downloads : <font color=\"#0D97F5\">${size}</font>"
        )
    }
}