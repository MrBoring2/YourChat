package com.example.yourchat;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.Helpers.FirebaseHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ViewRoomHolder> {

    Task<Uri> downloadUrl;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewRoomHolder holder, int position, @NonNull Message model) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY HH:mm", myDateFormatSymbols );
        final StorageReference path = FirebaseHelper.FSTORAGE.child(FirebaseHelper.ROOT_IMAGE_FOLDER).child(FirebaseHelper.FOLDER_PRIFILE_AVATAR).child(model.getAuthor_id());

        if(model.getAuthor_id().equals(FirebaseHelper.FIREBASEUSER.getUid())){
            holder.yourContainer.setVisibility(View.VISIBLE);
            holder.otherContainer.setVisibility(View.GONE);
            holder.yourSendText.setText(model.getMessage_text());
            if(model.getDate()!=null)
                holder.yourSendDate.setText(dateFormat.format(model.getDate()));
            downloadUrl = path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Picasso.get()
                                .load(task.getResult().toString())
                                .placeholder(R.drawable.standart_avatar)
                                .into(holder.yourImage);
                    }
                }
            });

        }
        else{

            holder.yourContainer.setVisibility(View.GONE);
            holder.otherContainer.setVisibility(View.VISIBLE);
            holder.otherName.setText(model.getAuthor());
            holder.otherSendText.setText(model.getMessage_text());
            holder.otherSendDate.setText(dateFormat.format(model.getDate()));
            downloadUrl = path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Picasso.get()
                                .load(task.getResult().toString())
                                .placeholder(R.drawable.standart_avatar)
                                .into(holder.otherImage);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new ViewRoomHolder(view);
    }

    private DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };

    class ViewRoomHolder extends  RecyclerView.ViewHolder{
        private View view;

        ConstraintLayout yourContainer;
        ConstraintLayout otherContainer;
        TextView yourName;
        TextView otherName;
        TextView yourSendDate;
        TextView otherSendDate;
        TextView yourSendText;
        TextView otherSendText;
        ImageView yourImage;
        ImageView otherImage;
        public ViewRoomHolder(View itemView) {
            super(itemView);
            view = itemView;
            yourSendDate = itemView.findViewById(R.id.your_message_date);
            otherSendDate = itemView.findViewById(R.id.other_message_date);
            yourName = itemView.findViewById(R.id.your_fullname);
            otherName = itemView.findViewById(R.id.other_fullname);
            yourSendText = itemView.findViewById(R.id.your_message_text);
            otherSendText = itemView.findViewById(R.id.other_chat_message);
            yourImage = itemView.findViewById(R.id.your_chat_photo);
            otherImage = itemView.findViewById(R.id.other_chat_photo);
            yourContainer = itemView.findViewById(R.id.your_container);
            otherContainer = itemView.findViewById(R.id.other_container);
        }
    }
}
