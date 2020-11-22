package mhj.Grp10_AppProject.Activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.LoginViewModel;
import mhj.Grp10_AppProject.ViewModels.LoginViewModelFactory;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private LoginViewModel viewModel;
    FirebaseAuth auth;
    LoginActivity context;
    private Button loginBtn;
    public static final int REQUEST_LOGIN = 1337;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        loginBtn = findViewById(R.id.SignInBtn);
        userName = findViewById(R.id.UserNameTxt);

        //Calling / creating ViewModel with the factory pattern is inspired from: https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this,
                new LoginViewModelFactory(this.getApplicationContext())).get(LoginViewModel.class);
        auth = FirebaseAuth.getInstance();

    }

    public void SignIn(View view) {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        if (auth.getCurrentUser() != null) {
            //Bruger er logget ind
            updateUserName();
            //Åben ListActivity eller hvad den første activity er
        }
        else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.GitHubBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build()
            );
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(), REQUEST_LOGIN
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(context,  auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "You have logged in", Toast.LENGTH_SHORT).show();
                updateUserName();
            }
        }
    }
    public void SignOut(View view) {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(context, "You have logged out", Toast.LENGTH_SHORT).show();
            updateUserName();
        }
    }

    private void updateUserName()
    {
        if (auth.getCurrentUser() != null) {
            userName.setText(auth.getCurrentUser().getEmail());
        }
        else
        {
            userName.setText("");
        }
    }
}