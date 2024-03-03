package com.qualcomm.qti.qa.ui.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.qualcomm.qti.qa.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.qualcomm.qti.R;

public class ModelSelectionActivity extends AppCompatActivity{
    private Spinner spinner;
    private TextView modelInfoTextView;

    private int currModelIndex;

    private List<Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_selection);

        spinner = findViewById(R.id.spinner);
        modelInfoTextView = findViewById(R.id.model_info);

        Toolbar toolBar = findViewById(R.id.xml_toolbar);
        TextView textView = toolBar.findViewById(R.id.textToolBar);
        setSupportActionBar(toolBar);


        // Initialize your models
        initializeModels();

        // Populate spinner with model names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getModelNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currModelIndex = position;
                updateModelInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    private void initializeModels() {
        // Initialize your models with names and information
        models = new ArrayList<>();
        models.add(new Model("Roberta", "Information about Model 1"));
        models.add(new Model("Electra", "Information about Model 2"));
        // Add more models as needed
    }

    private List<String> getModelNames() {
        List<String> modelNames = new ArrayList<>();
        for (Model model : models) {
            modelNames.add(model.getName());
        }
        return modelNames;
    }

    private void updateModelInfo() {
        Model selectedModel = models.get(currModelIndex);
        modelInfoTextView.setText(selectedModel.getInfo());
    }

    public void goToChatActivity(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

}
