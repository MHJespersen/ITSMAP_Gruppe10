package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import mhj.Grp10_AppProject.Adapter.InboxAdapter;
import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.InboxViewModel;
import mhj.Grp10_AppProject.ViewModels.InboxViewModelFactory;


public class InboxActivity extends AppCompatActivity implements InboxAdapter.IMessageClickedListener {

    InboxActivity context;
    private InboxViewModel viewModel;
    private RecyclerView recyclerView;
    private InboxAdapter adapter;
    private ArrayList<PrivateMessage> messageList;

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

        messageList = new ArrayList<>();
        messageList.add(new PrivateMessage(0, 0, 0, 0, "Message", "26/04/2020", Boolean.TRUE));

        adapter.updateMessageList(messageList);

    }

    @Override
    public void onMessageClicked(int index) {
        Toast.makeText(context, "Message clicked", Toast.LENGTH_SHORT).show();
    }
}