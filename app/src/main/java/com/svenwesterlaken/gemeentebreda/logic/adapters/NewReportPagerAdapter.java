package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportCategoryFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportDescriptionFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportLocationFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportMediaFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportSummaryFragment;

/**
 * Created by Sven Westerlaken on 16-5-2017.
 */

public class NewReportPagerAdapter extends FragmentPagerAdapter {

    private Bundle bundle;
    private static int tabCount = 5;

    public NewReportPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewReportMediaFragment media = new NewReportMediaFragment();
                media.setArguments(bundle);
                return media;
            case 1:
                NewReportLocationFragment location = new NewReportLocationFragment();
                location.setArguments(bundle);
                return location;
            case 2:
                NewReportCategoryFragment category = new NewReportCategoryFragment();
                category.setArguments(bundle);
                return category;
            case 3:
                NewReportDescriptionFragment description = new NewReportDescriptionFragment();
                description.setArguments(bundle);
                return description;
            case 4:
                NewReportSummaryFragment summary = new NewReportSummaryFragment();
                summary.setArguments(bundle);
                return summary;
            default:
                return null;
        }
    }
}
