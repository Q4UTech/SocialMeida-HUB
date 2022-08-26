package com.jatpack.socialmediahub.helper;

import android.content.Context;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.jatpack.socialmediahub.model.MediaData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class EffectManager {

    public static String projectionImage[] = new String[]{
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,

    };

    public static String projectionImage2[] = new String[]{
            Images.Media._ID, Images.Media.DISPLAY_NAME,
            Images.Media.DATA, Images.Media.DATE_TAKEN,
            Images.Media.MIME_TYPE,
            Images.Media.BUCKET_ID,
            Images.Media.BUCKET_DISPLAY_NAME,

    };


    public static List<MediaData> getVideoFromFolder(Context context, String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    MediaData mData = new MediaData();
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }

        Collections.sort(inFiles, new Comparator<MediaData>() {
            @Override
            public int compare(MediaData o1, MediaData o2) {
                return (o2.getDate()).compareTo(o1.getDate());
            }
        });

        return inFiles;
    }





//    public static List<MediaData> getAllGalleryImages(Context context, String mBucket) {
//        List<MediaData> gallerydata = new ArrayList<MediaData>();
//        Cursor galCursor = null;
//        try {
//            galCursor = context.getContentResolver().query(
//                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    projectionImage, MediaStore.Video.Media.BUCKET_DISPLAY_NAME + "='"
//                            + mBucket + "'", null,
//                    MediaStore.Video.Media.DATE_TAKEN + " DESC");
//
//
//            if (galCursor != null) {
//                int count = galCursor.getCount();
//
//
//                galCursor.moveToFirst();
//                for (int i = 0; i < count; i++) {
//                    MediaData galData = new MediaData();
//                    galData.setKey_id(i);
//                    galData.setId(galCursor.getString(0));
//                    galData.setName(galCursor.getString(1));
//                    galData.setPath(galCursor.getString(2));
//                    galData.setDate(galCursor.getString(3));
//                    galData.setTag("");
//                    gallerydata.add(galData);
//                    galCursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("exception is pick data" + " " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (galCursor != null)
//                galCursor.close();
//        }
//        return gallerydata;
//    }


    public static List<MediaData> getAllGalleryImages(Context context, String mBucket) {


        Log.d("EffectManager", "Hello getAllGalleryImages our creation aaa"+ " "+mBucket);
        ////new
        ArrayList<MediaData> inFiles = new ArrayList<>();
        File[] files;
        File parentDir = new File(mBucket);
        files = parentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.getName().isEmpty()) {
                    MediaData galData = new MediaData();
                    /*mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));*/

                    galData.setName(file.getName());
                    galData.setPath((file.getPath()));
                    galData.setDate(String.valueOf(file.lastModified()));
                    galData.setTag("");

                    if (!inFiles.contains(file)) {
                        inFiles.add(galData);
                    }
                }
            }
        }

        Collections.sort(inFiles, new Comparator<MediaData>() {
            @Override
            public int compare(MediaData o1, MediaData o2) {
                return (o2.getDate()).compareTo(o1.getDate());
            }
        });

        return inFiles;


    }


    public static List<MediaData> getAll_media_FromFolder(Context context, String mBucket) {
        ArrayList<MediaData> inFiles = new ArrayList<>();
        System.out.println("my file size shgaskdj 1");
        File[] files;
        System.out.println("my file size shgaskdj 2");
        File parentDir = new File(mBucket);
        System.out.println("my file size shgaskdj 3");
        files = parentDir.listFiles();
        if (files != null) {
            System.out.println("my file size shgaskdj"+" "+files.length);
            for (File file : files) {
                if (file.getName().endsWith(".mp4") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")||file.getName().endsWith(".jpeg")) {
                    MediaData mData = new MediaData();
                    mData.setPath(file.getAbsolutePath());
                    mData.setDate(Long.toString(file.lastModified()));
                    if (!inFiles.contains(file)) {
                        inFiles.add(mData);
                    }
                }
            }
        }


        Collections.sort(inFiles, new Comparator<MediaData>() {
            // SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            @Override
            public int compare(MediaData o1, MediaData o2) {
                return (o1.getDate()).compareTo(o2.getDate());
            }

        });
        Collections.reverse(inFiles);
        return inFiles;
    }


}
