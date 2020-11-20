package mhj.Grp10_AppProject.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModel;
import mhj.Grp10_AppProject.ViewModels.CreateSaleViewModelFactory;

public class CreateSaleActivity extends AppCompatActivity {
    private CreateSaleViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsale);
        //Calling / creating ViewModel with the factory pattern is inspired from:
        // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this, new CreateSaleViewModelFactory(this.getApplicationContext()))
                .get(CreateSaleViewModel.class);
    }
}

