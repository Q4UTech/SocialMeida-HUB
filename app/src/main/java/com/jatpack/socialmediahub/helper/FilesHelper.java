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
 * Created by Anon on 13,August,2018
 */
public class FilesHelper extends AsyncTask<Void, Void, ArrayList<File>> {
    private HelperListener l;

    public FilesHelper(HelperListener mListener) {
        this.l = mListener;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        File parentDir = new File(AppUtils.WHATSAPP_STATUS_DIR);
        System.out.println("FilesHelper.doInBackground 002"+parentDir.toString());
        System.out.println("FilesHelper.doInBackground 001"+parentDir);
        files = parentDir.listFiles();

        if (files != null) {
            System.out.println("FilesHelper.doInBackground 00"+files.length);
            for (File file : files) {
                if (is24Hour(file)) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG") || file.getName().endsWith(".jpeg")
                            || file.getName().endsWith(".mp4")) {
                        if (!inFiles.contains(file))
                            inFiles.add(file);
                    }
                }
            }
        }

        System.out.println("FilesHelper.doInBackground 01 "+inFiles.size());
        return inFiles;
    }

    private class Compare implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
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
//            Collections.reverse(statusList);
            Collections.sort(statusList, new Compare());
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
