package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mhj.Grp10_AppProject.Model.SalesItem;

public class CreateSaleViewModel extends ViewModel {

    private Context context;

    private static final String TAG = "CreateSaleViewModel";

    private LiveData<List<SalesItem>> salesitemLiveData;
    //private final Repository repository;

    public CreateSaleViewModel(Context context) {
        this.context = context;
        salesitemLiveData = new MutableLiveData<>();
        //repository = Repository.getInstance(context);
        //salesitemLiveData = repository.getItems();

    }

    public void AddItemToDatebase()
    {
    }

    public void addItem(int id){
        //repository.addItem(id);
    }


    // https://stackoverflow.com/a/2296416/1448765
    public String getCityName(double lat, double lng) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();
            String subLocality = address.getSubLocality();
            String locality = address.getLocality();

            String s = "";

            if (subLocality == null) {
                s = postalCode + " " + locality;
            } else {
                s = postalCode + " " + subLocality;
            }

            Log.d(TAG, "getCityName: " + s);
            return s;
        }
        else {
            return null;
        }
    }



}