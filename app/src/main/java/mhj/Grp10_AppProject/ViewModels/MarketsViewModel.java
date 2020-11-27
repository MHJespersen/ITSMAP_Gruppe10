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
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("SalesItems")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshopValue,
                                        @Nullable FirebaseFirestoreException error) {
                        ArrayList<SalesItem> updatedListOfItems = new ArrayList<>();
                        if(snapshopValue != null && !snapshopValue.isEmpty())
                        {
                            for (DocumentSnapshot item: snapshopValue.getDocuments()) {
                                SalesItem newItem = new SalesItem(
                                        item.get("title").toString(),
                                        item.get("description").toString(),
                                        Float.parseFloat(item.get("price").toString()),
                                        item.get("user").toString(),
                                        item.get("image").toString(),
                                        SalesItem.createLocationPoint(item.get("location", GeoPoint.class)),
                                        item.get("documentPath").toString()
                                );
                                updatedListOfItems.add(newItem);
                            }
                        }
                        salesitemLiveData.setValue(updatedListOfItems);
                    }
                });
    }

    public LiveData<List<SalesItem>> getSalesitemLiveData(){
        return salesitemLiveData;
    }

    public void selectAItem(int index){
        //int item = salesitemLiveData.getValue().get(index).getItemId();
        //Add call to setter in repository when possible
    }

    public void SetSelected(int index) {
        String d = salesitemLiveData.getValue().get(index).getPath();
        String k = salesitemLiveData.getValue().get(index).getDescription();
        repository.setSelectedItem(salesitemLiveData.getValue().get(index).getPath());
    }

}

