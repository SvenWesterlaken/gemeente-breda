package com.svenwesterlaken.gemeentebreda.presentation.activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewReportPagerAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportCategoryFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportDescriptionFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportLocationFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportMediaFragment;
import com.svenwesterlaken.gemeentebreda.presentation.partials.ChangableViewPager;

import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewReportActivity extends BaseActivity implements ViewPager.OnPageChangeListener, NewReportDescriptionFragment.DescriptionChangedListener, NewReportMediaFragment.MediaChangedListener, NewReportLocationFragment.LocationChangedListener, NewReportCategoryFragment.CategoryChangedListener{

    private NewReportPagerAdapter mSectionsPagerAdapter;
    private ChangableViewPager mViewPager;
    private Report newReport;
    protected View view;

    private static final int PERMISSIONS_REQUEST_CAMERA = 2;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();
        newReport = new Report();
        bundle.putParcelable("report", newReport);

        mSectionsPagerAdapter = new NewReportPagerAdapter(getSupportFragmentManager(), bundle);

        mViewPager = (ChangableViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(this);
        CircleIndicator page_indicator = (CircleIndicator) findViewById(R.id.pageIndicator);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        page_indicator.setViewPager(mViewPager);
        requestPermissions();
    }

    public Report getNewReport() {
        return this.newReport;
    }

    public void scrollToNext() {mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);}

    public void enableSwiping() { mViewPager.enableSwiping(); }

    public boolean swipingIsEnabled() {
        return mViewPager.isSwipeable();
    }

    public void requestPermissions() {

        // Request ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // sees the explanation, try again to request the permission.

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.body_permission_alert)
                        .setTitle(R.string.title_permission_alert);

                // 3. Add button.
                builder.setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestCameraPermission();
                    }
                });

                // 4. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                dialog.show();

            } else {

                // No explanation needed, we can request the permission.

                requestCameraPermission();

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Request ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // sees the explanation, try again to request the permission.

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.body_permission_alert)
                        .setTitle(R.string.title_permission_alert);

                // 3. Add button.
                builder.setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestWritePermission();
                    }
                });

                // 4. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                dialog.show();

            } else {

                // No explanation needed, we can request the permission.

                requestWritePermission();

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
    }

    public void requestWritePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setDescription(String t) {
        newReport.setDescription(t);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        if(position == 1) {
            NewReportLocationFragment lf;

            try {
                lf = (NewReportLocationFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
            } catch (Exception e) {
                lf = null;
            }

            if (lf != null && newReport.getMedia() != null) {
                lf.getImageLocation(newReport.getMedia());
            }
        }

        SummaryFragmentListener f;

        try {
            f = (SummaryFragmentListener) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
        } catch (Exception e) {
            f = null;
        }

        if (f != null) {
            f.fragmentBecameVisible();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void setLocation(Location t) {
        newReport.setLocation(t);
    }

    @Override
    public void setCategory(Category c) {
        newReport.setCategory(c);
    }

    @Override
    public void setMedia(Media m) {
        newReport.setMedia(m);
    }

    public interface SummaryFragmentListener {
        void fragmentBecameVisible();
    }


}
