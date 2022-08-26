package com.jatpack.socialmediahub.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jatpack.socialmediahub.R
import com.squareup.picasso.Picasso
import java.io.File

class GalleryAdapter(private var context: Context, var tempList: ArrayList<File>) :
    RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {
    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val img = item.findViewById<ImageView>(R.id.img)
        val rl_video = item.findViewById<RelativeLayout>(R.id.rl_video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.own_gallary_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Picasso.get().load(Uri.fromFile(File(tempList[position].path))).into(holder.img)
        Glide.with(context).load(Uri.fromFile(File(tempList[position].path))).into(holder.img)
        if (tempList[position].path.endsWith(".mp4")) {
            holder.rl_video.visibility - View.VISIBLE
        } else {
            holder.rl_video.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: " + tempList.size)
        return tempList.size
    }
}