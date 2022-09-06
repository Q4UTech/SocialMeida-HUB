package com.example.whatsdelete.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.whatsdelete.adapter.ViewPagerAdapter
import com.pds.wastatustranding.R


class ImagePreviewFragment : Fragment() {
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
     private var list:ArrayList<String>?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.idViewPager)
        val args = arguments
        list= args?.get("list") as ArrayList<String>?
        Log.d("TAG", "onViewCreated: "+list?.size)

        viewPagerAdapter = list?.let { ViewPagerAdapter(requireActivity(), it) }

        viewPager!!.adapter = viewPagerAdapter
    }


}