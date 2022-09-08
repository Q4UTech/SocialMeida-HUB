package com.pds.socialmediahub

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.AppUtils.Companion.openCustomTab
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pds.socialmediahub.activities.SettingActivity
import com.pds.socialmediahub.databinding.ActivityMainBinding
import com.pds.socialmediahub.ui.socialmediadownloader.services.ClipBoardService
import com.pds.socialmediahub.ui.status.MyDownloadsFragment
import engine.app.adshandler.AHandler
import engine.app.fcm.MapperUtils
import engine.app.inapp.InAppUpdateManager
import engine.app.listener.InAppUpdateListener
import engine.app.serviceprovider.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*

class MainActivity : AppCompatActivity(), InAppUpdateListener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var isSocialMediaClicked: Boolean = false
    private var navController: NavController? = null
    private lateinit var inAppUpdateManager: InAppUpdateManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        binding.openNavDrawer.setOnClickListener(this)
        binding.searchDrop.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        binding.navSetting.setOnClickListener(this)
        binding.ivDownloaded.setOnClickListener(this)
        binding.navRate.setOnClickListener(this)
        binding.navAboutUs.setOnClickListener(this)
        binding.navShareApp.setOnClickListener(this)
        binding.navMore.setOnClickListener(this)
        binding.navShareFeedback.setOnClickListener(this)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController!!)
        callingForMapper(this@MainActivity)
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
        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController?.navigate(R.id.navigation_home)
                    AHandler.getInstance().showFullAds(this@MainActivity, false)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController?.navigate(R.id.navigation_dashboard)
                    AHandler.getInstance().showFullAds(this@MainActivity, false)
                    true
                }
                R.id.navigation_notifications -> {
                    navController?.navigate(R.id.navigation_notifications)
                    AHandler.getInstance().showFullAds(this@MainActivity, false)
                    true
                }
            }
            false
        }
        findViewById<LinearLayout>(R.id.ads_layout).addView(
            AHandler.getInstance().getBannerHeader(this)
        )
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
            R.id.nav_aboutUs -> {

            }
            R.id.nav_share_app -> {
                Utils().shareUrl(this)
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
            binding.searchBoxEditText.hint = "Search"
            binding.llDownload.visibility = View.GONE
            binding.selectedSocial.setImageDrawable(getDrawable(R.drawable.ic_search))
            mypopupWindow.dismiss()
        }
        down_video.setOnClickListener {
            isSocialMediaClicked = true
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
                    .sendBroadcast(intent);
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
                    MapperUtils.DL_SEARCH_PAGE -> {
                        navController?.navigate(R.id.navigation_home)
                    }
                    MapperUtils.DL_DOWNLOAD_PAGE -> {
                        navController?.navigate(R.id.navigation_dashboard)
                    }
                    MapperUtils.DL_CHAT_PAGE -> {
                        navController?.navigate(R.id.navigation_notifications)
                    }
                    /* MapperUtils.DL_BLOCK_PAGE -> {
                         viewPager?.currentItem = 2
                     }
                     MapperUtils.DL_ADD_BLOCK_DIAL -> {
                         context.startActivity(Intent(context, BlockNumberActivity::class.java))
                     }
                     MapperUtils.DL_ADD_BLOCK_CONTACT -> {
                         context.startActivity(Intent(context, BlockContactActivity::class.java))
                     }

                     MapperUtils.DL_Permission_CDO -> {
                         checkPermissionCDO()
                     }*/
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
}

