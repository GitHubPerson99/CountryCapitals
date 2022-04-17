package com.aacreations.countryandcapitals;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswerRecyclerViewAdapter extends RecyclerView.Adapter<AnswerRecyclerViewAdapter.AnswerViewHolder> {
    private static final String TAG = "AnswerRecyclerAdapter";
    List<CountryCapital> countryCapitalList;

    public AnswerRecyclerViewAdapter(List<CountryCapital> countryCapitalList) {
        super();
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        this.countryCapitalList = countryCapitalList;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        CountryCapital currentCountryCapital = countryCapitalList.get(position);
        holder.correct.setText("Incorrect");
        holder.country.setText(currentCountryCapital.getCountryName());
        holder.userAnswer.setText(currentCountryCapital.getAnswerSent());
        holder.answer.setText(currentCountryCapital.getCapitalName());
        if (currentCountryCapital.isCorrect()) {
            holder.answerWas.setText(R.string.and_the_answer_was);
            holder.answer.setVisibility(View.GONE);
            holder.correct.setText("Correct");
        }
    }

    @Override
    public int getItemCount() {
        return countryCapitalList.size();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AnswerViewHolder";

        TextView correct;
        TextView country;
        TextView userAnswer;
        TextView answer;
        TextView answerWas;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "AnswerViewHolder: starts");

            correct = itemView.findViewById(R.id.correct);
            country = itemView.findViewById(R.id.country_value);
            userAnswer = itemView.findViewById(R.id.user_capital_value);
            answer = itemView.findViewById(R.id.answer_capital_value);
            answerWas = itemView.findViewById(R.id.but_the_answer_was);
        }
    }
}
