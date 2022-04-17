package com.aacreations.countryandcapitals;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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

        MainActivity activity = (MainActivity) requireActivity();

        // Buttons
        View.OnClickListener optionListener = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(@NonNull View v) {
                switch (v.getId()) {
                    case R.id.exam:
                        examPracticeLearn(activity, MainAccess.Options.EXAM);
                        break;
                    case R.id.practice:
                        examPracticeLearn(activity, MainAccess.Options.PRACTICE);
                        break;
                    case R.id.learn:
                        examPracticeLearn(activity, MainAccess.Options.LEARN);
                        break;
                }
            }
        };
        
        binding.exam.setOnClickListener(optionListener);
        binding.practice.setOnClickListener(optionListener);
        binding.learn.setOnClickListener(optionListener);

        return view;

    }

    private void examPracticeLearn(@NonNull MainActivity activity, String option) {
        activity.options = option;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            toolbar.setTitle(option);
        }
        activity.optionFragment.setVisibility(View.GONE);
        activity.mainActivityFragment.setVisibility(View.VISIBLE);
        activity.selectFragment.setVisibility(View.GONE);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.whatIsOpen = "list";
        activity.initNavigationDrawer(false);
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
