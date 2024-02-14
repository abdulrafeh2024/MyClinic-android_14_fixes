package com.codemybrainsout.placesearch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rahul Juneja on 30-10-2015.
 */
public class PlaceSearchDialog extends AppCompatDialog implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private String TAG = "PlaceSearchDialog";

    private AppCompatAutoCompleteTextView locationET;
    private TextInputLayout locationTIL;
    private AppCompatTextView cancelTV;
    private AppCompatTextView okTV;
    private ImageView headerImageIV;
    String street = "", suburb = "", postcode = "", country = "", state = "";

    public GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static LatLngBounds BOUNDS_WORLD = new LatLngBounds(
            new LatLng(-85, 180), new LatLng(85, -180));

    private Context context;
    private Builder builder;

    public interface LocationNameListener {
        public void locationName(String locationName, String street, String suburb, String postcode, String country, String state);
    }

    public PlaceSearchDialog(Context context, Builder builder) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.place_search_dialog);
        this.builder = builder;
        this.context = builder.context;
    }

    public void init() {

        locationET = (AppCompatAutoCompleteTextView) findViewById(R.id.place_search_dialog_location_ET);
        locationTIL = (TextInputLayout) findViewById(R.id.place_search_dialog_location_TIL);
        cancelTV = (AppCompatTextView) findViewById(R.id.place_search_dialog_cancel_TV);
        okTV = (AppCompatTextView) findViewById(R.id.place_search_dialog_ok_TV);
        headerImageIV = (ImageView) findViewById(R.id.place_search_dialog_header_image_IV);

        okTV.setOnClickListener(this);
        cancelTV.setOnClickListener(this);

        buildDialog();

        locationET.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient, BOUNDS_WORLD, null);
        locationET.setThreshold(5);
        locationET.setAdapter(mAdapter);
    }

    private void buildDialog() {

        okTV.setText(!TextUtils.isEmpty(builder.positiveText) ? builder.positiveText : context.getResources().getString(R.string.ok));
        cancelTV.setText(!TextUtils.isEmpty(builder.negativeText) ? builder.negativeText : context.getResources().getString(R.string.cancel));
        locationET.setHint(!TextUtils.isEmpty(builder.hintText) ? builder.hintText : context.getResources().getString(R.string.enter_location_hint));

        okTV.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : ContextCompat.getColor(context, R.color.mt_red));
        cancelTV.setTextColor(builder.negativeTextColor != 0 ? ContextCompat.getColor(context, builder.negativeTextColor) : ContextCompat.getColor(context, R.color.mt_gray4));
        locationET.setHintTextColor(builder.hintTextColor != 0 ? ContextCompat.getColor(context, builder.hintTextColor) : ContextCompat.getColor(context, R.color.mt_gray3));

        if (builder.latLngBounds != null) {
            BOUNDS_WORLD = builder.latLngBounds;
        }

        if (builder.headerImageResource != 0) {
            headerImageIV.setImageResource(builder.headerImageResource);
        } else {
            headerImageIV.setImageResource(R.drawable.place_picker_dialog);
        }

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            final CharSequence primaryText = item.getPrimaryText(null);

            //Hide Keyboard
            InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(), 0);

            placeMehtod(mGoogleApiClient, placeId);

            Log.i(TAG, "Autocomplete item selected: " + primaryText + "Place Id " + placeId);
        }
    };

    private void ok() {
        if (builder.locationNameListener != null) {
            builder.locationNameListener.locationName(locationET.getText().toString().trim(), street, suburb, postcode, country, state);
        }
        dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                //.enableAutoManage(activity, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "Google API client connected");
        init();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Google API client suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(context,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.place_search_dialog_ok_TV) {
            ok();
        } else if (view.getId() == R.id.place_search_dialog_cancel_TV) {
            dismiss();
        }
    }

    public static class Builder {

        private Context context;
        private LocationNameListener locationNameListener;
        private LatLngBounds latLngBounds;
        private String positiveText, negativeText, hintText;
        private int positiveTextColor, negativeTextColor, hintTextColor;
        private int headerImageResource;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLocationNameListener(LocationNameListener locationNameListener) {
            this.locationNameListener = locationNameListener;
            return this;
        }

        public Builder setLatLngBounds(LatLngBounds latLngBounds) {
            this.latLngBounds = latLngBounds;
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setHintText(String hintText) {
            this.hintText = hintText;
            return this;
        }

        public Builder setPositiveTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder setNegativeTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        public Builder setHintTextColor(int hintTextColor) {
            this.hintTextColor = hintTextColor;
            return this;
        }

        public Builder setHeaderImage(int headerImageResource) {
            this.headerImageResource = headerImageResource;
            return this;
        }

        public PlaceSearchDialog build() {
            return new PlaceSearchDialog(context, this);
        }

    }


    void placeMehtod(GoogleApiClient googleApiClient, String placeId) {
        Places.GeoDataApi.getPlaceById(googleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {

                    @Override
                    public void onResult(PlaceBuffer places) {

                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            return;
                        }

                        // Setup Geocoder
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addresses;

                        // Attempt to Geocode from place lat & long
                        try {

                            addresses = geocoder.getFromLocation(
                                    places.get(0).getLatLng().latitude,
                                    places.get(0).getLatLng().longitude,
                                    1);

                            if (addresses.size() > 0) {

                                // Here are some results you can geocode
//                                String ZIP;
//                                String city;
//                                String state;
//                                String country;

                                if (addresses.get(0).getPostalCode() != null) {
                                    postcode = addresses.get(0).getPostalCode();
                                    Log.d("postcode", postcode);
                                }

                                if (addresses.get(0).getLocality() != null) {
                                    state = addresses.get(0).getLocality();
                                    Log.d("state", state);
                                }

                                if (addresses.get(0).getAdminArea() != null) {
                                    state = addresses.get(0).getAdminArea();
                                    Log.d("state", state);
                                }

                                if (addresses.get(0).getCountryName() != null) {
                                    country = addresses.get(0).getCountryName();
                                    Log.d("country", country);
                                }

                                if (addresses.get(0).getSubAdminArea() != null) {
                                    suburb = addresses.get(0).getSubAdminArea();
                                    Log.d("suburb", suburb);
                                }

                                if (addresses.get(0).getAddressLine(0) != null) {
                                    street = addresses.get(0).getAddressLine(0);
                                    Log.d("suburb", suburb);
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        places.release();
                    }
                });


    }

}
