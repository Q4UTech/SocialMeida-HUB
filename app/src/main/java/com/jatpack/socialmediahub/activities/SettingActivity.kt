package com.jatpack.socialmediahub.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.BottomStatusAdapter
import com.jatpack.socialmediahub.adapter.NotificationAdapter
import com.jatpack.socialmediahub.helper.Pref
import com.jatpack.socialmediahub.model.BottomList
import com.jatpack.socialmediahub.model.NotificatioListItem
import com.jatpack.socialmediahub.service.SocialMediaHubService
import com.jatpack.socialmediahub.util.SetClick


class SettingActivity : AppCompatActivity(), SetClick, View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var notificationList = ArrayList<NotificatioListItem>()
    private var bottomRecyclerView: RecyclerView? = null
    private var bottomStatusAdapter: BottomStatusAdapter? = null
    private var bottomAppList = ArrayList<BottomList>()
    private var serviceToggle: ToggleButton? = null
    private var cvStatus: CardView? = null
    private var cvChat: CardView? = null
    private var cvDownloader: CardView? = null
    private var ivStatus: ImageView? = null
    private var ivChat: ImageView? = null
    private var ivDownload: ImageView? = null
    private var pref: Pref? = null
    private var position: Int? = null
    private var rlSearch: RelativeLayout? = null
    private var rlCamera: RelativeLayout? = null
    private var rlWhatsApp: RelativeLayout? = null
    private var rlMessage: RelativeLayout? = null
    private var rlMessenger: RelativeLayout? = null
    private var rlFacebook: RelativeLayout? = null
    private var cbSearch: AppCompatCheckBox? = null
    private var cbCamera: AppCompatCheckBox? = null
    private var cbWhatsApp: AppCompatCheckBox? = null
    private var cbMessage: AppCompatCheckBox? = null
    private var cbMessenger: AppCompatCheckBox? = null
    private var cbFacebook: AppCompatCheckBox? = null
    private var llSearch: LinearLayout? = null
    private var llCamera: LinearLayout? = null
    private var llWhatsApp: LinearLayout? = null
    private var llMessege: LinearLayout? = null
    private var llMessenger: LinearLayout? = null
    private var llFacebook: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        serviceToggle = findViewById(R.id.serviceToggle)
        llSearch = findViewById(R.id.ll_search)
        llCamera = findViewById(R.id.ll_camera)
        llWhatsApp = findViewById(R.id.ll_whatsapp)
        llMessege = findViewById(R.id.ll_msg)
        llMessenger = findViewById(R.id.ll_messanger)
        llFacebook = findViewById(R.id.ll_facebook)
        cbSearch = findViewById(R.id.cb_search)
        cbCamera = findViewById(R.id.cb_camera)
        cbWhatsApp = findViewById(R.id.cb_whats_app)
        cbMessage = findViewById(R.id.cb_msg)
        cbMessenger = findViewById(R.id.cb_messanger)
        cbFacebook = findViewById(R.id.cb_facebook)
        cbSearch = findViewById(R.id.cb_search)
        rlSearch = findViewById(R.id.rl_search)
        rlSearch?.setOnClickListener(this)
        rlCamera = findViewById(R.id.rl_camera)
        rlCamera?.setOnClickListener(this)
        rlWhatsApp = findViewById(R.id.rl_whats_app)
        rlWhatsApp?.setOnClickListener(this)
        rlMessage = findViewById(R.id.rl_msg)
        rlMessage?.setOnClickListener(this)
        rlMessenger = findViewById(R.id.rl_messanger)
        rlMessenger?.setOnClickListener(this)
        rlFacebook = findViewById(R.id.rl_facebook)
        rlFacebook?.setOnClickListener(this)
        cvChat = findViewById(R.id.cv_direct_chat)
        cvChat?.setOnClickListener(this)
        cvDownloader = findViewById(R.id.cv_video_downloader)
        cvDownloader?.setOnClickListener(this)
        cvStatus = findViewById(R.id.cv_whats_staus)
        cvStatus?.setOnClickListener(this)
        ivStatus = findViewById(R.id.img_check1)
        ivDownload = findViewById(R.id.img_check2)
        ivChat = findViewById(R.id.img_check3)
        pref = Pref(this)
        position = pref?.getPostion()
        showData()
        notificationList.add(NotificatioListItem(R.drawable.ic_search_option, "Search", true))
        notificationList.add(NotificatioListItem(R.drawable.ic_camera_option, "Camera", true))
        if (isAppInstalled("com.whatsapp")) {
            /*  notificationList.add(
                  NotificatioListItem(
                      R.drawable.ic_whats_app_option,
                      "Whats App",
                      true
                  )
              )*/
            rlWhatsApp?.visibility = View.VISIBLE
        } else {
            rlWhatsApp?.visibility = View.GONE
        }

        notificationList.add(NotificatioListItem(R.drawable.ic_msg_option, "Message", true))
        if (isAppInstalled("com.facebook.orca")) {
            /* notificationList.add(
                 NotificatioListItem(
                     R.drawable.ic_messanger_option,
                     "Messanger",
                     false
                 )
             )*/
            rlFacebook?.visibility = View.VISIBLE
        } else {
            rlFacebook?.visibility = View.GONE
        }
        if (isAppInstalled("com.facebook.katana")) {
            //  notificationList.add(NotificatioListItem(R.drawable.ic_facebook, "Facebook", false))
            rlFacebook?.visibility = View.VISIBLE
        } else {
            rlFacebook?.visibility = View.GONE
        }
        recyclerView = findViewById(R.id.rec)
        bottomAppList.add(BottomList(R.drawable.ic_wa_status_icon, "WA Status"))
        bottomAppList.add(BottomList(R.drawable.ic_video_downloader_icon, "Video Downloader"))
        bottomAppList.add(BottomList(R.drawable.ic_direct_chat_icon, "Direct Chat"))
        bottomRecyclerView = findViewById(R.id.horizontal_rec)

        showBottomData()
        serviceToggle?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

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
        })
        /*serviceToggle?.setOnCheckedChangeListener { _, isChecked ->

        }*/
    }

    private fun showBottomData() {
        /* bottomStatusAdapter = BottomStatusAdapter(this, bottomAppList, this)
         val layoutManager = LinearLayoutManager(this)
         layoutManager.orientation = LinearLayoutManager.HORIZONTAL
         bottomRecyclerView?.layoutManager = layoutManager
         bottomRecyclerView?.adapter = bottomStatusAdapter*/
        if (position != null) {
            when (position) {
                0 -> {
                    ivStatus?.visibility = View.VISIBLE
                    ivDownload?.visibility = View.GONE
                    ivChat?.visibility = View.GONE
                }
                1 -> {
                    ivStatus?.visibility = View.GONE
                    ivDownload?.visibility = View.VISIBLE
                    ivChat?.visibility = View.GONE
                }
                2 -> {
                    ivStatus?.visibility = View.GONE
                    ivDownload?.visibility = View.GONE
                    ivChat?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showData() {
        /*  notificationAdapter = NotificationAdapter(this, notificationList, this)
          val layoutManager = LinearLayoutManager(this)
          recyclerView?.layoutManager = layoutManager
          recyclerView?.adapter = notificationAdapter*/
        pref?.setSearchPref(true)
        pref?.setCameraPref(true)
        pref?.setWhatsAppPref(true)
        pref?.setMessagePref(true)

        if (pref?.getSearchPref() == true) {
            cbSearch?.isChecked = true
        }
        if (pref?.getCameraPref() == true) {
            cbCamera?.isChecked = true
        }
        if (pref?.getWhatsAppPref() == true) {
            cbWhatsApp?.isChecked = true
        }
        if (pref?.getMessagePref() == true) {
            cbMessage?.isChecked = true
        }
    }

    override fun onClick(view: View, position: Int) {
        if (view.id == R.id.rl) {
            findViewById<ImageView>(R.id.img_check).visibility = View.VISIBLE
        }
    }

    override fun onLongClcik(view: View, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cv_whats_staus -> {
                pref?.setPositon(0)
                ivStatus?.visibility = View.VISIBLE
                ivDownload?.visibility = View.GONE
                ivChat?.visibility = View.GONE
            }
            R.id.cv_video_downloader -> {
                pref?.setPositon(1)
                ivStatus?.visibility = View.GONE
                ivDownload?.visibility = View.VISIBLE
                ivChat?.visibility = View.GONE
            }
            R.id.cv_direct_chat -> {
                pref?.setPositon(2)
                ivStatus?.visibility = View.GONE
                ivDownload?.visibility = View.GONE
                ivChat?.visibility = View.VISIBLE
            }
            R.id.rl_search -> {
                if (pref?.getSearchPref() == true) {
                    llSearch?.visibility = View.GONE
                    pref?.setSearchPref(false)
                    cbSearch?.isChecked = true
                } else {
                    pref?.setSearchPref(true)
                    llSearch?.visibility = View.VISIBLE
                    cbSearch?.isChecked = false
                }
            }
            R.id.rl_camera -> {
                if (pref?.getCameraPref() == true) {
                    llCamera?.visibility = View.GONE
                    pref?.setSearchPref(false)
                    cbCamera?.isChecked = true
                } else {
                    pref?.setCameraPref(true)
                    llCamera?.visibility = View.VISIBLE
                    cbCamera?.isChecked = false
                }
            }
            R.id.rl_whats_app -> {
                if (pref?.getWhatsAppPref() == true) {
                    llWhatsApp?.visibility = View.GONE
                    pref?.setWhatsAppPref(false)
                    cbCamera?.isChecked = true
                } else {
                    pref?.setCameraPref(true)
                    llWhatsApp?.visibility = View.VISIBLE
                    cbCamera?.isChecked = false
                }
            }
            R.id.rl_msg -> {
                if (pref?.getMessagePref() == true) {
                    llMessege?.visibility = View.GONE
                    pref?.setWhatsAppPref(false)
                    cbCamera?.isChecked = true
                } else {
                    pref?.setMessagePref(true)
                    llMessege?.visibility = View.VISIBLE
                    cbCamera?.isChecked = false
                }
            }
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (ignored: PackageManager.NameNotFoundException) {
            false
        }
    }
}