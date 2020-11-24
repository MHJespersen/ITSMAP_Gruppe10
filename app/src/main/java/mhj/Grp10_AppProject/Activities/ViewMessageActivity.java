package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import mhj.Grp10_AppProject.R;

public class ViewMessageActivity extends AppCompatActivity {
    private TextView textSender, textRegarding, textMessage;
    private Button btnBack, btnReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        
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
    }
}