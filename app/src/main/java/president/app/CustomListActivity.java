package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class CustomListActivity extends AppCompatActivity {

    ListView listView;

    String[] nameArray = {"Apple","Orange","Banana","Watermelon"};
    int[] imageArray = {R.drawable.apple,R.drawable.orange,R.drawable.banana,R.drawable.watermelon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        listView = findViewById(R.id.custom_list);

        CustomListAdapter adapter = new CustomListAdapter(CustomListActivity.this,nameArray,imageArray);
        listView.setAdapter(adapter);

    }
}