package com.example.yourchat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourchat.ui.RoomsFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RoomsAdapter extends FirestoreRecyclerAdapter<Room, RoomsAdapter.ViewRoomHolder> {

    private OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RoomsAdapter(@NonNull FirestoreRecyclerOptions<Room> options) {
        super(options);
    }

    @Override

    protected void onBindViewHolder(@NonNull ViewRoomHolder holder, final int position, @NonNull final Room model) {
        holder.setRoomName(model.getRoom_name());
        holder.setRoomUri(model.getImage_source());

        Log.d(TAG, model.getImage_source()+"sadasda");
    }

    @NonNull
    @Override
    public ViewRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_item, parent, false);

        return new ViewRoomHolder(view);
    }

    class ViewRoomHolder extends  RecyclerView.ViewHolder{
            private View view;
            TextView mRoomName;;
            ImageView mRoomImage;
            public ViewRoomHolder(View itemView) {
                super(itemView);
                view = itemView;
                mRoomName = itemView.findViewById(R.id.irem_room_name);
                mRoomImage = itemView.findViewById(R.id.room_item_image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION && listener!=null)
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                });

            }
            void setRoomName(String name){
                TextView textView = view.findViewById(R.id.irem_room_name);
                textView.setText(name);
            }
            void setRoomUri(String uri){
                ImageView imageView = view.findViewById(R.id.room_item_image);
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.rooms_image)
                        .into(imageView);
            }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(RoomsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}


