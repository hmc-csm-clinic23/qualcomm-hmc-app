package com.qualcomm.qti.qa.chat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qualcomm.qti.R;

import java.util.ArrayList;

public class MessageRVAdapter extends RecyclerView.Adapter {

    // variable for our array list and context.
    private ArrayList<MessageModel> messageModelArrayList;
    private Context context;

    private String message_color;

    // constructor class.
    public MessageRVAdapter(ArrayList<MessageModel> messageModelArrayList, Context context, String color) {
        this.messageModelArrayList = messageModelArrayList;
        this.context = context;
        this.message_color = color;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // below code is to switch our
        // layout type along with view holder.
        switch (viewType) {
            case 0:
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
                return new UserViewHolder(view);
            case 1:
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
//                view.setBackgroundColor(Color.parseColor("#FFFF00"));\
                CardView cardView = view.findViewById(R.id.idCard);
                cardView.setBackgroundColor(Color.parseColor(this.message_color));
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // this method is use to set data to our layout file.
        MessageModel modal = messageModelArrayList.get(position);
        switch (modal.getSender()) {
            case "user":
                // below line is to set the text to our text view of user layout
                ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                break;
            case "bot":// below line is to set the text to our text view of bot layout
                ((BotViewHolder) holder).botTV.setText(modal.getMessage());
//                ((BotViewHolder) holder).botTV.setBackgroundColor(Color.parseColor("#FFFF00"));
//                ((BotViewHolder) holder).botTV.setTextColor(Color.parseColor("#FF69B4"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        // return the size of array list
        return messageModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // below line of code is to set position.
        switch (messageModelArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView userTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView botTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            botTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
