package com.pds.socialmediahub.ui.status;

import androidx.documentfile.provider.DocumentFile;


import java.io.File;
import java.util.List;

public class AllMediaListingImage_singleton {

    private static AllMediaListingImage_singleton instance;
    public List<AllMediaListing_mainModel> allMediaListing_mainModels;
    public List<File> fileStatus;
    public List<DocumentFile> DocfileStatus;

    public static AllMediaListingImage_singleton getInstance()
    {
        if (instance == null)
        {
            synchronized (AllMediaListingImage_singleton.class) {
                if (instance == null ) {
                    instance = new AllMediaListingImage_singleton();
                }
            }
        }
        return instance;
    }

    public List<AllMediaListing_mainModel> getAllMediaListing_mainModels() {
        return allMediaListing_mainModels;
    }

    public void setAllMediaListing_mainModels(List<AllMediaListing_mainModel> allMediaListing_mainModels) {
        this.allMediaListing_mainModels = allMediaListing_mainModels;
    }

    public List<File> getAllStatusFiles() {
        return fileStatus;
    }

    public void setAllStatusFiles(List<File> fileStatus) {
        this.fileStatus = fileStatus;
    }

    public List<DocumentFile> getAllStatusDocumentFiles() {
        return DocfileStatus;
    }

    public void setAllStatusDocumentFiles(List<DocumentFile> fileStatus) {
        this.DocfileStatus = fileStatus;
    }


}
