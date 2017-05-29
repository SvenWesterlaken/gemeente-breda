package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.MyReportsOneFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.MyReportsTwoFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportListFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;

/**
 * Created by lukab on 29-5-2017.
 */

public class MyReportPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;
    private Context context;

    public MyReportPagerAdapter(FragmentManager fragmentmanager, int tabCount, Context context){
        super(fragmentmanager);
        this.tabCount = tabCount;
        this.context = context;
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
