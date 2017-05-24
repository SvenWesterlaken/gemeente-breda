package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        final Button homeBtn = (Button) findViewById(R.id.confirmation_BTN_home);
        final Button reportBtn = (Button) findViewById(R.id.confirmation_BTN_report);

        homeBtn.setEnabled(false);
        reportBtn.setEnabled(false);

        final ProgressBar loader = (ProgressBar) findViewById(R.id.confirmation_PB_sending);

        final TextView message = (TextView) findViewById(R.id.confirmation_TV_message);

        final ImageView succes = (ImageView) findViewById(R.id.confirmation_IV_succesIcon);
        final ImageView error = (ImageView) findViewById(R.id.confirmation_IV_errorIcon);

        final Animation popupAnimation = AnimationUtils.loadAnimation(this, R.anim.popup_animation);
        final Animation fadeinAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_animation);

        new CountDownTimer(1500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                succes.setVisibility(View.VISIBLE);
                succes.startAnimation(popupAnimation);
                loader.setVisibility(View.INVISIBLE);
                message.startAnimation(fadeinAnimation);
                message.setVisibility(View.VISIBLE);
                homeBtn.setEnabled(true);
                reportBtn.setEnabled(true);
            }
        }.start();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(i);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Report report;

                Bundle extras = getIntent().getExtras();
                report = (Report) extras.getSerializable("REPORT");

                Intent intent = new Intent(getApplicationContext(), DetailedReportActivity.class);
                intent.putExtra("REPORT", report);

                startActivity(intent);

            }
        });
    }
}
