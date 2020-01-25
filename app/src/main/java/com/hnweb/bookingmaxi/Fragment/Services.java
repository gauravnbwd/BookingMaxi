package com.hnweb.bookingmaxi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.bookingmaxi.Adapter.ServiceAdapter;
import com.hnweb.bookingmaxi.AppConstant;
import com.hnweb.bookingmaxi.AppData;
import com.hnweb.bookingmaxi.HomeActivity;
import com.hnweb.bookingmaxi.LoginActivity;
import com.hnweb.bookingmaxi.Pojo.PojoServices;
import com.hnweb.bookingmaxi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Services extends Fragment {

ListView listView;
    String message;
    int message_code;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    String id,name,img,seats,rate;
    ServiceAdapter adapter;
    PojoServices model;
    ArrayList<PojoServices> dataModelslist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.services_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView=view.findViewById(R.id.listview);
        Service_api();


    }



    public void Service_api(){

        // Creating string request with post method.
        stringRequest = new StringRequest(Request.Method.POST, AppConstant.SERVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        model = new PojoServices();
                        Log.e("Service responce>>",ServerResponse);

                        try {

                            JSONObject obj = new JSONObject(ServerResponse);
                            message_code = obj.getInt("message_code");
                            message = obj.getString("message");
                            if (message_code == 1) {

                                JSONArray jsonArray = obj.getJSONArray("details");
                                for(int i=0;i<jsonArray.length();i++)

                                {

                                    JSONObject data = jsonArray.getJSONObject(i);
                                    id=data.getString("id");
                                    name=data.getString("name");
                                    seats=data.getString("seats");
                                    rate=data.getString("rate");
                                    img=data.getString("image");

                                    model.setTitle(name);
                                  //  model.setImg(img);

                                    dataModelslist= new ArrayList<>();
                                    dataModelslist.add(model);

                                    Log.e("service data >>",name+"\n"+seats);

                                }

                                adapter = new ServiceAdapter(dataModelslist, getActivity());
                                listView.setAdapter(adapter);

                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.e("login volley error>>",""+volleyError);

                        Toast.makeText(getActivity(), ""+volleyError, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
