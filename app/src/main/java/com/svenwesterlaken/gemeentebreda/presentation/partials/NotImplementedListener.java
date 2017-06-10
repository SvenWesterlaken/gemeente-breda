package com.svenwesterlaken.gemeentebreda.presentation.partials;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Sven Westerlaken on 21-5-2017.
 */

public class NotImplementedListener implements MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private Context context;
    private Toast toast;

    public NotImplementedListener(Context c) {
        this.context = c;
        toast = Toast.makeText(context, "Deze functie is nog niet geïmplementeerd", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        toast.show();
        return true;
    }

    @Override
    public void onClick(View v) {
        toast.show();
    }
}
