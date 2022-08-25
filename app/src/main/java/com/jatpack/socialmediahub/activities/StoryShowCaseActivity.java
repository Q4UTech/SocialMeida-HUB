package com.jatpack.socialmediahub.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.allenxuan.xuanyihuang.xuanimageview.XuanImageView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.helper.FilesHelper;
import com.jatpack.socialmediahub.helper.MediaPreferences;
import com.jatpack.socialmediahub.listener.HelperListener;
import com.jatpack.socialmediahub.util.AppUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.shts.android.storiesprogressview.StoriesProgressView;

/**
 * Created by Anon on 13,August,2018
 */
public class StoryShowCaseActivity extends BaseActivity implements StoriesProgressView.StoriesListener {

    private static final int PROGRESS_COUNT = 6;
    public static final String STATUS_IMAGE = "Status Image";
    private String getpathstr;
    private File file;
    private MediaPreferences mediaPreferences;
    private Bitmap b;

    private int counter = 0;
    private String image_path;
    private String copypath;
    private Uri myUri;
    private int tempetc, tempetc1;

    private XuanImageView image;
    private StoriesProgressView spv;
    private Button btnsaved;
    private VideoView videoViews;
    private TextView date;
    private View skip, reverse;
    private ImageView fab_save, fab_share, fab_story, next, privious;
    private LinearLayout adsBanner, relative_ads_background, rl_set_status, rl_download;
    private RelativeLayout rl_toplayout, ll_parent, adsView, rl_share;

    private Uri download_uri;


    private ArrayList<File> mList = new ArrayList<>();
    private ArrayList<DocumentFile> fList = new ArrayList<>();
    private ArrayList<DocumentFile> documentFile;

    private boolean isPaused = false;
    private boolean isNotification = false;
    private int latestFiles;

