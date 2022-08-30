package com.jatpack.socialmediahub.ui.status;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.activities.BaseActivity;
import com.jatpack.socialmediahub.databinding.ActivityStatusDetailBinding;
import com.jatpack.socialmediahub.helper.MediaPreferences;
import com.jatpack.socialmediahub.util.AppUtils;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class StatusPriviewActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    Activity activity;
    ActivityStatusDetailBinding binding;
    private Bitmap bitmap;

    private MediaPreferences mediaPreferences;
    TextView lbl_tittle;
    private boolean isPortraitOrientation = false;
    String imgPath = "", imgName = "";
    boolean isFromStatus;
    // private boolean isToolbarHidden=true;
    List<DocumentFile> documentFileArrayList = null;
    MediaPlayer mediaPlayer;
    ViewPager viewPager_imgDetail;
    List<AllMediaListing_mainModel> allMediaListing_mainModels = null;
    List<File> statusFileList = null;
    List<DocumentFile> statusDocumentFileList = null;
    StatusDetail_slideAdapter imageDetail_slideAdapter = null;

    private ImageDetail_appViewModel imgDetail_appViewModel = null;
    private int imgSelectedPos;

    public static String TAG = "ImageDetailActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_image_detail);

        activity = this;
        mediaPreferences = new MediaPreferences(this);
        mediaPlayer = new MediaPlayer();


        init();

        if (AppUtils.check_hasExtra(activity, "absoluteImgPath")
                && AppUtils.check_hasExtra(activity, "imgName")
                && AppUtils.check_hasExtra(activity, "selectedPos")
            /*&& Utils.check_hasExtra(activity,"img_allList")*/) {
            imgPath = AppUtils.getPutExtraValue(activity, "absoluteImgPath");
            imgName = AppUtils.getPutExtraValue(activity, "imgName");
            isFromStatus = getIntent().getBooleanExtra("isFromStatus", false);
            imgSelectedPos = Integer.parseInt(AppUtils.getPutExtraValue(activity, "selectedPos"));

            /*Type typeToken = new TypeToken<ArrayList<AllMediaListing_mainModel>>(){}.getType();
            allMediaListing_mainModels = new Gson().fromJson(getIntent().getStringExtra("img_allList")
                    ,typeToken);*/

            if (isFromStatus) {
                binding.videoDelete.setVisibility(View.GONE);
                binding.videoSave.setVisibility(View.VISIBLE);
            } else {
                binding.videoDelete.setVisibility(View.VISIBLE);
                binding.videoSave.setVisibility(View.GONE);
            }


            if (AllMediaListingImage_singleton.getInstance().getAllStatusFiles() != null ||
                    AllMediaListingImage_singleton.getInstance().getAllStatusDocumentFiles() != null) {
                statusFileList = AllMediaListingImage_singleton.getInstance().getAllStatusFiles();
                statusDocumentFileList = AllMediaListingImage_singleton.getInstance().getAllStatusDocumentFiles();
//                    binding.videoDelete.setVisibility(View.GONE);
//                    binding.videoSave.setVisibility(View.VISIBLE);
            }

            // Log.e("##imgPathValue", imgName);


//            binding.toolbarImgDetail.lblTittle.setText("" + imgName);

            loadSlider(imgSelectedPos);
        } else {
//            Toast.makeText(activity, "" + Utils.getStringResource(activity,
//                    R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
            finish();
        }


        binding.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String total_time=convertDurationMillis(total);

                playAudio();
                if (isMediaCntrollerVisible) {
                    showHide_toolbar_mediaController(0);
                } else {
                    showHide_toolbar_mediaController(1);
                }
            }
        });
        binding.imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });

        viewPager_imgDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                imgSelectedPos = position;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {
                        if (statusDocumentFileList.get(position).getName() != null &&
                                !statusDocumentFileList.get(position).getName().equals("") && statusDocumentFileList.get(position).getName().endsWith(".mp4")) {
                            binding.statusVideoContainer.setVisibility(View.VISIBLE);
//                                binding.toolbarImgDetail.lblTittle.setText("" + statusDocumentFileList.get(position).getName());

//                            binding.statusImageContainer.setVisibility(View.GONE);

                            documentFileArrayList = AllMediaListingImage_singleton.getInstance().getAllStatusDocumentFiles();

                            try {
                                binding.videoView.setVideoURI(Uri.parse(documentFileArrayList
                                        .get(position)
                                        .getUri().toString()));

                                binding.videoView.seekTo(1); // display video thumbnail
                                // binding.lblTotalTime.setText((int) statusFileList.get(position).getTotalSpace());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            binding.statusVideoContainer.setVisibility(View.GONE);
//                            binding.statusImageContainer.setVisibility(View.VISIBLE);
                            if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {

//                                    binding.toolbarImgDetail.lblTittle.setText("" + statusDocumentFileList.get(position).getName());

                            }
                        }
                    } else {

                        if (statusFileList.get(position).getName().endsWith(".mp4")) {
                            binding.statusVideoContainer.setVisibility(View.VISIBLE);
//                                binding.toolbarImgDetail.lblTittle.setText("" + statusFileList.get(position).getName());

//                            binding.statusImageContainer.setVisibility(View.GONE);

                            statusFileList = AllMediaListingImage_singleton.getInstance().getAllStatusFiles();

                            try {
                                binding.videoView.setVideoURI(Uri.parse(statusFileList
                                        .get(position)
                                        .getPath()));

                                binding.videoView.seekTo(1); // display video thumbnail
                                // binding.lblTotalTime.setText((int) statusFileList.get(position).getTotalSpace());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            binding.statusVideoContainer.setVisibility(View.GONE);
//                            binding.statusImageContainer.setVisibility(View.VISIBLE);
                            if (statusFileList != null && statusFileList.size() > 0) {

//                                    binding.toolbarImgDetail.lblTittle.setText("" + statusFileList.get(position).getName());

                            }
                        }
                    }
                } else {
                    if (statusFileList.get(position).getName().endsWith(".mp4")) {
                        binding.statusVideoContainer.setVisibility(View.VISIBLE);
//                            binding.statusImageContainer.setVisibility(View.GONE);
                        Type typeToken = new TypeToken<ArrayList<File>>() {
                        }.getType();
//                            statusFileList = new Gson().fromJson(getIntent().getStringExtra("video_allList")
//                                    , typeToken);

                        // binding.lblTotalTime.setText((int) statusFileList.get(position).getTotalSpace());

                        try {
                            binding.videoView.setVideoURI(Uri.parse(statusFileList
                                    .get(position)
                                    .getAbsolutePath()));

                            binding.videoView.seekTo(1); // display video thumbnail

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        binding.statusVideoContainer.setVisibility(View.GONE);
//                            binding.statusImageContainer.setVisibility(View.VISIBLE);
                        if (statusFileList != null && statusFileList.size() > 0) {

//                                binding.toolbarImgDetail.lblTittle.setText("" + statusFileList.get(position).getName());

                        }
                    }


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                playAudio();
            }
        });


        if (imgDetail_appViewModel != null) {
            imgDetail_appViewModel.getScreenOrientation()
                    .observe(this,
                            new Observer<ImageDetail_screenOrientation_model>() {
                                @SuppressLint("SourceLockedOrientationActivity")
                                @Override
                                public void onChanged(ImageDetail_screenOrientation_model model) {
                                    Log.e("#videpScreenOrientaion", String.valueOf(model
                                            .getPortraitOrientation()));

                                    if (model.getPortraitOrientation()) {
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                        isPortraitOrientation = true;

                                    } else {
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        isPortraitOrientation = false;

                                    }

                                }
                            });

            imgDetail_appViewModel.isFirstTimeLivedata_ads.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    Log.d(TAG, "onChanged: " + aBoolean);
                    if (aBoolean) {

                    } else {
                        // AHandler.getInstance().showFullAds(StatusPriviewActivity.this, false);
                        imgDetail_appViewModel.setIssFirstTime_ads(true);
                    }
                }
            });
        }


    }

    public void init() {
        try {
            binding = ActivityStatusDetailBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            lbl_tittle = findViewById(R.id.lbl_tittle);
            viewPager_imgDetail = findViewById(R.id.viewPager_imgDetail);


           /* LinearLayout adsbanner = findViewById(R.id.adsbanner);
            adsbanner.addView(AHandler.getInstance().getBannerHeader(this));*/

            //  AHandler.getInstance().showFullAds(this,false);

            if (imgDetail_appViewModel == null) {
                imgDetail_appViewModel = new ViewModelProvider(this)
                        .get(ImageDetail_appViewModel.class);
            }

            binding.videoDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        deleteVideo(allMediaListing_mainModels.get(imgSelectedPos)
//                                        .getAbsolutePathOfImage(),
//                                allMediaListing_mainModels.get(imgSelectedPos)
//                                        .getImageTitle());

//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//
//                            try {
//                                deleteImages(Uri.parse(statusFileList.get(imgSelectedPos)
//                                        .getAbsolutePath()),imgSelectedPos);
//                            } catch (IntentSender.SendIntentException e) {
//                                e.printStackTrace();
//                            }
//                        }else {
                    show_DeletepRompt(statusFileList.get(imgSelectedPos)
                            .getAbsolutePath());
//                        }


                }
            });

            binding.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pauseAudio();
                    if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {

                        shareImage(StatusPriviewActivity.this, statusDocumentFileList.get(imgSelectedPos).getUri());
                    } else if (statusFileList != null && statusFileList.size() > 0) {
                        Log.d("StatusPriviewActivity", "Hello onClick hi pathhh" + " " + statusFileList.get(imgSelectedPos)
                                .getPath());
                        Uri uri = FileProvider.getUriForFile(
                                StatusPriviewActivity.this,
                                getApplicationContext().getPackageName() + ".provider",
                                new File(statusFileList.get(imgSelectedPos)
                                        .getPath()));
                        shareImage(StatusPriviewActivity.this, uri);
                    }
                }
            });

            binding.llRepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {

                        setwatsappdp(statusDocumentFileList.get(imgSelectedPos).getUri());
                    } else if (statusFileList != null && statusFileList.size() > 0) {
                        Log.d("StatusPriviewActivity", "Hello onClick hi pathhh" + " " + statusFileList.get(imgSelectedPos)
                                .getPath());
                        Uri uri = FileProvider.getUriForFile(
                                StatusPriviewActivity.this,
                                getApplicationContext().getPackageName() + ".provider",
                                new File(statusFileList.get(imgSelectedPos)
                                        .getPath()));
                        setwatsappdp(uri);
                    }
                }
            });
            binding.videoSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("StatusPriviewActivity", "Hello onClick hihihih selcted position" + " " + imgSelectedPos);
                    if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {
                        if (statusDocumentFileList.get(imgSelectedPos).getUri().getPath().endsWith(".mp4")) {
                            saveVideo(statusDocumentFileList.get(imgSelectedPos).getUri(), ".mp4");
                        } else {
                            saveVideo(statusDocumentFileList.get(imgSelectedPos).getUri(), ".jpg");

                        }
//                        copyFileOrDirectory(statusDocumentFileList.get(imgSelectedPos).getUri().toString(),Utils.createAppDir(ImageDetailActivity.this));

                    } else if (statusFileList != null && statusFileList.size() > 0) {

                        copyFileOrDirectory(statusFileList.get(imgSelectedPos).getAbsolutePath(), AppUtils.createAppDir(StatusPriviewActivity.this));

                    }
                }
            });
        } catch (Exception e) {
            Log.d("StatusPriviewActivity", "Hello init adjsgffjsdhgfasdjfagsjkhD" + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ll_back: {
                    onBackPressed();
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSlider(int postion) {

        if (statusFileList != null && statusFileList.size() > 0) {

            if (statusFileList.get(postion).getName().endsWith(".mp4")) {
                binding.statusVideoContainer.setVisibility(View.VISIBLE);
//                binding.toolbarImgDetail.lblTittle.setText("" + statusFileList.get(postion).getName());

//                            binding.statusImageContainer.setVisibility(View.GONE);

                statusFileList = AllMediaListingImage_singleton.getInstance().getAllStatusFiles();

                try {
                    binding.videoView.setVideoURI(Uri.parse(statusFileList
                            .get(postion)
                            .getPath()));

                    binding.videoView.seekTo(1); // display video thumbnail
                    // binding.lblTotalTime.setText(binding.videoView.getDuration());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imageDetail_slideAdapter = new StatusDetail_slideAdapter(activity,
                    null, statusFileList, null, new StatusDetail_slideAdapter.getItemClick() {
                @Override
                public void onItemClickListner(int pos) {
                    if (isMediaCntrollerVisible) {
                        showHide_toolbar_mediaController(0);
                    } else {
                        showHide_toolbar_mediaController(1);
                    }
                }
            });


            viewPager_imgDetail.setAdapter(imageDetail_slideAdapter);
            viewPager_imgDetail.setCurrentItem(postion);
        } else if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {

            if (statusDocumentFileList.get(postion).getName().endsWith(".mp4")) {
                binding.statusVideoContainer.setVisibility(View.VISIBLE);
//                binding.toolbarImgDetail.lblTittle.setText("" + statusDocumentFileList.get(postion).getName());

//                            binding.statusImageContainer.setVisibility(View.GONE);

                documentFileArrayList = AllMediaListingImage_singleton.getInstance().getAllStatusDocumentFiles();

                try {
                    binding.videoView.setVideoURI(Uri.parse(documentFileArrayList
                            .get(postion)
                            .getUri().toString()));

                    binding.videoView.seekTo(1); // display video thumbnail
                    //binding.lblTotalTime.setText((int) statusFileList.get(postion).getTotalSpace());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imageDetail_slideAdapter = new StatusDetail_slideAdapter(activity,
                    null, null, statusDocumentFileList, new StatusDetail_slideAdapter.getItemClick() {
                @Override
                public void onItemClickListner(int pos) {
                    if (isMediaCntrollerVisible) {
                        showHide_toolbar_mediaController(0);
                    } else {
                        showHide_toolbar_mediaController(1);
                    }
                }
            });

            viewPager_imgDetail.setAdapter(imageDetail_slideAdapter);
            viewPager_imgDetail.setCurrentItem(postion);

        } else {
            if (allMediaListing_mainModels != null) {
                imageDetail_slideAdapter = new StatusDetail_slideAdapter(activity,
                        allMediaListing_mainModels, null, null, new StatusDetail_slideAdapter.getItemClick() {
                    @Override
                    public void onItemClickListner(int pos) {
                        if (isMediaCntrollerVisible) {
                            showHide_toolbar_mediaController(0);
                        } else {
                            showHide_toolbar_mediaController(1);
                        }
                    }
                });
                viewPager_imgDetail.setAdapter(imageDetail_slideAdapter);
                viewPager_imgDetail.setCurrentItem(postion);
            }
        }


    }


    public static boolean delete(final Context context, final File file) {
        Log.d("ImageDetailActivity", "Hello show_DeletepRompt test delete path >>>" + " " + file.getAbsolutePath());

        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    private void setwatsappdp(Uri uri) {


        try {
            if (uri != null) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
        } catch (Exception e) {
//            Log.d(TAG, "setwatsappdp1: " + e.getMessage());
        }

        if (bitmap != null) {

            Uri urisqure = saveImage(cropToSquare(bitmap));
            PackageManager pm = getPackageManager();
           // Log.d("TAG", "setSingleStatus5: " + urisqure);
            try {
                // Raise exception if whatsapp doesn't exist
                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                Intent waIntent = new Intent(Intent.ACTION_ATTACH_DATA);
                waIntent.setDataAndType(urisqure, "image/jpg");
                waIntent.setPackage("com.whatsapp");
                waIntent.putExtra("mimeType", "image/jpg");
                startActivity(waIntent);
                //   finish();
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "Please install whatsapp app", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void shareImage(Activity activity, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Download this cool app from " +
                "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(intent, "Share..."));

    }

    public void shareFileImage(Activity activity, String path) {
        ArrayList<Uri> uris = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
//        for (String path : paths) {
        File file = new File(path);
        uris.add(FileProvider.getUriForFile(StatusPriviewActivity.this, "com.pnd.shareall.provider", file));
//        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(intent);
    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {

            Log.d("ImageDetailActivity", "Hello copyFileOrDirectory hi test path" + " " +
                    srcDir + " " + ">>>>>> " + dstDir);
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());
            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                    System.out.println("my video dir if" + " " + src1 + " " + dst1);
                }
            } else {
                copyFile(src, dst);
                System.out.println("my video dir else" + " " + src.getPath() + " " + dst.getPath());
            }


        } catch (Exception e) {
            System.out.println("qsdafqhakj" + " " + e);
            e.printStackTrace();
        }
    }

    private static String APP_DIR = null;

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        System.out.println("MY LOG CHECK 01");

        if (!destFile.exists()) {
            destFile.createNewFile();
            System.out.println("MY LOG CHECK 02 ggfahsdgfahj" + " " + destFile.getPath());

            //  mediaPreferences.setgallerycount(mediaPreferences.getgallerycount() + 1);

            Toast.makeText(StatusPriviewActivity.this, getResources().getString(R.string.save_image_toast), Toast.LENGTH_LONG).show();


        } else {
            System.out.println("MY LOG CHECK override");

            Toast.makeText(StatusPriviewActivity.this, "This Image is already saved ", Toast.LENGTH_LONG).show();


        }


        FileChannel source = null;
        FileChannel destination = null;

        try {
            System.out.println("MY LOG CHECK 03");
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
                System.out.println("MY LOG CHECK 04");
            }
            if (destination != null) {
                System.out.println("MY LOG CHECK 05");
                destination.close();
            }
        }
    }


    private void saveVideo(Uri myUri, String fileExtension) {
        File file;
        try {
            String FileName = System.currentTimeMillis() + fileExtension;
            File path = new File(AppUtils.createAppDir(StatusPriviewActivity.this));
            if (path.exists()) {
                file = new File(path, "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                FileUtils.copyInputStreamToFile(inputStream, file);

            } else {
                path.mkdir();
                file = new File(path, "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            Toast.makeText(StatusPriviewActivity.this, getResources().getString(R.string.saved_image_toast), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("TAG", "saveVideo: " + e.getMessage());
        }

    }

    public void show_DeletepRompt(String filepath) {
        Log.d("ImageDetailActivity", "Hello show_DeletepRompt test delete path" + " " + filepath);
        File path = new File(filepath);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_delete);
        dialog.getWindow().setBackgroundDrawable(null);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        LinearLayout ll_okProceed;
        TextView ll_cancel;
        ll_cancel = dialog.findViewById(R.id.ll_cancel);
        ll_okProceed = dialog.findViewById(R.id.ll_okProceed);
//        LinearLayout ll_dont_ask_again = dialog.findViewById(R.id.ll_dont_ask_again);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        ll_okProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    Log.d("ImageDetailActivity", "Hello show_DeletepRompt test delete path >>>" + " ");

                    path.delete();

//                    if(delete(StatusPriviewActivity.this,path)){
                    if (mediaPreferences != null) {
                        mediaPreferences.setRefresh(true);
                    }
                    Toast.makeText(StatusPriviewActivity.this, getResources().getString(R.string.delete_image), Toast.LENGTH_SHORT).show();

//                    }

                    dialog.dismiss();

                    finish();


                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();
    }


    private void deleteImages(Uri uris, int selectedPos) throws IntentSender.SendIntentException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // WARNING: if the URI isn't a MediaStore Uri and specifically
            // only for media files (images, videos, audio) then the request
            // will throw an IllegalArgumentException, with the message:
            // 'All requested items must be referenced by specific ID'

            // No need to handle 'onActivityResult' callback, when the system returns
            // from the user permission prompt the files will be already deleted.
            // Multiple 'owned' and 'not-owned' files can be combined in the
            // same batch request. The system will automatically delete them
            // using the same prompt dialog, making the experience homogeneous.

            ArrayList<Uri> fichiers = new ArrayList<>();
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    Long.parseLong(allMediaListing_mainModels.get(selectedPos).getImageId()));
//            File file = new File(allMediaListing_mainModels.get(selectedPos).getAbsolutePathOfImage());


            fichiers.add(contentUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                PendingIntent demande = MediaStore.createDeleteRequest(getContentResolver(), fichiers);
                try {
                    startIntentSenderForResult(demande.getIntentSender(), 199, new Intent(), 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 199 && resultCode == Activity.RESULT_OK) {
            // Image deleted successfully
            Toast.makeText(this, getResources().getString(R.string.delete_image), Toast.LENGTH_LONG).show();

            if (mediaPreferences != null) {
                mediaPreferences.setRefresh(true);
            }
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        binding.videoView.seekTo(seekBar.getProgress());

    }

    public void playAudio() {
        try {

            if (mediaPlayer != null) {
              /*  String total = String.format(
                        Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()))
                );*/
                binding.lblTotalTime.setText(AppUtils.timeConversion1((long)
                        binding.videoView.getDuration()));
                binding.videoView.requestFocus();
                binding.videoView.start();


                binding.imgPause.setVisibility(View.VISIBLE);
                binding.imgPlay.setVisibility(View.GONE);

                updateSeekbarPosition();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String formattedTime(int mCurrentPosition) {
        int sec = mCurrentPosition % 60;
        int min = (mCurrentPosition / 60) % 60;
        int hours = (mCurrentPosition / 60) / 60;

        String strSec = (sec < 10) ? "0" + Integer.toString(sec) : Integer.toString(sec);
        String strmin = (min < 10) ? "0" + Integer.toString(min) : Integer.toString(min);
        String strHours = (hours < 10) ? "0" + Integer.toString(hours) : Integer.toString(hours);
        return strHours + ":" + strmin;
    }

    public String convertDurationMillis(Integer getDurationInMillis) {

        int getDurationMillis = getDurationInMillis;

        String convertHours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(getDurationMillis));
        String convertMinutes = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(getDurationMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getDurationMillis))); //I needed to add this part.
        String convertSeconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(getDurationMillis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getDurationMillis)));


        String getDuration = convertHours + ":" + convertMinutes + ":" + convertSeconds;

        return getDuration;

    }

    public void updateSeekbarPosition() {
        try {
            binding.seekbar.setMax(binding.videoView.getDuration());

            final Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {

                        binding.lblCurrentTime.setText(AppUtils.timeConversion1((long)
                                binding.videoView.getCurrentPosition()));

                        binding.seekbar.setProgress(binding.videoView.getCurrentPosition());
                        // seekbar.setProgress(60000);
                        handler.postDelayed(this, 1000);
                    } catch (Exception ed) {
                        ed.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseAudio() {
        try {

            binding.videoView.pause();

            binding.imgPause.setVisibility(View.GONE);
            binding.imgPlay.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHide_toolbar_mediaController(int type) {
        // 0 -> hide & 1 -> show
        if (type == 0) {
            binding.bottomLayout.setVisibility(View.GONE);
//            toolbar.setVisibility(View.GONE);
            // Hide status bar
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isMediaCntrollerVisible = false;
        } else {
            binding.bottomLayout.setVisibility(View.VISIBLE);
//            toolbar.setVisibility(View.VISIBLE);
            // Show status bar
            // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isMediaCntrollerVisible = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        AllMediaListingImage_singleton.getInstance().setAllStatusFiles(null);
        AllMediaListingImage_singleton.getInstance().setAllStatusDocumentFiles(null);
    }

    //time conversion
    public static String timeConversion1(long value) {
        String videoTime = "";
        try {
            int dur = (int) value;
            int hrs = (dur / 3600000);
            int mns = (dur / 60000) % 60000;
            int scs = dur % 60000 / 1000;

            if (hrs > 0) {
                videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
            } else {
                videoTime = String.format("%02d:%02d", mns, scs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.e("videoTime::",videoTime);
        return videoTime;
    }


    private Uri saveImage(Bitmap finalBitmap) {
        //String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        //System.out.println(root + " Root value in saveImage Function");
        String bitmapPath;
        /*File myDir = null;
        if (!myDir.exists()) {
            myDir.mkdirs();
        }*/

        String iname = "Photo0" + ".jpg";
//        Log.d(TAG, "saveImage: "+statusDocumentFileList.get(imgSelectedPos).getUri());

        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(String.valueOf(statusDocumentFileList.get(imgSelectedPos).getUri()));
        }else{
            file=new File(String.valueOf(statusFileList.get(imgSelectedPos).getPath()));
        }
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmapPath = String.valueOf(getImageContentUri(StatusPriviewActivity.this, file));

        System.out.println("ExternalStorage saveImage" + bitmapPath);

        return Uri.parse(bitmapPath);

    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Bitmap cropToSquare(Bitmap srcBmp) {
        int dim = Math.max(srcBmp.getWidth(), srcBmp.getHeight());


        Bitmap dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBmp);
        canvas.setBitmap(dstBmp);
        canvas.drawBitmap(srcBmp, (dim - srcBmp.getWidth()) / 2, (dim - srcBmp.getHeight()) / 2, null);

        return dstBmp;
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String mp3Minutes = "";
        // Convert total duration into time

        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
// menor que 10
        if (minutes < 10) {
            mp3Minutes = "0" + minutes;
        } else {
            mp3Minutes = "" + minutes;
        }
        finalTimerString = finalTimerString + mp3Minutes + ":" + secondsString;
        // return timer string
        return finalTimerString;
    }


    private boolean isMediaCntrollerVisible = true;

}