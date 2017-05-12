package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.svenwesterlaken.gemeentebreda.R;

public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new CountDownTimer(1500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }.start();
    }
}
