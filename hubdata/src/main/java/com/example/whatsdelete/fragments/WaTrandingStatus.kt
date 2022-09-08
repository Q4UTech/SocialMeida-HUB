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
import com.bumptech.glide.Glide
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
//import com.example.whatsdelete.room.database.CategoryDatabase
//import com.example.whatsdelete.room.entity.SubCatEntity
//import com.example.whatsdelete.room.repositry.SubCatRepository
//import com.example.whatsdelete.room.viewmodulefactory.RoomDbMainViewModel
//import com.example.whatsdelete.room.viewmodulefactory.RoomDbMainViewModelFactory
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.ItemOffsetView
import com.example.whatsdelete.utils.OverlapDecoration
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.Repository
import com.google.android.material.tabs.TabLayout
import com.pds.wastatustranding.R
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
    private var txt_total: TextView? = null
    private var txt_total_installed_apps: TextView? = null
    private var downloadTag: String? = null
    private var tempCategoriesID: String? = null
    private var remainingDownloadingFileCount: Int? = 0
    var progressDialog: ProgressDialog? = null
    private var tempImgList = ArrayList<String>()
    private var tempFileNameList = ArrayList<String>()
    private var temFileList = ArrayList<File>()
    private var prefs: Prefs? = null
    private  var isContains:Boolean=false

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
//        tabs = view.findViewById(R.id.tabs)
        share?.setOnClickListener(this)
        shareOnFacebook = view.findViewById(R.id.share_facebook)
        sub_cat_container = view.findViewById(R.id.sub_cat_container)
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
        recyclerView = view.findViewById(R.id.recycler)
        recyclerView?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        val repository = Repository(APIClient.getNetworkService())
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        ).get(ApiDataViewModel::class.java)
        itemOffsetView = activity?.let { ItemOffsetView(it, R.dimen.item_offset) }
        recCategoryitem?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))
        rv_installed_app?.addItemDecoration(ItemOffsetView(requireActivity(), R.dimen.item_offset))


//        val quoteDAO = CategoryDatabase.getDatabase(requireActivity()).SubcatDAO()
//        val repository12 = SubCatRepository(quoteDAO)
//        mainViewModel = ViewModelProvider(
//            this,
//            RoomDbMainViewModelFactory(repository12)
//        ).get(RoomDbMainViewModel::class.java)


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

        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh opopopopop" + " " + prefs?.getSubCategoryList()?.size
                +" "+prefs?.getSubCategoryID()!!.size+" "+
                prefs?.getSubCategoryList()?.toString() +" "+prefs?.getSubCategoryID().toString())



        if (country != null) {
//            if (isServerHit) {
            val applicationResponceList =
                prefs?.getApplicationLsit(requireActivity())!!.get(position)

            println(
                "WaTrandingStatus.callApplicationListView sghjasdfgajshd kaun h wooo" + " " +
                        applicationResponceList.toString()
            )




            if (applicationResponceList != null && !applicationResponceList!!.equals("")) {
                println(
                    "WaTrandingStatus.callApplicationListView gshjdgakdh aaaaaaa 001" + " " + prefs?.getApplicationLsit(
                        requireActivity()
                    )!!.size
                )

                txt_total?.setText(
                    prefs?.getApplicationLsit(
                        requireActivity()
                    )!!.size.toString()
                )

                recCategoryitem?.apply {

                    if (applicationResponceList != null && applicationResponceList?.size!! > 0) {
                        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 aaaa" + " " + applicationResponceList.size)
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
                        whatsDelteCategoryItemAdapter =
                            WhatsDelteCategoryItemAdapter(
                                requireContext(),
                                applicationResponceList,
                                this@WaTrandingStatus
                            )
                        adapter = whatsDelteCategoryItemAdapter
                        progressBar?.visibility = View.GONE
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
                )
                viewModel?.categoryItemLiveData?.observe(viewLifecycleOwner) { categoryItemList ->
                    txt_total?.setText(categoryItemList.data.size.toString())
                    if (categoryItemList != null) {
                        ll_nodata_container?.visibility = View.GONE
                        sub_cat_container?.visibility = View.VISIBLE
//                        mainViewModel.insertQuotes(SubCatEntity(position,categoryItemList.data.toString()))






//                        val aMap = HashMap<String, List<ApplicationListData>>()
//                        aMap[position] = categoryItemList.data
//                        prefs?.setApplicationList(requireActivity(), aMap)



                        var mlistStringId: ArrayList<String> = ArrayList<String>()
                        if (prefs?.getSubCategoryID()!=null && prefs?.getSubCategoryID()!!.size>0){
                            for (i in prefs?.getSubCategoryID()!!.indices){
                                if (!prefs?.getSubCategoryID()!![i].equals(position)){
                                    mlistStringId.add(prefs?.getSubCategoryID()!![i])
                                }
                            }
                        }
                        mlistStringId.add(position)
                        prefs?.setSubCategoryID(mlistStringId)


                        var appContainsList: ArrayList<ApplicationListData> = ArrayList<ApplicationListData>()

                        if (prefs?.getSubCategoryList()!=null && prefs?.getSubCategoryList()!!.size>0){
                            for (i in prefs?.getSubCategoryList()!!.indices){
                                if (!prefs?.getSubCategoryList()!![i].cat_id.equals(position)){
                                    appContainsList.add(prefs?.getSubCategoryList()!![i])
                                }else{
                                    isContains=true
                                }
                            }
                        }
                        if (!isContains){
                            isContains=false
                            appContainsList.add(categoryItemList.data!![Integer.parseInt(position)])

                        }
                        prefs?.setSubCategoryList(appContainsList)




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

                    viewModel?.categoryItemLiveData?.removeObservers(this)

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

}