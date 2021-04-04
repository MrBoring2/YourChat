package com.example.yourchat.ui;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class PersonalAccountFragment extends Fragment {

    private FragmentTransaction ft;
    private FragmentManager fm;

    private Button accRefactorButton;
    private TextView nameText;
    private TextView emailText;
    private TextView familyNameText;
    private TextView loginText;
    private ImageView profileAvatar;

    private String fullNameT;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        initializeFields(view);
        setViewListeners();
        setProfileInformation();
        return view;
    }

    private void setProfileInformation(){
        String[] fullName = FirebaseHelper.CURRENTUSER.getFullName().split(" ");
        nameText.setText(fullName[0]);
        familyNameText.setText(fullName[1]);
        emailText.setText(FirebaseHelper.CURRENTUSER.getEmail());
        loginText.setText(FirebaseHelper.CURRENTUSER.getLogin());
        if(!FirebaseHelper.CURRENTUSER.getDowndloadUrl().isEmpty())
             Picasso.get()
                .load(FirebaseHelper.CURRENTUSER.getDowndloadUrl())
                .placeholder(R.drawable.standart_avatar)
                .into(profileAvatar);
        else{
            profileAvatar.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.standart_avatar) + '/' + getResources().getResourceTypeName(R.drawable.standart_avatar) + '/' + getResources().getResourceEntryName(R.drawable.standart_avatar)));
        }
    }


    private void initializeFields(View view){
        accRefactorButton  = view.findViewById(R.id.acc_refactor_burron);
        loginText = view.findViewById(R.id.acc_login);
        emailText = view.findViewById(R.id.acc_email);
        nameText = view.findViewById(R.id.acc_name);
        familyNameText = view.findViewById(R.id.acc_familyName);
        profileAvatar = view.findViewById(R.id.profile_avatar);

    }

    private void setViewListeners() {
        accRefactorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAccRefactor();
            }
        });
    }

    private void launchAccRefactor() {
        Fragment accRefactorFragment = new AccRefactorFragment();
        fm = getParentFragmentManager();
        Bundle args = new Bundle();
        args.putString("login", loginText.getText().toString());
        args.putString("name", nameText.getText().toString());
        args.putString("familyName", familyNameText.getText().toString());
        args.putString("imageDownloadUrl", (FirebaseHelper.CURRENTUSER.getDowndloadUrl()));
        accRefactorFragment.setArguments(args);

        fm.beginTransaction().replace(R.id.container, accRefactorFragment).addToBackStack(null).commit();
    }
}