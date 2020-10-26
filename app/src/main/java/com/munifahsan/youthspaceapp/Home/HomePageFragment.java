package com.munifahsan.youthspaceapp.Home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    @BindView(R.id.imageSlider_promo_home)
    SliderView mImageSlider;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPromoRef = firebaseFirestore.collection("PROMO");
    private PromoAdapter mPromoAdapter;
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
        mImageSlider.setIndicatorSelectedColor(R.color.colorPrimary);
        mImageSlider.setIndicatorUnselectedColor(Color.GRAY);
        mImageSlider.setScrollTimeInSec(3);
        mImageSlider.setAutoCycle(true);
        mImageSlider.startAutoCycle();

        return view;
    }

    @OnClick(R.id.beasiswaButton)
    public void onBeasiswaClick(){
        Intent intent = new Intent(getActivity(), BeasiswaListActivity.class);
        startActivity(intent);
    }
}