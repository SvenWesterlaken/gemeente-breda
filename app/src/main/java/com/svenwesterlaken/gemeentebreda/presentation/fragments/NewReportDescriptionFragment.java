package com.svenwesterlaken.gemeentebreda.presentation.fragments;

        import android.content.Context;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;

        import com.svenwesterlaken.gemeentebreda.R;
        import com.svenwesterlaken.gemeentebreda.domain.Report;


public class  NewReportDescriptionFragment extends Fragment{

    EditText descriptionView;
    Button descriptionbttn;
    String description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
