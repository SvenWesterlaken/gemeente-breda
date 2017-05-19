package com.svenwesterlaken.gemeentebreda.logic.adapters;


import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.svenwesterlaken.gemeentebreda.presentation.fragments.LoginOneFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.LoginTwoFragment;

/**
 * Created by Sven Westerlaken on 12-5-2017.
 */

public class LoginPagerAdapter extends FragmentPagerAdapter {

    private static int fragmentCount = 2;

    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginOneFragment();
            case 1:
                return new LoginTwoFragment();

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return fragmentCount;
    }
}
