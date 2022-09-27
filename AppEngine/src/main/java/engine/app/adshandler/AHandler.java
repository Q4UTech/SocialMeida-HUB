package engine.app.adshandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import app.pnd.adshandler.R;
import engine.app.EngineAppApplication;
import engine.app.PrintLog;
import engine.app.enginev4.AdsEnum;
import engine.app.enginev4.AdsHelper;
import engine.app.enginev4.LoadAdData;
import engine.app.fcm.GCMPreferences;
import engine.app.fcm.MapperUtils;
import engine.app.listener.AppAdsListener;
import engine.app.listener.AppFullAdsCloseListner;
import engine.app.listener.AppFullAdsListener;
import engine.app.listener.OnBannerAdsIdLoaded;
import engine.app.listener.OnCacheFullAdLoaded;
import engine.app.listener.OnRewardedEarnedItem;
import engine.app.listener.onParseDefaultValueListener;
import engine.app.server.v2.DataHubConstant;
import engine.app.server.v2.DataHubPreference;
import engine.app.server.v2.GameProvidersResponce;
import engine.app.server.v2.GameServiceV2ResponseHandler;
import engine.app.server.v2.MoreFeature;
import engine.app.server.v2.MoreFeatureResponseHandler;
import engine.app.server.v2.Slave;
import engine.app.serviceprovider.Utils;
import engine.app.ui.PrintActivity;
import engine.app.ui.ShowAssetValueDialog;


public class AHandler {
    private static AHandler instance;
    private PromptHander promptHander;
    private int mMinBannerHeight = -1;
    private int mMinBannerLargeHeight = -1;
    //private int mMinBannerRectHeight = -1;
    private FrameLayout appAdContainer;

    private int adsCounterTaps = 0;


    private AHandler() {
        promptHander = new PromptHander();
    }

    public static AHandler getInstance() {
        if (instance == null) {
            synchronized (AHandler.class) {
                if (instance == null) {
                    instance = new AHandler();
                }
            }
        }
        return instance;
    }


    /**
     * calling from app launcher class
     */
    public void v2CallOnSplash(final Activity context, final OnCacheFullAdLoaded l) {
        new GCMPreferences(context).setSplashName(context.getClass().getName());
        DataHubPreference dP = new DataHubPreference(context);
        dP.setAppName(Utils.getAppName(context));
        DataHubConstant.APP_LAUNCH_COUNT = Integer.parseInt(dP.getAppLaunchCount());
        Log.d("hello test ads load", "Hello onparsingDefault navigation 001");

        EngineHandler engineHandler = new EngineHandler(context);
        engineHandler.initDefaultValue();
        Log.d("AHandler", "NewEngine Hello onparsingDefault navigation 002");

        engineHandler.initServices(false, new onParseDefaultValueListener() {
            @Override
            public void onParsingCompleted() {
                Log.d("DataHubHandler", "Test DataHubHandler old hjhjhjhjh");
                /*
                 * cache are working on navigation ..
                 */
                handleLaunchCache(context, l);
            }
        });

        engineHandler.initServices(true, new onParseDefaultValueListener() {
            @Override
            public void onParsingCompleted() {
            }
        });

        initCacheOpenAds(context);
    }


    /**
     * calling from app dashboard
     */

    public void v2CallonAppLaunch(final Activity context) {
        new GCMPreferences(context).setDashboardName(context.getClass().getName());


            cacheNavigationFullAd(context);
            if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("true")) {
                cacheExitFullAd(context);
            }
        


        if (promptHander == null) {
            promptHander = new PromptHander();
        }
        promptHander.checkForForceUpdate(context);
        promptHander.checkForNormalUpdate(context);

