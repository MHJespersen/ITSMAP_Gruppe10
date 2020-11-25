package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModel;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModelFactory;

public class CreateSaleActivity extends BaseActivity {
    private static final String TAG = "CreateSaleActivity";

    public static CreateSaleActivity context;
    private CreateSaleViewModel viewModel;

    private final int PERMISSIONS_REQUEST_LOCATION = 789;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static boolean locationPermissionGranted = Boolean.FALSE;

    //widgets
    private TextView saleHeader;
    private EditText description, location;
    private ImageView itemImage;
    private Button btnBack, btnCapture, btnGetLocation;

    String currentPhotoPath;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsale);

        context = this;

        // Calling / creating ViewModel with the factory pattern is inspired from:
        // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this, new CreateSaleViewModelFactory(this.getApplicationContext()))
                .get(CreateSaleViewModel.class);

        setupUI();

    }

    private void setupUI() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        btnCapture = findViewById(R.id.btnTakePhoto);
        btnCapture.setOnClickListener(view -> {
            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cInt, REQUEST_IMAGE_CAPTURE);
        });

        btnGetLocation = findViewById(R.id.createSaleBtnGetLocation);
        btnGetLocation.setOnClickListener(view -> {
            checkPermissions();
            recursiveGetLocation();
//            currentLocation = viewModel.getDeviceLocation();
            Log.d(TAG, "setupUI: " + currentLocation);

//            recursiveShit();

//            String s = viewModel.getCityName(currentLocation.getLatitude(), currentLocation.getLongitude());
//            location.setText(s);
        });


        itemImage = findViewById(R.id.imgTaken);
        saleHeader = findViewById(R.id.txtCreateSaleHeader);
        description = findViewById(R.id.editTxtEnterDescription);
        location = findViewById(R.id.createSaleTextLocation);

    }

    // Location is retrieved via a Task, which is async. Therefore location is null if we don't wait for it to finish.
    // Not pretty, but here we run the function repeatedly until we have a result -> async to not freeze UI
    // TODO: Consider just waiting a few ms before call instead of calling several times
    private void recursiveGetLocation() {
        ExecutorService execService = Executors.newSingleThreadExecutor();
        execService.submit(() -> {
            currentLocation = viewModel.getDeviceLocation();
            if (currentLocation != null) {
                String s = viewModel.getCityName(currentLocation.getLatitude(), currentLocation.getLongitude());
                location.setText(s);
            } else {
                recursiveGetLocation();
            }
        });
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

    // Location permissions - should they be in view model?
    // https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
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
    }

}

