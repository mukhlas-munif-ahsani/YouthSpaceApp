package com.munifahsan.youthspaceapp.Home;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.youthspaceapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PromoAdapter extends SliderViewAdapter<PromoAdapter.Holder> {

    private List<PromoModel> mData;

    public PromoAdapter() {
        this.mData = new ArrayList<>();
    }

    public void addCardItem(PromoModel item) {
        mData.add(item);
    }

    @Override
    public PromoAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(PromoAdapter.Holder viewHolder, int position) {
        viewHolder.textViewDescription.setText(mData.get(position).getnTitle());
        Glide.with(viewHolder.itemView)
                .load(mData.get(position).getnImage())
                .transform(new BlurTransformation())
                .fitCenter()
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageViewBackground;
        TextView textViewDescription;
        ShimmerFrameLayout msShimmer;

        public Holder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            msShimmer = itemView.findViewById(R.id.shimmerSlider);
        }
    }
}
