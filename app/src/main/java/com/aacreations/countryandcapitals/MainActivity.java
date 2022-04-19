package com.aacreations.countryandcapitals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    View optionFragment;
    View mainActivityFragment;
    View selectFragment;
    GridView continents;
    Slider slider;
    String continent = MainAccess.Options.ALL;
    String options;
    final String[] continentArray = new String[]{MainAccess.Options.AFRICA, MainAccess.Options.ASIA, MainAccess.Options.EUROPE,
            MainAccess.Options.NORTH_AMERICA, MainAccess.Options.OCEANA, MainAccess.Options.SOUTH_AMERICA, MainAccess.Options.ALL};
    private NavigationView navigationView;

    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whatIsOpen = "home";
        Log.d(TAG, "onCreate: started");

        // set the handler to access the toast
        mHandler = new Handler();

        // check if the instance was copied previously
//        if (savedInstanceState != null) {
//            copied = savedInstanceState.getBoolean(COPIED_ID);
//        }
        // because I'm putting update databases, copied should be false
        copied = false;

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initNavigationDrawer(true);

        initOptionsFragment();
        initSlider();
        initContinentsFragment();
        initSliderFragment();


        // open the database and copy the database
        mDatabaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);
        File database = getApplicationContext().getDatabasePath(SQLiteCountryCapitalsDAO.DB_NAME);
        if (!database.exists()) {
            mDatabaseHelper.getWritableDatabase();
            // Copy db
            if (copyDatabase()) {
                Log.d(TAG, "onCreate: copy successful");
            } else {
                Log.d(TAG, "onCreate: copy unsuccessful");
            }
        }

    }

    private void initOptionsFragment() {
        // set the base screens
        // first screen that shows: quiz, practise and learn
        optionFragment = findViewById(R.id.options);
        // make it visible as its the first screen
        optionFragment.setVisibility(View.VISIBLE);

    }

    private void initContinentsFragment() {
        // second screen that shows the continents
        mainActivityFragment = findViewById(R.id.fragment);
        // make it invisible
        mainActivityFragment.setVisibility(View.GONE);
        // continents in this screen
        continents = findViewById(R.id.continents);

        // set the gridView
        GridViewAdapter adapter = new GridViewAdapter(this, continentArray);
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
                optionFragment.setVisibility(View.GONE);
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
                    Toast toast = Toast.makeText(getApplicationContext(), "There is only " + completeItems.size() + " countries in " + continent + " and " + (int) slider.getValue() + " is too much", Toast.LENGTH_SHORT);
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

    public void initNavigationDrawer(boolean visible) {
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setVisibility(visible ? View.VISIBLE : View.GONE);

        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, OldTestsActivity.class);
            startActivity(intent);
            finish();
            return true;
        });

        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            intent.putExtra(ABOUT_BUNDLE_CLASS, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (toggle == null) {
//            toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
//            Drawable myIcon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_menu_24);
//            toggle.setHomeAsUpIndicator(myIcon);
//            drawer.addDrawerListener(toggle);
//            toggle.syncState();
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        if (!visible) {
//            drawer.removeDrawerListener(toggle);
//            Drawable myIcon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_arrow_back_24);
//            toggle.setHomeAsUpIndicator(myIcon);
//            toggle.syncState();
//            toggle = null;
//        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.options)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.options);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
                        optionFragment.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Toolbar toolbar = findViewById(R.id.toolbar);
                            toolbar.setTitle(R.string.company);
                        }
                        mainActivityFragment.setVisibility(View.GONE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        whatIsOpen = "home";
                        initNavigationDrawer(true);
                        return true;
                    case "slider":
                        optionFragment.setVisibility(View.GONE);
                        mainActivityFragment.setVisibility(View.VISIBLE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        whatIsOpen = "list";
                        initNavigationDrawer(true);
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
                optionFragment.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    toolbar.setTitle(R.string.company);
                }
                mainActivityFragment.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                whatIsOpen = "home";
                break;
            case "slider":
                optionFragment.setVisibility(View.GONE);
                mainActivityFragment.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                whatIsOpen = "list";
                break;
            default:
                super.onBackPressed();
        }
    }

    /**
     * copies the database into internal storage
     * @return whether the database is copied or not
     */
    private boolean copyDatabase() {
        if (!copied) {
            try {

                InputStream inputStream = getAssets().open(SQLiteCountryCapitalsDAO.DB_NAME);
                String outFileName = SQLiteCountryCapitalsDAO.DB_LOCATION + SQLiteCountryCapitalsDAO.DB_NAME;
                OutputStream outputStream = new FileOutputStream(outFileName);
                byte[] buff = new byte[1024];
                int length;
                while ((length = inputStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                Log.d("MainActivity", "DB copied");
                copied = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "copyDatabase: FAILED TO COPY");
            }

        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.options);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}