package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.UpvoteRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

public class DetailedReportActivity extends BaseActivity  {

    Report report;
    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handler = new DatabaseHandler(getApplicationContext());


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            report =  extras.getParcelable("REPORT");
        }


        TextView category =  (TextView) findViewById(R.id.report_TV_title);
        TextView address = (TextView) findViewById(R.id.report_TV_address);
        TextView descriptionTV = (TextView) findViewById(R.id.report_TV_description);
        ImageView icon = (ImageView) findViewById(R.id.report_IV_icon);
        ImageView status = (ImageView) findViewById(R.id.report_IV_status);
        final TextView upvotesTxt = (TextView) findViewById(R.id.report_TV_upvotes);
        
        upvotesTxt.setText(getString(R.string.detailed_upvotes, report.getUpvotes()));
        category.setText(report.getCategory().getCategoryName());

        address.setText(report.getLocation().getStreet());

        String description = report.getDescription();

        if(description == null || description.equals("null") || description.isEmpty() ) {
            description = getString(R.string.summary_missing_description);
        }

        descriptionTV.setText(description);

        icon.setImageResource( R.drawable.lightbulb);
        
        if (report.getStatus().equals("open")) {
           status.setImageResource(R.drawable.eye_off);
        } else {
            status.setImageResource(R.drawable.check);
        }

        RecyclerView reportComments = (RecyclerView) findViewById(R.id.detailed_RV_comments);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        reportComments.setLayoutManager(layoutManager2);

        ImageView button1 = (ImageView) findViewById(R.id.detailed_IV_button1);
        button1.setImageResource(R.drawable.comment_alert_outline);
        final ImageView button2 = (ImageView) findViewById(R.id.detailed_IV_button2);
        button2.setImageResource(R.drawable.comment_text_outline);
        final ImageView button3 = (ImageView) findViewById(R.id.detailed_IV_button3);
        final User user = handler.getUser(1);

        button3.setImageResource(R.drawable.star_outline);
    
    
        if (handler.searchUpvote(report, user) != null){
            button3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            button3.setClickable(false);
        }
        
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    
                if (handler.searchUpvote(report, user) == null) {
                    addUpvote(report);
                    button3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    upvotesTxt.setText(report.getUpvotes()+1 + " upvotes");
                  
                    
                }
                
                // delete kan pas als dat ook via API kan
    
//                else {
//                    handler.deleteUpvote(report);
//                    button3.setColorFilter(null);
//                }
            }
        });
        final ImageView button4 = (ImageView) findViewById(R.id.detailed_IV_button4);
        button4.setImageResource(R.drawable.bookmark_outline);

        if (handler.searchFavourite(report, user) != null){
            button4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (handler.searchFavourite(report, user) == null) {
                    handler.addFavourite(handler.getUser(1), report);
                    button4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                }

                else {
                    handler.deleteFavourite(report);
                    button4.setColorFilter(null);
                }



            }
        });

    }
    
    public void addUpvote(Report report){
        UpvoteRequest request = new UpvoteRequest(getApplicationContext());
        request.addAUpvote(report);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
