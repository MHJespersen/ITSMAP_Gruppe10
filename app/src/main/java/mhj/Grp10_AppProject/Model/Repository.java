package mhj.Grp10_AppProject.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import mhj.Grp10_AppProject.Services.ForegroundService;
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
        firestore.collection("PrivateMessage").add(map);
    }
}
