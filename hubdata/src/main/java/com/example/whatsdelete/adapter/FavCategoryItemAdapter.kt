package com.example.whatsdelete.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.modal.CategoryDetailItem
import com.example.whatsdelete.responce.ApplicationListData
import com.pds.wastatustranding.R
import com.squareup.picasso.Picasso

class FavCategoryItemAdapter(var context:Context, private val list: List<ApplicationListData>?,
                             var listener:openOnClick): RecyclerView.Adapter<FavCategoryItemAdapter.ViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fav_data_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
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



        if (list?.get(position)?.isChecked == true){
            holder.serviceToggle.isChecked=true
        }else{
            holder.serviceToggle.isChecked=false

        }

        holder.serviceToggle.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                list?.get(position)?.isChecked=true
            }else{
                list?.get(position)?.isChecked=false

            }
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