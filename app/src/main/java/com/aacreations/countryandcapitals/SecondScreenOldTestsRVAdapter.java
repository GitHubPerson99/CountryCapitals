package com.aacreations.countryandcapitals;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecondScreenOldTestsRVAdapter extends RecyclerView.Adapter<SecondScreenOldTestsRVAdapter.OldTestsViewHolder> {
    private static final String TAG = "AnswerRecyclerAdapter";
    List<CountryCapital> oldTests;

    public SecondScreenOldTestsRVAdapter(List<CountryCapital> oldTests) {
        super();
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        this.oldTests = oldTests;
    }

    @NonNull
    @Override
    public SecondScreenOldTestsRVAdapter.OldTestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_screen_old_tests_item, parent, false);
        return new OldTestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SecondScreenOldTestsRVAdapter.OldTestsViewHolder holder, int position) {
        CountryCapital currentCountryCapital = oldTests.get(position);
        String countryName = currentCountryCapital.getCountryName();
        String userAnswer = currentCountryCapital.getAnswerSent();
        String correctAnswer = currentCountryCapital.getCapitalName();

        holder.countryQuestion.setText(countryName);
        holder.userAnswer.setText(userAnswer);
        holder.correctAnswer.setText(correctAnswer);
    }

    @Override
    public int getItemCount() {
        return oldTests.size();
    }

    static class OldTestsViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AnswerViewHolder";

        TextView countryQuestion;
        TextView userAnswer;
        TextView correctAnswer;

        public OldTestsViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "AnswerViewHolder: starts");

            countryQuestion = itemView.findViewById(R.id.country);
            userAnswer = itemView.findViewById(R.id.user_answer);
            correctAnswer = itemView.findViewById(R.id.correct_answer);
        }
    }
}
