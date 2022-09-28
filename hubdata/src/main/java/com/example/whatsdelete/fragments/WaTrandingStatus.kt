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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.whatsdelete.adapter.FavCategoryItemAdapter
import com.example.whatsdelete.adapter.InstalledAppItemAdapter
import com.example.whatsdelete.adapter.WhatsDeleteCategoryAdapter
import com.example.whatsdelete.adapter.WhatsDelteCategoryItemAdapter
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.dynamictab.DynamicFragment
import com.example.whatsdelete.listener.onclickInstalledApp
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.modal.ApplicationModelDataList
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.responce.CategoryListData
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.ItemOffsetView
import com.example.whatsdelete.utils.OverlapDecoration
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.Repository
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
    private var recCategoryitem: RecyclerView? = null
    private var rv_installed_app: RecyclerView? = null
    private var fav_rv_category_item: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var whatsDeleteDataAdapter: WhatsDeleteCategoryAdapter? = null
    private var whatsDelteCategoryItemAdapter: WhatsDelteCategoryItemAdapter? = null
    private var favCategoryItemAdapter: FavCategoryItemAdapter? = null
    private var installedAppItemAdapter: InstalledAppItemAdapter? = null
    private var isContains: Boolean = false
    private var isHitData: Boolean = false
    private val ARG_SECTION_NUMBER = "section_number"
    private val ARG_TAB_SELECTED_POSITION = "ARG_TAB_SELECTED_POSITION"
    private var viewModel: ApiDataViewModel? = null
    private var preview: TextView? = null
    private var close: ImageView? = null
    private var ll_nodata_container: LinearLayout? = null
    private var sub_cat_container: LinearLayout? = null

    private var itemOffsetView: ItemOffsetView? = null
    private var rv_statck: RecyclerView? = null
    private var bottomStack: RelativeLayout? = null
    private var rl_counter: RelativeLayout? = null
    private var ll_installed_apps_container: LinearLayout? = null
    private var itemCount: TextView? = null
    private var share: ImageView? = null
    private var shareOnFacebook: ImageView? = null
    private var shareOnInsta: ImageView? = null
    private var shareOnWhatsApp: ImageView? = null

    private var txt_total: TextView? = null
    private var native_ads: LinearLayout? = null
    private var viewPager: ViewPager? = null
    private var txt_total_installed_apps: TextView? = null
    private var downloadTag: String? = null
    private var tempCategoriesID111: String? = null
    private var remainingDownloadingFileCount: Int? = 0
    var progressDialog: ProgressDialog? = null
    private var tempImgList = ArrayList<String>()
    private var tempFileNameList = ArrayList<String>()
    private var temFileList = ArrayList<File>()
    private var prefs: Prefs? = null
    private var country: String? = null
    private var appId: String? = null
    private var main_container:CoordinatorLayout?=null
    private var rl_add_new_fav:RelativeLayout?=null


//    lateinit var mainViewModel: RoomDbMainViewModel
//    private var tabs: TabLayout? = null

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


        share = view.findViewById(R.id.share)
        native_ads = view.findViewById(R.id.native_ads)
        main_container = view.findViewById(R.id.main_container)
        rl_add_new_fav = view.findViewById(R.id.rl_add_new_fav)

        share?.setOnClickListener(this)
        shareOnFacebook = view.findViewById(R.id.share_facebook)
        sub_cat_container = view.findViewById(R.id.sub_cat_container)
        ll_installed_apps_container = view.findViewById(R.id.ll_installed_apps_container)
        txt_total = view.findViewById(R.id.txt_total)
        txt_total_installed_apps = view.findViewById(R.id.txt_total_installed_apps)
        ll_nodata_container = view.findViewById(R.id.ll_nodata_container)
        shareOnFacebook?.setOnClickListener(this)
        shareOnInsta = view.findViewById(R.id.share_insta)
        shareOnInsta?.setOnClickListener(this)
        shareOnWhatsApp = view.findViewById(R.id.share_whats_app)
        shareOnWhatsApp?.setOnClickListener(this)
        recCategoryitem = view.findViewById(R.id.rec_category_item)
        rv_installed_app = view.findViewById(R.id.rv_installed_app)
        fav_rv_category_item = view.findViewById(R.id.fav_rv_category_item)
        preview = view.findViewById(R.id.preview)
        preview?.setOnClickListener(this)
        rv_statck = view.findViewById(R.id.rv_statck)
        rv_statck?.addItemDecoration(OverlapDecoration())
        close = view.findViewById(R.id.close)
//        viewPager = view.findViewById(R.id.viewpager)
        close?.setOnClickListener(this)
        rl_counter = view.findViewById(R.id.rl_counter)
        itemCount = view.findViewById(R.id.temp_count)
        // initStackView(tempImgList)
        progressBar = view.findViewById(R.id.progress_circular)
        bottomStack = view.findViewById(R.id.bottom_stack)
