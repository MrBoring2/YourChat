package com.example.yourchat.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.example.yourchat.MainActivity;
import com.example.yourchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class CreateRoomFragment  extends Fragment {

    ImageView roomImage;
    TextView roomName;
    Button uploadImage;
    Button createRoom;
    Uri roomUri;
    private UUID roomsUUID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_room, container, false);
        initializeFields(view);
        setViewListeners();
        return view;
    }

    private void initializeFields(View view){
        roomsUUID = UUID.randomUUID();
        roomImage  = view.findViewById(R.id.room_image);
        roomName = view.findViewById(R.id.room_name_text);
        uploadImage = view.findViewById(R.id.upload_image_room);
        createRoom = view.findViewById(R.id.create_room);
        Resources resources = getContext().getResources();
        roomUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.rooms_image) + '/' + resources.getResourceTypeName(R.drawable.rooms_image) + '/' + resources.getResourceEntryName(R.drawable.rooms_image) );
    }
    private void setViewListeners() {
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoto();
            }
        });
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });
    }


    private void createRoom(){
        if(!roomName.getText().toString().isEmpty()) {
            final StorageReference path = FirebaseHelper.FSTORAGE.child(FirebaseHelper.ROOT_IMAGE_FOLDER).child(FirebaseHelper.FOLDER_ROOMS_IMAGES).child(roomsUUID.toString());
            path.putFile(roomUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    roomUri = task.getResult();
                                    Log.d(TAG, roomUri.toString());
                                    DocumentReference documentReference = FirebaseHelper.FIRESTORE.collection("Rooms").document(roomsUUID.toString());
                                    Map<String, Object> data = new HashMap<>();
                                    data.put(FirebaseHelper.ROOMNAME_KEY, roomName.getText().toString());
                                    data.put(FirebaseHelper.IMAGESOURCE_KEY, roomUri.toString());
                                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ((MainActivity) getActivity()).loadProfileInformation();
                                            FunctionsHelper.showToast(getContext(), "Команта чата создана!");
                                            getParentFragmentManager().popBackStack();
                                            FunctionsHelper.setToolbarTitle((MainActivity) getActivity(), "Чаты");
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            });
        }
        else{
            FunctionsHelper.showToast(getContext(), "Не указано название комнаты!");
        }
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
                roomUri = result.getUri();
                roomImage.setImageURI(roomUri);
            }
        }
    }
}
