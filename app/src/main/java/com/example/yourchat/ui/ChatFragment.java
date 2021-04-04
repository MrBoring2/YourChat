package com.example.yourchat.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.ChatScroolListener;
import com.example.yourchat.CustomLayoutManager;
import com.example.yourchat.Helpers.FirebaseHelper;
import com.example.yourchat.Helpers.FunctionsHelper;
import com.example.yourchat.MainActivity;
import com.example.yourchat.Message;
import com.example.yourchat.MessageAdapter;
import com.example.yourchat.R;
import com.example.yourchat.Room;
import com.example.yourchat.RoomsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ChatFragment extends Fragment {
    RecyclerView roomsView;
    private MessageAdapter listAdapter;
    private String roomId;
    private View curView;
    private EditText messageText;
    private Button sendMessage;
    private Button scrool;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        curView = inflater.inflate(R.layout.fragment_chat, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("room_name"));
        initView(curView);
        Log.d(TAG, Integer.toString (roomsView.getAdapter().getItemCount()));
        return curView;
    }

    private void initView(View view){
        scrool = view.findViewById(R.id.scrool);
        scrool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listAdapter.getItemCount()>0)
                    roomsView.smoothScrollToPosition(listAdapter.getItemCount() - 1);
            }
        });
        CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
        messageText = view.findViewById(R.id.message_text);
        sendMessage = view.findViewById(R.id.send_message_button);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        roomId = getArguments().getString("room_id");
        Query query = FirebaseHelper.FIRESTORE.collection(FirebaseHelper.ROOMS_DERRICTORY).document(roomId).collection(FirebaseHelper.MESSAGE_DERRICTORY).orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        listAdapter = new MessageAdapter(options);

        roomsView = view.findViewById(R.id.chatView);
        roomsView.setHasFixedSize(true);

        roomsView.setLayoutManager(layoutManager);
        roomsView.setAdapter(listAdapter);
    }



    private void sendMessage() {
        if(!messageText.getText().toString().isEmpty()) {
            DocumentReference documentReference = FirebaseHelper.FIRESTORE.collection("Rooms").document(roomId).collection("Messages").document();
            Map<String, Object> data = new HashMap<>();
            data.put("message_text", messageText.getText().toString());
            data.put("author", FirebaseHelper.CURRENTUSER.getFullName());
            data.put("author_id", FirebaseHelper.FIREBASEUSER.getUid());
            data.put("date", FieldValue.serverTimestamp());
            data.put("avatar", FirebaseHelper.CURRENTUSER.getDowndloadUrl());
            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    messageText.setText("");
                    roomsView.smoothScrollToPosition(listAdapter.getItemCount());
                    Log.d(TAG, Integer.toString(listAdapter.getItemCount()));
                }
            });

        }
        else{
            FunctionsHelper.showToast(getContext(), "Сообщение не должно быть пустым!");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!roomId.equals(FirebaseHelper.CurrentChatId))
            initView(curView);
        listAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if(listAdapter!=null)
            listAdapter.stopListening();
    }
}
