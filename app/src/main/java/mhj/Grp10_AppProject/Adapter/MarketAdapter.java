package mhj.Grp10_AppProject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;


// Inspired by demovideo from lesson 3
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ItemViewHolder> {

    public interface IItemClickedListener{
        void OnItemClicked(int index);
    }
    private IItemClickedListener listener;

    private List<SalesItem> itemList;

    public MarketAdapter(IItemClickedListener listener)
    {
        this.listener = listener;
    }

    public void updateSalesItemList(List<SalesItem> lists){
        itemList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    //Default image inspired by:
    //https://stackoverflow.com/questions/33194477/display-default-image-in-imageview-if-no-image-returned-from-server
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position)
    {
        holder.name.setText(itemList.get(position).getItemId());
        holder.description.setText(itemList.get(position).getDescription());
        //holder.price.setText((int) itemList.get(position).getPrice());
        Glide.with(holder.img.getContext()).load(itemList.get(position).getImage()).placeholder(R.drawable.emptycart).into(holder.img);
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //viewholder ui widget references
        ImageView img;
        TextView name;
        TextView description;
        TextView price;

        //custom callback interface for user actions
        IItemClickedListener listener;

        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);

            //references from layout
            img = itemView.findViewById(R.id.imgItem);
            name = itemView.findViewById(R.id.txtItem);
            description = itemView.findViewById(R.id.txtDescription);
            price = itemView.findViewById(R.id.txtSalePrice);
        }

        //react to a click on a listitem
        @Override
        public void onClick(View view)
        {
            listener.OnItemClicked(getAdapterPosition());
        }

    }
}

