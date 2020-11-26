package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;



public class DetailsActivity extends BaseActivity {
    public static final String EXTRA_ITEM_ID = "extra_itemId";
    private static final String TAG = "DetailsActivity";
    private DetailsViewModel viewModel;
    private TextView textTitle, textPrice, textDescription, textLocation;
    private ImageView imgItem;
    private Button btnBack, btnMessage;
    private ImageButton btnMap;

    private FirebaseStorage mStorageRef;
    private LiveData<SalesItem> SelectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupUI();
        mStorageRef = FirebaseStorage.getInstance();

        viewModel = new ViewModelProvider(this, new DetailsViewModelFactory(this.getApplicationContext()))
                .get(DetailsViewModel.class);

        viewModel.returnSelected().observe(this, updateObserver );

    }

    Observer<SalesItem> updateObserver = new Observer<SalesItem>() {
        @Override
        public void onChanged(SalesItem Item) {
            if(Item != null)
            {
                textTitle.setText(Item.getTitle());
                textPrice.setText(String.valueOf(Item.getPrice()));
                textDescription.setText(Item.getDescription());
                //textLocation.getText(Item.getLocation().toString());

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
                }
        }
    };

    private void setupUI() {

        textTitle = findViewById(R.id.detailsTextTitle);
        imgItem = findViewById(R.id.detailsImage);
        textPrice = findViewById(R.id.detailsTextPrice);
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
        startActivity(intent);
    }

    private void gotoMap() {
        Toast.makeText(this, "map", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //Added for menu, if the user is logged in
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logoutTxt);
        MenuItem userItem = menu.findItem(R.id.userTxt);
        if(auth.getCurrentUser() != null)
        {
            logoutItem.setVisible(true);
            userItem.setVisible(true);
            userItem.setTitle("User: " + auth.getCurrentUser().getEmail());
        }
        return super.onPrepareOptionsMenu(menu);
    }
}