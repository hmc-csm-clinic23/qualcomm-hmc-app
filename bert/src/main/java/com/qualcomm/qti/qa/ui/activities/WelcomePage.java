package com.qualcomm.qti.qa.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qualcomm.qti.R;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }
    public void goToScrollingActivity(View view) {
        Intent intent = new Intent(this, ContextActivity.class);
        startActivity(intent);
        finish();
    }
}