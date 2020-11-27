package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mhj.Grp10_AppProject.Adapter.InboxAdapter;
import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.InboxViewModel;
import mhj.Grp10_AppProject.ViewModels.InboxViewModelFactory;


public class InboxActivity extends BaseActivity implements InboxAdapter.IMessageClickedListener {

    private static final String TAG = "InboxActivity";
    private static final String EXTRA_INDEX = "EXTRA_INDEX";

    InboxActivity context;
    private InboxViewModel viewModel;
    private RecyclerView recyclerView;
    private InboxAdapter adapter;
    private ArrayList<PrivateMessage> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        context = this;

        viewModel = new ViewModelProvider(context, new InboxViewModelFactory(this.getApplicationContext()))
                .get(InboxViewModel.class);

        adapter = new InboxAdapter((InboxAdapter.IMessageClickedListener) context);
        recyclerView = findViewById(R.id.inboxMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        messageList.add(new PrivateMessage(0, 0, 0, 0, "Message", "26/04/2020", true));

        //adapter.updateMessageList(messageList);
        viewModel.getMessages().observe(this, updateObserver);
    }

    Observer<List<PrivateMessage>> updateObserver = new Observer<List<PrivateMessage>>() {
        @Override
        public void onChanged(List<PrivateMessage> UpdatedItems) {

            adapter.updateMessageList(messageList);
        }
    };

    @Override
    public void onMessageClicked(int index) {
        Intent intent = new Intent(this, ViewMessageActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        startActivity(intent);

    }

}