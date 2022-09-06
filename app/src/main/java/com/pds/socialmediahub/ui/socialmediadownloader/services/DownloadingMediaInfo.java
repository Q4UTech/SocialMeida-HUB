package com.pds.socialmediahub.ui.socialmediadownloader.services;

/**
 * Created by qunatum4u2 on 27/12/18.
 */

public class DownloadingMediaInfo {
    private int downloadingId;
    private String url;
    private String fileName;
    private String extension;

    public String getPinVideoUrl() {
        return pinVideoUrl;
    }

    public void setPinVideoUrl(String pinVideoUrl) {
        this.pinVideoUrl = pinVideoUrl;
    }

    private String pinVideoUrl;
    private long date;
    private String downloadFilePath;

    //TODO: Verify
    private int progress;

    private boolean isCompleted;
    private boolean ispause;
    private long currentbitService;
    private long totalbitService;

    public long getTotalbitAfterDownload() {
        return totalbitAfterDownload;
    }

    public void setTotalbitAfterDownload(long totalbitAfterDownload) {
        this.totalbitAfterDownload = totalbitAfterDownload;
    }

    private long totalbitAfterDownload;
    private String urlService;

    public String getDownloadedFileName() {
        return downloadedFileName;
    }

    public void setDownloadedFileName(String downloadedFileName) {
        this.downloadedFileName = downloadedFileName;
    }

    private String downloadedFileName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isIspause() {
        return ispause;
    }

    public void setIspause(boolean pause) {
        ispause = pause;
    }

    public long getCurrentbitService() {
        return currentbitService;
    }

    public void setCurrentbitService(long currentbitService) {
        this.currentbitService = currentbitService;
    }

    public long getTotalbitService() {
        return totalbitService;
    }

    public void setTotalbitService(long totalbitService) {
        this.totalbitService = totalbitService;
    }




    public int getDownloadingId() {
        return downloadingId;
    }

    public void setDownloadingId(int downloadingId) {
        this.downloadingId = downloadingId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }



    @Override
    public int hashCode() {
        if (url.isEmpty()) {
            return super.hashCode();
        }
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        DownloadingMediaInfo info = (DownloadingMediaInfo) obj;
        if (url.isEmpty()) {
            return super.equals(obj);
        }
        return this.url.equals(info.url);
    }



}
