package mhj.Grp10_AppProject.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModel;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModelFactory;

public class CreateSaleActivity extends BaseActivity {
    private static final String TAG = "CreateSaleActivity";
    private CreateSaleViewModel viewModel;

    CreateSaleActivity context;

    //widgets
    private TextView saleHeader;
    private EditText description;
    private ImageView itemImage;
    private Button btnCapture, btnGetLocation;

    String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_createsale);
        btnCapture = findViewById(R.id.btnTakePhoto);
        btnGetLocation = findViewById(R.id.createSaleBtnGetLocation);
        itemImage = findViewById(R.id.imgTaken);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnGetLocation.setOnClickListener(view -> {
            checkPermissions();
            getDeviceLocation();
        });

        //Calling / creating ViewModel with the factory pattern is inspired from:
        // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this, new CreateSaleViewModelFactory(this.getApplicationContext()))
                .get(CreateSaleViewModel.class);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saleHeader = findViewById(R.id.txtCreateSaleHeader);
        description = findViewById(R.id.editTxtEnterDescription);
    }

    //Get the thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK){
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                itemImage.setImageBitmap(bp);
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    //Create a file name for the full images
    private File createImageFile() throws IOException{
        //Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName ="JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        //Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*
    //Code for the Camera inspired by https://developer.android.com/training/camera/photobasics
    //and https://www.tutlane.com/tutorial/android/android-camera-app-with-examples
    //Intent to capture photo
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there is a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){
                //Handle the Error occured while creating File
            }
            //Continue only if the File was created
            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
            }
        }
    }

 */
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


    // Location stuff
    // https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    private static final int PERMISSIONS_REQUEST_LOCATION = 789;

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation = null;

    private Boolean locationPermissionGranted = Boolean.FALSE;

    private void getDeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastLocation = task.getResult();
                            if (lastLocation != null) {
                                double lat = lastLocation.getLatitude();
                                double lon = lastLocation.getLongitude();
                                Log.d(TAG, "onComplete:" + lat + ", " + lon);
                                Toast.makeText(context, lat + ", " + lon, Toast.LENGTH_SHORT).show();
                                getCityName(lat, lon);

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    private void checkPermissions() {
        try {
            if (locationPermissionGranted) {

            } else {
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
//        checkPermissions();
    }

    private void getCityName(double lat, double lng) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            Log.d(TAG, "getCityName: " + addresses.get(0).getLocality());
        }
        else {
            // do your stuff
        }
    }

}

