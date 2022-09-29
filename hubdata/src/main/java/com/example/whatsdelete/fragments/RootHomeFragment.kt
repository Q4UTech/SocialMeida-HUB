package com.example.whatsdelete.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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
import com.google.android.material.tabs.TabLayoutMediator
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
        country = AppUtils.getCountryCode(requireActivity())
        appId = Constants.APP_ID
        prefs = Prefs(requireActivity())
        fetchDataFromServer()
        return view
    }

    private fun updateVewPager2() {

        val mPagerAdapter = ViewPagerAdapter(this, mList)
        viewpager?.adapter = mPagerAdapter
        viewpager?.setCurrentItem(1, true)
        TabLayoutMediator(
            tabs!!, viewpager!!
        ) { tab, position ->

            if (position == 0) {
                tab.customView = getTabViewStatic("Favourite")
            } else {
                tab.customView = getTabView(position - 1, prefs?.getCategoryList()!!)
            }


        }.attach()
    }


    class ViewPagerAdapter(
        fragment: Fragment,
        var mList: MutableList<CategoryListData>?
    ) :
        FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment {
            val temppostion: Int
            if (position == 0) {
                temppostion = 1
            } else {
                temppostion = position
            }


            return WaTrandingStatus.newInstance(mList?.get(temppostion - 1)!!.cat_id, position)
        }


        override fun getItemCount(): Int {
            return mList?.size?.plus(1) ?: 0
        }
    }

    private fun getTabView(position: Int, data: List<CategoryListData>): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_v2, null)
        val tv = v.findViewById<View>(R.id.custum_tab_text) as TextView
        tv.text = data[position].cat_name
        val img = v.findViewById<View>(R.id.iv_cat_icon) as ImageView
        Glide.with(requireActivity()).load(data[position].cat_image).into(img)
        return v
    }

    private fun getTabViewStatic(catName: String): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_v2, null)
        val tv = v.findViewById<View>(R.id.custum_tab_text) as TextView
        tv.text = catName
        val img = v.findViewById<View>(R.id.iv_cat_icon) as ImageView
        Glide.with(requireActivity()).load(R.drawable.ic_tab_fav).into(img)

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
                    viewpager?.adapter?.notifyDataSetChanged()
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

    private fun fetchDataFromServer() {
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
                    }

                    mList = prefs?.getCategoryList()!!
                    updateVewPager2()
                    callApplicationListView(prefs?.getCategoryList()!![0].cat_id)
                } else {
                    prefs?.setCategoryList(null)
                    viewModel?.callApiData(CategoryListRequest(country!!, appId!!))
                        ?.observe(viewLifecycleOwner) { list ->
                            prefs?.setCategoryList(list.data)

                            //add a fav tab
                            tabs?.addTab(
                                tabs?.newTab()!!.setText("Favourite")
                            )

                            // Iterate over all tabs and set the custom view
                            for (i in 0 until list.data!!.size) {
                                val cat_pos = i + 1
                                tabs?.addTab(
                                    tabs?.newTab()!!.setText(list.data!![i].cat_name),
                                    cat_pos
                                )
                            }

                            mList = list.data
                            updateVewPager2()
                            callApplicationListView(list.data[0].cat_id)
                        }
                }


            }
        }
    }

}