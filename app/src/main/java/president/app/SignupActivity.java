package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    EditText name, email, password, confirmPassword, contact;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ImageView passwordHide, passwordShow, confirmPasswordHide, confirmPasswordShow;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = openOrCreateDatabase("President.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        signup = findViewById(R.id.signup_signup);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);

        passwordHide = findViewById(R.id.signup_password_hide);
        passwordShow = findViewById(R.id.signup_password_show);

        confirmPassword = findViewById(R.id.signup_confirm_password);

        confirmPasswordHide = findViewById(R.id.signup_confirm_password_hide);
        confirmPasswordShow = findViewById(R.id.signup_confirm_password_show);

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

        confirmPasswordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword.setTransformationMethod(null);
                confirmPasswordHide.setVisibility(View.GONE);
                confirmPasswordShow.setVisibility(View.VISIBLE);
            }
        });

        confirmPasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                confirmPasswordHide.setVisibility(View.VISIBLE);
                confirmPasswordShow.setVisibility(View.GONE);
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Min. 6 Char Password Required");
                } else if (confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (confirmPassword.getText().toString().trim().length() < 6) {
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                } else if (!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())) {
                    confirmPassword.setError("Password Does Not Match");
                } else {
                    if(new ConnectionDetector(SignupActivity.this).networkConnected()){
                        new DoSignup().execute();
                    }
                    else{
                        new ConnectionDetector(SignupActivity.this).networkDisconnected();
                    }

                    /*String selectQuery = "SELECT * FROM USERS WHERE EMAIL='" + email.getText().toString() + "' OR CONTACT='" + contact.getText().toString() + "' ";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.getCount() > 0) {
                        new CommonMethod(SignupActivity.this, "Email Id/Contact No. Already Registered");
                    } else {
                        String insertQuery = "INSERT INTO USERS VALUES (NULL,'" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + password.getText().toString() + "')";
                        db.execSQL(insertQuery);
                        new CommonMethod(SignupActivity.this, "Signup Successfully");
                        onBackPressed();
                    }*/
                }
            }
        });
    }

    private class DoSignup extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignupActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",name.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"signup.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    new CommonMethod(SignupActivity.this,object.getString("message"));
                    onBackPressed();
                }
                else{
                    new CommonMethod(SignupActivity.this,object.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}