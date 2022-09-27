package com.example.whatsdelete.fragments

//import com.example.whatsdelete.room.database.CategoryDatabase
//import com.example.whatsdelete.room.entity.SubCatEntity
//import com.example.whatsdelete.room.repositry.SubCatRepository
//import com.example.whatsdelete.room.viewmodulefactory.RoomDbMainViewModel
//import com.example.whatsdelete.room.viewmodulefactory.RoomDbMainViewModelFactory
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
import com.bumptech.glide.Glide
import com.example.whatsdelete.adapter.InstalledAppItemAdapter
import com.example.whatsdelete.adapter.WhatsDeleteCategoryAdapter
import com.example.whatsdelete.adapter.WhatsDelteCategoryItemAdapter
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.listener.onclickInstalledApp
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.modal.ApplicationModelDataList
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
import com.example.whatsdelete.viewmodel.Repository
import com.google.android.material.tabs.TabLayout
import com.pds.wastatustranding.R
import engine.app.adshandler.AHandler
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat


class WaTrandingStatus : Fragment(), setClick, openOnClick, onclickInstalledApp,
    View.OnClickListener {
    public val DATEFORMAT = SimpleDateFormat("ddMMyyyy")
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
    private var ll_installed_apps_container: LinearLayout? = null
    private var itemCount: TextView? = null
    private var share: ImageView? = null
    private var shareOnFacebook: ImageView? = null
    private var shareOnInsta: ImageView? = null
    private var shareOnWhatsApp: ImageView? = null
    private var tabs: TabLayout? = null
    private var txt_total: TextView? = null
    private var native_ads: LinearLayout? = null
    private var txt_total_installed_apps: TextView? = null
    private var downloadTag: String? = null
    private var tempCategoriesID111: String? = null
    private var remainingDownloadingFileCount: Int? = 0
    var progressDialog: ProgressDialog? = null
    private var tempImgList = ArrayList<String>()
    private var tempFileNameList = ArrayList<String>()
    private var temFileList = ArrayList<File>()
    private var prefs: Prefs? = null
    private var isContains: Boolean = false
    private var isHitData: Boolean = false

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
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID
        share = view.findViewById(R.id.share)
        native_ads = view.findViewById(R.id.native_ads)
        tabs = view.findViewById(R.id.tablayout)
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
        bottomStack?.setOnClickListener {

        }

        native_ads?.addView(AHandler.getInstance().getNativeLarge(requireActivity()))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("TAG", "onActivityCreated1: adgfadsgadg 001")
        super.onActivityCreated(savedInstanceState)
        if (viewModel != null) {
            if (country != null) {


                if (prefs?.getCategoryList() != null && prefs!!.getCategoryList()!!.isNotEmpty()) {





                    // Iterate over all tabs and set the custom view
                    for (i in 0 until prefs?.getCategoryList()!!.size) {

                        tabs?.addTab(
                            tabs?.newTab()!!.setText(prefs?.getCategoryList()!![i].cat_name)
                        )
                        val tab: TabLayout.Tab = tabs?.getTabAt(i)!!
                        tab.customView = getTabView(i, prefs?.getCategoryList()!!)
                    }

                    callApplicationListView(prefs?.getCategoryList()!![0].cat_id)
                } else {
                    prefs?.setCategoryList(null)
                    viewModel?.callApiData(CategoryListRequest(country!!, appId!!))
                        ?.observe(viewLifecycleOwner) { list ->
                            Log.d("TAG", "onActivityCreated1: adgfadsgadg 002")
                            Log.d("TAG", "onActivityCreated1: " + list.data.size)






                            prefs?.setCategoryList(list.data)


                            // Iterate over all tabs and set the custom view
                            for (i in 0 until list.data!!.size) {
                                tabs?.addTab(tabs?.newTab()!!.setText(list.data!![i].cat_name))
                                val tab: TabLayout.Tab = tabs?.getTabAt(i)!!
                                tab.customView = getTabView(i, list.data!!)
                            }
                            callApplicationListView(list.data[0].cat_id)
                        }
                }



                tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        println("WaTrandingStatus.onTabSelected asdgnasmnasfa" + " " + tab.position)

                        if (tab.position==0){

                        }else{
                            openSubCatListWithID(tab.position)
                        }

                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {}
                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })

            }
        } else {
            Log.d("TAG", "onViewCreated1: ")
        }


    }

    override fun onClick(data: CategoryListData, position: String) {
        println("WaTrandingStatus.onClick fgjdszkgbskbja opopopopo")
        callApplicationListView(position)
    }

    private fun callApplicationListView(position: String) {
        isContains = false
        isHitData = false

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


    fun openSubCatListWithID(position: Int) {

        var catID: String? = null
        if (prefs?.getCategoryList() != null && prefs!!.getCategoryList()!!.isNotEmpty()) {
            catID = prefs?.getCategoryList()!![position].cat_id
            callApplicationListView(catID!!)
        } else {

        }


    }


    fun getTabView(position: Int, data: List<CategoryListData>): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_v2, null)
        val tv = v.findViewById<View>(R.id.custum_tab_text) as TextView
        tv.text = data[position].cat_name
        val img = v.findViewById<View>(R.id.iv_cat_icon) as ImageView
        Glide.with(requireActivity()).load(data[position].cat_image).into(img)
        return v
    }

}