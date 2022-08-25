package com.jatpack.socialmediahub.ui.status;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.PagerAdapter;


import com.allenxuan.xuanyihuang.xuanimageview.XuanImageView;
import com.jatpack.socialmediahub.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class StatusDetail_slideAdapter extends PagerAdapter {
    Context context;
    List<AllMediaListing_mainModel> allMediaListing_mainModels=null;
    List<File> sttausList=null;
    List<DocumentFile> documentFileList=null;
    private boolean isToolbarHidden=true;
    private getItemClick itemClickListner;

    public StatusDetail_slideAdapter(Context context,
                                     List<AllMediaListing_mainModel> allMediaListing_mainModels,
                                     List<File> statusList, List<DocumentFile> documentFileList,
                                     getItemClick itemClickListner ) {
        this.context = context;
        this.allMediaListing_mainModels = allMediaListing_mainModels;
        this.sttausList = statusList;
        this.documentFileList = documentFileList;
        this.itemClickListner=itemClickListner;
    }

    @Override
    public int getCount() {
        if(sttausList!=null){
            return sttausList.size();

        }else if(documentFileList!=null){
            return documentFileList.size();

        }
        else {
            return allMediaListing_mainModels.size();

        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        View view = LayoutInflater.from(context).inflate(R.layout.row_imgslide_imgdetail,
                container,false);
        MyHolder holder = new MyHolder();
        init(view,holder);

        if(sttausList!=null){
            String imgPath = sttausList.get(position).getPath();
            if (imgPath!=null) {
                holder.img_detail.setBackgroundResource(0);
                holder.img_detail.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
            }
        } else if(documentFileList!=null){
            String imgPath = documentFileList.get(position).getUri().toString();
            if (imgPath!=null) {
                holder.img_detail.setBackgroundResource(0);
                Picasso.get().load(imgPath).into(holder.img_detail);
//                holder.img_detail.setImageBitmap(BitmapFactory
//                        .decodeFile(imgPath));
            }
        }else {
            String imgPath = allMediaListing_mainModels.get(position).getAbsolutePathOfImage();
            if (imgPath!=null) {
                holder.img_detail.setBackgroundResource(0);
                holder.img_detail.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
            }
        }


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListner.onItemClickListner(position);


            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    public void init(View view, MyHolder holder)
    {
        holder.img_detail = view.findViewById(R.id.img_detail);
        holder.mainLayout = view.findViewById(R.id.mainLayout);
    }
    public class MyHolder{
        XuanImageView img_detail;
        LinearLayout mainLayout;
    }


    public interface getItemClick{
        void onItemClickListner(int pos);
    }
}
