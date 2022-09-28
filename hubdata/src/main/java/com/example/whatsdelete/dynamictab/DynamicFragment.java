package com.example.whatsdelete.dynamictab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pds.wastatustranding.R;

public class DynamicFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;


    public DynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionNumber = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;

        Log.d("DynamicFragment", "Hello onCreate hi section number"+" "+sectionNumber);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);





        return  view;
    }

    public static DynamicFragment newInstance(int sectionNumber) {
        Log.d("DynamicFragment", "Hello onCreate hi section number aaaa"+" "+sectionNumber);

        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
