package com.munifahsan.youthspaceapp.Login.repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;
import com.munifahsan.youthspaceapp.Login.LoginEvent;
import com.munifahsan.youthspaceapp.Register.RegisterEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginRepo implements LoginRepoInt {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public String mNowTime;

    private GoogleSignInOptions googleSignInOptions;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mCurrent_id;
    private String sMail;
    private String sNama;
    private String sNotlp;
    private String sImageUrl;
    private FirebaseFirestore mFirestore;

    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirebaseFirestore.collection("USERS");

    public LoginRepo() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void doEmailLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    /* TODO
                     *update token id for push notification*/
                    postEvent(LoginEvent.onSuccess, null);
                } else {
                    String error = task.getException().getMessage();
                    postEvent(LoginEvent.onError, error);
                }
            }
        });
    }

    @Override
    public void doGoogleSignIn(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("googleSignUp", "signInWithCredential:success");
//                            postEvent(LoginEvent.onSuccess, null);
                            mCurrent_id = mAuth.getCurrentUser().getUid();
                            sMail = mAuth.getCurrentUser().getEmail();
                            sNama = mAuth.getCurrentUser().getDisplayName();
                            sNotlp = mAuth.getCurrentUser().getPhoneNumber();
                            sImageUrl = mAuth.getCurrentUser().getPhotoUrl().toString();

                            input();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("googleSignUp", "signInWithCredential:failure", task.getException());
                            postEvent(LoginEvent.onError, task.getException().toString());
                        }

                    }
                });
    }

    @Override
    public void doFacebookSignIn(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //showMessage("facebook signin SUCCESS");
//                    postEvent(RegisterEvent.onSuccess, null);

                    mCurrent_id = mAuth.getCurrentUser().getUid();
                    sMail = mAuth.getCurrentUser().getEmail();
                    sNama = mAuth.getCurrentUser().getDisplayName();
                    sNotlp = mAuth.getCurrentUser().getPhoneNumber();
                    sImageUrl = mAuth.getCurrentUser().getPhotoUrl().toString();

                    input();
                } else {
                    postEvent(LoginEvent.onError, task.getException().toString());
                    //showMessage(task.getException().toString());
                }
            }
        });
    }

    public void input() {

        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            String token_id = task.getResult().getToken();

                            String user_id = mAuth.getCurrentUser().getUid();

                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("nToken_id", token_id);

                            mCollectionReference.document(user_id).update(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    postEvent(LoginEvent.onSuccess, null);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            postEvent(LoginEvent.onError, e.getMessage());
                        }
                    });
                } else {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            String token_id = task.getResult().getToken();

                            getNowTime();

                            String user_id = mAuth.getCurrentUser().getUid();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("nNama", sNama + " ");
                            userMap.put("nEmail", sMail);
                            userMap.put("nNoTlp", "");
                            userMap.put("nToken_id", token_id);
                            userMap.put("nCreated_at", mNowTime);
                            userMap.put("nLevel", "");
                            userMap.put("nGender", "");
                            userMap.put("nImageUrl", sImageUrl);

                            mFirestore.collection("USERS").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    postEvent(RegisterEvent.onSuccess, null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    postEvent(RegisterEvent.onError, e.getMessage());
                                }
                            });
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getNowTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy");
        mNowTime = dateFormat.format(calendar.getTime());
    }

    public void postEvent(int type, String errorMessage, String level) {
        LoginEvent event = new LoginEvent();

        event.setEventType(type);

        if (level != null) {
            event.setLevel(level);
        }

        if (errorMessage != null) {
            event.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }

    public void postEvent(int type, String errorMessage) {
        postEvent(type, errorMessage, null);
    }

}
