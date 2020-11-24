package mhj.Grp10_AppProject.ViewModels;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

<<<<<<< Updated upstream
import com.google.firebase.firestore.FirebaseFirestore;
=======
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mhj.Grp10_AppProject.Model.SalesItem;

import static android.app.Activity.RESULT_OK;
>>>>>>> Stashed changes

public class CreateSaleViewModel extends ViewModel {

    private Activity activity;

    private LiveData<List<SalesItem>> salesitemLiveData;
    //private final Repository repository;

    public CreateSaleViewModel(Context context) {
        salesitemLiveData = new MutableLiveData<>();
        //repository = Repository.getInstance(context);
        //salesitemLiveData = repository.getItems();

    }

<<<<<<< Updated upstream
    public void AddItemToDatebase()
    {
    }
=======
    public void addItem(int id){
        //repository.addItem(id);
    }

>>>>>>> Stashed changes
}