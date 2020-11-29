package mhj.Grp10_AppProject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    public interface IMessageClickedListener {
        void onMessageClicked(int index);
    }

    private IMessageClickedListener listener;
    private FirebaseStorage mStorageRef;
    private List<PrivateMessage> messagelist;

    public InboxAdapter(IMessageClickedListener listener) {
        mStorageRef = FirebaseStorage.getInstance();
        this.listener = listener;
    }

    public void updateMessageList(List<PrivateMessage> list){
        messagelist = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_list_item, parent, false);
        InboxViewHolder vh = new InboxViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position)
    {
        holder.senderUserName.setText(String.valueOf(messagelist.get(position).getSender()));
        holder.messageDate.setText(messagelist.get(position).getMessageDate());
        holder.itemRegarding.setText(messagelist.get(position).getRegarding());
        
        Glide.with(holder.senderUserImg.getContext())
                .load(R.drawable.emptycart)
                .placeholder(R.drawable.emptycart)
                .into(holder.senderUserImg);
    }

    @Override
    public int getItemCount()
    {
        //Return size of list
        return messagelist.size();
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //viewholder ui widget references
        ImageView senderUserImg;
        TextView senderUserName, itemRegarding, messageDate, readStatus;

        //custom callback interface for user actions
        //IMessageClickedListener listener;

        public InboxViewHolder(@NonNull View itemView)
        {
            super(itemView);

            //references from layout
            senderUserImg = itemView.findViewById(R.id.inboxImgUser);
            senderUserName = itemView.findViewById(R.id.inboxTextUser);
            itemRegarding = itemView.findViewById(R.id.inboxTextRegarding);
            messageDate = itemView.findViewById(R.id.inboxTextDate);
            readStatus = itemView.findViewById(R.id.inboxTextReadStatus);

            itemView.setOnClickListener(this);
        }

        //react to a click on a listitem
        @Override
        public void onClick(View view)
        {
            listener.onMessageClicked(getAdapterPosition());
        }
    }
}
