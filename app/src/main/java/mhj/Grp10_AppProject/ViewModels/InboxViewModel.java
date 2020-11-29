package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;

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

public class InboxViewModel extends ViewModel {

    private MutableLiveData<List<PrivateMessage>> privateMessagelist;
    private final Repository repository;

    public InboxViewModel(Context context) {
        privateMessagelist = new MutableLiveData<>();
        repository = Repository.getInstance(context);
        //privateMessagelist = repository.getPrivateMessages();
    }

    public LiveData<List<PrivateMessage>> getMessages()
    {
        UpdateList();
        return privateMessagelist;
    }

    private void UpdateList()
    {
        //Lytter på om der tilføjes noget til listen
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("PrivateMesssages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshopValue,
                                        @Nullable FirebaseFirestoreException error) {
                        ArrayList<PrivateMessage> updatedListOfMessages = new ArrayList<>();
                        if(snapshopValue != null && !snapshopValue.isEmpty())
                        {
                            for (DocumentSnapshot item: snapshopValue.getDocuments()) {
                                PrivateMessage privateMessage = new PrivateMessage(
                                        item.get("Receiver").toString(),
                                        item.get("Sender").toString(),
                                        item.get("MessageBody").toString(),
                                        item.get("MessageDate").toString(),
                                        false
                                        );

                                updatedListOfMessages.add(privateMessage);
                            }
                        }
                        privateMessagelist.setValue(updatedListOfMessages);
                    }
                });
    }

//    public void SetSelectedMessage(int index) {
//        String d = privateMessagelist.getValue().get(index).getMessageDate();
//        String k = String.valueOf(privateMessagelist.getValue().get(index).getSender());
//        repository.setSelectedItem(String.valueOf(privateMessagelist.getValue().get(index).getSenderId()));
//    }
}
