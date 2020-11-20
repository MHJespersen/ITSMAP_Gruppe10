package mhj.Grp10_AppProject.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import mhj.Grp10_AppProject.Services.ForegroundService;
import mhj.Grp10_AppProject.WebAPI.WebAPI;

public class Repository {

    private static Repository INSTANCE = null;
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
    }

    public void startMyForegroundService()
    {
        Intent foregroundService = new Intent(con, ForegroundService.class);
        con.startService(foregroundService);
    }
}
