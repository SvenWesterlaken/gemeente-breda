package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportListFragment;

/**
 * Created by lukab on 4-5-2017.
 */

public class ReportPagerAdapter extends FragmentPagerAdapter {

    private ReportMapFragment mapFragment;
    private ReportListFragment listFragment;
    private int tabCount;
    private Context context;

    public ReportPagerAdapter(FragmentManager fragmentmanager, int tabCount, Context context, ReportMapFragment mapFragment) {
        super(fragmentmanager);
        this.tabCount = tabCount;
        this.context = context;
        this.mapFragment = mapFragment;
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
                return mapFragment;
            case 1:
                listFragment = new ReportListFragment();
                return listFragment;

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

