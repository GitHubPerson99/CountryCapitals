package com.aacreations.countryandcapitals;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import org.jetbrains.annotations.Contract;

import java.util.List;

public class OldTestsActivity extends AppCompatActivity implements FirstScreenOldTestsRVAdapter.FSRVAdapterOnItemClickListener{

    private static final String TAG = "OldTestsActivity";

    private FirstScreenOldTestsRVAdapter mFirstScreenAdapter;
    private SecondScreenOldTestsRVAdapter mSecondScreenAdapter;
    private List<CountryCapital> mOldTestsIdList;
    private List<CountryCapital> mOldTestsList;
    private SQLiteCountryCapitalsDAO mDatabaseHelper;
    NavigationView navigationView;
    View firstScreenView;
    View secondScreenView;

    private java.text.DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_tests);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        firstScreenView = findViewById(R.id.first);
        secondScreenView = findViewById(R.id.second);

        mDatabaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);

        initFirstScreen();

        dateFormat = DateFormat.getDateFormat(this);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(OldTestsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        });

        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(OldTestsActivity.this, AboutActivity.class);
                intent.putExtra(MainActivity.ABOUT_BUNDLE_CLASS, OldTestsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    @Override
    public void onClick(int id) {
        initSecondScreenScreen(id);
    }

    @Override
    public void onBackPressed() {

        if (secondScreenView.getVisibility() == View.VISIBLE) {
            initFirstScreen();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                if (secondScreenView.getVisibility() == View.VISIBLE) {
                    initFirstScreen();
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFirstScreen() {
        firstScreenView.setVisibility(View.VISIBLE);
        secondScreenView.setVisibility(View.GONE);

        mDatabaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);
        mOldTestsIdList = mDatabaseHelper.readIdFromOldTests();

        RecyclerView recyclerView = findViewById(R.id.old_tests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mFirstScreenAdapter == null) {
            mFirstScreenAdapter = new FirstScreenOldTestsRVAdapter(this, mOldTestsIdList, this);
        }

        recyclerView.setAdapter(mFirstScreenAdapter);
    }

    private void initSecondScreenScreen(int id) {
        firstScreenView.setVisibility(View.GONE);
        secondScreenView.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = findViewById(R.id.second_screen_old_tests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOldTestsList = mDatabaseHelper.readAllFromOldTests(true, id);
        if (mSecondScreenAdapter == null) {
            mSecondScreenAdapter = new SecondScreenOldTestsRVAdapter(mOldTestsList);
        }

        recyclerView.setAdapter(mSecondScreenAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CountryCapital current = mOldTestsList.get(0);

        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.addData(new SimplePieInfo(current.getQuestionsCorrect(), Color.parseColor("#AA00FF00")));
        config.addData(new SimplePieInfo(current.getQuestionsWrong(), Color.parseColor("#AAFF0000")));
        config.setDuration(1000);
        config.strokeMode(false);

        AnimatedPieView pieChart = findViewById(R.id.second_screen_pie_view);

        pieChart.applyConfig(config);
        pieChart.start();

        TextView passFail = findViewById(R.id.pass_fail_name);
        passFail.setVisibility(View.VISIBLE);
        TextView totalQuestions = findViewById(R.id.total_question_name);
        totalQuestions.setVisibility(View.VISIBLE);
        TextView correctAnswer = findViewById(R.id.correct_answers_name);
        correctAnswer.setVisibility(View.VISIBLE);
        TextView wrongAnswers = findViewById(R.id.wrong_questions_name);
        wrongAnswers.setVisibility(View.VISIBLE);
        TextView testId = findViewById(R.id.testId_name);
        testId.setVisibility(View.VISIBLE);
        TextView percentage = findViewById(R.id.percentage_name);
        percentage.setVisibility(View.VISIBLE);
        TextView timeTakenAt = findViewById(R.id.time_taken_at);
        timeTakenAt.setVisibility(View.VISIBLE);
        TextView dateTakenAt = findViewById(R.id.date_taken_at);
        dateTakenAt.setVisibility(View.VISIBLE);
        TextView timeTaken = findViewById(R.id.time_taken);
        timeTaken.setVisibility(View.VISIBLE);

//        passFail.setText(current.getPassFail());
        totalQuestions.setText(MainAccess.toString("Total Questions: ", current.getTotalQuestions()));
        correctAnswer.setText(MainAccess.toString("Questions Correct: ", current.getQuestionsCorrect()));
        wrongAnswers.setText(MainAccess.toString("Questions wrong: ", current.getQuestionsWrong()));
        testId.setText(MainAccess.toString("Test ", current.getTestId()));
        percentage.setText(current.getPercentage());
        timeTakenAt.setText("Time Taken At: " + current.getTime());
        dateTakenAt.setText("Date Take At: " + dateFormat.format(current.getDate()));
        timeTaken.setText("Time Taken: " + current.getTimeTaken());

        Log.d(TAG, "initSecondScreenScreen: " + current.getTimeTaken());

        Log.d(TAG, "initSecondScreenScreen: Visibility = " + timeTaken.getVisibility());
        Log.d(TAG, "initSecondScreenScreen: Text = " + timeTaken.getText());

        Log.d(TAG, "initSecondScreenScreen: " + current.ttoString());

    }

}
