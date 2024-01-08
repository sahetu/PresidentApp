package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    Button editProfile,submit,logout,delete;
    EditText name, email, password, confirmPassword, contact;
    LinearLayout confirmPasswordLayout;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ImageView passwordHide, passwordShow, confirmPasswordHide, confirmPasswordShow;

    SQLiteDatabase db;

    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("President.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        editProfile = findViewById(R.id.profile_edit);
        submit = findViewById(R.id.profile_submit);

        delete = findViewById(R.id.profile_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String deleteQuery = "DELETE FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                db.execSQL(deleteQuery);
                new CommonMethod(ProfileActivity.this,"Account Deleted Successfully");
                sp.edit().clear().commit();
                new CommonMethod(ProfileActivity.this,MainActivity.class);
                finish();*/
                if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                    //new DoDelete().execute();
                    pd = new ProgressDialog(ProfileActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
                    doDeleteData();
                }
                else{
                    new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                }
            }
        });

        logout = findViewById(R.id.profile_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sp.edit().remove(ConstantSp.NAME).commit();
                sp.edit().clear().commit();
                new CommonMethod(ProfileActivity.this,MainActivity.class);
                finish();
            }
        });

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_contact);
        password = findViewById(R.id.profile_password);

        passwordHide = findViewById(R.id.profile_password_hide);
        passwordShow = findViewById(R.id.profile_password_show);

        confirmPassword = findViewById(R.id.profile_confirm_password);
        confirmPasswordLayout = findViewById(R.id.profile_confirm_password_layout);

        confirmPasswordHide = findViewById(R.id.profile_confirm_password_hide);
        confirmPasswordShow = findViewById(R.id.profile_confirm_password_show);

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

        submit.setOnClickListener(new View.OnClickListener() {
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
                    if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                        //new DoUpdate().execute();
                        pd = new ProgressDialog(ProfileActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doUpdateData();
                    }
                    else{
                        new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                    }
                    /*String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',PASSWORD='"+password.getText().toString()+"' WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' ";
                    db.execSQL(updateQuery);
                    new CommonMethod(ProfileActivity.this,"Profile Update Successfully");

                    sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();

                    setData(false);*/

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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

    }

    private void doUpdateData() {
        Call<GetSignupData> call = apiInterface.updateProfileData(sp.getString(ConstantSp.USERID,""),name.getText().toString(),contact.getText().toString(),email.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status==true){
                        new CommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();

                        setData(false);
                    }
                    else{
                        new CommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethod(ProfileActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                Log.d("RESPONSE",t.getMessage());
                new CommonMethod(ProfileActivity.this,t.getMessage());
            }
        });
    }

    private void doDeleteData() {
        Call<GetSignupData> call = apiInterface.deleteProfileData(sp.getString(ConstantSp.USERID,""));
        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status==true){
                        new CommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().clear().commit();
                        new CommonMethod(ProfileActivity.this,MainActivity.class);
                        finish();
                    }
                    else{
                        new CommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethod(ProfileActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                Log.d("RESPONSE",t.getMessage());
                new CommonMethod(ProfileActivity.this,t.getMessage());
            }
        });
    }

    private void setData(boolean b) {
        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        password.setText(sp.getString(ConstantSp.PASSWORD,""));
        confirmPassword.setText(sp.getString(ConstantSp.PASSWORD,""));

        name.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        password.setEnabled(b);

        if(b){
            confirmPasswordLayout.setVisibility(View.VISIBLE);

            passwordHide.setVisibility(View.VISIBLE);
            passwordShow.setVisibility(View.GONE);

            editProfile.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);

        }
        else{
            password.setTransformationMethod(new PasswordTransformationMethod());

            confirmPasswordLayout.setVisibility(View.GONE);

            passwordHide.setVisibility(View.GONE);
            passwordShow.setVisibility(View.GONE);

            editProfile.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);

        }

    }

    private class DoUpdate extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",sp.getString(ConstantSp.USERID,""));
            hashMap.put("name",name.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"updateProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("status")==true){
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                    sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();

                    setData(false);
                }
                else{
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class DoDelete extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",sp.getString(ConstantSp.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"deleteProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("status")==true){
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                    sp.edit().clear().commit();
                    new CommonMethod(ProfileActivity.this,MainActivity.class);
                    finish();
                }
                else{
                    new CommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}