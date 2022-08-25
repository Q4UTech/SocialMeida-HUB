package com.jatpack.socialmediahub.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.util.CircleImageView;

import java.io.File;
import java.util.List;



/**
 * Created by Anon on 13,August,2018
 */
public class VideoStoryAdapter extends RecyclerView.Adapter<VideoStoryAdapter.ViewHolder> {

    private List<File> mList;
    private Context mContext;
    private Animation animation;

    public VideoStoryAdapter(Context c, List<File> l) {
        this.mList = l;
        this.mContext = c;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_splash);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dashboard_adapter_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mList.get(pos).getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bd = new BitmapDrawable(thumb);
        System.out.println("i m in showcase");
        viewHolder.civ.setBackgroundDrawable(bd);

        viewHolder.civ.setOnClickListener(this.getCIVClickListener(mList.get(pos), pos));

    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civ = (CircleImageView) itemView.findViewById(R.id.civ_adapter);
        }
    }

    private View.OnClickListener getCIVClickListener(final File mSource, final int pos) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("my list postion on clic" + " " + pos);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSource.getAbsolutePath()));
                intent.setDataAndType(Uri.parse(mSource.getAbsolutePath()), "video/mp4");
                mContext.startActivity(intent);

            }
        };
    }
}
