package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SpinnerListActivity extends AppCompatActivity {

    MultiAutoCompleteTextView multiAutoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView;
    //ListView listView;
    GridView gridView;
    Spinner spinner;
    //String[] technologyArray = {"Android", "Java", "Php", "iOS", "Python", "Flutter", "Data Science", "Node JS", "HTML"};
    ArrayList<String> technologyArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_list);

        spinner = findViewById(R.id.spinner);

        technologyArray = new ArrayList<>();
        technologyArray.add("Android");
        technologyArray.add("Php");
        technologyArray.add("Demo");
        technologyArray.add("Pyton");
        technologyArray.add("Java");
        technologyArray.add("Magento");
        technologyArray.add("Rect JS");
        technologyArray.add("HTML");
        technologyArray.add("CSS");

        technologyArray.set(3,"Python");
        technologyArray.remove(2);

        ArrayAdapter adapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1, technologyArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        //CustomAdapter;
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String sSelectedTechnology = String.valueOf(adapterView.getItemAtPosition(i));
                //String sSelectedTechnology = technologyArray[i];
                String sSelectedTechnology = technologyArray.get(i);
                new CommonMethod(SpinnerListActivity.this, sSelectedTechnology);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*listView = findViewById(R.id.spinner_listview);
        ArrayAdapter listAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,technologyArray);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sSelectedTechnology = technologyArray.get(i);
                new CommonMethod(SpinnerListActivity.this, sSelectedTechnology);
            }
        });*/

        gridView = findViewById(R.id.spinner_gridview);
        ArrayAdapter gridAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,technologyArray);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sSelectedTechnology = technologyArray.get(i);
                new CommonMethod(SpinnerListActivity.this, sSelectedTechnology);
            }
        });

        autoCompleteTextView = findViewById(R.id.spinner_autocomplete);
        ArrayAdapter autoCompleteAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,technologyArray);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        multiAutoCompleteTextView = findViewById(R.id.spinner_multiautocomplete);
        ArrayAdapter multiautoCompleteAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,technologyArray);
        multiAutoCompleteTextView.setThreshold(1);

        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        multiAutoCompleteTextView.setAdapter(multiautoCompleteAdapter);

    }
}