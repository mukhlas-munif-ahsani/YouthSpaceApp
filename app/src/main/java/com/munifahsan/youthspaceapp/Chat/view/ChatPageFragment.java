package com.munifahsan.youthspaceapp.Chat.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.youthspaceapp.Chat.adapter.ChatListAdapter;
import com.munifahsan.youthspaceapp.Chat.model.ChatListModel;
import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatPageFragment extends Fragment implements ChatListAdapter.onItemClickListener{

    @BindView(R.id.recyclerView_chatListt)
    RecyclerView mRvChatList;
    @BindView(R.id.floatingActionButton_add_chatPage)
    FloatingActionButton mAddButton;
    @BindView(R.id.shimmer_fragmentChatPage)
    ShimmerFrameLayout mShimmer;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference chatRef = firebaseFirestore.collection("CHAT_ROOM");
    private LinearLayoutManager mLayoutManager;
    Query query;
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

    private ChatListAdapter mAdapter;

    public ChatPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_page, container, false);

        ButterKnife.bind(this, view);

        query = chatRef.whereArrayContains("nSpeakers", user.getUid());

        showMessage(user.getUid());
        FirestoreRecyclerOptions<ChatListModel> options = new FirestoreRecyclerOptions.Builder<ChatListModel>()
                .setQuery(query, ChatListModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    showMessage("muncul");
                    mRvChatList.setVisibility(View.VISIBLE);
                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                   // showMessage("Kosong");
//                    mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mShimmer.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapter = new ChatListAdapter(options);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvChatList.setLayoutManager(mLayoutManager);
        mRvChatList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.stopListening();
    }

    @Override
    public void onItemClick(String id, int position) {
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra("CHAT_ROOM_ID", id);
        startActivity(intent);
    }

    @OnClick(R.id.floatingActionButton_add_chatPage)
    public void onAddButtonClick(){
        Intent intent = new Intent(getActivity(), ContactListActivity.class);
        startActivity(intent);
    }

    public void showMessage(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}