package com.qualcomm.qti.qa.ui.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qualcomm.qti.qa.ui.Model;
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.qti.R;

public class ModelSelectionActivity extends AppCompatActivity{
    private Spinner spinner;
    private TextView modelInfoTextView;

    private List<Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_selection);

        spinner = findViewById(R.id.spinner);
        modelInfoTextView = findViewById(R.id.model_info);

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
                updateModelInfo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void initializeModels() {
        // Initialize your models with names and information
        models = new ArrayList<>();
        models.add(new Model("Roberta", "Information about Model 1"));
        models.add(new Model("", "Information about Model 2"));
        // Add more models as needed
    }

    private List<String> getModelNames() {
        List<String> modelNames = new ArrayList<>();
        for (Model model : models) {
            modelNames.add(model.getName());
        }
        return modelNames;
    }

    private void updateModelInfo(int position) {
        Model selectedModel = models.get(position);
        modelInfoTextView.setText(selectedModel.getInfo());
    }

    private void goToNextActivity(){

        // go to next activity which should be chat bot.
    }

}
