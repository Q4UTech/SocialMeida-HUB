package com.quantum.dashboard.ui.wastatus

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.ui.status.AllMediaListingImage_singleton
import com.jatpack.socialmediahub.ui.status.StatusPriviewActivity
import com.jatpack.socialmediahub.util.VideoRequestHandler
import com.makeramen.roundedimageview.RoundedImageView


import com.squareup.picasso.Picasso
import java.io.File

class WAStatusWith11ListAdapter(private val mContext : Context, private val status :ArrayList<DocumentFile> ) : ListAdapter<DocumentFile, WAStatusWith11ListAdapter.ListAdapterViewHolder>(DiffUtils()) {
     val videoRequestHandler: VideoRequestHandler = VideoRequestHandler()
     val picassoInstance: Picasso = Picasso.Builder(mContext.getApplicationContext())
        .addRequestHandler(videoRequestHandler)
        .build()




    class ListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val img_media=view.findViewById<RoundedImageView>(R.id.img_media_Video)
        val video_thum=view.findViewById<ImageView>(R.id.defImg_media_video)
        val parent_click=view.findViewById<LinearLayout>(R.id.parent_click)



        fun bind(file: DocumentFile,picassoInstance : Picasso,context:Context,status :ArrayList<DocumentFile>){

            println("WAStatusListAdapter.onBindViewHolder 009"+" "+file.uri.path)
            parent_click.setOnClickListener(View.OnClickListener {

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
                if(file?.uri != null){
                    var intent = Intent(context, StatusPriviewActivity::class.java)
                    intent.putExtra(
                        "absoluteImgPath", file.uri.toString()
                    )
                    intent.putExtra(
                        "imgName", file.name
                    )
                    intent.putExtra(
                        "isFromStatus", true
                    )
                    AllMediaListingImage_singleton.getInstance()
                        .setAllStatusDocumentFiles(status)
                    intent.putExtra("selectedPos", "" + position)
                    context.startActivity(intent)
                }

//                }

            })


            if(file.uri.path!!.endsWith(".mp4")){
                video_thum.visibility=View.VISIBLE

//                picassoInstance.load(
//                    VideoRequestHandler.SCHEME_VIDEO.toString() + ":" + file.uri
//                ).into(img_media)

                Glide
                    .with(context)
                    .load((file.uri))
                    .placeholder(R.drawable.ic_placeholder_video)
                    .into(img_media)


            }else{
                video_thum.visibility=View.GONE
                Picasso.get().load(file.uri)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.ic_placeholder_image).into(img_media)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_row_view,parent,false)


        return ListAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapterViewHolder, position: Int) {

        val item=getItem(position)

        println("WAStatusListAdapter.onBindViewHolder "+" "+position)
        holder.bind(item,picassoInstance,mContext,status)

    }

    //also called DiffUtilS Callback , Comparator
    class DiffUtils : DiffUtil.ItemCallback<DocumentFile>(){

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