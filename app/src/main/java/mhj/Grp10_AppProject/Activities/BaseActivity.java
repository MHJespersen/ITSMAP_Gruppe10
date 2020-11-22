package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import mhj.Grp10_AppProject.R;

//Inspired from: https://stackoverflow.com/questions/17889240/reuse-the-action-bar-in-all-the-activities-of-app
public class BaseActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void LogOut(MenuItem item) {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "You have logged out", Toast.LENGTH_SHORT).show();
        }
    }
}