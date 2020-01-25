package com.hnweb.bookingmaxi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import de.hdodenhof.circleimageview.CircleImageView;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hnweb.bookingmaxi.Fragment.BookNow;
import com.hnweb.bookingmaxi.Fragment.ContactUs;
import com.hnweb.bookingmaxi.Fragment.MyProfile;
import com.hnweb.bookingmaxi.Fragment.Offers;
import com.hnweb.bookingmaxi.Fragment.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // tags used to attach the fragments
    private static final String TAG_my_profile = "My Profile";
    private static final String TAG_book_now = "Book Now";
    private static final String TAG_services = "Services";
    private static final String TAG_offers = "Offers";
    private static final String TAG_contactus = "Contact Us";
    private static final String TAG_history = "History";
    public static String CURRENT_TAG = TAG_my_profile;

    // index to identify current nav menu item
    public static int navItemIndex = 0;




    CircleImageView img_nav_profpic;
    ImageView img_edit_nav;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    String Imagepath = "none";
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        img_edit_nav=findViewById(R.id.imageView_edit_nav);
        img_nav_profpic=findViewById(R.id.iv_profpic_nav);

        /*img_edit_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });*/





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       /* navHeader = navigationView.getHeaderView(0);

        // Loading profile image
        ImageView profileImage = navHeader.findViewById(R.id.profileImage);
        Glide.with(this).load(NavigationDrawerConstants.PROFILE_URL)
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(0.5f)
                .into(profileImage);
        //Loading backgrounf image
        ImageView navBackground = navHeader.findViewById(R.id.img_header_bg);
        Glide.with(this).load(NavigationDrawerConstants.BACKGROUND_URL)
                .thumbnail(0.5f)
                .into(navBackground);
*/
        //Select Home by default
        navigationView.setCheckedItem(R.id.nav_book_now);
        Fragment fragment = new BookNow();
        displaySelectedFragment(fragment);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_my_profile) {

            navItemIndex = 0;
            CURRENT_TAG = TAG_my_profile;

            fragment = new MyProfile();
            displaySelectedFragment(fragment);
        } else if (id == R.id.nav_book_now) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_book_now;

            fragment = new BookNow();
            displaySelectedFragment(fragment);

        }else if (id == R.id.nav_services) {

            navItemIndex = 2;
            CURRENT_TAG = TAG_services;
            fragment = new Services();
            displaySelectedFragment(fragment);

        }else if (id == R.id.nav_offers) {

            navItemIndex = 3;
            CURRENT_TAG = TAG_offers;
            fragment = new Offers();
            displaySelectedFragment(fragment);

        }else if (id == R.id.nav_contact_us) {

            navItemIndex = 4;
            CURRENT_TAG = TAG_contactus;
            fragment = new ContactUs();
            displaySelectedFragment(fragment);

        }else if (id == R.id.nav_logout) {
            showLogoutAlert();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }





    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Are you Sure you want to Logout");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                dialog.cancel();


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }



    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getApplicationContext());
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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Imagepath= saveImage(bitmap);

                    img_nav_profpic.setImageBitmap(bitmap);
                    Toast.makeText(this, Imagepath, Toast.LENGTH_SHORT).show();
                    //  Log.e("Image path >>",Imagepath);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img_nav_profpic.setImageBitmap(thumbnail);
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
            MediaScannerConnection.scanFile(this,
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

}
