package com.example.yourchat.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.example.yourchat.MainActivity;
import com.example.yourchat.R;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AccRefactorFragment extends Fragment {

    String login="";
    String imageSource="";
    String name="";
    String familyName="";
    String imageDownloadUrl = "";
    Uri imageUri;

    ImageView avatar;
    Button loadImageButton;
    Button saveButton;
    TextView loginText;
    TextView nameText;
    TextView familyNameText;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_acc_refactor, container, false);
        initializeFields(root);
        setListeners();
        loadData();

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Редактирование");
        return root;
    }

    private void initializeFields(View view){
        avatar = view.findViewById(R.id.ref_avatar);
        saveButton = view.findViewById(R.id.save_changes);
        loadImageButton = view.findViewById(R.id.load_iamge);
        loginText = view.findViewById(R.id.ref_login);
        nameText = view.findViewById(R.id.ref_name);
        familyNameText = view.findViewById(R.id.ref_family_name);
    }

    private void setListeners(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoto();
            }
        });
    }


    private void loadData(){
        login = getArguments().getString("login");
        name = getArguments().getString("name");
        familyName = getArguments().getString("familyName");
        imageDownloadUrl = getArguments().getString("imageDownloadUrl");

        loginText.setText(login);
        nameText.setText(name);
        familyNameText.setText(familyName);
        avatar.setImageURI(Uri.parse(imageSource));

        if(FirebaseHelper.CURRENTUSER.getDowndloadUrl().isEmpty()) {
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.standart_avatar) + '/' + getResources().getResourceTypeName(R.drawable.standart_avatar) + '/' + getResources().getResourceEntryName(R.drawable.standart_avatar));
            avatar.setImageURI(imageUri);
        }
        else{
            imageUri = Uri.parse(imageDownloadUrl);
            Picasso.get()
                    .load(FirebaseHelper.CURRENTUSER.getDowndloadUrl())
                    .placeholder(R.drawable.standart_avatar)
                    .into(avatar);
        }


    }


    private void saveChanges(){
        final StorageReference path = FirebaseHelper.FSTORAGE.child(FirebaseHelper.ROOT_IMAGE_FOLDER).child(FirebaseHelper.FOLDER_PRIFILE_AVATAR).child(FirebaseHelper.FIREBASEUSER.getUid());
        if(!imageDownloadUrl.equals(imageUri.toString()))
        {
            path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    imageUri = task.getResult();
                                }
                            }
                        });
                    }
                }
            });
        }
        DocumentReference documentReference = FirebaseHelper.FIRESTORE.collection("Users").document(FirebaseHelper.FIREBASEUSER.getUid());
        Map<String, Object> data = new HashMap<>();
        data.put(FirebaseHelper.LOGIN_KEY, loginText.getText().toString());
        data.put(FirebaseHelper.IMAGESOURCE_KEY, imageUri.toString());
        data.put(FirebaseHelper.FULLNAME_KEY, name + " " + familyNameText.getText().toString());
        documentReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ((MainActivity) getActivity()).loadProfileInformation();
                FunctionsHelper.showToast(getContext(), "Данные обновлены");
            }
        });
    }

    private void loadPhoto(){

        CropImage.activity()
                .setAspectRatio(1,1)
                .setRequestedSize(600,600)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK) {
                imageUri = result.getUri();
                avatar.setImageURI(imageUri);
            }

        }
    }


}
