package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.Utilities.Constants;
import mhj.Grp10_AppProject.ViewModels.ViewMessageViewModel;
import mhj.Grp10_AppProject.ViewModels.ViewMessageViewModelFactory;

public class ViewMessageActivity extends AppCompatActivity {
    private TextView textSender, textRegarding, textMessage, textReply;
    private Button btnReply;
    private ViewMessageViewModel viewModel;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        viewModel = new ViewModelProvider(this, new ViewMessageViewModelFactory(this.getApplicationContext()))
                .get(ViewMessageViewModel.class);
        viewModel.returnSelected().observe(this, updateObserver );

        Intent intent = getIntent();
        String index = intent.getStringExtra(Constants.EXTRA_INDEX);

        setupUI();
    }

    private void setupUI() {
        textSender = findViewById(R.id.viewMessageTextFrom);
        textRegarding = findViewById(R.id.viewMessageTextRegarding);
        textMessage = findViewById(R.id.viewMessageTextMessage);
        btnReply = findViewById(R.id.viewMessageBtnReply);
        textReply = findViewById(R.id.viewMessageReply);
        btnReply.setOnClickListener(view -> reply());
    }

    private void reply() {
        auth = FirebaseAuth.getInstance();
        String sender = auth.getCurrentUser().getEmail();

        Intent intent = getIntent();
        String replyMessage = textReply.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault()).format(new Date());
        String user = intent.getStringExtra(Constants.DETAILS_USER);
        String title = intent.getStringExtra(Constants.DETAILS_TITLE);

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageDate(timeStamp);
        privateMessage.setSender(sender);
        privateMessage.setReceiver(user);
        privateMessage.setMessageBody(replyMessage);
        privateMessage.setRegarding(title);
        privateMessage.setMessageRead(false);

        //viewModel.reply(privateMessage);
        Toast.makeText(this, "Reply was sent: " + replyMessage, Toast.LENGTH_SHORT).show();
        finish();
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