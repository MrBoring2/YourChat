package com.example.yourchat.Helpers;

import android.app.Application;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.telephony.mbms.MbmsErrors;

import androidx.annotation.NonNull;

import com.example.yourchat.MainActivity;
import com.example.yourchat.R;
import com.example.yourchat.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.logging.Logger;

public class FirebaseHelper {
    public static Logger log = Logger.getLogger(MainActivity.class.getName());
    public final static String USER_DERRICTORY = "Users";
    public final static String ROOMS_DERRICTORY = "Rooms";
    public final static String MESSAGE_DERRICTORY = "Messages";
    public final static String PROFILE_DERRICTORY = "Profile";
    public final static String EMAIL_KEY = "email";
    public final static String LOGIN_KEY = "login";
    public final static String FULLNAME_KEY = "fullName";
    public final static String PASSWORD_KEY = "password";
    public final static String IMAGESOURCE_KEY = "image_source";
    public final static String ROOMNAME_KEY = "room_name";

    public final static String ROOT_IMAGE_FOLDER = "Images";
    public final static String FOLDER_PRIFILE_AVATAR = "UserAvatars";
    public final static String FOLDER_ROOMS_IMAGES = "RoomsImages";

    public static FirebaseAuth AUTH;
    public static FirebaseFirestore FIRESTORE;
    public static StorageReference FSTORAGE;
    public static FirebaseUser FIREBASEUSER= null;
    public static User CURRENTUSER;
    public static String CurrentChatId = null;
    public static String LastToolBarTitle = null;
}
