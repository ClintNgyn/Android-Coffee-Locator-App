package com.example.androidfinalprojectcoffeeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeScreenBottomNav extends Fragment {

    private communicator communicator;
    private ImageButton mapButton;
    private ImageButton profileButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_screen_bottom_nav, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (communicator) getActivity();
        mapButton = getActivity().findViewById(R.id.mapButton);
        mapButton.setOnClickListener(v -> communicator.goToMaps());

        //Button favoriteListButton = view.findViewById(R.id.favoriteListButton);

        //Button settingsButton = view.findViewById(R.id.settingsButton);

        profileButton = getActivity().findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> communicator.goToProfile());
    }
}