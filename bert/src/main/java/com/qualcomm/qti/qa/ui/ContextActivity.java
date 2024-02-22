package com.qualcomm.qti.qa.ui;

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

import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        button = (Button)findViewById(R.id.questionButton);
        contextText = (EditText)findViewById(R.id.contextText);
        questionText = (EditText)findViewById(R.id.questionText);
        answerText = (TextView)findViewById(R.id.answerView);
        spinnerDLC = (Spinner)findViewById(R.id.spinnerDLC);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DLCFiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDLC.setAdapter(adapter);
        spinnerDLC.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(contextText.getText().toString().trim().length() > 0 && questionText.getText().toString().trim().length() > 0) {
                    questionAnswered = false;
                    answerQuestion(questionText.getText().toString().trim(), contextText.getText().toString().trim());
                }else {
                    Toast.makeText(ContextActivity.this, "Put a question!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup QA client to and background thread to run inference.
        HandlerThread handlerThread = new HandlerThread("QAClient");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        qaClient = new QaClient(this);
        

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if(modelPos == pos) {
            return;
        }

        modelPos = pos;
        modelUsed = DLCPaths[pos];
        qaClient = new QaClient(this);

//        qaClient.unload();
        handler.post(
                ()-> {
                    String init_files = qaClient.loadModel(modelUsed);
                    qaClient.loadDictionary();

                }
        );

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }

    @Override
    protected void onStart() {
        super.onStart();

        handler.post(
                ()-> {
                    String init_files = qaClient.loadModel(modelUsed);
                    qaClient.loadDictionary();

                }
        );

    }

    @Override
    protected void onStop() {
//        Log.v(TAG, "onStop");
        super.onStop();
        handler.post(() -> qaClient.unload());
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
                    String runtime= "CPU";

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


