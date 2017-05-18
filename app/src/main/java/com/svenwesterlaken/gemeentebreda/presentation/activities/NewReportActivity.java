package com.svenwesterlaken.gemeentebreda.presentation.activities;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewReportPagerAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.partials.PageIndicator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewReportActivity extends BaseActivity {

    private NewReportPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new NewReportPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        new PageIndicator(mSectionsPagerAdapter, this, mViewPager, this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
