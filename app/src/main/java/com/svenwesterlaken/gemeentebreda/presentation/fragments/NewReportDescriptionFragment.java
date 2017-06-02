package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;


public class  NewReportDescriptionFragment extends Fragment implements View.OnClickListener{
    private DescriptionChangedListener mListener;
    private EditText descriptionView;
    private Button confirmBTN, skipBTN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_report_description, container, false);

        descriptionView = (EditText) rootView.findViewById(R.id.description_ET_description);
        confirmBTN = (Button) rootView.findViewById(R.id.description_BTN_submit);
        skipBTN = (Button) rootView.findViewById(R.id.description_BTN_skip);

        confirmBTN.setOnClickListener(this);
        skipBTN.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        NewReportActivity activity = (NewReportActivity) getActivity();

        if(v.getId() == R.id.description_BTN_submit) {
            String description = descriptionView.getText().toString();
            mListener.setDescription(description);
        }

        if(activity.swipingIsEnabled()) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.new_report_enable_swiping, Toast.LENGTH_SHORT).show();
            activity.scrollToNext();
            activity.enableSwiping();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity){
            a = (Activity) context;
        }        
        try {
            mListener = (DescriptionChangedListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement DescriptionChangedListener");
        }
    }



    public interface DescriptionChangedListener {
        void setDescription(String t);
    }
}
