package mhj.Grp10_AppProject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {
    public interface IMessageClickedListener {
        void onMessageClicked(int index);
    }

    private IMessageClickedListener listener;
    private List<PrivateMessage> messageList;

    public InboxAdapter(IMessageClickedListener listener) {
        this.listener = listener;
    }

    public void updateMessageList(List<PrivateMessage> lists){
        messageList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_list_item, parent, false);
        InboxViewHolder vh = new InboxViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position)
    {
        holder.senderUserName.setText(String.valueOf(messageList.get(position).getSenderId()));
        holder.itemRegarding.setText(String.valueOf(messageList.get(position).getItemId()));
        holder.messageDate.setText(messageList.get(position).getMessageDate());
//        Glide.with(holder.senderUserImg.getContext()).load(itemList.get(position).getImage()).placeholder(R.drawable.emptycart).into(holder.img);
    }

    @Override
    public int getItemCount()
    {
        //Return size of list
        return messageList.size();
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //viewholder ui widget references
        ImageView senderUserImg;
        TextView senderUserName, itemRegarding, messageDate, readStatus;

        //custom callback interface for user actions
        IMessageClickedListener listener;

        public InboxViewHolder(@NonNull View itemView, IMessageClickedListener messageClickedListener)
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