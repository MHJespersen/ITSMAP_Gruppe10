package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.LoginViewModel;
import mhj.Grp10_AppProject.ViewModels.LoginViewModelFactory;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    LoginActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        //Calling / creating ViewModel with the factory pattern is inspired from: https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this,
                new LoginViewModelFactory(this.getApplicationContext())).get(LoginViewModel.class);

    }
}