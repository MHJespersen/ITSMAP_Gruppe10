package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.Utilities.LocationUtility;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModel;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModelFactory;

public class CreateSaleActivity extends BaseActivity {
    private static final String TAG = "CreateSaleActivity";

    //upload
    private FirebaseStorage firebaseStorage;

    public static CreateSaleActivity context;
    private CreateSaleViewModel viewModel;
    private SalesItem salesItem;
    private CreateSaleViewModel createSaleViewModel;
    private LocationUtility locationUtility;

    final String APP_TAG = "SmartSale";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final long MIN_TIME_BETWEEN_LOCATION_UPDATES = 5 * 1000;    // milisecs
    private static final float MIN_DISTANCE_MOVED_BETWEEN_LOCATION_UPDATES = 1;  // meters
    private static final int PERMISSIONS_REQUEST_LOCATION = 789;
    public static boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private Location lastLocation = null;
    private Boolean isTrackingLocation;

    //widgets
    private TextView saleHeader;
    private EditText title, price, description, location;
    private ImageView itemImage;
    private Button btnBack, btnCapture, btnGetLocation, btnCreate;

    public File photoFile;
    public String photoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsale);
        firebaseStorage = FirebaseStorage.getInstance();

        context = this;

        // Calling / creating ViewModel with the factory pattern is inspired from:
        // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this, new CreateSaleViewModelFactory(this.getApplicationContext()))
                .get(CreateSaleViewModel.class);
        photoFileName =createFileName();
        setupUI();

        locationUtility = new LocationUtility(this);

        startTrackingLocation();
        Log.d(TAG, "onCreate: started tracking");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isTrackingLocation){
            startTrackingLocation();
            Log.d(TAG, "onResume: started tracking");
        }

    }

    @Override
    protected void onPause() {
        stopTrackingLocation();
        Log.d(TAG, "onPause: stopped tracking");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (isTrackingLocation) {
            stopTrackingLocation();
            Log.d(TAG, "onDestroy: stopped tracking");
        }
        super.onDestroy();
    }

    private void setupUI() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        btnCreate = findViewById(R.id.btnPublish);
        btnCreate.setOnClickListener(view -> {
            //Save file:
            Uri file = Uri.fromFile(photoFile);
            StorageReference imgRef = firebaseStorage.getReference().child(photoFileName);
            imgRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Get a URL to the uploaded content
                    //Uri downloadUrl = taskSnapshot.getDownload();
                    Save();
                    Log.d("Successfull upload!", APP_TAG);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Unsuccesfull upload!", APP_TAG);
                }
            });
        });

        //Camera Code inspired by:
        //https://developer.android.com/training/camera/photobasics
        //https://www.tutlane.com/tutorial/android/android-camera-app-with-examples
        //https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media#using-capture-intent
        btnCapture = findViewById(R.id.btnTakePhoto);
        btnCapture.setOnClickListener(view -> {
            //Create intent to take picture and return control to the calling application
            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Create a File reference for future access
            createFileName();
            photoFile = getPhotoFileUri(photoFileName);
            
            //Wrap file object into a content provider, Required for API >= 24
            //See  https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            Uri fileProvider = FileProvider.getUriForFile(CreateSaleActivity.this, "mhj.fileprovider", photoFile);
            cInt.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if(cInt.resolveActivity(getPackageManager()) != null){
                startActivityForResult(cInt, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnGetLocation = findViewById(R.id.createSaleBtnGetLocation);
        btnGetLocation.setOnClickListener(view -> {
            checkPermissions();
            getDeviceLocation();
        });

        price = findViewById(R.id.createSaleTextPrice);
        title = findViewById(R.id.createSaleTextTitle);
        itemImage = findViewById(R.id.imgTaken);
        saleHeader = findViewById(R.id.txtCreateSaleHeader);
        description = findViewById(R.id.editTxtEnterDescription);
        location = findViewById(R.id.createSaleTextLocation);
    }

    //Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName){
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        //Create the storage dir if it doesn't exist
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        //Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    //Get the thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK){
                //Bitmap bp = (Bitmap) data.getExtras().get("data");
                Bitmap bp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                itemImage.setImageBitmap(bp);

            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String createFileName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + ".jpg";
        return imageFileName;
    }

    // Location permissions - should apparently not be in view model?
    // https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    // Also from L8 demo 2/3

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                        if (lastLocation != null) {
                            double lat = lastLocation.getLatitude();
                            double lon = lastLocation.getLongitude();
                            Log.d(TAG, "getDeviceLocation: " + lat + ", " + lon);
                            String s = locationUtility.getCityName(lat, lon);
                            location.setText(s);
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
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
                // something
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
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(CreateSaleActivity.context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
    }

    private void startTrackingLocation() {
        try {
            if (locationManager == null) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }

            long minTime = MIN_TIME_BETWEEN_LOCATION_UPDATES;
            float minDistance = MIN_DISTANCE_MOVED_BETWEEN_LOCATION_UPDATES;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            if (locationManager != null) {
                try {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);         //for specifying GPS provider
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, locationListener);     //for specifying Network provider
                    locationManager.requestLocationUpdates(minTime, minDistance, criteria, locationListener, null);
                    //Use criteria to chose best provider
                } catch (SecurityException ex) {
                    //TODO: user have disabled location permission - need to validate this permission for newer versions
                }
            }

            isTrackingLocation = true;
        } catch (Exception ex) {
            //things can go wrong
            Log.e("TRACKER", "Error starting location tracking", ex);
        }
    }

    private void stopTrackingLocation() {
        try {
            try {
                locationManager.removeUpdates(locationListener);
                isTrackingLocation = false;
            } catch (SecurityException ex) {
                //TODO: user have disabled location permission - need to validate this permission for newer versions
            }

        } catch (Exception ex) {
            //things can go wrong here as well (listener is null)
            Log.e("TRACKER", "Error stopping location tracking", ex);
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Log.d(TAG, "onLocationChanged: " + location.getLatitude());
//            userLocation = location;
//            updateStatus();
//            broadcastLocationUpdate(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void Save(){
        salesItem = new SalesItem();
        if(photoFileName != null){
            salesItem.setImage(photoFileName);
        }
        if(title.getText().toString() != null){
            salesItem.setTitle(title.getText().toString());
        }
        if(price.getText().toString() != null){
            salesItem.setPrice(Double.parseDouble(price.getText().toString()));
        }
        if(location.getText().toString() != null){
            if(lastLocation == null){
                Location loc = locationUtility.getLocationFromString(location.getText().toString());
                salesItem.setLocation(loc);
            }
            else{
                salesItem.setLocation(lastLocation);
            }
        }
        if(description.getText().toString() != null){
            salesItem.setDescription(description.getText().toString());
        }
        salesItem.setUser(auth.getCurrentUser().getEmail());
        viewModel.updateSalesItem(salesItem);
        finish();
    }
}