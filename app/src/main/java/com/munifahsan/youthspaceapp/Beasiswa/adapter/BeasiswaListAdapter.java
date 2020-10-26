package com.munifahsan.youthspaceapp.Beasiswa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaModel;
import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class BeasiswaListAdapter extends FirestoreRecyclerAdapter<BeasiswaModel, BeasiswaListAdapter.Holder> {
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

    public BeasiswaListAdapter(@NonNull FirestoreRecyclerOptions<BeasiswaModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull BeasiswaModel model) {
        holder.id = model.getId();

        Glide.with(holder.itemView)
                .load(model.getnImage())
                .transform(new BlurTransformation())
                .fitCenter()
                .into(holder.mImage);

        holder.mKategori.setText(model.getnKategori());
        holder.mTitle.setText(model.getnTitle());
        holder.mLokasi.setText(model.getnLokasi());
        holder.mDeadline.setText(model.getnDeadline());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beasiswa_list, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView_thumbnail_itemBeasisList)
        ImageView mImage;
        @BindView(R.id.textView_kategori_itemBeasiswaList)
        TextView mKategori;
        @BindView(R.id.textView_title_itemBeasiswaList)
        TextView mTitle;
        @BindView(R.id.textView_lokasi_itemBeasiswaList)
        TextView mLokasi;
        @BindView(R.id.textView_deadline_itemBessiswaList)
        TextView mDeadline;
        @BindView(R.id.linearLayout_itemBeasiswaList)
        LinearLayout mLinearLayout;

        String id;

        public Holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mLinearLayout.setOnClickListener(this);
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
