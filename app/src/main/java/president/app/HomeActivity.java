package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView email,password;
    //RadioButton male,female;
    RadioGroup gender;

    CheckBox androidCheck,java,php,flutter,react;
    Button showTechnology;

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

        gender = findViewById(R.id.home_gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                new CommonMethod(HomeActivity.this,radioButton.getText().toString());
            }
        });

        /*male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(HomeActivity.this,male.getText().toString());
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(HomeActivity.this,female.getText().toString());
            }
        });*/

        androidCheck = findViewById(R.id.home_android);
        java = findViewById(R.id.home_java);
        php = findViewById(R.id.home_php);
        flutter = findViewById(R.id.home_flutter);
        react = findViewById(R.id.home_react);

        showTechnology = findViewById(R.id.home_technology);

        showTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if(androidCheck.isChecked()){
                    sb.append(androidCheck.getText().toString()+"\n");
                }
                if(java.isChecked()){
                    sb.append(java.getText().toString()+"\n");
                }
                if(php.isChecked()){
                    sb.append(php.getText().toString()+"\n");
                }
                if(flutter.isChecked()){
                    sb.append(flutter.getText().toString()+"\n");
                }
                if(react.isChecked()){
                    sb.append(react.getText().toString());
                }
                new CommonMethod(HomeActivity.this,sb.toString());
            }
        });

        /*androidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new CommonMethod(HomeActivity.this,androidCheck.getText().toString());
                }
            }
        });

        java.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new CommonMethod(HomeActivity.this,java.getText().toString());
                }
            }
        });

        php.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new CommonMethod(HomeActivity.this,php.getText().toString());
                }
            }
        });

        flutter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new CommonMethod(HomeActivity.this,flutter.getText().toString());
                }
            }
        });

        react.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new CommonMethod(HomeActivity.this,react.getText().toString());
                }
            }
        });*/

    }
}