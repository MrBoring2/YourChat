package com.example.yourchat.ui;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.example.yourchat.MainActivity;
import com.example.yourchat.R;
import com.example.yourchat.Room;
import com.example.yourchat.RoomsAdapter;
import com.example.yourchat.ui.RoomActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
public class RoomsFragment extends Fragment{

    RecyclerView roomsView;
    private RoomsAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        initView(root);
        textAdd();

        FirebaseFunctions.getInstance().getHttpsCallable("getTime")
                .call().addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
            @Override
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                long timestamp = (long) httpsCallableResult.getData();
                Log.d(TAG, Long.toString(timestamp));

            }
        });
        return root;
    }

    private void initView(View view){
        Query query = FirebaseHelper.FIRESTORE.collection(FirebaseHelper.ROOMS_DERRICTORY).orderBy("room_name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Room> options = new FirestoreRecyclerOptions.Builder<Room>()
                .setQuery(query, Room.class)
                .build();

        listAdapter = new RoomsAdapter(options);

        roomsView = view.findViewById(R.id.roomsView);
        roomsView.setHasFixedSize(true);
        roomsView.setLayoutManager(new LinearLayoutManager(getContext()));
        roomsView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new RoomsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Fragment fragment = new ChatFragment();
                Bundle args = new Bundle();
                args.putString("room_id",documentSnapshot.getId());
                args.putString("room_name", documentSnapshot.toObject(Room.class).getRoom_name());
                fragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(listAdapter!=null)
            listAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    void textAdd(){
        DocumentReference documentReference = FirebaseHelper.FIRESTORE.collection("Test").document();
        Map<String, Object> data = new HashMap<>();
        data.put("date", FieldValue.serverTimestamp());

        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

}