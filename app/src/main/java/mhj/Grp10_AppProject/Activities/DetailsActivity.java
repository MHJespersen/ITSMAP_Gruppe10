package mhj.Grp10_AppProject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mhj.Grp10_AppProject.R;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModel;
import mhj.Grp10_AppProject.ViewModels.DetailsViewModelFactory;

public class DetailsActivity extends AppCompatActivity {
    private DetailsViewModel viewModel;
    private TextView textItemPrice, textItemDesc;
    private ImageView imgItem;
    private Button btnBack, btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Calling / creating ViewModel with the factory pattern is inspired from:
        // https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
        viewModel = new ViewModelProvider(this, new DetailsViewModelFactory(this.getApplicationContext()))
                .get(DetailsViewModel.class);

        textItemPrice = findViewById(R.id.detailsTextPrice);
        textItemDesc = findViewById(R.id.detailsTextDesc);
        imgItem= findViewById(R.id.detailsImage);
        btnBack = findViewById(R.id.detailsBtnBack);
        btnMessage = findViewById(R.id.detailsBtnMessage);

        textItemDesc.setMovementMethod(new ScrollingMovementMethod());
    }
}