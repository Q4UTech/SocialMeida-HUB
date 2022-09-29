package com.example.whatsdelete.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsdelete.listener.FavSelectionListListner
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.modal.CategoryDetailItem
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.utils.Prefs
import com.pds.wastatustranding.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class FavCategoryItemAdapter(var context:Context, private val list: List<ApplicationListData>?,
                             var listener: FavSelectionListListner
): RecyclerView.Adapter<FavCategoryItemAdapter.ViewHolder>() {
    var tempList = ArrayList<String>()

    var checkStatus = list?.let {
        BooleanArray(it.size)}

    var checkStatustemp = list?.let {
        BooleanArray(it.size)
        if (Prefs(context).getFavList()!=null && Prefs(context).getFavList()!!.size>0) {
//            GlobalScope.launch {
                for (i in list!!.indices) {

                    for (pos in Prefs(context).getFavList()!!.indices){
                        if (Prefs(context).getFavList()?.get(pos)?.package_name.equals(list[i].package_name)) {
                            checkStatus!![i]=true
                            list[i].isChecked=true
                            list?.get(i)?.let { it1 -> tempList.add(it1.package_name) }
                            println("FavCategoryItemAdapter. sdghskdjfh"+" "+list[i].package_name+" "+
                                    list[i].isChecked
                            )

                        }
//                        else{
//                            checkStatus!![i]=false
//                            list[i].isChecked=false
//
//                        }
                    }
                }
//            }
            notifyDataSetChanged()
        }

    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fav_data_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        println("FavCategoryItemAdapter. sdghskdjfh 002"+" "+ list!![position].isChecked)

        val itemData= list?.get(position)
     /*   Glide.with(context)
            .load(itemData?.img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.img)*/
       Picasso.get().load(itemData?.image)
           .into(holder.img)
//        holder.ll_app_container.setOnClickListener {
//            if (itemData != null) {
////                listener.openAppInWebView(itemData.package_name, "itemDatacolor")
//                println("WhatsDelteCategoryItemAdapter.onBindViewHolder sgjkhaskgddjsa"+" "+itemData.color)
//                listener.openAppInWebView(itemData.click_url, itemData.color)
//            }
//        }





        if (list!![position].isChecked){
            println("FavCategoryItemAdapter.onBindViewHolder hi check test >>> aaa"+" "+position
            +" "+checkStatus!!.size+" "+checkStatus.toString())
            holder.serviceToggle.isChecked=true
        }else{
            println("FavCategoryItemAdapter.onBindViewHolder hi check test >>> bbb"+" "+position)

            holder.serviceToggle.isChecked=false

        }

        holder.serviceToggle.setOnClickListener {

            if (list!![position].isChecked){
                checkStatus!![position]=false
                list?.get(position)?.isChecked=false
                list?.get(position)?.let { it1 -> tempList.remove(it1.package_name) }
            }else{
                checkStatus!![position]=true
                list?.get(position)?.isChecked=true

                list?.get(position)?.let { it1 -> tempList.add(it1.package_name) }
            }
            listener.favSelectedPackageList(tempList)

            notifyDataSetChanged()
        }



        holder.title.text=itemData?.app_name


        /*if (itemData != null) {
            holder.bind(itemData)
        }*/
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: "+list?.size)
        return list?.size!!
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var title=itemView.findViewById<TextView>(R.id.app_name)
        var img=itemView.findViewById<ImageView>(R.id.category_item_img)
        var serviceToggle=itemView.findViewById<ToggleButton>(R.id.serviceToggle)
        var ll_app_container=itemView.findViewById<CardView>(R.id.ll_app_container)


    }
}