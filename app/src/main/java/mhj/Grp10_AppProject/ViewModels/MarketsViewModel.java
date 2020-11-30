package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mhj.Grp10_AppProject.Model.Repository;
import mhj.Grp10_AppProject.Model.SalesItem;

public class MarketsViewModel extends ViewModel {

    private MutableLiveData<List<SalesItem>> salesitemLiveData;
    private final Repository repository;

    public MarketsViewModel(Context context)
    {
        salesitemLiveData = new MutableLiveData<>();
        repository = Repository.getInstance(context);
    }

    public LiveData<List<SalesItem>> getItems()
    {
        UpdateList();
        return salesitemLiveData;
    }

    private void UpdateList()
    {
        //Lyt efter om der tilføjes noget til listen, f.eks fra CreateSaleViewet
        //Hvis noget tilføjes (fra en anden bruger) skal dette opdateres i vores liste, hvilket gøres her.
        if(salesitemLiveData != null)
        {
           salesitemLiveData = repository.getItems();
        }
    }

    public void SetSelected(int index) {
        String d = salesitemLiveData.getValue().get(index).getPath();
        String k = salesitemLiveData.getValue().get(index).getDescription();
        repository.setSelectedItem(salesitemLiveData.getValue().get(index).getPath());
    }
}

