package com.aacreations.countryandcapitals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FirstScreenActivity extends AppCompatActivity {
    private static final String TAG = "FirstScreenActivity";

    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    private boolean copied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // check if the instance was copied previously
//        if (savedInstanceState != null) {
//            copied = savedInstanceState.getBoolean(COPIED_ID);
//        }
        // because I'm putting update databases, copied should be false
        copied = false;

        File database = getApplicationContext().getDatabasePath(SQLiteCountryCapitalsDAO.DB_NAME);
        if (!database.exists()) {
            SQLiteCountryCapitalsDAO databaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);
            databaseHelper.getWritableDatabase();
            // Copy db
            if (copyDatabase()) {
                Log.d(TAG, "onCreate: copy successful");
            } else {
                Log.d(TAG, "onCreate: copy unsuccessful");
            }
        }

        initNavigationDrawer(true);

        toolbar.setTitle(R.string.app_name);
    }

    protected void examPracticeLearn(String option) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.OPTIONS, option);
        startActivity(intent);
        finish();
    }

    public void initNavigationDrawer(boolean visible) {
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setVisibility(visible ? View.VISIBLE : View.GONE);

        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(FirstScreenActivity.this, OldTestsActivity.class);
            startActivity(intent);
            finish();
            return true;
        });

        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(FirstScreenActivity.this, AboutActivity.class);
            intent.putExtra(MainActivity.ABOUT_BUNDLE_CLASS, FirstScreenActivity.class);
            startActivity(intent);
            finish();
            return true;
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.first)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.options);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.options);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

}