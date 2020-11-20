package mhj.Grp10_AppProject.Database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import mhj.Grp10_AppProject.Model.SalesItem;

//**** USE FIREBASE INSTEAD OF ROOM //

@Database(entities = {SalesItem.class}, version = 1)
public abstract class MarketsDatabase extends RoomDatabase {

    public abstract SalesItemDAO SalesItemDAO();
    private static MarketsDatabase instance;

    public static MarketsDatabase getDatabase(final Context context)
    {
        if(instance == null)
        {
            synchronized (MarketsDatabase.class)
            {
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MarketsDatabase.class, "MarkedsDataBase")
                            .build();
                }
            }
        }
        return instance;
    }
}
