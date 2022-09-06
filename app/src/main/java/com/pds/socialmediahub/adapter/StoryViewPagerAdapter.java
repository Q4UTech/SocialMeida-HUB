package com.pds.socialmediahub.adapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pds.socialmediahub.ui.status.WAStatusFragment;


/**
 * Created by Anon on 24,August,2018
 */
public class StoryViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int COUNT_ITEMS = 1;

    public StoryViewPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.d("StoryViewPagerAdapter", "Hello StoryViewPagerAdapter oolla ooo ola");

    }


    @Override
    public Fragment getItem(int i) {
        Log.d("StoryViewPagerAdapter", "Hello StoryViewPagerAdapter oolla ooo ola 002"+" "+i);

        if (i == 0) {
            return new WAStatusFragment();
        }
        /*else if (i == 1) {

//            imageFragment.disableSelection();
            return new WaTrandingStatus();
        }*/
        return null;
    }

    @Override
    public int getCount() {
        Log.d("StoryViewPagerAdapter", "Hello StoryViewPagerAdapter oolla ooo ola 003"+" "+COUNT_ITEMS);

        return COUNT_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("StoryViewPagerAdapter", "Hello StoryViewPagerAdapter oolla ooo ola 004"+" "+position);

        if (position == 0)
            return "Recent Status";
//        else if (position == 1)
//            return "Trending Status";
//        else if (position == 2)
//            return "Downloads";
        return "";
    }

//    public void disableSelection(){
//        if(imageFragment != null){
//            videoFragment.disableSelection();
//        }else if(videoFragment != null){
//            imageFragment.disableSelection();
//        }
//    }


}
