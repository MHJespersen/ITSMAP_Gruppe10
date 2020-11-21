package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;



public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private DetailsViewModel viewModel;
    private TextView textItemTitle, textItemPrice, textItemDesc, textItemLocation;
    private ImageView imgItem;
    private Button btnBack, btnMessage;
    
    private DummyItem dummyItem;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        viewModel = new ViewModelProvider(this, new DetailsViewModelFactory(this.getApplicationContext()))
                .get(DetailsViewModel.class);

        dummyItem = new DummyItem("Chair", "Great chair, please buy", "Aarhus", "some_img", 200, 0);
        setupUI();


    }

    private void setupUI() {
        textItemTitle = findViewById(R.id.detailsTextTitle);
        textItemTitle.setText(dummyItem.getTitle());
        
        textItemPrice = findViewById(R.id.detailsTextPrice);
        textItemPrice.setText(String.valueOf(dummyItem.getPrice()));
        
        textItemDesc = findViewById(R.id.detailsTextDesc);
        textItemDesc.setMovementMethod(new ScrollingMovementMethod());
        textItemDesc.setText(dummyItem.getDescription());
        
        textItemLocation = findViewById(R.id.detailsTextLocation);
        textItemLocation.setText(dummyItem.getLocation());

        imgItem= findViewById(R.id.detailsImage);

        btnBack = findViewById(R.id.detailsBtnBack);
        btnBack.setOnClickListener(view -> finish());

        btnMessage = findViewById(R.id.detailsBtnMessage);
        btnMessage.setOnClickListener(view -> gotoMessage());

        
    }

    private void gotoMessage() {
        Intent i = new Intent(this, MessageActivity.class);
        startActivity(i);
    }
}

// Dummy Item to test with
class DummyItem {
    private String title, description, location, img;
    private float price;
    private int userId;

    public DummyItem() {
    }

    public DummyItem(String title, String description, String location, String img, float price, int userId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.img = img;
        this.price = price;
        this.userId = userId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) {this.userId = userId;}
}