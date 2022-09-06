package engine.app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdFormat;
//import com.applovin.mediation.MaxAdListener;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.ads.MaxAdView;
//import com.applovin.mediation.ads.MaxInterstitialAd;
//import com.applovin.mediation.nativeAds.MaxNativeAdListener;
//import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
//import com.applovin.mediation.nativeAds.MaxNativeAdView;
//import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
//import com.applovin.sdk.AppLovinSdkUtils;

import app.pnd.adshandler.R;
import engine.app.enginev4.AdsEnum;
import engine.app.listener.AppAdsListener;
import engine.app.listener.AppFullAdsListener;

public class AppLovinMaxAdsProvider {

    private static AppLovinMaxAdsProvider appLovinAdsProvider = null;
//    private MaxAdView adView;
//    private MaxInterstitialAd interstitialAd;
//    private MaxNativeAdView nativeAdView;
//    private MaxNativeAdLoader nativeAdLoader;
//    private MaxAd nativeAd;

    private AppLovinMaxAdsProvider() {
    }

    public static AppLovinMaxAdsProvider getAppLovinObject() {
        if (appLovinAdsProvider == null) {
            synchronized (AppLovinAdsProvider.class) {
                if (appLovinAdsProvider == null) {
                    appLovinAdsProvider = new AppLovinMaxAdsProvider();
                }
            }
        }
        return appLovinAdsProvider;
    }


