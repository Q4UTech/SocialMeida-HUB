package com.jatpack.socialmediahub.adapter

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
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.model.PersonNumber
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
        val delete = item.findViewById<ImageView>(R.id.cross)
        val rl = item.findViewById<RelativeLayout>(R.id.rl)
        val ll_number = item.findViewById<LinearLayout>(R.id.ll_number)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_number_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.contact.text = galleryList[position].contactNumber
        holder.duration.text = galleryList[position].timDate
        holder.delete.setOnClickListener {
            listener.onClick(it, position)
        }
        holder.rl.setOnClickListener {
            listener.onClick(it, position)
        }
        holder.ll_number.setOnClickListener {
            listener.onClick(it, position)
        }

    }


    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: " + galleryList.size)
        return galleryList.size
    }

    fun updateList(newList: ArrayList<PersonNumber>) {
        this.galleryList = newList
        Log.d("TAG", "updateList: " + galleryList.size)
        notifyDataSetChanged()

    }
}