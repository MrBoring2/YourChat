package com.example.yourchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourchat.MainActivity;
import com.example.yourchat.R;

public class AboutProgramFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_program, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("О программе");
        return root;
    }
}
