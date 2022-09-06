package com.pds.socialmediahub.util;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pds.socialmediahub.R;
import com.pds.socialmediahub.model.Conversation;
import com.pds.socialmediahub.model.MediaData;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * Created by Anon on 06,August,2019
 */
public class Constants {

    public static final String MAIN_FOLDER_NAME = "Whats Delete";
    public static final String KEY_USER_NAME = "_user_name_";
    //this one is old directory.
    public static final String MAIN_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MAIN_FOLDER_NAME;

    //Created bcoz of new storage changes...
//    public static final String MAIN_DIR_ABOVE_PIE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.m24apps.socialvideo" + File.separator + MAIN_FOLDER_NAME;



    /*storage paths */
    public static final int NOTIFICATION_ID = 5001;

    public static final String SETTINGS_ACTION = "com.quantum.settings.notification.action";
    public static final int SETTINGS_RESULT = 1234;


    private final static String APP_ROOT_PATH = ".WhatsDelete/";
    public static  String MAIN_DIR_ABOVE_PIE;

    public final static String APP_GALLERY_MEDIA = "/Media/";
    public final static String APP_GALLERY_STATUS = "/Story/";
    public final static String APP_DIR_SS = "ScreenShots/";

    public static final String TEMP_FOLDER = ".TEMP/";

    //public static final String WHATSAPP_STATUS_DIR_GALLERY = "/storage/emulated/0/" + APP_ROOT_GALLERY_PATH + APP_GALLERY_STATUS;

    public static final int RQ_RUNTIME_CALL_PHONE = 1003;
    public static final int RQ_RUNTIME_STORAGE = 1001;

    public static final String DEFAULT_USER = "default_user";

    public static final String KEY_FROM_MEDIA_REMOVE = "_from_media_remove_";

    public static final String WHATSAPP_PACKAGE = "com.whatsapp";
    public static final String WA_Status_Gallery = "WA Status Gallery";

    public static final String WHATSAPP_SETTING_ACTIVITY_PATH = WHATSAPP_PACKAGE.concat(".SettingsDataUsage");
    /**
     * messages from whatsapp, not to add in database
     */
    public static final String WHATSAPP = "WhatsApp"; //sender_name
    public static final String WHATSAPP_WEB = "WhatsApp Web";//sender_name
    public static final String FINISHED_BACKUP = "Finished backup";//sender_name

    /*constants declarations*/
    public static final String BACKUP_PROGRESS = "Backup in progress";//sender name
    public static final String BACKUP_PAUSED = "Backup paused"; //sender name
    public static final String YOU = "You";//sender name
    public static final String CHECKING_NEW_MESSAGE = "Checking for new messages";//sender message
    public static final String CHECKING_WEB_LOGIN = "WhatsApp Web is currently active";//sender message
    public static final String BACKUP_INFO = "Tap for more info";//sender message
    public static final String WAITING_FOR_WIFI = "Waiting for Wi-Fi";// sender message

    public static final String MESSAGE_DELETED = "This message was deleted";

    public static final String IS_VIDEO = ".mp4";
    public static final String IS_IMAGE = ".jpg";
    public static final String IS_VOICE = ".mp3";

    public static final String IS_IMAGE_PNG = ".png";
    public static final String IS_IMAGE_JPEG = ".jpeg";
    public static final String IS_IMAGE_RAW = ".raw";
    public static final String IS_VIDEO_GIF = ".gif";
    public static final String IS_VIDEO_MKV = ".mkv";
    public static final String IS_VOICE_WAV = ".wav";
    public static final String IS_VOICE_3GPP = ".3gpp";
    public static final String IS_VOICE_OGG = ".ogg";
    public static final String IS_VOICE_AAC = ".aac";

    private final static String BASE_PATH = Environment.getExternalStorageDirectory().toString();

    private final static String BASE_PATH_FOR_WHATSAPP = "/Android/media/com.whatsapp";

    private static final String WHATSAPP_MEDIA_PATH_IMAGES = BASE_PATH + "/WhatsApp/Media/WhatsApp Images/";
    private static final String WHATSAPP_MEDIA_PATH_VIDEOS = BASE_PATH + "/WhatsApp/Media/WhatsApp Video/";
    private static final String WHATSAPP_MEDIA_PATH_DOCUMENTS = BASE_PATH + "/WhatsApp/Media/WhatsApp Documents/";
    private static final String WHATSAPP_MEDIA_PATH_VOICE = BASE_PATH + "/WhatsApp/Media/WhatsApp Voice Notes/";
    private static final String WHATSAPP_MEDIA_PATH_GIF = BASE_PATH + "/WhatsApp/Media/WhatsApp Animated Gifs/";
    private static final String WHATSAPP_MEDIA_PATH_AUDIO = BASE_PATH + "/WhatsApp/Media/WhatsApp Audio/";