    private BottomSheetBehavior setStatusBottomSheet, downloadBottomSheet;
    private RelativeLayout setStatusContainer, downloadBottomSheetContainer;
    private ImageView iv_background, iv_close;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterImmersiveMode();
        setContentView(R.layout.activity_show_case);
        statusBarColor(ContextCompat.getColor(this, R.color.color_trans));
//        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar_main_image);
        toolbar.setNavigationIcon(R.drawable.ic_title_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setStatusContainer = findViewById(R.id.bottom_sheet_status);
        iv_background = findViewById(R.id.iv_background);
        iv_close = findViewById(R.id.iv_close);
        setStatusBottomSheet = BottomSheetBehavior.from(setStatusContainer);

        // downloadBottomSheetContainer = findViewById(R.id.bottom_sheet_download);
        //  downloadBottomSheet = BottomSheetBehavior.from(downloadBottomSheetContainer);

        videoViews = findViewById(R.id.videoView);
        rl_toplayout = findViewById(R.id.rl_toplayout);
        adsBanner = findViewById(R.id.adsBanner);
        adsBanner = findViewById(R.id.adsBanner);
        btnsaved = findViewById(R.id.btnsaved);
        next = findViewById(R.id.next);
        privious = findViewById(R.id.privious);
      /*  rl_set_status = findViewById(R.id.rl_set_status);
        fab_share = findViewById(R.id.tv_share);
        rl_share = findViewById(R.id.rl_share);
        fab_save = findViewById(R.id.tv_download);
        rl_download = findViewById(R.id.rl_download);*/
        spv = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        date = findViewById(R.id.date);
        relative_ads_background = findViewById(R.id.relative_ads_background);
        adsView = findViewById(R.id.adsView);
        ll_parent = findViewById(R.id.ll_parent);

        mediaPreferences = new MediaPreferences(StoryShowCaseActivity.this);
        XuanImageView xuanImageView = new XuanImageView(StoryShowCaseActivity.this);
        xuanImageView.setImageResource(R.id.image);
        copypath = savedpath();
        image.setDoubleTapScaleRunnableDelay(1000 * 1 * 60);

        System.out.println("hello testplz .onCreate:helo bro how are u ");

      /*  if (!Slave.ETC_5.equalsIgnoreCase("") && TextUtils.isDigitsOnly(Slave.ETC_5)) {
            System.out.println("i m here slave etc5 value " + " " + Slave.ETC_5);
            tempetc = Integer.parseInt(Slave.ETC_5);
            tempetc1 = tempetc + 1;

        }

        ll_parent.setOnTouchListener(new OXSwipe() {
            @Override
            public void leftSwipe() {
                System.out.println("i m herer f0r n touched19998 gfhghjfsd left");

                onPreviousClicked();
            }

            @Override
            public void rightSwipe() {

                System.out.println("i m herer f0r n touched19998 gfhghjfsd right");
                onNextClicked();
            }

            @Override
            public void upSwipe() {

                ll_parent.setVisibility(View.GONE);
            }

            @Override
            public void downSwipe() {

                ll_parent.setVisibility(View.GONE);
            }

            @Override
            public void oneTouch() {
                System.out.println("i m herer f0r n touched19998 is open");
                if (!isPaused) {

                    System.out.println("i m herer f0r n touched19998");
                    spv.pause();
                    ll_parent.setVisibility(View.GONE);
                    isPaused = true;
                } else {

                    System.out.println("i m herer f0r n touched199982");
                    spv.resume();

                    isPaused = false;
                }

                ll_parent.setVisibility(View.GONE);


            }

        });*/

        iv_close.setOnClickListener(v -> hideBottomSheet());

        iv_background.setOnClickListener(v -> {
        });

        privious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("check.onHelperFinished myTest: 05helllo" + " ");
                onNextClicked();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousClicked();
            }
        });
        Intent intent = getIntent();

        try {

            System.out.println("check.onHelperFinished myTest: 05" + " " + counter);
            counter = intent.getIntExtra("position", 0);
            image_path = intent.getStringExtra("imageuri");
            myUri = Uri.parse(intent.getStringExtra("imageuri_new"));
            isNotification = intent.getExtras().getBoolean("isNotification");
            latestFiles = intent.getExtras().getInt("latestFiles");
            download_uri = Uri.parse(intent.getStringExtra("doc_imageuri"));

            System.out.println("check.onHelperFinished myTest: 06" + " " + counter);
            System.out.println("check.onHelperFinished myTest: 07" + " " + image_path.toString());
            System.out.println("check.onHelperFinished myTest: 08" + " " + myUri.toString());
            System.out.println("check.onHelperFinished myTest: 08 aaa" + " " + download_uri.toString());

        } catch (Exception e) {
            System.out.println("hello testplz .onCreate: excep  " + e.getMessage());
        }

     //   adsBanner.addView(AHandler.getInstance().getBannerHeader(this));

        btnsaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, false);
                spv.pause();
                //saveImage();
            }
        });

        rl_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("here is the get media tiemkjklsaDKLJ");
             //   AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, false);
                spv.pause();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    saveImageStatus(download_uri);

                } else {
                    saveImage();
//                 copyFileOrDirectory(myUri.getPath(), copypath);
                }
            }
        });

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test.onClick sharebutton kasak1");
                spv.pause();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    shareImageStatus();

                } else {

                    try {
                        b = BitmapFactory.decodeFile(mList.get(counter).getAbsolutePath());
                    } catch (Exception e) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                        int imageHeight = options.outHeight;
                        int imageWidth = options.outWidth;
                        String imageType = options.outMimeType;
                    }

                    Uri uri = null;
                    try {
                        uri = getImageUri(StoryShowCaseActivity.this, b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //shareImage(StoryShowCaseActivity.this, uri);
                  //  AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, false);
//                fam.close(true);

                }
            }
        });


        rl_set_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheet();

            }
        });


        setStatusBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        iv_background.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        iv_background.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

/*
        downloadBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        iv_background.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        iv_background.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
*/

