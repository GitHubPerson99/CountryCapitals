package com.aacreations.countryandcapitals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
    private static final String TAG = "GridViewAdapter";

    private Context context;
    private LayoutInflater inflater;
    private final String[] continents;

    public GridViewAdapter(Context context, String[] continents) {
        this.context = context;
        this.continents = continents;
        Log.d(TAG, "GridViewAdapter: " + continents.length);
    }

    @Override
    public int getCount() {
        return continents.length;
    }

    @Override
    public Object getItem(int position) {
        return continents[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: " + position);
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.continent_item, null);

        }

        TextView continent = convertView.findViewById(R.id.continent);
        Log.d(TAG, "getView: " + continents[position]);
        continent.setText(continents[position]);
        if (continents[position].equals(MainAccess.Options.OCEANA)) {
            continent.setText("Australia");
        }
        continent.setHeight(300);
        return convertView;
    }
}
