package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mhj.Grp10_AppProject.Activities.CreateSaleActivity;
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


    // Location stuff
    // https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation = null;

    public Location getDeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (CreateSaleActivity.locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(CreateSaleActivity.context, task -> {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                        if (lastLocation != null) {
                            double lat = lastLocation.getLatitude();
                            double lon = lastLocation.getLongitude();
                            Log.d(TAG, "getDeviceLocation: " + lat + ", " + lon);
                            Toast.makeText(context, lat + ", " + lon, Toast.LENGTH_SHORT).show();
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

        return lastLocation;
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
            // Only works with DK addresses for now, because US fx does not have sublocality
            // TODO: check if sublocality is null, if it is, get locality instead
            String s = address.getPostalCode() + " " + address.getSubLocality() + ", " + address.getCountryName();
            Log.d(TAG, "getCityName: " + s);
            return s;
        }
        else {
            return null;
        }
    }


}