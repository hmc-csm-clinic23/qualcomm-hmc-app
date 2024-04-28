### [IMPORTANT] Read Me:
This repository contains some of the code necessary to run the 2024 Qualcomm CS Clinic team's project which is an android app built to run on the Qualcomm Snapdragon SOC. The app has three activities, a welcome page (`WelcomePage.java`), a model selection activity where users can select between different models that have been given different contexts to take on a different persona in order to assist with different types of tasks (`ContextActivity.java`), and the activity where users interact with their selected chat bot (`ChatBotActivity.java`). 

The code written in the repository needs a number of auxillary files to run. The first set of files are shared object files that are a part of the SNPE SDK which can be downloaded [from Qualcomm](https://developer.qualcomm.com/software/qualcomm-neural-processing-sdk).

Missing Qualcomm SNPE SDK Files
* `bert/src/main/cmakeLibs/arm64-v8a/libSNPE.so`
* `bert/src/main/jniLibs/arm64-v8a/libSNPE.so`
* `bert/src/main/jniLibs/arm64-v8a/libSnpeHtpPrepare.so`
* `bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV69Skel.so`
* `bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV69Stub.so`
* `bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV73Skel.so`
* `bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV73Stub.so`
* `bert/src/main/jniLibs/arm64-v8a/libc++_shared.so`

## Files to Add
The language models that are used in the chat bot, Distilbert and Electra small, are not included in the repository and need to be downloaded locally and placed in the following directories within the locally cloned repository.
* `bert/src/main/assets/distilbert1_optimum_cached.dlc`
* `bert/src/main/assets/distilbert_cached.dlc`
* `bert/src/main/assets/electra_small_squad2_cached.dlc`
In order to change the models that are used in the chat bot/to add more models that are in the DLC format to the current set of models a number of steps need to be followed:
* Need to name output tensors for the model that is being converted to DLC as `Identity` and `Identity_1`.
* All models need to be in the `bert/src/main/assets` folders.
* The paths/model names that are coded will need to be modified as well, specifically the `modelUsed` and `previousContext` in `bert/src/main/java/com/qualcomm/qti/qa/ui/activities/ChatActivity.java` should be modified as well as `modelUsed`, `DLCPaths`, `initializeModels()`,  in `bert/src/main/java/com/qualcomm/qti/qa/ui/activities/ContextActivity.java`.
We also recommend caching your models after converting your models to a DLC file since caching is faster using the following command: `snpe-dlc-graph-prepare --input_dlc frozen_models/distilbert.dlc --use_float_io --htp_archs v73 --set_output_tensors Identity:0,Identity_1:0`

## Welcome Page Activity
The `bert/src/main/java/com/qualcomm/qti/qa/ui/activities/WelcomePage.java` file holds the code for the initial welcome page of the app. Right now this is simply a button to transition to the `ContextActivity.java` file. The UI is dictated by `bert/src/main/res/layout/activity_welcome_page.xml`.

## Model Selection Activity
The model selection activity's code is found at `bert/src/main/java/com/qualcomm/qti/qa/ui/activities/ContextActivity.java`. The code in this file dictates how the model selection is loaded, creating a drop down element which gives different model personality names (e.g. Eric the Earthquake Helper) which upon selection, loads information about the chat bot assistant as well as the underlying model that is used in such a personality (e.g. ElectraSmall) and stores the currently selected model name and path, the context used to generate said model personality, as well as an associated color with said personality in order to update the UI on the chat bot page. The file that handles the UI for this page is found at `bert/src/main/res/layout/activity_context.xml`. Users will select their model using the drop down, then click the button in the bottom right corner that says "Go to chat" to proceed to the chat bot activity.

## Chat Bot Activity
The `bert/src/main/java/com/qualcomm/qti/qa/ui/activities/ChatActivity.java` file holds the code for interacting with the different model personalities that are included in the app. This activity supports the communication between the chatbot and user by loading the model in the backend with the written context that was passed in from `ContextActivity.java` when transitioning between activities when starting the activity, actively uses the langauge model to generate responses after they have been posted by the user, and updates the UI so users can see their messages and the bots messages. The UI is updated according to the related `bert/src/main/res/layout/activity_chat.xml` which can also be updated and modified for UI updates.

`bert/java/com.qualcomm.qti.qa/ui `:
* The before described activities can be found in the `activities` directory.
* Model.java is the class container that stores information about each model.

`bert/src/main/java/com/qualcomm/qti/qa/ml`:
* QaClient is called for inferences. Developers can modify what piece of hardware (e.g. DSP) inference is run on through their QaClient instances (see `ChatActivity` file for examples of usage).

`bert/res/layout`
* activity_chat.xml has the UI for the chatting app.
* bot_msg.xml is the UI for the bot message.
* user_msg.xml is the UI for the user message.
* activity_welcome_page.xml is the UI for the welcome page.
* activity_context.xml is the UI for the model selection page.
* activity_chat.xml is the UI for the chat bot page.
* toolbar.xml is the UI for the "Friends" banner at the top of the pages.

This repository is built off of the [Qualcomm NLP Solution 1](https://github.com/quic/qidk/tree/master/Solutions/NLPSolution1-QuestionAnswering) from QIDK Github.

