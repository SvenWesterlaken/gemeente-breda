package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportTab2;

/**
 * Created by lukab on 4-5-2017.
 */

public class ReportPagerAdapter extends FragmentPagerAdapter {

    ReportMapFragment tab1;
    ReportTab2 tab2;
    int mNumOfTabs;
    Context context;

    public ReportPagerAdapter(FragmentManager fm, int mNumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
        this.context = context;


    }

    /**
     * Retrieve the number of tabs
     * @return A int data type
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }


    /**
     * Method that decides which tab class the application is showing
     * @param position the position of the tab
     * @return the fragment that belongs to the tab
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new ReportMapFragment();
                return tab1;
            case 1:
                tab2 = new ReportTab2();
                return tab2;

            default:
                return null;

        }

    }


    /**
     * Method that sets the title of a tab
     * @param position the position of the tab
     * @return the title that belongs to the tab
     */
    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return context.getResources().getString(R.string.homepage_tab1);
            case 1:
                return context.getResources().getString(R.string.homepage_tab2);

            default:
                return null;
        }
    }




}

