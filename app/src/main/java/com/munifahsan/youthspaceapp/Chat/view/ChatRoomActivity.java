package com.munifahsan.youthspaceapp.Chat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.munifahsan.youthspaceapp.Chat.adapter.ChatRoomAdapter;
import com.munifahsan.youthspaceapp.Chat.model.ChatModel;
import com.munifahsan.youthspaceapp.Chat.pres.ChatRoomPres;
import com.munifahsan.youthspaceapp.Chat.pres.ChatRoomPresInt;
import com.munifahsan.youthspaceapp.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity implements ChatRoomViewInt {

    private ChatRoomPresInt mChatRoomPresInt;

    @BindView(R.id.rv_chat_chatRoom)
    RecyclerView mRvChat;
    @BindView(R.id.progress_sending_chatRoom)
    ProgressBar mSendingProgress;
    @BindView(R.id.editText_message_chatRoom)
    EditText mMessage;
    @BindView(R.id.button_send_chatRoom)
    FloatingActionButton mSendBtn;
    @BindView(R.id.profile_image_chatRoom)
    CircleImageView mProfileImage;
    @BindView(R.id.textView_nama_chatRoom)
    TextView mNamaChatRoom;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference chatRef = firebaseFirestore.collection("CHAT_ROOM");
    private LinearLayoutManager mLayoutManager;
    Query query;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ChatRoomAdapter mChatAdapter;
    private String mChatRoomId;

    private int numPeak;
    private String fromOrTo;

    boolean isChatExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ButterKnife.bind(this);
        mChatRoomPresInt = new ChatRoomPres(this);
        mChatRoomPresInt.onCreate();

        Intent intent = getIntent();
        //showMessage(intent.getStringExtra("CHAT_ROOM_ID"));

        mChatRoomId = intent.getStringExtra("CHAT_ROOM_ID");

        query = chatRef.document(mChatRoomId).collection("CHAT")
                .orderBy("nSentTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

        mChatAdapter = new ChatRoomAdapter(options);
        mChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRvChat.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(mLayoutManager);
        mRvChat.setAdapter(mChatAdapter);

        mMessage.addTextChangedListener(sendMessageTextWatcher);
        mMessage.requestFocus();

        mChatRoomPresInt.getData(mChatRoomId);
        mChatRoomPresInt.getMsgData(mChatRoomId);

        /*
        check apakah sudah pernha chating
         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        mChatAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatRoomPresInt.onDestroy();
        mChatAdapter.stopListening();
    }

    private TextWatcher sendMessageTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String message = mMessage.getText().toString().trim();

            mSendBtn.setEnabled(!message.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @OnClick(R.id.button_send_chatRoom)
    public void onSendBtnClick() {
        //showProgress();
        ChatModel message = new ChatModel(user.getUid(), mMessage.getText().toString());

        if (!mMessage.getText().toString().isEmpty()) {
            sendMessage(message, mMessage.getText().toString());
            mMessage.setText("");
        }
    }

    @Override
    public void setNumPeak(int numPeak){
        this.numPeak = numPeak;
    }

    @Override
    public void setFromOrTo(String fromOrTo){
        this.fromOrTo = fromOrTo;
    }

    public void showProgress() {
        mSendingProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mSendingProgress.setVisibility(View.INVISIBLE);
    }

    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void sendMessage(ChatModel message, String msg) {
        chatRef.document(mChatRoomId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });

        /*
        mengirim pesan
         */
        chatRef.document(mChatRoomId).collection("CHAT").add(message)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // hideProgress();
                            //mSendBtn.setEnabled(true);
                        }
                    }
                });

        /*
        update peak message dan tanggal message
         */

        int n = numPeak + 1;

        if (fromOrTo.equals("from")){
            chatRef.document(mChatRoomId).update("nToNumPeak", n);
        }

        if (fromOrTo.equals("to")){
            chatRef.document(mChatRoomId).update("nFromNumPeak", n);
        }

        chatRef.document(mChatRoomId).update("nPeakMsg", msg,
                "nUpdatedAt", new Timestamp(new Date())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mChatRoomPresInt.getMsgData(mChatRoomId);
            }
        });

    }

    @Override
    public void setProfileImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .into(mProfileImage);
    }

    @Override
    public void setNamaChatRoom(String nama) {
        mNamaChatRoom.setText(nama);
    }

    @OnClick(R.id.floatingActionButton_back_chatRoom)
    public void onBackArrowClick(){
        finish();
    }
}