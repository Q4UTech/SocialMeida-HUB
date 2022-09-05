package com.pds.socialmediahub.ui.status;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.pds.socialmediahub.util.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StatusPresenter implements StatusFragmentContracts.StatusPresenter {

    private final StatusFragmentContracts.StatusView mView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public StatusPresenter(StatusFragmentContracts.StatusView mView) {
        this.mView = mView;
    }


    @SuppressLint("CheckResult")
    @Override
    public void start(Context context) {
        Observable.fromCallable(() -> {
                    ArrayList<File> inFiles = new ArrayList<File>();
                    File parentDir;
//                    if (isFromAppGallery) {
//                        parentDir = new File(AppUtils.createAppDir(context));
//                    } else {
                        parentDir = new File(AppUtils.getWhatsAppStatusPath());
//                    }


                    Log.d("StatusPresenter", "Hello start 001" + " isExits: " + parentDir.exists() + " isCanwrite" +
                            parentDir.canWrite() + " canread" + parentDir.canRead() + " " +
                            parentDir.getAbsolutePath());
                    File[] files = parentDir.listFiles();
                    Log.d("StatusPresenter", "Hello start 002" + " " + files.length + " " + parentDir.list().length);
                    if (files != null) {
                        for (File file : files) {
                            Log.d("StatusPresenter", "Hello start 003" + " " + files.length);
//                    if (AppUtils.is24Hour(file)) {

                            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG") ||
                                    file.getName().endsWith(".jpeg") || file.getName().endsWith(".mp4")) {
                                if (!inFiles.contains(file)) {
                                    inFiles.add(file);
                                }
                            }
                        }

                    }
                    Collections.sort(inFiles, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

                    return inFiles;
                }).map(files -> {
                    Collections.reverse(files);
                    return files;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<File>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArrayList<File> files) {

                            mView.loadStatus(files);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("StatusPresenter", "Hello onError fsdjfahs001");
                        mView.statusEroor();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("StatusPresenter", "Hello onComplete fsdjfahs002");
                    }
                });
    }

    @Override
    public void clear() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    /**
     * Getting All Images Path.
     *
     * @param activity the activity
     * @return ArrayList with images Path
     */
    @SuppressLint("Recycle")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

//        cursor = activity.getContentResolver().query(uri, projection, null,
//                null, null);
        cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='"
                        + ".Statuses" + "'", null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        Collections.reverse(listOfAllImages);
        return listOfAllImages;
    }

}
