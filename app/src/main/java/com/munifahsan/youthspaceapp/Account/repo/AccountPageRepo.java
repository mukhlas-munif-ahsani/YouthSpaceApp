package com.munifahsan.youthspaceapp.Account.repo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.youthspaceapp.Account.AccountPageEvent;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

public class AccountPageRepo implements AccountPageRepoInt {

    private FirebaseAuth mAuth;
    private String mCurrent_id;

    private boolean exist;

    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirebaseFirestore.collection("USERS");

    public AccountPageRepo() {
        mAuth = FirebaseAuth.getInstance();
        mCurrent_id = mAuth.getCurrentUser().getUid();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    }

    @Override
    public void getProfileDataFirebase(){
        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    AccountPageModel model =documentSnapshot.toObject(AccountPageModel.class);
                    postEvent(AccountPageEvent.onSuccess,
                            model.getnImageUrl(),
                            model.getnNama(),
                            model.getnEmail(),
                            model.getnNoTlp(),
                            model.getnGender());
                } else {
                    postEvent(AccountPageEvent.onError, "Data pengguna tidak ditemukan");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(AccountPageEvent.onError, e.getMessage());
            }
        });
    }

    @Override
    public void getProfileDataGoogleOrFacebook(){

    }

    @Override
    public void checkProfileDataExist(){

        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                exist = documentSnapshot.exists();
            }
        });

    }

    @Override
    public Boolean isProfileDataExist(){
        return exist;
    }

    public void postEvent(int type, String errorMessage, String imageUrl, String nama, String email, String noTlp, String gender){
        AccountPageEvent event = new AccountPageEvent();

        event.setEventType(type);

        if (errorMessage != null){
            event.setErrorMessage(errorMessage);
        }

        event.setImageUrl(imageUrl);
        event.setNama(nama);
        if (email == null){
            event.setEmail("");
        } else {
            event.setEmail(email);
        }
        event.setNo_tlp(noTlp);
        event.setGender(gender);

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }

    private void postEvent(int type, String imageUrl, String nama, String email, String noTlp, String gender){
        postEvent(type, null, imageUrl, nama, email, noTlp, gender);
    }

    private void postEvent(int type, String errorMessage) {
        postEvent(type, errorMessage, null,null, null, null, null);
    }
}