//helllo start
        System.out.println("here is the get media tiem" + mediaPreferences.getServiceTime());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            System.out.println("check.onHelperFinished myTest: 02");

            documentFile = new ArrayList<>();
            if (mediaPreferences.getFileImage()) {
                System.out.println("check.onHelperFinished myTest: 03");

                String strmyuri = mediaPreferences.getFileUri();
                Uri myUri = Uri.parse(strmyuri);
                DocumentFile docFile = DocumentFile.fromTreeUri(getApplicationContext(), myUri);
                for (DocumentFile file : docFile.listFiles()) {
                    documentFile.add(file);
//                    if (file.getUri().getPath().endsWith(".jpg") || ) {
//
//                    }
                }

                for (int i = 0; i < documentFile.size(); i++) {
                    if (documentFile.get(i).getUri().getPath().equalsIgnoreCase(image_path)) {
                        System.out.println("my click pos here" + " " + i);
                        counter = i;

                    }
                }

                fList = documentFile;

                System.out.println("check.onHelperFinished myTest: 04" + " " + fList.size());
                System.out.println("here is the total count of showcase abc11" + " " + mList.size() + " " + " " + counter);

                image.setVisibility(View.VISIBLE);
                if (!fList.isEmpty()) {
//                    float fdatetime = fList.get(counter).getParentFile().lastModified();
                    float fdatetime = fList.get(counter).getParentFile().lastModified();
                    date.setText(DATEFORMAT.format(fdatetime));
                }

                try {
                    b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());

                    System.out.println("simpleTest.onCreate mytest:00111" + " " + b + " Uri of item: " + fList.get(counter).getUri());
//                    Picasso.get().load(fList.get(counter).getUri()).into(image);

                } catch (Exception e) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                }

                Glide
                        .with(this)
                        .load(fList.get(counter).getUri())
                        .into(image);

