package com.pds.socialmediahub.ui.status;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.STORAGE_SERVICE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pds.socialmediahub.R;
import com.pds.socialmediahub.activities.SettingActivity;
import com.pds.socialmediahub.helper.MediaPreferences;
import com.pds.socialmediahub.util.AppUtils;
import com.pds.socialmediahub.util.ItemOffsetView;
import com.pds.socialmediahub.util.SetClick;
import com.pds.socialmediahub.util.Utilities;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class WAStatusFragment extends Fragment implements StatusFragmentContracts.StatusView, SetClick, View.OnClickListener {

    Context context;
    private RecyclerView recyclerView;
    private WAStatusListAdapter adapter;
    private ItemOffsetView itemOffsetView;
    private Boolean selectAll = false;
    private LinearLayout rl_saved_options;
    private LinearLayout ll_select_all;
    private LinearLayout ll_share;
    private LinearLayout ll_download;
    private RelativeLayout top_tool;
    private TextView tvSelectAll;
    private WAStatusWith11ListAdapter waStatusWith11ListAdapter;
    private TextView allow_doc_permission;
    private RelativeLayout above_10_permission, main_layout;
    private LinearLayout ll_below_10_permission;
    private LinearLayout ll_small_wa_permission, ll_wa_big_container;
    public static final int WA_STATUS_FOLDER_REQ_CODE = 1001;
    public static final String WA_STATUS_FOLDER_REQ_RECEIVER = "SuceesReceiver";
    private MediaPreferences mediaPreferences;
    private ImageView iv_wa_doc_permission;
    List<DocumentFile> statusDocumentFileList = null;
    List<File> statusFileList = null;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private TextView no_data, below_10_permission;
    private Button open, settings;
    BottomNavigationView bottomNavigationView;
    private LinearLayout ll_noData;
    public static final int STATUS_DURATION = 2 * 60 * 1000; // 2 minute
    public static final long TIMER_Alarm_HOUR = 1000 * 60 * 60 * 12;//24 hour
    private StatusFragmentContracts.StatusPresenter mPresenter;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Boolean from = false;

    public WAStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();

        mediaPreferences = new MediaPreferences(context);
        View view = inflater.inflate(R.layout.fragment_allmedia_image, container, false);
        mPresenter = new StatusPresenter(this);
        init(view);

//        getAlarmNotificationTime(TIMER_Alarm_HOUR);

//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(customReceiver,
//                new IntentFilter(WA_STATUS_FOLDER_REQ_RECEIVER));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (mediaPreferences != null && !mediaPreferences.getDocumetFilePath().equals("NA")) {
                above_10_permission.setVisibility(View.GONE);
                ll_below_10_permission.setVisibility(View.VISIBLE);
                fetchFile();
            } else {
                above_10_permission.setVisibility(View.VISIBLE);
                ll_below_10_permission.setVisibility(View.GONE);

                allow_doc_permission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        requestFolderAccessPermission();
                    }
                });

            }

        } else {
            above_10_permission.setVisibility(View.GONE);
            ll_below_10_permission.setVisibility(View.VISIBLE);
            if (isStoragePermissionGrantedonly()) {
                load_imgList();
            }
            below_10_permission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isStoragePermissionGrantedonly()) {
                        requestStoragePermission();
                    } else {
                        //  mPresenter = new StatusPresenter(this);
                        load_imgList();
                    }
                }
            });


        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("list_refresh");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
        setHasOptionsMenu(true);
    }

    public void init(View view) {
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        top_tool = requireActivity().findViewById(R.id.top_tool);
        tvSelectAll = view.findViewById(R.id.tvSelectAll);
        rl_saved_options = view.findViewById(R.id.rl_saved_options);
        ll_select_all = view.findViewById(R.id.ll_select_all);
        ll_select_all.setOnClickListener(this);
        ll_share = view.findViewById(R.id.ll_share);
        ll_share.setOnClickListener(this);
        ll_download = view.findViewById(R.id.ll_save);
        ll_download.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.gv_allMediaImage);
        itemOffsetView = new ItemOffsetView(getActivity(), com.intuit.sdp.R.dimen._5sdp);
        recyclerView.addItemDecoration(itemOffsetView);
        main_layout = view.findViewById(R.id.main_layout);
        allow_doc_permission = view.findViewById(R.id.allow_doc_permission);
        no_data = view.findViewById(R.id.no_data);
        ll_noData = view.findViewById(R.id.ll_nodata);
        above_10_permission = view.findViewById(R.id.above_10_permission);
        ll_below_10_permission = view.findViewById(R.id.ll_below_10_permission);
        below_10_permission = view.findViewById(R.id.below_10_permission);
        ll_small_wa_permission = view.findViewById(R.id.ll_small_wa_permission);
        ll_wa_big_container = view.findViewById(R.id.ll_wa_big_container);
        iv_wa_doc_permission = view.findViewById(R.id.iv_wa_doc_permission);
        ll_noData.setVisibility(View.GONE);
        open = view.findViewById(R.id.open_gallery);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), MyDownloadsFragment.class));
            }
        });
        settings = view.findViewById(R.id.open_setting);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), SettingActivity.class));
            }
        });

