package com.munifahsan.youthspaceapp.EditProfile.repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.youthspaceapp.Account.AccountPageEvent;
import com.munifahsan.youthspaceapp.Account.repo.AccountPageModel;
import com.munifahsan.youthspaceapp.EditProfile.EditProfileEvent;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileRepo implements EditProfileRepoInt {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public String mNowTime;

    private FirebaseAuth mAuth;
    private String mCurrent_id;

    private boolean exist;

    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirebaseFirestore.collection("USERS");

    public EditProfileRepo() {
        mAuth = FirebaseAuth.getInstance();
        mCurrent_id = mAuth.getCurrentUser().getUid();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    }

    @Override
    public void getData(){
        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    EditProfileModel model =documentSnapshot.toObject(EditProfileModel.class);
                    postEvent(EditProfileEvent.onSuccess,
                            model.getnImageUrl(),
                            model.getnNama(),
                            model.getnEmail(),
                            model.getnNoTlp(),
                            model.getnGender());
                    Log.d("nama in getdata: " , model.getnNama());
                } else {
                    postEvent(EditProfileEvent.onError, "Data pengguna tidak ditemukan");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(EditProfileEvent.onError, e.getMessage());
            }
        });
    }

    public void getDataAfterUpdate(){
        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    EditProfileModel model =documentSnapshot.toObject(EditProfileModel.class);
                    postEvent(EditProfileEvent.onSuccess,
                            null,
                            model.getnNama(),
                            model.getnEmail(),
                            model.getnNoTlp(),
                            model.getnGender());
                } else {
                    postEvent(EditProfileEvent.onError, "Data pengguna tidak ditemukan");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(EditProfileEvent.onError, e.getMessage());
            }
        });
    }

    @Override
    public void updateData(String imageUrl, String email, String nama, String noTlp, String gender){

        getNowTime();

        Map<String, Object> userMap = new HashMap<>();
        if (imageUrl != null){
            userMap.put("nImageUrl", imageUrl);
        }
        if (email != null){
            userMap.put("nEmail", email);
        }
        userMap.put("nNama", nama);
        userMap.put("nNoTlp", noTlp);
        userMap.put("nGender", gender);
        userMap.put("nUpdated_at", mNowTime);

        mCollectionReference.document(mCurrent_id).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                getDataAfterUpdate();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(EditProfileEvent.onError, e.getMessage());
            }
        });
    }

    public void getNowTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy");
        mNowTime = dateFormat.format(calendar.getTime());
    }

    public void postEvent(int type, String errorMessage, String imageUrl, String nama, String email, String noTlp, String gender){
        EditProfileEvent event = new EditProfileEvent();

        event.setEventType(type);

        if (errorMessage != null){
            event.setErrorMessage(errorMessage);
        }
        Log.d("nama in post event: " , nama);
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
