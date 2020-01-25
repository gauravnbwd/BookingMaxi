package com.hnweb.bookingmaxi.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hnweb.bookingmaxi.DatePickerFragment;
import com.hnweb.bookingmaxi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Calendar;

public class BookNow extends Fragment {

    RelativeLayout relaytive_layout;
    LinearLayout lr_booknow;

    // calendar

    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;

    //-------------


    FloatingActionButton floatingActionButton;
    LinearLayout lySecondView, lyThirdView;
    String value_new = "";
    ImageView imageViewDeleteTwo, imageViewDeleteThree, imageViewThree, imageViewFour;
    View viewSecond, viewThree;


    Button btn_calendar,btn_book_now,btn_paypal;
    TextView txt_distance,txt_time,txt_price,txt_datelabel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.booknow_fragment, container, false);


        Window window = getActivity().getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorbookmax));


        try {
            String apiKey = "AIzaSyBuWLVkb_JXI3kfbULGcyaM-YAH1pme_eM";
            if (!Places.isInitialized()) {
                Places.initialize(getActivity(), apiKey);
            }

            // Create a new Places client instance.
            PlacesClient placesClient = Places.createClient(getActivity());

            getFromLocations();
            getToLocationsFirst();
            getToLocationsSecond();
            getToLocationsThird();

        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return view;



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Book now");
        initViewByIDs(view);
    }

    private void initViewByIDs(View view) {

        relaytive_layout=view.findViewById(R.id.layout_payment);
        lr_booknow=view.findViewById(R.id.layout_lr_booknow);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
       // showDate(year, month+1, day);

        btn_calendar=view.findViewById(R.id.btn_calender);
        btn_paypal=view.findViewById(R.id.btn_paynow);
        btn_book_now=view.findViewById(R.id.btn_book_now);
        txt_distance=view.findViewById(R.id.txt_distance);
        txt_time=view.findViewById(R.id.txt_time);
        txt_price=view.findViewById(R.id.txt_price);
        txt_datelabel=view.findViewById(R.id.txt_date_label);



        lySecondView = view.findViewById(R.id.ly_second_destination);
        lyThirdView = view.findViewById(R.id.ly_three_destination);
        imageViewDeleteTwo = view.findViewById(R.id.imageView_delete_two);
        imageViewDeleteThree = view.findViewById(R.id.imageView_delete_three);
        imageViewThree = view.findViewById(R.id.image_three);
        imageViewFour = view.findViewById(R.id.image_four);
        viewSecond = view.findViewById(R.id.view3);
        viewThree = view.findViewById(R.id.view4);

        floatingActionButton = view.findViewById(R.id.floating_button_add_destinations);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_new.equals("")) {
                    value_new = "1";
                    lySecondView.setVisibility(View.VISIBLE);
                    viewSecond.setVisibility(View.VISIBLE);
                    imageViewThree.setVisibility(View.VISIBLE);
                } else if (value_new.equals("1")) {
                    value_new = "2";
                    lyThirdView.setVisibility(View.VISIBLE);
                    imageViewDeleteTwo.setVisibility(View.GONE);
                    viewThree.setVisibility(View.VISIBLE);
                    imageViewFour.setVisibility(View.VISIBLE);
                } else if (value_new.equals("2")) {
                    Toast.makeText(getActivity(), "Only Add 3 Destination Locations", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imageViewDeleteTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_new = "";
                lySecondView.setVisibility(View.GONE);
                viewSecond.setVisibility(View.GONE);
                imageViewThree.setVisibility(View.GONE);
            }
        });

        imageViewDeleteThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_new = "1";
                imageViewDeleteTwo.setVisibility(View.VISIBLE);
                lyThirdView.setVisibility(View.GONE);
                viewThree.setVisibility(View.GONE);
                imageViewFour.setVisibility(View.GONE);
            }
        });


        //calender view

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();

            }
        });
        //--------------------------

