package com.jatpack.socialmediahub.ui.status

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.util.SetClick
import com.jatpack.socialmediahub.util.VideoRequestHandler
import com.makeramen.roundedimageview.RoundedImageView


import com.squareup.picasso.Picasso
import java.io.File

class WAStatusListAdapter(
    private val mContext: Context,
    var status: ArrayList<File>,
    private val isFromGalleryBoolean: Boolean,
    private var listener:SetClick
) : ListAdapter<File, WAStatusListAdapter.ListAdapterViewHolder>(
    DiffUtils()
) {

    var checkStatus = BooleanArray(status.size)
     var tempList = ArrayList<Int>()
     var isLongClickEnabled = false
     var visible = false
    val videoRequestHandler: VideoRequestHandler = VideoRequestHandler()
    val picassoInstance: Picasso = Picasso.Builder(mContext.getApplicationContext())
        .addRequestHandler(videoRequestHandler)
        .build()


    class ListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val img_media = view.findViewById<RoundedImageView>(R.id.img_media_Video)
        val video_thum = view.findViewById<ImageView>(R.id.defImg_media_video)
        val parent_click = view.findViewById<LinearLayout>(R.id.parent_click)
        val checkBox=view.findViewById<CheckBox>(R.id.ch_select)
        val fl_download=view.findViewById<FrameLayout>(R.id.fl_download)

       /* fun bind(
            file: File,
            picassoInstance: Picasso,
            context: Context,
            status: ArrayList<File>,
            isFromGalleryBoolean: Boolean,
        ) {

            println("WAStatusListAdapter.onBindViewHolder 009" + " " + file.path)





        }*/

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.status_row_view, parent, false)


        return ListAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapterViewHolder, position: Int) {

        val item = getItem(position)

        println("WAStatusListAdapter.onBindViewHolder " + " " + position)
       // holder.bind(item, picassoInstance, mContext, status, isFromGalleryBoolean)
        holder.checkBox?.isClickable=false
        holder.checkBox?.isChecked = checkStatus[position]



        if (isLongClickEnabled) {
            Log.d("TAG", "onBindViewHolder1: ")
            holder.checkBox?.visibility = View.VISIBLE
            holder.fl_download?.visibility=View.GONE
        } else {
            Log.d("TAG", "onBindViewHolder2: ")
            holder.checkBox?.visibility = View.GONE
            holder.fl_download?.visibility=View.VISIBLE
        }

        holder.checkBox?.setOnClickListener {
            checkStatus[position] = holder.checkBox!!.isChecked
            checkStatus[position]!=checkStatus[position]
        }


        holder.parent_click.setOnClickListener(View.OnClickListener {

            if (isLongClickEnabled) {
                Log.d("TAG", "onBindViewHolder3: ")
                holder.checkBox.performClick()

            } else {
                Log.d("TAG", "onBindViewHolder4: ")
//                if(file.path.endsWith(".mp4")){
                val gson = Gson()
//                  val  intent = Intent(context, VideoDetailActivity::class.java)
//                    intent.putExtra("video_allList", gson.toJson(status))
//                    intent.putExtra("selectedPos", position)
//                    intent.putExtra("isFromStatus", true)
//                    context.startActivity(intent)
//
//                }else{
                var intent = Intent(mContext, StatusPriviewActivity::class.java)
                intent.putExtra(
                    "absoluteImgPath", status[position].path
                )
                intent.putExtra("video_allList", gson.toJson(status))
                intent.putExtra(
                    "imgName", status[position].name
                )
                intent.putExtra(
                    "isFromStatus", isFromGalleryBoolean
                )
                AllMediaListingImage_singleton.getInstance()
                    .setAllStatusFiles(status)
                intent.putExtra("selectedPos", "" + position)
                mContext.startActivity(intent)
//                }
            }
        })

        holder.parent_click.setOnLongClickListener {
            isLongClickEnabled = true
            holder.checkBox?.performClick()
            checkStatus[position] = holder.checkBox!!.isChecked
            notifyDataSetChanged()
            listener.onLongClcik(it,position)
            true
        }

        if (status[position].path.endsWith(".mp4")) {
           holder.video_thum.visibility = View.VISIBLE

            picassoInstance.load(
                VideoRequestHandler.SCHEME_VIDEO.toString() + ":" + status[position].path
            ).placeholder(R.drawable.ic_placeholder_video).into(holder.img_media)


        } else {
            holder.video_thum.visibility = View.GONE
            Picasso.get().load(File(status[position].path)) //
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.ic_placeholder_image).into(holder.img_media)
        }
    }

    fun removeAllSelected() {
        isLongClickEnabled = false

        for (i in checkStatus.indices) {
            checkStatus[i] = false
        }
        notifyDataSetChanged()
    }

    fun getList(): List<File?> {
        return status
    }
    override fun getItemCount(): Int {
        return status.size
    }

    fun selectAll() {

    }

    //also called DiffUtilS Callback , Comparator
    class DiffUtils : DiffUtil.ItemCallback<File>() {

        override fun areItemsTheSame(
            oldItem: File,
            newItem: File
        ): Boolean {
            return oldItem.path == newItem.path

        }

        override fun areContentsTheSame(
            oldItem: File,
            newItem: File
        ): Boolean {
            return oldItem == newItem
        }

    }

}