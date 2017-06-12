package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.AddReportRequest;
import com.svenwesterlaken.gemeentebreda.domain.Report;

public class ConfirmationActivity extends BaseActivity implements AddReportRequest.onReportAddedListener, View.OnClickListener{

    private ProgressBar loader;
    private TextView message;
    private ImageView succes;
    private ImageView error;
    private Animation popupAnimation;
    private Animation fadeinAnimation;

    private Report report;

    private Button homeBtn;
    private Button reportBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        homeBtn = (Button) findViewById(R.id.confirmation_BTN_home);
        reportBtn = (Button) findViewById(R.id.confirmation_BTN_report);

        homeBtn.setEnabled(false);
        reportBtn.setEnabled(false);

        homeBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);

        loader = (ProgressBar) findViewById(R.id.confirmation_PB_sending);

        message = (TextView) findViewById(R.id.confirmation_TV_message);

        succes = (ImageView) findViewById(R.id.confirmation_IV_succesIcon);
        error = (ImageView) findViewById(R.id.confirmation_IV_errorIcon);

        popupAnimation = AnimationUtils.loadAnimation(this, R.anim.popup_animation);
        fadeinAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_animation);

        Bundle extras = getIntent().getExtras();
        final Report report = extras.getParcelable("REPORT");

        addReport(report);
    }

    public void addReport(Report report){
        AddReportRequest reportRequest = new AddReportRequest(getApplicationContext(), this);
        reportRequest.addAReport(report);
    }
    
    @Override
    public  void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), ReportActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onSucces() {
        succes.setVisibility(View.VISIBLE);
        succes.startAnimation(popupAnimation);
        loader.setVisibility(View.INVISIBLE);
        message.startAnimation(fadeinAnimation);
        message.setVisibility(View.VISIBLE);
        homeBtn.setEnabled(true);
        reportBtn.setEnabled(true);
    }

    @Override
    public void onError() {
        error.setVisibility(View.VISIBLE);
        error.startAnimation(popupAnimation);
        loader.setVisibility(View.INVISIBLE);
        message.setText(getString(R.string.confirmation_error_message));
        message.startAnimation(fadeinAnimation);
        message.setVisibility(View.VISIBLE);
        homeBtn.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        if(v.getId() == R.id.confirmation_BTN_report) {
            i = new Intent(getApplicationContext(), DetailedReportActivity.class);
            i.putExtra("REPORT", report);
        } else if (v.getId() == R.id.confirmation_BTN_home) {
            i = new Intent(getApplicationContext(), ReportActivity.class);
        }

        if(i != null) {
            finish();
            startActivity(i);
        }
    }
}
