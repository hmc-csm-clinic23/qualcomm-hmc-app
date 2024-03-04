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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.qualcomm.qti.qa.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.qualcomm.qti.R;
import com.qualcomm.qti.qa.ui.QaActivity;

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
                Toast.makeText(getApplicationContext(), "Please select a model", Toast.LENGTH_SHORT).show();
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
        models.add(new Model("Please select a model", ""));
        models.add(new Model("RoBERTA", "RoBERTa is a transformers model pretrained on a large corpus of English data in a self-supervised fashion. This means it was pretrained on the raw texts only, with no humans labelling them in any way (which is why it can use lots of publicly available data) with an automatic process to generate inputs and labels from those texts.\n" +
                "\n" +
                "More precisely, it was pretrained with the Masked language modeling (MLM) objective. Taking a sentence, the model randomly masks 15% of the words in the input then run the entire masked sentence through the model and has to predict the masked words. This is different from traditional recurrent neural networks (RNNs) that usually see the words one after the other, or from autoregressive models like GPT which internally mask the future tokens. It allows the model to learn a bidirectional representation of the sentence.\n" +
                "\n" +
                "This way, the model learns an inner representation of the English language that can then be used to extract features useful for downstream tasks: if you have a dataset of labeled sentences for instance, you can train a standard classifier using the features produced by the BERT model as inputs."));
        models.add(new Model("Electra", "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
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
