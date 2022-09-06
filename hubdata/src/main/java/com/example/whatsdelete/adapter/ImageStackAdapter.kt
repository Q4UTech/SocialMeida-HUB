package com.example.whatsdelete.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsdelete.listener.setClick
import com.example.whatsdelete.modal.Data
import com.example.whatsdelete.utils.CircleImageView
import com.pds.wastatustranding.R
import com.squareup.picasso.Picasso

class ImageStackAdapter(private val context : Context, private val list: ArrayList<String>): RecyclerView.Adapter<ImageStackAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stack_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: "+list[position])


        Glide.with(context).load(list[position]).into(holder.statckImg)


    }

    override fun getItemCount(): Int {
        return if(list.size<=6){
            list.size
        }else{
            6
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //  private var id=itemView.findViewById<TextView>(R.id.user_id)
       // var categoryName=itemView.findViewById<TextView>(R.id.category_name)
        var statckImg=itemView.findViewById<CircleImageView>(R.id.statck_img)





    }
}