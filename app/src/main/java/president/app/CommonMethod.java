package president.app;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class CommonMethod {

    CommonMethod(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    CommonMethod(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }

}
