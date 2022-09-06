package com.pds.socialmediahub.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pds.socialmediahub.R;


/**
 * Created by qunatum4u2 on 27/08/18.
 */

public class TutorialActivity extends AppCompatActivity {

    RelativeLayout rel_tran;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_layout);

        rel_tran=(RelativeLayout)findViewById(R.id.rel_tran);
        rel_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             finish();
            }
        }, 3000);

    }
}
