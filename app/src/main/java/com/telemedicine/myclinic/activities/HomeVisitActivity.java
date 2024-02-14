package com.telemedicine.myclinic.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.telemedicine.myclinic.LocationSinglton.FusedLocationSingleton;
import com.telemedicine.myclinic.adapters.AutoCompleteAdapter;
import com.telemedicine.myclinic.adapters.SimpleLabTestAdapter;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.models.homevisit.HomeCreateDTO;
import com.telemedicine.myclinic.models.homevisit.LabOrder;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.activities.ServicesOrdersActivity.isLocationEnabled;

public class HomeVisitActivity extends BaseActivity implements OnMapReadyCallback {


    @BindView(R.id.lab_test_recycler)
    RecyclerView labTestRecycler;

    @BindView(R.id.autocomplete)
    com.telemedicine.myclinic.views.AutoCompleteTextView autoCompleteTextView;

    AutoCompleteAdapter adapter;
    PlacesClient placesClient;

    @BindView(R.id.address)
    LightEdittext mAddress;

    @BindView(R.id.dateTime)
    LightEdittext dateTime;

    @BindView(R.id.time_selection)
    LightSpinnerEdittext timeDoctor;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.booknow)
    LightButton booknow;

    private GoogleMap mMap = null;
    Marker marker = null;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    ArrayList<OrdersLabModel> OrdersLab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_home_visit;
    }


    void init() {

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDOB();
            }
        };

        labTestRecycler.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        if (intent != null) {
            OrdersLab = intent.getParcelableArrayListExtra("orderLab");
            if (!ValidationHelper.isNullOrEmpty(OrdersLab)) {

                SimpleLabTestAdapter confirmationAdapter = new SimpleLabTestAdapter(this, OrdersLab);
                labTestRecycler.setAdapter(confirmationAdapter);
            }

        }

        setUpMapIfNeeded();

        String apiKey = getString(R.string.api_key);
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "api key is empty", Toast.LENGTH_LONG).show();
            return;
        }

        // Setup Places Client
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//        }

      //  placesClient = Places.createClient(this);
        //initAutoCompleteTextView();
        setTimeValue();
        transformView();
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            autoCompleteTextView.setGravity(Gravity.RIGHT);
            dateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
            dateTime.setGravity(Gravity.RIGHT);
            timeDoctor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            timeDoctor.setGravity(Gravity.RIGHT);
            booknow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            booknow.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            toolbar_left_iv.setRotation(180);
        }
    }

    private void updateDOB() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String day = "E";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat(day, Locale.getDefault());

        if (sdf1.format(myCalendar.getTime()).equalsIgnoreCase("Sat") ||
                sdf1.format(myCalendar.getTime()).equalsIgnoreCase("Fri")) {

            ErrorMessage.getInstance().showError(this, getString(R.string.select_other_days));

            return;
        }

        dateTime.setText(sdf.format(myCalendar.getTime()));
    }

    private void initAutoCompleteTextView() {

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        autoCompleteTextView.setAdapter(adapter);
    }

    public void BookNow(View view) {

        String address = mAddress.getText().toString();
        String date = dateTime.getText().toString();
        String time = timeDoctor.getText().toString();
        String prefVisitTime = "";
        String timeSelected = "";

        ArrayList<LabOrder> labOrders = new ArrayList<>();

        if (!ValidationHelper.isNullOrEmpty(OrdersLab)) {

            for (OrdersLabModel ordersLabModel : OrdersLab) {

                LabOrder orderLab = new LabOrder();

                long id = ordersLabModel.getId();
                orderLab.setId(id);

                labOrders.add(orderLab);
            }
        }


        if (ValidationHelper.isNullOrEmpty(address)) {
            ErrorMessage.getInstance().showValidationMessage(this, mAddress, getString(R.string.please_select_Address));

            return;
        }

        if (ValidationHelper.isNullOrEmpty(date)) {
            ErrorMessage.getInstance().showValidationMessage(this, dateTime, getString(R.string.please_select_prefered_date));
            return;
        }

        if (ValidationHelper.isNullOrEmpty(time)) {
            ErrorMessage.getInstance().showValidationMessage(this, timeDoctor, getString(R.string.please_select_prefered_time));
            return;
        }

        if (time.equalsIgnoreCase("09 AM - 12 PM")) {
            timeSelected = " 09:00 am";

        } else if (time.equalsIgnoreCase("12 PM - 03 PM")) {
            timeSelected = " 12:00 pm";

        } else if (time.equalsIgnoreCase("03 PM - 06 PM")) {
            timeSelected = " 03:00 pm";
        } else if (time.equalsIgnoreCase("06 PM - 09 PM")) {
            timeSelected = " 06:00 pm";
        }

        TinyDB tinyDB = new TinyDB(this);

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String lat = String.valueOf(latLng.latitude);
        String lng = String.valueOf(latLng.longitude);
        String districtId = "137";
        String companyId = tinyDB.getString(Const.COMPANY_ID);

        if (TextUtil.getArabic(this))
            date = arabicToDecimal(date);

        String finalDate = date + timeSelected;


        prefVisitTime = LocaleDateHelper.convertDateStringFormatHome(finalDate, "dd/MM/yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        HomeCreateDTO homeCreateDTO = new HomeCreateDTO(securityToken, String.valueOf(patientId),
                lat, lng, address, districtId, prefVisitTime, labOrders,companyId);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().homeCreate(homeCreateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            SweetAlertDialog alertDialog = new SweetAlertDialog(HomeVisitActivity.this, SweetAlertDialog.SUCCESS_TYPE);

                            alertDialog.setTitleText(getString(R.string.my_clininc))
                                    .setContentText(getString(R.string.home_visit_message)).setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            }).show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(HomeVisitActivity.this, R.color.colorPrimary));


                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(HomeVisitActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(HomeVisitActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(HomeVisitActivity.this, errorMesg);

                        }

                    } else {
                        ErrorMessage.getInstance().showError(HomeVisitActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }


    }

    private void setUpMapIfNeeded() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {

                ErrorMessage.hideKeyboard(HomeVisitActivity.this, autoCompleteTextView);

                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {

                            String address = task.getPlace().getAddress();

                            LatLng latLng = task.getPlace().getLatLng();
                            locationSet(address, latLng);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HomeVisitActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    LatLng latLng = new LatLng(21.6589791, 39.1203779);

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_map)));
        locationSet(getString(R.string.my_clininc), latLng);

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

        if (!accessFineLocation || !accessCourseLocation || !isLocationEnabled(this)) {
            locationSet(getString(R.string.my_clininc), latLng);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    LocalBroadcastManager.getInstance(HomeVisitActivity.this).registerReceiver(mMessageReceiver,
                            new IntentFilter("location"));

                    FusedLocationSingleton.getInstance().startLocationUpdates();
                }
            }, 1000);
        }
    }

    void locationSet(String address, LatLng latLng) {

        marker.setPosition(latLng);
        marker.setTitle(address);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        mAddress.setText(address);


    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                Location location = intent.getParcelableExtra("location");

                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    locationSet("", latLng);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

        if (accessFineLocation || accessCourseLocation) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        }
    }

    @OnClick(R.id.dateTime)
    void apptDate() {

        long now = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1);

        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.DatePickerDialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(now);
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    void setTimeValue() {

        ArrayList<String> timeStr = new ArrayList<>();
        timeStr.add("09 AM - 12 PM");
        timeStr.add("12 PM - 03 PM");
        timeStr.add("03 PM - 06 PM");
        timeStr.add("06 PM - 09 PM");
        android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, timeStr);
        timeDoctor.setAdapter(cityAdapter);
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }


    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";

    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

}
