package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.Utilities.LocationUtility;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;
import mhj.Grp10_AppProject.WebAPI.APICallback;
import mhj.Grp10_AppProject.WebAPI.WebAPI;


public class DetailsActivity extends BaseActivity {
    private static final String TAG = "DetailsActivity";
    public static final String EXTRA_ITEM_ID = "extra_itemId";
    public static final String EXTRA_COORDS = "extra_coords";
    private DetailsViewModel viewModel;

    // widgets
    private TextView textTitle, textPrice, textPriceEur, textDescription, textLocation;
    private ImageView imgItem;
    private Button btnBack, btnMessage;
    private ImageButton btnMap;

    private ExecutorService executor;
    private WebAPI webAPI;
    private LocationUtility locationUtility;
    private FirebaseStorage mStorageRef;
    private LiveData<SalesItem> SelectedItem;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupUI();
        mStorageRef = FirebaseStorage.getInstance();

        viewModel = new ViewModelProvider(this, new DetailsViewModelFactory(this.getApplicationContext()))
                .get(DetailsViewModel.class);

        webAPI = new WebAPI(this);
        locationUtility = new LocationUtility(this);
        
        viewModel.returnSelected().observe(this, updateObserver );
        

    }

    Observer<SalesItem> updateObserver = new Observer<SalesItem>() {
        @Override
        public void onChanged(SalesItem Item) {
            if(Item != null)
            {
                textTitle.setText(Item.getTitle());

                double price = Item.getPrice();
                // Check price for decimals, if zero, don't show
                String sPrice = null;
                if(price % 1 == 0) {
                    sPrice = String.format(java.util.Locale.getDefault(),"%.0f kr", price);
                } else {
                    sPrice = String.format(java.util.Locale.getDefault(),"%.2f kr", price);
                }
                textPrice.setText(sPrice);

                textDescription.setText(Item.getDescription());

                location = Item.getLocation();
                String sLocation = locationUtility.getCityName(location.getLatitude(), location.getLongitude());
                textLocation.setText(sLocation);

                if(Item.getImage() != "")
                {
                    StorageReference strRef = mStorageRef.getReference().child(Item.getImage());
                    strRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            Glide.with(imgItem).load(imageURL).into(imgItem);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Glide.with(imgItem).load(R.drawable.emptycart).into(imgItem);
                        }
                    });
                }
                else
                {
                    Glide.with(imgItem).load(R.drawable.emptycart).into(imgItem);
                }

                getExchangeRates(price);
            }



        }
    };

    private void setupUI() {

        textTitle = findViewById(R.id.detailsTextTitle);
        imgItem = findViewById(R.id.detailsImage);
        textPrice = findViewById(R.id.detailsTextPrice);
        textPriceEur = findViewById(R.id.detailsTextEur);
        textDescription = findViewById(R.id.detailsTextDesc);
        textLocation = findViewById(R.id.detailsTextLocation);
        imgItem= findViewById(R.id.detailsImage);
        btnBack = findViewById(R.id.detailsBtnBack);
        btnMessage = findViewById(R.id.detailsBtnMessage);
        btnMap = findViewById(R.id.detailsBtnMap);

        textDescription.setMovementMethod(new ScrollingMovementMethod());

        btnBack.setOnClickListener(view -> finish());
        btnMessage.setOnClickListener(view -> gotoSendMessage());
        btnMap.setOnClickListener(view -> gotoMap());
    }

    private void gotoSendMessage() {
        Intent intent = new Intent(this, SendMessageActivity.class);
        //intent.putExtra(EXTRA_ITEM_ID, dummyItem.getItemId());
        //Title of salesItem is used to set regarding field of message example:(Regarding: Chair)
        intent.putExtra("Title", SelectedItem.getValue().getTitle());
        intent.putExtra("User", SelectedItem.getValue().getUser());
        startActivity(intent);
    }

    private void gotoMap() {
        
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(EXTRA_COORDS, new double[]{lat, lng});

        startActivity(intent);
    }

    public void getExchangeRates(double price) {

        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        executor.submit(() -> {
            APICallback callback = exchangeRates -> {
                //What happens on API call completion
                Log.d(TAG, "OnApiCallback: EUR" + exchangeRates.getRates().getEUR());

                double eur = exchangeRates.getRates().getEUR();
                double eurPrice = price*eur;

                String sPrice = String.format(java.util.Locale.getDefault(),"%.2f \u20ac", eurPrice);

                textPriceEur.setText(sPrice);


            };
            webAPI.loadData(callback);
        });
    }

}