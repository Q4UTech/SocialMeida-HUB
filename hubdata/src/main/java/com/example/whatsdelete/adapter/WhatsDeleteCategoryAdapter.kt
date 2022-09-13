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
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.modal.Data
import com.example.whatsdelete.responce.CategoryListData
import com.pds.wastatustranding.R
import com.squareup.picasso.Picasso


class WhatsDeleteCategoryAdapter(var context:Context, private val list: List<CategoryListData>,
                                 var listener: setClick): RecyclerView.Adapter<WhatsDeleteCategoryAdapter.ViewHolder>() {
private var index:Int?=0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        val data=list[position]
        //holder.bind(holder,data)
        holder.categoryName.text= data.cat_name
        Picasso.get().load(data.cat_image).into(holder.categoryImg)
        holder.rv_select.setOnClickListener{
            index=position
            listener.onClick(data,data.cat_id)
//            notifyDataSetChanged()
        }

//        if (index == position)
//            holder.ll_cat_container.background= context.resources.getDrawable(R.drawable.active_bg,null)
//        else
//            holder.ll_cat_container.background= context.resources.getDrawable(R.drawable.category_rectangle,null)

    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: "+list.size)
        return list.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
      //  private var id=itemView.findViewById<TextView>(R.id.user_id)
      var categoryName=itemView.findViewById<TextView>(R.id.category_name)
        var categoryImg=itemView.findViewById<ImageView>(R.id.category_image)
        var ll_cat_container=itemView.findViewById<LinearLayout>(R.id.ll_cat_container)
        var rv_select=itemView.findViewById<RelativeLayout>(R.id.rv_select)




    }
}