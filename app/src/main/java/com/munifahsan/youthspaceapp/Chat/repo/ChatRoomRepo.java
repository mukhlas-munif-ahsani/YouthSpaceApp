package com.munifahsan.youthspaceapp.Chat.repo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.youthspaceapp.Chat.ChatEvent;
import com.munifahsan.youthspaceapp.Chat.model.ChatListModel;
import com.munifahsan.youthspaceapp.Chat.model.ChatModel;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

public class ChatRoomRepo implements ChatRoomRepoInt {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference chatRoomRef = firebaseFirestore.collection("CHAT_ROOM");
    private CollectionReference userRef = firebaseFirestore.collection("USERS");

    public ChatRoomRepo() {
    }

    @Override
    public void getData(String chatRoomId){
        chatRoomRef.document(chatRoomId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatListModel model = documentSnapshot.toObject(ChatListModel.class);
                if (!model.getnFrom().equals(user.getUid())){
                    getUser(model.getnFrom());
                }

                if (!model.getnTo().equals(user.getUid())){
                    getUser(model.getnTo());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getUser(String userId){
        userRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatModel model = documentSnapshot.toObject(ChatModel.class);
                postEvent(ChatEvent.onSuccess, null,
                        model.getnImageUrl(),
                        model.getnNama());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void postEvent(int type, String errorMessage, String imageUrl, String nama){
        ChatEvent event = new ChatEvent();
        event.setEventType(type);

        if (errorMessage != null){
            event.setError(errorMessage);
        }

        event.setImage(imageUrl);
        event.setNama(nama);

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }
}
