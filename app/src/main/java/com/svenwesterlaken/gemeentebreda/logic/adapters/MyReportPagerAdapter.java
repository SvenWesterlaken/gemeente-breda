package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.svenwesterlaken.gemeentebreda.presentation.fragments.MyReportsOneFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.MyReportsTwoFragment;

/**
 * Created by lukab on 29-5-2017.
 */

public class MyReportPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public MyReportPagerAdapter(FragmentManager fragmentmanager, int tabCount){
        super(fragmentmanager);
        this.tabCount = tabCount;
    }

    //Return the amount of tabas
    @Override
    public int getCount() {
        return tabCount;
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
                return new MyReportsOneFragment();
            case 1:
                return new MyReportsTwoFragment();

            default:
                return null;

        }

    }


    /**
     * Method that sets the title of a tab
     * @param position the position of the tab
     * @return the title that belongs to the tab
     */
    @Override
    public CharSequence getPageTitle(int position){
        switch (position) {
            case 0:
                return "Geplaatst";
            case 1:
                return "Volgend";
            default:
                return null;
        }
    }
}
