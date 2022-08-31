package com.example.whatsdelete.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.whatsdelete.adapter.ImageStackAdapter
import com.example.whatsdelete.adapter.WhatsDeleteCategoryAdapter
import com.example.whatsdelete.adapter.WhatsDelteCategoryItemAdapter
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.listener.OnBackPressedInterface
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.modal.*
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.ItemOffsetView
import com.example.whatsdelete.utils.OverlapDecoration
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.WhatsDeleteRepository
import com.jatpack.wastatustranding.R
import engine.app.adshandler.AHandler
import java.io.File
import java.text.SimpleDateFormat


class WaTrandingStatus : Fragment(), setClick, openOnClick, View.OnClickListener{
    public val DATEFORMAT = SimpleDateFormat("ddMMyyyy")
    private var recyclerView: RecyclerView? = null
    private var recCategoryitem: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var whatsDeleteDataAdapter: WhatsDeleteCategoryAdapter? = null
    private var whatsDelteCategoryItemAdapter: WhatsDelteCategoryItemAdapter? = null
    private var viewModel: ApiDataViewModel? = null
    private var country: String? = null
    private var appId: String? = null
    private var preview: TextView? = null
    private var close: ImageView? = null
    private var itemOffsetView: ItemOffsetView? = null
    private var rv_statck: RecyclerView? = null
    private var bottomStack: RelativeLayout? = null
    private var rl_counter: RelativeLayout? = null
    private var itemCount: TextView? = null
    private var share: ImageView? = null
    private var shareOnFacebook: ImageView? = null
    private var shareOnInsta: ImageView? = null
    private var shareOnWhatsApp: ImageView? = null
    private var downloadTag: String? = null
    private var tempCategoriesID: String? = null
    private var remainingDownloadingFileCount: Int? = 0
    var progressDialog: ProgressDialog? = null
    private var tempImgList = ArrayList<String>()
    private var tempFileNameList = ArrayList<String>()
    private var temFileList = ArrayList<File>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated: ")
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setTitle("Downloading..")
        progressDialog?.setMessage("Please wait..")
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog?.max = 100
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID
        share = view.findViewById(R.id.share);
        share?.setOnClickListener(this)
        shareOnFacebook = view.findViewById(R.id.share_facebook)
        shareOnFacebook?.setOnClickListener(this)
        shareOnInsta = view.findViewById(R.id.share_insta)
        shareOnInsta?.setOnClickListener(this)
        shareOnWhatsApp = view.findViewById(R.id.share_whats_app)
        shareOnWhatsApp?.setOnClickListener(this)
        recCategoryitem = view.findViewById(R.id.rec_category_item);
        preview = view.findViewById(R.id.preview)
        preview?.setOnClickListener(this)
        rv_statck = view.findViewById(R.id.rv_statck)
        rv_statck?.addItemDecoration(OverlapDecoration())
        close = view.findViewById(R.id.close)
        close?.setOnClickListener(this)
        rl_counter = view.findViewById(R.id.rl_counter)
        itemCount = view.findViewById(R.id.temp_count)
        // initStackView(tempImgList)
        progressBar = view.findViewById(R.id.progress_circular)
        bottomStack = view.findViewById(R.id.bottom_stack)
        recyclerView = view.findViewById(R.id.recycler)
        recyclerView?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        val whatsDeleteRepository = WhatsDeleteRepository(APIClient.getNetworkService())
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(whatsDeleteRepository)
        ).get(ApiDataViewModel::class.java)
        itemOffsetView = activity?.let { ItemOffsetView(it, R.dimen.item_offset) }
        recCategoryitem?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))

        bottomStack?.setOnClickListener{

        }

    }

    private fun initStackView(tempImgList: ArrayList<String>) {
        if (tempImgList.size >= 6) {
            rl_counter?.visibility = View.VISIBLE
            itemCount?.visibility = View.VISIBLE
            itemCount?.text = tempImgList.size.toString()
        } else {
            rl_counter?.visibility = View.GONE
            itemCount?.visibility = View.GONE

        }
        rv_statck?.apply {
            val linearLayoutManager = LinearLayoutManager(activity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            val imageStackAdapter = ImageStackAdapter(context, tempImgList)
            layoutManager = linearLayoutManager
            adapter = imageStackAdapter

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        Log.d("TAG", "onActivityCreated: $country,$appId"
//        +" "+DATEFORMAT.format(System.currentTimeMillis()))

//        if (!AppUtils.isSameDay(requireActivity(), DATEFORMAT.format(System.currentTimeMillis()))) {

//        requireActivity().addOnBackPressedCallback(getViewLifecycleOwner(),this)
            if (viewModel != null) {
                if (country != null) {
                    Prefs(requireActivity()).setCategoryList(null)
                    viewModel?.callApiData(CategoryRequestData(country, appId))
                        ?.observe(viewLifecycleOwner) { list ->
                            recyclerView?.apply {
                                Log.d("TAG", "onActivityCreated1: " + list)
                                val linearLayoutManager = LinearLayoutManager(activity)
                                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                layoutManager = linearLayoutManager
                                whatsDeleteDataAdapter =
                                    WhatsDeleteCategoryAdapter(
                                        requireContext(),
                                        list.data,
                                        this@WaTrandingStatus,true
                                    )
                                adapter = whatsDeleteDataAdapter
//                                Prefs(requireActivity()).setCategoryList(list.data)

                                callCategoryItem(list.data[0].cat_id, true)
                            }
                        }
                }
            } else {
                Log.d("TAG", "onViewCreated1: ")


//            }
//        }

//    else {
//            println("WaTrandingStatus.onActivityCreated already hitted ")
//
//            if (Prefs(requireActivity()).getCategoryList() != null) {
//                recyclerView?.apply {
//                    Log.d(
//                        "TAG",
//                        "onActivityCreated1: " + Prefs(requireActivity()).getCategoryList()
//                    )
//                    val linearLayoutManager = LinearLayoutManager(activity)
//                    linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//                    layoutManager = linearLayoutManager
//                    whatsDeleteDataAdapter =
//                        WhatsDeleteCategoryAdapter(
//                            requireContext(),
//                            Prefs(requireActivity()).getCategoryList()!!,
//                            this@WaTrandingStatus,false
//                        )
//                    adapter = whatsDeleteDataAdapter
//
//                    callCategoryItem(Prefs(requireActivity()).getCategoryList()!![0].cat_id,false)
//                }
//            }
        }


    }

    override fun onClick(data: Data, position: Int,isServerHit:Boolean) {
        callCategoryItem(data.cat_id,isServerHit)
    }

    private fun callCategoryItem(position: String, isServerHit: Boolean) {
        tempCategoriesID = position
        progressBar?.visibility = View.VISIBLE
        println("WaTrandingStatus.callCategoryItem fasdgjhafsgh"+" "+isServerHit)
        if (country != null) {
//            if (isServerHit) {

                viewModel?.callCategoryItemData(
                    CategoryItemRequestData(
                        country,
                        position.toString(),
                        appId
                    )
                )?.observe(viewLifecycleOwner) { categoryItemList ->
                    recCategoryitem?.apply {
                        Log.d("TAG", "onActivityCreated1: $categoryItemList")
                        if (categoryItemList?.data != null && categoryItemList.data?.size!! > 0) {
                            val linearLayoutManager =
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            layoutManager = linearLayoutManager
                            whatsDelteCategoryItemAdapter =
                                WhatsDelteCategoryItemAdapter(
                                    requireContext(),
                                    categoryItemList.data,
                                    this@WaTrandingStatus,isServerHit
                                )

                            adapter = whatsDelteCategoryItemAdapter

                            progressBar?.visibility = View.GONE
//                            Prefs(requireActivity()).setSubCategoryList(null)
//                            Prefs(requireActivity()).setSubCategoryList(categoryItemList.data)
                        } else {
                            recCategoryitem?.visibility = View.GONE
                            progressBar?.visibility = View.GONE

                        }
                    }


                }
//            } else {
//                if (Prefs(requireActivity()).getSubCategoryList() != null &&
//                    Prefs(requireActivity()).getSubCategoryList()!!.isNotEmpty()
//                ) {
//                    recCategoryitem?.apply {
//                        val linearLayoutManager =
//                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                        layoutManager = linearLayoutManager
//                        whatsDelteCategoryItemAdapter =
//                            WhatsDelteCategoryItemAdapter(
//                                requireContext(),
//                                Prefs(requireActivity()).getSubCategoryList(),
//                                this@WaTrandingStatus,isServerHit
//                            )
//
//                        adapter = whatsDelteCategoryItemAdapter
//
//                        progressBar?.visibility = View.GONE
//
//                    }
//                } else {
//                    recCategoryitem?.visibility = View.GONE
//                    progressBar?.visibility = View.GONE
//                }
//            }

        }
    }

    override fun onLongClcik(view: View, position: Int) {

    }

    override fun openItem(view: View, categoryDetailItem: CategoryDetailItem) {
        if (view == view.findViewById<TextView>(R.id.rl_bottom)) {
            Log.d("TAG", "openItem:  001")
            startDownload(categoryDetailItem)
        }

        if (view == view.findViewById<TextView>(R.id.category_item_img)) {
            Log.d("TAG", "openItem: 002")
            openDetailPage(categoryDetailItem)
        }


//        else {
//            Log.d("TAG", "openItem1: ")
//            openDetailPage(categoryDetailItem)
//
//        }


    }

    private fun startDownload(categoryDetailItem: CategoryDetailItem) {
        var filename = AppUtils.getImageName(AppUtils.stringToURL(categoryDetailItem.img))
        val directory = File(
            context?.getExternalFilesDir("WA Status Tranding Gallery")?.path
        )
        if (!directory.exists())
            directory.mkdirs()
//        if (tempFileNameList != null && tempFileNameList.size >= 1 && tempFileNameList.contains(
//                filename
//            )
//        ) {
//            Toast.makeText(requireActivity(), "Image already downloaded", Toast.LENGTH_SHORT).show()
//        } else {
            PRDownloader.download(categoryDetailItem.img, directory.toString(), filename)
                .build()
                .setOnStartOrResumeListener {
                    Log.d("TAG", "onHandleIntent: ")
                    progressBar?.visibility = View.VISIBLE
                    true
                }
                .setOnPauseListener {
                    Log.d("TAG", "onHandleIntent: ")
                }
                .setOnCancelListener {
                    progressBar?.visibility = View.GONE
                    Log.d("TAG", "onHandleIntent: ")
                }
                .setOnProgressListener {

                    val currentByte = it.currentBytes.toFloat()
                    val totalByte = it.totalBytes.toFloat()
                    val percentage = (currentByte / totalByte) * 100
                    Log.d("TAG", "onHandleIntent: $percentage")
                    /* sendMessageToActivity(
                     downloadingPercentage = percentage.toInt(),
                     totalFileCount = totalDownloadingFileCount,
                     remainingFileCount = remainingDownloadingFileCount,
                     fileThumbnail = mediaUrl?.mediaUrl,
                     downloadedFilePath = directory + filename
                 )*/
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        progressBar?.visibility = View.GONE

                        Log.d("TAG", "onDownloadComplete: $directory$filename")
                        tempFileNameList.add(filename)

                        val file = File(directory, filename)
                        tempImgList.add(file.path)
                        Log.d("TAG", "onDownloadComplete1: " + tempImgList.size)
                        if (tempImgList.size > 0) {
                            bottomStack?.visibility = View.VISIBLE
                            initStackView(tempImgList)
                        } else {
                            bottomStack?.visibility = View.GONE
                        }


                        // api hit

                        postDownloadCountAPI(categoryDetailItem.id!!)
                        //success refresh list

                    }

                    override fun onError(error: com.downloader.Error?) {
                        Log.d(
                            "TAG",
                            "onError:  error ${error?.isServerError}"
                        )
                    }


                })
//        }
    }


    private fun scanPath(any: Any) {

    }

    private fun openDetailPage(categoryDetailItem: CategoryDetailItem) {



//        val ldf = DetailFragment()
//        val args = Bundle()
//        args.putString("id", categoryDetailItem.cat_id)
//        args.putString("img_url", categoryDetailItem.img)
//        ldf.arguments = args
//        activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container, ldf)
//            .addToBackStack(null).commit()


        startActivity(Intent(requireActivity(),DetailFragment::class.java)
            .putExtra("id", categoryDetailItem.cat_id)
            .putExtra("img_url", categoryDetailItem.img))

        AHandler.getInstance().showFullAds(requireActivity(),false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.preview -> {
                val ldf = ImagePreviewFragment()
                val args = Bundle()
                args.putStringArrayList("list", tempImgList)
                ldf.arguments = args
                activity?.supportFragmentManager!!.beginTransaction().replace(R.id.container, ldf)
                    .addToBackStack(null).commit()
            }
            R.id.close -> {
                bottomStack?.visibility = View.GONE
                tempImgList.clear()
                tempFileNameList.clear()
            }
            R.id.share -> {
                downloadTag = "share"
                //startDownload(imageUrl, downloadTag!!)
                val uriList = ArrayList<Uri>()
                for (i in tempImgList.indices) {
                    val uri = FileProvider.getUriForFile(
                        requireActivity()!!,
                        context?.packageName + ".provider",
                        File(tempImgList[i])
                    )
                    uriList.add(uri)
                }
                AppUtils.shareMutliple(requireActivity(), uriList)
                bottomStack?.visibility = View.GONE
                tempImgList.clear()
                tempFileNameList.clear()
                //  downloadTag?.let { ShareMultipleTask(this, it).execute(temFileList) }
            }
            R.id.share_facebook -> {
                downloadTag = "share_facebook"
                val uriList = ArrayList<Uri>()
                for (i in tempImgList.indices) {
                    val uri = FileProvider.getUriForFile(
                        requireActivity(),
                        context?.packageName + ".provider",
                        File(tempImgList[i])
                    )
                    uriList.add(uri)
                }
                AppUtils.shareOnFacebookMultiple(requireActivity(), uriList)
                bottomStack?.visibility = View.GONE
                tempImgList.clear()
                tempFileNameList.clear()
            }
            R.id.share_whats_app -> {
                downloadTag = "share_whats_app"
                val uriList = ArrayList<Uri>()
                for (i in tempImgList.indices) {
                    val uri = FileProvider.getUriForFile(
                        requireActivity(),
                        context?.packageName + ".provider",
                        File(tempImgList[i])
                    )
                    uriList.add(uri)
                }
                AppUtils.shareOnWhatsAppMultiple(requireActivity(), uriList)
                bottomStack?.visibility = View.GONE
                tempImgList.clear()
                tempFileNameList.clear()
            }
            R.id.share_insta -> {
                downloadTag = "share_insta"
                val uriList = ArrayList<Uri>()
                for (i in tempImgList.indices) {
                    val uri = FileProvider.getUriForFile(
                        requireActivity(),
                        context?.packageName + ".provider",
                        File(tempImgList[i])
                    )
                    uriList.add(uri)
                }
                AppUtils.shareOnInstaMultiple(requireActivity(), uriList)
                bottomStack?.visibility = View.GONE
                tempImgList.clear()
                tempFileNameList.clear()
            }
        }
    }


    fun postDownloadCountAPI(downloadID: String) {
        if (viewModel != null) {
            viewModel?.callCategoryDownloadCount(CategoryDownloadRequest(downloadID))
                ?.observe(viewLifecycleOwner) { list ->

//                    if (tempCategoriesID != null && !tempCategoriesID.equals("")) {
//                        callCategoryItem(tempCategoriesID!!,isServerHit)
//
//                    }

                }
        } else {
            Log.d("TAG", "onViewCreated1: ")
        }
    }

//    override fun handleOnBackPressed() {
//        TODO("Not yet implemented")
//    }


    override fun onDestroyView() {
        super.onDestroyView()
//        getActivity().removeOnBackPressedCallback(this);
    }


}