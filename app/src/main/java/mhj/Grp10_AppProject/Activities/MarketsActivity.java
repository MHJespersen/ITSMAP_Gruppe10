package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import mhj.Grp10_AppProject.Adapter.MarketAdapter;
import mhj.Grp10_AppProject.Database.MarketsDatabase;
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
    private MarketsDatabase db;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markets);
        context = this;


        itemList = findViewById(R.id.rcvItems);

        viewModel = new ViewModelProvider(context, new MarketsViewModelFactory(this.getApplicationContext())).get(MarketsViewModel.class);
    }
}