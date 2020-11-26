package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mhj.Grp10_AppProject.Adapter.MarketAdapter;
import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.MarketsViewModel;
import mhj.Grp10_AppProject.ViewModels.MarketsViewModelFactory;


public class MarketsActivity extends BaseActivity implements MarketAdapter.IItemClickedListener {

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
    //Inspired from https://medium.com/@atifmukhtar/recycler-view-with-mvvm-livedata-a1fd062d2280
    Observer<List<SalesItem>> updateObserver = new Observer<List<SalesItem>>() {
        @Override
        public void onChanged(List<SalesItem> UpdatedItems) {
            adapter = new MarketAdapter(context);
            itemList = findViewById(R.id.rcvItems);
            itemList.setLayoutManager(new LinearLayoutManager(context));
            itemList.setAdapter(adapter);
            adapter.updateSalesItemList(UpdatedItems);
        }
    };


    //Added for menu, if the user is logged in
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logoutTxt);
        MenuItem userItem = menu.findItem(R.id.userTxt);
        if(auth.getCurrentUser() != null)
        {
            logoutItem.setVisible(true);
            userItem.setVisible(true);
            userItem.setTitle("User: " + auth.getCurrentUser().getEmail());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void OnItemClicked(int index) {
        viewModel.SetSelected(index);
        startActivity(new Intent(this, DetailsActivity.class));
    }
}