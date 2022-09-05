package com.pds.socialmediahub.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.pds.socialmediahub.listener.IRuntimeCallback;


/**
 * Created by Anon on 13,August,2018
 */
public class BaseActivity extends AppCompatActivity {
    private IRuntimeCallback mListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void enterImmersiveMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /*
     * USED BY SPLASHACTIVITY for RUNTIME PERMISSION WORK
     * */
    public void setUpToolBar(int _ID, String pageTitle) {
        Toolbar toolbar = (Toolbar) findViewById(_ID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(pageTitle);
    }

    public boolean isStoragePermissionGranted() {

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(getApplicationContext(), "permission allow success.", Toast.LENGTH_SHORT).show();
                return true;

            }
        }*/

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public boolean isStoragePermissionGrantedonly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestStoragePermission(IRuntimeCallback l, int requestCode) {
        this.mListener = l;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    public void requestPermission() {
     /*   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //request for the permission
            Toast.makeText(getApplicationContext(), "Request for permission allow success.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }else {*/
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.CAMERA}, 199);
//        }
    }

    public void showPermissionDenialDialog(String msg) {
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Grant Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                        requestPermission();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                    }
                });
        dialog.show();
    }

    @SuppressLint("NewApi")
    public void statusBarColor(int colors){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(colors);
    }


    public boolean isCallPhonePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestCallPhonePermission(int permissionRequestCODE) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE},
                permissionRequestCODE);
    }



    public void requestCallPhonePermission(IRuntimeCallback l, int requestCode) {
        this.mListener = l;
        ActivityCompat
                .requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}, requestCode);
    }



}
