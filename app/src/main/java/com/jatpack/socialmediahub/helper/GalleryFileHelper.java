package com.jatpack.socialmediahub.helper;

import android.os.AsyncTask;

import com.jatpack.socialmediahub.listener.HelperListener;
import com.jatpack.socialmediahub.util.AppUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by qunatum4u2 on 12/09/18.
 */

public class GalleryFileHelper extends AsyncTask<Void, Void, ArrayList<File>> {
    private HelperListener l;

    public GalleryFileHelper(HelperListener mListener) {
        this.l = mListener;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        File parentDir = new File(AppUtils.WHATSAPP_STATUS_DIR_GALLERY);

        files = parentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG")) {
                        if (!inFiles.contains(file))
                            inFiles.add(file);

                        System.out.println("my gallery lastModified date is here"+" "+file.lastModified());

                }
            }
        }

        Collections.sort(inFiles, new Compare());
        return inFiles;
    }

    private class Compare implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
        }
    }


    public boolean getLastModified(File dir) {

        File[] files = dir.listFiles();
        if (files.length == 0) {
            return false;
        }
        ArrayList<File> _24Hour = new ArrayList<>();

        Date modifiedDate = null;
        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                modifiedDate = new Date(files[i].lastModified());
            }
        }
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR, -24);
        Date alertDate = cal.getTime();

        if (modifiedDate != null && modifiedDate.after(alertDate)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<File> statusList) {
        super.onPostExecute(statusList);
        if (statusList != null) {
            Collections.reverse(statusList);
            this.l.onHelperFinished(statusList);
        }
    }

    public void loadHelper() {
        execute();
    }

    public static final String formatTime(long millis) {
        long secs = millis / 1000;
        return String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60);
    }

    public boolean is24Hour(File f) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR, -24);
        Date alertDate = cal.getTime();

        Date fileDate = new Date(f.lastModified());

        if (fileDate != null && fileDate.after(alertDate))
            return true;
        else
            return false;
    }

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat DATEFORMAT2 = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
}

