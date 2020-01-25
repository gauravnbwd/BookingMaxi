package com.hnweb.bookingmaxi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnweb.bookingmaxi.Pojo.PojoServices;
import com.hnweb.bookingmaxi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceAdapter extends ArrayAdapter<PojoServices> {

    private ArrayList<PojoServices> dataSet;
    Context mContext;

    public ServiceAdapter(ArrayList<PojoServices> data, Context context) {
        super(context, R.layout.item_services, data);
        this.dataSet = data;
        this.mContext=context;

    }

    // View lookup cache
    private static class ViewHolder {

ImageView img;
        TextView txt_name_itemservices;



    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PojoServices dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ServiceAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ServiceAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_services, parent, false);

            viewHolder.txt_name_itemservices = (TextView) convertView.findViewById(R.id.txt_name_itemservices);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_itemservices);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ServiceAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.txt_name_itemservices.setText(dataModel.getTitle());

       /* if(!dataModel.getImg().trim().isEmpty())
            Picasso.get().load(dataModel.getImg().trim()).into(viewHolder.img);
*/



        return convertView;
    }


}
