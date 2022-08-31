package com.jatpack.socialmediahub.ui.socialmediadownloader.services;

import static com.example.whatsdelete.constants.Constants.DOWNLOADED_WITH_MULTIPLE;
import static com.example.whatsdelete.constants.Constants.DOWNLOADED_WITH_SINGLE;
import static com.example.whatsdelete.constants.Constants.GO_BUTTON_CLICK;
import static com.example.whatsdelete.constants.Constants.PASTE_MEDIA_URL;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.example.whatsdelete.constants.Constants;
import com.example.whatsdelete.utils.AppUtils;
import com.example.whatsdelete.utils.Prefs;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jatpack.socialmediahub.R;
import com.jatpack.socialmediahub.ui.socialmediadownloader.ecrypt.MCrypt;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ClipBoardService extends Service {
    private final int NOTIFICATION_ID = 10;
    private Prefs preference;
    public long progressPercent;
    private boolean goButtonClick;
    private String pin_url, pin_video_image;
    private Notification mRunningNotification;
    private static final String PRIMARY_CHANNEL = "Social Media Downloader";
    private static Set<DownloadingMediaInfo> mCurrentDownLoadingSet;
    private static boolean pinvideo_boolean = false;
    private static DownloadingListener mDownloadingListener;
    private PendingIntent pIntent;
    private static int downloadingId;
    Pattern pattern = Pattern.compile("window\\.data \\s*=\\s*(\\{.+?\\});");


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            super.startForeground(NOTIFICATION_ID, new Notification());
//        }
        preference = new Prefs(getApplicationContext());
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        String data = intent.getStringExtra(PASTE_MEDIA_URL);
        goButtonClick = intent.getExtras().getBoolean(GO_BUTTON_CLICK);
        pin_url = intent.getStringExtra(Constants.PINTEREST_URL);
        pin_video_image = intent.getStringExtra(Constants.PINTEREST_VIDEO_IMAGE);

        Log.d("ClipBoardService", "Hello onStartCommand 02" + " " + data);

        new DownloadingAsynTask(this)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        return START_REDELIVER_INTENT;
    }


    /**
     * @param url
     * @param pathfile
     * @param pathextension
     */

    private RemoteViews remoteViews;
    private String totalSize;

    private void downloadMedia(String url, String pathfile, String pathextension) {
        Log.d("ClipBoardService", "Hello downloadMedia ");

        if (mCurrentDownLoadingSet == null) {
            mCurrentDownLoadingSet = new HashSet<>();
        }

        DownloadingMediaInfo downloadingMediaInfo = new DownloadingMediaInfo();

        downloadingMediaInfo.setUrl(url);
        downloadingMediaInfo.setDownloadedFileName(pathextension);

        if (mCurrentDownLoadingSet.add(downloadingMediaInfo)) {
            Log.d("ClipBoardService", "Hello downloadMedia data" + " " + mCurrentDownLoadingSet.size());
            DownloadRequest downloadRequest = PRDownloader.download(url, pathfile, pathextension).build();

            long date = System.currentTimeMillis();
            downloadingMediaInfo.setDate(date);

            downloadingMediaInfo.setIspause(false);
            PRDownloaderProgress downloaderProgress = new PRDownloaderProgress(this, downloadingMediaInfo);
            PRDownloaderComplete downloaderComplete = new PRDownloaderComplete(this, downloadingMediaInfo);
            PRDownloaderCancel downloaderCancel = new PRDownloaderCancel(this);


            downloadRequest.setOnCancelListener(downloaderCancel);
            downloadRequest.setOnProgressListener(downloaderProgress);


            //STRAT DOWNLOADING ID
            downloadingId = downloadRequest.start(downloaderComplete);
            downloadingMediaInfo.setDownloadingId(downloadingId);


        } else {
            downloadingMediaInfo = null;
            Toast.makeText(getApplicationContext(), "This media file is already running!!",
                    Toast.LENGTH_SHORT).show();
        }

    }


    private static class PRDownloaderProgress implements OnProgressListener {
        private final ClipBoardService mClipBoardService;
        private final DownloadingMediaInfo downloadingMediaInfo;


        private PRDownloaderProgress(ClipBoardService mClipBoardService, DownloadingMediaInfo downloadingMediaInfo) {
            this.mClipBoardService = mClipBoardService;
            this.downloadingMediaInfo = downloadingMediaInfo;
        }

        @Override
        public void onProgress(Progress progress) {
            int progressPercent = ((int) ((progress.currentBytes * 100) / progress.totalBytes));
            Log.d("ClipBoardService", "Hello onProgress my slow test " + " " + progressPercent + "   " + progress.currentBytes + "  " +
                    progress.totalBytes);

            if (progressPercent < 5) {
                Log.d("PRDownloaderProgress", "Hello onProgress asdfa" + " " + progress.totalBytes);
                downloadingMediaInfo.setTotalbitAfterDownload(progress.totalBytes);
            }

            if (progressPercent % 20 == 0) {
                mClipBoardService.updateRunningNotification(progressPercent, progress.totalBytes);
                Log.d("PRDownloaderProgress", "Hello onProgress my progress is here " + " " + progressPercent);
            }

            if (ClipBoardService.mDownloadingListener != null) {
                downloadingMediaInfo.setProgress(progressPercent);
                downloadingMediaInfo.setTotalbitService(progress.totalBytes);
                downloadingMediaInfo.setCurrentbitService(progress.currentBytes);

                ClipBoardService.mDownloadingListener.onProgress(downloadingMediaInfo);
            }


        }
    }


    private static class PRDownloaderComplete implements OnDownloadListener {
        private final ClipBoardService mClipBoardService;
        private final DownloadingMediaInfo downloadingMediaInfo;

        private PRDownloaderComplete(ClipBoardService mClipBoardService, DownloadingMediaInfo downloadingMediaInfo) {
            this.mClipBoardService = mClipBoardService;
            this.downloadingMediaInfo = downloadingMediaInfo;

        }

        @Override
        public void onDownloadComplete() {
            pinvideo_boolean = false;
            Toast.makeText(mClipBoardService.getApplicationContext(), mClipBoardService.getApplicationContext().getResources().getString(R.string.download_completed), Toast.LENGTH_LONG).show();
            downloadingMediaInfo.setCompleted(true);
//
//            new SaveDownloadInfoAsyncTask(new WeakReference<ClipBoardService>(mClipBoardService), downloadingMediaInfo)
//                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        }


        @Override
        public void onError(Error error) {

            Toast.makeText(mClipBoardService.getApplicationContext(), "Error please try again", Toast.LENGTH_LONG).show();

        }
    }

    private static class PRDownloaderCancel implements OnCancelListener {
        private final ClipBoardService mClipBoardService;


        private PRDownloaderCancel(ClipBoardService mClipBoardService) {
            this.mClipBoardService = mClipBoardService;
        }

        @Override
        public void onCancel() {
            mClipBoardService.mCurrentDownLoadingSet.clear();
            mClipBoardService.stopSelf();
        }
    }

    public static class DownloadingAsynTask extends AsyncTask<String, String, Boolean> {
        private final ClipBoardService clipBoardService;
        private final Prefs preference;
        private String downloadType;

        public DownloadingAsynTask(ClipBoardService clipBoardService) {
            this.clipBoardService = clipBoardService;
            preference = clipBoardService.preference;

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String pasteData = strings[0];

            Log.d("DownloadingAsynTask", "Hello doInBackground test" + " " + pasteData);

            Set<String> setUrl = null;
            if (pasteData.contains("https://www.instagram.com/reel")) {
                downloadType = Constants.DOWNLOADED_WITH_INSTA_REELS;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("https://www.instagram.com")) {
                downloadType = Constants.DOWNLOADED_WITH_INSTA;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("https://www.facebook.com") ||
                    pasteData.contains("https://m.facebook.com")) {
                downloadType = Constants.DOWNLOADED_WITH_FACEBOOK;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("https://like.video") || pasteData.contains("https://l.likee.video")
                    || pasteData.contains("https://share.like.video") || pasteData.contains("https://mobile.like-video")
                    || pasteData.contains("https://like-video")) {
                downloadType = Constants.DOWNLOADED_WITH_LIKE;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("https://pin.it") || pasteData.contains("https://in.pinterest")
                    || pasteData.contains("https://www.pinterest")) {
                downloadType = Constants.DOWNLOADED_WITH_PINTEREST;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("tumblr.com/post")) {
                downloadType = Constants.DOWNLOADED_WITH_TUMBLER;
                setUrl = downloadInstaGram(pasteData, downloadType);
            } else if (pasteData.contains("tiktok.com")) {
                downloadType = Constants.DOWNLOADED_WITH_TIKTOK;
                setUrl = downloadInstaGram(pasteData, downloadType);
            }

            if (setUrl != null && setUrl.size() > 0) {
                return onPost(setUrl);
            }


            return false;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String url = values[0];
            String pathfile = values[1];
            String pathextension = values[2];

            clipBoardService.downloadMedia(url, pathfile, pathextension);
        }

        @Override
        protected void onPostExecute(Boolean isUrlFound) {
            super.onPostExecute(isUrlFound);
            Log.d("DownloadingAsynTask", "Test onPostExecute " + ClipBoardService.mDownloadingListener);
            if (ClipBoardService.mDownloadingListener != null) {
                ClipBoardService.mDownloadingListener.dismissProgressDialog();
            }

            if (isUrlFound) {
                if (!clipBoardService.goButtonClick) {
                    // clipBoardService.startServiceinapp(clipBoardService.getApplicationContext(), ChatHeadService.class);
                }

            } else {

                Log.d("DownloadingAsynTask", "Hello onPostExecute " + " " + isUrlFound);

                Toast.makeText(clipBoardService.getApplicationContext(),
                        "Do not download private media ", Toast.LENGTH_SHORT)
                        .show();

            }
        }

        String link;

        private String getParseJson(String json) throws IOException {

            Log.d("DownloadingAsynTask", "Hello getParseJson ");
            JsonFactory factory = new JsonFactory();
            String value = null;
            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode = mapper.readTree(json);

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {

                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                Log.d("DownloadingAsynTask", "Hello getParseJson oops" + " " + field.getKey());
//            String paramName = field.getKey();
                JsonNode jsonNode = field.getValue();

                switch (jsonNode.getNodeType()) {


                    case OBJECT:
                        getParseJson(jsonNode.toString());
                        break;
                    case STRING:
                        value = jsonNode.asText();
                        Log.d("DownloadingAsynTask", "Hello getParseJson oioioioio" + " " + jsonNode.textValue());
                        if (value.endsWith(".mp4")) {
                            //issues create when download image and video both
                            pinvideo_boolean = true;

                            Log.d("DownloadingAsynTask", "Hello getParseJson f02 outside" + " " + value);
                            link = value;

                        }
                        if (value.endsWith(".jpg") && value.contains("https://i.pinimg.com/736x/") && !pinvideo_boolean) {
                            link = value;
                            Log.d("DownloadingAsynTask", "Hello getParseJson jpg outside" + " " + value);
                        }
                        break;
                }

            }
            return link;

        }

        private String reelsURL = "";

        private Set<String> downloadInstaGram(String pasteData, String downloadType) {
//            boolean isvideo = true;
            Set<String> urlSet = null;
            Log.d("DownloadingAsynTask", "Hello downloadInstaGram copy link" + " " + pasteData);

            try {
                Document document = Jsoup.connect(pasteData).get();

                if (Constants.DOWNLOADED_WITH_LIKE.equals(downloadType)) {
//                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like");
//                    Element e = document.getElementById("videoPlayer");
//                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like 001"+" "+e.getAllElements());
//                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like 002"+" "+e.attr("src"));
//
//                    urlSet = new HashSet<String>();
//                    urlSet.add(e.attr("src"));

                    String JSONData = "";
                    Matcher matcher = clipBoardService.pattern.matcher(document.toString());
                    while (matcher.find()) {
                        JSONData = matcher.group().replaceFirst("window.data = ", "").replace(";", "");
                    }
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like 002 aa" + " ");

//
                    JSONObject jsonObject = new JSONObject(JSONData);
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like 002 bb" + " ");

                    String VideoUrl = jsonObject.getString("video_url").replace("_4", "");
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram like 002" + " " + VideoUrl);
                    urlSet = new HashSet<String>();
                    urlSet.add(VideoUrl);


                }

                if (Constants.DOWNLOADED_WITH_TIKTOK.equals(downloadType)) {
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram with tiktok");
                    if (urlSet == null) {
                        urlSet = new HashSet<String>();
                    }
                    try {
                        String URL = document.select("script[id=\"videoObject\"]").last().html();
                        String URL1 = document.select("script[id=\"__NEXT_DATA__\"]").last().html();

                        if (!URL.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(URL);
                                new JSONObject(URL1);
                                String VideoUrl = jsonObject.getString("contentUrl");
                                Log.d("DownloadingAsynTask", "Hello downloadInstaGram tiktok video url" + " " +
                                        VideoUrl);
                                urlSet.add(VideoUrl);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                    } catch (NullPointerException e) {
                        Log.d("DownloadingAsynTask", "Hello downloadInstaGram exertrtt" + " " + e.getMessage());
                        e.printStackTrace();
                    }


                }

                if (Constants.DOWNLOADED_WITH_PINTEREST.equals(downloadType)) {
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram pin");

                    if (urlSet == null) {
                        urlSet = new HashSet<String>();
                    }
                    urlSet.add(clipBoardService.pin_url);


//                    String json = document.getElementById("initial-state").data();
//                    String link = getParseJson(json);
//
//                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram mp4 value" + " " + link);
//
//                    if (urlSet == null) {
//                        urlSet = new HashSet<String>();
//                    }
//                    if (link != null) {
//                        Log.d("DownloadingAsynTask", "Hello downloadInstaGram 004");
//                        urlSet.add(link);
//                    }
//                    if (!pinvideo_boolean) {
//                        Log.d("DownloadingAsynTask", "Hello downloadInstaGram pin jpg");
//                        if (urlSet == null) {
//                            urlSet = new HashSet<String>();
//                        }
//                        urlSet.add(link);
//                    }

                }

                if ((Constants.DOWNLOADED_WITH_TUMBLER.equals(downloadType))) {
                    Elements links = document.select("source");
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram 001" + " " + links);
                    for (Element link : links) {
                        Log.d("DownloadingAsynTask", "Hello downloadInstaGram 002" + " " + link);
                        String attrnew = link.attr("src");
                        Log.d("DownloadingAsynTask", "Hello downloadInstaGram 003" + " " + attrnew);
                        if (urlSet == null) {
                            urlSet = new HashSet<String>();
                        }
                        if (attrnew != null) {
                            Log.d("DownloadingAsynTask", "Hello downloadInstaGram 004");
                            urlSet.add(attrnew);
                        }


                    }
                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram 005");
                    Elements metaTags2 = document.getElementsByTag("meta");
                    for (Element metaTag : metaTags2) {
                        if (metaTag.attr("property").equalsIgnoreCase("og:image")) {
                            if (urlSet == null) {
                                urlSet = new HashSet<String>();
                            }
                            urlSet.add(metaTag.attr("content"));

                        }


                    }
                }

                if ((Constants.DOWNLOADED_WITH_INSTA_REELS.equals(downloadType))) {
                    Elements metaTags = document.getElementsByTag("meta");

                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram reels"+metaTags);

                    for (Element metaTag : metaTags) {
                        if (metaTag.attr("property").equalsIgnoreCase("og:image")) {
                            if (urlSet == null) {
                                urlSet = new HashSet<String>();
                            }
                            urlSet.add(metaTag.attr("content"));
                            Log.d("DownloadingAsynTask", ": "+metaTag.attr("content"));

                        } else if (metaTag.attr("property").equalsIgnoreCase("og:url")) {
                            reelsURL = metaTag.attr("content");
                        }
                    }


                } else {
                    boolean isvideo = true;
                    Elements metaTags = document.getElementsByTag("meta");
                    for (Element metaTag : metaTags) {
                        if (metaTag.attr("property").equalsIgnoreCase("og:type")) {
                            if (metaTag.attr("content").equalsIgnoreCase("video")) {
                                isvideo = true;
                            } else if (metaTag.attr("content").equalsIgnoreCase("instapp:photo")) {
                                isvideo = false;
                            }
                        }
                    }

                    Log.d("DownloadingAsynTask", "Hello downloadInstaGram else");
//                    Elements metaTags = document.getElementsByTag("meta");

                    for (Element metaTag : metaTags) {
                        if (metaTag.attr("property").equalsIgnoreCase("og:image") && !isvideo) {
                            if (urlSet == null) {
                                urlSet = new HashSet<String>();
                            }
                            urlSet.add(metaTag.attr("content"));
//                        DownloaderLink(arrayList.get(0), pathfile, pathextension);

                        } else if (metaTag.attr("property").equalsIgnoreCase("og:video") && isvideo) {
                            if (urlSet == null) {
                                urlSet = new HashSet<String>();
                            }
                            urlSet.add(metaTag.attr("content"));
                            //  DownloaderLink(arraylistvideo.get(0), pathfile, pathextensionmp4);

                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return urlSet;
        }


        private boolean onPost(Set<String> s) {

            if (s == null || s.size() == 0 || downloadType == null) {
                return false;
            }

            boolean hasDownloadingURL = false;

            for (String url : s) {
                String fileName = null;

//                String fileName = ""+System.currentTimeMillis() ;

                if (url != null) {
                    switch (downloadType) {
                        case Constants.DOWNLOADED_WITH_INSTA_REELS:

                            if (url.contains(".jpg") || url.contains(".jpeg")) {

                                //Saving file as URL encrypt from and when get url we decrypt same..
                                Log.d("DownloadingAsynTask", "downloadInstaGram: INSTA_REELS " + reelsURL);
                                try {
                                    String encrypt = MCrypt.bytesToHex(new MCrypt().encrypt(reelsURL));
                                    //Don't remove this line............
                                    fileName = "Reels" + encrypt + "Reels" + Constants.INSTA_JPG;

                                    Log.d("DownloadingAsynTask", "downloadInstaGram: INSTA_REELS encrypt " + encrypt);

                                    String decrypt = new String(new MCrypt().decrypt(encrypt));

                                    Log.d("DownloadingAsynTask", "downloadInstaGram: INSTA_REELS decrypt " + decrypt);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case Constants.DOWNLOADED_WITH_INSTA:

                            if (url.contains(".mp4")) {

                                fileName = System.currentTimeMillis() + "" + Constants.INSTA_MP4;

                            } else if (url.contains(".jpg") || url.contains(".jpeg")) {

                                fileName = System.currentTimeMillis() + "" + Constants.INSTA_JPG;
                            }

                            break;

                        case Constants.DOWNLOADED_WITH_TIKTOK:

                            fileName = System.currentTimeMillis() + "" + Constants.TIKTOK_MP4;

                            break;

                        case Constants.DOWNLOADED_WITH_FACEBOOK:


                            if (url.contains(".mp4")) {
                                fileName = System.currentTimeMillis() + "" + Constants.FB_MP4;
                            } else if (url.contains(".jpg") || url.contains(".jpeg")) {
                                fileName = System.currentTimeMillis() + "" + Constants.FB_JPG;
                            }


                            break;
                        case Constants.DOWNLOADED_WITH_LIKE:

                            if (url.contains(".mp4")) {
                                fileName = System.currentTimeMillis() + "" + Constants.LIKE_MP4;
                            }

                            break;


                        case Constants.DOWNLOADED_WITH_PINTEREST:

                            if (url.contains(".mp4")) {
                                fileName = System.currentTimeMillis() + "" + Constants.PIN_MP4;
                            } else if (url.contains(".jpg")) {
                                fileName = System.currentTimeMillis() + "" + Constants.PIN_JPG;
                            }

                            break;

                        case Constants.DOWNLOADED_WITH_TUMBLER:

                            if (url.contains(".mp4")) {
                                fileName = System.currentTimeMillis() + "" + Constants.TUMBLR_MP4;
                            } else if (url.contains(".jpg")) {
                                fileName = System.currentTimeMillis() + "" + Constants.TUMBLR_JPG;
                            }
                            break;
                    }


                }

                if (fileName != null) {
                    hasDownloadingURL = true;
                    publishProgress(url, AppUtils.Companion.createAppDir(clipBoardService.getApplicationContext()), fileName);

                }

            }

            return hasDownloadingURL;
        }

    }


    private void getsavedpath(ClipBoardService mContext, String filepath, String pathextension, String multipleDownload, int size) {
        Log.d("DownloadingAsynTask", "Hello getsavedpath nkjjKSGLjakl" + " " + filepath);
        File f = new File(filepath, pathextension);
        if (f.getPath().endsWith(".mp4") && multipleDownload.equals(Constants.DOWNLOADED_WITH_SINGLE)) {
            if (mContext != null) {
//                mContext.startActivity(DialogHandler.Companion.getNewIntent(ClipBoardService.this, goButtonClick, false,
//                        true, f.getPath(), getResources().getString(R.string.download_completed), 0, String.valueOf(f.getAbsoluteFile().lastModified())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Intent intent = new Intent(mContext, TransparentActivityShowPrompt.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_MP4, AppUtils.DOWNLOADED_WITH_MP4);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_MP4_BTN, AppUtils.DOWNLOADED_WITH_MP4_BTN);
//                intent.putExtra(AppUtils.MP4_PATH_FILE, f.getPath());
//                intent.putExtra(AppUtils.MP4_PATH_FILE_DATE, String.valueOf(f.getAbsoluteFile().lastModified()));
//                intent.putExtra(AppUtils.FROM_APP_SERVICE_DOWNLOAD_COMPLETE_PROMPT, goButtonClick);
//                mContext.startActivity(intent);
                mCurrentDownLoadingSet.clear();
            }

        } else if (f.getPath().endsWith(".jpg") && multipleDownload.equals(Constants.DOWNLOADED_WITH_SINGLE)) {
            if (mContext != null) {
//                mContext.startActivity(DialogHandler.Companion.getNewIntent(ClipBoardService.this, goButtonClick, true,
//                        true, f.getPath(), getResources().getString(R.string.download_completed), 0, String.valueOf(f.getAbsoluteFile().lastModified())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Intent intent = new Intent(mContext, TransparentActivityShowPrompt.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_JPG, AppUtils.DOWNLOADED_WITH_JPG);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_JPG_BTN, AppUtils.DOWNLOADED_WITH_JPG_BTN);
//                intent.putExtra(AppUtils.JPG_PATH_FILE, f.getPath());
//                intent.putExtra(AppUtils.JPG_PATH_FILE_DATE, String.valueOf(f.getAbsoluteFile().lastModified()));
//                intent.putExtra(AppUtils.FROM_APP_SERVICE_DOWNLOAD_COMPLETE_PROMPT, goButtonClick);
//
//                mContext.startActivity(intent);
                mCurrentDownLoadingSet.clear();


            }
        } else if (multipleDownload.equals(Constants.DOWNLOADED_WITH_MULTIPLE)) {
            if (mContext != null) {
//                mContext.startActivity(DialogHandler.Companion.getNewIntent(ClipBoardService.this, goButtonClick, false,
//                        true, f.getPath(), getResources().getString(R.string.download_completed), 0, String.valueOf(f.getAbsoluteFile().lastModified())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Intent intent = new Intent(mContext, TransparentActivityShowPrompt.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_MULTIPLE_TXT, AppUtils.DOWNLOADED_WITH_MULTIPLE_TXT);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_MULTIPLE_TXT_BTN, AppUtils.DOWNLOADED_WITH_MULTIPLE_TXT_BTN);
//                intent.putExtra(AppUtils.DOWNLOADED_WITH_MULTIPLE_SIZE, size);
//                intent.putExtra(AppUtils.JPG_PATH_FILE_DATE, String.valueOf(f.getAbsoluteFile().lastModified()));
//                intent.putExtra(AppUtils.FROM_APP_SERVICE_DOWNLOAD_COMPLETE_PROMPT, goButtonClick);
//
//                mContext.startActivity(intent);
                mCurrentDownLoadingSet.clear();

            }
        }

    }

//    private List<DownloadingMediaInfo> saveDownloadInfo(Context context, DownloadingMediaInfo downloadingMediaInfo) {
//        /*.....for saving data in preference....*/
//        List<DownloadingMediaInfo> downloadListComplete = AppPreference.getList(context,
//                AppPreference.PROGRESS_KEY_PREF, DownloadingMediaInfo.class);
//
//        if (downloadListComplete == null) {
//            downloadListComplete = new ArrayList<>();
//        }
//
//        File file = new File(AppUtils.Companion.getAppMainDir(),
//                downloadingMediaInfo.getDownloadedFileName());
//
//        downloadingMediaInfo.setDownloadFilePath(file.getPath());
//        downloadListComplete.add(downloadingMediaInfo);
//
//        AppPreference.saveList(context, AppPreference.PROGRESS_KEY_PREF, downloadListComplete);
//
//        Collections.sort(downloadListComplete, new Comparator<DownloadingMediaInfo>() {
//            @Override
//            public int compare(DownloadingMediaInfo list1, DownloadingMediaInfo list2) {
//
//                if (list1 == null || list2 == null) {
//                    return 0;
//                }
//
//                return Long.compare(list2.getDate(), list1.getDate());
//
//            }
//        });
//
//        return downloadListComplete;
//    }

//    private static class SaveDownloadInfoAsyncTask extends AsyncTask<String, Void, List<DownloadingMediaInfo>> {
//
//        private final WeakReference<ClipBoardService> weakReference;
//        private final DownloadingMediaInfo mediaInfoToSave;
//
//        private SaveDownloadInfoAsyncTask(WeakReference<ClipBoardService> weakReference, DownloadingMediaInfo mediaInfoToSave) {
//            this.weakReference = weakReference;
//            this.mediaInfoToSave = mediaInfoToSave;
//        }
//
//
//        @Override
//        protected List<DownloadingMediaInfo> doInBackground(String... values) {
//            ClipBoardService mClipBoardService = weakReference.get();
//            Context context = mClipBoardService.getApplicationContext();
//            return mClipBoardService.saveDownloadInfo(context, mediaInfoToSave);
//
//        }
//
//
//        @Override
//        protected void onPostExecute(List<DownloadingMediaInfo> downloadedList) {
//            super.onPostExecute(downloadedList);
//            // Log.d("PasteLinkFragment", "123123 downloadlist123123..." + " " + downloadedList.size());
//
//            if (weakReference.get() != null) {
//                ClipBoardService mClipBoardService = weakReference.get();
//
//                if (ClipBoardService.mDownloadingListener != null) {
//                    ClipBoardService.mDownloadingListener.onComplete(downloadedList);
//                }
//
//
//                for (DownloadingMediaInfo downloadingMediaInfo : mClipBoardService.mCurrentDownLoadingSet) {
//                    if (!downloadingMediaInfo.isCompleted()) {
//                        return;
//                    }
//                }
//
//                final int size = mClipBoardService.mCurrentDownLoadingSet.size();
//
//                if (size > 1) {
//                    //Multiple
//
//
//                    if (mClipBoardService.preference.getKeyAutoDownload()) {
//                        mClipBoardService.getsavedpath(mClipBoardService, AppUtils.Companion.getAppMainDir(),
//                                mediaInfoToSave.getDownloadedFileName(),
//                                DOWNLOADED_WITH_MULTIPLE,
//                                size);
//                    }
//                } else {
//                    //Single
//
//                    if (mClipBoardService.preference.getKeyAutoDownload()) {
//                        mClipBoardService.getsavedpath(mClipBoardService, AppUtils.Companion.getAppMainDir(),
//                                mediaInfoToSave.getDownloadedFileName(),
//                                DOWNLOADED_WITH_SINGLE,
//                                size);
//                    }
//                }
//                mClipBoardService.mCurrentDownLoadingSet.clear();
//                mClipBoardService.stopSelf();
//
//            }
//        }
//    }

    public static void setDownloadingListener(DownloadingListener mDownloadingListener) {
        ClipBoardService.mDownloadingListener = mDownloadingListener;
    }


    public static void removeDownloadingItem(Context context, DownloadingMediaInfo downloadingMediaInfo) {
        if (mCurrentDownLoadingSet != null) {
            mCurrentDownLoadingSet.remove(downloadingMediaInfo);


            if (mCurrentDownLoadingSet.size() == 0) {
                context.stopService(new Intent(context, ClipBoardService.class));
            }
        }
    }

    private void updateRunningNotification(int progressPercent, long totalByte) {


        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (nm != null) {
            Log.d("PRDownloaderProgress", "Hello onProgress noti 3 not null");
            createNotificationChannelID(nm);
            notifyRunningNotification(nm, progressPercent, totalByte);
        }
    }

    private Notification initRunningNotification() {
        if (remoteViews == null) {
            Log.d("ClipBoardService", "Hello initRunningNotification 01" + " ");
            remoteViews = new RemoteViews(getPackageName(),
                    R.layout.progress_notification);

        }


        if (mRunningNotification == null) {

            Notification.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(this, PRIMARY_CHANNEL);
            } else {
                builder = new Notification.Builder(this);
            }

            pIntent = PendingIntent.getActivity(this, 0, new Intent(),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

            // Set Icon


            builder.setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? R.drawable.ic_notifications_black_24dp : R.mipmap.ic_launcher)
                    .setContentIntent(pIntent)
                    .setOnlyAlertOnce(true)
                    // Set RemoteViews into Notification
                    .setContent(remoteViews);
            //  .setPriority(NotificationCompat.PRIORITY_LOW);


            mRunningNotification = builder.build();
        }

        return mRunningNotification;
    }

    private void notifyRunningNotification(NotificationManager nm, int progressPercent, long totalBytes) {
        final int size = mCurrentDownLoadingSet.size();
        try {
            Notification notification = initRunningNotification();
            totalSize = AppUtils.Companion.getFileSize(totalBytes);


            Intent intent2 = new Intent(getApplicationContext(), DownloadCancelReceiver.class);
            intent2.setAction("stop_btn");
            intent2.putExtra("TYPE", NOTIFICATION_ID);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent contentIntent2 = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (progressPercent == 100) {
                remoteViews.setProgressBar(R.id.progress_bar, 100, 100, false);
                remoteViews.setTextViewText(R.id.contentTitle2, totalSize);
                remoteViews.setTextViewText(R.id.currentdownloaditemnumber, size + "/");


                nm.notify(NOTIFICATION_ID, notification);
                nm.cancel(NOTIFICATION_ID);
//                 notification.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;

            } else {
                remoteViews.setProgressBar(R.id.progress_bar, 100, progressPercent, false);
                remoteViews.setTextViewText(R.id.contentTitle2, totalSize);
                remoteViews.setTextViewText(R.id.totaldownloaditemnumber, size + "");
                remoteViews.setOnClickPendingIntent(R.id.stop_download_noti,
                        contentIntent2);
                nm.notify(NOTIFICATION_ID, notification);
            }
            // Build Notification with Notification Manager

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void createNotificationChannelID(NotificationManager nm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getResources().getString(R.string.app_name));

            nm.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mCurrentDownLoadingSet != null) {
            mCurrentDownLoadingSet.clear();
        }

        if (ClipBoardService.mDownloadingListener != null) {
            ClipBoardService.mDownloadingListener.dismissProgressDialog();
        }
    }

    public static class DownloadCancelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("stop_btn")) {
                int NOTIFICATION_ID = intent.getIntExtra("TYPE", 0);
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_ID);

                if (ClipBoardService.downloadingId != -1) {
                    PRDownloader.pause(ClipBoardService.downloadingId);
                }
                PRDownloader.cancel(ClipBoardService.downloadingId);

            }


        }
    }

    private int getRandomNo() {
        Random r = new Random();
        int low = 10;
        int high = 100;
        final int result = r.nextInt(high - low) + low;
        return result;
    }
}