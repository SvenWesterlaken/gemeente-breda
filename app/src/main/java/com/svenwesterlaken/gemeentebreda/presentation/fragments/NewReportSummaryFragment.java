package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ConfirmationActivity;

import java.util.Random;

public class NewReportSummaryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_report_summary, container, false);
        final Button send = (Button) rootView.findViewById(R.id.summary_BTN_send);
        final DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ConfirmationActivity.class);
                //handle the sending of the report
                Category testCategory = new Category(handler.getAllReports().size()+1, "category", "summary");
                handler.addCategory(testCategory);
                User testUser = new User(handler.getAllReports().size(), "mobiel", "naam", "email");
                handler.addUser(testUser);

                Random r = new Random();
                double randomValue1 = 51.619139 + (51.560467 - 51.619139) * r.nextDouble();
                double randomValue2 = 4.730599 + (4.815561 - 4.730599) * r.nextDouble();

                Location testLocation = new Location("straat", "city", 00, "postcode", handler.getAllReports().size()+1, randomValue1, randomValue2);
                handler.addLocation(testLocation);



                handler.addReport(new Report(handler.getAllReports().size()+1, testUser, testLocation, "toegevoegd na klikken knop", testCategory, 2));

                getActivity().finish();
                startActivity(i);
            }
        });

        return rootView;
    }
}