    public void getAppLovinMaxBanner(Context context, String id, final AppAdsListener listener) {
      /*  if (id != null && !id.equals("")) {
            id = id.trim();

            adView = new MaxAdView(id, MaxAdFormat.BANNER, context);
            //banner ads listner
            try {
                adView.setListener(new AppLovinMaxAdsListener(adView, listener));
            } catch (Exception e) {
                Log.d("TAG", "NewEngine getNewBannerHeader applovinMax setlistner error" + e.getMessage());
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                e.printStackTrace();
            }

            // Stretch to the width of the screen for banners to be fully functional
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // Banner height on phones and tablets is 50 and 90, respectively
            int heightPx = context.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);

            adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));

            // Set background or background color for banners to be fully functional
            adView.setBackgroundColor(ContextCompat.getColor(context, R.color.banner_applovin_bg_color));
            // Load the ad
            adView.loadAd();
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Banner Id null");
        }*/

        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void getAppLovinMaxAdaptiveBanner(Activity context, String id, final AppAdsListener listener) {
        /*if (id != null && !id.equals("")) {
            id = id.trim();

            Log.d("TAG", "NewEngine getNewBannerHeader mediation applovinMax id:-  "+id);
            adView = new MaxAdView(id, MaxAdFormat.BANNER, context);
            //banner ads listner
            try {
                adView.setListener(new AppLovinMaxAdsListener(adView, listener));
            } catch (Exception e) {
                Log.d("TAG", "NewEngine getNewBannerHeader applovinMax setlistner error" + e.getMessage());
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                e.printStackTrace();
            }

            // Stretch to the width of the screen for banners to be fully functional
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // Get the adaptive banner height.
            int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(context).getHeight();
            int heightPx = AppLovinSdkUtils.dpToPx(context, heightDp);

            adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
            adView.setExtraParameter("adaptive_banner", "true");

            // Set background or background color for banners to be fully functional
            adView.setBackgroundColor(ContextCompat.getColor(context, R.color.banner_applovin_bg_color));
            // Load the ad
            adView.loadAd();
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Banner Id null");
        }*/
        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void getAppLovinMaxBannerLarge(Activity context, String id, final AppAdsListener listener) {
        /*if (id != null && !id.equals("")) {
            id = id.trim();
            Log.d("TAG", "NewEngine getNewBannerLarge mediation applovinMax id:-  "+id);
            adView = new MaxAdView(id, MaxAdFormat.LEADER, context);
            //banner ads listner
            try {
                adView.setListener(new AppLovinMaxAdsListener(adView, listener));
            } catch (Exception e) {
                Log.d("TAG", "NewEngine getNewBannerLarge applovinMax setlistner error" + e.getMessage());
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                e.printStackTrace();
            }

            // Stretch to the width of the screen for banners to be fully functional
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // Get the adaptive banner height.
            int heightDp = MaxAdFormat.LEADER.getAdaptiveSize(context).getHeight();
            int heightPx = AppLovinSdkUtils.dpToPx(context, heightDp);

            adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
            //adView.setExtraParameter( "adaptive_banner", "true" );

            // Set background or background color for banners to be fully functional
            adView.setBackgroundColor(ContextCompat.getColor(context, R.color.banner_large_applovin_bg_color));
            // Load the ad
            adView.loadAd();
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Banner Id null");
        }*/
        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void getAppLovinMaxBannerRect(Activity context, String id, final AppAdsListener listener) {
        /*if (id != null && !id.equals("")) {
            id = id.trim();
            Log.d("TAG", "NewEngine getNewBannerRect mediation applovinMax id:-  "+id);

            adView = new MaxAdView(id, MaxAdFormat.MREC, context);
            //banner ads listner
            try {
                adView.setListener(new AppLovinMaxAdsListener(adView, listener));
            } catch (Exception e) {
                Log.d("TAG", "NewEngine getNewBannerRect applovinMax setlistner error" + e.getMessage());
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                e.printStackTrace();
            }
            // MREC width and height are 300 and 250 respectively, on phones and tablets
            int widthPx = AppLovinSdkUtils.dpToPx(context, context.getResources().getDimensionPixelSize(R.dimen.banner_rectangle_width));
            int heightPx = AppLovinSdkUtils.dpToPx(context, context.getResources().getDimensionPixelSize(R.dimen.banner_rectangle_height));

            adView.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx));

            // Set background or background color for banners to be fully functional
            adView.setBackgroundColor(ContextCompat.getColor(context, R.color.banner_rect_applovin_bg_color));
            // Load the ad
            adView.loadAd();
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Banner Id null");
        }*/

        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void loadAppLovinFullAds(final Activity context, String id, final AppFullAdsListener listener, boolean isFromCache) {

       /* if (id != null && !id.equals("")) {
            id = id.trim();

            Log.d("TAG", "NewEngine getNewCacheFullPageAd mediation applovinMax id:-  "+id);

            interstitialAd = new MaxInterstitialAd(id, context);
            try {
                interstitialAd.setListener(new AppLovinMaxFullAdsListener(context, id, interstitialAd, listener, isFromCache));
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, e.getMessage());
            }

            // Load the first ad
            interstitialAd.loadAd();
        } else {
            listener.onFullAdFailed(AdsEnum.ADS_APPLOVIN, "FUll ads id null");
        }*/

        listener.onFullAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void showAppLovinFullAds(final Activity context, String id, final AppFullAdsListener listener, boolean isFromSplash) {
      /*  if (interstitialAd != null && interstitialAd.isReady()) {
            interstitialAd.showAd();
        } else {
            interstitialAd =null;
            if (!isFromSplash) {
                loadAppLovinFullAds(context, id, listener, false);
            }
            listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, "Ads is null");
        }*/
        listener.onFullAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void showAppLovinNativeMedium(final Context context, String id, final AppAdsListener listener) {
       /* if (id != null && !id.equals("")) {
            id = id.trim();
            Log.d("TAG", "NewEngine showAppLovinNativeMedium mediation applovinMax id:-  "+id);

            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ad_applovin_native_medium)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.appIcon)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();

            nativeAdView = new MaxNativeAdView(binder, context);

            nativeAdLoader = new MaxNativeAdLoader(id, context);

//        nativeAdLoader.setRevenueListener( ad -> {
//            AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue( AdjustConfig.AD_REVENUE_APPLOVIN_MAX );
//            adjustAdRevenue.setRevenue( ad.getRevenue(), "USD" );
//            adjustAdRevenue.setAdRevenueNetwork( ad.getNetworkName() );
//            adjustAdRevenue.setAdRevenueUnit( ad.getAdUnitId() );
//            adjustAdRevenue.setAdRevenuePlacement( ad.getPlacement() );
//
//            Adjust.trackAdRevenue( adjustAdRevenue );
//        } );

            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    Log.d("TAG", "NewEngine getNewNativeMedium mediation ApplovinMax " + ad.getNetworkName() + " " +
                            ad.getNetworkPlacement() + "  " + ad.getPlacement() + "  " + ad.getFormat() + "  " + ad.getWaterfall());
                    // Cleanup any pre-existing native ad to prevent memory leaks.
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }

                    // Save ad for cleanup.
                    nativeAd = ad;

                    // Add ad view to view.
                    listener.onAdLoaded(nativeAdView);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    listener.onAdFailed(AdsEnum.ADS_APPLOVIN, error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {
                }
            });
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Ads Id Null");
        }*/

        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void showAppLovinNativeLarge(final Context context, String id, final AppAdsListener listener) {
       /* if (id != null && !id.equals("")) {
            id = id.trim();
            Log.d("TAG", "NewEngine showAppLovinNativeLarge mediation applovinMax id:-  "+id);

            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ad_applovin_native_large)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.appIcon)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();

            nativeAdView = new MaxNativeAdView(binder, context);

            nativeAdLoader = new MaxNativeAdLoader(id, context);

//        nativeAdLoader.setRevenueListener( ad -> {
//            AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue( AdjustConfig.AD_REVENUE_APPLOVIN_MAX );
//            adjustAdRevenue.setRevenue( ad.getRevenue(), "USD" );
//            adjustAdRevenue.setAdRevenueNetwork( ad.getNetworkName() );
//            adjustAdRevenue.setAdRevenueUnit( ad.getAdUnitId() );
//            adjustAdRevenue.setAdRevenuePlacement( ad.getPlacement() );
//
//            Adjust.trackAdRevenue( adjustAdRevenue );
//        } );

            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    Log.d("TAG", "NewEngine getNewNativeLarge mediation ApplovinMax " + ad.getNetworkName() + " " +
                            ad.getNetworkPlacement() + "  " + ad.getPlacement() + "  " + ad.getFormat() + "  " + ad.getWaterfall());

                    // Cleanup any pre-existing native ad to prevent memory leaks.
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }

                    // Save ad for cleanup.
                    nativeAd = ad;

                    // Add ad view to view.
                    listener.onAdLoaded(nativeAdView);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    listener.onAdFailed(AdsEnum.ADS_APPLOVIN, error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {
                }
            });
        } else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Ads Id Null");
        }*/

        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void showNativeRectangleAds(final Activity context, String id, final AppAdsListener listener) {
      /*  if (id != null && !id.equals("")) {
            id = id.trim();
            Log.d("TAG", "NewEngine showNativeRectangleAds mediation applovinMax id:-  "+id);

            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ad_applovin_native_rect)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();

            nativeAdView = new MaxNativeAdView(binder, context);

            nativeAdLoader = new MaxNativeAdLoader(id, context);

//        nativeAdLoader.setRevenueListener( ad -> {
//            AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue( AdjustConfig.AD_REVENUE_APPLOVIN_MAX );
//            adjustAdRevenue.setRevenue( ad.getRevenue(), "USD" );
//            adjustAdRevenue.setAdRevenueNetwork( ad.getNetworkName() );
//            adjustAdRevenue.setAdRevenueUnit( ad.getAdUnitId() );
//            adjustAdRevenue.setAdRevenuePlacement( ad.getPlacement() );
//
//            Adjust.trackAdRevenue( adjustAdRevenue );
//        } );

            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    Log.d("TAG", "NewEngine getNewNativeRect mediation ApplovinMax " + ad.getNetworkName() + " " +
                            ad.getNetworkPlacement() + "  " + ad.getPlacement() + "  " + ad.getFormat() + "  " + ad.getWaterfall());

                    // Cleanup any pre-existing native ad to prevent memory leaks.
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }

                    // Save ad for cleanup.
                    nativeAd = ad;

                    // Add ad view to view.
                    listener.onAdLoaded(nativeAdView);
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    listener.onAdFailed(AdsEnum.ADS_APPLOVIN, error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {
                }
            });
        }else {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Ads Id Null");

        }*/

        listener.onAdFailed(AdsEnum.ADS_APPLOVIN, "Mediation not used");
    }

    public void onApplovinMaxDestroy(){
        /*if(adView!=null){
            adView.destroy();
        }
        if(nativeAdView!=null){
            nativeAdView.destroyDrawingCache();
        }*/

    }
}
/*

class AppLovinMaxFullAdsListener implements MaxAdListener {
    private final MaxInterstitialAd maxInterstitialAd;
    private final AppFullAdsListener mAppAdListener;
    private int retryAttempt;
    private boolean isFromCache;
    private Activity activity;
    private String adsID;


    AppLovinMaxFullAdsListener(final Activity activity, String id,
                               MaxInterstitialAd maxInterstitialAd, AppFullAdsListener mAppAdListener,
                               boolean isFromCache) throws Exception {
        this.maxInterstitialAd = maxInterstitialAd;
        this.mAppAdListener = mAppAdListener;
        this.isFromCache = isFromCache;
        this.activity = activity;
        this.adsID = id;
        // Log.d("TAG", "NewEngine getNewCacheFullPageAd medaition applovinMax 333" + maxInterstitialAd + "  " + mAppAdListener);

        if (maxInterstitialAd == null || mAppAdListener == null) {
            throw new Exception("AdView and AppAdsListener cannot be null ");
        }
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        Log.d("TAG", "NewEngine getNewCacheFullPageAd medaition applovinMax " + ad.getNetworkName() + " " +
                ad.getNetworkPlacement() + "  " + ad.getPlacement() + "  " + ad.getFormat() + "  " + ad.getWaterfall());
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
        // Reset retry attempt
        retryAttempt = 0;
        mAppAdListener.onFullAdLoaded();
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

//        retryAttempt++;
//        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );
//
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                maxInterstitialAd.loadAd();
//            }
//        }, delayMillis );

        if (isFromCache) {
            mAppAdListener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, error.getMessage());
        }
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        // maxInterstitialAd.loadAd();

        if (isFromCache) {
            mAppAdListener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, error.getMessage());
        }
    }


}
*/
