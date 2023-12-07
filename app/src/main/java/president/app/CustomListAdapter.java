package president.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

    Context context;
    String[] nameArray;
    int[] imageArray;

    public CustomListAdapter(Context context, String[] nameArray, int[] imageArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.imageArray = imageArray;
    }

    @Override
    public int getCount() {
        return nameArray.length;
    }

    @Override
    public Object getItem(int i) {
        return nameArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =  layoutInflater.inflate(R.layout.item_list,null);

        ImageView image = view.findViewById(R.id.item_list_image);
        TextView name = view.findViewById(R.id.item_list_name);

        name.setText(nameArray[i]);
        image.setImageResource(imageArray[i]);

        return view;
    }
}
