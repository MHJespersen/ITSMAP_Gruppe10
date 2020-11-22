package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;



public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "extra_itemId";
    private static final String TAG = "DetailsActivity";
    private DetailsViewModel viewModel;
    private TextView textItemTitle, textItemPrice, textItemDesc, textItemLocation;
    private ImageView imgItem;
    private Button btnBack, btnMessage;
    private ImageButton btnMap;
    
    private DummyItem dummyItem;
    public static ArrayList<DummyItem> dummyItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        viewModel = new ViewModelProvider(this, new DetailsViewModelFactory(this.getApplicationContext()))
                .get(DetailsViewModel.class);

        dummyItems.add(new DummyItem(0, 0, "Chair", "Great chair, please buy", "Aarhus", "sample_chair", 200));
        dummyItems.add(new DummyItem(1, 0, "Bed", "Great bed, please buy", "Aarhus", "sample_bed", 500));
        dummyItems.add(new DummyItem(2, 1, "Dress", "Great dress, please buy", "Copenhagen", "sample_dress", 500));

        dummyItem = dummyItems.get(2);
        setupUI();


    }

    private void setupUI() {
        textItemTitle = findViewById(R.id.detailsTextTitle);
        textItemTitle.setText(dummyItem.getTitle());

        imgItem = findViewById(R.id.detailsImage);
        String s = dummyItem.getImg();
        int id = getApplicationContext().getResources().getIdentifier(s, "drawable", getApplicationContext().getPackageName());
        imgItem.setImageResource(id);
        
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
        btnMessage.setOnClickListener(view -> gotoSendMessage());

        btnMap = findViewById(R.id.detailsBtnMap);
        btnMap.setOnClickListener(view -> gotoMap());
    }

    private void gotoSendMessage() {
        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, dummyItem.getItemId());
        startActivity(intent);
    }

    private void gotoMap() {
        Toast.makeText(this, "map", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

// Dummy Item to test with, delete later
class DummyItem {
    private int itemId, userId;
    private String title, description, location, img;
    private float price;

    public DummyItem() {
    }

    public DummyItem(int itemId, int userId, String title, String description, String location, String img, float price) {
        this.itemId = itemId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.img = img;
        this.price = price;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) {this.itemId = itemId;}
    public int getUserId() { return userId; }
    public void setUserId(int userId) {this.userId = userId;}
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
}