//                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();

            }

        } else {

            new FilesHelper(new HelperListener() {
                @Override
                public void onHelperFinished(ArrayList<File> status) {

                    System.out.println("check.onHelperFinished myTest: 01");
                    ArrayList<File> images = new ArrayList<>();

                    for (File f : status) {

                        images.add(f);
//                        if (f.getAbsolutePath().endsWith(".jpg")) {
//                            images.add(f);
//                        }
                    }

                    for (int i = 0; i < images.size(); i++) {
                        if (images.get(i).getAbsolutePath().equalsIgnoreCase(image_path)) {
                            System.out.println("my click pos here" + " " + i);
                            counter = i;

                        }

                    }

                    mList = images;

                    System.out.println("here is the total count of showcase" + " " + mList.size() + " " + images.size() + " " + counter);
                    image.setVisibility(View.VISIBLE);
                    if (!mList.isEmpty()) {
                        float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                        date.setText(DATEFORMAT.format(fdatetime));
                    }
                    try {
                        b = BitmapFactory.decodeFile(mList.get(counter).getAbsolutePath());
                    } catch (Exception e) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                    }

                    image.setImageBitmap(b);
                    spv.setStoriesCount(1);
                    spv.setStoryDuration(mediaPreferences.getServiceTime());
                    spv.setStoriesListener(StoryShowCaseActivity.this);
                    spv.startStories();
                }
            }).loadHelper();
        }

        //helllo stop

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("my touuch count" + " " + event.getAction());
                System.out.println("my touuch count1212" + " " + event.getX());


                if (event.getAction() == 1) {
                    spv.resume();
                    ll_parent.setVisibility(View.VISIBLE);
                } else {
                    spv.pause();
                }

                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  spv.resume();

    }

    private void openBottomSheet() {
        if (setStatusBottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            iv_background.setVisibility(View.VISIBLE);
            setStatusBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

            findViewById(R.id.tv_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus();
                    hideBottomSheet();
                }
            });

            findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideBottomSheet();
                }
            });

        }
    }

    private void hideBottomSheet() {
        if (setStatusBottomSheet.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            iv_background.setVisibility(View.GONE);
            setStatusBottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onNext() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 001");
      /*  if (!Slave.hasPurchased(this) && isNetworkAvailable() && counter != 0 && Slave.ETC_5 != null && !Slave.ETC_5.equalsIgnoreCase("") &&
                TextUtils.isDigitsOnly(Slave.ETC_5) && Integer.parseInt(Slave.ETC_5) != 0 &&
                (counter == Integer.parseInt(Slave.ETC_5) || counter % tempetc1 == 0 && counter != tempetc1)) {

            adsView.setVisibility(View.VISIBLE);
            date.setText("Ads");


            relative_ads_background.removeAllViews();
           // relative_ads_background.addView(AHandler.getInstance().getBannerLarge(this));
            spv.setStoriesCount(1);
            spv.setStoryDuration(6000);
            spv.startStories();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(BitmapFactory.decodeFile(fList.get(++counter).getUri().getPath()));
                        float fdatetime = fList.get(counter).lastModified();
                        date.setText(DATEFORMAT.format(fdatetime));
                    } else {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(BitmapFactory.decodeFile(mList.get(++counter).getAbsolutePath()));
                        float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                        date.setText(DATEFORMAT.format(fdatetime));
                    }

                }
            }, 6000);


        } else {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(BitmapFactory.decodeFile(fList.get(++counter).getUri().getPath()));
                float fdatetime = fList.get(counter).lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
            } else {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(BitmapFactory.decodeFile(mList.get(++counter).getAbsolutePath()));
                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
            }

            Log.d("StoryShowCaseActivity", "Hello onNext opppes " + " " +
                    fList.get(counter).getUri());

        }*/
    }

    @Override
    public void onPrev() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 002");
        System.out.println("here is the total count of showcase privious" + " " + mList.size() + " " + counter);
       /* if (!Slave.hasPurchased(this) && isNetworkAvailable() && counter != 0 && Slave.ETC_5 != null && !Slave.ETC_5.equalsIgnoreCase("") &&
                TextUtils.isDigitsOnly(Slave.ETC_5) && Integer.parseInt(Slave.ETC_5) != 0 &&
                (counter == Integer.parseInt(Slave.ETC_5) || counter % tempetc1 == 0 && counter != tempetc1)) {
            adsView.setVisibility(View.VISIBLE);
            date.setText("Ads");

            relative_ads_background.removeAllViews();
            relative_ads_background.addView(AHandler.getInstance().getBannerLarge(this));
            spv.setStoriesCount(1);
            spv.setStoryDuration(6000);
            spv.startStories();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);
                    if ((counter - 1) < 0) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(BitmapFactory.decodeFile(fList.get(--counter).getUri().getPath()));
                        float fdatetime = fList.get(counter).lastModified();
                        date.setText(DATEFORMAT.format(fdatetime));
                    } else {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(BitmapFactory.decodeFile(mList.get(--counter).getAbsolutePath()));
                        float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                        date.setText(DATEFORMAT.format(fdatetime));
                    }

                }
            }, 6000);
        } else {
            if ((counter - 1) < 0) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(BitmapFactory.decodeFile(fList.get(--counter).getUri().getPath()));
                float fdatetime = fList.get(counter).lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
            } else {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(BitmapFactory.decodeFile(mList.get(--counter).getAbsolutePath()));
                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
            }

        }*/

//        }
    }

    @Override
    public void onComplete() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 003");
        System.out.println("here is the total count of showcase next" + " " + mList.size() + " " + counter);
       /* if (!Slave.hasPurchased(this) && isNetworkAvailable() && counter != 0 && Slave.ETC_5 != null && !Slave.ETC_5.equalsIgnoreCase("") &&
                TextUtils.isDigitsOnly(Slave.ETC_5) && Integer.parseInt(Slave.ETC_5) != 0 &&
                (counter == Integer.parseInt(Slave.ETC_5) || counter % tempetc1 == 0 && counter != tempetc1)) {
            adsView.setVisibility(View.VISIBLE);
            date.setText("Ads");
            //  counter--;
            relative_ads_background.removeAllViews();
            relative_ads_background.addView(AHandler.getInstance().getBannerLarge(this));
            spv.setStoriesCount(1);
            spv.setStoryDuration(6000);
            spv.startStories();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);
                    moveNext();

                }
            }, 6000);
        } else {

            moveNext();
        }*/

    }

    //auto completed than call moveNext
    private void moveNext() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 004 auto swap");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            Log.d("TAG", "moveNext: myTest 00abab 1: " + counter);

            if (counter != fList.size() - 1) {

                if(fList.get(counter).getName().endsWith(".mp4")){
                    ++counter;
                    moveNext();
                    return;
                }

                image.setVisibility(View.VISIBLE);
                Log.d("TAG", "moveNext: myTest 002: " + fList.size());
                try {
//                    Log.d("TAG", "moveNext: myTest 007: "+ fList.get(0).getUri().toString());


                    Glide
                            .with(this)
                            .load(fList.get(++counter).getUri())
                            .into(image);

                    download_uri = fList.get(counter).getUri();
//                    Picasso.get().load(fList.get(counter).getUri().getPath()).into(image);
                    b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());
                    System.out.println("adbzvnB.moveNext 001" + " " + counter + " " +
                            fList.get(counter).getUri().getPath());

                } catch (Exception e) {
                    Log.d("TAG", "moveNext: myTest 004 aaa: " + e.getMessage());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;


                    b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(fList.get(++counter).getUri().getPath()), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;
                }

