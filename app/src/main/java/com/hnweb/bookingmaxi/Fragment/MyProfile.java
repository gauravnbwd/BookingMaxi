package com.hnweb.bookingmaxi.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hnweb.bookingmaxi.AppConstant;
import com.hnweb.bookingmaxi.AppData;
import com.hnweb.bookingmaxi.LoginActivity;
import com.hnweb.bookingmaxi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends Fragment {

    CircleImageView img_profpic;
    ImageView img_editbtn;

    Button btn_about_label,btn_setting_label,btn_save;
    LinearLayout layout_about,layout_setting;
    TextView txt_name,txt_email,txt_phonno;
FloatingActionButton edit;
    EditText et_name,et_email,et_phonno,et_password,et_address;
    String str_name,str_email,str_phoneno,str_pass,str_address;

    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    String Imagepath = "none";

    String message;
    int message_code;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    String id,full_name,email,password,address,phoneno;
    Activity activity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myprofile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        img_profpic=view.findViewById(R.id.iv_profpic);
        img_editbtn=view.findViewById(R.id.imageView_edit);

        btn_about_label=view.findViewById(R.id.btn_about_lable);
        btn_setting_label=view.findViewById(R.id.btn_setting_lable);

        txt_name=view.findViewById(R.id.txt_name);
        txt_email=view.findViewById(R.id.txt_email);
        txt_phonno=view.findViewById(R.id.txt_phonno);

        et_name=view.findViewById(R.id.et_name);
        et_email=view.findViewById(R.id.et_email);
        et_phonno=view.findViewById(R.id.et_phonno);
        et_password=view.findViewById(R.id.et_pass);
        et_address=view.findViewById(R.id.et_address);

        et_name.setText(AppData.KEY_FULLNAME);
        et_email.setText(AppData.KEY_EMAIL);
        et_address.setText(AppData.KEY_ADDRESS);
        et_phonno.setText(AppData.KEY_PHONENO);
        et_password.setText(AppData.KEY_PASSWORD);

        layout_about=view.findViewById(R.id.layout_about);
        layout_setting=view.findViewById(R.id.layout_setting);

        edit=view.findViewById(R.id.btn_edit);
        btn_save=view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                My_Profile_update_api();
            }
        });


        btn_about_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                My_Profile_api();
                layout_about.setVisibility(View.VISIBLE);
                layout_setting.setVisibility(View.GONE);

            }
        });

        btn_setting_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_about.setVisibility(View.GONE);
                layout_setting.setVisibility(View.VISIBLE);

            }
        });

        img_editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit your profile...!", Toast.LENGTH_SHORT).show();
            }
        });

        getActivity().setTitle("My profile");


    }

    // pick photo from camera

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Imagepath= saveImage(bitmap);

                    img_profpic.setImageBitmap(bitmap);
                   // Toast.makeText(getActivity(), Imagepath, Toast.LENGTH_SHORT).show();
                    //  Log.e("Image path >>",Imagepath);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img_profpic.setImageBitmap(thumbnail);
            Imagepath = saveImage(thumbnail);

        }
    }


    public Bitmap bitmapImage;
    public String saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = null;
        File wallpaperDirectory = null;
        try {

            bytes = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            wallpaperDirectory = new File(
                    Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            // have the object build the directory structure, if needed.
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //.....................................................


    public void My_Profile_api() {

        // Creating string request with post method.
                    stringRequest = new StringRequest(Request.Method.POST, AppConstant.MY_PROFILE_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    Log.e("my prof responce>>", ServerResponse);

                                    try {

                                        JSONObject obj = new JSONObject(ServerResponse);
                                        message_code = obj.getInt("message_code");
                                        message = obj.getString("message");

                                        if (message_code == 1) {

                                           /* "id": "1",
                                                    "full_name": "Roman Reign",
                                                    "email": "john@gmail.com",
                                                    "phone": "1234567890",
                                                    "password": "123456",
                                                    "address": "New York",
                                                    "notification_receive": "Yes",
                                                    "created_at": "2019-12-18 02:28:15",
                                                    "updated_at": "2019-12-18 02:28:15"*/

                                            JSONArray jsonArray = obj.getJSONArray("details");
                                            for(int i=0;i<jsonArray.length();i++) {

                                                JSONObject data = jsonArray.getJSONObject(i);
                                                id = data.getString("id");
                                                full_name = data.getString("full_name");
                                                email = data.getString("email");
                                                phoneno = data.getString("phone");
                                                address = data.getString("address");

                                            }

                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                                            txt_name.setText(full_name);
                                            txt_email.setText(email);
                                            txt_phonno.setText(phoneno);


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

                                    Log.e("volley myprof error>>", "" + volleyError);

                                    Toast.makeText(getActivity(), "" + volleyError, Toast.LENGTH_SHORT).show();

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("id", AppData.id);

                            Log.e("param>>", params.toString());

                            return params;
                        }

                    };

                    requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);



    }

    public void My_Profile_update_api() {

        str_name=et_name.getText().toString();
        str_address=et_address.getText().toString();
        str_phoneno=et_phonno.getText().toString();
        str_pass=et_password.getText().toString();

        // Creating string request with post method.
        stringRequest = new StringRequest(Request.Method.POST, AppConstant.MY_PROFILE_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        Log.e("myprofupdate responce>>", ServerResponse);

                        try {

                            JSONObject obj = new JSONObject(ServerResponse);
                            message_code = obj.getInt("message_code");
                            message = obj.getString("message");

                            if (message_code == 1) {


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

                        Log.e("voly profupdate error>>", "" + volleyError);

                        Toast.makeText(getActivity(), "" + volleyError, Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", AppData.id);
                params.put("notification_receive", AppData.KEY_NOTIFICATION_STATUS);
                params.put("address", str_address);
                params.put("full_name", str_name);
                params.put("phone", str_phoneno);
                params.put("password", str_pass);

                Log.e("param>>", params.toString());

                return params;
            }

        };

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }
}
