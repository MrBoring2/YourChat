package com.example.yourchat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.example.yourchat.ui.AboutProgramFragment;
import com.example.yourchat.ui.AccRefactorFragment;
import com.example.yourchat.ui.CreateRoomFragment;
import com.example.yourchat.ui.PersonalAccountFragment;
import com.example.yourchat.ui.RoomsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private FragmentTransaction ft;
    private FragmentManager fm;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    Toolbar toolbar;
    ImageView avatar;
    TextView fullName;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        initializeFields();
        if(!checkUser())
            launchLogin();
        else {
            setContentView(R.layout.activity_main);
            loadNavigationView();
            showRooms();
            loadProfileInformation();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    private void initializeFields(){

        FirebaseHelper.FSTORAGE = FirebaseStorage.getInstance().getReference();
        FirebaseHelper.AUTH = FirebaseAuth.getInstance();
        FirebaseHelper.FIRESTORE = FirebaseFirestore.getInstance();
        FirebaseHelper.FIREBASEUSER = FirebaseHelper.AUTH.getCurrentUser();
        FirebaseHelper.CURRENTUSER = new User();
        fm = getSupportFragmentManager();
    }

    private void initializeHeader(){
        try {
            avatar = findViewById(R.id.header_avatar);
            fullName = findViewById(R.id.heeader_full_name);
            email = findViewById(R.id.header_email);
            fullName.setText(FirebaseHelper.CURRENTUSER.getFullName());
            email.setText(FirebaseHelper.CURRENTUSER.getEmail());
            Picasso.get()
                    .load(FirebaseHelper.CURRENTUSER.getDowndloadUrl())
                    .placeholder(R.drawable.standart_avatar)
                    .into(avatar);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Ошибка");
        }
    }

    private void loadNavigationView(){
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Чаты");
        setSupportActionBar(toolbar);
        fm.addOnBackStackChangedListener(this);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseHelper.LastToolBarTitle = toolbar.getTitle().toString();

    }

    private void showRooms(){
        setCurrentFragment(fm, (new RoomsFragment()));
    }


    private boolean checkUser() {
        if(FirebaseHelper.FIREBASEUSER == null)
            return false;
        else return true;
    }

    private void launchLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    private void signOut(){
        FirebaseHelper.AUTH.signOut();
        launchLogin();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void openRoom(Activity activity){
        FunctionsHelper.openActivity(getApplicationContext(), activity.getClass());
    }


    //скрытие клавиатуры
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_add_room) {
            fragmentClass = CreateRoomFragment.class;
        } else if (id == R.id.nav_personal_account) {
            fragmentClass = PersonalAccountFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutProgramFragment.class;
        } else if (id == R.id.nav_exit) {
            signOut();
            return true;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        fm.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        item.setChecked(false);
        FunctionsHelper.setToolbarTitle(this, item.getTitle().toString());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        FirebaseHelper.LastToolBarTitle = toolbar.getTitle().toString();
        return true;
    }

    public void setCurrentFragment(FragmentManager manager, Fragment fragment){
        manager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void loadProfileInformation() {
        FirebaseHelper.FIRESTORE.collection(FirebaseHelper.USER_DERRICTORY).document(FirebaseHelper.FIREBASEUSER.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> list = document.getData();
                        FirebaseHelper.CURRENTUSER.setEmail((String) list.get(FirebaseHelper.EMAIL_KEY));
                        FirebaseHelper.CURRENTUSER.setFullName((String) list.get(FirebaseHelper.FULLNAME_KEY));
                        try{
                            FirebaseHelper.CURRENTUSER.setDowndloadUrl(((String) list.get(FirebaseHelper.IMAGESOURCE_KEY)));
                        }
                        catch (Exception ex){
                            FirebaseHelper.CURRENTUSER.setDowndloadUrl(null);
                        }
                        FirebaseHelper.CURRENTUSER.setLogin((String) list.get(FirebaseHelper.LOGIN_KEY));
                        initializeHeader();
                    }
                }

            }
        });


    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    toolbar.setTitle(FirebaseHelper.LastToolBarTitle);
                    onBackPressed();
                }
            });
        } else {
            toolbar.setTitle("Чаты");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);//show hamburger
            toggle.syncState();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        navigationView.getCheckedItem().setChecked(false);
                    }
                    catch (Exception ignored)
                    {

                    }
                    drawer.openDrawer(GravityCompat.START);

                }
            });
        }
    }


}