//        recyclerView = view.findViewById(R.id.recycler)
//        recyclerView?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        val repository = Repository(APIClient.getNetworkService())
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        ).get(ApiDataViewModel::class.java)

        itemOffsetView = activity?.let { ItemOffsetView(it, R.dimen.item_offset) }
        recCategoryitem?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        rv_installed_app?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        fav_rv_category_item?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        bottomStack?.setOnClickListener {

        }

        native_ads?.addView(AHandler.getInstance().getNativeLarge(requireActivity()))


        var sectionNumber =
            if (arguments != null) requireArguments().getString(ARG_SECTION_NUMBER) else 1

        var tabpos =
            if (arguments != null) requireArguments().getInt(ARG_TAB_SELECTED_POSITION) else 1


        if (tabpos!=null && tabpos==0){
            rl_add_new_fav?.visibility=View.VISIBLE
            main_container?.visibility=View.GONE


            rl_add_new_fav?.setOnClickListener{
                callAllApplicationListView("all")
            }

        }else{
            rl_add_new_fav?.visibility=View.GONE
            main_container?.visibility=View.VISIBLE

        }

        if (tabpos!=null && tabpos>0 && sectionNumber!=null){
            Log.d("DynamicFragment", "Hello onCreate hi section number $sectionNumber  $tabpos ")
            callApplicationListView(sectionNumber.toString())
        }

    }




    override fun onClick(data: CategoryListData, position: String) {
        println("WaTrandingStatus.onClick fgjdszkgbskbja opopopopo")
//        callApplicationListView(position)
    }


