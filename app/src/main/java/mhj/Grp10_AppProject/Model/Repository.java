package mhj.Grp10_AppProject.Model;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
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
    FirebaseAuth auth;
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
        auth = FirebaseAuth.getInstance();
    }

    public void startMyForegroundService()
    {
        Intent foregroundService = new Intent(con, ForegroundService.class);
        con.startService(foregroundService);
        SelectedItemLive = new MutableLiveData<SalesItem>();
        SelectedMessageLive = new MutableLiveData<PrivateMessage>();
    }

    public GeoPoint GeoCreater(Location l){
        GeoPoint geo = new GeoPoint(l.getLatitude(), l.getLongitude());
        return geo;
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

    public void sendMessage(PrivateMessage privateMessage)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("Receiver", privateMessage.getReceiver());
        map.put("Sender", privateMessage.getSender());
        map.put("MessageDate", privateMessage.getMessageDate());
        map.put("MessageBody", privateMessage.getMessageBody());
        map.put("Regarding", privateMessage.getRegarding());
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


    public void getPrivateMessages(String messageId)
    {
        Task<DocumentSnapshot> task = firestore.collection("PrivateMesssages").document(messageId).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().get("receiver") == auth.getCurrentUser().getEmail()) {
                    SelectedMessageLive.setValue(PrivateMessage.fromSnapshot(task.getResult()));
                }
            }

        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GetPrivateMessages", "onFailure: " + e);
            }
        });
    }

    public void createSale(SalesItem item){
        GeoPoint geo = GeoCreater(item.getLocation());
        Map<String, Object> map  = new HashMap<>();
        CollectionReference CollRef = firestore.collection("SalesItems");
        String UniqueID = CollRef.document().getId();
        map.put("description", item.getDescription());
        map.put("image", item.getImage());
        map.put("location", geo);
        map.put("price", item.getPrice());
        map.put("title", item.getTitle());
        map.put("user", item.getUser());
        map.put("documentPath", UniqueID);
        CollRef.document(UniqueID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Testing", "Completed");
            }
        });

    }
}