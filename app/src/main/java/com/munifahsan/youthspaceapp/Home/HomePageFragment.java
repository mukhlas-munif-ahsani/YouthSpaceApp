package com.munifahsan.youthspaceapp.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaModel;
import com.munifahsan.youthspaceapp.Beasiswa.adapter.BeasiswaListAdapter;
import com.munifahsan.youthspaceapp.Beasiswa.view.BeasiswaListActivity;
import com.munifahsan.youthspaceapp.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageFragment extends Fragment {

    @BindView(R.id.imageSlider_promo_home2)
    SliderView mImageSlider;

    @BindView(R.id.recyclerView_hottestEvent_homePage)
    RecyclerView mRvHottestEvent;
    @BindView(R.id.recyclerView_artikelTerkini_homePage)
    RecyclerView mRvArtikelTerkini;

    @BindView(R.id.textView_greating_homePage)
    TextView mGreatingText;
    @BindView(R.id.textView_nama_homePage)
    TextView mNama;
    @BindView(R.id.textView_cardText1_homePage)
    TextView mCardText1;
    @BindView(R.id.textView_cardText2_homePage)
    TextView mCardText2;
    @BindView(R.id.imageView_welcomeImage_homePage)
    ImageView mImageWelcome;

    @BindView(R.id.content_homePage)
    LinearLayout mContent;
    @BindView(R.id.shimmer_homePage)
    ShimmerFrameLayout mShimmer;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = firebaseFirestore.collection("USERS");
    private CollectionReference mPromoRef = firebaseFirestore.collection("PROMO");
    private CollectionReference mHottestEventRef = firebaseFirestore.collection("HOTTEST_EVENT");
    private CollectionReference mArtikelTerkiniRef = firebaseFirestore.collection("ARTIKEL_TERKINI");
    private DocumentReference mPageContentRef = firebaseFirestore.collection("PAGE_CONTENT").document("HOME_PAGE");
    private PromoAdapter mPromoAdapter;
    private HottestEventAdapter mHottestEventAdapter;
    private ArtikelTerkiniAdapter mArtikelTerkiniAdapter;
    private LinearLayoutManager mLayoutManager;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Query query;

    public HomePageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        ButterKnife.bind(this, view);

        mPromoAdapter = new PromoAdapter();

        mPromoRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<PromoModel> mPromoList = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        PromoModel model = documentSnapshot.toObject(PromoModel.class);
                        mPromoAdapter.addCardItem(model);
                    }

                    mImageSlider.setSliderAdapter(mPromoAdapter);
                } else {
                    Log.d("GET_DATA_ERROR", task.getException().toString());
                }
            }
        });

        mImageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mImageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mImageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mImageSlider.setIndicatorSelectedColor(Color.parseColor("#62C3D0"));
        mImageSlider.setIndicatorUnselectedColor(Color.parseColor("#F5F5F5"));
        mImageSlider.setScrollTimeInSec(3);
        mImageSlider.setAutoCycle(true);
        mImageSlider.startAutoCycle();

        showHottestEvent();
        showArtikelTerkini();
        loadHomeContent();
        return view;
    }

    private void showHottestEvent() {
        query = mHottestEventRef;

        FirestoreRecyclerOptions<HomeModel> options = new FirestoreRecyclerOptions.Builder<HomeModel>()
                .setQuery(query, HomeModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mRvHottestEvent.setVisibility(View.VISIBLE);
//                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mHottestEventAdapter = new HottestEventAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvHottestEvent.setLayoutManager(mLayoutManager);
        mRvHottestEvent.setAdapter(mHottestEventAdapter);

    }

    private void showArtikelTerkini() {
        query = mArtikelTerkiniRef;

        FirestoreRecyclerOptions<HomeModel> options = new FirestoreRecyclerOptions.Builder<HomeModel>()
                .setQuery(query, HomeModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mRvArtikelTerkini.setVisibility(View.VISIBLE);
//                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mArtikelTerkiniAdapter = new ArtikelTerkiniAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvArtikelTerkini.setLayoutManager(mLayoutManager);
        mRvArtikelTerkini.setAdapter(mArtikelTerkiniAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHottestEventAdapter.startListening();
        mArtikelTerkiniAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHottestEventAdapter.stopListening();
        mArtikelTerkiniAdapter.stopListening();
    }

    public void loadHomeContent(){
        mPageContentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HomeModel model = documentSnapshot.toObject(HomeModel.class);
                mGreatingText.setText(model.getnGreating());
                mCardText1.setText(model.getnHeaderCardText1());
                mCardText2.setText(model.getnHeaderCardText2());
                Glide.with(getActivity())
                        .load(model.getnWelcomeImageUrl())
                        .fitCenter()
                        .into(mImageWelcome);

                mContent.setVisibility(View.VISIBLE);
                mShimmer.setVisibility(View.INVISIBLE);
            }
        });

        mUsersRef.document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HomeModel model = documentSnapshot.toObject(HomeModel.class);
                int index = model.getnNama().indexOf(' ');
                mNama.setText(model.getnNama().substring(0, index));
            }
        });
    }

    @OnClick(R.id.linearLayout_iconBeasiswa_home)
    public void onBeasiswaClick(){
        Intent intent = new Intent(getActivity(), BeasiswaListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linearLayout_iconEvent_home)
    public void onEventClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconPromo_home)
    public void onPromoClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconVolunteering_home)
    public void onVolunteeringClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconLomba_home)
    public void onLombaClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconMarketplace_home)
    public void onMarketPlaceClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconSurvey_home)
    public void onSurveyClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconForum_home)
    public void onForumClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconCrowdfunding_home)
    public void onCrowdfundingClick(){
        showMessage("Coming Soon !!");
    }

    @OnClick(R.id.linearLayout_iconPetisi_home)
    public void onPetisiClick(){
        showMessage("Coming Soon !!");
    }

    public void showMessage(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}