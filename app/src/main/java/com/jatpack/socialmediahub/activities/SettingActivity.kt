package com.jatpack.socialmediahub.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.BottomStatusAdapter
import com.jatpack.socialmediahub.adapter.NotificationAdapter
import com.jatpack.socialmediahub.model.BottomList
import com.jatpack.socialmediahub.model.NotificatioListItem
import com.jatpack.socialmediahub.service.SocialMediaHubService
import com.jatpack.socialmediahub.util.SetClick

class SettingActivity : AppCompatActivity(), SetClick {
    private var recyclerView: RecyclerView? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var notificationList = ArrayList<NotificatioListItem>()
    private var bottomRecyclerView: RecyclerView? = null
    private var bottomStatusAdapter: BottomStatusAdapter? = null
    private var bottomAppList = ArrayList<BottomList>()
    private var serviceToggle: ToggleButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        serviceToggle = findViewById(R.id.serviceToggle)
        notificationList.add(NotificatioListItem(R.drawable.ic_search_option, "Search"))
        notificationList.add(NotificatioListItem(R.drawable.ic_camera_option, "Camera"))
        notificationList.add(NotificatioListItem(R.drawable.ic_whats_app_option, "Whats App"))
        notificationList.add(NotificatioListItem(R.drawable.ic_msg_option, "Message"))
        notificationList.add(NotificatioListItem(R.drawable.ic_messanger_option, "Messanger"))
        notificationList.add(NotificatioListItem(R.drawable.ic_facebook, "Facebook"))
        recyclerView = findViewById(R.id.rec)
        bottomAppList.add(BottomList(R.drawable.ic_wa_status_icon, "WA Status"))
        bottomAppList.add(BottomList(R.drawable.ic_video_downloader_icon, "Video Downloader"))
        bottomAppList.add(BottomList(R.drawable.ic_direct_chat_icon, "Direct Chat"))
        bottomRecyclerView = findViewById(R.id.horizontal_rec)
        showData()
        showBottomData()
        serviceToggle?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                Log.d("TAG", "onCreate: "+isChecked)
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
                Log.d("TAG", "onCreate1: "+isChecked)
                stopService(Intent(this, SocialMediaHubService::class.java))
            }
        })
        /*serviceToggle?.setOnCheckedChangeListener { _, isChecked ->

        }*/
    }

    private fun showBottomData() {
        bottomStatusAdapter = BottomStatusAdapter(this, bottomAppList, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        bottomRecyclerView?.layoutManager = layoutManager
        bottomRecyclerView?.adapter = bottomStatusAdapter
    }

    private fun showData() {
        notificationAdapter = NotificationAdapter(this, notificationList, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = notificationAdapter
    }

    override fun onClick(view: View, position: Int) {
        if (view.id == R.id.rl) {
            findViewById<ImageView>(R.id.img_check).visibility = View.VISIBLE
        }
    }

    override fun onLongClcik(view: View, position: Int) {
        TODO("Not yet implemented")
    }
}