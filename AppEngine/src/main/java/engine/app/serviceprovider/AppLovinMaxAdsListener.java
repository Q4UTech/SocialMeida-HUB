//package engine.app.serviceprovider;
//
//import android.util.Log;
//
//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdViewAdListener;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.ads.MaxAdView;
//
//import engine.app.enginev4.AdsEnum;
//import engine.app.listener.AppAdsListener;
//
//public class AppLovinMaxAdsListener implements MaxAdViewAdListener {
//
//    private final MaxAdView mAdView;
//    private final AppAdsListener mAppAdListener;
//
//
//    AppLovinMaxAdsListener(MaxAdView mAdView, AppAdsListener mOnAppAdListener) throws Exception {
//        this.mAdView = mAdView;
//        this.mAppAdListener = mOnAppAdListener;
//        Log.d("TAG", "NewEngine getNewBannerHeader mediation applovinMax 333 " + mAdView+"  "+mOnAppAdListener);
//
//        if (mAdView == null || mOnAppAdListener == null) {
//            throw new Exception("AdView and AppAdsListener mediation cannot be null ");
//        }
//    }
//
//    @Override
//    public void onAdExpanded(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdCollapsed(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdLoaded(MaxAd ad) {
//        Log.d("TAG", "NewEngine getNewBannerHeader mediation applovinMax onAdLoaded   " + ad.getNetworkName()+" "+
//                ad.getNetworkPlacement()+"  "+ad.getPlacement()+"  "+ad.getFormat()+"  "+ad.getWaterfall());
//        mAppAdListener.onAdLoaded(mAdView);
//
//    }
//
//    @Override
//    public void onAdDisplayed(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdHidden(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdClicked(MaxAd ad) {
//
//    }
//
//    @Override
//    public void onAdLoadFailed(String adUnitId, MaxError error) {
//        Log.d("TAG", "NewEngine getNewBannerHeader mediation applovinMax onAdLoadFailed" + error.getMessage());
//        mAppAdListener.onAdFailed(AdsEnum.ADS_APPLOVIN, error.getMessage());
//        onDestroyView();
//    }
//
//    @Override
//    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//        Log.d("TAG", "NewEngine getNewBannerHeader mediation applovinMax onAdDisplayFailed" + error.getMessage());
//        mAppAdListener.onAdFailed(AdsEnum.ADS_APPLOVIN, error.getMessage());
//        onDestroyView();
//
//    }
//
//    private void onDestroyView(){
//        if(mAdView!=null){
//            mAdView.stopAutoRefresh();
//            mAdView.destroy();
//        }
//    }
//}
