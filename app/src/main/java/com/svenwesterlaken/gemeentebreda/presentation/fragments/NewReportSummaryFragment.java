package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ConfirmationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import java.util.Random;

public class NewReportSummaryFragment extends Fragment implements NewReportActivity.SummaryFragmentListener {

    Category category;
    String description;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_report_summary, container, false);
        final Button send = (Button) rootView.findViewById(R.id.summary_BTN_send);
        final DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), null, null, 1);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = preferences.getString("pref_name", null);
        String email = preferences.getString("pref_email", null);
        String phone = preferences.getString("pref_phone", null);



        final TextView authorName = (TextView) rootView.findViewById(R.id.summary_TV_authorName);
        final TextView authorEmail = (TextView) rootView.findViewById(R.id.summary_TV_authorEmail);
        final TextView authorPhone = (TextView) rootView.findViewById(R.id.summary_TV_authorPhone);

        authorName.setText(name);
        authorEmail.setText(email);

        if(phone != null) {
            if (phone.equals("Onbekend")) {
                authorPhone.setVisibility(View.GONE);
            } else {
                authorPhone.setText(phone);
            }
        } else {
            authorPhone.setVisibility(View.GONE);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ConfirmationActivity.class);

                //handle the sending of the report
                Category testCategory = new Category(handler.getAllReports().size()+1, category.getCategoryName(), category.getCategorySummary());
                handler.addCategory(testCategory);
                User testUser = new User(handler.getAllReports().size(), "mobiel", "naam", "email");
                handler.addUser(testUser);

                Random r = new Random();
                double randomValue1 = 51.619139 + (51.560467 - 51.619139) * r.nextDouble();
                double randomValue2 = 4.730599 + (4.815561 - 4.730599) * r.nextDouble();

                Location testLocation = new Location("straat", "city", 00, "postcode", handler.getAllReports().size()+1, randomValue1, randomValue2);
                handler.addLocation(testLocation);

                Report report = new Report(handler.getAllReports().size()+1, testUser, testLocation, description, testCategory, 2);
                handler.addReport(report);
                i.putExtra("REPORT", report);

                getActivity().finish();
                startActivity(i);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentBecameVisible();
    }

    @Override
    public void fragmentBecameVisible() {
        Report report = ((NewReportActivity)getActivity()).getNewReport();
        description = report.getDescription();
        category = report.getCategory();

        final TextView descriptionTV = (TextView) rootView.findViewById(R.id.summary_TV_description);
        final TextView categoryTV = (TextView) rootView.findViewById(R.id.summary_TV_reportTitle);
        descriptionTV.setText(description);
        if (category != null) {
            categoryTV.setText(category.getCategoryName());
        }
    }
}
