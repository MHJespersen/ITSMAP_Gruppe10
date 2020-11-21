package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.MessageViewModel;
import mhj.Grp10_AppProject.ViewModels.MessageViewModelFactory;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private MessageViewModel viewModel;
    private TextView textRecipient, textItem;
    private EditText inputMessage;
    private Button btnCancel, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        viewModel = new ViewModelProvider(this, new MessageViewModelFactory(this.getApplicationContext()))
                .get(MessageViewModel.class);

        int itemId = getIntent().getIntExtra(DetailsActivity.EXTRA_ITEM_ID, 42);
        Log.d(TAG, "onCreate: " + itemId);

        setupUI();
    }

    private void setupUI() {
        textRecipient = findViewById(R.id.messageTextRecipient);
        textRecipient.setText("User");

        textItem = findViewById(R.id.messageTextItem);
        textItem.setText("Fancy Couch");

        inputMessage = findViewById(R.id.messageInputMessage);
        inputMessage.requestFocus();

        btnCancel = findViewById(R.id.messageBtnCancel);
        btnCancel.setOnClickListener(view -> finish());

        btnSend = findViewById(R.id.messageBtnSend);
        btnSend.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString();
        Toast.makeText(this, "Message sent: " + message, Toast.LENGTH_SHORT).show();
        finish();
    }


}