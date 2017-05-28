package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ConfirmationActivity;


public class NewReportLocationFragment extends Fragment {
    private LocationChangedListener mListener;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_location, container, false);
        ConstraintLayout title = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_choose);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getTargetFragment(), NewReportNewLocationFragment.class);
//                startActivity(i);
                NewReportNewLocationFragment nextFrag= new NewReportNewLocationFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.viewpager_content, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }


        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity){
            a=(Activity) context;
        }
        try {
            mListener = (LocationChangedListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement LocationChangedListener");
        }
    }

    public interface LocationChangedListener {
        void setLocation(Location t);
    }
}
