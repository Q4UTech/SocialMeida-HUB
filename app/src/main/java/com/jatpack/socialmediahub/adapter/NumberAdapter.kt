package com.jatpack.socialmediahub.adapter

import android.content.Context
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
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.model.PersonNumber
import com.jatpack.socialmediahub.util.AppUtils
import com.jatpack.socialmediahub.util.SetClick
import java.io.File

class NumberAdapter(
    private var context: Context,
    var galleryList: ArrayList<PersonNumber>,
    var listener: SetClick
) :
    RecyclerView.Adapter<NumberAdapter.MyViewHolder>() {
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
        val contact = item.findViewById<TextView>(R.id.contact)
       val duration = item.findViewById<TextView>(R.id.dataTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_number_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.contact.text=galleryList[position].contactNumber
        holder.duration.text=galleryList[position].timDate

    }





    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: " + galleryList.size)
        return galleryList.size
    }

   /* fun updateList(newList: List<File>) {
        this.galleryList = newList as ArrayList<File>
        Log.d("TAG", "updateList: " + galleryList.size)
        notifyDataSetChanged()

    }*/
}