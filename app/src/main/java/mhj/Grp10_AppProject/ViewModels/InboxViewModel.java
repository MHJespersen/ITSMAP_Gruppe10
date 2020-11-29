package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.Model.Repository;
import mhj.Grp10_AppProject.Model.SalesItem;
import mhj.Grp10_AppProject.WebAPI.FirebaseCallback;

public class InboxViewModel extends ViewModel {

    private LiveData<List<PrivateMessage>> privateMessagelist;
    private final Repository repository;

    public InboxViewModel(Context context) {
        repository = Repository.getInstance(context);
        privateMessagelist = new MutableLiveData<>();
    }

    public LiveData<List<PrivateMessage>> getMessages()
    {
        UpdateList();
        return privateMessagelist;
    }

    private void UpdateList()
    {
        privateMessagelist = repository.getPrivateMessages();
    }

//    public void SetSelectedMessage(int index) {
//        String d = privateMessagelist.getValue().get(index).getMessageDate();
//        String k = String.valueOf(privateMessagelist.getValue().get(index).getSender());
//        repository.setSelectedItem(String.valueOf(privateMessagelist.getValue().get(index).getSenderId()));
//    }
}