        EngineHandler engineHandler = new EngineHandler(context);
        engineHandler.doGCMRequest();
        engineHandler.doTopicsRequest();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callingForMapper(context);
            }
        }, 2000);

    }

    /**
     * @param activity call this function when app launch from background
     */
    public void v2CallOnBGLaunch(Activity activity) {
        EngineHandler engineHandler = new EngineHandler(activity);
        engineHandler.initServices(false, new onParseDefaultValueListener() {
            @Override
            public void onParsingCompleted() {
            }
        });

        cacheLaunchFullAd(activity, new OnCacheFullAdLoaded() {
            @Override
            public void onCacheFullAd() {

            }

            @Override
            public void onCacheFullAdFailed() {

            }
        });
        Utils.setFullAdsCount(activity, Utils.getStringtoInt(Slave.FULL_ADS_nevigation));


    }


    /*
     *
     *
     * */
    public void v2CallOnExitPrompt(Activity context) {
//        context.finishAffinity();

        /*
         * full ads count will be 0 on exit..
         */
        Log.d("AHandler", "Hello v2CallOnExitPrompt gsfgsdgds");
        Utils.setFullAdsCount(context, 0);

        //Open Ads count will be 0 on exit..
        Utils.setOpenAdsCount_start(context, 0);
    }



    /**
     * open FAQs customtab
     */
    public void showFAQs(Activity mContext) {
        Utils.showFAQs(mContext);
    }

    /**
     * show banner footer ads
     */
    public View getBannerFooter(Activity context, OnBannerAdsIdLoaded bannerAdsIdLoaded) {
        if (!Utils.isNetworkConnected(context)) {
            bannerAdsIdLoaded.onBannerFailToLoad();
            return getDefaultAdView(context);
        }

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.BOTTOM_BANNER_start_date)) {

            if (Slave.TYPE_BOTTOM_BANNER.equalsIgnoreCase(Slave.BOTTOM_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                appAdContainer.setMinimumHeight(getMinBannerHeight(context, R.dimen.banner_height));
                appAdContainer.setPadding(0, 10, 0, 0);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerFooter(context, loadAdData, appAdContainer, bannerAdsIdLoaded);

                return appAdContainer;

            } else if (Slave.TYPE_BANNER_LARGE.equalsIgnoreCase(Slave.BOTTOM_BANNER_call_native)) {
                return getBannerLarge(context);

            }

        } else {
            bannerAdsIdLoaded.onBannerFailToLoad();
        }
        return getDefaultAdView(context);
    }

    private void loadBannerFooter(final Activity context, final LoadAdData loadAdData, final ViewGroup ll,
                                  final OnBannerAdsIdLoaded bannerAdsIdLoaded) {
        AdsHelper.getInstance().getNewBannerFooter(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerFooter onAdFailed " + pos + " provider name " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.BOTTOM_BANNER_providers.size()) {
                    bannerAdsIdLoaded.onBannerFailToLoad();
                    ll.setVisibility(View.GONE);
                }
                loadBannerFooter(context, loadAdData, ll, bannerAdsIdLoaded);
            }
        });

    }

    /**
     * show banner header ads
     */
    public View getBannerHeader(Activity context) {
        if (!Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.TOP_BANNER_start_date)) {
            if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.TOP_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                appAdContainer.setMinimumHeight(getMinBannerHeight(context, R.dimen.banner_height));
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerHeader(context, loadAdData, appAdContainer);

                return appAdContainer;
            }

            if (Slave.TYPE_BANNER_LARGE.equalsIgnoreCase(Slave.TOP_BANNER_call_native)) {
                return getDefaultAdView(context);
            }

        }
        return getDefaultAdView(context);

    }

    private void loadBannerHeader(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerHeader(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerHeader onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.TOP_BANNER_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadBannerHeader(context, loadAdData, ll);
            }
        });

    }

    /**
     * show banner large ads
     */
    public View getBannerLarge(Activity context) {
        if (!Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.LARGE_BANNER_start_date)) {
            if (Slave.BANNER_TYPE_LARGE.equalsIgnoreCase(Slave.LARGE_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                appAdContainer.setMinimumHeight(getMinBannerHeight(context, R.dimen.banner_large_height));
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerLarge(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.BANNER_TYPE_HEADER.equalsIgnoreCase(Slave.LARGE_BANNER_call_native)) {
                return getDefaultAdView(context);

            }

        }
        return getDefaultAdView(context);
    }

    private void loadBannerLarge(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerLarge(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerLarge onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.LARGE_BANNER_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadBannerLarge(context, loadAdData, ll);
            }
        });

    }

    /**
     * show banner rectangle ads
     */
    public View getBannerRectangle(Activity context) {
        if ( !Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.RECTANGLE_BANNER_start_date)) {

            if (Slave.BANNER_TYPE_RECTANGLE.equalsIgnoreCase(Slave.RECTANGLE_BANNER_call_native)) {
                if (appAdContainer != null) {
                    appAdContainer.removeAllViews();
                }


                Log.d("AHandler", "Test getBannerRectangle..." + " " + context);
                appAdContainer = (FrameLayout) LayoutInflater.from(context)
                        .inflate(R.layout.native_ads_progress_dialog_ads_loader, null, false);
                LinearLayout linearLayout = appAdContainer.findViewById(R.id.ll_progress_layout);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_rect_height));

                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerRectangle(context, loadAdData, appAdContainer);

                return appAdContainer;


            } else if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.RECTANGLE_BANNER_call_native)) {
                Log.d("AHandler", "Test getBannerRectangle2222...");
                //return getNativeMedium(context);
                return getNativeRectangle(context);
            }
        }
        return getDefaultAdView(context);

    }

    private void loadBannerRectangle(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerRectangle(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerRectangle onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.RECTANGLE_BANNER_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadBannerRectangle(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native medium ads
     */
    public View getNativeRectangle(Activity context) {
        if ( !Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }


        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {

            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {

                FrameLayout nativeAdsContainer = (FrameLayout) LayoutInflater.from(context)
                        .inflate(R.layout.native_ads_progress_dialog_ads_loader, null, false);
                LinearLayout linearLayout = nativeAdsContainer.findViewById(R.id.ll_progress_layout);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_rect_height));

                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeRectangle(context, loadAdData, nativeAdsContainer);

                return nativeAdsContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);

            }
        }
        return getDefaultAdView(context);

    }

    private void loadNativeRectangle(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeRectangle(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                addAdViewInContainer(ll, adsView);
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeRectangle onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.NATIVE_MEDIUM_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadNativeRectangle(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native large ads
     */
    public View getNativeLarge(Activity context) {
        if (!Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_LARGE_start_date)) {

            if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {

                LinearLayout nativeAdsContainer = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.native_ads_progress_dialog_ads_loader_new, null, false);
                LinearLayout linearLayout = nativeAdsContainer.findViewById(R.id.ll_progress_layout);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_large_height));
//
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeLarge(context, loadAdData, nativeAdsContainer);
                return nativeAdsContainer;

            } else if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {
                return getNativeMedium(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {
                return getBannerHeader(context);
            }
        }
        return getDefaultAdView(context);
    }

    private void loadNativeLarge(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeLarge(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                Log.d("AHandler ", "NewEngine getNewNativeLarge loadNativeLarge " + ll + "  " + adsView);

                addAdViewInContainer(ll, adsView);
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeLarge onAdFailed "
                        + pos + " " + providerName + " msg " + errorMsg + "providers list size  " + Slave.NATIVE_LARGE_providers.size());
                loadAdData.setPosition(pos);
                if (pos >= Slave.NATIVE_LARGE_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadNativeLarge(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native grid ads and using native medium ads id
     */
    public View getNativeGrid(Activity context) {

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {
            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {

                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                appAdContainer.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_grid_height));

                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeGrid(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);

            }
        }

        return getDefaultAdView(context);
    }

    private void loadNativeGrid(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeGrid(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                } else {
                    LinearLayout appAdContainer = new LinearLayout(context);
                    appAdContainer.setGravity(Gravity.CENTER);
                    appAdContainer.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_grid_height));
                    appAdContainer.removeAllViews();
                    appAdContainer.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeGrid onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                if (pos >= Slave.NATIVE_MEDIUM_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadNativeGrid(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native rectangle ads
     */
    public View getNativeMedium(Activity context) {
        if ( !Utils.isNetworkConnected(context)) {
            return getDefaultAdView(context);
        }

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {
            // Slave.NATIVE_MEDIUM_call_native = "native_large";
            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                FrameLayout nativeAdsContainer = (FrameLayout) LayoutInflater.from(context)
                        .inflate(R.layout.native_ads_progress_dialog_ads_loader, null, false);
                LinearLayout linearLayout = nativeAdsContainer.findViewById(R.id.ll_progress_layout);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setMinimumHeight(getMinNativeHeight(context, R.dimen.native_medium_height));

                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeMedium(context, loadAdData, nativeAdsContainer);

                return nativeAdsContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);
            }
        }
        return getDefaultAdView(context);

    }

    private void loadNativeMedium(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeMedium(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                addAdViewInContainer(ll, adsView);
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeMedium onAdFailed " + pos + " " + providerName + " msg " + errorMsg + "   " + Slave.NATIVE_MEDIUM_providers.size());
                loadAdData.setPosition(pos);
                if (pos >= Slave.NATIVE_MEDIUM_providers.size()) {
                    ll.setVisibility(View.GONE);
                }
                loadNativeMedium(context, loadAdData, ll);

            }
        });

    }


    /*
     * cache are working on launch full ads ..
     */
    private void handleLaunchCache(Activity context, OnCacheFullAdLoaded l) {
        try {
            int full_nonRepeat;
            PrintLog.print("cacheHandle >>1" + " " + DataHubConstant.APP_LAUNCH_COUNT);
            if (Slave.LAUNCH_NON_REPEAT_COUNT != null && Slave.LAUNCH_NON_REPEAT_COUNT.size() > 0) {
                for (int i = 0; i < Slave.LAUNCH_NON_REPEAT_COUNT.size(); i++) {
                    full_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_full);

                    PrintLog.print("cacheHandle >>2" + " launchCount = " + DataHubConstant.APP_LAUNCH_COUNT + " | launchAdsCount = " + full_nonRepeat);

                    if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                        PrintLog.print("cacheHandle >>3" + " " + full_nonRepeat);
                        cacheLaunchFullAd(context, l);
                        return;
                    }
                }
            }
            PrintLog.print("cacheHandle >>4" + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
            if (Slave.LAUNCH_REPEAT_FULL_ADS != null && !Slave.LAUNCH_REPEAT_FULL_ADS.equalsIgnoreCase("")
                    && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_FULL_ADS) == 0) {
                PrintLog.print("cacheHandle >>5" + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
                cacheLaunchFullAd(context, l);
            }

        } catch (Exception e) {
            PrintLog.print("cacheHandle excep ");
        }

    }

    /**
     * cache Launch full ads
     */
    private void cacheLaunchFullAd(Activity activity, OnCacheFullAdLoaded listener) {
        if (!Utils.isNetworkConnected(activity) ) {
            listener.onCacheFullAdFailed();
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadLaunchCacheFullAds(activity, loadAdData, listener);
    }

    private void loadLaunchCacheFullAds(final Activity context, final LoadAdData loadAdData, final OnCacheFullAdLoaded listener) {
        AdsHelper.getInstance().getNewLaunchCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                // loadtofailedLaunchAds=false;
                if (listener != null) {
                    listener.onCacheFullAd();
                }
                System.out.println("NewEngine loadLaunchCacheFullAds onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                // loadtofailedLaunchAds=true;
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadLaunchCacheFullAds(context, loadAdData, listener);
                Log.d("AHandler", "NewEngine loadLaunchCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                System.out.println("NewEngine loadLaunchCacheFullAds onFullAdClosed");
            }
        }, listener);

    }

    /**
     * show full ads on launch
     */
    private void showFullAdsOnLaunch(Activity activity, AppFullAdsCloseListner listener) {

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadFullAdsOnLaunch(activity, loadAdData, listener);
    }

    private void loadFullAdsOnLaunch(final Activity context, final LoadAdData loadAdData, AppFullAdsCloseListner listener) {
        System.out.println("NewEngine loadFullAdsOnLaunch " + context.getLocalClassName());
        AdsHelper.getInstance().showFullAdsOnLaunch(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("NewEngine loadFullAdsOnLaunch onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                Log.d("AHandler", "NewEngine loadFullAdsOnLaunch onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg + "   " +
                        Slave.LAUNCH_FULL_ADS_providers.size());

                pos++;
                loadAdData.setPosition(pos);
                if (pos >= Slave.LAUNCH_FULL_ADS_providers.size()) {
                    //  listener.onFullAdClosed();
                    onCloseFullAd(context, listener);
                } else {
                    loadFullAdsOnLaunch(context, loadAdData, listener);
                }

            }

            @Override
            public void onFullAdClosed() {
                Log.d("Listener Error", "NewEngine loadFullAdsOnLaunch onAdClosed. " + listener);
                // listener.onFullAdClosed();
                onCloseFullAd(context, listener);
                Log.d("Listener Error", "NewEngine loadFullAdsOnLaunch onAdClosed. 111");
            }
        });

    }

    /**
     * cache Exit full ads
     */
    private void cacheExitFullAd(Activity activity) {

        Log.d("AHandler", "Hello showFullAdsOnExit fullads checked 001 bb");
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadExitCacheFullAds(activity, loadAdData);
    }

    private void loadExitCacheFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().getNewExitCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {

                System.out.println("NewEngine loadExitCacheFullAds.onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadExitCacheFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadExitCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                Log.d("AHandler", "NewEngine loadExitCacheFullAds onClosedFullAds  >>>>> 003");
            }
        });

    }

    /**
     * show full ads on exit
     */
    private void showFullAdsOnExit(Activity activity, AppFullAdsCloseListner listner) {


        Log.d("AHandler", "Hello showFullAdsOnExit fullads checked 001");
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadFullAdsOnExit(activity, loadAdData, listner);
    }

    private void loadFullAdsOnExit(final Activity context, final LoadAdData loadAdData, final AppFullAdsCloseListner mListener) {
        AdsHelper.getInstance().showFullAdsOnExit(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                Log.d("AHandler", "Hello showFullAdsOnExit fullads checked 002");

            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine loadFullAdsOnExit onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);

                loadAdData.setPosition(pos);
                if (pos >= Slave.EXIT_FULL_ADS_providers.size()) {
                    onCloseFullAd(context, mListener);
                } else {
                    loadFullAdsOnExit(context, loadAdData, mListener);
                }
            }

            @Override
            public void onFullAdClosed() {
                Log.d("AHandler", "Hello showFullAdsOnExit fullads checked 004");

                onCloseFullAd(context, mListener);
            }
        });

    }

    /**
     * cache full ads
     */
    private void cacheNavigationFullAd(Activity activity) {
        Log.d("Ahandler","NewEngine cachenavigationfullad");

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadNavigationCacheFullAds(activity, loadAdData);
    }

    private void loadNavigationCacheFullAds(final Activity context, final LoadAdData loadAdData) {

        //  System.out.println("BBB AHandler.onFullAdLoaded2222"+" "+ loadtofailedLaunchAds);

        AdsHelper.getInstance().getNewNavCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("BBB AHandler.onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadNavigationCacheFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadNavigationCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                System.out.println("BBB AHandler.onFullAdClosed");
            }
        });

    }

    /**
     * show full ads forcefully or not depend on isForced boolean
     */
    public void showFullAds(Activity activity, boolean isForced) {

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        Log.d("AHandler", " NewEngine showFullAds getFullAdsCount "
                + Utils.getFullAdsCount(activity) + " FULL_ADS_nevigation " + Utils.getStringtoInt(Slave.FULL_ADS_nevigation)
                + activity.getLocalClassName());

        if (Utils.getDaysDiff(activity) >= Utils.getStringtoInt(Slave.FULL_ADS_start_date)) {
            Utils.setFullAdsCount(activity, -1);
            System.out.println("Full Nav Adder setter >>> " + Utils.getFullAdsCount(activity));

            if (!isForced) {
                if (Utils.getFullAdsCount(activity) >= Utils.getStringtoInt(Slave.FULL_ADS_nevigation)) {
                    Utils.setFullAdsCount(activity, 0);
                    System.out.println("Full Nav Adder setter >>> 1 " + Utils.getFullAdsCount(activity));
                    loadFullAds(activity, loadAdData);
                }
            } else {
                loadForceFullAds(activity, loadAdData);
            }
        }
    }

    /**
     * load force full ads
     */
    private void loadForceFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showForcedFullAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadForceFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadForceFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {

            }
        });

    }

    /**
     * load full ads
     */
    private void loadFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showFullAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                Log.d("AHandler", "NewEngine  showFullAds onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine  showFullAds onFullAdFailed " + loadAdData.getPosition() + " " + adsEnum.name() + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                Log.d("AHandler", "NewEngine  showFullAds onFullAdClosed");
            }
        });
    }


    /**
     * cache rewarded ads
     */
    private void cacheNavigationRewardedAds(Activity activity) {

        if (Slave.REWARDED_VIDEO_status.equals("true")) {
            LoadAdData loadAdData = new LoadAdData();
            loadAdData.setPosition(0);
            loadNavigationCacheRewardedAds(activity, loadAdData);
        }
    }

    private void loadNavigationCacheRewardedAds(final Activity context,
                                                final LoadAdData loadAdData) {
        AdsHelper.getInstance().getNewNavCacheRewardedAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("AHandler.loadNavigationCacheRewardedAds");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadNavigationCacheRewardedAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadNavigationCacheRewardedAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                System.out.println("AHandler.onFullAdClosed");
            }
        });

    }

    /**
     * show rewarded video..
     */
    public void showRewardedVideoOrFullAds(Activity activity, boolean forceFull, OnRewardedEarnedItem onRewardedEarnedItem) {

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);

        Log.d("AHandler", " NewEngine showRewardedVideo getAdsCount " + Utils.getFullAdsCount(activity)
                + " REWARDED_VIDEO_nevigation " + Utils.getStringtoInt(Slave.REWARDED_VIDEO_nevigation));

        if (Slave.REWARDED_VIDEO_status.equals("true")
                && Utils.getDaysDiff(activity) >= Utils.getStringtoInt(Slave.REWARDED_VIDEO_start_date)) {
            Utils.setFullAdsCount(activity, -1);

            if (Utils.getFullAdsCount(activity) >= Utils.getStringtoInt(Slave.REWARDED_VIDEO_nevigation)) {
                Utils.setFullAdsCount(activity, 0);
                loadRewardedAds(activity, loadAdData, onRewardedEarnedItem);
            }

        } else {
            showFullAds(activity, forceFull);
        }
    }

    /**
     * load rewarded ads
     */
    private void loadRewardedAds(final Activity context, final LoadAdData loadAdData, OnRewardedEarnedItem onRewardedEarnedItem) {
        AdsHelper.getInstance().showRewardedAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                Log.d("AHandler", "NewEngine  loadRewardedAds onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadRewardedAds(context, loadAdData, onRewardedEarnedItem);
                Log.d("AHandler", "NewEngine  loadRewardedAds onFullAdFailed " + loadAdData.getPosition() + " " + adsEnum.name() + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                Log.d("AHandler", "NewEngine  loadRewardedAds onFullAdClosed");
            }
        }, onRewardedEarnedItem);
    }


    public void handle_launch_For_FullAds(Activity context, AppFullAdsCloseListner listener) {
        PrintLog.print("handle launch trans prompt full ads " + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.LAUNCH_REPEAT_FULL_ADS);

        int full_nonRepeat;
        new GCMPreferences(context).setTransLaunchName(context.getClass().getName());
        if (Slave.LAUNCH_NON_REPEAT_COUNT != null && Slave.LAUNCH_NON_REPEAT_COUNT.size() > 0) {
            for (int i = 0; i < Slave.LAUNCH_NON_REPEAT_COUNT.size(); i++) {
                full_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_full);

                PrintLog.print("handle launch trans fullads " + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + full_nonRepeat);
                if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                    PrintLog.print("handle launch trans fullads non repeat..");
                    showFullAdsOnLaunch(context, listener);
                    return;
                }
            }
        }
        PrintLog.print("handle launch trans prompt repease" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
        if (Slave.LAUNCH_REPEAT_FULL_ADS != null && !Slave.LAUNCH_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_FULL_ADS) == 0) {
            PrintLog.print("handle launch trans fullads repeat..");
            showFullAdsOnLaunch(context, listener);
            return;
        }

        listener.onFullAdClosed();
    }


    public void handle_exit_fullads(Context context, AppFullAdsCloseListner
            appFullAdsCloseListner) {
        int full_nonRepeat;
        new GCMPreferences(context).setTransLaunchName(context.getClass().getName());

        if (Slave.EXIT_NON_REPEAT_COUNT != null && Slave.EXIT_NON_REPEAT_COUNT.size() > 0) {
            for (int i = 0; i < Slave.EXIT_NON_REPEAT_COUNT.size(); i++) {
                full_nonRepeat = Utils.getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).full);

                PrintLog.print("handle exit trans fullads." + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + full_nonRepeat);
                if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                    PrintLog.print("handle exit trans fullads inside 3 fullads");
                    if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("true")) {
                        showFullAdsOnExit((Activity) context, appFullAdsCloseListner);
                    }
                    return;
                }
            }
        }
        PrintLog.print("handle exit trans fullads repeat check" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.EXIT_REPEAT_FULL_ADS);
        if (Slave.EXIT_REPEAT_FULL_ADS != null && !Slave.EXIT_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.EXIT_REPEAT_FULL_ADS) == 0) {
            PrintLog.print("handle exit trans fullads inside 13 fullads" + " " + Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT);
//            showFullAdsOnExit((Activity) context, false, "repeat");
            if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("true")) {
                showFullAdsOnExit((Activity) context, appFullAdsCloseListner);
                return;
            }

        }
        appFullAdsCloseListner.onFullAdClosed();
    }

    /**
     * @param context mapper class handeling
     */
    private void callingForMapper(Activity context) {
        Intent intent = context.getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);
        System.out.println("AHandler.callingForMapper " + type + " " + value);
        try {
            if (type != null && value != null) {
                if (type.equalsIgnoreCase("url")) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    builder.addDefaultShareMenuItem();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(value));

                } else if (type.equalsIgnoreCase("deeplink")) {
                    switch (value) {
                        case MapperUtils.gcmMoreApp:
                            //Remember to add here your gcmMoreApp.
                            new Utils().moreApps(context);

                            break;
                        case MapperUtils.gcmRateApp:
                            //Remember to add here your RareAppClass.
                            new Utils().rateApps(context);
                            // new PromptHander().rateUsDialog(true, context);

                            break;
                        case MapperUtils.gcmFeedbackApp:
                            new Utils().showFeedbackPrompt(context, "Please share your valuable feedback.");
                            break;
                        case MapperUtils.gcmShareApp:
                            //Remember to add here your ShareAppClass.
                            new Utils().showSharePrompt(context, "Share this cool & fast performance app with friends & family");
                            break;
                        case MapperUtils.gcmForceAppUpdate:
                            new Utils().showAppUpdatePrompt(context);
                            break;

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("AHandler.callingForMapper excep " + e.getMessage());
        }
    }

    public ArrayList<MoreFeature> getMoreFeatures() {
        return MoreFeatureResponseHandler.getInstance().getMoreFeaturesListResponse();
    }

    public void onAHandlerDestroy() {
        AdsHelper.getInstance().onAHandlerDestroy();
    }

    private int getMinBannerHeight(Context context, int minBannerHight) {
        if (mMinBannerHeight == -1) {
            mMinBannerHeight = context.getResources().getDimensionPixelOffset(minBannerHight);
        }
        return mMinBannerHeight;
    }

    private int getMinNativeHeight(Context context, int minHight) {
        if (mMinBannerLargeHeight == -1) {
            mMinBannerLargeHeight = context.getResources().getDimensionPixelOffset(minHight);
        }
        return mMinBannerLargeHeight;
    }

    private View getDefaultAdView(Context context) {
        return new LinearLayout(context);
    }

    private void addAdViewInContainer(final ViewGroup ll, View adsView) {
        if (ll != null) {

            //Hiding the Progress
            LinearLayout ll_progress_layout = ll.findViewById(R.id.ll_progress_layout);
            if (ll_progress_layout != null) {
                ll_progress_layout.setVisibility(View.GONE);
            }

            //Adding the Native Ad in ads container
            LinearLayout ll_banner_native_layout = ll.findViewById(R.id.ll_banner_native_layout);
            if (ll_banner_native_layout != null) {
                ll_banner_native_layout.removeAllViews();
                ll_banner_native_layout.addView(adsView);
            }
        }
    }

    private void onCloseFullAd(Activity activity, AppFullAdsCloseListner appFullAdsCloseListner) {
        Log.d("Listener Error", "Error in onCloseFullAd 0000000000000000    ");
        Log.d("Listener Error", "Error in onCloseFullAd activity = " + activity + ", appFullAdsCloseListner = " + appFullAdsCloseListner);
        if (activity == null || appFullAdsCloseListner == null) {
            return;
        }

        EngineAppApplication engineAppApplication;
        if (activity.getApplication() instanceof EngineAppApplication) {
            engineAppApplication = (EngineAppApplication) activity.getApplication();
            engineAppApplication.addAppForegroundStateListener(() -> {
                if (appFullAdsCloseListner != null) {
                    appFullAdsCloseListner.onFullAdClosed();
                }
            });
        } else {
            if (appFullAdsCloseListner != null) {
                appFullAdsCloseListner.onFullAdClosed();
            }
        }
    }


    public boolean isGameShow() {
        return Slave.game_ads_responce_show_status != null && !Slave.game_ads_responce_show_status.equalsIgnoreCase("")
                && Slave.game_ads_responce_show_status.equalsIgnoreCase("true");
    }

    public boolean is_game_title() {
        return Slave.game_ads_responce_title != null && !Slave.game_ads_responce_title.equalsIgnoreCase("");
    }

    public boolean is_game_sub_title() {
        return Slave.game_ads_responce_sub_title != null && !Slave.game_ads_responce_sub_title.equalsIgnoreCase("");
    }

    public boolean is_game_icon() {
        return Slave.game_ads_responce_icon != null && !Slave.game_ads_responce_icon.equalsIgnoreCase("");
    }

    public boolean is_game_click_link() {
        return Slave.game_ads_responce_Link != null && !Slave.game_ads_responce_Link.equalsIgnoreCase("");
    }

    public boolean is_page_id(String pageid) {
        if (Slave.getGame_ads_responce_position_name != null && !Slave.getGame_ads_responce_position_name.equalsIgnoreCase("")) {

            return Slave.getGame_ads_responce_position_name.equalsIgnoreCase(pageid);

        }
        return false;
    }

    public boolean isViewTypeGame() {
        return Slave.getGame_ads_responce_view_type_game != null && !Slave.getGame_ads_responce_view_type_game.equalsIgnoreCase("");
    }

    public ArrayList<GameProvidersResponce> getGameServiceResponce() {
        return GameServiceV2ResponseHandler.getInstance().getGameV2FeaturesListResponse();
    }


    public void getGameServicesSlaveValue(String pageId) {
        if (getGameServiceResponce() != null && getGameServiceResponce().size() > 0) {
            for (int i = 0; i < getGameServiceResponce().size(); i++) {
                if (getGameServiceResponce().get(i).position_name.equalsIgnoreCase(pageId)) {
                    PrintLog.print("0555 checking Type Top Bannergameservices 0012 game provider ff");
                    //Slave.TOP_BANNER_provider_id = list.get(i).provider_id;
                    //Slave.TOP_BANNER_ad_id = list.get(i).ad_id;
                    Slave.game_ads_responce_show_status = getGameServiceResponce().get(i).show_status;
                    Slave.game_ads_responce_provider = getGameServiceResponce().get(i).provider;
                    Slave.getGame_ads_responce_position_name = getGameServiceResponce().get(i).position_name;
                    Slave.game_ads_responce_title = getGameServiceResponce().get(i).title;
                    Slave.game_ads_responce_sub_title = getGameServiceResponce().get(i).sub_title;
                    Slave.game_ads_responce_icon = getGameServiceResponce().get(i).icon;
                    Slave.game_ads_responce_Link = getGameServiceResponce().get(i).link;
                    Slave.game_ads_responce_button_text = getGameServiceResponce().get(i).button_text;
                    Slave.game_ads_responce_button_bg_color = getGameServiceResponce().get(i).button_bg_color;
                    Slave.game_ads_responce_button_text_color = getGameServiceResponce().get(i).button_text_color;
                    Slave.getGame_ads_responce_view_type_game = getGameServiceResponce().get(i).view_type_game;
                    Slave.getGame_ads_responce_page_id = getGameServiceResponce().get(i).pageid;


                }
            }
        }

    }


    /**
     * cache open ads
     */
    private void initCacheOpenAds(Activity activity) {

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadCacheOpenAds(activity, loadAdData);
    }

    private void loadCacheOpenAds(final Activity context,
                                  final LoadAdData loadAdData) {
        AdsHelper.getInstance().getAppOpenAdsCache(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("AHandler.loadNavigationCacheOpenAds");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadCacheOpenAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadNavigationCacheOpenAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                System.out.println("AHandler.onFullAdClosed");
            }
        });

    }

    /**
     * show App Open Ads..
     */
    public void showAppOpenAds(Activity activity, AppFullAdsListener listener) {

        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);

        Log.d("AHandler", " NewEngine showAppOpenAds getAdsCount " + Utils.getOpenAdsCount_start(activity)
                + " APP_OPEN_ADS_nevigation " + Utils.getStringtoInt(Slave.APP_OPEN_ADS_nevigation));

        if (Utils.getDaysDiff(activity) >= Utils.getStringtoInt(Slave.APP_OPEN_ADS_start_date)) {
            Utils.setOpenAdsCount_start(activity, -1);

            if (Utils.getOpenAdsCount_start(activity) >= Utils.getStringtoInt(Slave.APP_OPEN_ADS_nevigation)) {
                Utils.setOpenAdsCount_start(activity, 0);
                loadAppOpenAds(activity, loadAdData, listener);
            }
        }
    }

    /**
     * load App Open Ads..
     */
    private void loadAppOpenAds(final Activity context, final LoadAdData loadAdData, AppFullAdsListener listener) {
        AdsHelper.getInstance().showAppOpenAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                if (listener != null) {
                    listener.onFullAdLoaded();
                }
                Log.d("AHandler", "NewEngine  loadAppOpenAds onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadAppOpenAds(context, loadAdData, listener);
                if (listener != null) {
                    listener.onFullAdFailed(adsEnum, errorMsg);
                }
                Log.d("AHandler", "NewEngine  loadAppOpenAds onFullAdFailed " + loadAdData.getPosition() + " " + adsEnum.name() + " msg " + errorMsg);
            }

            @Override
            public void onFullAdClosed() {
                if (listener != null) {
                    listener.onFullAdClosed();
                }
                Log.d("AHandler", "NewEngine  loadAppOpenAds onFullAdClosed");
            }
        });
    }


    public void getDefaultServerAdsData(Activity activity) {
        if (activity != null) {

            adsCounterTaps++;
            if (adsCounterTaps == 10) {
                adsCounterTaps = 0;
                ShowAssetValueDialog showAssetValueDialog = new ShowAssetValueDialog(activity, new ShowAssetValueDialog.OnSelecteShowValueCallBack() {
                    @Override
                    public void onShowValueSelected(int position) {
                        Intent myIntent = new Intent(activity, PrintActivity.class);
                        myIntent.putExtra(Utils.SHOW_VALUE, position);
                        activity.startActivity(myIntent);
                    }
                });
                showAssetValueDialog.setCancelable(false);
                showAssetValueDialog.show();

            }
        }
    }

    public void onStartPrivacyPolicy(Context mContext){
        if(mContext!=null && Slave.ABOUTDETAIL_PRIVACYPOLICY!=null) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_PRIVACYPOLICY)));
        }
    }

    public void onStartTermCondistion(Context mContext){
        if(mContext!=null && Slave.ABOUTDETAIL_TERM_AND_COND!=null)
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_TERM_AND_COND)));
    }

}
