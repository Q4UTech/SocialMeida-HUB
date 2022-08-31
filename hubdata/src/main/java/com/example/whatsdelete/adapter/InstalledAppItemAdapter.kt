package com.example.whatsdelete.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsdelete.listener.onclickInstalledApp
import com.example.whatsdelete.listener.openOnClick
import com.example.whatsdelete.modal.CategoryDetailItem
import com.example.whatsdelete.responce.ApplicationListData
import com.jatpack.wastatustranding.R
import com.squareup.picasso.Picasso

class InstalledAppItemAdapter(var context:Context, private val list: List<ApplicationListData>?,
                              var listener: onclickInstalledApp
): RecyclerView.Adapter<InstalledAppItemAdapter.ViewHolder>() {


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
       Picasso.get().load(itemData?.image)
           .into(holder.img)
        holder.ll_app_container.setOnClickListener {
            if (itemData != null) {
//                listener.openAppInWebView(itemData.package_name, "itemDatacolor")
                listener.onclickInstalledApp(itemData.package_name, "itemDatacolor")
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
        var ll_app_container=itemView.findViewById<LinearLayout>(R.id.ll_app_container)


    }
}