//    }

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
        appContainsList.clear()

        GlobalScope.launch {
            val packList: List<PackageInfo> = activity.packageManager.getInstalledPackages(0)
            val apps = arrayOfNulls<String>(packList.size)
            appContainsList.clear()
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
                                webAppList[pos].color,
                                false
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

                txt_total_installed_apps?.setText(appContainsList.size.toString())
                ll_installed_apps_container?.visibility = View.VISIBLE
                rv_installed_app?.apply {

                    println("WaTrandingStatus.callApplicationListView hihhih 001")
                    if (appContainsList != null && appContainsList.size!! > 0) {
//                                ll_nodata_container?.visibility=View.GONE
//                                sub_cat_container?.visibility=View.VISIBLE
                        println("WaTrandingStatus.callApplicationListView hihhih 002" + " " + appContainsList.size)
                        installedAppItemAdapter =
                            InstalledAppItemAdapter(
                                requireActivity(),
                                appContainsList,
                                this@WaTrandingStatus
                            )





                        adapter = installedAppItemAdapter
                        progressBar?.visibility = View.GONE
                    } else {
//                            recCategoryitem?.visibility = View.GONE
//                                ll_nodata_container?.visibility=View.VISIBLE
//                                sub_cat_container?.visibility=View.GONE
                        ll_installed_apps_container?.visibility = View.GONE
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


//    private fun setUpTabIcons(position: Int, data: List<CategoryListData>) {
//        val chatView: View = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_tab_v2, null)
//       val tv_cat_name= chatView.findViewById<TextView>(R.id.custum_tab_text)
//        val iv_cat = chatView.findViewById<ImageView>(R.id.iv_cat_icon)
//
//        tv_cat_name?.setText(data[position].cat_name)
//        Glide.with(requireActivity()).load(data[position].cat_image).into(iv_cat)
//
//        tabs?.getTabAt(position)?.customView = chatView
//
//    }

    companion object {
        fun newInstance(sectionNumber: String, position: Int): WaTrandingStatus {

            val fragment = WaTrandingStatus()
            val args = Bundle()
            args.putString(fragment.ARG_SECTION_NUMBER, sectionNumber)
            args.putInt(fragment.ARG_TAB_SELECTED_POSITION, position)
            fragment.arguments = args
//            fragment.callApplicationListView(sectionNumber)
            return fragment
        }
    }



    private fun callApplicationListView(position: String) {
        isContains = false
        isHitData = false
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID

        val applicationResponceList =
            prefs?.getSubCategoryList()
        var dataList: List<ApplicationListData> = ArrayList<ApplicationListData>()
//        CoroutineScope(Dispatchers.IO).launch {
        progressBar?.visibility = View.VISIBLE
        if (country != null) {
//            if (isServerHit) {


            var appContainsList: ArrayList<ApplicationModelDataList> =
                ArrayList<ApplicationModelDataList>()
            if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                appContainsList.clear()
                for (i in applicationResponceList!!.indices) {

                    appContainsList.add(
                        ApplicationModelDataList(
                            applicationResponceList!![i].data,
                            applicationResponceList!![i].cat_id
                        )
                    )

                    if (applicationResponceList!![i].cat_id.equals(position)) {
                        println("WaTrandingStatus.callApplicationListView hi conatines data ")
                        isContains = true

                    }
                }
            }
            if (!isContains) {
                isContains = false
                println("WaTrandingStatus.callApplicationListView hi conatines data 001")

                viewModel?.callApplicationListData(
                    ApplicationListRequest(
                        appId!!,
                        position,
                        country!!

                    )
                )
                viewModel?.categoryItemLiveData?.observe(viewLifecycleOwner) { categoryItemList ->

                    if (categoryItemList != null) {
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
//                            if (!isHitData){
//                                isHitData=true
                        println("WaTrandingStatus.callApplicationListView gfdjshgaja opopop " + " " + categoryItemList.data.toString())
                        appContainsList.add(
                            ApplicationModelDataList(
                                categoryItemList.data!!,
                                position
                            )
                        )


                        println("WaTrandingStatus.callApplicationListView gfdjshgaja aaaa " + " " + appContainsList.toString())



                        prefs?.setSubCategoryList(null)
                        prefs?.setSubCategoryList(appContainsList)
                        println(
                            "WaTrandingStatus.callApplicationListView hi this is log 001" + " " +
                                    prefs?.getSubCategoryList()!!.size + " " + position
                        )

                        val applicationResponceList =
                            prefs?.getSubCategoryList()
                        if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                            for (i in applicationResponceList!!.indices) {
                                if (applicationResponceList!![i].cat_id.equals(position)) {
                                    dataList = applicationResponceList!![i].data
                                    println(
                                        "WaTrandingStatus.callApplicationListView hi this is log 002" + " " +
                                                dataList.toString()
                                    )

                                }
                            }
                        }

                        recCategoryitem?.apply {

                            if (applicationResponceList != null && applicationResponceList?.size!! > 0) {
                                println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 aaaa" + " " + dataList.toString())

                                txt_total?.setText(
                                    dataList!!.size.toString()
                                )

                                ll_nodata_container?.visibility = View.GONE
                                sub_cat_container?.visibility = View.VISIBLE
                                whatsDelteCategoryItemAdapter =
                                    WhatsDelteCategoryItemAdapter(
                                        requireContext(),
                                        dataList,
                                        this@WaTrandingStatus
                                    )
                                adapter = whatsDelteCategoryItemAdapter
                                progressBar?.visibility = View.GONE
                                getallapps(requireActivity(), dataList)


//                            Prefs(requireActivity()).setSubCategoryList(null)
//                            Prefs(requireActivity()).setSubCategoryList(categoryItemList.data)
                            } else {
//                            recCategoryitem?.visibility = View.GONE
                                ll_nodata_container?.visibility = View.VISIBLE
                                sub_cat_container?.visibility = View.GONE
                                progressBar?.visibility = View.GONE

                            }
                        }


//                        }

                    } else {
                        progressBar?.visibility = View.GONE
                        sub_cat_container?.visibility = View.GONE

                        ll_nodata_container?.visibility = View.VISIBLE

                    }
                }

            } else {

                println("WaTrandingStatus.callApplicationListView gfdjshgaja aaaa ccc" + " " + appContainsList.toString())


                prefs?.setSubCategoryList(appContainsList)
                println(
                    "WaTrandingStatus.callApplicationListView hi this is log 001" + " " +
                            prefs?.getSubCategoryList()!!.size + " " + position
                )

                val applicationResponceList =
                    prefs?.getSubCategoryList()
                if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                    for (i in applicationResponceList!!.indices) {
                        if (applicationResponceList!![i].cat_id.equals(position)) {
                            dataList = applicationResponceList!![i].data
                            println(
                                "WaTrandingStatus.callApplicationListView hi this is log 002" + " " +
                                        dataList.toString()
                            )

                        }
                    }
                }

                if (applicationResponceList != null && applicationResponceList?.size!! > 0) {

                    ll_nodata_container?.visibility = View.GONE
                    sub_cat_container?.visibility = View.VISIBLE
                    recCategoryitem?.apply {


                        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 aaaa" + " " + dataList.toString())



                        txt_total?.setText(
                            dataList!!.size.toString()
                        )

                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
                        whatsDelteCategoryItemAdapter =
                            WhatsDelteCategoryItemAdapter(
                                requireContext(),
                                dataList,
                                this@WaTrandingStatus
                            )
                        adapter = whatsDelteCategoryItemAdapter
                        progressBar?.visibility = View.GONE
                        getallapps(requireActivity(), dataList)
                    }

//                            Prefs(requireActivity()).setSubCategoryList(null)
//                            Prefs(requireActivity()).setSubCategoryList(categoryItemList.data)
                } else {
//                            recCategoryitem?.visibility = View.GONE
                    ll_nodata_container?.visibility = View.VISIBLE
                    sub_cat_container?.visibility = View.GONE
                    progressBar?.visibility = View.GONE

                }

            }


        }


    }


    private fun callAllApplicationListView(position: String) {
        isContains = false
        isHitData = false
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID

        val applicationResponceList =
            prefs?.getSubCategoryList()
        var dataList: List<ApplicationListData> = ArrayList<ApplicationListData>()
//        CoroutineScope(Dispatchers.IO).launch {
        progressBar?.visibility = View.VISIBLE
        if (country != null) {
//            if (isServerHit) {


            var appContainsList: ArrayList<ApplicationModelDataList> =
                ArrayList<ApplicationModelDataList>()
            if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                appContainsList.clear()
                for (i in applicationResponceList!!.indices) {

                    appContainsList.add(
                        ApplicationModelDataList(
                            applicationResponceList!![i].data,
                            applicationResponceList!![i].cat_id
                        )
                    )

                    if (applicationResponceList!![i].cat_id.equals(position)) {
                        println("WaTrandingStatus.callApplicationListView hi conatines data ")
                        isContains = true

                    }
                }
            }
            if (!isContains) {
                isContains = false
                println("WaTrandingStatus.callApplicationListView hi conatines data 001")

                viewModel?.callApplicationListData(
                    ApplicationListRequest(
                        appId!!,
                        position,
                        country!!

                    )
                )
                viewModel?.categoryItemLiveData?.observe(viewLifecycleOwner) { categoryItemList ->

                    if (categoryItemList != null) {
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
//                            if (!isHitData){
//                                isHitData=true
                        println("WaTrandingStatus.callApplicationListView gfdjshgaja opopop " + " " + categoryItemList.data.toString())
                        appContainsList.add(
                            ApplicationModelDataList(
                                categoryItemList.data!!,
                                position
                            )
                        )


                        println("WaTrandingStatus.callApplicationListView gfdjshgaja aaaa " + " " + appContainsList.toString())



                        prefs?.setSubCategoryList(null)
                        prefs?.setSubCategoryList(appContainsList)
                        println(
                            "WaTrandingStatus.callApplicationListView hi this is log 001" + " " +
                                    prefs?.getSubCategoryList()!!.size + " " + position
                        )

                        val applicationResponceList =
                            prefs?.getSubCategoryList()
                        if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                            for (i in applicationResponceList!!.indices) {
                                if (applicationResponceList!![i].cat_id.equals(position)) {
                                    dataList = applicationResponceList!![i].data
                                    println(
                                        "WaTrandingStatus.callApplicationListView hi this is log 002" + " " +
                                                dataList.toString()
                                    )

                                }
                            }
                        }

                        fav_rv_category_item?.apply {
                            if (applicationResponceList != null && applicationResponceList?.size!! > 0) {
                                txt_total?.setText(
                                    dataList!!.size.toString()
                                )
                                ll_nodata_container?.visibility = View.GONE
                                sub_cat_container?.visibility = View.VISIBLE
                                favCategoryItemAdapter =
                                    FavCategoryItemAdapter(
                                        requireContext(),
                                        dataList,
                                        this@WaTrandingStatus
                                    )
                                adapter = favCategoryItemAdapter
                                progressBar?.visibility = View.GONE
                                getallapps(requireActivity(), dataList)
                            } else {
                                progressBar?.visibility = View.GONE

                            }
                        }

                    } else {
                        progressBar?.visibility = View.GONE

                    }
                }

            } else {
                prefs?.setSubCategoryList(appContainsList)
                val applicationResponceList =
                    prefs?.getSubCategoryList()
                if (applicationResponceList != null && applicationResponceList!!.size > 0) {
                    for (i in applicationResponceList!!.indices) {
                        if (applicationResponceList!![i].cat_id.equals(position)) {
                            dataList = applicationResponceList!![i].data
                        }
                    }
                }

                if (applicationResponceList != null && applicationResponceList?.size!! > 0) {
                    fav_rv_category_item?.apply {
                        txt_total?.setText(
                            dataList!!.size.toString()
                        )

                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
                        favCategoryItemAdapter =
                            FavCategoryItemAdapter(
                                requireContext(),
                                dataList,
                                this@WaTrandingStatus
                            )
                        adapter = favCategoryItemAdapter
                        progressBar?.visibility = View.GONE
                        getallapps(requireActivity(), dataList)
                    }

                } else {

                    progressBar?.visibility = View.GONE

                }

            }


        }


    }

}