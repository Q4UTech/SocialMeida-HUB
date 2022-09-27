package com.pds.socialmediahub.ui.status


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.pds.socialmediahub.R
import com.pds.socialmediahub.util.SetClick
import com.pds.socialmediahub.util.Utilities
import com.pds.socialmediahub.util.VideoRequestHandler
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import engine.app.adshandler.AHandler
import java.io.File

class WAStatusListAdapter(
    private val mContext: Context,
    var status: ArrayList<File>,
    private val isFromGalleryBoolean: Boolean,
    private var listener: SetClick
) : ListAdapter<File, WAStatusListAdapter.ListAdapterViewHolder>(
    DiffUtils()
) {

    var checkStatus = BooleanArray(status.size)
    var tempList = ArrayList<File>()
    var isLongClickEnabled = false
    var visible = false
    private var listenerSelection: CounterSlection? = null
    private val videoRequestHandler: VideoRequestHandler = VideoRequestHandler()
    private val picassoInstance: Picasso = Picasso.Builder(mContext.applicationContext)
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
        val checkBox = view.findViewById<ImageView>(R.id.ch_select)
        val fl_download = view.findViewById<FrameLayout>(R.id.fl_download)
        val rl_play = view.findViewById<RelativeLayout>(R.id.rl_play)
        val duration = view.findViewById<TextView>(R.id.duration)

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
        /* holder.checkBox?.isClickable = false
         holder.checkBox?.isChecked = checkStatus[position]





         holder.checkBox?.setOnClickListener {
             if (checkStatus[position]) {
                 checkStatus[position] = false
                 Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                 tempList.remove(status[position])
                 listenerSelection?.selectItems(tempList.size)

             } else {
                 checkStatus[position] = true
                 Log.d("TAG", "onBindViewHolder5: " + position + "," + checkStatus!![position])
                 tempList.add(status[position])
                 listenerSelection?.selectItems(tempList.size)


             }
         }
 */
        if (isLongClickEnabled) {

            holder.fl_download?.visibility = View.GONE
        } else {

            holder.fl_download?.visibility = View.VISIBLE
        }
        if (checkStatus[position]) {
            holder.checkBox.visibility = View.VISIBLE
        } else {
            holder.checkBox.visibility = View.GONE
        }
        holder.parent_click.setOnClickListener(View.OnClickListener {

            if (isLongClickEnabled) {
                Log.d("TAG", "onBindViewHolder3: ")
                if (checkStatus[position]) {
                    checkStatus[position] = false
                    tempList.remove(status[position])
                    listenerSelection?.selectItems(tempList.size)
                } else {
                    checkStatus[position] = true
                    tempList.add(status[position])
                    listenerSelection?.selectItems(tempList.size)
                }

                notifyItemChanged(position, status[position])

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

                AHandler.getInstance().showFullAds(mContext as Activity?,false)
            }
        })
        holder.fl_download.setOnClickListener {
            listener.onClick(it, position)
        }
        holder.parent_click.setOnLongClickListener {
            isLongClickEnabled = true
            if (!checkStatus[position]) {
                checkStatus[position] = true
                tempList.add(status[position])
                Log.d("TAG", "onBindViewHolder: " + tempList.size)
                listenerSelection?.selectItems(tempList.size)

            }
            notifyItemChanged(position, status[position])
            listener.onLongClcik(it, position)
            true
        }

        if (status[position].path.endsWith(".mp4")) {
            holder.rl_play.visibility = View.VISIBLE

            var duration1 = Utilities.getDuration(mContext,Uri.parse(status[position].path))
            holder.duration.text = duration1

            /* picassoInstance.load(
                 VideoRequestHandler.SCHEME_VIDEO.toString() + ":" + status[position].path
             ).placeholder(R.drawable.ic_placeholder_video).into(holder.img_media)*/

            Glide.with(mContext)
                .load(/*VideoRequestHandler.SCHEME_VIDEO.toString() + ":" +*/ status[position].path)
                .placeholder(R.drawable.ic_placeholder_video)
                .into(holder.img_media)

        } else {
            holder.rl_play.visibility = View.GONE
            Glide.with(mContext).load(File(status[position].path)) //
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.ic_placeholder_image).into(holder.img_media)
            /*   Picasso.get().load(File(status[position].path)) //
                   //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                   .placeholder(R.drawable.ic_placeholder_image).into(holder.img_media)*/
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

    fun unSelectAll() {
        isLongClickEnabled = true
        tempList.clear()
        for (i in checkStatus.indices) {
            checkStatus[i] = false
            listenerSelection?.selectItems(tempList.size)
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
        tempList.clear()
        for (i in checkStatus.indices) {
            checkStatus[i] = true
            tempList.add(status[i])
            listenerSelection?.selectItems(tempList.size)
        }
        notifyDataSetChanged()
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

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

}