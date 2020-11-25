package mhj.Grp10_AppProject.ViewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mhj.Grp10_AppProject.Model.SalesItem;

public class CreateSaleViewModel extends ViewModel {

    private Activity activity;

    private LiveData<List<SalesItem>> salesitemLiveData;
    //private final Repository repository;

    public CreateSaleViewModel(Context context) {
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


}