package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.List;

import mhj.Grp10_AppProject.Adapter.MarketAdapter;
import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.MarketsViewModel;
import mhj.Grp10_AppProject.ViewModels.MarketsViewModelFactory;

public class MarketsActivity extends AppCompatActivity {

    MarketsActivity context;
    MarketsViewModel viewModel;

    //widgets
    private RecyclerView itemList;

    private MarketAdapter adapter;

    //state
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markets);
        context = this;

        itemList = findViewById(R.id.rcvItems);

        viewModel = new ViewModelProvider(context, new MarketsViewModelFactory(this.getApplicationContext())).get(MarketsViewModel.class);

        viewModel.getItems().observe(this, updateObserver);
    }


    //Tilføjet for at kunne starte på firebase - Hvis noget tilføjes til listen bliver det opdaget her
    Observer<List<SalesItem>> updateObserver = new Observer<List<SalesItem>>() {
        @Override
        public void onChanged(List<SalesItem> UpdatedItems) {
            adapter = new MarketAdapter((MarketAdapter.IItemClickedListener) context);
            itemList = findViewById(R.id.country_list);
            itemList.setLayoutManager(new LinearLayoutManager(context));
            itemList.setAdapter(adapter);
            adapter.updateSalesItemList(UpdatedItems);
        }
    };

}