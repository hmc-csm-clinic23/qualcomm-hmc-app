## Files to Add
Models 
* bert/src/main/assets/distilbert1_optimum_cached.dlc
* bert/src/main/assets/distilbert_cached.dlc
* bert/src/main/assets/electra_small_squad2_cached.dlc
  
Library Files
* bert/src/main/cmakeLibs/arm64-v8a/libSNPE.so
* bert/src/main/jniLibs/arm64-v8a/libSNPE.so
* bert/src/main/jniLibs/arm64-v8a/libSnpeHtpPrepare.so
* bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV69Skel.so
* bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV69Stub.so
* bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV73Skel.so
* bert/src/main/jniLibs/arm64-v8a/libSnpeHtpV73Stub.so
* bert/src/main/jniLibs/arm64-v8a/libc++_shared.so


## Chatting App
bert/java/com.qualcomm.qti.qa/chat have 2 files
* MessageModel contains code on how user and bot sends messages
* MessageRVAdapter makes sure updates to UI messaging screen get pushed through

bert/java/com.qualcomm.qti.qa/ui 
* ChatActivity has the activity that controlles the UI for the Chatting App
* QaClient is called for the inferences

bert/res/layout
* activity_chat.xml has the ui for the chatting app
* bot_msg.xml is the ui for the bot message
* user_msg.xml is the ui for the user message

## Switch DLCs App
bert/java/com.qualcomm.qti.qa/ui 
* ContextActivity has the activity that controlls the switch between DLCS and allows for a context and a question
  
bert/res/layout
* activity_context.xml has the UI for the context Activity
 * Modify both to add a new DLC 
  * DLCFiles contains the names of the Models
  * DLCPaths  are the paths to the Models

