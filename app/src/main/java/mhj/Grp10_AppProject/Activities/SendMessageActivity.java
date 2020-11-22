package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        userId = DetailsActivity.dummyItems.get(itemId).getUserId();
        itemTitle = DetailsActivity.dummyItems.get(itemId).getTitle();

        setupUI();
    }

    private void setupUI() {
        textRecipient = findViewById(R.id.sendMessageTextRecipient);
        textRecipient.setText(String.valueOf(userId));

        textItem = findViewById(R.id.sendMessageTextItem);
        textItem.setText(itemTitle);

        inputMessage = findViewById(R.id.sendMessageInputMessage);
        inputMessage.requestFocus();

        btnCancel = findViewById(R.id.sendMessageBtnCancel);
        btnCancel.setOnClickListener(view -> finish());

        btnSend = findViewById(R.id.sendMessageBtnSend);
        btnSend.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString();
        Toast.makeText(this, "Message sent: " + message, Toast.LENGTH_SHORT).show();
        finish();
    }


}