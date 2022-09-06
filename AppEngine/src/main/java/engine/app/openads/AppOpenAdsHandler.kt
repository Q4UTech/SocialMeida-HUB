package engine.app.openads

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import app.pnd.adshandler.BuildConfig
import com.applovin.adview.AppLovinFullscreenActivity
//import com.applovin.adview.AppLovinFullscreenActivity
import com.appnext.ads.interstitial.InterstitialActivity
import com.facebook.ads.AudienceNetworkActivity
import com.google.android.gms.ads.AdActivity

import engine.app.adshandler.AHandler
import engine.app.adshandler.FullPagePromo
import engine.app.enginev4.AdsEnum
import engine.app.fcm.NotificationTypeFour
import engine.app.listener.AppFullAdsListener
import engine.app.listener.OpenAdsClosedListener

object AppOpenAdsHandler : AppFullAdsListener {
    private var isOpenAdsShowing = false
    private var openAdsClosedListenerList: ArrayList<OpenAdsClosedListener>? = null
    private var excludedList: List<String>? = null

    fun showAppOpenAds(activity: Activity?) {
        Log.d("AppOpenAdsHandler", "Hello showAppOpenAds activity")
        activity?.let {
            val canShow = canShowAppOpenAds(it)
            Log.d("activityopenads", "Hello showAppOpenAds: >>> " + " " + canShow)
            if (canShow) {

                Log.d("AppOpenAdsHandler", "Hello showAppOpenAds opening")
                AHandler.getInstance().showAppOpenAds(it, this)
            }
        }
    }

    private fun initExcludedScreenLists(activity: Activity) {
        if (excludedList == null) {
            excludedList = ArrayList<String>().apply {

                //admob full ads class name.
                add(AdActivity::class.java.name)
                //Fb full ads class name.
                add(AudienceNetworkActivity::class.java.name)


                // Applovin full ads class name.
                //add(AppLovinInterstitialActivity::class.java.name)
                add(AppLovinFullscreenActivity::class.java.name)


                //appnext full ads class name..
                add(InterstitialActivity::class.java.name)

                //Inhouse full ads class name.
                add(FullPagePromo::class.java.name)

                //Type4 notification.
                add(NotificationTypeFour::class.java.name)

                //splash.
                add("SplashActivity")
                add("TransLaunchFullAdsActivity")


                //applovin
                add("com.applovin")


                //Fb full ads class name.
                add(AudienceNetworkActivity::class.java.name)


            }
        }

    }


    private fun canShowAppOpenAds(activity: Activity): Boolean {
        initExcludedScreenLists(activity)
        excludedList?.forEach { name ->

            /**
             * never write try catch condition..
             */

            if (TextUtils.isEmpty(name)) {
                if (BuildConfig.DEBUG) {
                    throw NullPointerException("App Open ads list should not have NULL value")
                } else {
                    return false;
                }
            }

            if (activity::class.java.name.contains(name)) {
                Log.d("AppOpenAdsHandler", "Hello canShowAppOpenAds: name = " + name)
                return false
            }
        }
        return true
    }

    override fun onFullAdLoaded() {
        Log.d("AppOpenAdsHandler", "callback - showAdMobOpenAds onFullAdLoaded ")
        isOpenAdsShowing = true
    }

    override fun onFullAdFailed(adsEnum: AdsEnum?, errorMsg: String?) {
        Log.d("AppOpenAdsHandler", "callback - showAdMobOpenAds onAdFailed $adsEnum msg $errorMsg")
        isOpenAdsShowing = false
    }

    override fun onFullAdClosed() {
        Log.d("AppOpenAdsHandler", "callback - showAdMobOpenAds onFullAdClosed ")
        isOpenAdsShowing = false

        openAdsClosedListenerList?.let { listenerList ->
            listenerList.forEach {
                it.onOpenAdsClosed()
            }
            listenerList.clear()
        }
        openAdsClosedListenerList = null
    }

    private fun isAppOpenAdsShowing(): Boolean {
        return isOpenAdsShowing
    }

    fun addAppOpenAdCloseListener(openAdsClosedListener: OpenAdsClosedListener?) {
        if (openAdsClosedListener == null)
            return
        if (isAppOpenAdsShowing()) {
            if (openAdsClosedListenerList == null)
                openAdsClosedListenerList = ArrayList()
            openAdsClosedListenerList?.add(openAdsClosedListener)
        } else {
            openAdsClosedListener.onOpenAdsClosed()
        }
    }
}