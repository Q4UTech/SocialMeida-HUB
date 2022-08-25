package com.jatpack.socialmediahub.ui.status;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.STORAGE_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.helper.MediaPreferences;
import com.jatpack.socialmediahub.util.ItemOffsetView;
import com.jatpack.socialmediahub.util.SetClick;
import com.quantum.dashboard.ui.wastatus.WAStatusWith11ListAdapter;


import java.io.File;
import java.util.ArrayList;

public class WAStatusFragment extends Fragment implements StatusFragmentContracts.StatusView, SetClick {

    Context context;
    private RecyclerView recyclerView;
    private WAStatusListAdapter adapter;
    private ItemOffsetView itemOffsetView;
    private WAStatusWith11ListAdapter waStatusWith11ListAdapter;
    private TextView  allow_doc_permission;
    private RelativeLayout  above_10_permission,main_layout;
    private LinearLayout   ll_small_wa_permission, ll_wa_big_container;
    public static final int WA_STATUS_FOLDER_REQ_CODE = 1001;
    public static final String WA_STATUS_FOLDER_REQ_RECEIVER = "SuceesReceiver";
    private MediaPreferences mediaPreferences;
    private ImageView iv_wa_doc_permission;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private TextView no_data;
    public static final int STATUS_DURATION=2 * 60 * 1000; // 2 minute
    public static final long TIMER_Alarm_HOUR = 1000 * 60 * 60 * 12;//24 hour
    private StatusFragmentContracts.StatusPresenter mPresenter;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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
                main_layout.setVisibility(View.VISIBLE);
                fetchFile();
            } else {
                above_10_permission.setVisibility(View.VISIBLE);
                main_layout.setVisibility(View.GONE);

                allow_doc_permission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        requestFolderAccessPermission();
                    }
                });

            }

        } else {
            above_10_permission.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            mPresenter = new StatusPresenter(this);
            mPresenter.start(context);
        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public void init(View view) {
        recyclerView = view.findViewById(R.id.gv_allMediaImage);
        itemOffsetView=new ItemOffsetView(getActivity(), com.intuit.sdp.R.dimen._5sdp);
        recyclerView.addItemDecoration(itemOffsetView);
        main_layout = view.findViewById(R.id.main_layout);
        allow_doc_permission = view.findViewById(R.id.allow_doc_permission);
        no_data = view.findViewById(R.id.no_data);
        above_10_permission = view.findViewById(R.id.above_10_permission);
        ll_small_wa_permission = view.findViewById(R.id.ll_small_wa_permission);
        ll_wa_big_container = view.findViewById(R.id.ll_wa_big_container);
        iv_wa_doc_permission = view.findViewById(R.id.iv_wa_doc_permission);
        no_data.setVisibility(View.GONE);



//        setting_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), SettingActivity.class));
//            }
//        });



    }

    public void load_imgList() {
        mPresenter.start(context);
    }


    @Override
    public void loadStatus(ArrayList<File> status) {
        Log.d("TAG", "loadStatus: "+status.size());
        if(status.size()==0){
            no_data.setVisibility(View.VISIBLE);
        }else {
            no_data.setVisibility(View.GONE);

        }
        adapter = new WAStatusListAdapter(getActivity(), status,true,this);
        adapter.submitList(status);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Log.d("WAStatusFragment", "Hello loadStatus oopss" + " " + status.size());
    }

    @Override
    public void statusEroor() {
        no_data.setVisibility(View.VISIBLE);
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
        waStatusWith11ListAdapter = new WAStatusWith11ListAdapter(getActivity(), documentFile);
        waStatusWith11ListAdapter.submitList(documentFile);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(waStatusWith11ListAdapter);
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

    }

    @Override
    public void onLongClcik(@NonNull View view, int position) {
        actionModeCallback = new ActionModeCallback(this, R.menu.action_menu);
        actionMode = getActivity().startActionMode(actionModeCallback);
    }

    private static class ActionModeCallback implements ActionMode.Callback {

        WAStatusFragment waStatusFragment;
        CheckBox checkBox;
        boolean flag = false;
        final int menu_lauout;

        public ActionModeCallback(WAStatusFragment waStatusFragment, int menu_lauout) {
            this.waStatusFragment = waStatusFragment;
            flag = false;
            this.menu_lauout = menu_lauout;
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
            waStatusFragment.adapter.removeAllSelected();
            waStatusFragment.actionMode=null;

        }

    }


//    private void setStatusAlarm() {
//        Log.d("StatusAlarmReceiver", "Hello onReceive alarmmmm 002 aaa");
//        int i = 1;
//        Intent myIntent = new Intent(context, StatusAlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//        myIntent.setAction(Intent.ACTION_MAIN);
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//        if (alarmManager != null) {
////                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000,
////                        2 * 60 * 1000, pendingIntent);
//
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()
//                    + (i * 1000), STATUS_DURATION, pendingIntent);
//        }
//
//
//
//    }


//    private void getAlarmNotificationTime(long timing) {
//
//        int i = 60;
//        Log.d("MyAppAlarmReceiver", "Hello onReceive test notii 001 cc");
//        if (alarmManager == null && pendingIntent == null) {
//            Log.d("MyAppAlarmReceiver", "Hello onReceive test notii 001 dd");
//            Intent intent = new Intent(getActivity(), StatusAlarmReceiver.class);
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
//            alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
//                    + timing, timing, pendingIntent);
//        } else {
//
//
//        }
//    }


}