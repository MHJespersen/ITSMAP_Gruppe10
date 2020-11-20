package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.LoginViewModel;
import mhj.Grp10_AppProject.ViewModels.LoginViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    FirebaseAuth auth;
    LoginActivity context;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        loginBtn = findViewById(R.id.SignInBtn);

        //Calling / creating ViewModel with the factory pattern is inspired from: https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this,
                new LoginViewModelFactory(this.getApplicationContext())).get(LoginViewModel.class);
        auth = FirebaseAuth.getInstance();
    }

    public void SignIn(View view) {
        if(auth == null)
        {
            auth = FirebaseAuth.getInstance();
        }

    }
}