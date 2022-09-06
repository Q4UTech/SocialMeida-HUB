package com.jatpack.socialmediahub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.request.CheckUpdateAPIRequest
import com.example.whatsdelete.responce.ApplicationListData
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.Repository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jatpack.socialmediahub.activities.SettingActivity
import com.jatpack.socialmediahub.databinding.ActivityMainBinding
import com.jatpack.socialmediahub.helper.Pref
import com.jatpack.socialmediahub.ui.socialmediadownloader.services.ClipBoardService
import com.jatpack.socialmediahub.ui.status.MyDownloadsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var isSocialMediaClicked: Boolean = false
    private var viewModel: ApiDataViewModel? = null
    private var country: String? = null
    private var appId: String? = null
    private var pref:Prefs?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref=Prefs(this)

        appId = Constants.APP_ID
        country = AppUtils.getCountryCode(this)
        //when add splash than below api call is moved into splash class
        val repository = Repository(APIClient.getNetworkService())
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        )[ApiDataViewModel::class.java]
        viewModel?.callCheckUpdateAPI(CheckUpdateAPIRequest(appId!!,country!!))?.observe(this) { list ->
                Log.d("TAG", "updateedd keyyy: " + list.data.updatekey)

            if (list?.data != null){
                if(list.data.updatekey!=null && list.data.updatekey != ""){
                    println("MainActivity.onCreate")
                    if (pref?.getUpdatedKey()!=null){
                        if (!pref?.getUpdatedKey().equals(list.data.updatekey)){
                            pref?.setUpdatedKey(list.data.updatekey)
                            println("MainActivity.onCreate hihihihihih")
                            val aMap =HashMap<String, List<ApplicationListData>>()
                            pref?.setApplicationList(this,aMap)
                            pref?.setCategoryList(null)

                        }else{

                        }
                    }


                }

            }

            }

        val navView: BottomNavigationView = binding.navView

        binding.openNavDrawer.setOnClickListener(this)
        binding.searchDrop.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        binding.navSetting.setOnClickListener(this)
        binding.ivDownloaded.setOnClickListener(this)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

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
            }
            R.id.iv_downloaded -> {

                startActivity(Intent(this, MyDownloadsFragment::class.java))
                closeMenuDrawer()
            }

            R.id.ll_download -> {
                if (isSocialMediaClicked) {


                    val paste = searchBoxEditText?.text.toString()


                    if (paste != null && paste.equals("")) {
                        Toast.makeText(this, "Please paste valid URL", Toast.LENGTH_LONG).show()
                        return
                    }


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


}