    private static final String WHATSAPP_MEDIA_PATH_IMAGES_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Images/";
    private static final String WHATSAPP_MEDIA_PATH_VIDEOS_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Video/";
    private static final String WHATSAPP_MEDIA_PATH_DOCUMENTS_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Documents/";
    private static final String WHATSAPP_MEDIA_PATH_VOICE_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Voice Notes/";
    private static final String WHATSAPP_MEDIA_PATH_GIF_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Animated Gifs/";
    private static final String WHATSAPP_MEDIA_PATH_AUDIO_WITH_IN_APP = BASE_PATH + BASE_PATH_FOR_WHATSAPP + "/WhatsApp/Media/WhatsApp Audio/";


    private static final float BLUR_RADIUS = 25f;

    public static final String IS_FROM_SOCIAL = "IS_FROM_SOCIAL";
    public static final String IS_FROM_INSTA = "IS_FROM_INSTA";
    public static final String IS_FROM_FB = "IS_FROM_FB";
    public static final String IS_FROM_TMBLER = "IS_FROM_TMBLER";
    public static final String IS_FROM_PINTEREST = "IS_FROM_PINTEREST";
    public static final String IS_FROM_TIKTOK = "IS_FROM_TIKTOK";
    public static final String IS_FROM_LIKE = "IS_FROM_LIKE";
    public static final String IS_FROM_WA_DELETE = "IS_FROM_WA_DELETE";
    public static final String IS_FROM_WA_STATUS = "IS_FROM_WA_STATUS";
    public static final String IS_FROM_TWITTER = "IS_FROM_TWITTER";