//        setting_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), SettingActivity.class));
//            }
//        });


    }

    public void load_imgList() {
        ll_below_10_permission.setVisibility(View.GONE);
        mPresenter.start(context);

    }


    @Override
    public void loadStatus(ArrayList<File> status) {
        Log.d("TAG", "loadStatus: " + status.size());
        if (status.size() == 0) {
            ll_noData.setVisibility(View.VISIBLE);
        } else {
            ll_noData.setVisibility(View.GONE);

        }
        adapter = new WAStatusListAdapter(getActivity(), status, true, this);
        adapter.submitList(status);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        adapter.setCheckedListener(new WAStatusListAdapter.CounterSlection() {
            @Override
            public void selectItems(int itemSlectionCount) {
                setPageTitle(itemSlectionCount);
            }

        });
        Log.d("WAStatusFragment", "Hello loadStatus oopss" + " " + status.size());
    }

    @Override
    public void statusEroor() {
        ll_noData.setVisibility(View.VISIBLE);
    }


    public void requestFolderAccessPermission() {
        Intent intent;
        StorageManager sm = (StorageManager) getContext().getSystemService(STORAGE_SERVICE);
        String statusDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
        String str = "android.provider.extra.INITIAL_URI";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String scheme = ((Uri) intent.getParcelableExtra(str)).toString().replace("/root/", "/document/");
            String stringBuilder = scheme +
                    "%3A" +
                    statusDir;
            intent.putExtra(str, Uri.parse(stringBuilder));
            System.out.println("FindPathActivity.onActivityResult fileUri0022bbb: " + Uri.parse(stringBuilder));
            System.out.println("FindPathActivity.onActivityResult fileUri00helloooo123: " + Uri.parse(statusDir));
            startActivityForResult(intent, WA_STATUS_FOLDER_REQ_CODE);
        } else {
            intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.putExtra(str, Uri.parse(statusDir));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WAStatusFragment", "Hello onActivityResult hi test result 001" + " " + requestCode + " " + resultCode);
        if (requestCode == WA_STATUS_FOLDER_REQ_CODE && resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            mediaPreferences.setDocumetFilePath(data.getData().toString());
            getContext().getContentResolver().takePersistableUriPermission(treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            above_10_permission.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            fetchFile();

        }
    }

    public void fetchFile() {
        ArrayList<DocumentFile> documentFile = new ArrayList<>();
        String strmyuri = mediaPreferences.getDocumetFilePath();
        Uri myUri = Uri.parse(strmyuri);
        DocumentFile docFile = DocumentFile.fromTreeUri(context, myUri);
        System.out.println("temp = ck1" + docFile.canRead() + " " + docFile.canWrite() + " " + " " + docFile.toString());
        for (DocumentFile file : docFile.listFiles()) {
//            if (!file.getName().endsWith(".nomedia"))
            documentFile.add(file);

        }
        System.out.println("MainActivity2.fetchFile: fileSize: " + documentFile.size());
        waStatusWith11ListAdapter = new WAStatusWith11ListAdapter(getActivity(), documentFile, this);
        waStatusWith11ListAdapter.submitList(documentFile);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(waStatusWith11ListAdapter);
        waStatusWith11ListAdapter.setCheckedListener(new WAStatusWith11ListAdapter.CounterSlection() {
            @Override
            public void selectItems(int itemSlectionCount) {
                setPageTitle(itemSlectionCount);
            }

        });
    }

    private void setPageTitle(int itemSlectionCount) {
        if (actionMode != null) {
            actionMode.setTitle("Selected : " + itemSlectionCount);

        }
    }

//    private BroadcastReceiver customReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d("WAStatusFragment", "Hello onReceive hi test receiver 001");
//            String message = intent.getStringExtra(WA_STATUS_FOLDER_REQ_RECEIVER);
//
//            if (message.equals(WA_STATUS_FOLDER_REQ_RECEIVER)) {
//                fetchFile();
//            }
//        }
//    };


    @Override
    public void onResume() {
        super.onResume();

        if (mediaPreferences != null && mediaPreferences.getRefresh()) {
            mediaPreferences.setRefresh(false);
            try {

                load_imgList();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(customReceiver);
    }

    @Override
    public void onClick(@NonNull View view, int position) {
        getFilePathData();
        if (view.getId() == R.id.fl_download) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (statusDocumentFileList.get(position).getUri().getPath().endsWith(".mp4")) {
                    saveVideo(statusDocumentFileList.get(position).getUri(), ".mp4");
                } else {
                    saveVideo(statusDocumentFileList.get(position).getUri(), ".jpg");

                }
            } else {
                copyFileOrDirectory(statusFileList.get(position).getAbsolutePath(), AppUtils.createAppDir(requireActivity()));
            }

        }

    }

    @Override
    public void onLongClcik(@NonNull View view, int position) {

        if (adapter != null) {
            from = true;
            top_tool.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            rl_saved_options.setVisibility(View.VISIBLE);
            getFilePathData();
            actionModeCallback = new ActionModeCallback(this, R.menu.action_menu, true);
            actionMode = getActivity().startActionMode(actionModeCallback);
            setPageTitle(1);
        }
        if (waStatusWith11ListAdapter != null) {
            from = true;
            top_tool.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            rl_saved_options.setVisibility(View.VISIBLE);
            getFilePathData();
            actionModeCallback = new ActionModeCallback(this, R.menu.action_menu, false);
            actionMode = getActivity().startActionMode(actionModeCallback);
            setPageTitle(1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_all:
                if (from) {
                    if (!selectAll) {
                        selectAll = true;
                        adapter.selectAll();
                        tvSelectAll.setText("Unselect All");
                    } else {
                        selectAll = false;
                        adapter.unSelectAll();
                        tvSelectAll.setText("select All");
                    }
                } else {
                    if (!selectAll) {
                        selectAll = true;
                        waStatusWith11ListAdapter.selectAll();
                        tvSelectAll.setText("Unselect All");
                    } else {
                        selectAll = false;
                        waStatusWith11ListAdapter.unSelectAll();
                        tvSelectAll.setText("select All");
                    }

                }

                break;
            case R.id.ll_share:
                if (from) {
                    shareMultipleImage();
                } else {
                    shareMultipleImageFor11();
                }
                actionMode.finish();

                break;
            case R.id.ll_save:
                if (from) {
                    downloadMultipleImage();
                } else {
                    downloadMultipleImageFor11();
                }

                actionMode.finish();
                break;
        }
    }

   /* @Override
    public void onClick11(@NonNull View view, int position) {

    }

    @Override
    public void onLongClick11(@NonNull View view, int position) {

    }*/

    private static class ActionModeCallback implements ActionMode.Callback {

        WAStatusFragment waStatusFragment;
        CheckBox checkBox;
        boolean flag = false;
        final int menu_lauout;
        boolean from;

        public ActionModeCallback(WAStatusFragment waStatusFragment, int menu_lauout, boolean from) {
            this.waStatusFragment = waStatusFragment;
            flag = false;
            this.menu_lauout = menu_lauout;
            this.from = from;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(menu_lauout, menu);

            // checkBox = (CheckBox) menu.findItem(R.id.select_all).getActionView();
//            checkBox.setChecked(true);
           /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!flag) {
                        recordedFragment.selectAll(isChecked);
                    }
                    flag = false;
                }
            });*/
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // retrieve selected items and print them out

            return false;


        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection

            //  waStatusFragment.actionMode.finish();
            if (waStatusFragment.adapter != null) {
                waStatusFragment.adapter.removeAllSelected();
                waStatusFragment.bottomNavigationView.setVisibility(View.VISIBLE);
                waStatusFragment.rl_saved_options.setVisibility(View.GONE);
                waStatusFragment.top_tool.setVisibility(View.VISIBLE);
                waStatusFragment.selectAll = false;
                waStatusFragment.tvSelectAll.setText("select All");
            }
            if (waStatusFragment.waStatusWith11ListAdapter != null) {
                waStatusFragment.waStatusWith11ListAdapter.removeAllSelected();
                waStatusFragment.bottomNavigationView.setVisibility(View.VISIBLE);
                waStatusFragment.rl_saved_options.setVisibility(View.GONE);
                waStatusFragment.top_tool.setVisibility(View.VISIBLE);
                waStatusFragment.selectAll = false;
                waStatusFragment.tvSelectAll.setText("select All");
            }
            waStatusFragment.actionMode = null;

        }

    }

    private void downloadMultipleImageFor11() {
        /*List<DocumentFile> statusDocumentFileList = waStatusWith11ListAdapter.getList();*/
        if (statusDocumentFileList != null && statusDocumentFileList.size() > 0) {
            for (int i = 0; i < statusDocumentFileList.size(); i++) {
                if (waStatusWith11ListAdapter.getCheckStatus()[i]) {
                    if (statusDocumentFileList.get(i).getUri().getPath().endsWith(".mp4")) {
                        saveVideo(statusDocumentFileList.get(i).getUri(), ".mp4");
                    } else {
                        saveVideo(statusDocumentFileList.get(i).getUri(), ".jpg");

                    }
                }

            }

//                        copyFileOrDirectory(statusDocumentFileList.get(imgSelectedPos).getUri().toString(),Utils.createAppDir(ImageDetailActivity.this));

        }
    }

    private void downloadMultipleImage() {
        //List<File> videoList = adapter.getList();

        if (statusFileList != null && statusFileList.size() > 0) {
            for (int i = 0; i < statusFileList.size(); i++) {
                Log.d("TAG", "shareMultipleImage1: " + adapter.getCheckStatus()[i]);
                if (adapter.getCheckStatus()[i]) {
                    copyFileOrDirectory(statusFileList.get(i).getAbsolutePath(), AppUtils.createAppDir(requireActivity()));
                }

            }


        }
    }

    private void shareMultipleImage() {
        // List<File> list = adapter.getList();
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        Log.d("TAG", "shareMultipleImage: " + statusFileList.size());
        for (int i = 0; i < statusFileList.size(); i++) {
            Log.d("TAG", "shareMultipleImage1: " + adapter.getCheckStatus()[i]);
            if (adapter.getCheckStatus()[i]) {
                Uri uri = FileProvider.getUriForFile(
                        requireActivity(),
                        context.getPackageName() + ".provider",
                        new File(statusFileList.get(i).getPath())
                );
                uriArrayList.add(uri);
            }

        }
        Utilities.Companion.shareFileImage(requireActivity(), uriArrayList);
    }

    private void shareMultipleImageFor11() {
        //   List<DocumentFile> waList = waStatusWith11ListAdapter.getList();
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        Log.d("TAG", "shareMultipleImage: " + statusDocumentFileList.size());
        for (int i = 0; i < statusDocumentFileList.size(); i++) {

            if (waStatusWith11ListAdapter.getCheckStatus()[i]) {

                uriArrayList.add(statusDocumentFileList.get(i).getUri());
            }

        }
        Utilities.Companion.shareFileImage(requireActivity(), uriArrayList);
    }

