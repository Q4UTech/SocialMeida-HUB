package com.jatpack.socialmediahub.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.activities.StoryShowCaseActivity;
import com.jatpack.socialmediahub.listener.AdapterCallBack;
import com.jatpack.socialmediahub.model.MediaModel;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by Anon on 13,August,2018
 */
public class DashboardStoriesAdapter extends RecyclerView.Adapter<DashboardStoriesAdapter.ViewHolder> {

    public boolean[] mCheckStates = null;
    public int position;
    private List<MediaModel> mList;
    private Context mContext;
    private Animation animation;
    private AdapterCallBack adapterCallBack;
    public boolean isLonClick = false;
    public int count = 0;
    private boolean isLonClickSelected = false;
    private boolean isSelectorEnabled = false;


    public DashboardStoriesAdapter(Context c, List<MediaModel> l, AdapterCallBack adapterCallBack) {
        this.mList = l;
        this.mContext = c;
        this.adapterCallBack = adapterCallBack;
//        animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_splash);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dashboard_adapter_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int pos) {
        System.out.println("Onclick.onBindViewHolder002: " + pos);

        int position = pos;
        viewHolder.itemPosition(position);

        if (mList.get(pos).getSelected()){
            viewHolder.iv_download.setVisibility(View.VISIBLE);
        }else {
            viewHolder.iv_download.setVisibility(View.GONE);

        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//
//            viewHolder.iv_id_icon.setImageResource(R.drawable.ic_video);
//            Glide
//                    .with(viewHolder.itemView.getContext())
//                    .load(mList.get(pos).getdFileUri())
//                    .into(viewHolder.civ);
//        }

        if (mList.get(pos).getFileName().endsWith(".mp4")) {
            viewHolder.iv_id_icon.setVisibility(View.VISIBLE);
            viewHolder.iv_id_icon.setImageResource(R.drawable.ic_video);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Glide
                        .with(viewHolder.itemView.getContext())
                        .load(mList.get(pos).getdFileUri())
                        .into(viewHolder.civ);
            } else {
                Glide
                        .with(viewHolder.itemView.getContext())
                        .load(mList.get(pos).getAbsoluteFile())
                        .into(viewHolder.civ);
            }


//            System.out.println("Onclick.onBindViewHolder003: " + mList.get(pos));
//            viewHolder.iv_id_icon.setImageResource(R.drawable.ic_video);
//            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mList.get(pos).getFilePath(), MediaStore.Images.Thumbnails.MINI_KIND);
//            RoundedBitmapDrawable circularBitmapDrawable =
//                    RoundedBitmapDrawableFactory.create(mContext.getResources(), thumb);
//            circularBitmapDrawable.setCircular(false);
//            System.out.println("i m in showcase");
//            viewHolder.civ.setBackgroundDrawable(circularBitmapDrawable);

        }else {
            viewHolder.iv_id_icon.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Picasso.get().load(mList.get(pos).getdFileUri()).into(viewHolder.civ);
            } else {
                Picasso.get().load(mList.get(pos).getAbsoluteFile()).into(viewHolder.civ);
            }
        }


//        viewHolder.civ.setAnimation(animation);

        System.out.println("Onclick.onBindViewHolder001: " + mList.get(pos));

        viewHolder.civ.setOnClickListener(this.getCIVClickListener(mList.get(pos), pos, viewHolder));

