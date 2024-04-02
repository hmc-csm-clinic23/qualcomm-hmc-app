package com.qualcomm.qti.qa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.qualcomm.qti.R;
import com.qualcomm.qti.qa.ml.QaAnswer;
import com.qualcomm.qti.qa.ml.QaClient;
import com.qualcomm.qti.qa.ui.activities.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class ContextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Button button;
    Spinner spinnerDLC;

    private TextView modelInfoTextView;

    String modelUsed = "distilbert_cached.dlc";
    int modelPos = 0;

    String[] DLCPaths = {"distilbert_cached.dlc", "electra_small_squad2_cached.dlc"};

    private List<Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        button = (Button)findViewById(R.id.nextStepButton);
        spinnerDLC = (Spinner)findViewById(R.id.spinnerDLC);
        initializeModels();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getModelNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDLC.setAdapter(adapter);
        spinnerDLC.setOnItemSelectedListener(this);
        modelInfoTextView = (TextView)findViewById(R.id.model_info);
        modelInfoTextView.setText("Select a model from above.");

        button.setOnClickListener(goToChatListener);
    }

    private List<String> getModelNames() {
        List<String> modelNames = new ArrayList<>();
        for (Model model : models) {
            modelNames.add(model.getName());
        }
        return modelNames;
    }

    private View.OnClickListener goToChatListener = new View.OnClickListener() {
        public void onClick(View v) {
            goToChatActivity(modelUsed);
        }
    };

    private void initializeModels() {
        // Initialize your models with names and information
        models = new ArrayList<Model>();
        // TODO: change the information tag for distilbert
        models.add(new Model("Distilbert", "RoBERTa is a transformers model pretrained on a large corpus of English data in a self-supervised fashion. This means it was pretrained on the raw texts only, with no humans labelling them in any way (which is why it can use lots of publicly available data) with an automatic process to generate inputs and labels from those texts.\n" +
                "\n" +
                "More precisely, it was pretrained with the Masked language modeling (MLM) objective. Taking a sentence, the model randomly masks 15% of the words in the input then run the entire masked sentence through the model and has to predict the masked words. This is different from traditional recurrent neural networks (RNNs) that usually see the words one after the other, or from autoregressive models like GPT which internally mask the future tokens. It allows the model to learn a bidirectional representation of the sentence.\n" +
                "\n" +
                "This way, the model learns an inner representation of the English language that can then be used to extract features useful for downstream tasks: if you have a dataset of labeled sentences for instance, you can train a standard classifier using the features produced by the BERT model as inputs."));
        models.add(new Model("Electra", "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
        // Add more models as needed
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if(modelPos == pos) {
            return;
        }
        modelPos = pos;
        modelUsed = DLCPaths[pos];
        modelInfoTextView.setText(models.get(pos).getInfo());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        modelInfoTextView.setText("Please select a model.");
    }

    public void goToChatActivity(String modelUsed) {
        Intent intent = new Intent(this, ChatActivity.class);
        Toast.makeText(ContextActivity.this, modelUsed, Toast.LENGTH_SHORT).show();
        intent.putExtra("modelUsed", modelUsed);
        startActivity(intent);
        finish();
    }
}


