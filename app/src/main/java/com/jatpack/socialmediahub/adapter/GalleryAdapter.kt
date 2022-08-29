package com.jatpack.socialmediahub.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.ui.status.AllMediaListingImage_singleton
import com.jatpack.socialmediahub.ui.status.MyDownloadsFragment
import com.jatpack.socialmediahub.ui.status.StatusPriviewActivity
import com.jatpack.socialmediahub.ui.status.WAStatusListAdapter
import com.jatpack.socialmediahub.util.AppUtils
import com.jatpack.socialmediahub.util.SetClick
import com.squareup.picasso.Picasso
import java.io.File

class GalleryAdapter(
    private var context: Context,
    var galleryList: ArrayList<File>,
    var listener: SetClick
) :
    RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {
    var checkStatus = BooleanArray(galleryList.size)
    var tempList = ArrayList<File>()
    var isLongClickEnabled = false
    var visible = false
    private var listenerSelection: CounterSlection? = null

    interface CounterSlection {
        fun selectItems(itemSlectionCount: Int)
    }

    fun setCheckedListener(listenerSelection: CounterSlection) {
        this.listenerSelection = listenerSelection
    }

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val img = item.findViewById<ImageView>(R.id.img)
        val rl_video = item.findViewById<RelativeLayout>(R.id.rl_video)
        val duration = item.findViewById<TextView>(R.id.duration)
        val rl = item.findViewById<RelativeLayout>(R.id.rl)
        val checkBox = item.findViewById<ImageView>(R.id.ch_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.own_gallary_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context).load(Uri.fromFile(File(galleryList[position].path))).into(holder.img)
        if (galleryList[position].path.endsWith(".mp4")) {
            holder.rl_video.visibility = View.VISIBLE

            if (MediaPlayer.create(context, Uri.parse(galleryList[position].path)) != null) {
                Log.d("TAG", "onBindViewHolder: " + Uri.parse(galleryList[position].path))
                val mediaPlayer =
                    MediaPlayer.create(context, Uri.parse(galleryList[position].path));
                holder.duration.text = AppUtils.timeConversion1(mediaPlayer.duration.toLong())
            } else {
                Log.d("TAG", "onBindViewHolder1: " + Uri.parse(galleryList[position].path))
            }


            // holder.duration.text = AppUtils.timeConversion1(mediaPlayer.duration.toLong())
        } else {
            holder.rl_video.visibility = View.GONE
        }

        if (checkStatus[position]) {
            holder.checkBox.visibility = View.VISIBLE
        } else {
            holder.checkBox.visibility = View.GONE
        }
        holder.rl.setOnClickListener(View.OnClickListener {

            if (isLongClickEnabled) {

                if (checkStatus[position]) {
                    checkStatus[position] = false
                    tempList.remove(galleryList[position])
                    listenerSelection?.selectItems(tempList.size)
                } else {
                    checkStatus[position] = true
                    tempList.add(galleryList[position])
                    listenerSelection?.selectItems(tempList.size)
                }
                notifyDataSetChanged()

            } else {
                Log.d("TAG", "onBindViewHolder4: ")

            }
        })

        holder.rl.setOnLongClickListener {
            isLongClickEnabled = true
            if (!checkStatus[position]) {
                checkStatus[position] = true
                tempList.add(galleryList[position])
                listenerSelection?.selectItems(tempList.size)
            }
            notifyDataSetChanged()
            listener.onLongClcik(it, position)
            true
        }
    }

    fun selectAll() {
        for (i in checkStatus.indices) {
            checkStatus[i] = true
            listenerSelection?.selectItems(checkStatus.size)
        }
        notifyDataSetChanged()
    }

    fun removeAllSelected() {
        isLongClickEnabled = false
        tempList.clear()
        for (i in checkStatus.indices) {
            checkStatus[i] = false

        }
        notifyDataSetChanged()
    }

    fun getList(): List<File> {
        return galleryList
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: " + galleryList.size)
        return galleryList.size
    }

    fun updateList(newList: List<File>) {
        this.galleryList = newList as ArrayList<File>
        Log.d("TAG", "updateList: " + galleryList.size)
        notifyDataSetChanged()

    }
}