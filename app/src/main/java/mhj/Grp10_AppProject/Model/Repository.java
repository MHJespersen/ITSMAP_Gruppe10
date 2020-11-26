package mhj.Grp10_AppProject.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import mhj.Grp10_AppProject.Services.ForegroundService;
import mhj.Grp10_AppProject.WebAPI.APICallback;
import mhj.Grp10_AppProject.WebAPI.WebAPI;

public class Repository {

    private static Repository INSTANCE = null;
    private FirebaseFirestore firestore;
    private WebAPI api;

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
        firestore = FirebaseFirestore.getInstance();
        api = new WebAPI(context);
    }

    public void startMyForegroundService()
    {
        Intent foregroundService = new Intent(con, ForegroundService.class);
        con.startService(foregroundService);
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
        firestore.collection("PrivateMesssages").add(map);
    }

    public List<DocumentSnapshot> getPrivateMessages()
    {
        /*
       List<PrivateMessage> list = firestore.collection("PrivateMesssages").document().get();
        //asynchronously retrieve all documents
        //ApiFuture<QuerySnapshot> future = db.collection("PrivateMesssages").get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            PrivateMessage message = document.toObject(PrivateMessage.class);
        }
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println(document.getId() + " => " + document.toObject(City.class));
        }*/

        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = (ApiFuture<QuerySnapshot>)
                firestore.collection("PrivateMesssages").get();
        // query.get() blocks on response
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DocumentSnapshot> messageDocuments = querySnapshot.getDocuments();
        for (DocumentSnapshot messageDocument : messageDocuments) {

            PrivateMessage message = new PrivateMessage();
            if(messageDocument.exists()) {
                message.setItemId(Integer.parseInt(messageDocument.getId()));
                message.setMessageBody(messageDocument.getString("MessageBody"));
                message.setMessageDate(messageDocument.getString("MessageDate"));
            }
            List<PrivateMessage> list = (List<PrivateMessage>) message;
        }
       return messageDocuments;
    }

    public void getExchangeRates() {
        APICallback callback = new APICallback() {
            @Override
            public void OnApiCallback(ExchangeRates exchangeRates) {
                if (executor == null) {
                    executor = Executors.newSingleThreadExecutor();
                }

                executor.submit(new Runnable() {
                    @Override
                    public void run() {

                    }

                });

                //HVad der skal ske når APIet er færdigt
            }
        };
        api.loadData(callback);

    }

}
