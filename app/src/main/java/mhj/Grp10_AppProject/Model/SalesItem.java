package mhj.Grp10_AppProject.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SalesItem {

    @PrimaryKey
    @NonNull
    String pm = "NONE";

    SalesItem()
    {
    }
}
