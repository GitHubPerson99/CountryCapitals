package com.aacreations.countryandcapitals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.slider.Slider;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String COPIED_ID = "Copy";
    public static final String QUESTIONS = "questions";
    public static final String OPTIONS = "Options";
    public static final String CONTINENT = "CONTINENT";

    public static final String ABOUT_BUNDLE_CLASS = "Class";

    private SQLiteCountryCapitalsDAO mDatabaseHelper;

    private boolean copied = false;
    private boolean toastShowing = false;
    private Handler mHandler;
    protected String whatIsOpen;

    View mainActivityFragment;
    View selectFragment;
    GridView continents;
    Slider slider;
    String continent = MainAccess.Options.ALL;
    String options;
    final String[] continentArray = new String[]{MainAccess.Options.AFRICA, MainAccess.Options.ASIA, MainAccess.Options.OCEANA, MainAccess.Options.EUROPE,
            MainAccess.Options.NORTH_AMERICA, MainAccess.Options.SOUTH_AMERICA, MainAccess.Options.ALL};
    final String[] continentPicturesArray = new String[]{"africa", "asia", "australia", "europe", "north_america", "south_america", "south_america"};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whatIsOpen = "list";
        Log.d(TAG, "onCreate: started");

        Bundle bundle = getIntent().getExtras();
        options = bundle.getString(OPTIONS);

        // set the handler to access the toast
        mHandler = new Handler();

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSlider();
        initContinentsFragment();
        initSliderFragment();


        // open the database and copy the database
        mDatabaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);

    }

    private void initContinentsFragment() {
        // second screen that shows the continents
        mainActivityFragment = findViewById(R.id.fragment);
        // make it invisible
        mainActivityFragment.setVisibility(View.VISIBLE);
        // continents in this screen
        continents = findViewById(R.id.continents);

        // set the gridView
        GridViewAdapter adapter = new GridViewAdapter(this, continentArray, continentPicturesArray);
        continents.setAdapter(adapter);

        // when an item is clicked...
        continents.setOnItemClickListener((parent, view, position, id) -> {
            // ...get the continent...
            continent = continentArray[position];
            if (options.equals(MainAccess.Options.LEARN)) {
                // ... and if the option that you selected in the
                // first screen is the learn button "teleport" to
                // the LearnActivity class
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                Bundle learnBundle = new Bundle();
                learnBundle.putString(CONTINENT, continent);
                intent = intent.putExtras(learnBundle);
                startActivity(intent);
                finish();
            } else {
                // ...otherwise make the slider visible
                mainActivityFragment.setVisibility(View.GONE);
                selectFragment.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                // set the slider value to 5
                slider.setValue(5);

                // the slider is open
                whatIsOpen = "slider";
            }
        });
    }

    private void initSliderFragment() {
        // third screen that shows the slider
        selectFragment = findViewById(R.id.select);
        // make it invisible
        selectFragment.setVisibility(View.GONE);

        // next button in this screen
        Button next = findViewById(R.id.next);

        // next button on the slider screen
        next.setOnClickListener(v -> {
            // if a toast is showing the app wont let you slide the slider or press the button
            // continue when the toast is gone
            if (!toastShowing) {
                // the complete items in the list for a specific continent
                List<CountryCapital> completeItems = mDatabaseHelper.readAllFromCountryCapitals(continent);
                // if you selected 30 questions and there was only
                // 15 countries the go into the if statement
                if (slider.getValue() > completeItems.size()) {
                    // shows a toast saying: There is only __ countries in __ and __ is to much
                    String current = continent;
                    if (current.equals(MainAccess.Options.OCEANA)) {
                        current = "Australia";
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "There is only " + completeItems.size() + " countries in " + current + " and " + (int) slider.getValue() + " is too much", Toast.LENGTH_SHORT);
                    // show the toast
                    toast.show();
                    // A TOAST IS SHOWING!
                    toastShowing = true;
                    // after 0.8 seconds remove the toast
                    mHandler.postDelayed(() -> {
                        toast.cancel();
                        toastShowing = false;
                    }, 800);
                    return;
                }
                // go to the QuizActivity class
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(QUESTIONS, (int) slider.getValue());
                bundle.putString(CONTINENT, continent);
                bundle.putString(OPTIONS, options);
                intent = intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initSlider() {
        // slider in this screen
        slider = findViewById(R.id.slider);
        // amount of questions in this screen
        TextView questions = findViewById(R.id.questions);
        // sets the text view in the slider screen
        questions.setText("There will be 5 questions");

        // sets the slider
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            slider1.setValue(value);
            questions.setText("There will be " + (int) value + " questions");
        });
        slider.getValue();
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
            // if home button is pressed go back
            case android.R.id.home:
                selectFragment.setVisibility(View.GONE);
                switch (whatIsOpen) {
                    case "list":
                        startActivity(new Intent(this, FirstScreenActivity.class));
                        finish();
                        return true;
                    case "slider":
                        mainActivityFragment.setVisibility(View.VISIBLE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        whatIsOpen = "list";
                        return true;
                    case "home":
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(COPIED_ID, copied);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // if back button is pressed go back
        selectFragment.setVisibility(View.GONE);
        switch (whatIsOpen) {
            case "list":
                startActivity(new Intent(this, FirstScreenActivity.class));
                finish();
                break;
            case "slider":
                mainActivityFragment.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                whatIsOpen = "list";
                break;
            default:
                super.onBackPressed();
        }
    }

}