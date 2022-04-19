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
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class FirstScreenActivity extends AppCompatActivity {

    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}