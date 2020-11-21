package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.MessageViewModel;
import mhj.Grp10_AppProject.ViewModels.MessageViewModelFactory;

public class MessageActivity extends AppCompatActivity {
    private MessageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        viewModel = new ViewModelProvider(this, new MessageViewModelFactory(this.getApplicationContext()))
                .get(MessageViewModel.class);
    }
}