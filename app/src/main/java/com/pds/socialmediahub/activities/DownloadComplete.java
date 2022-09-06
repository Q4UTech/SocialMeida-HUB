package com.pds.socialmediahub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pds.socialmediahub.R;


public class DownloadComplete extends BottomSheetDialogFragment {

    String type = null;
   public DownloadComplete(String type){
        this.type = type;

    }


    private Intent intent = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.download_bottom_sheet, container, false);
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String type = getActivity().getIntent().getExtras().getString(BOTTOM_SHEET_TYPE);
        Log.e("TAG", "onViewCreated: "+type );

       /* if (type.equals(VIDEO)) {

        } else {

        }*/

        view.findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());

        view.findViewById(R.id.tv_view).setOnClickListener(v -> {

            try {

                intent = new Intent(getContext(),
                        Class.forName("nocropdp.GalleryBothActivity"));
                intent.putExtra("download", type );
                startActivity(intent);

                dismiss();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

    }

}
