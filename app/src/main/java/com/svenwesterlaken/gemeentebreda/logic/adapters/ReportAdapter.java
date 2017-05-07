package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;

import java.util.ArrayList;

/**
 * Created by lukab on 7-5-2017.
 */

public class ReportAdapter extends ArrayAdapter {

    private Context mContext;

    private ArrayList mReportList;


    public ReportAdapter(Context context, ArrayList reports) {

        super(context, 0, reports);

    }


    public View getView(int position, View convertView, ViewGroup parent) {

        getItem(position);

        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_report_row, parent, false);
        }

        ImageView reportImage = (ImageView) convertView.findViewById(R.id.reportNumberTxt);

        //heel veel info uit database per report

        return convertView;

    }

}