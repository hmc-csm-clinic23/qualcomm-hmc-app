package com.qualcomm.qti.qa.ui.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.qualcomm.qti.R;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatContainer = findViewById(R.id.chat_container);
        messageEditText = findViewById(R.id.message_edit_text);
        Button sendButton = findViewById(R.id.send_button);
        Toolbar toolBar = findViewById(R.id.xml_toolbar);
        TextView textView = toolBar.findViewById(R.id.textToolBar);
        setSupportActionBar(toolBar);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            addMessage("You: " + messageText, true);
            // Simulate receiving a response message after a delay
            simulateResponse();
            messageEditText.setText("");
        }
    }

    private void simulateResponse() {
        // Simulate receiving a response message after a delay
        String responseMessage = "Bot: This is a response to your message.";
        chatContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                addMessage(responseMessage, false);
            }
        }, 1000); // Adjust delay time as needed
    }

    private void addMessage(String message, boolean isUserMessage) {
        TextView textView = new TextView(this);
        textView.setText(message);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int padding = getResources().getDimensionPixelSize(R.dimen.chat_bubble_padding);
        layoutParams.setMargins(padding, padding, padding, padding);

        int backgroundColor;
        int textColor;
        if (isUserMessage) {
            backgroundColor = ContextCompat.getColor(this, R.color.user_bubble_background);
            textColor = ContextCompat.getColor(this, R.color.user_bubble_text);
            layoutParams.gravity = Gravity.END;
        } else {
            backgroundColor = ContextCompat.getColor(this, R.color.bot_bubble_background);
            textColor = ContextCompat.getColor(this, R.color.bot_bubble_text);
            layoutParams.gravity = Gravity.START;
        }
        textView.setBackgroundResource(R.drawable.chat_bubble);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(R.drawable.chat_bubble);
        textView.setLayoutParams(layoutParams);
        chatContainer.addView(textView);
    }

}