//    public void shareFileImage(ArrayList<Uri> path) {
//
//        Log.d("TAG", "shareMutliple1: ");
//        Intent sendIntent = new Intent();
//
//        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        sendIntent.putExtra(Intent.EXTRA_STREAM, path);
//        sendIntent.setType("*/*");
//
//        Intent shareIntent = Intent.createChooser(sendIntent, null);
//        requireActivity().startActivity(shareIntent);
//
//    }


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

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        System.out.println("MY LOG CHECK 01");

        if (!destFile.exists()) {
            destFile.createNewFile();
            System.out.println("MY LOG CHECK 02 ggfahsdgfahj" + " " + destFile.getPath());

            //  mediaPreferences.setgallerycount(mediaPreferences.getgallerycount() + 1);

            Toast.makeText(requireActivity(), getResources().getString(R.string.save_image_toast), Toast.LENGTH_LONG).show();


        } else {
            System.out.println("MY LOG CHECK override");

            Toast.makeText(requireActivity(), "This Image is already saved ", Toast.LENGTH_LONG).show();


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
            File path = new File(AppUtils.createAppDir(requireActivity()));
            if (path.exists()) {
                file = new File(path, "/" + FileName);
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(myUri);
                org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);

            } else {
                path.mkdir();
                file = new File(path, "/" + FileName);
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(myUri);
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            Toast.makeText(requireActivity(), getResources().getString(R.string.saved_image_toast), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("TAG", "saveVideo: " + e.getMessage());
        }

    }

    private void getFilePathData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            statusDocumentFileList = waStatusWith11ListAdapter.getList();
            Log.d("TAG", "getFilePathData: " + statusDocumentFileList.size());
        } else {
            statusFileList = adapter.getList();
        }
    }

    public boolean isStoragePermissionGrantedonly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestStoragePermission() {

        ActivityCompat.requestPermissions(requireActivity()
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG", "onReceive called: ");
            load_imgList();


        }
    };


}