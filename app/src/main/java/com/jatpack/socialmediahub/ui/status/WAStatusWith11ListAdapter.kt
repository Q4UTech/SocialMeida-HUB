package com.jatpack.socialmediahub.ui.status

import android.annotation.SuppressLint
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
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.util.SetClick
import com.jatpack.socialmediahub.util.VideoRequestHandler
import com.makeramen.roundedimageview.RoundedImageView


import com.squareup.picasso.Picasso

class WAStatusWith11ListAdapter(
    private val mContext: Context,
    private val status: ArrayList<DocumentFile>,
    var listener: SetClick
) : ListAdapter<DocumentFile, WAStatusWith11ListAdapter.ListAdapterViewHolder>(
    DiffUtils()
) {
    var checkStatus = BooleanArray(status.size)
    var tempList = ArrayList<DocumentFile>()
    var isLongClickEnabled = false
    private var listenerSelection: CounterSlection? = null
    var visible = false
    val videoRequestHandler: VideoRequestHandler = VideoRequestHandler()
    val picassoInstance: Picasso = Picasso.Builder(mContext.getApplicationContext())
        .addRequestHandler(videoRequestHandler)
        .build()

    interface CounterSlection {
        fun selectItems(itemSlectionCount: Int)
    }

    fun setCheckedListener(listenerSelection: CounterSlection) {
        this.listenerSelection = listenerSelection
    }


    class ListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val img_media = view.findViewById<RoundedImageView>(R.id.img_media_Video)
        val video_thum = view.findViewById<ImageView>(R.id.defImg_media_video)
        val parent_click = view.findViewById<LinearLayout>(R.id.parent_click)
        val checkBox = view.findViewById<CheckBox>(R.id.ch_select)
        val fl_download = view.findViewById<FrameLayout>(R.id.fl_download)


        /*  fun bind(file: DocumentFile,picassoInstance : Picasso,context:Context,status :ArrayList<DocumentFile>){

              println("WAStatusListAdapter.onBindViewHolder 009"+" "+file.uri.path)

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
        // holder.bind(item,picassoInstance,mContext,status)
        holder.checkBox?.isClickable = false
        holder.checkBox?.isChecked = checkStatus[position]



        if (isLongClickEnabled) {
            Log.d("TAG", "onBindViewHolder1: ")
            holder.checkBox?.visibility = View.VISIBLE
            holder.fl_download?.visibility = View.GONE
        } else {
            Log.d("TAG", "onBindViewHolder2: ")
            holder.checkBox?.visibility = View.GONE
            holder.fl_download?.visibility = View.VISIBLE
        }

        holder.checkBox?.setOnClickListener {
        //    checkStatus[position] = holder.checkBox!!.isChecked
            if (checkStatus!![position]) {
                checkStatus!![position] = false
                Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                tempList.remove(status[position])
                listenerSelection?.selectItems(tempList.size)

            } else {
                checkStatus!![position] = true
                Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                tempList.add(status[position])
                listenerSelection?.selectItems(tempList.size)


            }
            notifyDataSetChanged()
        }
        holder.parent_click.setOnClickListener(View.OnClickListener {
            if (isLongClickEnabled) {
                Log.d("TAG", "onBindViewHolder3: ")
                //  holder.checkBox.performClick()
                if (checkStatus!![position]) {
                    checkStatus!![position] = false
                    Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                    tempList.remove(status[position])
                    listenerSelection?.selectItems(tempList.size)

                } else {
                    checkStatus!![position] = true
                    Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                    tempList.add(status[position])
                    listenerSelection?.selectItems(tempList.size)


                }
                notifyDataSetChanged()

            } else {
//                if(file.uri.path!!.endsWith(".mp4")){
                val gson = Gson()
//                    AllMediaListingImage_singleton.getInstance()
//                        .setAllStatusDocumentFiles(status)
//                  val  intent = Intent(context, VideoDetailActivity::class.java)
////                    intent.putExtra("video_allList", gson.toJson(status))
//                    intent.putExtra("selectedPos", position)
//                    intent.putExtra("isFromStatus", true)
//                    context.startActivity(intent)
//                }else{
                if (status[position]?.uri != null) {
                    var intent = Intent(mContext, StatusPriviewActivity::class.java)
                    intent.putExtra(
                        "absoluteImgPath", status[position].uri.toString()
                    )
                    intent.putExtra(
                        "imgName", status[position].name
                    )
                    intent.putExtra(
                        "isFromStatus", true
                    )
                    AllMediaListingImage_singleton.getInstance()
                        .setAllStatusDocumentFiles(status)
                    intent.putExtra("selectedPos", "" + position)
                    mContext.startActivity(intent)
                }

//                }
            }
        })


        if (status[position].uri.path!!.endsWith(".mp4")) {
            holder.video_thum.visibility = View.VISIBLE

//                picassoInstance.load(
//                    VideoRequestHandler.SCHEME_VIDEO.toString() + ":" + file.uri
//                ).into(img_media)

            Glide
                .with(mContext)
                .load((status[position].uri))
                .placeholder(R.drawable.ic_placeholder_video)
                .into(holder.img_media)


        } else {
            holder.video_thum.visibility = View.GONE
            Picasso.get().load(status[position].uri)
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.ic_placeholder_image).into(holder.img_media)
        }
        holder.parent_click.setOnLongClickListener {
            isLongClickEnabled = true
            holder.checkBox?.performClick()
          //  checkStatus[position] = holder.checkBox!!.isChecked
            if (!checkStatus[position]) {
                checkStatus[position] = true
                tempList.add(status[position])
                listenerSelection?.selectItems(tempList.size)

            }
            notifyDataSetChanged()
            listener.onLongClcik(it, position)
            true
        }

        holder.fl_download.setOnClickListener {
            listener.onClick(it, position)
        }
    }

    fun removeAllSelected() {
        isLongClickEnabled = false
        tempList.clear()
        for (i in checkStatus.indices) {
            checkStatus[i] = false
        }
        notifyDataSetChanged()
    }

    fun getList(): List<DocumentFile?> {
        return status
    }

    fun selectAll() {
        for (i in status.indices) {
            checkStatus[i] = true
        }
        notifyDataSetChanged()
    }

    //also called DiffUtilS Callback , Comparator
    class DiffUtils : DiffUtil.ItemCallback<DocumentFile>() {

        override fun areItemsTheSame(
            oldItem: DocumentFile,
            newItem: DocumentFile
        ): Boolean {
            return oldItem.uri.path == newItem.uri.path

        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: DocumentFile,
            newItem: DocumentFile
        ): Boolean {
            return oldItem == newItem
        }

    }
}