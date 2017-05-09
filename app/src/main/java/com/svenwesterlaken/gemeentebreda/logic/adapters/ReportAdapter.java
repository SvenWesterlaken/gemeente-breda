package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

import java.util.ArrayList;

/**
 * Created by lukab on 7-5-2017.
 */

public class ReportAdapter extends CursorAdapter {

    // Constructor
    public ReportAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, 0);
    }

    // Inflate the new view. Geen databinding
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_report_row, parent, false);
        return view;
    }

    // Hier wortd de data gebind aan de view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //ViewHolder holder = new ViewHolder();
        TextView reportTextView = (TextView) view.findViewById(R.id.ReportText);
        TextView addressTxtView = (TextView) view.findViewById(R.id.AddressTxt);

        //heel veel textviews

        String report = cursor.getString(cursor.getColumnIndex("category"));
        String address = cursor.getString(cursor.getColumnIndex("street"));

        reportTextView.setText(report);
        addressTxtView.setText(address);
    }

//    private Context mContext;
//    private Category category;
//    private Media media;
//    private User user;
//    private Location location;
//    private Report report;
//
//
//    private ArrayList<Report> mReportList;
//
//
//    public ReportAdapter(Context context, ArrayList<Report> reports) {
//
//        super(context, 0, reports);
//
//        mReportList = reports;
//
//    }
//
//    public void testData(){
//
//        category = new Category(1, "Straatverlichting");
//        media = new Media(1);
//        user = new User(1, "0645678900", "arie", "arie@hotmail.com");
//        location = new Location("Van Voorst tot Voorstraat", "Breda", 51, "4814AA", 1);
//        report = new Report(1, user, location, "Kappotte straatlantaarn", media, category);
//
//        mReportList.add(report);
//
//
//    }
//
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        Report report  = getItem(position);
//
//        if( convertView == null ) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_report_row, parent, false);
//        }
//
//        testData();
//
//        ImageView reportImage = (ImageView) convertView.findViewById(R.id.reportNumberTxt);
//        TextView reportTxt = (TextView) convertView.findViewById(R.id.ReportText);
//        TextView descriptionTxt = (TextView) convertView.findViewById(R.id.DescriptionTxt);
//        TextView addressTxt = (TextView) convertView.findViewById(R.id.AddressTxt);
//
//        reportTxt.setText(report.getCategory().getCategoryName());
//        descriptionTxt.setText(report.getDescription());
//        addressTxt.setText(report.getLocation().getStreet() + report.getLocation().getHouseNumber());
//
//        //heel veel info uit database per report
//
//        return convertView;
//
//    }

}