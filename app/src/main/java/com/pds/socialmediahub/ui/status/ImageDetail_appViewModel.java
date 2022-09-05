package com.pds.socialmediahub.ui.status;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ImageDetail_appViewModel extends ViewModel {
    private boolean isPortraitOrientation=false;

    public MutableLiveData<ImageDetail_screenOrientation_model> modelMutableLiveData_sOrientation;
    private final MutableLiveData<Boolean> isFirstTime_ads = new MutableLiveData<>();

    public LiveData<Boolean> isFirstTimeLivedata_ads = isFirstTime_ads;

    {
        isFirstTime_ads.setValue(false);
    }

    public LiveData<ImageDetail_screenOrientation_model> getScreenOrientation()
    {
        if (modelMutableLiveData_sOrientation == null)
        {
            modelMutableLiveData_sOrientation=new MutableLiveData<>();
        }
        loadScreenOrientation(isPortraitOrientation);
        return modelMutableLiveData_sOrientation;
    }
    @SuppressLint("SourceLockedOrientationActivity")
    private void loadScreenOrientation(boolean isPortraitOrientation)
    {
        ImageDetail_screenOrientation_model model =
                new ImageDetail_screenOrientation_model();

        if (isPortraitOrientation)
        {
            model.setPortraitOrientation(true);
            modelMutableLiveData_sOrientation.setValue(model);
        }else
        {
            model.setPortraitOrientation(false);
            modelMutableLiveData_sOrientation.setValue(model);
        }
    }
    public void setScreenOrientation(boolean orientationStatus)
    {   isPortraitOrientation=orientationStatus;
        loadScreenOrientation(isPortraitOrientation);
    }

    public void setIssFirstTime_ads(boolean value){
        isFirstTime_ads.setValue(value);
    }
}
