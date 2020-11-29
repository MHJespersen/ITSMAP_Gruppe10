package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.SendMessageViewModel;
import mhj.Grp10_AppProject.ViewModels.SendMessageViewModelFactory;

public class SendMessageActivity extends BaseActivity {
    private static final String TAG = "SendMessageActivity";
    private SendMessageViewModel viewModel;
    private TextView textRecipient, textItem;
    private EditText inputMessage;
    private Button btnCancel, btnSend;
    private int userId;
    private String itemTitle, itemImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        viewModel = new ViewModelProvider(this, new SendMessageViewModelFactory(this.getApplicationContext()))
                .get(SendMessageViewModel.class);

        int itemId = getIntent().getIntExtra(DetailsActivity.EXTRA_ITEM_ID, 42);
        //userId = DetailsActivity.dummyItems.get(itemId).getUserId();
        //itemTitle = DetailsActivity.dummyItems.get(itemId).getTitle();

        setupUI();
    }

    private void setupUI() {
        textRecipient = findViewById(R.id.sendMessageTextRecipient);
        Intent intent = getIntent();
        String user = intent.getStringExtra("User");
        textRecipient.setText(String.valueOf(user));

        textItem = findViewById(R.id.sendMessageTextItem);
        textItem.setText(itemTitle);

        inputMessage = findViewById(R.id.sendMessageInputMessage);
        inputMessage.requestFocus();

        btnCancel = findViewById(R.id.sendMessageBtnCancel);
        btnCancel.setOnClickListener(view -> finish());

        btnSend = findViewById(R.id.sendMessageBtnSend);
        btnSend.setOnClickListener(view -> sendMessage1());
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString();
        Toast.makeText(this, "Message sent: " + message, Toast.LENGTH_SHORT).show();
        finish();
    }

    //Real section
    //guide: https://www.youtube.com/watch?v=f1HKTg2hyf0&ab_channel=KODDev
    private void sendMessage1() {
        auth = FirebaseAuth.getInstance();
        //input to message
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date());
        String message = inputMessage.getText().toString();
        String sender = auth.getCurrentUser().getEmail();
        //saving to database ->
        //viewModel.sendMessage(receiver, sender, timeStamp, message);

        //Toast.makeText(this, "Message sent: " + message, Toast.LENGTH_SHORT).show();
        //finish();

        //With Message Object.
        Intent intent = getIntent();
        String user = intent.getStringExtra("User");
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setMessageDate(timeStamp);
        privateMessage.setSender(sender);
        privateMessage.setReceiver(user);
        privateMessage.setMessageBody(message);

        // with message object to viewmodel.
        viewModel.sendMessage(privateMessage);
    }

}