        viewHolder.civ.setOnLongClickListener(v -> {
            if (isLonClick){
               return false;
            }

            isLonClick=true;
            updateCheckBox(pos);
            return true;
        });

    }

    public void selectUnSelctAll(boolean isSelected){
        if (isSelected==true){
            count=mList.size();
        }else {
            count=0;
        }
            for (MediaModel model:
                    mList ) {
                model.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

    public List<MediaModel> getmList(){
        List<MediaModel> selecteedList=new ArrayList<>();
        for (MediaModel mediaModel :
                mList) {
            if (mediaModel.getSelected()){
                selecteedList.add(mediaModel);
            }
        }
        return selecteedList;
    }

    public List<String> getSeletedPath(){
        List<String> selecteedList=new ArrayList<>();
        for (MediaModel mediaModel :
                mList) {
            if (mediaModel.getSelected()){
                selecteedList.add(mediaModel.getdFileUri().getPath());
            }
        }
        return selecteedList;
    }

    private void updateCheckBox(int pos) {
        MediaModel model = mList.get(pos);
        model.setSelected(!model.getSelected());
        if(isLonClick){
            count++;
            adapterCallBack.getLongClickSelected(true, pos);
            adapterCallBack.getSelected(count);

        }else {

            System.out.println("hello.updateCheckBox: "+isLonClick);
        }
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        System.out.println("Test.getItemCount: size" + mList.size());
        mCheckStates = new boolean[this.mList.size()];
        return this.mList.size();
    }

    private View.OnClickListener getCIVClickListener(final MediaModel mSource, final int pos, ViewHolder itemView) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLonClick){
//                    updateCheckBox(pos);
                    System.out.println("my list postion on clic" + " counthello1: " + count);
                    if(mList.get(pos).getSelected()){
                        count--;
                        itemView.iv_download.setVisibility(View.GONE);
                        mList.get(pos).setSelected(false);
                        adapterCallBack.getSelected(count);

                    }else {

                        count++;
                        itemView.iv_download.setVisibility(View.VISIBLE);
                        mList.get(pos).setSelected(true);
                        adapterCallBack.getSelected(count);
                    }
                    notifyItemChanged(pos);
                }else {
                    System.out.println("my list postion on clic" + " pos: " + pos);
//                System.out.println("my list postion on clic" + " " + mSource.getFilePath().glastModified());
                    //AHandler.getInstance().showFullAds((Activity) mContext, false);

                    System.out.println("my list postion on clic 00 "+ isLonClick);
//                    if (isLonClick) {
//                        System.out.println("my list postion on clic 00111" + " pos: " + pos);
//
//                        if(itemView.iv_download.getVisibility() == View.VISIBLE){
//                            mCheckStates[pos] = false;
//                            mList.get(pos);
//                            itemView.iv_download.setVisibility(View.GONE);
//                        }
//
//                        mCheckStates[pos] = true;
//                        mList.get(pos);
//                        itemView.iv_download.setVisibility(View.VISIBLE);
//
//                    }
//                    else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                            if (mSource.getFileName().endsWith(".jpg")) {

                                Intent intent = new Intent(mContext, StoryShowCaseActivity.class);
                                intent.putExtra("position", pos);
                                intent.putExtra("imageuri_new", (mSource.getdFileUri()).toString());
                                intent.putExtra("doc_imageuri", mList.get(pos).getdFileUri().toString());

                                mContext.startActivity(intent);
                            } else if (mSource.getFileName().endsWith(".mp4")) {

//                    Intent intentvideo= new Intent(mContext, VideoActivity.class);
//                    intentvideo.putExtra("video", Uri.parse(mSource.getAbsolutePath()).toString());
//                    mContext.startActivity(intentvideo);
                               // Intent intent = new Intent(mContext, VideoActivity.class);
                               /* intent.putExtra("video", (mSource.getdFileUri()).toString());
                                intent.putExtra("timedate", String.valueOf(mSource.getDateTime()));
                                mContext.startActivity(intent);*/

//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSource.getAbsolutePath()));
//                    intent.setDataAndType(Uri.parse(mSource.getAbsolutePath()), "video/mp4");
//                    mContext.startActivity(intent);
                            }

                        } else {

                            if (mSource.getFileName().endsWith(".jpg")) {

                                Intent intent = new Intent(mContext, StoryShowCaseActivity.class);
                                intent.putExtra("position", pos);
                                intent.putExtra("imageuri_new", Uri.parse(mSource.getFilePath()).toString());
                                mContext.startActivity(intent);
                            }
                            else if (mSource.getFileName().endsWith(".mp4")) {

//                    Intent intentvideo= new Intent(mContext, VideoActivity.class);
//                    intentvideo.putExtra("video", Uri.parse(mSource.getAbsolutePath()).toString());
//                    mContext.startActivity(intentvideo);
                             /*   Intent intent = new Intent(mContext, VideoActivity.class);
                                intent.putExtra("video", Uri.parse(mSource.getFilePath()).toString());
                                intent.putExtra("timedate", String.valueOf(mSource.getDateTime()));
                                mContext.startActivity(intent);*/

//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSource.getAbsolutePath()));
//                    intent.setDataAndType(Uri.parse(mSource.getAbsolutePath()), "video/mp4");
//                    mContext.startActivity(intent);
                            }

                        }
//                mContext.startActivity(new Intent(mContext,DemoAct.class));
//             mContext.startActivity(new Intent(mContext, StoryShowCaseActivity.class));
//                    }
                }
           }
        };

    }

    public void enableSelector(boolean status) {
        isSelectorEnabled = status;
        System.out.println("isSelectedEnable : " + " " + isSelectorEnabled);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView civ, iv_download, iv_id_icon;
        private View view;
        private boolean isLonClick = false;
        private int item_position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civ = (ImageView) itemView.findViewById(R.id.civ_adapter);
            iv_download = (ImageView) itemView.findViewById(R.id.iv_download);
            iv_id_icon = (ImageView) itemView.findViewById(R.id.iv_id_icon);

        }

        public void itemPosition(int itemPos){
            item_position = itemPos;
        }

    }

}
