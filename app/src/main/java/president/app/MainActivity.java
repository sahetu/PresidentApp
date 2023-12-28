package president.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import president.app.databinding.ActivityMainBinding;
import president.app.databinding.ActivityNavDemoBinding;

public class MainActivity extends AppCompatActivity {

    Button login; //,signup
    public static EditText email,password;
    TextView forgotPassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ImageView passwordHide,passwordShow;
    SQLiteDatabase db;
    SharedPreferences sp;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("President.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        //signup = findViewById(R.id.main_signup);

        binding.mainSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, SignupActivity.class);
            }
        });

        login = findViewById(R.id.main_login);
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        //email.setText("admin@gmail.com");
        //password.setText("admin@123");

        forgotPassword = findViewById(R.id.main_forgot_password);
        forgotPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        passwordHide = findViewById(R.id.main_password_hide);
        passwordShow = findViewById(R.id.main_password_show);

        passwordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(null);
                passwordHide.setVisibility(View.GONE);
                passwordShow.setVisibility(View.VISIBLE);
            }
        });

        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(new PasswordTransformationMethod());
                passwordHide.setVisibility(View.VISIBLE);
                passwordShow.setVisibility(View.GONE);
            }
        });

        //Button
        //TextView
        //EditText
        //Layout
        //ImageView
        //View
        //RadioButton
        //Checkbox

        //Context Type For Activity
        //1. Context context
        //2. this
        //3. CurrentActivity.this
        //4. getApplicationContext

        //Context Type For Class File
        //1. Context

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else {
                    if(new ConnectionDetector(MainActivity.this).networkConnected()){
                        new DoLogin().execute();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).networkDisconnected();
                    }
                    /*String loginQuery = "SELECT * FROM USERS WHERE (EMAIL='"+email.getText().toString()+"' OR CONTACT='"+email.getText().toString()+"') AND PASSWORD='"+password.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(loginQuery,null);
                    if(cursor.getCount()>0) {
                        while (cursor.moveToNext()) {
                            System.out.println("Login Successfully");
                            Log.d("PARTH", "Login Successfully");
                            //Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                            new CommonMethod(MainActivity.this, "Login Successfully");
                            //Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_SHORT).show();
                            new CommonMethod(view, "Login Successfully");
                            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                            sp.edit().putString(ConstantSp.USERID,cursor.getString(0)).commit();
                            sp.edit().putString(ConstantSp.NAME,cursor.getString(1)).commit();
                            sp.edit().putString(ConstantSp.EMAIL,cursor.getString(2)).commit();
                            sp.edit().putString(ConstantSp.CONTACT,cursor.getString(3)).commit();
                            sp.edit().putString(ConstantSp.PASSWORD,cursor.getString(4)).commit();

                            *//*Bundle bundle = new Bundle();
                            bundle.putString("PARTH",cursor.getString(2));
                            bundle.putString("NIHAR",cursor.getString(4));
                            intent.putExtras(bundle);*//*
                            startActivity(intent);
                        }
                    }
                    else{
                        new CommonMethod(MainActivity.this,"Login Unsuccessfully");
                    }*/

                    /*if(email.getText().toString().trim().equals("admin@gmail.com") && password.getText().toString().trim().equalsIgnoreCase("Admin@123")){
                        System.out.println("Login Successfully");
                        Log.d("PARTH", "Login Successfully");
                        //Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                        new CommonMethod(MainActivity.this, "Login Successfully");
                        //Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_SHORT).show();
                        new CommonMethod(view, "Login Successfully");
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("PARTH",email.getText().toString());
                        bundle.putString("NIHAR",password.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //new CommonMethod(MainActivity.this,HomeActivity.class);
                    }
                    else{
                        new CommonMethod(MainActivity.this, "Login Unsuccessfully");
                        new CommonMethod(view, "Login Unsuccessfully");
                    }*/
                }
            }
        });

        setButtonData(true);

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this,"Login Successfully");
            }
        });*/

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonData(false);
                if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else{
                    if(password.getText().toString().trim().equals("")) {
                        setButtonData(false);
                    }
                    else{
                        setButtonData(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setButtonData(false);
                if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else{
                    if(email.getText().toString().trim().equals("")) {
                        setButtonData(false);
                    }
                    else{
                        setButtonData(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setButtonData(boolean b) {
        if(b){
            login.setVisibility(View.VISIBLE);
        }
        else{
            login.setVisibility(View.GONE);
        }
        //login.setEnabled(b);
    }

    private class DoLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",email.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"login.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    new CommonMethod(MainActivity.this,object.getString("message"));

                    JSONArray jsonArray = object.getJSONArray("UserData");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sp.edit().putString(ConstantSp.USERID,jsonObject.getString("userId")).commit();
                        sp.edit().putString(ConstantSp.NAME,jsonObject.getString("name")).commit();
                        sp.edit().putString(ConstantSp.EMAIL,jsonObject.getString("email")).commit();
                        sp.edit().putString(ConstantSp.CONTACT,jsonObject.getString("contact")).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,jsonObject.getString("password")).commit();
                        new CommonMethod(MainActivity.this, ProfileActivity.class);
                    }
                }
                else{
                    new CommonMethod(MainActivity.this,object.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}