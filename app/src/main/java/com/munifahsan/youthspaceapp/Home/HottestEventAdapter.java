package com.munifahsan.youthspaceapp.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.youthspaceapp.Beasiswa.adapter.BeasiswaListAdapter;
import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HottestEventAdapter extends FirestoreRecyclerAdapter<HomeModel, HottestEventAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public HottestEventAdapter(@NonNull FirestoreRecyclerOptions<HomeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull HomeModel model) {
        holder.id = model.getId();
        Glide.with(holder.itemView)
                .load(model.getnImageUrl())
                .fitCenter()
                .into(holder.mImage);
        holder.mTitle.setText(model.getnTitle());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hottest_event, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.hottest_image)
        ImageView mImage;
        @BindView(R.id.hottest_text)
        TextView mTitle;
        @BindView(R.id.cardView_hottestEvent_card)
        CardView mCard;

        String id;
        public Holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, int position);
    }

}
