package com.aacreations.countryandcapitals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aacreations.countryandcapitals.databinding.ActivityLearnBinding;

import java.util.List;

public class LearnActivity extends AppCompatActivity {
    private static final String TAG = "LearnActivity";

    ActivityLearnBinding binding;
    List<CountryCapital> allItems;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();

        String title = bundle.getString(MainActivity.CONTINENT);
        binding.toolbar.setTitle(title);


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SQLiteCountryCapitalsDAO sqLiteCountryCapitalsDAO = SQLiteCountryCapitalsDAO.getInstance(this);
        allItems = sqLiteCountryCapitalsDAO.readAllFromCountryCapitals(title);
        Log.d(TAG, "onCreate: " + allItems.size());

        adapter = new ViewPagerAdapter(allItems, this);
        binding.learnPager.setAdapter(adapter);

    }

    // inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
