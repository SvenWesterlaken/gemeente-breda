package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;

import java.util.ArrayList;

/**
 * Created by lukab on 7-5-2017.
 */

public class ReportAdapter extends ArrayAdapter<Report> {


    // Constructor
    public ReportAdapter(Context context, ArrayList<Report> objects) {
        super(context, 0, objects);
    }

    // Hier wortd de data gebind aan de view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Report report = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_report, parent, false);

        }

        TextView reportTextView = (TextView) convertView.findViewById(R.id.ReportText);
        TextView addressTxtView = (TextView) convertView.findViewById(R.id.AddressTxt);
        ImageView  image = (ImageView) convertView.findViewById(R.id.reportImage);
        ImageView image2 =(ImageView) convertView.findViewById(R.id.seenNotifier);
        TextView descriptionTxtView = (TextView) convertView.findViewById(R.id.DescriptionTxt);
        ImageView image4 = (ImageView) convertView.findViewById(R.id.reportNumberTxt);

//        image.setImageDrawable();


        reportTextView.setText(report.getCategory().getCategoryName());
        addressTxtView.setText(report.getLocation().getStreet() + " " + report.getLocation().getHouseNumber());
        descriptionTxtView.setText(report.getDescription());

        return convertView;


    }

}
