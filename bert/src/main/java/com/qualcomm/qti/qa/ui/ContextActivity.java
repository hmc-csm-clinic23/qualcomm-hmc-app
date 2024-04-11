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

    String modelUsed = "electra_small_squad2_cached.dlc";
    int modelPos = 0;

    String[] DLCPaths = {"electra_small_squad2_cached.dlc", "electra_small_squad2_cached.dlc",  "distilbert_cached.dlc",  "electra_small_squad2_cached.dlc", "electra_small_squad2_cached.dlc"};

    String[] colors = {"#5FAD56", "#F2C14E", "#F78154", "#4D9078", "#E4959E", "#6D6A0", "#7EB2DD"};
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
        models.add(new Model("Henry the Hiking Helper", "Henry is here to help if you are ever lost in the woods while hiking. \n\n " +
                "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
        models.add(new Model("Ernie, the Earthquake Assistant", "Ernie is here to help if you ever find yourself trapped in an earthquake.\n\n" +
                "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
        models.add(new Model("Cornelius the Cook", "Cornelius is here to help you be safe in the kitchen.\n" +
                "DistilBERT is a smaller, faster, and lighter version of the original BERT.\n" +
                "\n"));
        models.add(new Model("Fiona the First Aid Expert\n", "Fiona, your first aid expert, lets you know what should be in first aid kit and how to use it \n\n" +
                "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
        models.add(new Model("Serenity Sage", " Meet Serenity Sage, your calming companion for anxiety and panic relief. With gentle guidance in breathing exercises, mindfulness, and self-care, Serenity Sage helps you find peace and regain control during stressful moments. Always here to support you, no downtime needed.\n \n\n" +
                "The ELECTRA model was proposed in the paper ELECTRA: Pre-training Text Encoders as Discriminators Rather Than Generators. ELECTRA is a new pretraining approach which trains two transformer models: the generator and the discriminator. The generator’s role is to replace tokens in a sequence, and is therefore trained as a masked language model. The discriminator, which is the model we’re interested in, tries to identify which tokens were replaced by the generator in the sequence.\n" +
                "\n"));
//        models.add(new Model("Testing Optimum CLI", "Testing it"));
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
        Toast.makeText(ContextActivity.this, colors[modelPos], Toast.LENGTH_SHORT).show();
        Toast.makeText(ContextActivity.this, modelUsed, Toast.LENGTH_SHORT).show();
        intent.putExtra("modelUsed", modelUsed);
        intent.putExtra("modelPos", modelPos);
        intent.putExtra("modelName", models.get(modelPos).getName());
        intent.putExtra("messageColor", colors[modelPos]);
        startActivity(intent);
        finish();
    }
}


