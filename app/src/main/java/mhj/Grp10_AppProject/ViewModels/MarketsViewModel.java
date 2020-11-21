package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        //salesitemLiveData = repository.getItems();
    }

    public LiveData<List<SalesItem>> getItems()
    {
        if(salesitemLiveData == null)
        {
            UpdateList();
        }
        return salesitemLiveData;
    }

    private void UpdateList()
    {
        //Lyt efter om der tilføjes noget til listen, f.eks fra CreateSaleViewet
        //Hvis noget tilføjes (fra en anden bruger) skal dette opdateres i vores liste, hvilket gøres her.
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Items")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshopValue,
                                        @Nullable FirebaseFirestoreException error) {
                        ArrayList<SalesItem> updatedListOfItems = new ArrayList<>();
                        if(snapshopValue != null && !snapshopValue.isEmpty())
                        {
                            for (DocumentSnapshot item: snapshopValue.getDocuments()) {
                                SalesItem newItem = item.toObject(SalesItem.class);
                                updatedListOfItems.add(newItem);
                            }
                        }
                        salesitemLiveData.setValue(updatedListOfItems);
                    }
                });
    }
}

