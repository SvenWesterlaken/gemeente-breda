package com.svenwesterlaken.gemeentebreda.presentation.fragments;

        import android.content.Context;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
=======
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
>>>>>>> 56878d5596fb7587517c3e8c581a913b5c14dbba
        import android.widget.EditText;

        import com.svenwesterlaken.gemeentebreda.R;
        import com.svenwesterlaken.gemeentebreda.domain.Report;


public class  NewReportDescriptionFragment extends Fragment{

<<<<<<< HEAD
    EditText descriptionView;
    Button descriptionbttn;
    String description;
=======
    EditText report;
>>>>>>> 56878d5596fb7587517c3e8c581a913b5c14dbba

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
<<<<<<< HEAD
        final View rootView = inflater.inflate(R.layout.fragment_new_report_description, container, false);

        descriptionView = (EditText) rootView.findViewById(R.id.DescriptionText);
        descriptionbttn = (Button) rootView.findViewById(R.id.DescriptionBttn);

        descriptionbttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


            description = descriptionView.getText().toString();

            Log.i("DESCRIPTION", description);
            }
    });
        return rootView;
}}
=======
        View rootView = inflater.inflate(R.layout.fragment_new_report_description, container, false);

        report = (EditText) rootView.findViewById(R.id.reportText);

        Report r = new Report();

        r.setReport(report.getText().toString());

        return rootView;
    }
}
>>>>>>> 56878d5596fb7587517c3e8c581a913b5c14dbba
