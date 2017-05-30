package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;

import java.util.Random;

/**
 * Created by Adonis on 26-5-2017.
 */

public class NewLocationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        final DatabaseHandler handler = new DatabaseHandler(getApplicationContext(), null, null, 1);

        final FloatingSearchView mSearchView = (FloatingSearchView) findViewById(R.id.newLocation_FSV_searchbar);
        final FloatingActionButton confirmBTN = (FloatingActionButton) findViewById(R.id.newLocation_FAB_confirm);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                double randomValue1 = 51.619139 + (51.560467 - 51.619139) * r.nextDouble();
                double randomValue2 = 4.730599 + (4.815561 - 4.730599) * r.nextDouble();
                int randomValue3 = r.nextInt(6);

                Location testLocation = new Location("Geselecteerde straat", "Breda", randomValue3, "4818RA", handler.getAllReports().size()+1, randomValue1, randomValue2);

                Intent intent = new Intent();
                intent.putExtra("location", testLocation);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}