package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ReportActivity;

/**
 * Created by Sven Westerlaken on 12-5-2017.
 */

public class LoginTwoFragment extends Fragment {
    private EditText email, username;
    private Button submitBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        email = (EditText) rootView.findViewById(R.id.login_ET_email);
        username = (EditText) rootView.findViewById(R.id.login_ET_username);
        submitBTN = (Button) rootView.findViewById(R.id.login_btn_submit);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();

                if (!TextUtils.isEmpty(email.getText())) {
                    editor.putString("pref_email", email.getText().toString());
                }

                if (!TextUtils.isEmpty(email.getText())) {
                    editor.putString("pref_name", username.getText().toString());
                }

                editor.commit();


                Intent i = new Intent(getActivity().getApplicationContext(), ReportActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        return rootView;
    }

}
