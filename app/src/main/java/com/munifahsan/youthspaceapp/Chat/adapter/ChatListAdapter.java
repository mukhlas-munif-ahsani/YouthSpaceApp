package com.munifahsan.youthspaceapp.Chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.youthspaceapp.Account.repo.AccountPageModel;
import com.munifahsan.youthspaceapp.Chat.model.ChatListModel;
import com.munifahsan.youthspaceapp.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends FirestoreRecyclerAdapter<ChatListModel, ChatListAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    PrettyTime prettyTime = new PrettyTime(new Locale("in_DE"));

    public onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChatListAdapter(@NonNull FirestoreRecyclerOptions<ChatListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull ChatListModel model) {
        holder.id = model.getId();

        if (!model.getnFrom().equals(holder.mCurrent_id)) {

            holder.mCollectionReference.document(model.getnFrom()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        AccountPageModel model = documentSnapshot.toObject(AccountPageModel.class);
                        holder.mNama.setText(model.getnNama());
                        Glide.with(holder.itemView)
                                .load(model.getnImageUrl())
                                .fitCenter()
                                .into(holder.mPhoto);
                    } else {

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            if (model.getnFromNumPeak() != 0) {
                holder.mNotifNumPeakLay.setVisibility(View.VISIBLE);
                holder.mNotifNumPeak.setText(String.valueOf(model.getnFromNumPeak()));
            }

        }

        if (!model.getnTo().equals(holder.mCurrent_id)) {
            holder.mCollectionReference.document(model.getnTo()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        AccountPageModel model = documentSnapshot.toObject(AccountPageModel.class);
                        holder.mNama.setText(model.getnNama());
                        Glide.with(holder.itemView)
                                .load(model.getnImageUrl())
                                .fitCenter()
                                .into(holder.mPhoto);
                    } else {

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            if (model.getnToNumPeak() != 0) {
                holder.mNotifNumPeakLay.setVisibility(View.VISIBLE);
                holder.mNotifNumPeak.setText(String.valueOf(model.getnToNumPeak()));
            }

        }

        holder.from = model.getnFrom();
        holder.to = model.getnTo();

        Glide.with(holder.itemView)
                .load(model.getnImageUrl())
                .fitCenter()
                .into(holder.mPhoto);
        //holder.mNama.setText(model.getnNama());
        holder.mPeakMessage.setText(model.getnPeakMsg());
        holder.mLastMsgTime.setText(prettyTime.format(model.getnUpdatedAt()));

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chat, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.circleImageView_photo_chatList)
        CircleImageView mPhoto;
        @BindView(R.id.textView_nama_chatList)
        TextView mNama;
        @BindView(R.id.textView_peakMessage_chatList)
        TextView mPeakMessage;
        @BindView(R.id.textView_lastMeesageTime_chatList)
        TextView mLastMsgTime;
        @BindView(R.id.textView_notifNumPeak_chatList)
        TextView mNotifNumPeak;
        @BindView(R.id.linearLayout_itemChatList)
        LinearLayout mLayout;
        @BindView(R.id.cardView_notifNumPeak_chatList)
        CardView mNotifNumPeakLay;
        String id;
        String from;
        String to;

        private FirebaseAuth mAuth;
        private String mCurrent_id;

        private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
        private CollectionReference mCollectionReference = mFirebaseFirestore.collection("USERS");

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mAuth = FirebaseAuth.getInstance();
            mCurrent_id = mAuth.getCurrentUser().getUid();

            mLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(id, position, from, to);
        }
    }

    public interface onItemClickListener {
        void onItemClick(String id, int position, String from, String to);
    }
}
