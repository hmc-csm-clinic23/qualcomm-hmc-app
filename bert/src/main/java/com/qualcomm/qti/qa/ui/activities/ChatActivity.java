package com.qualcomm.qti.qa.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qualcomm.qti.R;


//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;

import com.qualcomm.qti.qa.chat.MessageModel;
import com.qualcomm.qti.qa.chat.MessageRVAdapter;
import com.qualcomm.qti.qa.ml.QaAnswer;
import com.qualcomm.qti.qa.ml.QaClient;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // creating variables for our
    // widgets in xml file.
    private RecyclerView chatsRV;
    private ImageButton sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private Handler handler;
    private QaClient qaClient;

    private String modelUsed = "electra_small_squad2_cached.dlc";
    private String previousContext = "If someone finds themselves lost in the woods, it's essential to stay calm and focused to improve their chances of being found or finding their way out. Here are some crucial steps to take:Stop and Stay Put: As soon as you realize you're lost, stop walking. Continuing to move aimlessly can make it harder for rescuers to find you. Take a moment to assess your surroundings and gather your thoughts.Signal for Help: Use any available means to signal your location to potential rescuers. This could include blowing a whistle, shouting, or using a mirror or flashlight to reflect sunlight. Creating a signal fire can also attract attention. Stay Warm and Dry: If weather conditions are adverse, seek shelter to protect yourself from the elements. Use natural resources like branches, leaves, and rocks to build a makeshift shelter. Keep yourself warm and dry to prevent hypothermia. Stay Hydrated: Drink water regularly to stay hydrated, especially if you're exerting yourself or if it's hot outside. Avoid drinking from stagnant water sources, as they may contain harmful bacteria. Instead, try to find a flowing stream or collect rainwater.  Navigate with Caution: If you have a map and compass, use them to orient yourself and determine which direction to travel. However, if you're unfamiliar with navigation techniques, it's often best to stay put and wait for help.\n" +
            "\n" +
            "Use Trail Markers: Look for any signs of human activity or trail markers that could lead you back to a known path. Broken branches, footprints, or discarded items may indicate a nearby trail or road.Stay Positive and Patient: Remember that search and rescue teams are trained to locate lost individuals. Stay positive and patient, knowing that help is on the way. Use the time to conserve your energy and focus on staying safe.\n" +
            "\n" +
            "Stay Visible: Wear bright clothing if you have it, and avoid hiding in dense vegetation. Stay in an open area where you're more likely to be seen by search teams or passing hikers.\n" +
            "\n" +
            "Follow Water Sources: If you're near a river or stream, following it downstream can often lead to civilization. However, be cautious of steep terrain or other hazards along the way.\n" +
            "\n" +
            "Stay Rational: Avoid panicking or making impulsive decisions. Think through your actions carefully and prioritize your safety above all else.";


    // creating a variable for
    // our volley request queue.
//    private RequestQueue mRequestQueue;

    // creating a variable for array list and adapter class.
    private ArrayList<MessageModel> messageModelArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);

        // below line is to initialize our request queue.
//        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
//        mRequestQueue.getCache().clear();

        // creating a new array list
        messageModelArrayList = new ArrayList<>();

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
        messageRVAdapter = new MessageRVAdapter(messageModelArrayList, this);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);

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

        String contextAdd = userMsg;
        userMsg = userMsg.trim();
        String botResponse;
        if(false && previousContext.equals("")) {
            Toast.makeText(ChatActivity.this, userMsg, Toast.LENGTH_SHORT).show();
            previousContext = contextAdd;
            botResponse = "Please give more information";
            messageModelArrayList.add(new MessageModel(botResponse, BOT_KEY));

        }else {
            if (!userMsg.endsWith("?")) {
                userMsg += '?';
            }
            final String questionToAsk = userMsg;
            handler.removeCallbacksAndMessages(null);

//            handler.post(
//                    () -> {
                        String runtime= "DSP";

                        StringBuilder execStatus = new StringBuilder ();

                        long beforeTime = System.currentTimeMillis();
                        final List<QaAnswer> answers = qaClient.predict(questionToAsk, previousContext, runtime, execStatus);
                        previousContext += " " + contextAdd;

                        long afterTime = System.currentTimeMillis();
                        double totalSeconds = (afterTime - beforeTime) / 1000.0;
                        String displayMessage = runtime + " inference took : ";
                        displayMessage = String.format("%s %.3f sec. with %s", displayMessage, totalSeconds, modelUsed);
                        Toast.makeText(ChatActivity.this, displayMessage, Toast.LENGTH_SHORT).show();

                        if (!answers.isEmpty()) {

                            QaAnswer topAnswer = answers.get(0);
//                            botResponse = topAnswer.text;
                            final String response = topAnswer.text;
                            messageModelArrayList.add(new MessageModel(response, BOT_KEY));

                        }else {
                            Toast.makeText(ChatActivity.this, "Failed to get an answer", Toast.LENGTH_SHORT).show();

                        }
//                    });

//            botResponse = response;


        }


        messageRVAdapter.notifyDataSetChanged();
//        Toast.makeText(ChatActivity.this, "exit", Toast.LENGTH_SHORT).show();




//        messageModelArrayList.add(new MessageModel(botResponse, BOT_KEY));

        // notifying our adapter as data changed.



    }
}
