package engine.app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

import app.pnd.adshandler.R;
import engine.app.enginev4.AdsEnum;
import engine.app.listener.AppAdsListener;
import engine.app.listener.AppFullAdsListener;


/**
 * Created by Meenu Singh on 06-09-2017.
 */
public class FbAdsProvider {
//    public static String NATIVE_MEDIUM_ID_STATIC = "ca-app-pub-3451337100490595/6299996657";
//    public static String NATIVE_LARGE_ID_STATIC = "ca-app-pub-3451337100490595/1977608266";

    private static FbAdsProvider fbAdsProvider;
    private AdView adView;
    private InterstitialAd interstitialAd;
    //InterstitialAd.InterstitialShowAdConfig showAdConfig;

    public static FbAdsProvider getFbObject() {
        if (fbAdsProvider == null) {
            synchronized (FbAdsProvider.class) {
                if (fbAdsProvider == null) {
                    fbAdsProvider = new FbAdsProvider();
                }
            }
        }
        return fbAdsProvider;
    }

    public void getFBBanner(final Context ctx, String ads_ID, AppAdsListener listener) {
        if (ads_ID != null && !ads_ID.equals("")) {
            ads_ID = ads_ID.trim();
            adView = new AdView(ctx, ads_ID, AdSize.BANNER_HEIGHT_50);

            try {
                //adView.setAdListener(new FbAdsListener(adView, listener));
                AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                        .withAdListener(new FbAdsListener(adView, listener))
                        .build();

                adView.loadAd(loadAdConfig);

            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_FACEBOOK, e.getMessage());
                e.printStackTrace();
            }
        } else {
            listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Banner Id null");
        }

//        listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Not used");
    }

    public void getFBBannerLarge(final Context ctx, String ads_ID, AppAdsListener listener) {
        if (ads_ID != null && !ads_ID.equals("")) {
            ads_ID = ads_ID.trim();
            adView = new AdView(ctx, ads_ID, AdSize.BANNER_HEIGHT_90);

            try {
                //adView.setAdListener(new FbAdsListener(adView, listener));
                AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                        .withAdListener(new FbAdsListener(adView, listener))
                        .build();

                adView.loadAd(loadAdConfig);

            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_FACEBOOK, e.getMessage());
                e.printStackTrace();
            }

        } else {
            listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "BannerLarge Id null");
        }

        //   listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Not used");
    }

    public void getFBBannerRectangle(final Context ctx, String ads_ID, AppAdsListener listener) {
        if (ads_ID != null && !ads_ID.equals("")) {
            ads_ID = ads_ID.trim();
            adView = new AdView(ctx, ads_ID, AdSize.RECTANGLE_HEIGHT_250);

            try {
                //adView.setAdListener(new FbAdsListener(adView, listener));
                AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                        .withAdListener(new FbAdsListener(adView, listener))
                        .build();

                adView.loadAd(loadAdConfig);

            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_FACEBOOK, e.getMessage());
                e.printStackTrace();
            }
        } else {
            listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "FBBannerRectangle Id null");
        }
        //   listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Not used");
    }


    public void getNativeAds(final Activity ctx, final boolean isnativelarge, String adsID, final AppAdsListener listener) {
        if (adsID != null && !adsID.equals("")) {
            adsID = adsID.trim();
            System.out.println("FbAdsProvider.getNativeAds " + adsID);
            //adsID = "2018009395097025_2022404887990809";
            final NativeAd nativeAd = new NativeAd(ctx, adsID);

            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError error) {
                    listener.onAdFailed(AdsEnum.ADS_FACEBOOK, error.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    try {
                        final LinearLayout nativeAdContainer = new LinearLayout(ctx);
                        LayoutInflater inflater = LayoutInflater.from(ctx);
                        if (isnativelarge) {
                            NativeAdLayout adView = (NativeAdLayout) inflater.inflate(R.layout.ad_fb_native_large,
                                    nativeAdContainer, false);
                            populateFacebookNative(nativeAd, adView, nativeAdContainer);

                            nativeAdContainer.addView(adView);
                            listener.onAdLoaded(nativeAdContainer);

                        } else {
                            NativeAdLayout adView = (NativeAdLayout) inflater.inflate(R.layout.ad_fb_native_medium,
                                    nativeAdContainer, false);
                            populateFacebookNative(nativeAd, adView, nativeAdContainer);

                            nativeAdContainer.addView(adView);
                            listener.onAdLoaded(nativeAdContainer);
                        }
                    } catch (Exception e) {
                        listener.onAdFailed(AdsEnum.ADS_FACEBOOK, e.getMessage());
                    }


                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };


            NativeAd.NativeLoadAdConfig loadAdConfig = nativeAd.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build();

            // Request an ad
            nativeAd.loadAd(loadAdConfig);
        } else {
            listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "NativeAds Id null");
        }
        //  listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Not used");
    }

    private void populateFacebookNative(NativeAd nativeAd, NativeAdLayout adView, LinearLayout nativeAdContainer) {
        // Create native UI using the ad metadata.
        NativeAdLayout nativeAdLayout = adView.findViewById(R.id.native_ad_unit);
        ImageView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);


        // Download and display the cover image.
        nativeAdMedia.setListener(new FbMediaViewListener());

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = adView.findViewById(R.id
                .ad_choices_container);
        AdOptionsView adChoicesView = new AdOptionsView(adView.getContext(), nativeAd, nativeAdLayout);
        if (adChoicesContainer != null) {
            if (adChoicesContainer.getChildCount() > 0) {
                adChoicesContainer.removeAllViews();
            }
            adChoicesContainer.addView(adChoicesView);

        }
        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                nativeAdContainer,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        // Optional: tag views
        NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON);
        NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE);
        NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY);
        NativeAdBase.NativeComponentTag.tagView(nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION);

    }


    public void getNativeAds_Grid(String adsID, final Activity ctx, final AppAdsListener listener) {
        if (adsID != null && !adsID.equals("")) {
            adsID = adsID.trim();
            final LinearLayout nativeAdContainer = new LinearLayout(ctx);
            //adsID="2018009395097025_2022404887990809";
            final NativeAd nativeAd = new NativeAd(ctx, adsID);

            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError error) {
                    listener.onAdFailed(AdsEnum.ADS_FACEBOOK, error.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    try {
                        // Add the Ad view into the ad container.
                        LayoutInflater inflater = LayoutInflater.from(ctx);
                        NativeAdLayout adView = (NativeAdLayout) inflater.inflate(R.layout.ad_fb_native_grid,
                                nativeAdContainer, false);

                        populateFacebookNativeGrid(nativeAd, adView, nativeAdContainer);
                        nativeAdContainer.addView(adView);
                        listener.onAdLoaded(nativeAdContainer);
//                        if (moreLayout != null) {
//                            moreLayout.setVisibility(View.GONE);
//                        }
                    } catch (Exception e) {
                        listener.onAdFailed(AdsEnum.ADS_FACEBOOK, e.getMessage());
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };

            NativeAd.NativeLoadAdConfig loadAdConfig = nativeAd.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build();

            // Request an ad
            nativeAd.loadAd(loadAdConfig);
            System.out.println("log check 1234 pos final");
        } else {
            listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "NativeAds_Grid Id null");
        }
        //     listener.onAdFailed(AdsEnum.ADS_FACEBOOK, "Not used");
    }

    private void populateFacebookNativeGrid(NativeAd nativeAd, NativeAdLayout adView, LinearLayout nativeAdContainer) {
        // Create native UI using the ad metadata.
        NativeAdLayout nativeAdLayout = adView.findViewById(R.id.native_ad_unit);
        ImageView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Download and display the cover image.
        nativeAdMedia.setListener(new FbMediaViewListener());

        // Add the AdChoices icon
        try {
            LinearLayout adChoicesContainer = adView.findViewById(R.id
                    .ad_choices_container);
            AdOptionsView adChoicesView = new AdOptionsView(adView.getContext(), nativeAd, nativeAdLayout);
            if (adChoicesContainer != null) {
                if (adChoicesContainer.getChildCount() > 0) {
                    adChoicesContainer.removeAllViews();
                }
                adChoicesContainer.addView(adChoicesView);
            }

        } catch (Exception e) {
            System.out.println("log check 1234 pos exc");
            System.out.println(" Native Error error in adchoice container");
        }


        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        //sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                nativeAdContainer,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        // Optional: tag views
        NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON);
        NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE);
        NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY);
        NativeAdBase.NativeComponentTag.tagView(nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION);

    }


    private static class FbMediaViewListener implements MediaViewListener {

        @Override
        public void onPlay(MediaView mediaView) {

        }

        @Override
        public void onVolumeChange(MediaView mediaView, float v) {

        }

        @Override
        public void onPause(MediaView mediaView) {

        }

        @Override
        public void onComplete(MediaView mediaView) {

        }

        @Override
        public void onEnterFullscreen(MediaView mediaView) {

        }

        @Override
        public void onExitFullscreen(MediaView mediaView) {

        }

        @Override
        public void onFullscreenBackground(MediaView mediaView) {

        }

        @Override
        public void onFullscreenForeground(MediaView mediaView) {

        }
    }


    public void loadFBFullAds(String id, final Activity activity, final AppFullAdsListener listener, final boolean isFromCache) {
        if (id != null && !id.equals("")) {
            id = id.trim();
            interstitialAd = new InterstitialAd(activity, id);
            final String finalId = id;
            InterstitialAdListener adListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    System.out.println("Hello showFullAdsOnExit fullads checked 0010");
                    // Interstitial displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    System.out.println("Hello showFullAdsOnExit fullads checked 009");
                    listener.onFullAdClosed();
                    //loadFBFullAds(finalId, activity, listener, false);
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (isFromCache)
                        listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (isFromCache)
                        listener.onFullAdLoaded();
                    // Show the ad when it's done loading.
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            };


            InterstitialAd.InterstitialLoadAdConfig loadAdConfig = interstitialAd.buildLoadAdConfig()
                    .withAdListener(adListener)
                    .build();

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            if (interstitialAd != null && !interstitialAd.isAdLoaded()) {
                System.out.println("FbAdsProvider.loadFBFullAds");
                // Toast.makeText(activity,"Requesting new full ads "+interstitialAd.isAdLoaded(),Toast.LENGTH_LONG).show();
                try {
                    interstitialAd.loadAd(loadAdConfig);
                } catch (Exception e) {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, e.getMessage());

                }
            }
        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, "loadFBFullAds Id null");
        }
        //  listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, "Not used");
    }


    public void showFBFullAds(String id, final Activity activity, final AppFullAdsListener listener,boolean isFromSplash) {
        if (id != null && !id.equals("")) {
            id = id.trim();
            System.out.println("FbAdsProvider.showFBFullAds " + id + activity.getClass());

            if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                final String finalId = id;
                InterstitialAdListener adListener = new InterstitialAdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        System.out.println("FbAdsProvider.showFBFullAds onError"+adError );

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        System.out.println("FbAdsProvider.showFBFullAds onAdLoaded" );

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        System.out.println("FbAdsProvider.showFBFullAds Displayed" );

                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        System.out.println("FbAdsProvider.showFBFullAds Dismissed" );

                        if(!isFromSplash) {
                            loadFBFullAds(finalId, activity, listener, false);
                        }
                        listener.onFullAdClosed();

                    }
                };

                try {
                    InterstitialAd.InterstitialLoadAdConfig loadAdConfig =
                            interstitialAd.buildLoadAdConfig()
                                    .withAdListener(adListener)
                                    .build();

                    interstitialAd.show();
                    listener.onFullAdLoaded();
                } catch (Exception e) {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, e.getMessage());
                }

            } else {
                if(!isFromSplash) {
                    loadFBFullAds(id, activity, listener, false);
                }
                listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, "FbAdsProvider showFBFullAds False");

            }
        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, "showFBFullAds Id null");
        }
        //   listener.onFullAdFailed(AdsEnum.FULL_ADS_FACEBOOK, "Not used");
    }

    public void FbAdsDestroy() {
        try {
            if (adView != null) {
                adView.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
