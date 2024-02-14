package com.telemedicine.myclinic.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.telemedicine.myclinic.adapters.MedicationDeliveriesAdapter
import com.telemedicine.myclinic.adapters.MedicineListAdapter
import com.telemedicine.myclinic.eenum.Success
import com.telemedicine.myclinic.models.FuzzySearchMinModel
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO
import com.telemedicine.myclinic.models.appointments.ApptsModel
import com.telemedicine.myclinic.models.homevisit.*
import com.telemedicine.myclinic.myapplication.R
import com.telemedicine.myclinic.util.*
import com.telemedicine.myclinic.views.BoldTextInputLayout
import com.telemedicine.myclinic.webservices.RestClient
import kotlinx.android.synthetic.main.activity_add_new_address.*
import kotlinx.android.synthetic.main.activity_medication_deliveries.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.os.Handler
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class MedicationDeliveriesActivity : BaseActivity(), OnMapReadyCallback {
    var mLocationService: LocationService = LocationService()
    lateinit var mServiceIntent: Intent
    var driverMarker: Marker? = null
    var patientMarker: Marker? = null

    private var medicationDeliveriesList: ArrayList<OrdersListResponse> = ArrayList()
    private lateinit var medicationDeliveriesAdapter: MedicationDeliveriesAdapter
    private var mMap: GoogleMap? = null
    private lateinit var driversLatLng: LatLng
    private val handler = Handler()
    private var currentOrderId: String? = null

    private lateinit var medicationAdapter: MedicineListAdapter
    private var medicationList: ArrayList<MedicineList>? = ArrayList()
    private lateinit var requestQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        manage_address_Book_btn.setOnClickListener {
            val intent =
                Intent(this@MedicationDeliveriesActivity, MyAddressBookActivity::class.java)
            startActivity(intent)
        }

        getOrdersByPatientId()

    }


    private fun init() {
        initRecyclerView()
        initGoogleMap()
        initMedicationDialog()
        requestQueue = Volley.newRequestQueue(this)
    }

    override fun layout(): Int {
        return R.layout.activity_medication_deliveries
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(appointmentEvent: AppointmentEvent) {
        if (appointmentEvent.doctorNameEn != "") {
            if (TextUtil.getArabic(this)) {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@MedicationDeliveriesActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameAr + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            } else {
                ErrorMessage.getInstance().showSuccessGreen(
                    this@MedicationDeliveriesActivity,
                    getString(R.string.check_in_available_text) + " " + appointmentEvent.doctorNameEn + ". " + getString(
                        R.string.please_press_to_continue
                    )
                )
            }
        } else {
            ErrorMessage.getInstance()
                .showErrorYellow(this@MedicationDeliveriesActivity, appointmentEvent.errorMSg)
        }
    }
    private fun initRecyclerView() {
        medicationDeliveriesAdapter = MedicationDeliveriesAdapter(
            this,
            medicationDeliveriesList,
            object : MedicationDeliveriesAdapter.ClickItemListener {
                override fun onClicked(position: Int, model: OrdersListResponse) {
                    showMedicationsDialog(model)
                    if(model.orderStatus== 3){
                        setUpMapIfNeeded()
                        startTheService()
                        //driverTrackGet("22")


                        currentOrderId = model.orderId.toString()
                        startDriverTrackRunnable()
                        //Toast.makeText(this@MedicationDeliveriesActivity, "The Status is enroute",Toast.LENGTH_LONG).show()
                    }else{
                        hidemap()
                        stopDriverTrackRunnable()
                    }
                }
            })

        address_book_rv!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        address_book_rv!!.adapter = medicationDeliveriesAdapter
    }

    private fun showMedicationsDialog(model: OrdersListResponse) {
        if (model.medicine != null) {
            medicationList?.clear()
            medicationList?.addAll(model.medicine)
            medicationAdapter.notifyDataSetChanged()
        }
        dialog!!.show()
    }

    companion object {
        public const val MY_FINE_LOCATION_REQUEST = 99
        public const val MY_BACKGROUND_LOCATION_REQUEST = 100
    }
    private fun hidemap(){
        val map = findViewById<FragmentContainerView>(R.id.map)
        map.visibility = View.INVISIBLE
    }

    private fun setUpMapIfNeeded() {
        val map = findViewById<FragmentContainerView>(R.id.map)
        map.visibility = View.VISIBLE

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }

    private fun getOrdersByPatientId() {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0L) {


                val getOrdersByPatientIdDTO =
                    GetOrdersByPatientIdDTO(securityToken, patientId.toString())
                RestClient.getInstance().getOrdersbyPatientId(
                    getOrdersByPatientIdDTO
                ) { result, status, errorMessage ->
                    hideWaitDialog()
                    val orderListByPatientIdResponse = result as OrderListByPatientIdResponse
                    if (orderListByPatientIdResponse != null) {
                        val mobileOpResult = orderListByPatientIdResponse.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                val orderList = orderListByPatientIdResponse.ordersList
                                if (!ValidationHelper.isNullOrEmpty(orderList)) {
                                    loadOrderList(orderList)
                                }
                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@MedicationDeliveriesActivity)) errorMesg =
                                        """
     ${mobileOpResult.businessErrorMessageEn}
     
     """.trimIndent() else if (TextUtil.getArabic(
                                            this@MedicationDeliveriesActivity
                                        )
                                    ) errorMesg = """
     ${mobileOpResult.businessErrorMessageAr}
     
     """.trimIndent()
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                ${errorMesg}Technical Info : 
                                ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance()
                                    .showError(this@MedicationDeliveriesActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance()
                                .showError(this@MedicationDeliveriesActivity, errorMessage)
                        }
                    } else {
                        ErrorMessage.getInstance()
                            .showError(this@MedicationDeliveriesActivity, errorMessage)
                    }
                }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }

    private fun loadOrderList(orderList: List<OrdersListResponse>) {
        medicationDeliveriesList.clear()
        medicationDeliveriesList.addAll(orderList)
        medicationDeliveriesAdapter.notifyDataSetChanged()
    }


    private fun driverTrackGet(orderId: String) {
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog()

            val securityToken = TinyDB(this).getString(Const.TOKEN_KEY)
            val patientId = TinyDB(this).getLong(Const.PATIENT_ID, 0)

            val getDriverTrackDTO =
                GetDriverTrackDTO(securityToken, orderId.toString())

            if (!ValidationHelper.isNullOrEmpty(securityToken) && patientId != 0L) {
                RestClient.getInstance().driverTrackGet(getDriverTrackDTO) { result, status, errorMessage ->
                    hideWaitDialog()

                    val driverTrackResponse = result as GetDriverTrackResponse
                    if (driverTrackResponse != null) {
                        val mobileOpResult = driverTrackResponse.mobileOpResult
                        if (mobileOpResult != null) {
                            if (mobileOpResult.result == Success.SUCCESSCODE.value) {
                                val driverTrack = driverTrackResponse.getDriverTrack()
                                if (driverTrack != null) {
                                    val driverLat = driverTrack.getDriverLat()
                                    val driverLong = driverTrack.getDriverLong()
                                    driversLatLng = LatLng(driverLat, driverLong)
                                    val toastMessage = "Driver Location - Lat: $driverLat, Long: $driverLong"
                                   // Toast.makeText(this@MedicationDeliveriesActivity, toastMessage, Toast.LENGTH_LONG).show()
                                }else{

                                }

                            } else {
                                var errorMesg = ""
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.businessErrorMessageEn)) {
                                    if (TextUtil.getEnglish(this@MedicationDeliveriesActivity)) {
                                        errorMesg = """
                                        ${mobileOpResult.businessErrorMessageEn}
                                        
                                    """.trimIndent()
                                    } else if (TextUtil.getArabic(this@MedicationDeliveriesActivity)) {
                                        errorMesg = """
                                        ${mobileOpResult.businessErrorMessageAr}
                                        
                                    """.trimIndent()
                                    }
                                }
                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.technicalErrorMessage)) {
                                    errorMesg = """
                                    ${errorMesg}Technical Info : 
                                    ${mobileOpResult.technicalErrorMessage}
                                """.trimIndent()
                                }
                                ErrorMessage.getInstance().showError(this@MedicationDeliveriesActivity, errorMesg)
                            }
                        } else {
                            ErrorMessage.getInstance().showError(this@MedicationDeliveriesActivity, errorMessage)
                        }
                    } else {
                        ErrorMessage.getInstance().showError(this@MedicationDeliveriesActivity, errorMessage)
                    }
                }
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection))
        }
    }

    private fun initGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private var dialog: Dialog? = null

    private fun initMedicationDialog() {
        dialog = Dialog(this, R.style.ios_dialog_style)
        dialog?.setContentView(R.layout.dialog_medications_deliveries)
        dialog?.setCancelable(false)

        val medicationRecyclerView: RecyclerView = dialog?.findViewById(R.id.medication_recycler)!!
        val cancelBtn: AppCompatTextView = dialog?.findViewById(R.id.cancelBtn)!!

        medicationAdapter = MedicineListAdapter(medicationList, this)
        medicationRecyclerView.layoutManager = LinearLayoutManager(this)
        medicationRecyclerView.adapter = medicationAdapter

        cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            MY_BACKGROUND_LOCATION_REQUEST
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
            //val driversLatLng = LatLng(33.7296,73.0368)

            // sourceMarker!!.position = latlng!!
            // sourceMarker.title = address


            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11f))

            // Draw a Polyline between patientMarker and driverMarker
            drawPathBetweenMarkers(latlng, driversLatLng)


            val intents = Intent(this, AddressFetchService::class.java)
            //  enqueueWork(this, intents)
        } else {
            if (locationEvent.address.isNotEmpty()) {
                member_ship_no.setText(locationEvent.address[0].getAddressLine(0).toString())
            }
        }


    }

    private val driverTrackRunnable = object : Runnable {
        override fun run() {
            currentOrderId?.let { orderId ->
                driverTrackGet(orderId)
            }
            handler.postDelayed(this, 3000)
        }
    }

    private fun startDriverTrackRunnable() {
        handler.postDelayed(driverTrackRunnable, 3000)
    }

    private fun stopDriverTrackRunnable() {
        handler.removeCallbacks(driverTrackRunnable)
    }




    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        startDriverTrackRunnable()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        stopDriverTrackRunnable()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServiceFunc()
        stopDriverTrackRunnable()
    }

    private fun drawPathBetweenMarkers(startPosition: LatLng, endPosition: LatLng) {
        mMap?.clear()

        patientMarker = mMap!!.addMarker(
            MarkerOptions().position(startPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_map))
        )

        driverMarker = mMap!!.addMarker(
            MarkerOptions().position(endPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.motorbike))
        )


        fetchMapData(startPosition,endPosition)

    }

    fun fetchMapData(pickupCoordinate: LatLng, destinationCoordinate: LatLng) {
        val origin = "${pickupCoordinate.latitude},${pickupCoordinate.longitude}"
        val destination = "${destinationCoordinate.latitude},${destinationCoordinate.longitude}"
        val apiKey = "AIzaSyAv-3OjerSZpRFmOlk6GFzumrM04X4vUfU" //place api key here

        val directionURL =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&mode=driving&key=$apiKey"

        val request = JsonObjectRequest(Request.Method.GET, directionURL, null,
            { response ->
                val mapResponse = response as JSONObject

                val routesArray = mapResponse.optJSONArray("routes") ?: JSONArray()
                val routes = routesArray.optJSONObject(0) ?: JSONObject()

                val legs = routes.optJSONArray("legs") ?: JSONArray()
                val legs1 = legs.optJSONObject(0) ?: JSONObject()
                val duration1 = legs1.optJSONObject("duration") ?: JSONObject()
                val text = duration1.optString("text", "")
                println(text)

                val overviewPolyline = routes.optJSONObject("overview_polyline") ?: JSONObject()
                val polypoints = overviewPolyline.optString("points", "")
                val path = PolyUtil.decode(polypoints)
                if (path.isNotEmpty()) {
                        val polyline = PolylineOptions()
                            .addAll(path)
                        mMap?.addPolyline(polyline)
                }
            },
            { error ->
                error.printStackTrace()
            })

        requestQueue.add(request)
    }


}