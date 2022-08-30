/*
package com.jatpack.socialmediahub.ui.directchat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jatpack.socialmediahub.R;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;


*
 * Created by qunatum4u2 on 27/05/19.



public class DirectChat extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText input_number, input_message;
    private Button btn_send;
    private ImageView back_click,cross;
    private LinearLayout adsbanner;
    // FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.directchatlayout);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        input_number = (EditText) findViewById(R.id.input_number);
        input_message = (EditText) findViewById(R.id.input_message);
        btn_send = (Button) findViewById(R.id.btn_send);
        back_click = (ImageView) findViewById(R.id.back_click);
        adsbanner = (LinearLayout) findViewById(R.id.adsbanner);
        cross = (ImageView) findViewById(R.id.cross);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //adsbanner.addView(AHandler.getInstance().getBannerHeader(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Direct Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Log.d("DirectChat", "Hello onCreate number" + " " + ccp.getSelectedCountryCode());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Log.d("DirectChat", "Hello onCreate number selected" + " " + ccp.getSelectedCountryCode());
                Toast.makeText(DirectChat.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_message.setText("");
            }
        });
        back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle = new Bundle();
                //bundle.putInt("directChat", v.getId());
                //mFirebaseAnalytics.logEvent("DirectChatSend", bundle);
                FirebaseUtils.onClickButtonFirebaseAnalytics(DirectChat.this, AN_FIREBASE_DIRECT_CHAT_SEND);
                if (input_number != null && input_number.length() > 9) {
                    sendMessage(ccp.getSelectedCountryCode() + input_number.getText().toString(), input_message.getText().toString());
                }
            }
        });

    }

    private void sendMessage(String mobilenumber, String text) {
        Intent browserIntent;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobilenumber + "&text=" + text));
            startActivity(browserIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }

    }


    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
*/
