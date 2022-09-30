package com.pds.socialmediahub.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.example.whatsdelete.api.APIClient
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.request.CheckUpdateAPIRequest
import com.example.whatsdelete.utils.AppUtils
import com.example.whatsdelete.utils.Prefs
import com.example.whatsdelete.viewmodel.ApiDataViewModel
import com.example.whatsdelete.viewmodel.MyViewModelFactory
import com.example.whatsdelete.viewmodel.Repository
import com.pds.socialmediahub.R
import com.pds.socialmediahub.engine.AppMapperConstant
import com.pds.socialmediahub.engine.TransLaunchFullAdsActivity
import engine.app.adshandler.AHandler
import engine.app.fcm.GCMPreferences
import engine.app.fcm.MapperUtils
import engine.app.listener.OnBannerAdsIdLoaded
import engine.app.listener.OnCacheFullAdLoaded
import engine.app.serviceprovider.Utils
import engine.util.LinearLayoutBannerAdsContainer

class SplashActivity : BaseActivity(), OnBannerAdsIdLoaded {
    private var mPreference: GCMPreferences? = null
    private var layoutStart: RelativeLayout? = null
    private var h: Handler? = null
    private var appLaunch = false
    private var isBannerLoaded = false
    private var isFullAdsLoaded = false
    private var firstLaunchHandler: Handler? = null
    private var isFirstAdsNotLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initializeViews()
    }

    fun initializeViews() {
        if (!isTaskRoot) {
            finish()
            return
        }
        appLaunch = false


        checkUpdatedKeyForCategoryList()
        AHandler.getInstance().v2CallOnSplash(this, object : OnCacheFullAdLoaded {
            override fun onCacheFullAd() {
                println("SplashActivity.openDashboardThroughBannerLoaded 001 fulladscaching")
                openDashboardThroughFullAdsCaching()
            }

            override fun onCacheFullAdFailed() {
                openDashboardThroughFullAdsCaching()
            }
        })

        mPreference = GCMPreferences(this)
        layoutStart = findViewById(R.id.letsStart)
        layoutStart?.setOnClickListener(View.OnClickListener { view: View? ->
            // Constants.INSTANCE.logGAEvents(this, Constants.SPLASH_LETS_START)
            onInAppWithAdsClick()
        })
        if (mPreference!!.isFirsttime /* || !getCallDoRadoConditions()*/) {
            // layoutStart.setVisibility(View.GONE);
            // imageView.setAnimation(animation);
        } else {
            /*findViewById(R.id.imageView).setVisibility(View.GONE);
            ImageView logo = findViewById(R.id.Logo);
            logo.setVisibility(View.VISIBLE);*/
            //logo.setAnimation(animation);
            h = Handler()
            h!!.postDelayed(r, 10000)
        }
        val layout_tnc = findViewById<LinearLayout>(R.id.layout_tnc)
        Utils().showPrivacyPolicy(
            this,
            layout_tnc,
            mPreference!!.isFirsttime /*|| !getCallDoRadoConditions()*/
        )
        val adsbanner = findViewById<LinearLayout>(R.id.adsbanner)
        val item = LinearLayoutBannerAdsContainer(this, this)
        item.addView(AHandler.getInstance().getBannerFooter(this, this))
        adsbanner.removeAllViews()
        adsbanner.addView(item)
        if (mPreference!!.isFirsttime) {
            checkLetStartButton()
        }
    }

    private val r = Runnable { launchApp() }
    private fun launchApp() {
        println("1241 app launch logs $appLaunch")
        if (!appLaunch) {
            appLaunch = true
            appLaunch(TransLaunchFullAdsActivity::class.java)
            finish()
        }
    }

    override fun onBannerFailToLoad() {
        println("SplashActivity.openDashboardThroughBannerLoaded 001 failed")
        openDashboardThroughBannerLoaded()
    }

    override fun loadandshowBannerAds() {
        println("SplashActivity.openDashboardThroughBannerLoaded 001 success")
        openDashboardThroughBannerLoaded()
    }

    private fun openDashboardThroughBannerLoaded() {
        println("SplashActivity.openDashboardThroughBannerLoaded 001")
        Handler(Looper.getMainLooper()).postDelayed({
            isBannerLoaded = true
            if (mPreference!!.isFirsttime /* || !getCallDoRadoConditions()*/ && isFullAdsLoaded) {
                layoutStart!!.visibility = View.VISIBLE
                try {
                    if (firstLaunchHandler != null) {
                        firstLaunchHandler!!.removeCallbacks(firstLaunchRunable)
                    }
                } catch (e: Exception) {
                    println("exception splash 1 $e")
                }
            }
            if (!mPreference!!.isFirsttime && isFullAdsLoaded) {
                launchApp()
                try {
                    if (h != null) {
                        h!!.removeCallbacks(r)
                    }
                } catch (e: Exception) {
                    println("exception splash 1 \$e$e")
                }
            }
        }, 1500)
    }

    private fun openDashboardThroughLaunchFullAdsLoaded() {
        println("SplashActivity.openDashboardThroughBannerLoaded 002")
        launchApp()
        try {
            if (h != null) {
                h!!.removeCallbacks(r)
            }
        } catch (e: Exception) {
            println("exception splash 1 \$e")
        }
    }

    private val firstLaunchRunable = Runnable {
        println("SplashActivity.openDashboardThroughBannerLoaded 0099")
        if (mPreference!!.isFirsttime || !isFullAdsLoaded || !isBannerLoaded /*|| !getCallDoRadoConditions()*/) {
            isFirstAdsNotLoaded = true
            layoutStart!!.visibility = View.VISIBLE
        }
    }

    private fun checkLetStartButton() {
        firstLaunchHandler = Handler(Looper.getMainLooper())
        firstLaunchHandler!!.postDelayed(firstLaunchRunable, 10000)
    }

    private fun openDashboardThroughFullAdsCaching() {
        isFullAdsLoaded = true
        if (mPreference!!.isFirsttime /*|| !getCallDoRadoConditions()*/ && isBannerLoaded) {
            layoutStart!!.visibility = View.VISIBLE
            try {
                if (firstLaunchHandler != null) {
                    firstLaunchHandler!!.removeCallbacks(firstLaunchRunable)
                }
            } catch (e: Exception) {
                println("exception splash 1 $e")
            }
        }
        if (!mPreference!!.isFirsttime && isBannerLoaded) {
            try {
                openDashboardThroughLaunchFullAdsLoaded()
            } catch (e: Exception) {
                println("exception splash 1 $e")
            }
        }
    }

    private fun appLaunch(cls: Class<*>) {
        val intent = intent
        val type = intent.getStringExtra(MapperUtils.keyType)
        val value = intent.getStringExtra(MapperUtils.keyValue)
        println("meenu 123 SplashActivityV3.appLaunch $type $value ")
        try {
            if (type != null && value != null) {
                launchAppWithMapper(cls, type, value)
            } else {
                startActivity(
                    Intent(this@SplashActivity, cls)
                        .putExtra(
                            AppMapperConstant.getInstance().FULLADSTYPE,
                            AppMapperConstant.getInstance().Launch_FullAds
                        )
                )
            }
        } catch (e: Exception) {
            startActivity(
                Intent(this@SplashActivity, cls)
                    .putExtra(
                        AppMapperConstant.getInstance().FULLADSTYPE,
                        AppMapperConstant.getInstance().Launch_FullAds
                    )
            )
        }
    }

    private fun launchAppWithMapper(cls: Class<*>, type: String, value: String) {
        startActivity(
            Intent(this, cls)
                .putExtra(MapperUtils.keyType, type)
                .putExtra(MapperUtils.keyValue, value)
                .putExtra(
                    AppMapperConstant.getInstance().FULLADSTYPE,
                    AppMapperConstant.getInstance().Launch_FullAds
                )
        )
    }

    private fun onInAppWithAdsClick() {
        // acceptCallDoRadoConditions();
        launchApp()
        mPreference!!.setFirstTime(false)
    }





    fun checkUpdatedKeyForCategoryList(){
        val prefs = Prefs(this)
        val repository = Repository(APIClient.getNetworkService())
       val viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        ).get(ApiDataViewModel::class.java)
        viewModel?.callCheckUpdateAPI(CheckUpdateAPIRequest(Constants.APP_ID, AppUtils.getCountryCode(this)!!))
            ?.observe(this) { list ->
                Log.d("TAG", "onActivityCreated1: adgfadsgadg 002 abandfhaj splash")
                if (prefs?.getUpdatedKey()!=null && !prefs?.getUpdatedKey().equals("")){

                    if (!prefs?.getUpdatedKey().equals(list.data.updatekey)){
                        prefs?.setUpdatedKey(list.data.updatekey)
                        prefs?.setCategoryList(null)
                        prefs?.setSubCategoryList(null)
                    }

                }

            }
    }


}
