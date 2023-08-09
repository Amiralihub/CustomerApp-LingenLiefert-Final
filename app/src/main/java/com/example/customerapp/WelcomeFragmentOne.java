package com.example.customerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.Objects;


public class WelcomeFragmentOne extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View welcomeView = inflater.inflate(R.layout.fragment_welcome_one, container, false);

        Button nextButton = welcomeView.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            Fragment fragment = new WelcomeFragmentTwo();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.welcome_frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return welcomeView;
    }
}
