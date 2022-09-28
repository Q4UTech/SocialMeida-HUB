package com.example.whatsdelete.dynamictab;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int noOfItems;

    public ViewPagerAdapter(FragmentManager fm, int noOfItems) {
        super(fm);
        this.noOfItems = noOfItems;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.newInstance(position + 1);
    }



    @Override
    public int getCount() {
        return noOfItems;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "TAB "+(position+1);
    }


}
