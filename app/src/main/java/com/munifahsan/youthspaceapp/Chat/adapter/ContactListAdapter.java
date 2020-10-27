package com.munifahsan.youthspaceapp.Chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.munifahsan.youthspaceapp.Chat.model.ChatListModel;
import com.munifahsan.youthspaceapp.Chat.model.ChatModel;
import com.munifahsan.youthspaceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends FirestoreRecyclerAdapter<ChatListModel, ContactListAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ContactListAdapter(@NonNull FirestoreRecyclerOptions<ChatListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull ChatListModel model) {
        holder.id = model.getId();

        Glide.with(holder.itemView)
                .load(model.getnImageUrl())
                .fitCenter()
                .into(holder.mImage);
        holder.mNama.setText(model.getnNama());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.circleImageView_photo_addContact)
        CircleImageView mImage;
        @BindView(R.id.textView_nama_addContact)
        TextView mNama;
        @BindView(R.id.layout_addContact)
        FrameLayout mlayout;

        String id;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mlayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position);
        }
    }

    public interface onItemClickListener {
        void onItemClick(String id, int position);
    }
}
