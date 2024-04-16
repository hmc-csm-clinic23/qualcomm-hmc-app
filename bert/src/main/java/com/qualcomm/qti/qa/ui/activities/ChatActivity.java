package com.qualcomm.qti.qa.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qualcomm.qti.R;
import java.util.Locale;

import com.qualcomm.qti.qa.chat.MessageModel;
import com.qualcomm.qti.qa.chat.MessageRVAdapter;
import com.qualcomm.qti.qa.ml.QaAnswer;
import com.qualcomm.qti.qa.ml.QaClient;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // creating variables for our
    // widgets in xml file.
    Bundle extras;
    private RecyclerView chatsRV;
    private ImageButton sendMsgIB;
    private EditText userMsgEdt;

    private TextView bannerTextView;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private Handler handler;
    private QaClient qaClient;

    TextToSpeech textToSpeech;


    private  String modelUsed = "distilbert_cached.dlc";

    private ArrayList<String> previousQuestions;
    int numQuestions = 3;

    private int context_no = 0;
    private String[] previousContext = {"If someone finds themselves lost in the woods, it's essential to stay calm and focused to improve their chances of being found or finding their way out.  As soon as you realize you're lost, stop walking. Continuing to move aimlessly can make it harder for rescuers to find you. Take a moment to assess your surroundings and gather your thoughts. Use any available means to signal your location to potential rescuers. This could include blowing a whistle, shouting, or using a mirror or flashlight to reflect sunlight. Creating a signal fire can also attract attention.  If weather conditions are adverse, seek shelter to protect yourself from the elements. Use natural resources like branches, leaves, and rocks to build a makeshift shelter. Keep yourself warm and dry to prevent hypothermia. Drink water regularly to stay hydrated, especially if you're exerting yourself or if it's hot outside. Avoid drinking from stagnant water sources, as they may contain harmful bacteria. Instead, try to find a flowing stream or collect rainwater. If you have a map and compass, use them to orient yourself and determine which direction to travel. However, if you're unfamiliar with navigation techniques, it's often best to stay put and wait for help. Look for any signs of human activity or trail markers that could lead you back to a known path. Broken branches, footprints, or discarded items may indicate a nearby trail or road. Remember that search and rescue teams are trained to locate lost individuals. Stay positive and patient, knowing that help is on the way. Use the time to conserve your energy and focus on staying safe.  Wear bright clothing if you have it, and avoid hiding in dense vegetation. Stay in an open area where you're more likely to be seen by search teams or passing hikers. If you're near a river or stream, following it downstream can often lead to civilization. However, be cautious of steep terrain or other hazards along the way. Avoid panicking or making impulsive decisions. Think through your actions carefully and prioritize your safety above all else.",
            "Dealing with an earthquake and its aftershocks requires preparedness, quick response, and safety awareness. Before an earthquake, secure heavy furniture and objects that could fall, and identify safe places in each room, such as under sturdy tables or against interior walls. During the quake, drop to the ground, take cover under something sturdy, and hold on until the shaking stops, avoiding doorways and windows. Once the initial quake ends, be prepared for aftershocks, which can be strong and cause additional damage. Stay away from damaged buildings and structures, as these can be further compromised by aftershocks. Keep emergency supplies, like water, food, and first aid kits, accessible, and plan evacuation routes in advance. After the shaking stops, check for injuries and hazards, such as gas leaks or electrical shorts, and use text messages or social media to communicate with family and emergency services to avoid overwhelming phone lines. Stay informed through official sources for updates and instructions on recovery efforts and potential further seismic activity.",
            "If a small fire starts in a pan, do not try to move it. Use an oven mitt to carefully slide a lid over the burning pan. Turn off the heat and leave it there until it has completely cooled. Never throw water on a grease fire. If a fire starts in the oven, toaster oven or microwave, turn off the heat and keep the door shut. Replace the appliance or have it serviced before using it again. Call the fire department to report the fire. Do not try to fight large fires on your own. Get out of the house and call 9-1-1. Always stay in the kitchen while using the stove. Turn off the stove if you leave the room. Designate a “kids-free zone” that is at least 3 feet away from the stove. Always turn pot handles toward the back of the stove and use the back burners whenever possible. Roll up sleeves before cooking. Loose-fitting clothing can catch on fire. Clean the stove, oven and burners regularly to prevent grease buildup. Keep items that can catch on fire, such as dish towels and pot holders, away from the stove. Plug cooking appliances directly into an outlet. Never use an extension cord for a cooking appliance. Tuck appliance cords out of reach of children. Only use microwave-safe containers to heat things in the microwave. If a smoke alarm sounds during normal cooking, open a door or fan the area. Do not disable the smoke alarm or take out the batteries.",
            "Adhesive bandages in a variety of shapes and sizes. Cuts and scrapes come in all sizes and can occur on all parts of the body, so be prepared with different types of bandages. Gauze dressing pads. For burns, deep cuts, and bigger scrapes and lacerations, you may need to use gauze or compress dressing to clean and cover the wound. Gauze dressing with alcohol. Alcohol is used to clean and disinfect the skin, so make sure to use these before dressing a wound. Gauze bandage roll. After a gauze dressing is applied, use a gauze bandage roll to keep the dressing in place and to absorb any fluids that get through the first layer. Antiseptic gel or cream, such as Polyline. An antiseptic is used to clean wounds and prevent infections caused by bacteria or fungi.Antiseptic spray. Antiseptic spray can come in handy for accidents that involve many scrapes or wounds.Microporous wound closure strips. Used to secure dressings, the microporous tape is a breathable medical tape that is a necessity in every first aid kit. Elastic bandage. Compression bandages are very handy and can be used to secure dressings, keep a sprained ankle or wrist in place, or apply pressure. Cotton wool. Cotton is used for cleaning wounds as well as padding and protection. Scissors. Scissors can be used for cutting gauze, bandages, and tape, but they may also be necessary for cutting away clothing to expose injured areas. Eyewash. Eyewashes can be used to help release a foreign object from the eye or to wash out toxic substances. Antihistamine tablets and cream. Used to treat allergic reactions and itchy rashes. Medical rubber tourniquet. Tourniquets help to stop blood flow to a wound and limit blood loss. They can be dangerous when not used properly. Learn when a tourniquet may be necessary.",
            "Key Features:\n" +
                    "\n" +
                    "Calming Techniques: You can suggests various calming techniques such as progressive muscle relaxation, visualization, and grounding exercises to help the user manage their anxiety.\n" +
                    "\n" +
                    "Guided Breathing: You can provide guided breathing exercises, such as deep breathing and the 4-7-8 technique, to help the user slow their breathing and reduce feelings of panic.\n" +
                    "\n" +
                    "Reassurance: You can offer reassuring messages to remind the user that panic attacks are temporary and not life-threatening. You can also encourages the user to focus on the present moment and take one step at a time.\n" +
                    "\n" +
                    "Mindfulness and Meditation: You can introduce simple mindfulness exercises and short meditation practices to help the user stay grounded and reduce racing thoughts.\n" +
                    "\n" +
                    "Self-Care Reminders: You should emphasize the importance of self-care and encourage the user to engage in activities that promote relaxation and well-being, such as taking a walk, listening to soothing music, or practicing yoga.\n" +
                    "\n" +
                    "Emergency Support: In case of severe anxiety or panic attacks that require professional intervention, You should provide information on how to seek immediate help from a healthcare provider or a mental health hotline.\n" +
                    "\n" +
                    "User Interaction Guidelines:\n" +
                    "\n" +
                    "You should use a calm and empathetic tone throughout the conversation.\n" +
                    "You should provide clear and concise instructions for each technique or exercise.\n" +
                    "You should avoid overwhelming the user with too many options at once and instead focus on one technique at a time.\n" +
                    "You should regularly check in with the user to assess their comfort level and adjust suggestions accordingly.\n" +
                    "You should respect the user's pace and allow them to take breaks or end the conversation if needed.\n" +
                    "\n" +
                    "Limitations:\n" +
                    "\n" +
                    "You are not a substitute for professional medical advice or treatment. You are intended to provide immediate support during a panic or anxiety attack but should not replace consultation with a qualified healthcare provider for ongoing mental health issues.\n" +
                    "\n" +
                    "Tornado:\n" +
                    "Staying safe during a tornado involves preparation and understanding the best places to take shelter. Before tornado season, identify a safe room or area in your home, such as a basement, storm cellar, or an interior room on the lowest floor with no windows. Prepare an emergency kit containing water, non-perishable food, a flashlight, batteries, a first aid kit, and a whistle to signal for help. When a tornado warning is issued, move to your designated safe area immediately. If you are in a building, go to the center of the lowest level, away from corners, windows, doors, and outside walls. Get under a sturdy piece of furniture and protect your head and neck with your arms or a heavy coat or blanket. If you are outside or in a mobile home, seek shelter in a nearby building or a storm shelter. If there is no shelter available, lie flat in a nearby ditch or low-lying area and cover your head and neck with your arms. Avoid areas with potential for flooding. Stay in the shelter location until the tornado has passed and listen to a battery-powered radio or a weather app for updates from local authorities on when it is safe to emerge."
    };

    // creating a variable for
    // our volley request queue.

    // creating a variable for array list and adapter class.
    private ArrayList<MessageModel> messageModelArrayList;
    private MessageRVAdapter messageRVAdapter;
    private String modelName;

    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        bannerTextView = findViewById(R.id.bannerTextView);

        extras = getIntent().getExtras();
        color = extras.getString("messageColor");
        modelUsed = extras.getString("modelUsed");
        context_no = extras.getInt("modelPos");
        modelName = extras.getString("modelName");
        bannerTextView.setText("You are chatting with " + modelName);
        bannerTextView.setBackgroundColor(Color.parseColor(color));

        // creating a new array list
        messageModelArrayList = new ArrayList<>();
        previousQuestions = new ArrayList<>();
        int held_questions = 3;

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the message entered
                // by user is empty or not.
                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(ChatActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to send message
                // to our bot to get response.
                sendMessage(userMsgEdt.getText().toString());

                // below line we are setting text in our edit text as empty
                userMsgEdt.setText("");
            }
        });

        // on below line we are initializing our adapter class and passing our array list to it.
        messageRVAdapter = new MessageRVAdapter(messageModelArrayList, this, color);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);

        // creation of handler thread to run QAClient (inference)
        HandlerThread handlerThread = new HandlerThread("QAClient");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        qaClient = new QaClient(this);
    }

    protected void onStart() {
        super.onStart();
        handler.post(
                ()-> {
                    String init_files = qaClient.loadModel(modelUsed);
                    qaClient.loadDictionary();
                    Toast.makeText(ChatActivity.this, init_files, Toast.LENGTH_SHORT).show();
                }
        );

    }
    @Override
    public void onBackPressed() {
        goToContextActivity();
    }

    private void goToContextActivity(){
        Intent intent = new Intent(this, ContextActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onStop() {
//        Log.v(TAG, "onStop");
        super.onStop();
        handler.post(() -> qaClient.unload());
    }

    private void sendMessage(String userMsg) {
        // below line is to pass message to our
        // array list which is entered by the user.
        messageModelArrayList.add(new MessageModel(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();

        userMsg = userMsg.trim();
        String botResponse;
//        final String questionToAsk = userMsg;
        previousQuestions.add(userMsg);
        if(previousQuestions.size() > numQuestions) {
            previousQuestions.remove(0);
        }
        String combinedQuestions = "";
        for (int i = 0; i < previousQuestions.size(); i++) {
            combinedQuestions = combinedQuestions + previousQuestions.get(i) + " ";
        }

        if (!combinedQuestions.endsWith("?")) {
            combinedQuestions += '?';
        }

        final String questionToAsk = combinedQuestions;
        handler.removeCallbacksAndMessages(null);
        String runtime= "DSP";

        StringBuilder execStatus = new StringBuilder ();

        long beforeTime = System.currentTimeMillis();
        final List<QaAnswer> answers = qaClient.predict(questionToAsk, previousContext[context_no], runtime, execStatus);

        long afterTime = System.currentTimeMillis();
        double totalSeconds = (afterTime - beforeTime) / 1000.0;
        String displayMessage = runtime + " inference took : ";
        displayMessage = String.format("%s %s %.3f sec. with %s", modelUsed, displayMessage, totalSeconds, modelUsed);
        Toast.makeText(ChatActivity.this, displayMessage, Toast.LENGTH_SHORT).show();

        if (!answers.isEmpty()) {

            QaAnswer topAnswer = answers.get(0);
            final String response = topAnswer.text;
            speak(response);
            messageModelArrayList.add(new MessageModel(response, BOT_KEY));
        } else {
            Toast.makeText(ChatActivity.this, "Failed to get an answer", Toast.LENGTH_SHORT).show();
            botResponse = "Please give more information";
            speak(botResponse);
            messageModelArrayList.add(new MessageModel(botResponse, BOT_KEY));
        }

        messageRVAdapter.notifyDataSetChanged();
    }

    private void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
