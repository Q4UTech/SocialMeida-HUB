package com.jatpack.socialmediahub.model;

import android.net.Uri;

import java.io.File;

public class MediaModel {
    private String fileName=null;

    private Boolean isSelected=false;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Uri getdFileUri() {
        return dFileUri;
    }

    public void setdFileUri(Uri dFileUri) {
        this.dFileUri = dFileUri;
    }

    private long dateTime=0;
    private String filePath=null;
    private File absoluteFile;
    private Uri dFileUri;

    public File getAbsoluteFile() {
        return absoluteFile;
    }

    public void setAbsoluteFile(File absoluteFile) {
        this.absoluteFile = absoluteFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public float getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
