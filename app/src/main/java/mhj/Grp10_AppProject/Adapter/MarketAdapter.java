package mhj.Grp10_AppProject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mhj.Grp10_AppProject.R;


// Inspired by demovideo from lesson 3
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ItemViewHolder> {

    public interface IItemClickedListener{
        void OnItemClicked(int index);
    }
    private IItemClickedListener listener;

    public MarketAdapter(IItemClickedListener listener)
    {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position)
    {
    }

    @Override
    public int getItemCount()
    {
        //Return size of list
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
        @Override
        public void onClick(View view)
        {
            listener.OnItemClicked(getAdapterPosition());
        }
    }
}

