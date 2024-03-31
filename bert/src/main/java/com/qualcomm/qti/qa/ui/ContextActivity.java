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
    EditText contextText;
    EditText questionText;

    Spinner spinnerDLC;

    TextView answerText;

    private boolean questionAnswered = false;
    private Handler handler;
    private QaClient qaClient;

    String modelUsed = "electra_small_squad2_cached.dlc";
    int modelPos = 0;
    String[] DLCFiles = {  "Electra Small Squad2", "Distilbert", };

    String[] DLCPaths = {"electra_small_squad2_cached.dlc", "distilbert_cached.dlc"};

    private List<Model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        button = (Button)findViewById(R.id.nextStepButton);
        contextText = (EditText)findViewById(R.id.contextText);
        spinnerDLC = (Spinner)findViewById(R.id.spinnerDLC);
        initializeModels();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getModelNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDLC.setAdapter(adapter);
        spinnerDLC.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(contextText.getText().toString().trim().length() > 0 && questionText.getText().toString().trim().length() > 0) {
                    questionAnswered = false;
                    String context = contextText.getText().toString().trim();
                    goToChatActivity(context, modelUsed);
                } else {
                    Toast.makeText(ContextActivity.this, "Put a question!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> getModelNames() {
        List<String> modelNames = new ArrayList<>();
        for (Model model : models) {
            modelNames.add(model.getName());
        }
        return modelNames;
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if(modelPos == pos) {
            return;
        }

        modelPos = pos;
        modelUsed = DLCPaths[pos];
//        qaClient = new QaClient(this);

//        qaClient.unload();
//        handler.post(
//                ()-> {
//                    String init_files = qaClient.loadModel(modelUsed);
//                    qaClient.loadDictionary();
//
//                }
//        );

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        handler.post(
//                ()-> {
//                    String init_files = qaClient.loadModel(modelUsed);
//                    qaClient.loadDictionary();
//
//                }
//        );
//
//    }
//
//    @Override
//    protected void onStop() {
////        Log.v(TAG, "onStop");
//        super.onStop();
//        handler.post(() -> qaClient.unload());
//    }

    public void goToChatActivity(String context, String modelUsed) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("context", context);
        intent.putExtra("modelUsed", modelUsed);
        startActivity(intent);
        finish();
    }

    private void answerQuestion(String question, String context) {

        question = question.trim();
//        if (question.isEmpty()) {
//            questionEditText.setText(question);
//            return;
//        }

        // Append question mark '?' if not ended with '?'.
        // This aligns with question format that trains the model.
        if (!question.endsWith("?")) {
            question += '?';
        }
        final String questionToAsk = question;
        answerText.setText("Finding the answer with " + DLCFiles[modelPos]);

        handler.removeCallbacksAndMessages(null);


        questionAnswered = false;

        handler.post(
                () -> {
                    String runtime= "DSP";

                    StringBuilder execStatus = new StringBuilder ();

                    long beforeTime = System.currentTimeMillis();
                    final List<QaAnswer> answers = qaClient.predict(questionToAsk, context, runtime, execStatus);


                    long afterTime = System.currentTimeMillis();
                    double totalSeconds = (afterTime - beforeTime) / 1000.0;
                    String displayMessage = runtime + " inference took : ";
                    displayMessage = String.format("%s %.3f sec. with %s", displayMessage, totalSeconds, DLCFiles[modelPos]);
                    Toast.makeText(ContextActivity.this, displayMessage, Toast.LENGTH_SHORT).show();

                    if (!answers.isEmpty()) {

                        QaAnswer topAnswer = answers.get(0);
                        answerText.setText(topAnswer.text);

                    }else {
                        Toast.makeText(ContextActivity.this, "Failed to get an answer", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}