//                float fdatetime = mList.get(counter).getParentFile().lastModified();
//                date.setText(DATEFORMAT.format(fdatetime));


                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();
//            }
            } else {
                finish();
                //AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, true);
            }
        } else {
            Log.d("TAG", "moveNext: myTest 004: " + counter);
            if (counter != mList.size() - 1) {
                image.setVisibility(View.VISIBLE);

                try {

                    b = BitmapFactory.decodeFile(mList.get(++counter).getAbsolutePath());
                    Log.d("TAG", "moveNext: myTest 005: " + b.toString());
                    Log.d("TAG", "moveNext: myTest 005: aaabb02 " + counter);

                } catch (Exception e) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;
                }

                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));

                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();
//            }
            } else {
                finish();
               // AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, true);
            }
        }


    }

    @Override
    protected void onDestroy() {
        spv.destroy();
        super.onDestroy();
    }

    private void onNextClicked() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 005");
       // AHandler.getInstance().showFullAds(this, false);

    /*    if (!Slave.hasPurchased(this) && isNetworkAvailable() && counter != 0 && Slave.ETC_5 != null && !Slave.ETC_5.equalsIgnoreCase("") &&
                TextUtils.isDigitsOnly(Slave.ETC_5) && Integer.parseInt(Slave.ETC_5) != 0 &&
                (counter == Integer.parseInt(Slave.ETC_5) || counter % tempetc1 == 0 && counter != tempetc1)) {
            adsView.setVisibility(View.VISIBLE);
            date.setText("Ads");


            relative_ads_background.removeAllViews();
            relative_ads_background.addView(AHandler.getInstance().getBannerLarge(this));
            spv.setStoriesCount(1);
            spv.setStoryDuration(6000);
            spv.startStories();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);
                    getmNext();

                }
            }, 6000);
        } else {
            getmNext();
        }*/

    }

    private void getmNext() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 006");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            System.out.println("i m here next check NextClicked0003");

            getPreviousStatus();

        } else {

            if ((counter - 1) < 0) {

                System.out.println("i m here next check NextClicked0003 hiii");
                image.setVisibility(View.VISIBLE);
                try {

                    b = BitmapFactory.decodeFile(mList.get(counter).getAbsolutePath());
                    System.out.println("i m here next check NextClicked0003 hiii00001");

                } catch (Exception e) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
//                int imageHeight = options.outHeight;
//                int imageWidth = options.outWidth;
//                String imageType = options.outMimeType;

                }

                System.out.println("i m here next check NextClicked0003 hiii00002");
                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();
//                    }

                return;

            } else {

                image.setVisibility(View.VISIBLE);

                try {
                    System.out.println("i m here next check NextClicked0003 hiii00003");

                    b = BitmapFactory.decodeFile(mList.get(--counter).getAbsolutePath());

                } catch (Exception e) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;
                }
//            float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
//            String mytext = Float.toString(fdatetime);
//            date.setText(mytext);


                System.out.println("i m here next check NextClicked0003 hiii00004");
                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();
//                    }
            }/* else {
                    finish();
                }*/

        }
    }

    private void onPreviousClicked() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 007");
        //AHandler.getInstance().showFullAds(this, false);
