package president.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

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

    ImageView imageView;
    String[] appPermission = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] appPermission33 = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_VIDEO};

    int PERMISSION_REQUEST_CODE = 1000;
    int IMAGE_REQUEST_CODE = 1001;
    String sSelectedPath = "";
    String sChangeImage = "";

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

        imageView = findViewById(R.id.profile_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermission()) {
                    selectImageData();
                }
            }
        });

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
                        if(sChangeImage.equalsIgnoreCase("")) {
                            doUpdateData();
                        }
                        else{
                            doUpdateImageData();
                        }
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

    private void doUpdateImageData() {
        RequestBody idPart = RequestBody.create(MultipartBody.FORM,sp.getString(ConstantSp.USERID,""));
        RequestBody namePart = RequestBody.create(MultipartBody.FORM,name.getText().toString());
        RequestBody contactPart = RequestBody.create(MultipartBody.FORM,contact.getText().toString());
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM,email.getText().toString());
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM,password.getText().toString());

        File file = new File(sSelectedPath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("profileImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        Call<GetProfileImageData> call = apiInterface.updateProfileImageData(idPart,namePart,contactPart,emailPart,passwordPart,filePart);
        call.enqueue(new Callback<GetProfileImageData>() {
            @Override
            public void onResponse(Call<GetProfileImageData> call, Response<GetProfileImageData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status==true){
                        new CommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.IMAGE,response.body().image).commit();
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
            public void onFailure(Call<GetProfileImageData> call, Throwable t) {
                pd.dismiss();
                Log.d("RESPONSE",t.getMessage());
                new CommonMethod(ProfileActivity.this,t.getMessage());
            }
        });
    }

    public boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String perm : appPermission33) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(perm);
                }
            }
            if (!listPermission.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), PERMISSION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            for (String perm : appPermission) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(perm);
                }
            }
            if (!listPermission.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), PERMISSION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                selectImageData();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, permName)) {
                        /*showDialogPermission("", "This App needs Read External Storage And Location permissions to work whithout and problems.",*/
                        showDialogPermission("", "This App needs Read External Storage permissions to work whithout and problems.",
                                "Yes, Grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //finishAffinity();
                                    }
                                }, false);
                    } else {
                        showDialogPermission("", "You have denied some permissions. Allow all permissions at [Setting] > [Permissions]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }, "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    private void selectImageData() {
        FishBun.with(ProfileActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(1)
                .isStartInAllView(false)
                .setIsUseDetailView(false)
                .setReachLimitAutomaticClose(true)
                .setSelectCircleStrokeColor(android.R.color.transparent)
                .setActionBarColor(Color.parseColor("#F44336"), Color.parseColor("#F44336"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .startAlbumWithOnActivityResult(IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            //mSelected = Matisse.obtainResult(data);
            List<Uri> mSelected = data.getParcelableArrayListExtra(FishBun.INTENT_PATH);
            Log.d("RESPONSE_IMAGE_URI",mSelected.get(0).toString());
            sSelectedPath = getImage(mSelected.get(0));
            Log.d("RESPONSE_IMAGE_PATH",sSelectedPath);
            imageView.setImageURI(mSelected.get(0));
            sChangeImage = "Yes";
        }
    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor c = managedQuery(uri, s_array, null, null, null);
            int id = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (c.moveToFirst()) {
                do {
                    path = c.getString(id);
                }
                while (c.moveToNext());
                //c.close();
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

    public AlertDialog showDialogPermission(String title, String msg, String positiveLable, DialogInterface.OnClickListener positiveOnClickListener, String negativeLable, DialogInterface.OnClickListener negativeOnClickListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable, positiveOnClickListener);
        builder.setNegativeButton(negativeLable, negativeOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
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

        Glide.with(ProfileActivity.this).load(sp.getString(ConstantSp.IMAGE,"")).placeholder(R.mipmap.ic_launcher).into(imageView);

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