package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ImageCaptureFragment();
        }
        else if (position == 1)
        {
            fragment = new HealthUpdateFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Capture Image";
        }
        else if (position == 1)
        {
            title = "Medical Health";
        }

        return title;
    }
}
