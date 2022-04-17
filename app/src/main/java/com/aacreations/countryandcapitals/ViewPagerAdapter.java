package com.aacreations.countryandcapitals;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<CountryCapital> learnItems;
    private Context context;
    private LayoutInflater inflater;

    ViewPagerAdapter(List<CountryCapital> learnItems, Context context) {
        super();
        this.learnItems = learnItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return learnItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (object instanceof View) {
            return (view == object);
        }
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        CountryCapital current = learnItems.get(position);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.learn_item, container, false);
        TextView country = view.findViewById(R.id.country_name);
        TextView capital = view.findViewById(R.id.capital_name);
        TextView continent = view.findViewById(R.id.continent_name);
        ImageView imageView = view.findViewById(R.id.learn_flag);

        country.setText(current.getCountryName());
        capital.setText(current.getCapitalName());
        continent.setText(current.getContinent());
        imageView.setImageBitmap(SQLiteCountryCapitalsDAO.getImageAsBitmap(current.getFlagId(), context.getAssets()));

        view.setBackgroundColor(Color.parseColor(current.getColour()));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
