package com.example.yourchat.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yourchat.R;

public class FunctionsHelper {
    public static void setToolbarTitle(AppCompatActivity activity, String title){
        activity.getSupportActionBar().setTitle(title);
    }



    public static void openActivity(Context appContext, Class<?> activityClass){
        Intent intent = new Intent(appContext, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appContext.startActivity(intent);
    }

    public static void showToast(Context appContext, String toastMessage){
        Toast.makeText(appContext, toastMessage, Toast.LENGTH_LONG).show();
    }
}
