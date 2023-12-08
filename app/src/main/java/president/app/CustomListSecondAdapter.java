package president.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomListSecondAdapter  extends BaseAdapter {

    Context context;
    ArrayList<CustomSecondList> arrayList;
    SharedPreferences sp;

    public CustomListSecondAdapter(Context context, ArrayList<CustomSecondList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF,Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
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

        name.setText(arrayList.get(i).getName());
        //image.setImageResource(imageArray[i]);
        Glide.with(context).load(arrayList.get(i).getImage()).placeholder(R.mipmap.ic_launcher).into(image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new CommonMethod(context,nameArray[i]);
                sp.edit().putString(ConstantSp.PRODUCT_NAME,arrayList.get(i).getName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE,arrayList.get(i).getImage()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESC,arrayList.get(i).getDescription()).commit();
                new CommonMethod(context, ProductDetailActivity.class);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(context,"Image Clicked : "+arrayList.get(i).getName());
            }
        });

        return view;
    }
}