    /*Above android Q we are storing data on android folder
and below android Q storage part is same as old.*/
    public static String getAppMainDir() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            return MAIN_DIR_ABOVE_PIE;
        } else {
            return MAIN_DIR;
        }
    }

    public void init(Context context){
            MAIN_DIR_ABOVE_PIE = context.getExternalFilesDir(MAIN_FOLDER_NAME).getPath();

    }

    //public final static String APP_ROOT_GALLERY_PATH = "/Whats Delete/WhatsDeleteM24/";

    public static String getAppRootGalleryPath() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            return MAIN_DIR_ABOVE_PIE;
        } else {
            return MAIN_DIR ;
        }
    }

    // public final static String APP_GALLERY_SCREENSHOT = BASE_PATH + APP_ROOT_GALLERY_PATH + APP_DIR_SS;

    public static String getAppGalleryScreenShot() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            return getAppRootGalleryPath() + APP_DIR_SS;
        } else {
            return getAppRootGalleryPath() + APP_DIR_SS;
        }
    }


    //public static final String MEDIA_BACKUP_PATH = BASE_PATH + APP_ROOT_PATH + "reserved/";

    public static String getMediaBackupPath() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            //Hidden folder is not working in android Q and above devices so not storing in hidden file
            return getAppRootGalleryPath() + APP_ROOT_PATH + "reserved/";
        } else {
            return getAppRootGalleryPath() + APP_ROOT_PATH + "reserved/";
        }
    }


    // public static final String MEDIA_DELETED = BASE_PATH + APP_ROOT_PATH + "deleted/";

    public static String getMediaDeletedPath() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            //Hidden folder is not working in android Q and above devices so not storing in hidden file
            return getAppRootGalleryPath() + APP_ROOT_PATH + "deleted/";
        } else {
            return getAppRootGalleryPath() + APP_ROOT_PATH + "deleted/";
        }
    }


    /******************************************************************************/
    public static String getActualTimeFromTimeStamp(long timeStamp) {
        Date dt = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        return sdf.format(dt);
    }

    public static List<MediaData> fetchStoredMedia(Context context, String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                /*if (file.getName().endsWith(".mp4") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")) {*/
                MediaData mData = new MediaData();
                mData.setName(file.getName());
                mData.setPath(file.getAbsolutePath());
                mData.setDate(Long.toString(file.lastModified()));
                if (!inFiles.contains(file)) {
                    inFiles.add(mData);
                }
            }
        }
        Collections.sort(inFiles, (o1, o2) -> (o1.getDate()).compareTo(o2.getDate()));
        Collections.reverse(inFiles);
        return inFiles;
    }

    public static List<MediaData> fetchStoreImages(String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg")) {
                    MediaData mData = new MediaData();
                    mData.setName(file.getName());
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }
        Collections.sort(inFiles, (o1, o2) -> (o1.getDate()).compareTo(o2.getDate()));
        Collections.reverse(inFiles);
        return inFiles;
    }

    public static List<MediaData> fetchStoreVideo(String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    MediaData mData = new MediaData();
                    mData.setName(file.getName());
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }
        Collections.sort(inFiles, (o1, o2) -> (o1.getDate()).compareTo(o2.getDate()));
        Collections.reverse(inFiles);
        return inFiles;
    }

    public static List<MediaData> fetchStoreVoice(String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp3")) {
                    MediaData mData = new MediaData();
                    mData.setName(file.getName());
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }
        Collections.sort(inFiles, (o1, o2) -> (o1.getDate()).compareTo(o2.getDate()));
        Collections.reverse(inFiles);
        return inFiles;
    }

    public static List<MediaData> fetchStoreDocs(String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(".mp3") &&
                        !file.getName().endsWith(".mp4") &&
                        !file.getName().endsWith(".jpg") &&
                        !file.getName().endsWith(".gif") &&
                        !file.getName().endsWith(".m4a") &&
                        !file.getName().endsWith(".webp")) {
                    MediaData mData = new MediaData();
                    mData.setName(file.getName());
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }
        Collections.sort(inFiles, (o1, o2) -> (o1.getDate()).compareTo(o2.getDate()));
        Collections.reverse(inFiles);
        return inFiles;
    }

    public static List<Conversation> dSerializeJson(String json, Gson gson) {
        Type typeConverter = new TypeToken<List<Conversation>>() {
        }.getType();
        return gson.fromJson(json, typeConverter);
    }


   /* public static void showDeleteMessageNotification(Context context, String title, String description, String putExtra) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        GCMPreferences gcmPreferences = new GCMPreferences(context);
        String splashNameMain = gcmPreferences.getSplashName();
        //        Intent intent = new Intent(context, HomeActivity.class);
        //  Log.d("MediaDetectorService", "Test shoWDeleteDocumentNotification from chat");
        //  Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intent = new Intent();
        intent.setClassName(context, splashNameMain);
        intent.putExtra(KEY_USER_NAME, putExtra);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent(context, DeletedChatBroadCast.class);
        intent1.putExtra(KEY_USER_NAME, putExtra);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.contentTitle, "" + description);//description

        contentView.setTextViewText(R.id.linear_layout, "" + title); //title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.app_name),
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.app_name) + " Notification");

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,
                        context.getResources().getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setCustomContentView(contentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.splash_icon);

        } else {
            mBuilder.setSmallIcon(R.drawable.splash_icon);
        }


        Notification notification = mBuilder.build();
        notification.contentIntent = pendingIntent1;
        manager.notify(0, notification);
    }*/


    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        }

        fileOrDirectory.delete();
    }

    public static Bitmap instantBlurBitmap(Bitmap image, Context context) {
        if (null == image) {
            return null;
        }
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public static boolean deleteFileFromMediaStore(
            final ContentResolver contentResolver, final File file) {
        String canonicalPath;

        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?",
                new String[]{canonicalPath});
        System.out
                .println("<<<checking GalleryActivity.deleteFileFromMediaStore() "
                        + result);
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                int result2 = contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?",
                        new String[]{absolutePath});
                System.out
                        .println("<<<checking GalleryActivity.deleteFileFromMediaStore() "
                                + result2);
                if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


    public static String getWHATSAPP_MEDIA_PATH_IMAGES() {
        File file = new File(WHATSAPP_MEDIA_PATH_IMAGES_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_IMAGES_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_IMAGES;
        }
    }

    public static String getWHATSAPP_MEDIA_PATH_VIDEOS() {
        File file = new File(WHATSAPP_MEDIA_PATH_VIDEOS_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_VIDEOS_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_VIDEOS;
        }
    }

    public static String  getWHATSAPP_MEDIA_PATH_DOCUMENTS() {
        File file = new File(WHATSAPP_MEDIA_PATH_DOCUMENTS_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_DOCUMENTS_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_DOCUMENTS;
        }
    }

    public static String getWHATSAPP_MEDIA_PATH_VOICE() {
        File file = new File(WHATSAPP_MEDIA_PATH_VOICE_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_VOICE_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_VOICE;
        }
    }

    public static String getWHATSAPP_MEDIA_PATH_GIF() {
        File file = new File(WHATSAPP_MEDIA_PATH_GIF_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_GIF_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_GIF;
        }
    }

    public static String getWHATSAPP_MEDIA_PATH_AUDIO() {
        File file = new File(WHATSAPP_MEDIA_PATH_AUDIO_WITH_IN_APP);
        if (file.exists()) {
            return WHATSAPP_MEDIA_PATH_AUDIO_WITH_IN_APP;
        } else {
            return WHATSAPP_MEDIA_PATH_AUDIO;
        }
    }
}
