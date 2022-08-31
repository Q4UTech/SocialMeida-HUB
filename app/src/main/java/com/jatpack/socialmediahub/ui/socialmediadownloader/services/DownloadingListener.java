package com.universal;

import com.universal.helper.DownloadingMediaInfo;

import java.util.List;

/**
 * Created by qunatum4u2 on 04/01/19.
 */

public interface DownloadingListener {
    void dismissProgressDialog();

    void onProgress(DownloadingMediaInfo mediaInfo);

    void onComplete(List<DownloadingMediaInfo> downloadedList);

}
