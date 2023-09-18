package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Log.d("NIHAR_HOME_EMAIL",MainActivity.email.getText().toString());
        Log.d("NIHAR_HOME_PASSWORD",MainActivity.password.getText().toString());*/

        Bundle bundle = getIntent().getExtras();
        Log.d("NIHAR_HOME_EMAIL",bundle.getString("PARTH"));
        Log.d("NIHAR_HOME_PASSWORD",bundle.getString("NIHAR"));

        email = findViewById(R.id.home_email);
        password = findViewById(R.id.home_password);

        email.setText(bundle.getString("PARTH"));
        password.setText(bundle.getString("NIHAR"));

    }
}