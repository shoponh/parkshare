package com.parkshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parkshare.pojo.ItemInfo;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    private final Integer[] itemId;// = new ArrayList<Integer>();
    private final String[] itemName;// = new ArrayList<String>();
    private final String[] itemDesc;// = new ArrayList<String>();
    private final Double[] distance;// = new ArrayList<Double>();
    private final Integer[] imageId;// = new ArrayList<Integer>();

    private final ArrayList<ItemInfo> itemInfoList;

    public CustomListAdapter(Activity context, String[] itemname, ArrayList<ItemInfo> itemInfoList) {
        super(context, R.layout.mylist, itemname);



        this.context = context;
        this.itemname = itemname;
        this.itemInfoList = itemInfoList;

        this.imgid=null;
        this.itemId = null;
        this.itemName=null;
        this.itemDesc=null;
        this.distance=null;
        this.imageId=null;
    }

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylist, itemname);

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;

        this.itemId = null;
        this.itemName=null;
        this.itemDesc=null;
        this.distance=null;
        this.imageId=null;

        this.itemInfoList=null;
    }

    public CustomListAdapter(Activity context, Integer[] itemId,
                             String[] itemName,
                             String[] itemDesc,
                             Double[] distance,
                             Integer[] imageId) {
        super(context, R.layout.mylist, itemName);

        this.context=context;
        this.itemId=itemId;
        this.itemName=itemName;
        this.itemDesc=itemDesc;
        this.distance=distance;
        this.imageId=imageId;

        itemname = null;
        imgid = null;

        this.itemInfoList=null;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        ItemInfo currItemInfo = new ItemInfo();
        if(itemInfoList.size() > position){
            currItemInfo = itemInfoList.get(position);



            txtTitle.setText("ITEM-"+ currItemInfo.getId());
            imageView.setImageResource(currItemInfo.getImage());
            extratxt.setText("Description "+itemname[position]);
        }

        /**
        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText("Description "+itemname[position]);
        **/

        return rowView;
    };
}