// book now
        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relaytive_layout.setVisibility(View.VISIBLE);

            }
        });

        //--------------

        btn_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Payment integration is under development...:)", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public interface OnBackPressed {
        void onBackPressed();
    }

    @SuppressLint("ResourceAsColor")
    private void getFromLocations() {
        try {
            // Initialize the AutocompleteSupportFragment.
            AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            //autocompleteFragment.getView().setBackgroundResource(R.drawable.progress_bg);
            autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
            // Specify the types of place data to return.
            autocompleteFragment.getView().setBackgroundResource(R.drawable.et_search_bg);

            EditText etPlace = (EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
            etPlace.setTextColor(getResources().getColor(R.color.colorAccent));
            etPlace.setHintTextColor(getResources().getColor(R.color.colorAccent));


            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the response.
            if (autocompleteFragment != null) {
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i("Find", "Place: " + place.getName() + ", " + place.getId());

                        LatLng queriedLocation = place.getLatLng();
                        Log.d("Latitudeis", "" + queriedLocation.latitude);
                        Log.e("Longitudeis", "" + queriedLocation.longitude);
                        String stringLatitude = String.valueOf(queriedLocation.latitude);
                        String stringLongitude = String.valueOf(queriedLocation.longitude);
                        Log.d("SelectedLat", stringLatitude + " :: " + stringLongitude);
                        //getLocationFromAddress(Second_RegisterationActivity.this, queriedLocation.latitude, queriedLocation.longitude);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("Find ", "An error occurred: " + status);
                        //txtVw.setText(status.toString());
                    }
                });
            }
        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    private void getToLocationsFirst() {

        try {

            AutocompleteSupportFragment autocompleteFragmentOne = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_desti_one);
            autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
            autocompleteFragmentOne.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragmentOne.getView().setBackgroundResource(R.drawable.et_search_bg);

            EditText etPlace = (EditText)autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_input);
            etPlace.setTextColor(getResources().getColor(R.color.colorSearchText));
            etPlace.setHintTextColor(getResources().getColor(R.color.colorSearchText));

            // Set up a PlaceSelectionListener to handle the response.
            if (autocompleteFragmentOne != null) {
                autocompleteFragmentOne.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i("Find", "Place: " + place.getName() + ", " + place.getId());

                        LatLng queriedLocation = place.getLatLng();
                        Log.d("Latitudeis", "" + queriedLocation.latitude);
                        Log.e("Longitudeis", "" + queriedLocation.longitude);
                        String stringLatitude = String.valueOf(queriedLocation.latitude);
                        String stringLongitude = String.valueOf(queriedLocation.longitude);
                        Log.d("SelectedLat", stringLatitude + " :: " + stringLongitude);
                        //getLocationFromAddress(Second_RegisterationActivity.this, queriedLocation.latitude, queriedLocation.longitude);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("Find ", "An error occurred: " + status);
                        //txtVw.setText(status.toString());
                    }
                });
            }

        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    private void getToLocationsSecond() {
        try {

            AutocompleteSupportFragment autocompleteFragmentOne = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_desti_two);
            autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
            autocompleteFragmentOne.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragmentOne.getView().setBackgroundResource(R.drawable.et_search_bg);

            EditText etPlace = (EditText)autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_input);
            etPlace.setTextColor(getResources().getColor(R.color.colorSearchText));
            etPlace.setHintTextColor(getResources().getColor(R.color.colorSearchText));
            etPlace.setText("");

            // Set up a PlaceSelectionListener to handle the response.
            if (autocompleteFragmentOne != null) {
                autocompleteFragmentOne.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i("Find", "Place: " + place.getName() + ", " + place.getId());

                        LatLng queriedLocation = place.getLatLng();
                        Log.d("Latitudeis", "" + queriedLocation.latitude);
                        Log.e("Longitudeis", "" + queriedLocation.longitude);
                        String stringLatitude = String.valueOf(queriedLocation.latitude);
                        String stringLongitude = String.valueOf(queriedLocation.longitude);
                        Log.d("SelectedLat", stringLatitude + " :: " + stringLongitude);
                        //getLocationFromAddress(Second_RegisterationActivity.this, queriedLocation.latitude, queriedLocation.longitude);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("Find ", "An error occurred: " + status);
                        //txtVw.setText(status.toString());
                    }
                });
            }

        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private void getToLocationsThird() {

        try {

            AutocompleteSupportFragment autocompleteFragmentOne = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_desti_three);
            autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
            autocompleteFragmentOne.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragmentOne.getView().setBackgroundResource(R.drawable.et_search_bg);

            EditText etPlace = (EditText)autocompleteFragmentOne.getView().findViewById(R.id.places_autocomplete_search_input);
            etPlace.setTextColor(getResources().getColor(R.color.colorSearchText));
            etPlace.setHintTextColor(getResources().getColor(R.color.colorSearchText));

            // Set up a PlaceSelectionListener to handle the response.
            if (autocompleteFragmentOne != null) {
                autocompleteFragmentOne.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i("Find", "Place: " + place.getName() + ", " + place.getId());

                        LatLng queriedLocation = place.getLatLng();
                        Log.d("Latitudeis", "" + queriedLocation.latitude);
                        Log.e("Longitudeis", "" + queriedLocation.longitude);
                        String stringLatitude = String.valueOf(queriedLocation.latitude);
                        String stringLongitude = String.valueOf(queriedLocation.longitude);
                        Log.d("SelectedLat", stringLatitude + " :: " + stringLongitude);
                        //getLocationFromAddress(Second_RegisterationActivity.this, queriedLocation.latitude, queriedLocation.longitude);
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("Find ", "An error occurred: " + status);
                        //txtVw.setText(status.toString());
                    }
                });
            }

        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }



    //.......calendar....
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            txt_datelabel.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    //--------------------------------
}
