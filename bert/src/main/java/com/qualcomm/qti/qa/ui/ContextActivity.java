package com.qualcomm.qti.qa.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.qualcomm.qti.R;
import com.qualcomm.qti.qa.ml.QaAnswer;
import com.qualcomm.qti.qa.ml.QaClient;

import java.util.List;
import java.util.Locale;

public class ContextActivity extends AppCompatActivity {
    Button button;
    EditText contextText;
    EditText questionText;

    private boolean questionAnswered = false;
    private Handler handler;
    private QaClient qaClient;

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
//                    Toast.makeText(ContextActivity.this, questionText.getText().toString().trim(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
//        Log.v(TAG, "onStart");
        super.onStart();
        handler.post(
                () -> {
                    String initLogs = qaClient.loadModel();
//                    if(!initLogs.isEmpty()) {
//                        Snackbar initSnackbar =
//                                Snackbar.make(contentTextView, initLogs, Snackbar.LENGTH_SHORT);
//                        initSnackbar.show();
//                    }
                    qaClient.loadDictionary();
                });

//        textToSpeech =
//                new TextToSpeech(
//                        this,
//                        status -> {
//                            if (status == TextToSpeech.SUCCESS) {
//                                textToSpeech.setLanguage(Locale.US);
//                            } else {
//                                textToSpeech = null;
//                            }
//                        });
        Toast.makeText(ContextActivity.this, "initialized", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
//        Log.v(TAG, "onStop");
        super.onStop();
        handler.post(() -> qaClient.unload());
//
//        if (textToSpeech != null) {
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//        }
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
//        questionEditText.setText(questionToAsk);

        // Delete all pending tasks.
        handler.removeCallbacksAndMessages(null);

        // Hide keyboard and dismiss focus on text edit.
//        InputMethodManager imm =
//                (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//        View focusView = getCurrentFocus();
//        if (focusView != null) {
//            focusView.clearFocus();
//        }

        // Reset content text view
//        contentTextView.setText(content);

        questionAnswered = false;
//
//        Snackbar runningSnackbar =
//                Snackbar.make(contentTextView, "Looking up answer...", Snackbar.LENGTH_INDEFINITE);
//        runningSnackbar.show();

        // Run TF Lite model to get the answer.
        handler.post(
                () -> {
                    String runtime= "CPU";

                    StringBuilder execStatus = new StringBuilder ();

                    long beforeTime = System.currentTimeMillis();
                    final List<QaAnswer> answers = qaClient.predict(questionToAsk, context, runtime, execStatus);

                    Toast.makeText(ContextActivity.this, "got here 2", Toast.LENGTH_SHORT).show();

                    long afterTime = System.currentTimeMillis();
                    double totalSeconds = (afterTime - beforeTime) / 1000.0;

                    if (!answers.isEmpty()) {

                        Toast.makeText(ContextActivity.this, "got here 3", Toast.LENGTH_SHORT).show();

                        // Get the top answer
                        QaAnswer topAnswer = answers.get(0);
                        // Show the answer.
                        runOnUiThread(
                                () -> {
//                                    runningSnackbar.dismiss();
//                                    presentAnswer(topAnswer.text);
                                    Toast.makeText(ContextActivity.this, topAnswer.text, Toast.LENGTH_SHORT).show();


//                                    String displayMessage = runtime + " runtime took : ";
//                                    if (DISPLAY_RUNNING_TIME) {
//                                        displayMessage = String.format("%s %.3f sec.", displayMessage, totalSeconds);
//                                    }
//                                    if (! execStatus.toString().isEmpty())
//                                        Snackbar.make(contentTextView, execStatus.toString(), Snackbar.LENGTH_LONG).show();
//                                    else
//                                        Snackbar.make(contentTextView, displayMessage, Snackbar.LENGTH_LONG).show();

                                    questionAnswered = true;
                                });
                    }
                });
    }
}


