package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mhj.Grp10_AppProject.Model.Repository;
import mhj.Grp10_AppProject.Model.SalesItem;

public class MarketsViewModel extends ViewModel {

    private LiveData<List<SalesItem>> salesitemLiveData;
    //private final Repository repository;

    public MarketsViewModel(Context context)
    {
        salesitemLiveData = new MutableLiveData<>();
        //repository = Repository.getInstance(context);
        //salesitemLiveData = repository.getItems();
    }
}

