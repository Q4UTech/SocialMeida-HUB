package com.example.whatsdelete.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.modal.CategoryDetailItem
import com.jatpack.wastatustranding.R
import com.squareup.picasso.Picasso

class WhatsDelteCategoryItemAdapter(var context:Context,private val list: List<CategoryDetailItem>?,
                                    var listener:openOnClick,var isServerHit:Boolean): RecyclerView.Adapter<WhatsDelteCategoryItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.data_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        val itemData= list?.get(position)
     /*   Glide.with(context)
            .load(itemData?.img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.img)*/
       Picasso.get().load(itemData?.img)
           .into(holder.img)
        holder.img.setOnClickListener {
            if (itemData != null) {
                listener.openItem(it, itemData)
            }
        }
        holder.title.setOnClickListener {
            if (itemData != null) {
                listener.openItem(it, itemData)
            }
        }
        holder.rl_bottom.setOnClickListener {
            if (itemData != null) {
                listener.openItem(it, itemData)
            }
        }
        holder.download_count.text=itemData?.count

        /*if (itemData != null) {
            holder.bind(itemData)
        }*/
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: "+list?.size)
        return list?.size!!
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
       // private var id=itemView.findViewById<TextView>(R.id.user_id)
         var title=itemView.findViewById<TextView>(R.id.tv_title)
        var img=itemView.findViewById<ImageView>(R.id.category_item_img)
        var download_count=itemView.findViewById<TextView>(R.id.download_count)
        var rl_bottom=itemView.findViewById<RelativeLayout>(R.id.rl_bottom)


    }
}