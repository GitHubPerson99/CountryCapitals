package com.aacreations.countryandcapitals;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aacreations.countryandcapitals.databinding.FragmentFirstScreenBinding;

public class FirstScreenFragment extends Fragment {

    private static final String TAG = "FirstScreenFragment";
    
    FragmentFirstScreenBinding binding;
    
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false);
        
        View view = binding.getRoot();

        FirstScreenActivity activity = (FirstScreenActivity) requireActivity();

        // Buttons
        View.OnClickListener optionListener = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(@NonNull View v) {
                switch (v.getId()) {
                    case R.id.exam:
                        activity.examPracticeLearn(MainAccess.Options.EXAM);
                        break;
                    case R.id.practice:
                        activity.examPracticeLearn(MainAccess.Options.PRACTICE);
                        break;
                    case R.id.learn:
                        activity.examPracticeLearn(MainAccess.Options.LEARN);
                        break;
                }
            }
        };
        
        binding.exam.setOnClickListener(optionListener);
        binding.practice.setOnClickListener(optionListener);
        binding.learn.setOnClickListener(optionListener);

        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: detached");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: DESTROYED");
    }
}
