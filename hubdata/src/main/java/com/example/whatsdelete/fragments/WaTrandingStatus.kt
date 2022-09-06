package com.example.whatsdelete.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsdelete.adapter.InstalledAppItemAdapter
import com.example.whatsdelete.adapter.WhatsDeleteCategoryAdapter
import com.example.whatsdelete.adapter.WhatsDelteCategoryItemAdapter
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.listener.onclickInstalledApp
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.responce.CategoryListData
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.ItemOffsetView
import com.example.whatsdelete.utils.OverlapDecoration
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.WhatsDeleteRepository
import com.pds.wastatustranding.R
import engine.app.adshandler.AHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat


class WaTrandingStatus : Fragment(), setClick, openOnClick, onclickInstalledApp,
    View.OnClickListener {
    public val DATEFORMAT = SimpleDateFormat("ddMMyyyy")
    private var recyclerView: RecyclerView? = null
    private var recCategoryitem: RecyclerView? = null
    private var rv_installed_app: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var whatsDeleteDataAdapter: WhatsDeleteCategoryAdapter? = null
    private var whatsDelteCategoryItemAdapter: WhatsDelteCategoryItemAdapter? = null
    private var installedAppItemAdapter: InstalledAppItemAdapter? = null
    private var viewModel: ApiDataViewModel? = null
    private var country: String? = null
    private var appId: String? = null
    private var preview: TextView? = null
    private var close: ImageView? = null
    private var ll_nodata_container: LinearLayout? = null
    private var sub_cat_container: LinearLayout? = null

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
    private var prefs: Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home_cat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated: ")
        prefs = Prefs(requireActivity())
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
        sub_cat_container = view.findViewById(R.id.sub_cat_container)
        ll_nodata_container = view.findViewById(R.id.ll_nodata_container)
        shareOnFacebook?.setOnClickListener(this)
        shareOnInsta = view.findViewById(R.id.share_insta)
        shareOnInsta?.setOnClickListener(this)
        shareOnWhatsApp = view.findViewById(R.id.share_whats_app)
        shareOnWhatsApp?.setOnClickListener(this)
        recCategoryitem = view.findViewById(R.id.rec_category_item);
        rv_installed_app = view.findViewById(R.id.rv_installed_app);
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
        rv_installed_app?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        // view.findViewById<LinearLayout>(R.id.native_ads).addView(AHandler.getInstance().getNativeLarge(requireActivity()))
        bottomStack?.setOnClickListener {

        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("TAG", "onActivityCreated1: adgfadsgadg 001")
        super.onActivityCreated(savedInstanceState)
        if (viewModel != null) {
            if (country != null) {
                if (prefs?.getCategoryList() != null && prefs!!.getCategoryList()!!.isNotEmpty()) {

                    recyclerView?.apply {

                        whatsDeleteDataAdapter =
                            WhatsDeleteCategoryAdapter(
                                requireContext(),
                                prefs?.getCategoryList()!!,
                                this@WaTrandingStatus
                            )
                        adapter = whatsDeleteDataAdapter


                        callApplicationListView(prefs?.getCategoryList()!![0].cat_id)
                    }


                } else {
                    prefs?.setCategoryList(null)
                    viewModel?.callApiData(CategoryListRequest(country!!, appId!!))
                        ?.observe(viewLifecycleOwner) { list ->
                            Log.d("TAG", "onActivityCreated1: adgfadsgadg 002")
                            Log.d("TAG", "onActivityCreated1: " + list.data.size)

                            recyclerView?.apply {

                                whatsDeleteDataAdapter =
                                    WhatsDeleteCategoryAdapter(
                                        requireContext(),
                                        list.data,
                                        this@WaTrandingStatus
                                    )
                                adapter = whatsDeleteDataAdapter

                                prefs?.setCategoryList(list.data)

                                callApplicationListView(list.data[0].cat_id)
                            }
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
//                    callApplicationListView(Prefs(requireActivity()).getCategoryList()!![0].cat_id,false)
//                }
//            }
        }


    }

    override fun onClick(data: CategoryListData, position: String) {
        callApplicationListView(position)
    }

    private fun callApplicationListView(position: String) {
        tempCategoriesID = position
        progressBar?.visibility = View.VISIBLE
        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh" + " " + position)
        if (country != null) {
//            if (isServerHit) {


            val applicationResponceList =
                prefs?.getApplicationLsit(requireActivity())!!.get(position)



            if (applicationResponceList != null && !applicationResponceList!!.equals("")) {
                println(
                    "WaTrandingStatus.callApplicationListView gshjdgakdh 001" + " " +
                            applicationResponceList!!.size
                )

                recCategoryitem?.apply {

                    if (applicationResponceList != null && applicationResponceList?.size!! > 0) {
                        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 aaaa" + " ")
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE

//                            val linearLayoutManager =
//                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                            layoutManager = linearLayoutManager
                        whatsDelteCategoryItemAdapter =
                            WhatsDelteCategoryItemAdapter(
                                requireContext(),
                                applicationResponceList,
                                this@WaTrandingStatus
                            )


                        adapter = whatsDelteCategoryItemAdapter

                        progressBar?.visibility = View.GONE

                        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009" + " ")

                        getallapps(requireActivity(), applicationResponceList)


//                            Prefs(requireActivity()).setSubCategoryList(null)
//                            Prefs(requireActivity()).setSubCategoryList(categoryItemList.data)
                    } else {
//                            recCategoryitem?.visibility = View.GONE
                        ll_nodata_container?.visibility = View.VISIBLE
                        sub_cat_container?.visibility = View.GONE
                        progressBar?.visibility = View.GONE

                    }
                }


            } else {
                println("WaTrandingStatus.callApplicationListView gshjdgakdh 002")


                viewModel?.callApplicationListData(
                    ApplicationListRequest(
                        appId!!,
                        position,
                        country!!

                    )
                )?.observe(viewLifecycleOwner) { categoryItemList ->
                    println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 bbb" + " ")



                    if (categoryItemList != null) {
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
                        val aMap = HashMap<String, List<ApplicationListData>>()
                        aMap[position] = categoryItemList.data
                        prefs?.setApplicationList(requireActivity(), aMap)

                        recCategoryitem?.apply {

                            if (categoryItemList?.data != null && categoryItemList.data?.size!! > 0) {
                                println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 aaaa" + " ")
                                ll_nodata_container?.visibility = View.GONE
                                sub_cat_container?.visibility = View.VISIBLE

//                            val linearLayoutManager =
//                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//                            layoutManager = linearLayoutManager
                                whatsDelteCategoryItemAdapter =
                                    WhatsDelteCategoryItemAdapter(
                                        requireContext(),
                                        categoryItemList.data,
                                        this@WaTrandingStatus
                                    )


                                adapter = whatsDelteCategoryItemAdapter

                                progressBar?.visibility = View.GONE

                                println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009" + " ")

                                getallapps(requireActivity(), categoryItemList.data)


//                            Prefs(requireActivity()).setSubCategoryList(null)
//                            Prefs(requireActivity()).setSubCategoryList(categoryItemList.data)
                            } else {
//                            recCategoryitem?.visibility = View.GONE
                                ll_nodata_container?.visibility = View.VISIBLE
                                sub_cat_container?.visibility = View.GONE
                                progressBar?.visibility = View.GONE

                            }
                        }
                    } else {
                        ll_nodata_container?.visibility = View.VISIBLE
                        sub_cat_container?.visibility = View.GONE
                        progressBar?.visibility = View.GONE
                    }


                }


            }


        }
    }

    override fun onLongClcik(view: View, position: Int) {

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


//    override fun handleOnBackPressed() {
//        TODO("Not yet implemented")
//    }


    override fun onDestroyView() {
        super.onDestroyView()
//        getActivity().removeOnBackPressedCallback(this);
    }

    override fun openAppInWebView(appUrl: String, toolbarColor: String) {
        if (appUrl != null && !appUrl.equals("")) {
            AppUtils.openCustomTab(requireActivity(), Uri.parse(appUrl), toolbarColor)
        }
    }


    fun getallapps(activity: Activity, webAppList: List<ApplicationListData>) {
        // get list of all the apps installed
        var appContainsList: ArrayList<ApplicationListData> = ArrayList<ApplicationListData>()


        GlobalScope.launch {
            val packList: List<PackageInfo> = activity.packageManager.getInstalledPackages(0)
            val apps = arrayOfNulls<String>(packList.size)
            for (i in packList.indices) {
                val packInfo: PackageInfo = packList[i]
                apps[i] = packInfo.applicationInfo.packageName
                println("WaTrandingStatus.callApplicationListView hhifhafhia 001" + " " + apps[i])

                for (pos in webAppList.indices) {
                    println("WaTrandingStatus.callApplicationListView hhifhafhia 002" + " " + webAppList[pos].package_name)

                    if (apps[i].equals(webAppList[pos].package_name)) {
                        println("WaTrandingStatus.callApplicationListView hhifhafhia 003" + " " + webAppList[pos].package_name)


                        appContainsList.add(
                            ApplicationListData(
                                webAppList[pos].app_name,
                                webAppList[pos].cat_id,
                                webAppList[pos].id,
                                webAppList[pos].image,
                                webAppList[pos].package_name,
                                webAppList[pos].click_url,
                                webAppList[pos].color
                            )
                        )


//                            appContainsList=   object : java.util.ArrayList<ApplicationListData>() {
//                                init {
//                                    add(ApplicationListData(
//                                        webAppList[pos].app_name,
//                                        webAppList[pos].cat_id,
//                                        webAppList[pos].id,
//                                        webAppList[pos].image,
//                                        webAppList[pos].package_name,
//
//                                        ))
//                                }
//
//                            }


                    }
                }
            }

            withContext(Dispatchers.Main) {
                println("WaTrandingStatus.callApplicationListView hhifhafhia 006" + " " + appContainsList.size)

                rv_installed_app?.apply {

                    println("WaTrandingStatus.callApplicationListView hihhih 001")
                    if (appContainsList != null && appContainsList.size!! > 0) {
//                                ll_nodata_container?.visibility=View.GONE
//                                sub_cat_container?.visibility=View.VISIBLE
                        println("WaTrandingStatus.callApplicationListView hihhih 002" + " " + appContainsList.size)
                        installedAppItemAdapter =
                            InstalledAppItemAdapter(
                                requireContext(),
                                appContainsList,
                                this@WaTrandingStatus
                            )





                        adapter = installedAppItemAdapter
                        progressBar?.visibility = View.GONE
                    } else {
//                            recCategoryitem?.visibility = View.GONE
//                                ll_nodata_container?.visibility=View.VISIBLE
//                                sub_cat_container?.visibility=View.GONE
                        progressBar?.visibility = View.GONE

                    }
                }
            }
        }


        // set all the apps name in list view

        // write total count of apps available.
        println("WaTrandingStatus.callApplicationListView hhifhafhia 004" + " " + appContainsList.size)


    }

    override fun onclickInstalledApp(appPackageName: String, toolbarColor: String) {
        AppUtils.openInstalledApp(requireActivity(), appPackageName)
    }


}