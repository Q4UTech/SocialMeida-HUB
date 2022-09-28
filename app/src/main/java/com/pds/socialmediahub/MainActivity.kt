package com.pds.socialmediahub

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.fragments.RootHomeFragment
import com.example.whatsdelete.fragments.WaTrandingStatus
import com.example.whatsdelete.utils.AppUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pds.socialmediahub.activities.BaseActivity
import com.pds.socialmediahub.activities.SettingActivity
import com.pds.socialmediahub.databinding.ActivityMainBinding
import com.pds.socialmediahub.helper.Pref
import com.pds.socialmediahub.service.SocialMediaHubService
import com.pds.socialmediahub.ui.directchat.DirectChatFragment
import com.pds.socialmediahub.ui.socialmediadownloader.services.ClipBoardService
import com.pds.socialmediahub.ui.status.MyDownloadsFragment
import com.pds.socialmediahub.ui.status.WAStatusFragment
import engine.app.adshandler.AHandler
import engine.app.fcm.MapperUtils
import engine.app.inapp.InAppUpdateManager
import engine.app.listener.InAppUpdateListener
import engine.app.serviceprovider.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_status.*


class MainActivity : BaseActivity(), InAppUpdateListener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var isSocialMediaClicked: Boolean = false
//    private var navController: NavController? = null
    private lateinit var inAppUpdateManager: InAppUpdateManager
    private var isFromHOmeCount:Int=0
    private var prevMenuItem: MenuItem? = null
    private var dialog: ProgressDialog? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                binding.viewpager?.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                binding.viewpager?.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                binding.viewpager?.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adsbanner.addView(AHandler.getInstance().getBannerHeader(this))
        val navView: BottomNavigationView = binding.navView

        binding.openNavDrawer.setOnClickListener(this)
        binding.searchDrop.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        binding.navSetting.setOnClickListener(this)
        binding.ivDownloaded.setOnClickListener(this)
        binding.navRate.setOnClickListener(this)
        binding.navTermCondition.setOnClickListener(this)
        binding.navPrivacy.setOnClickListener(this)
        binding.navShareApp.setOnClickListener(this)
        binding.headerView.appLogo.setOnClickListener(this)
        binding.navMore.setOnClickListener(this)
        binding.navShareFeedback.setOnClickListener(this)
//        navController = findNavController(R.id.nav_host_fragment_activity_main)
//        navView.setupWithNavController(navController!!)
        binding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                if (binding.searchBoxEditTextLayout != null && binding.searchBoxEditTextLayout.visibility === View.VISIBLE) {
                    AppUtils.hideSoftKeyboard(this@MainActivity)
                    binding.searchBoxEditText.setText("")
                }
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
//        navView.setOnItemSelectedListener { menuItem ->
//
//            println("MainActivity.onCreate hjgjhgjhgjj"+" "+menuItem.title)
//            when (menuItem.itemId) {
//                R.id.navigation_home -> {
//                    navController?.popBackStack()
//                    navController?.navigate(R.id.navigation_home)
//                    AHandler.getInstance().showFullAds(this@MainActivity, false)
//                    true
//                }
//                R.id.navigation_dashboard -> {
//                    navController?.popBackStack()
//                    navController?.navigate(R.id.navigation_dashboard)
//                    AHandler.getInstance().showFullAds(this@MainActivity, false)
//                    true
//                }
//                R.id.navigation_notifications -> {
//                    navController?.popBackStack()
//                    navController?.navigate(R.id.navigation_notifications)
//                    AHandler.getInstance().showFullAds(this@MainActivity, false)
//                    true
//                }
//            }
//            false
//        }



      val  mPagerAdapter = ViewPagerAdapter(supportFragmentManager, 3)
       binding.viewpager?.setAdapter(mPagerAdapter)


//        navView.setOnNavigationItemSelectedListener(
//            BottomNavigationView.OnNavigationItemSelectedListener { item ->
//                try {
//                    Log.d("MainActivityLauncher", "Hello onNavigationItemSelected nav ")
//                    when (item.itemId) {
//                        R.id.navigation_home -> {
//                            binding.viewpager?.currentItem = 0
//                            return@OnNavigationItemSelectedListener true
//                        }
//                        R.id.navigation_dashboard -> {
//                            binding.viewpager?.currentItem = 1
//                            return@OnNavigationItemSelectedListener true
//                        }
//                        R.id.navigation_notifications -> {
//                            binding.viewpager?.currentItem = 2
//                            return@OnNavigationItemSelectedListener true
//                        }
//                    }
//                    //  AHandler.getInstance().showFullAds(MainActivityLauncher.this, false);
//                } catch (e: Exception) {
//                }
//                false
//            })





