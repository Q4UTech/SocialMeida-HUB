package com.pds.socialmediahub.ui.status;

import android.net.Uri;

public class AllMediaListing_mainModel {

    //video
    private String videoTitle;
    private String videoDuration;
    private String videoUri;
    private String video_fileExtension;
    private boolean videoTabSelected=false;
    private boolean isVideoLongPressedClicked=false;

    //audio
    private String audioTitle;
    private String audioDuration;
    private String audioUri;
    private String audio_fileExtension;
    private boolean audioTabSelected=false;
    private boolean isAudioLongPressedClicked=false;

    //Image
    private String absolutePathOfImage;
    private String imageTitle;

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    private String imageURI;
    private boolean imageTabSelected=false;
    private boolean isImageLongPressedClicked=false;

    //Document
    private String documentTitle;
    private String documentDuration;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private String videoId;

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    private String audioId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    private String imageId;
    private Uri documentUri;
    private String document_fileExtension;
    private boolean document_isHigherVer=false;
    private boolean documentTabSelected=false;

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getVideo_FileExtension() {
        return video_fileExtension;
    }

    public void setVideo_FileExtension(String video_fileExtension) {
        this.video_fileExtension = video_fileExtension;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public String getAudio_fileExtension() {
        return audio_fileExtension;
    }

    public void setAudio_fileExtension(String audio_fileExtension) {
        this.audio_fileExtension = audio_fileExtension;
    }

    public String getAbsolutePathOfImage() {
        return absolutePathOfImage;
    }

    public void setAbsolutePathOfImage(String absolutePathOfImage) {
        this.absolutePathOfImage = absolutePathOfImage;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentDuration() {
        return documentDuration;
    }

    public void setDocumentDuration(String documentDuration) {
        this.documentDuration = documentDuration;
    }

    public Uri getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(Uri documentUri) {
        this.documentUri = documentUri;
    }

    public String getDocument_fileExtension() {
        return document_fileExtension;
    }

    public void setDocument_fileExtension(String document_fileExtension) {
        this.document_fileExtension = document_fileExtension;
    }

    public boolean isDocument_isHigherVer() {
        return document_isHigherVer;
    }

    public void setDocument_isHigherVer(boolean document_isHigherVer) {
        this.document_isHigherVer = document_isHigherVer;
    }

    public boolean getVideoTabSelected() {
        return videoTabSelected;
    }

    public void setVideoTabSelected(boolean videoTabSelected) {
        this.videoTabSelected = videoTabSelected;
    }

    public boolean getAudioTabSelected() {
        return audioTabSelected;
    }

    public void setAudioTabSelected(boolean audioTabSelected) {
        this.audioTabSelected = audioTabSelected;
    }

    public boolean getImageTabSelected() {
        return imageTabSelected;
    }

    public void setImageTabSelected(boolean imageTabSelected) {
        this.imageTabSelected = imageTabSelected;
    }

    public boolean geDocumentTabSelected() {
        return documentTabSelected;
    }

    public void setDocumentTabSelected(boolean documentTabSelected) {
        this.documentTabSelected = documentTabSelected;
    }

    public boolean isImageLongPressedClicked() {
        return isImageLongPressedClicked;
    }

    public void setImageLongPressedClicked(boolean imageLongPressedClicked) {
        isImageLongPressedClicked = imageLongPressedClicked;
    }

    public boolean isVideoLongPressedClicked() {
        return isVideoLongPressedClicked;
    }

    public void setVideoLongPressedClicked(boolean videoLongPressedClicked) {
        isVideoLongPressedClicked = videoLongPressedClicked;
    }

    public boolean isAudioLongPressedClicked() {
        return isAudioLongPressedClicked;
    }

    public void setAudioLongPressedClicked(boolean audioLongPressedClicked) {
        isAudioLongPressedClicked = audioLongPressedClicked;
    }
}
