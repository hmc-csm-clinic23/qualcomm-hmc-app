package com.qualcomm.qti.qa.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qualcomm.qti.R;
import com.qualcomm.qti.qa.ml.QaClient;

public class ContextActivity extends AppCompatActivity {
    Button button;
    EditText contextText;
    EditText questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        button = (Button)findViewById(R.id.questionButton);
        contextText = (EditText)findViewById(R.id.contextText);
        questionText = (EditText)findViewById(R.id.questionText);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(contextText.getText().toString().trim().length() > 0 && questionText.getText().toString().trim().length() > 0) {
                    Toast.makeText(ContextActivity.this, questionText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ContextActivity.this, "Put a question!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
