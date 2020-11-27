package mhj.Grp10_AppProject.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mhj.Grp10_AppProject.Services.ForegroundService;
import mhj.Grp10_AppProject.WebAPI.APICallback;
import mhj.Grp10_AppProject.WebAPI.WebAPI;

public class Repository {

    private static Repository INSTANCE = null;
    private final FirebaseFirestore firestore;
    private WebAPI api;
    private String SelectedItem;
    private MutableLiveData<SalesItem> SelectedItemLive;
    private MutableLiveData<PrivateMessage> SelectedMessageLive;

    private ExecutorService executor;
    private static Context con;

    public static Repository getInstance(Context context) {
        if (INSTANCE == null) {
            con = context;
            INSTANCE = new Repository(context);
            Log.d("Repo", "Created Instance: ");
        }
        Log.d("Repo", "Repo Already instantiated: ");
        return(INSTANCE);
    }

    private Repository(Context context)
    {
        SelectedItemLive = new MutableLiveData<SalesItem>();
        firestore = FirebaseFirestore.getInstance();
        executor = Executors.newSingleThreadExecutor();
        SelectedMessageLive = new MutableLiveData<PrivateMessage>();
    }

    public void startMyForegroundService()
    {
        Intent foregroundService = new Intent(con, ForegroundService.class);
        con.startService(foregroundService);
        SelectedItemLive = new MutableLiveData<SalesItem>();
        SelectedMessageLive = new MutableLiveData<PrivateMessage>();
    }

    public void setSelectedItem(String ItemID)
    {
        Task<DocumentSnapshot> d = firestore.collection("SalesItems").document(ItemID).get();
        d.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                SelectedItemLive.setValue(SalesItem.fromSnapshot(task.getResult()));
            }
        });
    }

    public LiveData<SalesItem> getSelectedItem() {
        return SelectedItemLive;
    }

    public LiveData<PrivateMessage> getSelectedMessage() {
        return SelectedMessageLive;
    }

    public Task<QuerySnapshot> getItems()
    {
        return firestore.collection("SalesItems").get();
    }

    public void sendMessage(String receiver, String sender, String timeStamp, String message)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("Receiver", receiver);
        map.put("Sender", sender);
        map.put("MessageDate", timeStamp);
        map.put("MessageBody", message);
        Task<DocumentReference> task = firestore.collection("PrivateMesssages").add(map);
        task.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
            String result = String.valueOf(task.getResult());
            Log.d("SendMessage", "added message: " + result);
            }
        });
        task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("SendMessageSuccess", "onSucces: "+ documentReference.toString());
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SendMessageException", "onFailure: "+ e);
            }
        });
    }


    public Task<QuerySnapshot> getPrivateMessages()
    {
        return firestore.collection("PrivateMesssages").get();
    }

    public void createSale(SalesItem item){
        Map<String, Object> map  = new HashMap<>();
        String newDocumentPath = firestore.collection("SalesItems").document().getId();
        map.put("description", item.getDescription());
        //map.put("documentPath", item.getDocumentPath());
        map.put("image", item.getImage());
        map.put("location", item.getLocation());
        map.put("price", item.getPrice());
        map.put("title", item.getTitle());
        map.put("user", item.getUser());
        map.put("documentPath", newDocumentPath.toString());
        firestore.collection("SalesItems").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.d("CreateSale", "Created Sale!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CreateSale", "Sale was not Created! Exception: " + e);
            }
        });
    }

}
