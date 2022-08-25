package com.jatpack.socialmediahub.ui.status;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by Anon on 21,June,2019
 */
public interface StatusFragmentContracts {

    interface StatusView extends BaseView<StatusPresenter> {
        void loadStatus(ArrayList<File> status);
        void statusEroor();

    }

//    interface DownloadedStatusView extends BaseView<StatusPresenter> {
//        void loadDownloadedStatus(ArrayList<File> status);
//    }

    interface StatusPresenter extends BasePresenter {
        void clear();
    }
}
