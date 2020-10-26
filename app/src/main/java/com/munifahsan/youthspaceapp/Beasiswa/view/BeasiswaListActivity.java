package com.munifahsan.youthspaceapp.Beasiswa.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaModel;
import com.munifahsan.youthspaceapp.Beasiswa.adapter.BeasiswaListAdapter;
import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeasiswaListActivity extends AppCompatActivity implements BeasiswaListAdapter.OnItemClickListener {

    private BeasiswaListAdapter mBeasiswaListAdapter;
    @BindView(R.id.recyclerView_beasiswaList)
    RecyclerView mRvBeasiswaList;
    @BindView(R.id.shimmer_beasiswaList)
    ShimmerFrameLayout mShimmer;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference beasiswaRef = firebaseFirestore.collection("BEASISWA");
    private LinearLayoutManager mLayoutManager;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beasiswa_list);

        ButterKnife.bind(this);

        query = beasiswaRef;

        FirestoreRecyclerOptions<BeasiswaModel> options = new FirestoreRecyclerOptions.Builder<BeasiswaModel>()
                .setQuery(query, BeasiswaModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mRvBeasiswaList.setVisibility(View.VISIBLE);
                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    mRvBeasiswaList.setVisibility(View.INVISIBLE);
                    mShimmer.setVisibility(View.VISIBLE);
                }
            }
        });
        mBeasiswaListAdapter = new BeasiswaListAdapter(options);

        mLayoutManager = new LinearLayoutManager(this);
        mRvBeasiswaList.setLayoutManager(mLayoutManager);
        mRvBeasiswaList.setAdapter(mBeasiswaListAdapter);

        mBeasiswaListAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBeasiswaListAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeasiswaListAdapter.stopListening();
    }

    @OnClick(R.id.floatingActionButton_back_beasiswaList)
    public void onBackButtonClick() {
        finish();
    }

    @Override
    public void onItemClick(String id, int position) {
        showMessage(id);
        Intent intent = new Intent(this, DetailBeasiswaActivity.class);
        intent.putExtra("BEASISWA_ID", id);
        startActivity(intent);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}