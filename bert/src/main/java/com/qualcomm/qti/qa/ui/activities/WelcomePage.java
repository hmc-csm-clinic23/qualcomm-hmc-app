package com.qualcomm.qti.qa.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qualcomm.qti.R;
import com.qualcomm.qti.qa.ui.ContextActivity;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }
    public void goToScrollingActivity(View view) {
        // Uncomment when finished
        Intent intent = new Intent(this, ContextActivity.class);
        startActivity(intent);
        finish();
    }
}