//                navView.setupWithNavController(binding.viewpager!!)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.d("MainActivityLauncher", "Hello onPageSelected  $position")
                AHandler.getInstance().showFullAds(this@MainActivity, false)

                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    binding.navView.menu.getItem(0).isChecked = false
                }
                binding.navView.menu.getItem(position).isChecked = true
                prevMenuItem = binding.navView.menu.getItem(position)
                Log.d("MainActivityLauncher", "Test onPageSelected...")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })



        inAppUpdateManager = InAppUpdateManager(this)
        inAppUpdateManager.checkForAppUpdate(this)


        binding.searchBoxEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Your piece of code on keyboard search click

                if (!isSocialMediaClicked && binding.searchBoxEditText.text.toString() != "") {

                    openWebView(
                        "https://www.google.com/search?q=" + binding.searchBoxEditText.text.toString()
                            .trim()
                    )
                }
                binding.searchBoxEditText.setText("")
                return@OnEditorActionListener true
            }
            false
        })


        callingForMapper(this@MainActivity)


        if (Pref(this).getAutoNotificationEnable()){
            if (!isMyServiceRunning(SocialMediaHubService::class.java)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(
                        Intent(
                            this,
                            SocialMediaHubService::class.java
                        )
                    )

                } else {
                    startService(
                        Intent(
                            this,
                            SocialMediaHubService::class.java
                        )
                    )

                }

            }

        }
    }


    override fun onResume() {
        super.onResume()
        inAppUpdateManager.checkNewAppVersionState()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.open_nav_drawer -> {
                if (isDrawerOpen()) {
                    closeMenuDrawer()
                } else {
                    openMenuDrawer()
                }
            }

            R.id.search_drop -> {
                setPopUpWindow(v)
            }
            R.id.nav_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                closeMenuDrawer()
                AHandler.getInstance().showFullAds(this, false)
            }
            R.id.iv_downloaded -> {

                startActivity(Intent(this, MyDownloadsFragment::class.java))
                closeMenuDrawer()
            }

            R.id.ll_download -> {
                val paste = searchBoxEditText?.text.toString()
                if (paste != null && paste.equals("")) {
                    Toast.makeText(this, "Please paste valid URL", Toast.LENGTH_LONG).show()
                    return
                }
                if (isSocialMediaClicked) {

                    if (paste != null && paste.contains("https://www.instagram.com") || paste.contains(
                            "https://www.facebook.com"
                        ) ||
                        paste.contains("https://m.facebook.com") ||
                        paste.contains("https://like.video") || paste.contains("https://l.likee.video") || paste.contains(
                            "https://share.like.video"
                        ) || paste.contains("https://mobile.like-video")
                        || paste.contains("https://like-video") ||
                        paste.contains("tumblr.com/post") || paste.contains("tiktok.com")
                    ) {
                        Log.d("BackgroundRService", "Test onPrimaryClipChanged...$paste  ")

                        val intentFilter = IntentFilter()
                        intentFilter.addAction("download_complete")
                        LocalBroadcastManager.getInstance(this)
                            .registerReceiver(broadcastReceiver, intentFilter)

                        dialog =  ProgressDialog(this)
                        dialog?.setMessage("Downloading...")
                        dialog?.setCancelable(false)
                        dialog?.show()

                        startDownloadingLink(
                            applicationContext,
                            paste,
                            false
                        )
                    }

                }
            }
            R.id.nav_rate -> {
                Utils().rateUs(this)
            }
            R.id.nav_privacy -> {

                AHandler.getInstance().onStartPrivacyPolicy(this)
            }
            R.id.nav_term_condition -> {

                AHandler.getInstance().onStartTermCondistion(this)
            }
            R.id.nav_share_app -> {
                Utils().shareUrl(this)
            }

            R.id.app_logo -> {
                AHandler.getInstance().getDefaultServerAdsData(this)
            }
            R.id.nav_more -> {
                Utils().moreApps(this)
            }
            R.id.nav_shareFeedback -> {
                Utils().sendFeedback(this)
            }
        }
    }


    private fun isDrawerOpen(): Boolean {
        return binding.drawerLayout != null && binding.drawerLayout.isDrawerOpen(GravityCompat.START)
    }

    private fun closeMenuDrawer() {
        if (binding.drawerLayout != null && binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun openMenuDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setPopUpWindow(v: View) {
        val mypopupWindow: PopupWindow
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.social_media_pop_up_menu, null)
        val google_click = view.findViewById<ImageView>(R.id.google_click)
        val down_video = view.findViewById<ImageView>(R.id.down_video)

        mypopupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        mypopupWindow.showAsDropDown(v, -10, -90)
        google_click.setOnClickListener {
            isSocialMediaClicked = false
            binding.searchImageGoogle.visibility=View.VISIBLE
            binding.searchBoxEditText.setText("")
            binding.searchBoxEditText.hint = "Search"
            binding.llDownload.visibility = View.GONE
            binding.selectedSocial.setImageDrawable(getDrawable(R.drawable.ic_search))
            mypopupWindow.dismiss()
        }
        down_video.setOnClickListener {
            isSocialMediaClicked = true
            binding.searchImageGoogle.visibility=View.GONE
            binding.searchBoxEditText.setText("")
            binding.searchBoxEditText.hint = "Paste URL/Link here"
            binding.llDownload.visibility = View.VISIBLE
            binding.selectedSocial.setImageDrawable(getDrawable(R.drawable.ic_video_selected))
            mypopupWindow.dismiss()
        }

    }


    fun startDownloadingLink(context: Context, copyData: String?, goButtonClick: Boolean) {
        val downloadService = Intent(context, ClipBoardService::class.java)
        downloadService.putExtra(Constants.PASTE_MEDIA_URL, copyData)
            .putExtra(Constants.GO_BUTTON_CLICK, goButtonClick)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(downloadService);
//        } else {
        context.startService(downloadService)
        binding.searchBoxEditText.setText("")
        // }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            102 -> if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showADialog("Must require Storage Permission in order for app to work.",
                    "Allow", "Deny", object : ADialogClicked {
                        override fun onPositiveClicked(dialog: DialogInterface?) {
                            if (dialog != null) {
                                dialog.dismiss()
                                requestStoragePermission()
                            }
                        }

                        override fun onNegativeClicked(dialog: DialogInterface?) {
                            dialog?.dismiss()
                        }
                    })
            } else {
                val intent = Intent("list_refresh");

                LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(intent)
            }
        }
    }

    interface ADialogClicked {
        fun onPositiveClicked(dialog: DialogInterface?)
        fun onNegativeClicked(dialog: DialogInterface?)
    }

    fun showADialog(
        message: String,
        buttonPositive: String?,
        buttonNegative: String?,
        l: ADialogClicked
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setMessage("" + message)
        builder.setCancelable(true)
        builder.setPositiveButton(
            buttonPositive
        ) { dialog: DialogInterface?, id: Int ->
            /*if (dialog != null) {
            dialog.dismiss();
        }*/l.onPositiveClicked(dialog)
        }
        builder.setNegativeButton(
            buttonNegative
        ) { dialog: DialogInterface?, id: Int ->
            /*if (dialog != null) {
                 dialog.dismiss();
             }*/l.onNegativeClicked(dialog)
        }
        builder.setOnCancelListener { dialog: DialogInterface? -> }
        val dialog = builder.create()

        /* try {
            dialog.setCanceledOnTouchOutside(false);
            if (!isFinishing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 102
        )
    }


    private fun callingForMapper(context: Activity) {
        val intent = context.intent
        val value = intent.getStringExtra(MapperUtils.keyValue)

        try {
            if (value != null) {
                when (value) {
                    MapperUtils.MAPPER_SOCIAL_MEDIA -> {
//                        navController?.navigate(R.id.navigation_home)
                    }
                    MapperUtils.MAPPER_WA_STATUS -> {
//                        navController?.navigate(R.id.navigation_dashboard)
                    }
                    MapperUtils.MAPPER_WA_DIRECT_CHAT -> {
//                        navController?.navigate(R.id.navigation_notifications)
                    }
                     MapperUtils.MAPPER_GALLERY -> {
                         startActivity(Intent(this, MyDownloadsFragment::class.java))
                     }
                     MapperUtils.MAPPER_SETTING -> {
                         startActivity(Intent(this, SettingActivity::class.java))                     }

                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpdateAvailable() {
        println("InAppUpdateManager MainActivity.onUpdateAvailable ")
    }

    override fun onUpdateNotAvailable() {
        println("InAppUpdateManager MainActivity.onUpdateNotAvailable ")
        AHandler.getInstance().v2CallonAppLaunch(this)
    }


    //    public void updateUI(String url) {
    //
    //        if (!url.trim().isEmpty()) {
    //
    //            if (Utils.isValidUrl(url.trim())) {
    //                if (!url.trim().contains("https://")) {
    //                    openWebView("https://" + url.trim());
    //                } else {
    //                    openWebView(url.trim());
    //                }
    //            } else {
    //                openWebView("https://www.google.com/search?q=" + url);
    //            }
    //        }
    //
    //
    //    }
    fun openWebView(url: String?) {
        // img_home.setVisibility(View.VISIBLE);
        AppUtils.openCustomTab(this, Uri.parse(url),"#34A853")

//        AHandler.getInstance().showFullAds(CustomBrowserActivity.this,false);
    }


    override fun onBackPressed() {
        if (isDrawerOpen()) {
            closeMenuDrawer()
            return
        }



        if (binding.viewpager.currentItem!=0){
            binding.viewpager.currentItem=0
            return
        }

        super.onBackPressed()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


//        if (requestCode == WA_STATUS_FOLDER_REQ_CODE && resultCode == RESULT_OK) {
//            Uri treeUri = data.getData();
//            mediaPreferences.setDocumetFilePath(data.getData().toString());
//            getContentResolver().takePersistableUriPermission(treeUri,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//                    Intent intent = new Intent(WA_STATUS_FOLDER_REQ_RECEIVER);
//        // You can also include some extra data.
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//
//        }
        if (requestCode == InAppUpdateManager.REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                // If the update is cancelled or fails,
                // you can request to start the update again.
                inAppUpdateManager.unregisterInstallStateUpdListener()
                onUpdateNotAvailable()
                println("InAppUpdateManager MainActivityV2.onActivityResult RESULT_CANCELED $resultCode")
            } else {
                println("InAppUpdateManager MainActivityV2.onActivityResult RESULT_OK $resultCode")
            }
        }
    }


    override fun onDestroy() {
        AHandler.getInstance().v2CallOnExitPrompt(this)

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy()

    }


    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("download_complete")){
                if (dialog!=null && dialog?.isShowing!!) {
                    dialog?.dismiss();
                }
                Log.d("TAG", "onReceive called: fasmb kdhjgksjhkj")
            }

        }
    }


    fun openAppNotification(isChecked : Boolean){
        if (isChecked) {
            Log.d("TAG", "onCreate: " + isChecked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(
                    Intent(
                        this,
                        SocialMediaHubService::class.java
                    )
                )

            } else {
                startService(
                    Intent(
                        this,
                        SocialMediaHubService::class.java
                    )
                )

            }
        } else {
            Log.d("TAG", "onCreate1: " + isChecked)
            stopService(Intent(this, SocialMediaHubService::class.java))
        }
    }


    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }



    internal class ViewPagerAdapter(manager: FragmentManager?, _pageCount: Int) :
        FragmentPagerAdapter(manager!!) {
        private var pageCount = 0
        private val titles = arrayOf("Home", "Status","Direct Chat")
        private var home: RootHomeFragment? = null
        private var status: WAStatusFragment? = null
        private var directchat: DirectChatFragment? = null
        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                //load fragment one
                return home!!
            } else if (position == 1) {
                return status!!
            } else if (position == 2) {
                return directchat!!
            }
            return null!!
        }

        override fun getCount(): Int {
            return pageCount
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        init {
            pageCount = _pageCount
            home = RootHomeFragment()
            status = WAStatusFragment()
            directchat= DirectChatFragment()
        }
    }


}

