package com.pds.socialmediahub.activities

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
import androidx.recyclerview.widget.RecyclerView
import com.pds.socialmediahub.R
import com.pds.socialmediahub.adapter.BottomStatusAdapter
import com.pds.socialmediahub.adapter.NotificationAdapter
import com.pds.socialmediahub.helper.Pref
import com.pds.socialmediahub.model.BottomList
import com.pds.socialmediahub.model.NotificatioListItem
import com.pds.socialmediahub.service.SocialMediaHubService
import com.pds.socialmediahub.util.SetClick
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity(), SetClick, View.OnClickListener {
    val SEARCH_INDEX = 1
    val CAMERA_INDEX = 2
    val WHATS_APP_INDEX = 3
    val MESSAGE_INDEX = 4
    val MESSENGER_INDEX = 5
    val FACEBOOK_INDEX = 6
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
    private var notiDownloader: LinearLayout? = null
    private var notiChat: LinearLayout? = null
    private var notiStatus: LinearLayout? = null
    private var countList = ArrayList<Int>()

    var ivBack: ImageView? = null
    var save: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.hide()
        pref = Pref(this)
        ivBack = findViewById(R.id.ivBack)
        ivBack?.setOnClickListener {
            finish()
        }
        save = findViewById(R.id.tvSave)
        save?.setOnClickListener {

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
            Toast.makeText(this, "Setting Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
        serviceToggle = findViewById(R.id.serviceToggle)
        llSearch = findViewById(R.id.ll_search)
        llCamera = findViewById(R.id.ll_camera)
        llWhatsApp = findViewById(R.id.ll_whatsapp)
        llMessege = findViewById(R.id.ll_msg)
        llMessenger = findViewById(R.id.ll_messanger)
        llFacebook = findViewById(R.id.ll_facebook)
        notiDownloader = findViewById(R.id.ll_video_downloader)
        notiChat = findViewById(R.id.ll_direct_chat)
        notiStatus = findViewById(R.id.ll_status)
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

        position = pref?.getPostion()
        showData()
        notificationList.add(NotificatioListItem(R.drawable.ic_search_option, "Search", true))
        notificationList.add(NotificatioListItem(R.drawable.ic_camera_option, "Camera", true))
        if (isAppInstalled("com.whatsapp")) {

            rlWhatsApp?.visibility = View.VISIBLE
        } else {
            rlWhatsApp?.visibility = View.GONE
        }

        notificationList.add(NotificatioListItem(R.drawable.ic_msg_option, "Message", true))
        if (isAppInstalled("com.facebook.orca")) {

            rlMessenger?.visibility = View.VISIBLE
        } else {
            rlMessenger?.visibility = View.GONE
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
        if (pref != null) {
            pref?.getAutoNotificationEnable()?.let { serviceToggle?.setChecked(it) }
        }
        serviceToggle?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (pref != null) {
                pref?.setAutoNotificationEnable(isChecked)

                pref?.getAutoNotificationEnable()?.let { serviceToggle?.setChecked(it) }
            }
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
                    notiStatus?.visibility = View.VISIBLE
                    notiDownloader?.visibility = View.GONE
                    notiChat?.visibility = View.GONE
                    ivDownload?.visibility = View.GONE
                    ivChat?.visibility = View.GONE
                    pref?.setBottomPosition(0)
                }
                1 -> {
                    ivStatus?.visibility = View.GONE
                    ivDownload?.visibility = View.VISIBLE
                    notiStatus?.visibility = View.GONE
                    notiDownloader?.visibility = View.VISIBLE
                    notiChat?.visibility = View.GONE
                    ivChat?.visibility = View.GONE
                    pref?.setBottomPosition(1)
                }
                2 -> {
                    ivStatus?.visibility = View.GONE
                    ivDownload?.visibility = View.GONE
                    ivChat?.visibility = View.VISIBLE
                    notiStatus?.visibility = View.GONE
                    notiDownloader?.visibility = View.GONE
                    notiChat?.visibility = View.VISIBLE
                    pref?.setBottomPosition(2)
                }
            }
        }
    }

    private fun showData() {
        /*  notificationAdapter = NotificationAdapter(this, notificationList, this)
          val layoutManager = LinearLayoutManager(this)
          recyclerView?.layoutManager = layoutManager
          recyclerView?.adapter = notificationAdapter*/
        /* pref?.setSearchPref(true)
         pref?.setCameraPref(true)
         pref?.setWhatsAppPref(true)
         pref?.setMessagePref(true)*/

        if (pref?.getSearchPref() == true) {
            cbSearch?.isChecked = true
            llSearch?.visibility = View.VISIBLE
            if (!checkIndex(SEARCH_INDEX)) {
                countList.add(SEARCH_INDEX)
            }
        }
        if (pref?.getCameraPref() == true) {
            cbCamera?.isChecked = true
            llCamera?.visibility = View.VISIBLE
            if (!checkIndex(CAMERA_INDEX)) {
                countList.add(CAMERA_INDEX)
            }
        }
        if (pref?.getWhatsAppPref() == true) {
            cbWhatsApp?.isChecked = true
            llWhatsApp?.visibility = View.VISIBLE
            if (!checkIndex(WHATS_APP_INDEX)) {
                countList.add(WHATS_APP_INDEX)
            }
        }
        if (pref?.getMessagePref() == true) {
            cbMessage?.isChecked = true
            llMessege?.visibility = View.VISIBLE
            if (!checkIndex(MESSAGE_INDEX)) {
                countList.add(MESSAGE_INDEX)
            }
        }
        if (pref?.getMessengerPref() == true) {
            cbMessenger?.isChecked = true
            llMessenger?.visibility = View.VISIBLE
            if (!checkIndex(MESSENGER_INDEX)) {
                countList.add(MESSENGER_INDEX)
            }
        }

        if (pref?.getFacebookPref() == true) {
            cbFacebook?.isChecked = true
            llFacebook?.visibility = View.VISIBLE
            if (!checkIndex(FACEBOOK_INDEX)) {
                countList.add(FACEBOOK_INDEX)
            }
        }
        Log.d("TAG", "showData: " + countList.size)
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
                notiStatus?.visibility = View.VISIBLE
                notiDownloader?.visibility = View.GONE
                notiChat?.visibility = View.GONE
            }
            R.id.cv_video_downloader -> {
                pref?.setPositon(1)
                ivStatus?.visibility = View.GONE
                ivDownload?.visibility = View.VISIBLE
                ivChat?.visibility = View.GONE
                notiStatus?.visibility = View.GONE
                notiDownloader?.visibility = View.VISIBLE
                notiChat?.visibility = View.GONE
            }
            R.id.cv_direct_chat -> {
                pref?.setPositon(2)
                ivStatus?.visibility = View.GONE
                ivDownload?.visibility = View.GONE
                ivChat?.visibility = View.VISIBLE
                notiStatus?.visibility = View.GONE
                notiDownloader?.visibility = View.GONE
                notiChat?.visibility = View.VISIBLE
            }
            R.id.rl_search -> {

                if (pref?.getSearchPref() == true) {
                    llSearch?.visibility = View.GONE
                    pref?.setSearchPref(false)
                    cbSearch?.isChecked = false
                    removeIndex(SEARCH_INDEX)
                } else {

                    if (getIndexListSize() < 3) {
                        pref?.setSearchPref(true)
                        llSearch?.visibility = View.VISIBLE
                        cbSearch?.isChecked = true
                        addIndex(SEARCH_INDEX)
                        Log.d("TAG", "showData1: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            R.id.rl_camera -> {

                if (pref?.getCameraPref() == true) {
                    llCamera?.visibility = View.GONE
                    pref?.setCameraPref(false)
                    cbCamera?.isChecked = false
                    removeIndex(CAMERA_INDEX)
                } else {
                    if (getIndexListSize() < 3) {
                        pref?.setCameraPref(true)
                        llCamera?.visibility = View.VISIBLE
                        cbCamera?.isChecked = true
                        addIndex(CAMERA_INDEX)
                        Log.d("TAG", "showData2: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }


                }
            }
            R.id.rl_whats_app -> {

                if (pref?.getWhatsAppPref() == true) {
                    llWhatsApp?.visibility = View.GONE
                    pref?.setWhatsAppPref(false)
                    cbWhatsApp?.isChecked = false
                    removeIndex(WHATS_APP_INDEX)
                } else {
                    if (getIndexListSize() < 3) {
                        pref?.setWhatsAppPref(true)
                        llWhatsApp?.visibility = View.VISIBLE
                        cbWhatsApp?.isChecked = true
                        addIndex(WHATS_APP_INDEX)
                        Log.d("TAG", "showData3: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            R.id.rl_msg -> {

                if (pref?.getMessagePref() == true) {
                    llMessege?.visibility = View.GONE
                    pref?.setMessagePref(false)
                    cbMessage?.isChecked = false
                    removeIndex(MESSAGE_INDEX)
                } else {
                    if (getIndexListSize() < 3) {
                        pref?.setMessagePref(true)
                        llMessege?.visibility = View.VISIBLE
                        cbMessage?.isChecked = true
                        addIndex(MESSAGE_INDEX)
                        Log.d("TAG", "showData4: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            R.id.rl_messanger -> {

                if (pref?.getMessengerPref() == true) {
                    llMessenger?.visibility = View.GONE
                    pref?.setMessengerPref(false)
                    cbMessenger?.isChecked = false
                    removeIndex(MESSENGER_INDEX)
                } else {
                    if (getIndexListSize() < 3) {
                        pref?.setMessengerPref(true)
                        llMessenger?.visibility = View.VISIBLE
                        cbMessenger?.isChecked = true
                        addIndex(MESSENGER_INDEX)
                        Log.d("TAG", "showData5: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            R.id.rl_facebook -> {

                if (pref?.getFacebookPref() == true) {
                    llFacebook?.visibility = View.GONE
                    pref?.setFacebookPref(false)
                    cbFacebook?.isChecked = false
                    removeIndex(FACEBOOK_INDEX)
                } else {
                    if (getIndexListSize() < 3) {
                        pref?.setFacebookPref(true)
                        llFacebook?.visibility = View.VISIBLE
                        cbFacebook?.isChecked = true
                        addIndex(FACEBOOK_INDEX)
                        Log.d("TAG", "showData6: " + getIndexListSize())
                    } else {
                        Toast.makeText(this, "Only 3 items at a time", Toast.LENGTH_SHORT).show()
                    }

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

    fun checkIndex(index: Int): Boolean {
        return countList.contains(index)
    }

    fun addIndex(index: Int) {
        if (!countList.contains(index)) {
            countList.add(index)
        }

    }

    fun removeIndex(index: Int) {
        if (countList.contains(index)) {
            countList.remove(index)
        }
    }

    fun getIndexListSize(): Int {
        return countList.size
    }
}