package mhj.Grp10_AppProject.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModel;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModelFactory;

public class CreateSaleActivity extends BaseActivity {
    private CreateSaleViewModel viewModel;

    CreateSaleActivity context;

    //widgets
    private TextView saleHeader;
    private EditText description;
    private ImageView imageView;


    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsale);
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

    //Code for the Camera inspired by https://developer.android.com/training/camera/photobasics
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

    //Get the thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

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

}