/*
        if (!Slave.hasPurchased(this) && isNetworkAvailable() && counter != 0 && Slave.ETC_5 != null && !Slave.ETC_5.equalsIgnoreCase("") &&
                TextUtils.isDigitsOnly(Slave.ETC_5) && Integer.parseInt(Slave.ETC_5) != 0 &&
                (counter == Integer.parseInt(Slave.ETC_5) || counter % tempetc1 == 0 && counter != tempetc1)) {
            adsView.setVisibility(View.VISIBLE);
            date.setText("Ads");

            relative_ads_background.removeAllViews();
          //  relative_ads_background.addView(AHandler.getInstance().getBannerLarge(this));
            spv.setStoriesCount(1);
            spv.setStoryDuration(6000);
            spv.startStories();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adsView.setVisibility(View.GONE);
                    getPrivious();
                }
            }, 6000);
        } else {
            getPrivious();

        }*/

    }

    private void getPrivious() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 008");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            loadNextStatus();

        } else {

            if (counter != mList.size() - 1) {
                image.setVisibility(View.VISIBLE);
                try {
                    b = BitmapFactory.decodeFile(mList.get(++counter).getAbsolutePath());
                } catch (Exception e) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
//                int imageHeight = options.outHeight;
//                int imageWidth = options.outWidth;
//                String imageType = options.outMimeType;
                }

                float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
                date.setText(DATEFORMAT.format(fdatetime));
                image.setImageBitmap(b);
                spv.setStoriesCount(1);
                spv.setStoryDuration(mediaPreferences.getServiceTime());
                spv.setStoriesListener(StoryShowCaseActivity.this);
                spv.startStories();
//                    }
            } else {
                finish();
            }

        }
    }

    private void saveImage() {
        String FileName = System.currentTimeMillis() + ".jpg";
        File path = new File(AppUtils.createAppDir(StoryShowCaseActivity.this));
        System.out.println("CaseActivity.saveImage00001: " + path.getAbsolutePath());
        if (path.exists()) {
            file = new File(path, "/" + FileName);
            getpathstr = file.getAbsolutePath();
            file.getParentFile().mkdirs();
            System.out.println("CaseActivity.saveImage00002: " + file.getParentFile().mkdirs());
            func();

        } else {

            path.mkdir();
            file = new File(path, "/" + FileName);
            getpathstr = file.getAbsolutePath();
            System.out.println("CaseActivity.saveImage00003: " + file.getAbsolutePath());
            func();
        }

        FileOutputStream out = null;
        FileInputStream inputStream = null;
        //Bitmap b = Bitmap.createBitmap(framelayout.getDrawingCache());
        try {

            b = BitmapFactory.decodeFile(mList.get(counter).getAbsolutePath());
//            b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());
            System.out.println("getImagePath.saveImage0001: " + b.toString());

        } catch (Exception e) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
//            b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
        }
        int findlocation = ActivityCompat.checkSelfPermission(StoryShowCaseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (findlocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StoryShowCaseActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 12345);
        } else {
            try {
                out = new FileOutputStream(file);
                // bitmap = croptoheight(b);
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
                //  testdpimage.setImageBitmap(cropToSquare(b));
                //  framelayout.destroyDrawingCache();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DownloadComplete downloadComplete = new DownloadComplete(STATUS_IMAGE);
        downloadComplete.show(getSupportFragmentManager(), "show");

        mediaPreferences.setgallerycount(mediaPreferences.getgallerycount() + 1);
        mediaPreferences.setdownloadedboolean(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spv.resume();
            }
        }, 2000);

    }

    //call when scrool next
    public void loadNextStatus() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 009 when swip"+" "
        +fList.get(counter).getName()+" "+counter);

        if (counter != fList.size() - 1) {

            if(fList.get(counter).getName().endsWith(".mp4")){
                ++counter;
                loadNextStatus();
                return;
            }

            image.setVisibility(View.VISIBLE);
            try {
//                b = BitmapFactory.decodeFile(fList.get(++counter).getUri().getPath());


                Glide.with(this)
                        .load(fList.get(++counter).getUri())
                        .into(image);
                download_uri=fList.get(counter).getUri();

                Log.d("StoryShowCaseActivity", "Hello onNext hello opps 009 aaa when swip"+" "
                        +fList.get(counter).getName()+" "+counter);
            } catch (Exception e) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
//                int imageHeight = options.outHeight;
//                int imageWidth = options.outWidth;
//                String imageType = options.outMimeType;
            }

            float fdatetime = fList.get(counter).lastModified();
            date.setText(DATEFORMAT.format(fdatetime));
//            image.setImageBitmap(b);
            spv.setStoriesCount(1);
            spv.setStoryDuration(mediaPreferences.getServiceTime());
            spv.setStoriesListener(StoryShowCaseActivity.this);
            spv.startStories();
