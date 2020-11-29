package mhj.Grp10_AppProject.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
//import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mhj.Grp10_AppProject.Services.ForegroundService;
import mhj.Grp10_AppProject.WebAPI.WebAPI;

public class Repository {

    private static Repository INSTANCE = null;
    private final FirebaseFirestore firestore;
    private WebAPI api;
    FirebaseAuth auth;
    private String SelectedItem;
    private MutableLiveData<SalesItem> SelectedItemLive;
    private MutableLiveData<List<PrivateMessage>> SelectedMessageLive;


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
        SelectedMessageLive = new MutableLiveData<>();
        SelectedItemLive = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        executor = Executors.newSingleThreadExecutor();
        auth = FirebaseAuth.getInstance();
        initializePrivateMessages();
    }

    public void startMyForegroundService()
    {
        Intent foregroundService = new Intent(con, ForegroundService.class);
        con.startService(foregroundService);
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

    public MutableLiveData<List<PrivateMessage>> getSelectedMessage() {
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
        firestore.collection("PrivateMessages").document(privateMessage.getReceiver())
                .collection("Messages").add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d("PrivateMessages", "Completed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PrivateMessages", "Failed");
                    }
                });
    }

    public MutableLiveData<List<PrivateMessage>> getPrivateMessages(){
        return this.SelectedMessageLive;
    }

    private void initializePrivateMessages()
    {
        ArrayList privateMessages = new ArrayList();
        firestore.collection(
                "PrivateMessages").document(auth.getCurrentUser().getEmail()).
                collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot snap : value) {
                    privateMessages.add(PrivateMessage.fromSnapshot(snap));
                }
                if(!privateMessages.isEmpty())
                {
                    SelectedMessageLive.setValue(privateMessages);
                }
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
        })
        .addOnFailureListener(new OnFailureListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(con,"Creating sale Failed", Toast.LENGTH_SHORT);
            }
        });

    }
}