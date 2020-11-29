package mhj.Grp10_AppProject.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;
import mhj.Grp10_AppProject.ViewModels.ViewMessageViewModel;
import mhj.Grp10_AppProject.ViewModels.ViewMessageViewModelFactory;

public class ViewMessageActivity extends AppCompatActivity {
    private TextView textSender, textRegarding, textMessage;
    private Button btnBack, btnReply;
    private ViewMessageViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        viewModel = new ViewModelProvider(this, new ViewMessageViewModelFactory(this.getApplicationContext()))
                .get(ViewMessageViewModel.class);
        viewModel.returnSelected().observe(this, updateObserver );

        setupUI();
    }

    private void setupUI() {
        textSender = findViewById(R.id.viewMessageTextFrom);
        textRegarding = findViewById(R.id.viewMessageTextRegarding);
        textMessage = findViewById(R.id.viewMessageTextMessage);
        btnBack = findViewById(R.id.viewMessageBtnBack);
        btnBack.setOnClickListener(view -> finish());
        btnReply = findViewById(R.id.viewMessageBtnReply);
        btnReply.setOnClickListener(view -> reply());
    }

    private void reply() {
        Toast.makeText(this, "Reply functionality not implemented", Toast.LENGTH_SHORT).show();
    }

    Observer<PrivateMessage> updateObserver = new Observer<PrivateMessage>() {
        @Override
        public void onChanged(PrivateMessage message) {
            if(message != null)
            {
                textMessage.setText(message.getMessageBody());
                textRegarding.setText(message.getRegarding());
                textSender.setText(message.getSender());
            }
            Log.d("PrivateMessage", "ViewMessage:failed. Message = null ");
        }
    };

}