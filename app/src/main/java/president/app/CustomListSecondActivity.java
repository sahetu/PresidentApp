package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomListSecondActivity extends AppCompatActivity {

    ListView listView;

    String[] nameArray = {"Apple","Orange","Banana","Watermelon"};
    //int[] imageArray = {R.drawable.apple,R.drawable.orange,R.drawable.banana,R.drawable.watermelon};
    String[] imageArray = {
            "https://i.pinimg.com/originals/ea/c9/96/eac9960a545ec495d88ce0554b9dd51a.png",
            "https://i.pinimg.com/originals/df/16/37/df1637282d1be1f1b5cee0e2a1bd668d.png",
            "https://atlas-content-cdn.pixelsquid.com/stock-images/ripe-yellow-banana-cluster-vnJ4dO0-600.jpg",
            "https://png.pngtree.com/png-clipart/20230417/original/pngtree-watermelon-summer-transparent-png-image_9059773.png"
    };

    String[] descArray = {
            "The apple is one of the pome (fleshy) fruits. Apples at harvest vary widely in size, shape, colour, and acidity, but most are fairly round and some shade of red or yellow. The thousands of varieties fall into three broad classes: cider, cooking, and dessert varieties.",
            "Orange usually sweet juicy fruit with a yellowish to reddish orange rind. b. : any of various small evergreen citrus trees having shiny leaves, fragrant white flowers, and fruits which are oranges. 2. : a color between red and yellow.",
            "A banana is a curved, yellow fruit with a thick skin and soft sweet flesh. If you eat a banana every day for breakfast, your roommate might nickname you \"the monkey.\" A banana is a tropical fruit that's quite popular all over the world. It grows in bunches on a banana tree.",
            "Watermelon is grown in favorable climates from tropical to temperate regions worldwide for its large edible fruit, which is a berry with a hard rind and no internal divisions, and is botanically called a pepo. The sweet, juicy flesh is usually deep red to pink, with many black seeds, although seedless varieties exist."
    };

    ArrayList<CustomSecondList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_second);

        listView = findViewById(R.id.custom_list_second);

        arrayList = new ArrayList<>();
        for(int i=0;i<nameArray.length;i++){
            CustomSecondList list = new CustomSecondList();
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);
            list.setDescription(descArray[i]);
            arrayList.add(list);
        }

        CustomListSecondAdapter adapter = new CustomListSecondAdapter(CustomListSecondActivity.this,arrayList);
        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CommonMethod(CustomListActivity.this,nameArray[i]);
            }
        });*/
    }
}