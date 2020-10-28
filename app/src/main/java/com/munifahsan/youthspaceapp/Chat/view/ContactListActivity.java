package com.munifahsan.youthspaceapp.Chat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.youthspaceapp.Chat.adapter.ContactListAdapter;
import com.munifahsan.youthspaceapp.Chat.model.ChatListModel;
import com.munifahsan.youthspaceapp.Chat.model.ChatModel;
import com.munifahsan.youthspaceapp.R;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.onItemClickListener {

    ProgressDialog progressdialog;

    private ContactListAdapter mAdapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference userRef = firebaseFirestore.collection("USERS");
    private CollectionReference chatRoomRef = firebaseFirestore.collection("CHAT_ROOM");
    private LinearLayoutManager mLayoutManager;
    Query query;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @BindView(R.id.recyclerView_addContactList)
    RecyclerView mRvContact;
    @BindView(R.id.textInput_search_addContact)
    TextInputEditText mSearch;
    @BindView(R.id.outlinedTextField)
    TextInputLayout mSearchLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ButterKnife.bind(this);
        query = userRef;

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);

        //showMessage(user.getUid());

        FirestoreRecyclerOptions<ChatListModel> options = new FirestoreRecyclerOptions.Builder<ChatListModel>()
                .setQuery(query, ChatListModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    //showMessage("muncul");
//                    mRvBeasiswaList.setVisibility(View.VISIBLE);
//                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    // showMessage("Kosong");
//                    mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mShimmer.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapter = new ContactListAdapter(options);
        mLayoutManager = new LinearLayoutManager(this);
        mRvContact.setLayoutManager(mLayoutManager);
        mRvContact.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

//        mSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //search(mSearch.getText().toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                search(mSearch.getText().toString());
//            }
//        });

    }

    public void search(String text) {
        if (!text.isEmpty()) {
            query = userRef.orderBy("nNama").startAt(text).endAt(text + "\uf0ff");

            FirestoreRecyclerOptions<ChatListModel> options = new FirestoreRecyclerOptions.Builder<ChatListModel>()
                    .setQuery(query, ChatListModel.class)
                    .build();

            mAdapter = new ContactListAdapter(options);
            mLayoutManager = new LinearLayoutManager(this);
            mRvContact.setLayoutManager(mLayoutManager);
            mRvContact.setAdapter(mAdapter);
            mAdapter.startListening();
        }

        if (text.trim().isEmpty()) {
            query = userRef;

            FirestoreRecyclerOptions<ChatListModel> options = new FirestoreRecyclerOptions.Builder<ChatListModel>()
                    .setQuery(query, ChatListModel.class)
                    .build();

            mAdapter = new ContactListAdapter(options);
            mLayoutManager = new LinearLayoutManager(this);
            mRvContact.setLayoutManager(mLayoutManager);
            mRvContact.setAdapter(mAdapter);
            mAdapter.startListening();
        }

    }

    @OnClick(R.id.button_search)
    public void onSearchClick() {
        search(mSearch.getText().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.stopListening();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(String id, int position) {
        progressdialog.show();
        getChatRoom(id);
    }

    @OnClick(R.id.floatingActionButton_back_addContack)
    public void onBackArrowClick() {
        finish();
    }

    public void getChatRoom(String id) {

        chatRoomRef.document(user.getUid() + id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //showMessage(id);
                ChatListModel model = documentSnapshot.toObject(ChatListModel.class);
                if (documentSnapshot.exists()) {
                    navigateToChatRoom(model.getId());
                } else {
                    chatRoomRef.document(id + user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ChatListModel model = documentSnapshot.toObject(ChatListModel.class);
                            if (documentSnapshot.exists()) {
                                navigateToChatRoom(model.getId());
                            } else {
                                createChatRoom(id);
                            }
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

    public void createChatRoom(String id) {
        if (id.equals(user.getUid())) {
            showMessage("Anda Tidak bisa melakukan obrolan dengan diri sendiri");
            progressdialog.dismiss();
        }

        if (!id.equals(user.getUid())) {
            Map<String, Object> map = new HashMap<>();
            map.put("nFrom", user.getUid());
            map.put("nTo", id);
            map.put("nCreatedAt", new Timestamp(new Date()));
            map.put("nUpdatedAt", new Timestamp(new Date()));
            map.put("nSpeakers", Arrays.asList(user.getUid(), id));
            map.put("nFromNumPeak", 0);
            map.put("nToNumPeak", 0);

            chatRoomRef.document(user.getUid() + id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    navigateToChatRoom(user.getUid() + id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        //chatRoomRef.document(user.getUid() + id).collection("CHAT").document().set()
    }

    public void navigateToChatRoom(String chatRoomId) {
        progressdialog.dismiss();
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("CHAT_ROOM_ID", chatRoomId);
        startActivity(intent);
    }
}