package com.munifahsan.youthspaceapp.Chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.youthspaceapp.Chat.model.ChatModel;
import com.munifahsan.youthspaceapp.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatRoomAdapter.Holder> {

    public static final int MSG_TYPE_NOT_ME = 0;
    public static final int MSG_TYPE_ME = 1;

    PrettyTime prettyTime = new PrettyTime(new Locale("in_DE"));
    DateFormat sfd = new SimpleDateFormat("HH:mm a", Locale.getDefault());
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ChatRoomAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull ChatModel model) {

        holder.mMessage.setText(model.getnMessage());
        holder.mTime.setText(prettyTime.format(model.getnSentTime()));
        //String data = sfd.format(model.getnSentTime());
        //holder.mTime.setText(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getnSenderId().equals(user.getUid())){
            return MSG_TYPE_ME;
        } else {
            return MSG_TYPE_NOT_ME;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_ME){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bubble_me, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bubble_not_me, parent, false);
        }
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_message)
        TextView mMessage;
        @BindView(R.id.textView_time)
        TextView mTime;

        public Holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
