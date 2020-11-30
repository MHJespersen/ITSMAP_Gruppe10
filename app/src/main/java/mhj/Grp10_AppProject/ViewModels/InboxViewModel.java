package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import mhj.Grp10_AppProject.Model.PrivateMessage;
import mhj.Grp10_AppProject.Model.Repository;

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
