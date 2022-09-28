package com.example.whatsdelete.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.modal.ApplicationModelDataList
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.responce.CategoryListData
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.Repository
import com.google.android.material.tabs.TabLayout
import com.pds.wastatustranding.R

class RootHomeFragment : Fragment() {

    private var viewpager: ViewPager2? = null
    private var tabs: TabLayout? = null
    private var viewModel: ApiDataViewModel? = null
    private var country: String? = null
    private var appId: String? = null
    private var prefs: Prefs? = null
    private var isContains: Boolean = false
    private var isHitData: Boolean = false
    private var noOfTabs: Int = 0
    private var mList: MutableList<CategoryListData>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home_root, container, false)

        viewpager = view.findViewById(R.id.viewpager)
        tabs = view.findViewById(R.id.tablayout)

        val repository = Repository(APIClient.getNetworkService())
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        ).get(ApiDataViewModel::class.java)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID

        prefs = Prefs(requireActivity())


        fetchDataFromServer()

        val mPagerAdapter = ViewPagerAdapter(this, mList)
        viewpager?.adapter = mPagerAdapter


        viewpager?.currentItem=1
        tabs?.setScrollPosition(1,0f,true)



        viewpager?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("Selected_Page", position.toString())

                tabs?.setScrollPosition(position,0f,true)
            }


        })

        tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                println("WaTrandingStatus.onTabSelected asdgnasmnasfa" + " " + tab.position)

                if (tab.position == 0) {

                } else {
                    viewpager?.currentItem=tab.position
                    WaTrandingStatus.newInstance(mList?.get(tab.position - 1)!!.cat_id,tab.position)
//                    openSubCatListWithID(tab.position - 1)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    class ViewPagerAdapter(
        fragment: Fragment,
        var mList: MutableList<CategoryListData>?
    ) :
        FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment {
            println("ViewPagerAdapter.createFragment >>>>"+position)
            var temppostion:Int

            if (position==0){
                temppostion=1
            }else{
                temppostion=position
            }


            return WaTrandingStatus.newInstance(mList?.get(temppostion-1)!!.cat_id, position)
        }



        override fun getItemCount(): Int {
            return mList?.size?.plus(1) ?: 0
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

    fun getTabViewStatic(catName: String): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_v2, null)
        val tv = v.findViewById<View>(R.id.custum_tab_text) as TextView
        tv.text = catName
        val img = v.findViewById<View>(R.id.iv_cat_icon) as ImageView
        return v
    }

    private fun callApplicationListView(position: String) {
        isContains = false
        isHitData = false

        val applicationResponceList =
            prefs?.getSubCategoryList()
        var dataList: List<ApplicationListData> = ArrayList<ApplicationListData>()
//        CoroutineScope(Dispatchers.IO).launch {
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

                    } else {


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


                } else {
//

                }

            }


        }


    }


    fun openSubCatListWithID(position: Int) {

        var catID: String? = null
        if (prefs?.getCategoryList() != null && prefs!!.getCategoryList()!!.isNotEmpty()) {
            catID = prefs?.getCategoryList()!![position].cat_id
            callApplicationListView(catID!!)
        } else {

        }


    }

    fun fetchDataFromServer() {
        if (viewModel != null) {
            if (country != null) {

                if (prefs?.getCategoryList() != null && prefs!!.getCategoryList()!!.isNotEmpty()) {

                    //add a fav tab
                    tabs?.addTab(
                        tabs?.newTab()!!.setText("Favourite")
                    )
                    val tab: TabLayout.Tab = tabs?.getTabAt(0)!!
                    tab.customView = getTabViewStatic("Favourite")


                    // Iterate over all tabs and set the custom view
                    for (i in 0 until prefs?.getCategoryList()!!.size) {
                        val cat_pos = i + 1
                        tabs?.addTab(
                            tabs?.newTab()!!.setText(prefs?.getCategoryList()!![i].cat_name),
                            cat_pos
                        )
                        val tab: TabLayout.Tab = tabs?.getTabAt(cat_pos)!!
                        tab.customView = getTabView(i, prefs?.getCategoryList()!!)
                    }

                    mList = prefs?.getCategoryList()!!
                    callApplicationListView(prefs?.getCategoryList()!![0].cat_id)
                } else {
                    prefs?.setCategoryList(null)
                    viewModel?.callApiData(CategoryListRequest(country!!, appId!!))
                        ?.observe(viewLifecycleOwner) { list ->
                            Log.d("TAG", "onActivityCreated1: adgfadsgadg 002")
                            Log.d("TAG", "onActivityCreated1: " + list.data.size)

                            prefs?.setCategoryList(list.data)

                            //add a fav tab
                            tabs?.addTab(
                                tabs?.newTab()!!.setText("Favourite")
                            )
                            val tab: TabLayout.Tab = tabs?.getTabAt(0)!!
                            tab.customView = getTabViewStatic("Favourite")

                            // Iterate over all tabs and set the custom view
                            for (i in 0 until list.data!!.size) {
                                val cat_pos = i + 1
                                tabs?.addTab(
                                    tabs?.newTab()!!.setText(list.data!![i].cat_name),
                                    cat_pos
                                )
                                val tab: TabLayout.Tab = tabs?.getTabAt(cat_pos)!!
                                tab.customView = getTabView(i, list.data!!)
                            }
                            callApplicationListView(list.data[0].cat_id)
                            mList = list.data
                        }
                }



                viewpager?.adapter?.notifyDataSetChanged()


            }
        } else {
            Log.d("TAG", "onViewCreated1: ")
        }
    }

}