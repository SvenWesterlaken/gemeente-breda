package com.svenwesterlaken.gemeentebreda.presentation.partials;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.svenwesterlaken.gemeentebreda.R;

/**
 * Created by Sven Westerlaken on 17-5-2017.
 */

public class PageIndicator implements ViewPager.OnPageChangeListener {

    private Animation animation;
    private Context context;
    private int dotsCount;
    private ImageView [] dots;

    public PageIndicator(FragmentPagerAdapter mSectionsPagerAdapter, Activity a, ViewPager mViewPager, Context context) {
        this.context = context;
        dotsCount = mSectionsPagerAdapter.getCount();
        dots = new ImageView[dotsCount];
        animation = AnimationUtils.loadAnimation(context, R.anim.indicator_anim_in);
        LinearLayout pager_indicator = (LinearLayout) a.findViewById(R.id.viewPagerCountDots);

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_nonselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
            mViewPager.addOnPageChangeListener(this);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_selected));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("SCROLLEDLISTENER", "pos: " + position + " offset: " + positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_nonselected));

        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_selected));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("STATELISTENER", "" + state);
    }

}
