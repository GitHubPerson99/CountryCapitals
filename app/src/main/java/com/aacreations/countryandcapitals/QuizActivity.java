package com.aacreations.countryandcapitals;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

    public int currentScore = 0;
    View quiz;
    View answers;
    View viewAnswers;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Button viewFlag;
    ImageView flag;
    int correctAnswer;
    List<CountryCapital> currentQuestions;
    List<CountryCapital> allCountryCapitals;
    TextView question;
    AnimatedPieView pieChart;
    TextView score;
    RecyclerView recyclerView;
    private int seconds = 0;
    private String whatIsOpen;
    private Handler mHandler;
    private boolean toastShowing = false;
    private int currentQuestion = -1;
    private String options;
    private boolean breakLoop = false;
    private SQLiteCountryCapitalsDAO mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabaseHelper = SQLiteCountryCapitalsDAO.getInstance(this);
        mDatabaseHelper.getWritableDatabase();

        whatIsOpen = "home";

        quiz = findViewById(R.id.quiz);
        quiz.setVisibility(View.VISIBLE);
        answers = findViewById(R.id.answers);
        answers.setVisibility(View.GONE);
        viewAnswers = findViewById(R.id.answers_layout);
        viewAnswers.setVisibility(View.GONE);

        mHandler = new Handler();

        runTimer();

        correctAnswer = R.id.answer1;
        Bundle bundle = getIntent().getExtras();
        int questions = bundle.getInt(MainActivity.QUESTIONS);
        String continent = bundle.getString(MainActivity.CONTINENT);
        options = bundle.getString(MainActivity.OPTIONS);
        allCountryCapitals = mDatabaseHelper.readAllFromCountryCapitals(continent);
        currentQuestions = MainAccess.RandomGenerator.randomizeList(allCountryCapitals, questions);
        Log.d(TAG, "onCreate: " + allCountryCapitals.size());

        // set the views
        question = findViewById(R.id.question);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        score = findViewById(R.id.score);
        pieChart = findViewById(R.id.pie_chart);
        viewFlag = findViewById(R.id.show_flag);
        flag = findViewById(R.id.flag);

        setQuestions(false, "");

        View.OnClickListener answerListener = view -> {
            if (!toastShowing) {
                if (view.getId() == correctAnswer) {
                    currentScore++;
                    if (options.equals(MainAccess.Options.PRACTICE)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "YES, you got the question correct!", Toast.LENGTH_SHORT);
                        toast.show();
                        toastShowing = true;
                        mHandler.postDelayed(() -> {
                            toast.cancel();
                            toastShowing = false;
                            setQuestions(true, ((TextView) view).getText());
                        }, 800);
                    } else {
                        setQuestions(true, ((TextView) view).getText());
                    }
                } else {
                    if (options.equals(MainAccess.Options.PRACTICE)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "No, that is not the correct word", Toast.LENGTH_SHORT);
                        toast.show();
                        toastShowing = true;
                        mHandler.postDelayed(() -> {
                            toast.cancel();
                            toastShowing = false;
                            setQuestions(false, ((TextView) view).getText());
                        }, 800);
                    } else {
                        setQuestions(false, ((TextView) view).getText());
                    }
                }
            }
        };

        answer1.setOnClickListener(answerListener);
        answer2.setOnClickListener(answerListener);
        answer3.setOnClickListener(answerListener);
        answer4.setOnClickListener(answerListener);

        Button viewAnswersButton = findViewById(R.id.view_answers);
        viewAnswersButton.setOnClickListener(v -> {
            quiz.setVisibility(View.GONE);
            answers.setVisibility(View.GONE);
            viewAnswers.setVisibility(View.VISIBLE);
            recyclerView = findViewById(R.id.answers_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(QuizActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            AnswerRecyclerViewAdapter adapter = new AnswerRecyclerViewAdapter(currentQuestions);
            recyclerView.setAdapter(adapter);
            whatIsOpen = "viewAnswers";
        });

        flag.setVisibility(View.GONE);
        viewFlag.setOnClickListener(v -> flag.setVisibility(View.VISIBLE));

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
                Log.d(TAG, "onOptionsItemSelected: " + whatIsOpen);
                switch (whatIsOpen) {
                    case "viewAnswers":
                        quiz.setVisibility(View.GONE);
                        answers.setVisibility(View.VISIBLE);
                        viewAnswers.setVisibility(View.GONE);
                        whatIsOpen = "answers";
                        break;
                    case "answers":
                    case "home":
                        Log.d(TAG, "onOptionsItemSelected: called");
                        Intent intent = new Intent(this, FirstScreenActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (whatIsOpen) {
            case "viewAnswers":
                quiz.setVisibility(View.GONE);
                answers.setVisibility(View.VISIBLE);
                viewAnswers.setVisibility(View.GONE);
                whatIsOpen = "answers";
                break;
            case "answers":
            case "home":
                Intent intent = new Intent(this, FirstScreenActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    /**
     * sets the random questions
     * @param correct
     * @param userInput what the user clicked
     */
    private void setQuestions(boolean correct, @NonNull CharSequence userInput) {
        if (!userInput.equals("")) {
            CountryCapital currentCountryCapital = currentQuestions.get(currentQuestion);
            currentCountryCapital.setCorrect(correct);
            currentCountryCapital.setAnswerSent(userInput.toString());

            currentQuestions.set(currentQuestion, currentCountryCapital);
        }

        currentQuestion++;
        if (currentQuestion >= currentQuestions.size()) {
            setAnswerView();
            return;
        }

        List<String> capitals = MainAccess.RandomGenerator.getRandomCapitals(allCountryCapitals, currentQuestions.get(currentQuestion).getCapitalName());
        Collections.shuffle(capitals);
        int randomNumber = MainAccess.RandomGenerator.getRandom(1, 4);
        switch (randomNumber) {
            case 1:
                correctAnswer = R.id.answer1;
                answer2.setText(capitals.get(0));
                answer3.setText(capitals.get(1));
                answer4.setText(capitals.get(2));
                break;
            case 2:
                correctAnswer = R.id.answer2;
                answer1.setText(capitals.get(0));
                answer3.setText(capitals.get(1));
                answer4.setText(capitals.get(2));
                break;
            case 3:
                correctAnswer = R.id.answer3;
                answer1.setText(capitals.get(0));
                answer2.setText(capitals.get(1));
                answer4.setText(capitals.get(2));
                break;
            case 4:
                correctAnswer = R.id.answer4;
                answer1.setText(capitals.get(0));
                answer2.setText(capitals.get(1));
                answer3.setText(capitals.get(2));
        }
        Button correctAnswerButton = findViewById(correctAnswer);
        correctAnswerButton.setText(currentQuestions.get(currentQuestion).getCapitalName());
        question.setText(String.format("What is the capital of %s?", currentQuestions.get(currentQuestion).getCountryName()));

        flag.setVisibility(View.GONE);
        flag.setImageBitmap(SQLiteCountryCapitalsDAO.getImageAsBitmap(currentQuestions.get(currentQuestion).getFlagId(), getAssets()));
    }

    /**
     * sets the answer view with the pie chart
     */
    public void setAnswerView() {
        breakLoop = true;
        quiz.setVisibility(View.GONE);
        answers.setVisibility(View.VISIBLE);
        viewAnswers.setVisibility(View.GONE);
        TextView score = findViewById(R.id.score);
        score.setText(String.format(Locale.getDefault(),"%d/%d(%d%%)", currentScore, currentQuestions.size(), (int) (((double) currentScore / (double) currentQuestions.size()) * 100)));

        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.addData(new SimplePieInfo(currentScore, Color.parseColor("#AA00FF00")));
        config.addData(new SimplePieInfo(currentQuestions.size() - currentScore, Color.parseColor("#AAFF0000")));
        config.setDuration(1000);
        config.strokeMode(false);

        pieChart.applyConfig(config);
        pieChart.start();
        whatIsOpen = "answers";

        TextView textView = findViewById(R.id.answer_stopwatch);

        int minutes = (seconds % 3600) / 60;
        int secs = QuizActivity.this.seconds % 60;

        String time = String.format(Locale.getDefault(), "Time: %02d:%02d", minutes, secs);

        textView.setText(time);

        long dateInt = System.currentTimeMillis();
        DateFormat formatter = SimpleDateFormat.getTimeInstance();
        Date date = new Date();
        String currentTime = formatter.format(date);
        Log.d(TAG, "setAnswerView: " + currentTime);

        mDatabaseHelper.newTest();
        CountryCapital countryCapital;
        for (int i = 0; i < currentQuestions.size(); i++) {
            countryCapital = currentQuestions.get(i);
            countryCapital.setTestId(mDatabaseHelper.testId);
            countryCapital.setDate(dateInt);
            countryCapital.setTime(currentTime);
            countryCapital.setPercentage("" + (((double) currentScore / (double) currentQuestions.size()) * 100) + "%");
            countryCapital.setTotalQuestions(currentQuestions.size());
            countryCapital.setTimeTaken(String.format(Locale.getDefault(), "%02d:%02d", minutes, secs));
            countryCapital.setQuestionsWrong(currentQuestions.size()-currentScore);
            countryCapital.setQuestionsCorrect(currentScore);
            mDatabaseHelper.create(countryCapital, Table.TABLE_OLD_TESTS);
        }

        TextView passFail = findViewById(R.id.pass_fail);
        passFail.setText(currentQuestions.get(0).getPassFail());

    }

    private void runTimer() {
        TextView textView = findViewById(R.id.stopwatch);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = QuizActivity.this.seconds % 60;

                String time = String.format(Locale.getDefault(), "Time: %02d:%02d", minutes, secs);

                textView.setText(time);

                seconds++;

                if (!breakLoop) {
                    mHandler.postDelayed(this, 1000);
                }

            }
        });

    }

}
