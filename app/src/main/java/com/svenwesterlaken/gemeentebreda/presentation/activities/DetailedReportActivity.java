package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.DetailedCommentAdapter;
import com.svenwesterlaken.gemeentebreda.logic.adapters.DetailedReportAdapter;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

public class DetailedReportActivity extends MenuActivity  {

    Report report;
    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_report);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Melding");



        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            report =  (Report) extras.getSerializable("REPORT");
        }

        RecyclerView reportDetailed = (RecyclerView) findViewById(R.id.detailed_RV_reports);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportDetailed.setLayoutManager(layoutManager);

        DetailedReportAdapter adapter = new DetailedReportAdapter(this.getApplicationContext(), report);

        reportDetailed.setAdapter(adapter);

        RecyclerView reportComments = (RecyclerView) findViewById(R.id.detailed_RV_comments);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        reportComments.setLayoutManager(layoutManager2);


        DetailedCommentAdapter adapter2 = new DetailedCommentAdapter(this.getApplicationContext());

        reportComments.setAdapter(adapter2);

        reportDetailed.setHasFixedSize(true);

        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();

        ImageView button1 = (ImageView) findViewById(R.id.detailed_IV_button1);
        button1.setImageResource(R.drawable.comment_alert_outline);
        ImageView button2 = (ImageView) findViewById(R.id.detailed_IV_button2);
        button2.setImageResource(R.drawable.comment_text_outline);
        ImageView button3 = (ImageView) findViewById(R.id.detailed_IV_button3);
        button3.setImageResource(R.drawable.star_outline);
        ImageView button4 = (ImageView) findViewById(R.id.detailed_IV_button4);
        button4.setImageResource(R.drawable.bookmark_outline);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
