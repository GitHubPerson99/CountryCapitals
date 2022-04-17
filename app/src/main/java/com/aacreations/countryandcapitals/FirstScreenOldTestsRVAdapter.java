package com.aacreations.countryandcapitals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FirstScreenOldTestsRVAdapter extends RecyclerView.Adapter<FirstScreenOldTestsRVAdapter.OldTestsViewHolder> {
    private static final String TAG = "AnswerRecyclerAdapter";
    private List<CountryCapital> oldTests;
    private FSRVAdapterOnItemClickListener listener;

    public FirstScreenOldTestsRVAdapter(Context context, @NonNull List<CountryCapital> oldTests, FSRVAdapterOnItemClickListener listener) {
        super();
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        this.oldTests = oldTests;

        this.listener = listener;
    }

    @NonNull
    @Override
    public FirstScreenOldTestsRVAdapter.OldTestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_screen_old_tests_item, parent, false);
        return new OldTestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirstScreenOldTestsRVAdapter.OldTestsViewHolder holder, int position) {
        CountryCapital currentCountryCapital = oldTests.get(position);
        Log.d(TAG, "onBindViewHolder: " + currentCountryCapital.ttoString());
        int testId = currentCountryCapital.getTestId();
        Log.d(TAG, "onBindViewHolder: testId = " + testId);
        int totalQuestions = currentCountryCapital.getTotalQuestions();
        Log.d(TAG, "onBindViewHolder: totalQuestions = " + totalQuestions);
        int correctAnswer = currentCountryCapital.getQuestionsCorrect();
        Log.d(TAG, "onBindViewHolder: correctAnswer = " + correctAnswer);
        int questionsWrong = currentCountryCapital.getQuestionsWrong();
        Log.d(TAG, "onBindViewHolder: questionsWrong = " + questionsWrong);
        String percentage = currentCountryCapital.getPercentage();
        Log.d(TAG, "onBindViewHolder: percentage = " + percentage);
//        String passFail = currentCountryCapital.getPassFail();
        String passFail = "fail";

        holder.testId.setText(MainAccess.toString(testId));
        holder.totalQuestions.setText(MainAccess.toString(totalQuestions));
        holder.correctAnswers.setText(MainAccess.toString(correctAnswer));
        holder.wrongAnswers.setText(MainAccess.toString(questionsWrong));
        holder.percentage.setText(percentage);
        holder.passFail.setText(passFail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(oldTests.get(holder.getAdapterPosition()).getTestId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return oldTests.size();
    }

    public interface FSRVAdapterOnItemClickListener {
        void onClick(int id);
    }

    static class OldTestsViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AnswerViewHolder";

        TextView testId;
        TextView totalQuestions;
        TextView correctAnswers;
        TextView wrongAnswers;
        TextView percentage;
        TextView passFail;

        public OldTestsViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "AnswerViewHolder: starts");

            testId = itemView.findViewById(R.id.testId);
            totalQuestions = itemView.findViewById(R.id.total_question);
            correctAnswers = itemView.findViewById(R.id.correct_questions);
            wrongAnswers = itemView.findViewById(R.id.wrong_answers);
            percentage = itemView.findViewById(R.id.percentage);
            passFail = itemView.findViewById(R.id.pass_fail_column);
        }
    }
}