//                    }
        } else {
            finish();
        }


    }


    //call when scrool privious
    public void getPreviousStatus() {
        Log.d("StoryShowCaseActivity", "Hello onNext hello opps 0010 back swap");

        if (counter > 0) {
            if(fList.get(counter).getName().endsWith(".mp4")){
                --counter;
                getPreviousStatus();
                return;
            }
            System.out.println("i m here next check NextClicked0003 hiii");
            image.setVisibility(View.VISIBLE);
            try {
//                b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());


                Glide.with(this)
                        .load(fList.get(--counter).getUri())
                        .into(image);
                download_uri=fList.get(counter).getUri();

            } catch (Exception e) {

            }

            float fdatetime = fList.get(counter).lastModified();
            date.setText(DATEFORMAT.format(fdatetime));
//            image.setImageBitmap(b);
            spv.setStoriesCount(1);
            spv.setStoryDuration(mediaPreferences.getServiceTime());
            spv.setStoriesListener(StoryShowCaseActivity.this);
            spv.startStories();
//                    }

            return;

        } else {

            image.setVisibility(View.VISIBLE);

            try {
                System.out.println("i m here next check NextClicked0003eleven hiii00003");

//                b = BitmapFactory.decodeFile(fList.get(--counter).getUri().getPath());

                Glide.with(this)
                        .load(fList.get(0).getUri())
                        .into(image);

            } catch (Exception e) {

            }
//            float fdatetime = mList.get(counter).getAbsoluteFile().lastModified();
//            String mytext = Float.toString(fdatetime);
//            date.setText(mytext);


            System.out.println("i m here next check NextClicked0003eleven hiii00004");
            float fdatetime = fList.get(counter).lastModified();
            date.setText(DATEFORMAT.format(fdatetime));
//            image.setImageBitmap(b);
            spv.setStoriesCount(1);
            spv.setStoryDuration(mediaPreferences.getServiceTime());
            spv.setStoriesListener(StoryShowCaseActivity.this);
            spv.startStories();
//                    }
        }
    }


    //for 11.
 /*   private void saveVideo(Uri myUri) {

        try {
            String FileName = System.currentTimeMillis() + ".jpg";
//            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/my_gallery/");
            File path = new File(AppUtils.getAppMainDir());

            if (path.exists()) {
                file = new File(path, "WhatsApp story M24" + "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                FileUtils.copyInputStreamToFile(inputStream, file);

            } else {

                path.mkdir();
                file = new File(path, "WhatsApp story M24" + "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                FileUtils.copyInputStreamToFile(inputStream, file);
            }

            Toast.makeText(StoryShowCaseActivity.this, getResources().getString(R.string.save_story), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("TAG", "saveVideo: " + e.getMessage());
        }
    }*/


    private void func() {

        MediaScannerConnection.scanFile(this, new String[]{getpathstr}, null, new MediaScannerConnection.OnScanCompletedListener() {

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) throws IOException {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        // String path = Environment.getExternalStorageDirectory().getPath()+ inImage;
//        return Uri.parse(path);

        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        tempDir.mkdir();
        File tempFile = File.createTempFile("testwhatsapp", ".jpg", tempDir);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        return Uri.fromFile(tempFile);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        spv.resume();
    }


    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private String savedpath() {
//        String FileName = System.currentTimeMillis() + ".mp4";
        File path = new File(Environment.getExternalStorageDirectory(), "/my_gallery/");
        if (path.exists()) {
            file = new File(path, "WhatsApp story" + "/");
//            getpathstr = file.getAbsolutePath();
            getpathstr = file.getAbsolutePath();
            file.getParentFile().mkdirs();
            funcnew();
        } else {
            path.mkdir();
            file = new File(path, "WhatsApp story" + "/");
            getpathstr = file.getAbsolutePath();
            System.out.println("my file saved path" + " " + getpathstr);
            funcnew();
        }


        return getpathstr;
    }

    private void funcnew() {

//        MediaScannerConnection.scanFile(this, new String[]{getpathstr}, null, new MediaScannerConnection.OnScanCompletedListener() {
//
//            @Override
//            public void onScanCompleted(String path, Uri uri) {
//                Log.i("ExternalStorage", "Scanned " + path + ":");
//                Log.i("ExternalStorage", "-> uri=" + uri);
//            }
//        });

        MediaScannerConnection.scanFile(this, new String[]{getpathstr},

                new String[]{"video/mp4/jpg"}, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        System.out.println("completed");
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(uri);
                        sendBroadcast(intent);
                    }

                });

    }

    public void shareImageStatus() {
        try {
            b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());
        } catch (Exception e) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(fList.get(++counter).getUri().getPath()), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
        }

        Uri uri = null;
        try {
            uri = getImageUri(StoryShowCaseActivity.this, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  shareImage(StoryShowCaseActivity.this, uri);
        AHandler.getInstance().showFullAds(StoryShowCaseActivity.this, false);*/
//                fam.close(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                b = BitmapFactory.decodeFile(fList.get(counter).getUri().getPath());
            } catch (Exception e) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(fList.get(++counter).getUri().getPath()), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                String imageType = options.outMimeType;
            }

            Uri uri = null;
            try {
                uri = getImageUri(StoryShowCaseActivity.this, b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(StoryShowCaseActivity.this, "No App found to handle action", Toast.LENGTH_SHORT).show();
            }
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(StoryShowCaseActivity.this, TutorialActivity.class);
                        startActivity(intent1);
                    }
                }, 800);

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(StoryShowCaseActivity.this, "Please Install WhatsApp", Toast.LENGTH_SHORT).show();
            }


        } else {


            try {
                b = BitmapFactory.decodeFile(mList.get(counter).getAbsolutePath());
            } catch (Exception e) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                b = BitmapFactory.decodeResource(getResources(), Integer.parseInt(mList.get(++counter).getAbsolutePath()), options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                String imageType = options.outMimeType;
            }

            Uri uri = null;
            try {
                uri = getImageUri(StoryShowCaseActivity.this, b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(StoryShowCaseActivity.this, "No App found to handle action", Toast.LENGTH_SHORT).show();
            }
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(StoryShowCaseActivity.this, TutorialActivity.class);
                        startActivity(intent1);
                    }
                }, 800);

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(StoryShowCaseActivity.this, "Please Install WhatsApp", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");

    private void saveImageStatus(Uri temp) {

        Log.d("StoryShowCaseActivity", "Hello saveImageStatus opps here " + " " + temp);
        if (temp != null) {
            if (temp.getPath().endsWith(".mp4")) {
                saveVideo(temp, ".mp4");
            } else {
                saveVideo(temp, ".jpg");

            }
//                        copyFileOrDirectory(statusDocumentFileList.get(imgSelectedPos).getUri().toString(),Utils.createAppDir(ImageDetailActivity.this));

        }


        System.out.println("temp = chk1111 inputStream :" + " " + " ");

//        try {
////           Uri uri= Uri.fromFile(new File(temp.toString()));
//            String FileName = System.currentTimeMillis() + ".jpg";
//
////            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.quantum.whatsappstatus" + File.separator + "/hello");
////            File path = new File(Environment.DIRECTORY_DCIM + File.separator + "MyApp Gallery");
//            File path = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
////            path.mkdirs();
//
//            System.out.println("temp =helllo test " + path);
//
//            if (path.exists()) {
//
//                System.out.println("temp = ck1" + file+ " "+" "+temp.toString());
//
//                file = new File(path, "WhatsApp story M24" + "/" + FileName);
//                file.mkdirs();
//
//                InputStream inputStream =this.getContentResolver().openInputStream(temp);
////
//                System.out.println("temp = chk1111" + inputStream+ " "+" "+temp.toString());
//                FileUtils.copyInputStreamToFile(inputStream, file);
//
//            } else {
//
//                System.out.println("temp = ck2" + path+" "+" "+myUri.toString());
//                path.mkdir();
//                file = new File(path, "WhatsApp story M24" + "/" + FileName);
//                InputStream inputStream = getContentResolver().openInputStream(temp);
//                FileUtils.copyInputStreamToFile(inputStream, file);
//
//            }
//
//            System.out.println("temp = ck3" + file);
//            Toast.makeText(this, "Save Image ", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.d("TAG", "saveImage: cathhhh" + e.getMessage());
//        }

    }

    private void saveVideo(Uri myUri, String fileExtension) {
        Log.d("StoryShowCaseActivity", "Hello saveVideo sghfjahsgkda" + " " +
                myUri);
        File file;
        try {
            String FileName = System.currentTimeMillis() + fileExtension;
            File path = new File(AppUtils.createAppDir(StoryShowCaseActivity.this));
            if (path.exists()) {
                file = new File(path, "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);

            } else {
                path.mkdir();
                file = new File(path, "/" + FileName);
                InputStream inputStream = getContentResolver().openInputStream(myUri);
                org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
            }
            Toast.makeText(StoryShowCaseActivity.this, "saved story", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("TAG", "saveVideo: " + e.getMessage());
        }

    }
}