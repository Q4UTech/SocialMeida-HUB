package com.jatpack.socialmediahub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.activities.StoryShowCaseActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Anon on 13,August,2018
 */
public class ImageStoryAdapter extends RecyclerView.Adapter<ImageStoryAdapter.ViewHolder> {

    private List<File> mList;
    private Context mContext;
    private Animation animation;

    public ImageStoryAdapter(Context c, List<File> l) {
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

        Picasso.get().load(mList.get(pos).getAbsoluteFile()).into(viewHolder.civ);

        viewHolder.civ.setOnClickListener(this.getCIVClickListener(mList.get(pos), pos));

    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private CircleImageView civ;
        private ImageView iv_id_icon,civ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civ = (ImageView) itemView.findViewById(R.id.civ_adapter);
            iv_id_icon = (ImageView) itemView.findViewById(R.id.iv_id_icon);

        }
    }

    private View.OnClickListener getCIVClickListener(final File mSource, final int pos) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("here is the total count of imagelist"+" "+mList.size()+" "+pos);
                Intent intent = new Intent(mContext, StoryShowCaseActivity.class);
                intent.putExtra("position", pos);
                mContext.startActivity(intent);

            }
        };
    }
}
