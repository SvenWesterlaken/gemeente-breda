package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.presentation.partials.NotImplementedListener;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Button homeBtn = (Button) findViewById(R.id.confirmation_BTN_home);
        Button reportBtn = (Button) findViewById(R.id.confirmation_BTN_report);

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
