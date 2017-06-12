package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.svenwesterlaken.gemeentebreda.R;
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
    private final static int MEDIA_PICTURE = 1;
    private final static int MEDIA_VIDEO = 2;

    private static String defaultUserValue = "Onbekend";

    private DatabaseHandler handler;

    private Media media;
    private Button sendBTN;
    private String name;
    private String email;
    private String phone;
    private TextView authorName;
    private TextView authorEmail;
    private TextView authorPhone;
    private TextView descriptionTV;
    private TextView categoryTV;
    private TextView locationTV;
    private ImageView thumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_summary, container, false);
        sendBTN = (Button) rootView.findViewById(R.id.summary_BTN_send);
        handler = new DatabaseHandler(getActivity().getApplicationContext());

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
                i.putExtra("REPORT", createNewReport());
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

    private Report createNewReport() {
        Report report = ((NewReportActivity)getActivity()).getNewReport();

        User user = handler.getUser(1);

        if (user == null) {
            user = new User(1, name, email, phone);
            handler.addUser(user);
        }

        Location location = report.getLocation();
        Category category = report.getCategory();

        return new Report( handler.getLastReportId() + 1, user, location, report.getDescription(), category, report.getLocationID(), "open", 1);
    }

    private void processDescription(String d) {
        if (d != null) {
            if(!d.isEmpty()) {
                descriptionTV.setText(d);
            } else {
                descriptionTV.setText(R.string.summary_missing_description);
            }
        } else {
            descriptionTV.setText(R.string.summary_missing_description);
        }
    }

    private void processPhoneNumber() {
        if(phone != null) {
            if (phone.equals(defaultUserValue)) {
                authorPhone.setVisibility(View.GONE);
            } else {
                authorPhone.setText(phone);
            }
        } else {
            authorPhone.setVisibility(View.GONE);
        }
    }

    private void processCategory(Category c, Activity a) {
        if (c != null) {
            categoryTV.setText(c.getCategoryName());
            categoryTV.setTextColor(descriptionTV.getTextColors().getDefaultColor());

        } else {
            categoryTV.setText(R.string.summary_missing_category);
            categoryTV.setTextColor(ContextCompat.getColor(a, R.color.md_edittext_error));
        }
    }

    private void processLocation(Location l, Activity a) {
        if (l != null) {

            locationTV.setText(l.getStreet() + ", " + l.getCity());

            locationTV.setTextColor(descriptionTV.getTextColors().getDefaultColor());
        } else {
            locationTV.setText(R.string.summary_missing_location);
            locationTV.setTextColor(ContextCompat.getColor(a, R.color.md_edittext_error));
        }
    }

    private void processMedia(Media m) {
        if(media != null) {
            if(media.getType() == MEDIA_PICTURE) {
                Glide.with(getContext()).load(media.getFilePath()).into(thumbnail);
            } else if (media.getType() == MEDIA_VIDEO) {
                Glide.with(getContext()).load(media.getUri()).into(thumbnail);
            }
        }
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
        String description = report.getDescription();
        Category category = report.getCategory();
        Location location = report.getLocation();
        media = report.getMedia();

        authorName.setText(name);
        authorEmail.setText(email);

        processPhoneNumber();
        processDescription(description);
        processCategory(category, activity);
        processLocation(location, activity);
        processMedia(media);

        if (media != null && category != null && location != null && !name.equalsIgnoreCase(defaultUserValue) && !email.equalsIgnoreCase(defaultUserValue)) {
            sendBTN.setEnabled(true);
        } else {
            sendBTN.setEnabled(false);
        }
    }
    



}


        

