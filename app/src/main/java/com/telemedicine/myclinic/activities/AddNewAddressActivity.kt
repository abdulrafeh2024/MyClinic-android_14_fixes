package com.telemedicine.myclinic.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.extensions.setText
import com.telemedicine.myclinic.models.MobileOpResult
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateDTO
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateResponse
import com.telemedicine.myclinic.models.homevisit.AddressBookUpdateDTO
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.views.AutoCompleteTextView
import com.telemedicine.myclinic.views.BoldTextInputLayout
import com.telemedicine.myclinic.views.LightEdittext
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_add_new_address.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AddNewAddressActivity : BaseActivity(), OnMapReadyCallback {

    var mLocationService: LocationService = LocationService()
    lateinit var mServiceIntent: Intent
    private var oPatientAddressBookId:  Long? = 0
    private var patientLat : Double? = 0.0
    private var patientLng : Double? = 0.0

    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpMapIfNeeded()
        startTheService()
        populateIntentData()

        autocompleteMap()

    }

    override fun layout(): Int {
        return R.layout.activity_add_new_address
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(appointmentEvent: AppointmentEvent) {
        if (appointmentEvent.doctorNameEn != "") {
            if (TextUtil.getArabic(this)) {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@AddNewAddressActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@AddNewAddressActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@AddNewAddressActivity, appointmentEvent.errorMSg)
        }
    }

    private fun autocompleteMap() {
        Places.initialize(applicationContext, "AIzaSyAv-3OjerSZpRFmOlk6GFzumrM04X4vUfU") //place here
        placesClient = Places.createClient(this)

        member_ship_no.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performPlaceAutocomplete(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.toString() != member_ship_no.tag) {
                    placesSuggestions.showDropDown()
                }
                // Store the current text in the tag for comparison next time
                member_ship_no.tag = s.toString()
            }
        })


    }

    private fun performPlaceAutocomplete(query: String) {
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("PK")
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val suggestedPlaces = response.autocompletePredictions.map { it.getFullText(null).toString() }

                updateSuggestionsDropdown(suggestedPlaces)
                Log.d("Autocomplete", "Autocomplete predictions: ${response.autocompletePredictions}")
            }
            .addOnFailureListener { exception ->
                Log.e("Autocomplete", "Autocomplete query failed: $exception")
            }
    }


    private fun updateSuggestionsDropdown(suggestedPlaces: List<String>) {
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.placesSuggestions)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            suggestedPlaces
        )
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.visibility = if (suggestedPlaces.isEmpty()) View.GONE else View.VISIBLE

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = adapter.getItem(position)
            member_ship_no.setText(selectedPlace)
            member_ship_no.text?.let { member_ship_no.setSelection(it.length) }
            member_ship_no.requestFocus()
            autoCompleteTextView.dismissDropDown()

            // Fetch place details using Place ID
            fetchPlaceDetails(selectedPlace)
        }

        if(member_ship_no.text.toString().isNullOrEmpty()){
            autoCompleteTextView.dismissDropDown()
        }


    }


    private fun fetchPlaceDetails(placeName: String?) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("PK")
            .setSessionToken(AutocompleteSessionToken.newInstance())
            .setQuery(placeName)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                if (!response.autocompletePredictions.isNullOrEmpty()) {
                    val placeId = response.autocompletePredictions[0].placeId

                    val placeFields = listOf(Place.Field.LAT_LNG)
                    val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()

                    placesClient.fetchPlace(fetchPlaceRequest)
                        .addOnSuccessListener { fetchPlaceResponse ->
                            val place = fetchPlaceResponse.place
                            val latLng = place.latLng

                            // Extract latitude and longitude
                            val latitude = latLng?.latitude
                            val longitude = latLng?.longitude

                            patientLat = latitude
                            patientLng = longitude


                            val latlng = LatLng(patientLat!!, patientLng!!)
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11f))

                           // Toast.makeText(this,"Latitude: $latitude, Longitude: $longitude",Toast.LENGTH_SHORT).show()
                            Log.d("PlaceDetails", "Latitude: $latitude, Longitude: $longitude")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("PlaceDetails", "Failed to fetch place details: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Autocomplete", "Autocomplete query failed: $exception")
            }
    }


    private fun setUpMapIfNeeded() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    fun startTheService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    AlertDialog.Builder(this).apply {
                        setTitle("Background permission")
                        setMessage(R.string.background_location_permission_message)
                        setPositiveButton("Start service anyway",
                            DialogInterface.OnClickListener { dialog, id ->
                                starServiceFunc()
                            })
                        setNegativeButton("Grant background Permission",
                            DialogInterface.OnClickListener { dialog, id ->
                                requestBackgroundLocationPermission()
                            })
                    }.create().show()

                } else if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    starServiceFunc()
                }
            } else {
                starServiceFunc()
            }

        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("ACCESS_FINE_LOCATION")
                    .setMessage("Location permission required")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestFineLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestFineLocationPermission()
            }
        }
    }

    fun starServiceFunc() {
        mLocationService = LocationService()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (!Util.isMyServiceRunning(mLocationService.javaClass, this)) {
            startService(mServiceIntent)

            /* Toast.makeText(this, getString(R.string.service_start_successfully), Toast.LENGTH_SHORT)
                 .show()*/
        } else {
            /* Toast.makeText(this, getString(R.string.service_already_running), Toast.LENGTH_SHORT)
                 .show()*/
        }
    }

    fun stopServiceFunc() {
        mLocationService = LocationService()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (Util.isMyServiceRunning(mLocationService.javaClass, this)) {
            stopService(mServiceIntent)
            // Toast.makeText(this, "Service stopped!!", Toast.LENGTH_SHORT).show()
        } else {
            //  Toast.makeText(this, "Service is already stopped!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), MY_BACKGROUND_LOCATION_REQUEST
        )
    }

    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_FINE_LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, requestCode.toString(), Toast.LENGTH_LONG).show()
        when (requestCode) {
            MY_FINE_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        requestBackgroundLocationPermission()
                    }

                } else {
                    Toast.makeText(
                        this,
                        "ACCESS_FINE_LOCATION permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
            MY_BACKGROUND_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        starServiceFunc()
                        /*  Toast.makeText(
                              this,
                              "Background location Permission Granted",
                              Toast.LENGTH_LONG
                          ).show()*/
                    }
                } else {
                    Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent) {
        /*binding.latitude.text = "Latitude : ${locationEvent.latitude}"
        binding.longitude.text = "Longitude : ${locationEvent.latitude}"
        */

        if (locationEvent.address == null) {
            stopServiceFunc()
            /* val sourceMarker = mMap!!.addMarker(
                 MarkerOptions().position(LatLng(locationEvent.latitude, locationEvent.longitude))
             )*/

            val latlng = LatLng(locationEvent.latitude, locationEvent.longitude)
            patientLat = locationEvent.latitude
            patientLng = locationEvent.longitude

            // sourceMarker!!.position = latlng!!
            // sourceMarker.title = address
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11f))

            val intents = Intent(this, AddressFetchService::class.java)
            //  enqueueWork(this, intents)
        } else {
            if (locationEvent.address.isNotEmpty()) {
                member_ship_no.setText(locationEvent.address[0].getAddressLine(0).toString())
            }
        }


    }

    companion object {
        private const val MY_FINE_LOCATION_REQUEST = 99
        private const val MY_BACKGROUND_LOCATION_REQUEST = 100

        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    private var mMap: GoogleMap? = null
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0


    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServiceFunc()
    }

    private fun createTheAddress(
        addressNotes: String?,
        aprtId: Int?,
        aprtNo: String?,
        buildingName: String?,
        buildingNo: String?,
        district: String?,
        street: String?,
        lable: String?,
        areaName: String?,
        lat: Double?,
        lng: Double?
    ) {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

            if (!ValidationHelper.isNullOrEmpty(securityToken)) {
                val addressBookCreateDTO = AddressBookCreateDTO(
                    securityToken,
                    addressNotes ?: "",
                    aprtId ?: 0,
                    aprtNo ?: "",
                    buildingName ?: "",
                    buildingNo ?: "",
                    district ?: "",
                    patientId ?: 0,
                    street ?: "",
                    lable ?: "",
                    areaName ?: "",
                    lat ?: 0.0,
                    lng ?: 0.0
                )

                RestClient.getInstance()
                    .addressBookCreate(addressBookCreateDTO) { result, status, errorMessage ->
                        hideWaitDialog()

                        val createResponse = result as? AddressBookCreateResponse
                        if (createResponse != null) {
                            val mobileOpResult = createResponse.mobileOpResult
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                // Address book created successfully
                                    finish()
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult?.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@AddNewAddressActivity)) {
                                        errorMesg = mobileOpResult.businessErrorMessageEn
                                    } else if (TextUtil.getArabic(this@AddNewAddressActivity)) {
                                        errorMesg = mobileOpResult.businessErrorMessageAr
                                    }
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult?.technicalErrorMessage)) {
                                    errorMesg += "\nTechnical Info:\n${mobileOpResult?.technicalErrorMessage}"
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@AddNewAddressActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance()
                                .showError(this@AddNewAddressActivity, errorMessage)
                        }
                    }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }
    private fun updatetheAddress(
        oPatientAddressBookId: Long?,
        addressNotes: String?,
        aprtId: Int?,
        aprtNo: String?,
        buildingName: String?,
        buildingNo: String?,
        district: String?,
        street: String?,
        lable: String?,
        areaName: String?,
        lat: Double?,
        lng: Double?
    ) {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

            if (!ValidationHelper.isNullOrEmpty(securityToken)) {
                val addressBookUpdateDTO = AddressBookUpdateDTO(
                    securityToken,
                    oPatientAddressBookId,
                    addressNotes ?: "",
                    aprtId ?: 0,
                    aprtNo ?: "",
                    buildingName ?: "",
                    buildingNo ?: "",
                    district ?: "",
                    patientId ?: 0,
                    street ?: "",
                    lable ?: "",
                    areaName ?: "",
                    lat ?: 0.0,
                    lng ?: 0.0
                )

                RestClient.getInstance()
                    .addressBookUpdate(addressBookUpdateDTO) { result, status, errorMessage ->
                        hideWaitDialog()

                        val createResponse = result as? MobileOpResult
                        if (createResponse != null) {
                            if (createResponse.result == Success.SUCCESSCODE.value) {
                                // Address book updated successfully
                                finish()
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(createResponse.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@AddNewAddressActivity)) {
                                        errorMesg = createResponse.businessErrorMessageEn
                                    } else if (TextUtil.getArabic(this@AddNewAddressActivity)) {
                                        errorMesg = createResponse.businessErrorMessageAr
                                    }
                                }
                                if (!ValidationHelper.isNullOrEmpty(createResponse.technicalErrorMessage)) {
                                    errorMesg += "\nTechnical Info:\n${createResponse.technicalErrorMessage}"
                                }
                                ErrorMessage.getInstance().showError(this@AddNewAddressActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance().showError(this@AddNewAddressActivity, errorMessage)
                        }
                    }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }

    /* override fun onMapReady(p0: GoogleMap) {

     }*/

    fun populateIntentData(){
        val district = intent.getStringExtra("district")
        val street = intent.getStringExtra("street")
        val building_name = intent.getStringExtra("building_name")
        val building_no = intent.getStringExtra("building_no")
        val apt_no = intent.getStringExtra("apt_no")
        val address = intent.getStringExtra("address")
        val label = intent.getStringExtra("label")
        oPatientAddressBookId = intent.getLongExtra("id",0)


        if (!district.isNullOrBlank()) {
            district_tv.setText(district)
        }
        if (!street.isNullOrBlank()) {
            street_tv.setText(street)
        }
        if (!building_name.isNullOrBlank()) {
            building_tv.setText(building_name)
        }
        if (!building_no.isNullOrBlank()) {
            building_no_tv.setText(building_no)
        }
        if (!apt_no.isNullOrBlank()) {
            apt_no_tv.setText(apt_no)
        }
        if (!address.isNullOrBlank()) {
            address_notes_tv.setText(address)
        }
        if (!label.isNullOrBlank()) {
            label_lt.setText(label)
        }

    }

    fun next(view: View) {
        val locationLayout = findViewById<BoldTextInputLayout>(R.id.location_search_et)
        val labelLayout = findViewById<BoldTextInputLayout>(R.id.label_lt)
        val districtLayout = findViewById<BoldTextInputLayout>(R.id.district_tv)
        val streetLayout = findViewById<BoldTextInputLayout>(R.id.street_tv)
        val buildingNameLayout = findViewById<BoldTextInputLayout>(R.id.building_tv)
        val buildingNoLayout = findViewById<BoldTextInputLayout>(R.id.building_no_tv)
        val aptNoLayout = findViewById<BoldTextInputLayout>(R.id.apt_no_tv)
        val addressNotesLayout = findViewById<BoldTextInputLayout>(R.id.address_notes_tv)

        val locationEt = locationLayout.editText as LightEdittext
        val labelEt = labelLayout.editText as LightEdittext
        val districtEt = districtLayout.editText as LightEdittext
        val streetEt = streetLayout.editText as LightEdittext
        val buildingNameEt = buildingNameLayout.editText as LightEdittext
        val buildingNoEt = buildingNoLayout.editText as LightEdittext
        val aptNoEt = aptNoLayout.editText as LightEdittext
        val addressNotesEt = addressNotesLayout.editText as LightEdittext

        val locationStr = locationEt.text.toString()
        val labelStr = labelEt.text.toString()
        val districtStr = districtEt.text.toString()
        val streetStr = streetEt.text.toString()
        val buildingNameStr = buildingNameEt.text.toString()
        val buildingNoStr = buildingNoEt.text.toString()
        val aptNoStr = aptNoEt.text.toString()
        val addressNotesStr = addressNotesEt.text.toString()


        if(oPatientAddressBookId != null && oPatientAddressBookId != 0L){
            updatetheAddress(
                oPatientAddressBookId,
                addressNotesStr,
                aprtId = 0,
                aprtNo = aptNoStr,
                buildingNameStr,
                buildingNo = buildingNoStr,
                street = streetStr,
                areaName = locationStr,
                lable = labelStr,
                lat = patientLat,
                lng = patientLng,
                district = districtStr
            )
        }else{
            createTheAddress(
                addressNotesStr,
                aprtId = 0,
                aprtNo = aptNoStr,
                buildingNameStr,
                buildingNo = buildingNoStr,
                street = streetStr,
                areaName = locationStr,
                lable = labelStr,
                lat = patientLat,
                lng = patientLng,
                district = districtStr
            )
        }

    }


}