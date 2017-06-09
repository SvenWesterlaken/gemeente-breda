package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.AddReportRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ConfirmationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ImageActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.VideoActivity;

public class NewReportSummaryFragment extends Fragment implements NewReportActivity.SummaryFragmentListener{

    private Category category;
    private String description;
    private Location location;
    private Media media;
    private Button sendBTN;
    private String name, email, phone;
    private TextView authorName, authorEmail, authorPhone, descriptionTV, categoryTV, locationTV;
    private ImageView thumbnail;

    private final static int MEDIA_PICTURE = 1;
    private final static int MEDIA_VIDEO = 2;

    private View rootView;

    private RequestQueue queue;
    private Gson gson;
    private String URL;
    private Report newReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_report_summary, container, false);
        sendBTN = (Button) rootView.findViewById(R.id.summary_BTN_send);
        final DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext());

//        queue = Volley.newRequestQueue(rootView.getContext());
//        GsonBuilder gbuilder = new GsonBuilder();
//        gson = gbuilder.create();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name = preferences.getString("pref_name", null);
        email = preferences.getString("pref_email", null);
        phone = preferences.getString("pref_phone", null);

        authorName = (TextView) rootView.findViewById(R.id.summary_TV_authorName);
        authorEmail = (TextView) rootView.findViewById(R.id.summary_TV_authorEmail);
        authorPhone = (TextView) rootView.findViewById(R.id.summary_TV_authorPhone);
        descriptionTV = (TextView) rootView.findViewById(R.id.summary_TV_description);
        categoryTV = (TextView) rootView.findViewById(R.id.summary_TV_reportTitle);
        locationTV = (TextView) rootView.findViewById(R.id.summary_TV_address);
        thumbnail = (ImageView) rootView.findViewById(R.id.summary_IV_image);

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ConfirmationActivity.class);

                Report report = ((NewReportActivity)getActivity()).getNewReport();

                User user = handler.getUser(1);

                if (user == null) {
                    user = new User(1, name, email, phone);
                    handler.addUser(user);
                }

                Location location = report.getLocation();
                Category category = report.getCategory();

                Report reportNew = new Report(handler.getAllReports().size()+1, user, location, report.getDescription(), category, report.getLocationID(), "open");
            
                
                AddReport(reportNew);
                
                i.putExtra("REPORT", reportNew);

                getActivity().finish();
                startActivity(i);
            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(media != null) {
                    if(media.getType() == MEDIA_PICTURE) {
                        Intent intent = new Intent(getActivity(), ImageActivity.class);
                        intent.putExtra("filepath", media.getFilePath());
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), thumbnail, "media_preview");
                        startActivity(intent, options.toBundle());
                    } else if (media.getType() == MEDIA_VIDEO) {
                        Intent intent = new Intent(getActivity(), VideoActivity.class);
                        intent.putExtra("uri", media.getUri());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext(), "Nog geen media toegevoegd", Toast.LENGTH_SHORT).show();
                }
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
        NewReportActivity activity = ((NewReportActivity)getActivity());

        Report report = activity.getNewReport();
        description = report.getDescription();
        category = report.getCategory();
        location = report.getLocation();
        media = report.getMedia();

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

        if (description != null) {
            if(!description.isEmpty()) {
                descriptionTV.setText(description);
            } else {
                descriptionTV.setText(R.string.summary_missing_description);
            }
        } else {
            descriptionTV.setText(R.string.summary_missing_description);
        }

        if (category != null) {
            categoryTV.setText(category.getCategoryName());
            categoryTV.setTextColor(descriptionTV.getTextColors().getDefaultColor());

        } else {
            categoryTV.setText(R.string.summary_missing_category);
            categoryTV.setTextColor(ContextCompat.getColor(activity, R.color.md_edittext_error));
        }

        if (location != null) {

            locationTV.setText(location.getStreet());

            locationTV.setTextColor(descriptionTV.getTextColors().getDefaultColor());
        } else {
            locationTV.setText(R.string.summary_missing_location);
            locationTV.setTextColor(ContextCompat.getColor(activity, R.color.md_edittext_error));
        }

        if(media != null) {
            if(media.getType() == MEDIA_PICTURE) {
                Glide.with(getContext()).load(media.getFilePath()).into(thumbnail);
            } else if (media.getType() == MEDIA_VIDEO) {
                Glide.with(getContext()).load(media.getUri()).into(thumbnail);
            }
        }

        if (media != null && category != null && location != null && !name.equalsIgnoreCase("Onbekend") && !email.equalsIgnoreCase("Onbekend")) {
            sendBTN.setEnabled(true);
        } else {
            sendBTN.setEnabled(false);
        }
    }
    
    public void AddReport(Report report){
        
        AddReportRequest reportRequest = new AddReportRequest(getActivity().getApplicationContext());
        reportRequest.addAReport(report);
    }


        }


        

