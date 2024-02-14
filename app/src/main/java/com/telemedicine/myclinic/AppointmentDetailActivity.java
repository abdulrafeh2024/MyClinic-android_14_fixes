package com.telemedicine.myclinic;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.LocationSinglton.FusedLocationSingleton;
import com.telemedicine.myclinic.activities.AddInsuranceActivity;
import com.telemedicine.myclinic.activities.BaseActivity;
import com.telemedicine.myclinic.activities.BoardingPassActivity;
import com.telemedicine.myclinic.activities.BookAppointmentFourActivity;
import com.telemedicine.myclinic.activities.BookAppointmentThreeActivity;
import com.telemedicine.myclinic.activities.BookAppointmentTwoActivity;
import com.telemedicine.myclinic.activities.CardListActivity;
import com.telemedicine.myclinic.activities.InsuranceCardCashCardActivity;
import com.telemedicine.myclinic.activities.ReScheduleApptActivity;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.ApptValidationForCheckinDTO;
import com.telemedicine.myclinic.models.ApptValidationResponseModel;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.UpdateInsuranceDTO;
import com.telemedicine.myclinic.models.appointments.ApptModel;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptPaymentDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.cancelAppointment.ApptCancelDTO;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.onlineConfig.OpsConfigsModel;
import com.telemedicine.myclinic.models.patientMiniModels.InsuranceCardModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneDTO;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.PatientRegistrationUpdate;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.APPOINTMENT_LIST;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.INSURANCE_IMAGES;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PATIENT_UPDATE_KEY;
import static com.telemedicine.myclinic.util.Const.SECONDARY_APPT_REFRESH;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AppointmentDetailActivity extends BaseActivity {

    @BindView(R.id.pay_online)
    LightButton payOnline;

    @BindView(R.id.self_checkin)
    LightButton selfCheckin;

    @BindView(R.id.add_calendar)
    LightButton add_calendar;

    @BindView(R.id.reschedule)
    LightButton reschedule;

    @BindView(R.id.self_check_in_loading)
    View self_check_in_loading;

    @BindView(R.id.self_checkin_progress)
    ProgressBar self_checkin_progress;

    @BindView(R.id.changeInsurance)
    LightButton changeInsurance;

    @BindView(R.id.cancel)
    LightButton cancel;

    @BindView(R.id.apptTime)
    RegularTextView apptTime;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.doctor_name)
    BoldTextView doctorName;

    @BindView(R.id.doctor_profession)
    LightTextView doctorProfession;

    @BindView(R.id.dateTime)
    LightTextView dateTime;

    @BindView(R.id.visit_reception)
    LightTextView visit_reception;

    @BindView(R.id.visit_reception_error_tv)
    LightTextView visit_reception_error_tv;

    @BindView(R.id.visit_reception_layout)
    ConstraintLayout visit_reception_layout;

    @BindView(R.id.container_buttons)
    RelativeLayout container_buttons;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.company_name)
    TextView companyName;

    boolean isPaid = false;

    ApptsMiniModel apptsMiniModel = null;

    ApptModel apptDetailModel = null;

    int ordersRadPostion = 0;

    boolean apptGetTotalOutstandingCalled = false;
    private double ammountDue = 0.0;

    TinyDB tinyDB = null;

    DoctorsProfile doctorsProfile;

    boolean isSecondary = false;
    boolean isInsurance = false;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;
    TimeAgoMessages messages = null;

    String doctrNameStr = "";
    String doctrProfession = "";

    boolean isTentative = false;
    private final int RC_RESCHEDULED = 34322;

    private static final long UPDATE_INTERVAL_IN_MIL = 1000;
    private static final long FASTES_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL / 2;

    //location
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler serviceHandler;
    private Location mLocation;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLocation();
        // Check GPS is enabled

        getValue();
        transformViews();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dialogGPSEnable(getString(R.string.please_enable_location_to_confirm));
        }

      //  getPatientData(true, false);
    }

    private void initializeLocation() {
        mLocation = new Location("Location");
        mLastLocation = new Location("Last Location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                hideWaitDialog();
                if (locationRequested) {
                    locationRequested = false;
                   // Toast.makeText(AppointmentDetailActivity.this, "locaiton updates received", Toast.LENGTH_LONG).show();
                    onNewLocation(locationResult.getLastLocation());
                }
                handler.removeCallbacks(runnable);
                handler = null;
                runnable = null;
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        createLocationRequest();

    }

    boolean locationRequested = false;
    Handler handler = null;
    private Runnable runnable;

    public void requestLocationUpdates() {
        try {
            showWaitDialog();
            locationRequested = true;
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            handler = new Handler(Looper.getMainLooper());
            runnable = () -> {
                if (locationRequested) {
                    getLastLocation();
                }
            };

            if (handler == null || runnable == null) {
                return;
            }
            handler.postDelayed(runnable, 10000);

        } catch (SecurityException e) {
            hideWaitDialog();
            Log.e("result", "Lost location request");
        }
    }

    private void getLastLocation() {

       // Toast.makeText(AppointmentDetailActivity.this, "getting Last Location", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            hideWaitDialog();
            checkLocationPermission();
            return;
        }

        Task<Location> fusedLastLocation = fusedLocationProviderClient.getLastLocation();

        if (fusedLastLocation != null) {
            locationRequested = false;
           // Toast.makeText(AppointmentDetailActivity.this, "fused Last Location called", Toast.LENGTH_LONG).show();
            fusedLastLocation.addOnCompleteListener(this, new OnCompleteListener<Location>() {
               @Override
               public void onComplete(@NonNull Task<Location> task) {
                   hideWaitDialog();
                   if(task.getResult() != null){
                     //  Toast.makeText(AppointmentDetailActivity.this, "fused Last Location Received", Toast.LENGTH_LONG).show();
                       onNewLocation(task.getResult());
                   }
               }
           }).addOnFailureListener(this, new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   hideWaitDialog();
               }
           });
           //onNewLocation(fusedLastLocation.getResult());
            // fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void onNewLocation(Location lastLocation) {

      //  Toast.makeText(this, lastLocation.getLatitude()+"on New Locaiton", Toast.LENGTH_LONG).show();
        if (apptsMiniModel.getCompany().equals("nc01")) {
            mLocation.setLatitude(21.658793);
            mLocation.setLongitude(39.1225838);

            if (lastLocation.distanceTo(mLocation) < 400) {
               // validationofCheckIn();
                apptForCheckIn();
            } else {
                dialogLocationFarAway();
            }
            //  (lastLocation.distanceTo(mLocation));

        } else if (apptsMiniModel.getCompany().equals("safa")) {
            mLocation.setLatitude(21.575778);
            mLocation.setLongitude(39.199847);
            if (lastLocation.distanceTo(mLocation) < 400) {
                apptForCheckIn();
               // validationofCheckIn();
            } else {
                dialogLocationFarAway();
            }

        } else {

            mLocation.setLatitude(22.839535);
            mLocation.setLongitude(38.924132);

            if (lastLocation.distanceTo(mLocation) < 400) {
              //  validationofCheckIn();
                apptForCheckIn();
            } else {
                dialogLocationFarAway();
            }
        }

    }

    private void selfCheckInEligibility(){

         if(LocaleDateHelper.expiredAptTime(apptsMiniModel.getApptDate())){
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.check_in_period_expired);
            hideCheckinViews();
            return;
        }
        else if (!LocaleDateHelper.isSelfCheckingTimeStart(apptsMiniModel.getApptDate())) {
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.one_hour_wait_message);
            hideCheckinViews();
            return;
        }
        else if(isAppointmentValidated()){
            visit_reception_layout.setVisibility(View.GONE);
            showSelfCheckIn();
            return;
        }
        else {
            hideCheckinViews();
            visit_reception_layout.setVisibility(View.VISIBLE);
            visit_reception.setText(R.string.eligibility_check_message);
            checkServerValidation(apptsMiniModel);
        }
    }

    private void showSelfCheckIn() {
        self_check_in_loading.setVisibility(View.GONE);
        self_checkin_progress.setVisibility(View.GONE);
        selfCheckin.setVisibility(View.VISIBLE);
        payOnline.setVisibility(View.VISIBLE);
        payOnline.setText(getString(R.string.check_in_online));
    }

    private boolean isAppointmentValidated(){
        if (!tinyDB.getListAppointment(Const.APPOINTMENT_LIST).isEmpty()) {
            for (ApptsMiniModel apptLocalModel: tinyDB.getListAppointment(Const.APPOINTMENT_LIST)) {
                if (apptLocalModel.getApptId() == apptsMiniModel.getApptId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void hideCheckinViews() {
        self_check_in_loading.setVisibility(View.GONE);
        self_checkin_progress.setVisibility(View.GONE);
        selfCheckin.setVisibility(View.GONE);
        payOnline.setVisibility(View.GONE);
        payOnline.setText(getString(R.string.check_in_online));
    }

    private void validationofCheckIn() {

        //checkServerValidation(apptsMiniModel);

        if (ConnectionUtil.isInetAvailable(this)) {
            //showWaitDialog();

            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            ApptValidationForCheckinDTO apptValidationForCheckinDTO =
                    new ApptValidationForCheckinDTO(securityToken, apptsMiniModel.getApptId(), companyId);

            RestClient.getInstance().apptValidationForChecking(apptValidationForCheckinDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ApptValidationResponseModel apptsMiniModelLoc = (ApptValidationResponseModel) result;

                    if (apptsMiniModelLoc != null) {

                        MobileOpResult mobileOpResult = apptsMiniModelLoc.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                               // apptForCheckIn();
                                // dialogSuccessCheckin();

                                self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.VISIBLE);
                                payOnline.setVisibility(View.VISIBLE);
                                payOnline.setText(getString(R.string.check_in_online));

                            } else {

                                self_check_in_loading.setVisibility(View.GONE);
                                self_checkin_progress.setVisibility(View.GONE);
                                selfCheckin.setVisibility(View.GONE);
                                payOnline.setVisibility(View.GONE);

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(AppointmentDetailActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                }

                               // ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                        }
                    } else {

                        ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void apptForCheckIn() {
      //  Toast.makeText(AppointmentDetailActivity.this, "appt for checkin called", Toast.LENGTH_LONG).show();
        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);

            ApptValidationForCheckinDTO apptValidationForCheckinDTO =
                    new ApptValidationForCheckinDTO(securityToken, apptsMiniModel.getApptId(), companyId);

            RestClient.getInstance().apptChecking(apptValidationForCheckinDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                            dialogSuccessCheckin();

                        } else {

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);

                        }

                    } else {

                        ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    IOSDialog locationFarAwayDialog = null;

    private void dialogLocationFarAway() {
        locationFarAwayDialog = TextUtil.dialgoue(AppointmentDetailActivity.this,
                getString(R.string.you_cannot_check_in), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            locationFarAwayDialog.dismiss();
                        } catch (android.content.ActivityNotFoundException exception) {
                            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }
                    }
                });
    }

    IOSDialog checkSuccessDialog = null;

    private void dialogSuccessCheckin() {
        checkSuccessDialog = TextUtil.dialgoue(AppointmentDetailActivity.this,
                getString(R.string.You_have_successfully_CheckedIn), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            checkSuccessDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        } catch (android.content.ActivityNotFoundException exception) {
                            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }
                    }
                });
    }

    public void createLocationRequest() {
        locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MIL);
        locationRequest.setFastestInterval(FASTES_UPDATE_INTERVAL_IN_MIL);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected int layout() {
        return R.layout.activity_appointment_detail;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppointmentEvent appointmentEvent) {
        if (!appointmentEvent.getDoctorNameEn().equals("")) {
            if(TextUtil.getArabic(this)){
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(AppointmentDetailActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ".");
            }else{
                ErrorMessage.getInstance().showSuccessWithoutNavigationGreen(AppointmentDetailActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ".");
            }

            visit_reception_layout.setVisibility(View.GONE);
            visit_reception_error_tv.setVisibility(View.GONE);
            showSelfCheckIn();
        } else {
            ErrorMessage.getInstance().showErrorYellow(AppointmentDetailActivity.this, appointmentEvent.getErrorMSg());
            visit_reception_layout.setVisibility(View.GONE);
            visit_reception_error_tv.setVisibility(View.VISIBLE);
        }
    }
    void getValue() {

        tinyDB = new TinyDB(this);
        isTentative = tinyDB.getBoolean(Const.IS_TENT);
        if (TextUtil.getEnglish(this)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            if (!isTelemedcine) {
                companyName.setText(tinyDB.getString(COMPANY_NAME_EN));
            }
        } else if (TextUtil.getArabic(this)) {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("ar");
            messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            if (!isTelemedcine) {
                companyName.setText(tinyDB.getString(COMPANY_NAME_AR));
            }
        }


        Intent intent = getIntent();

        if (intent != null) {
           //apptsMiniModel = intent.getParcelableExtra(Const.APPTMODEL);
            String apptsMiniModelJson =intent.getStringExtra(Const.APPTMODEL);
            String apptDetailModelJson =intent.getStringExtra(Const.APPTDETAILMODEL);
            apptsMiniModel = new GsonBuilder().create().fromJson(apptsMiniModelJson, ApptsMiniModel.class);
            apptDetailModel = new GsonBuilder().create().fromJson(apptDetailModelJson, ApptModel.class);
          //  apptDetailModel = intent.getParcelableExtra(Const.APPTDETAILMODEL);
            ordersRadPostion = intent.getIntExtra(Const.ORDER_RAD_POSITION, 0);
            showData(apptsMiniModel, apptDetailModel);
        }

        dateListener = new DatePickerDialog.OnDateSetListener() {
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
    }

    String date;
    String exDate = "";

    String specialityId = "";
    long apptBookingType = 0;

    boolean isTelemedcine = false;

    private void showData(ApptsMiniModel apptsMiniModel, ApptModel apptDetailModel) {


        int apptStatus = 0;
        if (apptsMiniModel != null) {
            if (apptsMiniModel.getDoctorProfile().getSpecialtyId() != null) {
                specialityId = apptsMiniModel.getDoctorProfile().getSpecialtyId();
            }

            apptBookingType = apptsMiniModel.getApptBookType();


            isPaid = apptsMiniModel.isPaid();
            isTelemedcine = apptsMiniModel.isTelemedicine();
            apptStatus = apptsMiniModel.getApptStatus();
            if (apptsMiniModel.getRadOrder() != null) {
                isSecondary = true;
                ordersRadModel = new OrdersRadModel();
                ordersRadModel.setApptId(apptsMiniModel.getRadOrder().getApptId());
                ordersRadModel.setDeviceId(apptsMiniModel.getRadOrder().getDeviceId());
                ordersRadModel.setDeviceName(apptsMiniModel.getRadOrder().getDeviceName());
                ordersRadModel.setId(apptsMiniModel.getRadOrder().getId());
                ordersRadModel.setItemCode(apptsMiniModel.getRadOrder().getItemCode());
                if (!ValidationHelper.isNullOrEmpty(apptsMiniModel.getRadOrder().getOrderStatus()))
                    ordersRadModel.setOrderStatus(Long.parseLong(apptsMiniModel.getRadOrder().getOrderStatus()));
                ordersRadModel.setOrderDate(apptsMiniModel.getRadOrder().getOrderDate());
                ordersRadModel.setName(apptsMiniModel.getRadOrder().getName());
            } else {
                isSecondary = false;
            }

        } else if (apptDetailModel != null) {
            if (apptDetailModel.getDoctorProfile().getSpecialtyId() != null) {
                specialityId = apptDetailModel.getDoctorProfile().getSpecialtyId();
            }

            apptBookingType = apptsMiniModel.getApptBookType();

            apptStatus = apptDetailModel.getApptStatus();
            isSecondary = true;
            isPaid = apptDetailModel.isPaid();
            isTelemedcine = apptDetailModel.isTelemedicine();
        }

        apptStatusUI(apptStatus);

        if (isPaid) {

            if (!isTelemedcine) {
                self_check_in_loading.setVisibility(View.VISIBLE);
                self_checkin_progress.setVisibility(View.VISIBLE);
                validationofCheckIn();
                //selfCheckInEligibility();
                //selfCheckin.setVisibility(View.VISIBLE);
            } else {
                selfCheckin.setVisibility(View.GONE);
            }

            changeInsurance.setVisibility(View.GONE);
            payOnline.setText(getString(R.string.check_in_online));
            String mrnNo = tinyDB.getString(Const.MRN_NO);

            if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0")) {
                //for tentative patient hide self check and pay online button
                selfCheckin.setVisibility(View.GONE);
                payOnline.setVisibility(View.GONE);
                changeInsurance.setVisibility(View.GONE);
            }

            if (isTelemedcine) {
                payOnline.setVisibility(View.VISIBLE);
            } else {
                String spec = tinyDB.getString(DOCTOR_SPECIALITY);
                if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0") ||
                        spec.equals("Dental") || spec.equals("قسم طب الأسنان") || spec.equals("Dentist") || spec.equals("دكتورالاسنان")) {
                    payOnline.setVisibility(View.GONE);
                }
            }
        } else {
            selfCheckin.setVisibility(View.GONE);
            if (!isTentative) {
                changeInsurance.setVisibility(View.VISIBLE);
            } else {
                changeInsurance.setVisibility(View.GONE);
            }

            if (apptsMiniModel != null) {
                if (apptsMiniModel.getRadOrder() != null) {
                    apptGetTotalOutstanding(apptsMiniModel.getRadOrder().getApptId(), apptsMiniModel.isTelemedicine(), false);
                } else {
                    apptGetTotalOutstanding(apptsMiniModel.getApptId(), apptsMiniModel.isTelemedicine(), false);
                }

            } else if (apptDetailModel != null)
                apptGetTotalOutstanding(apptDetailModel.getApptId(), apptDetailModel.isTelemedicine(), false);

            payOnline.setText(getString(R.string.pay_online));

        }


        if (apptsMiniModel != null)
            doctorsProfile = apptsMiniModel.getDoctorProfile();

        else if (apptDetailModel != null)
            doctorsProfile = apptDetailModel.getDoctorProfile();

        if (doctorsProfile != null) {

            if (TextUtil.getEnglish(this)) {
                doctrNameStr = doctorsProfile.getNameEn();
                doctrProfession = doctorsProfile.getSpecialtyEn();
            } else if (TextUtil.getArabic(this)) {
                doctrNameStr = doctorsProfile.getNameAr();
                doctrProfession = doctorsProfile.getSpecialtyAr();
            }

            String apptDate = "";

            if (apptsMiniModel != null)
                apptDate = apptsMiniModel.getApptDate();
            else if (apptDetailModel != null)
                apptDate = apptDetailModel.getApptDate();

            String doctrUrl = doctorsProfile.getProfilePicUrl();

            if (!ValidationHelper.isNullOrEmpty(doctrNameStr))
                doctorName.setText(doctrNameStr);

            if (!ValidationHelper.isNullOrEmpty(doctrProfession))
                doctorProfession.setText(doctrProfession);

            if (apptsMiniModel != null) {

                if (apptsMiniModel.getApptBookType() == 10 || apptsMiniModel.getApptBookType() == 20) {
                    if (!ValidationHelper.isNullOrEmpty(doctrUrl)) {
                        Glide.with(this).load(doctrUrl).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage);
                    }
                } else if (apptsMiniModel.getApptBookType() == 30) {
                    Glide.with(this).load(R.drawable.xray).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage);
                }
            } else if (apptDetailModel != null) {
                if (apptDetailModel.getApptBookType() == 10 || apptDetailModel.getApptBookType() == 20) {
                    if (!ValidationHelper.isNullOrEmpty(doctrUrl)) {
                        Glide.with(this).load(doctrUrl).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage);
                    }
                } else if (apptDetailModel.getApptBookType() == 30) {
                    Glide.with(this).load(R.drawable.xray).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage);
                }
            }

            if (!ValidationHelper.isNullOrEmpty(apptDate)) {

                exDate = apptDate;
                date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm a");
                dateTime.setText(date);

                long dateLong = LocaleDateHelper.convertDateFormat(date, "EEEE, dd MMMM yyyy hh:mm a");
                String dateAgo = TimeAgo.using(dateLong, messages);

                String dateIn = dateAgo.replaceAll("within", getString(R.string.in));

                if (!ValidationHelper.isNullOrEmpty(dateIn)) {

                    String days = LocaleDateHelper.getCountOfDays("", apptDate);

                    if (days.equalsIgnoreCase("0")) {
                        apptTime.setText(R.string.today);
                    } else if (days.equalsIgnoreCase("1")) {
                        apptTime.setText(R.string.tomorrow);
                    } /*else if (days.equalsIgnoreCase("2")) {
                        apptTime.setText(R.string.in_2_days);
                    }*/ else {
                        int d = 0;
                        if (!ValidationHelper.isNullOrEmpty(days))
                            d = Integer.valueOf(days);

                        if (d > 1) {
                            d = d - 1;

                            String gy = getString(R.string.in) + " " + d + " " + getString(R.string.days);
                            apptTime.setText(gy);
                        } else apptTime.setText(dateIn);
                    }

                }
            }
        }

    }

    OrdersRadModel ordersRadModel = null;

    private void updateDOB() {


        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String date = sdf.format(myCalendar.getTime());

        String docName = "";
        String speciality = "";
        boolean isTele = false;
        long apptId = 0;

        if (apptsMiniModel != null && doctorsProfile != null) {

            if (TextUtil.getEnglish(this)) {
                docName = doctorsProfile.getNameEn();
                speciality = doctorsProfile.getSpecialtyEn();
            } else if (TextUtil.getArabic(this)) {
                docName = doctorsProfile.getNameAr();
                speciality = doctorsProfile.getSpecialtyAr();
            }

            isTele = apptsMiniModel.isTelemedicine();

            if (apptsMiniModel.getRadOrder() != null) {
                apptId = apptsMiniModel.getRadOrder().getApptId();
            } else {
                apptId = apptsMiniModel.getApptId();
            }

            isInsurance = apptsMiniModel.isInsurance();
        } else if (apptDetailModel != null && doctorsProfile != null) {

            if (TextUtil.getEnglish(this)) {
                docName = doctorsProfile.getNameEn();
                speciality = doctorsProfile.getSpecialtyEn();
            } else if (TextUtil.getArabic(this)) {
                docName = doctorsProfile.getNameAr();
                speciality = doctorsProfile.getSpecialtyAr();
            }

            isTele = apptDetailModel.isTelemedicine();
            apptId = apptDetailModel.getApptId();
            // XRAY- SKULL 1 VIEW (LAT)

            if (!ValidationHelper.isNullOrEmpty(apptDetailModel.getOrdersRad()))
                ordersRadModel = apptDetailModel.getOrdersRad().get(0);

            isInsurance = apptDetailModel.isInsurance();
        }


        Intent intent = new Intent(this, BookAppointmentTwoActivity.class);
        intent.putExtra(DOCTOR_NAME, docName);
        intent.putExtra(DOCTOR_SPECIALITY, speciality);
        intent.putExtra(DATE, date);
        intent.putExtra(SPECIALITY_ID, doctorsProfile.getSpecialtyId());
        intent.putExtra(DOCTOR_ID, String.valueOf(doctorsProfile.getDoctorId()));
        intent.putExtra(ISTELEMEDICINE_KEY, isTele);
        intent.putExtra(DOCTOR_IMAGE_URL, doctorsProfile.getProfilePicUrl());
        intent.putExtra(Const.APPT_ID, apptId);
        intent.putExtra(Const.IS_INSURANCE_KEY, isInsurance);
        intent.putExtra(Const.IS_SECONDARY, isSecondary);
        intent.putExtra(Const.ORDER_RAD, ordersRadModel);
        startActivityForResult(intent, 111);
    }

    @OnClick(R.id.self_checkin)
    void selfCheckin() {
        ArrayList<Object> configsModels = tinyDB.getListObject(Const.ONLINE_CONFIG_KEY, OpsConfigsModel.class);

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);
        //dialogSuccessCheckin();
    //   validationofCheckIn();
     //  apptForCheckIn();

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dialogGPSEnable(getString(R.string.Kindly_enable_your_location_to_conitinue));
            return;
        }

        checkLocationPermission();
    }

    IOSDialog gpdEnaleDialog = null;

    private void dialogGPSEnable(String msg) {

        gpdEnaleDialog = TextUtil.dialgoue(AppointmentDetailActivity.this,
                msg, getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            gpdEnaleDialog.dismiss();
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }
                    }
                });
    }

    IOSDialog permissionEnaleDialog = null;

    private void dialogPermissionEnable(String msg) {

        permissionEnaleDialog = TextUtil.dialgoue(AppointmentDetailActivity.this,
                msg, getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            permissionEnaleDialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }

                    }
                });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {

        boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
        boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

        if (!accessCourseLocation || !accessFineLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            requestLocationUpdates();
            return true;
        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED){


         /*   // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showLocationDisabledDialog();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }*/
       /*     return false;
        } else {
            requestLocationUpdates();
            return true;
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        requestLocationUpdates();
                        /*LocalBroadcastManager.getInstance(AppointmentDetailActivity.this).registerReceiver(mMessageReceiver,
                                new IntentFilter("location"));

                        FusedLocationSingleton.getInstance().startLocationUpdates();*/
                    } else {
                        requestLocationUpdates();
                    }

                } else {
                    dialogPermissionEnable(getString(R.string.please_enable_location_to_confirm));


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    IOSDialog locationDisabledDialog = null;

    private void showLocationDisabledDialog() {
        locationDisabledDialog = TextUtil.dialgoue(AppointmentDetailActivity.this,
                getString(R.string.Kindly_enable_your_location_to_conitinue), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            locationDisabledDialog.dismiss();
                            ActivityCompat.requestPermissions(AppointmentDetailActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        } catch (android.content.ActivityNotFoundException exception) {
                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.myclinic.ksa")));
                        }

                    }
                });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                Location location = intent.getParcelableExtra("location");

                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                    FusedLocationSingleton.getInstance().stopLocationUpdates();
                    //
                    //locationSet("", latLng);
                }
            }
        }
    };

    private double distance(double lat1, double lon1, double lat2, double
            lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @OnClick(R.id.pay_online)
    void payOnline() {
       // payOnline.setEnabled(false);

       /* if (!isTelemedcine) {
            if (LocaleDateHelper.expiredAptTime(exDate)) {
                showCheckinDialog();
                return;
            }
        }*/

        if (apptsMiniModel != null)
            isPaid = apptsMiniModel.isPaid();
        else if (apptDetailModel != null)
            isPaid = apptDetailModel.isPaid();

        String apptDate = "";
        long apptId = 0;

        if (apptsMiniModel != null) {
            apptDate = apptsMiniModel.getApptDate();
            if (apptsMiniModel.getRadOrder() != null)
                apptId = apptsMiniModel.getRadOrder().getApptId();
            else
                apptId = apptsMiniModel.getApptId();

        } else if (apptDetailModel != null) {
            apptDate = apptDetailModel.getApptDate();
            apptId = apptDetailModel.getApptId();
        }

        if (isPaid || ammountDue < 1) {

            //discuss in meeting with shahbaz and nagwa to comment all the apptpayment from all places except payment stage 2
            if (ammountDue < 1 && apptGetTotalOutstandingCalled) {
                confirmAppointment(apptId, apptDate, false, true);
                return;
            }
            gotoBoardingPass(doctorsProfile, apptDate, apptId);

        } else {
            String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammountDue);

            String mesg = getString(R.string.the_cost_for_this_appt_will_be) + " " + ammount + " " + getString(R.string.sar) + " " + getString(R.string.you_will_need_a_credit_or_debit_card);
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.cancel))
                    .setContentText(mesg).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                        }
                    }).setConfirmText(getString(R.string._continue)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                            if (ConnectionUtil.isInetAvailable(AppointmentDetailActivity.this)) {
                                if (apptsMiniModel != null)
                                    payStageOne(apptsMiniModel.getApptId());
                                else if (apptDetailModel != null)
                                    payStageOne(apptDetailModel.getApptId());

                            } else {
                                ErrorMessage.getInstance().showWarning(AppointmentDetailActivity.this, getString(R.string.internet_connection));
                            }
                        }
                    }).show();
        }
    }

    IOSDialog iosDialog1 = null;

    private void showCheckinDialog() {
        iosDialog1 = TextUtil.dialgoue(AppointmentDetailActivity.this, getString(R.string.check_in_eligible), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog1.dismiss();
            }
        });
    }

    private void gotoBoardingPass(DoctorsProfile doctorsProfile, String apptDate, long apptId) {

        if (!isTelemedcine) {
            if (LocaleDateHelper.expiredAptTime(apptDate)) {
                showCheckinDialog();
                return;
            }
        }

        Intent intent = new Intent(this, BoardingPassActivity.class);
        intent.putExtra(Const.DOC_OBJECT, doctorsProfile);
        intent.putExtra(Const.DATE, apptDate);
        intent.putExtra(Const.APPT_ID, apptId);
        startActivity(intent);
    }

    public void AppointmentDetail(View view) {
    }

    void apptGetTotalOutstanding(long apptId, boolean isTelemedicine, boolean isChangeInsurance) {

        if (ConnectionUtil.isInetAvailable(this)) {
            TinyDB tinyDB = new TinyDB(this);


            showWaitDialog();
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companydId = tinyDB.getString(Const.COMPANY_ID);
            ApptGetTotalOutstandingDTO apptGetTotalOutstandingDTO
                    = new ApptGetTotalOutstandingDTO(securityToken, apptId, isTelemedicine, companydId);
            RestClient.getInstance().apptGetTotalOutstanding(apptGetTotalOutstandingDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    ApptGetTotalOutstandingModel apptGetTotalOutstandingModel = (ApptGetTotalOutstandingModel) result;

                    if (apptGetTotalOutstandingModel != null) {

                        MobileOpResult mobileOpResult = apptGetTotalOutstandingModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                ammountDue = apptGetTotalOutstandingModel.getAmountDue();
                                apptGetTotalOutstandingCalled = true;

                                ammountDue = Math.round(ammountDue);

                                if (ammountDue > 0) {
                                    if (!isTentative) {
                                        changeInsurance.setVisibility(View.VISIBLE);
                                    } else {
                                        changeInsurance.setVisibility(View.GONE);
                                    }

                                    selfCheckin.setVisibility(View.GONE);
                                    payOnline.setText(getString(R.string.pay_online));
                                } else {

                                    if (!isTelemedicine) {
                                        if (!tentativeHasMRN) {
                                            self_check_in_loading.setVisibility(View.VISIBLE);
                                            self_checkin_progress.setVisibility(View.VISIBLE);
                                            validationofCheckIn();
                                           // selfCheckInEligibility();
                                           // selfCheckin.setVisibility(View.VISIBLE);
                                        } else {
                                            selfCheckin.setVisibility(View.GONE);
                                            payOnline.setVisibility(View.GONE);
                                            //commented
                                           // visit_reception.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        selfCheckin.setVisibility(View.GONE);
                                    }
                                    changeInsurance.setVisibility(View.GONE);
                                    payOnline.setText(getString(R.string.check_in_online));
                                    String mrnNo = tinyDB.getString(Const.MRN_NO);

                                    if (isTelemedcine) {
                                        if (!tentativeHasMRN) {
                                            payOnline.setVisibility(View.VISIBLE);
                                        } else {
                                            payOnline.setVisibility(View.GONE);
                                        }
                                        // payOnline.setVisibility(View.VISIBLE);
                                    } else {
                                        String spec = tinyDB.getString(DOCTOR_SPECIALITY);
                                        if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0") ||
                                                spec.equals("Dental") || spec.equals("قسم طب الأسنان") || spec.equals("Dentist") || spec.equals("دكتورالاسنان")) {
                                            payOnline.setVisibility(View.GONE);
                                        }
                                    }

                                    if (isChangeInsurance) {
                                        confirmAppointment(apptId, "", true, false);
                                    }
                                }


                            }else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(AppointmentDetailActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                }

                            }

                        } else
                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }

    }

    void payStageOne(long apptId) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            if (ammountDue > 0) {

                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                String companyId = tinyDB.getString(Const.COMPANY_ID);
                String oregId = tinyDB.getString(OREGID_KEY);
                String apptIdStr = String.valueOf(apptId);
                String ammount = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US)).format(ammountDue);

                PayStageOneDTO payStageOneDTO =
                        new PayStageOneDTO(ammount, customerEmail, apptIdStr, companyId);

                RestClient.getInstance().payStage1(payStageOneDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        PayStageOneModel payStageOneModel = (PayStageOneModel) result;

                        if (payStageOneModel != null) {

                            MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    String checkOutId = payStageOneModel.getResultVal();

                                    //   Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                 /*   Intent intent = new Intent(AppointmentDetailActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", String.valueOf(ammountDue));
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);*/

                                    getCards(apptId, checkOutId);

                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);

                                }

                            } else {

                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                            }

                        } else {

                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    void getCards(long apptId, String checkOutId) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            TinyDB tinyDB = new TinyDB(this);

            String oRegId = tinyDB.getString(OREGID_KEY);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            long OregIdLong = Long.parseLong(oRegId);

            CardDTO cardDTO = new CardDTO(securityToken, OregIdLong);

            RestClient.getInstance().paymentCardById(cardDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    CardsListModel payStageOneModel = (CardsListModel) result;

                    if (payStageOneModel != null) {

                        MobileOpResult mobileOpResult = payStageOneModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                ArrayList<CardModel> cards = payStageOneModel.getCardsModel();

                                if (cards.isEmpty()) {
                                    //   Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", String.valueOf(ammountDue));
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    //  intent.putExtra(SPECIALITY_ID, specialityId);
                                    //  intent.putExtra(DATE, date);
                                    //  intent.putExtra(DOCTOR_ID,String.valueOf( doctorsProfile.getDoctorId()));
                                    // intent.putExtra(Const.APPT_BOOK_TYPE, apptBookingType);
                                    // intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                } else {
                                    //   Intent intent = new Intent(AppointmentDetailActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(AppointmentDetailActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", String.valueOf(ammountDue));
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    //   intent.putExtra(DATE, date);
                                    //   intent.putExtra(SPECIALITY_ID, specialityId);
                                    //   intent.putExtra(DOCTOR_ID, String.valueOf(doctorsProfile.getDoctorId()));
                                    //  intent.putExtra(Const.APPT_BOOK_TYPE, apptBookingType);
                                    //  intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 500 && resultCode == RESULT_OK) {
            if (data != null) {
                boolean isPaid = data.getBooleanExtra(Const.CONFIRM, true);
                long apptId = data.getLongExtra(Const.APPT_ID, 0);

                if (apptsMiniModel != null)
                    apptsMiniModel.setPaid(isPaid);
                else if (apptDetailModel != null)
                    apptDetailModel.setPaid(isPaid);

                if (isPaid) {
                    changeInsurance.setVisibility(View.GONE);
                    if (!apptsMiniModel.isTelemedicine()) {
                       /* if(!tentativeHasMRN){
                            selfCheckin.setVisibility(View.VISIBLE);
                        }else{
                            selfCheckin.setVisibility(View.GONE);
                            visit_reception.setVisibility(View.VISIBLE);
                        }*/
                        payOnline.setText(getString(R.string.check_in_online));
                        //getPatientData(true, true);
                        //selfCheckInEligibility();
                        self_check_in_loading.setVisibility(View.VISIBLE);
                        self_checkin_progress.setVisibility(View.VISIBLE);
                        validationofCheckIn();
                        // selfCheckin.setVisibility(View.VISIBLE);
                    } else {
                        selfCheckin.setVisibility(View.GONE);
                    }
                    payOnline.setText(getString(R.string.check_in_online));

                    if (tentativeHasMRN) {
                        payOnline.setVisibility(View.GONE);
                        //commented
                       // visit_reception.setVisibility(View.VISIBLE);
                    }
                    String spec = tinyDB.getString(DOCTOR_SPECIALITY);
                    String mrnNo = tinyDB.getString(Const.MRN_NO);
                    if (ValidationHelper.isNullOrEmpty(mrnNo) || mrnNo.equalsIgnoreCase("0") ||
                            spec.equals("Dental") || spec.equals("قسم طب الأسنان") || spec.equals("Dentist") || spec.equals("دكتورالاسنان")
                    ) {
                        payOnline.setVisibility(View.GONE);
                    }
                } else {
                    selfCheckin.setVisibility(View.GONE);
                }

                sendMessage(apptsMiniModel, true);
            }
        } else if (requestCode == 111 && resultCode == RESULT_OK) {

            if (data != null) {
                String updatedTime = data.getStringExtra(Const.UPDATED_TIME);
                long apptId = data.getLongExtra(Const.APPT_ID, 0);
                if (apptsMiniModel != null)
                    apptsMiniModel.setApptId(apptId);
                else if (apptDetailModel != null)
                    apptDetailModel.setApptId(apptId);

                if (!ValidationHelper.isNullOrEmpty(updatedTime)) {

                    String date = LocaleDateHelper.convertDateStringFormat(updatedTime, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss");

                    if (!ValidationHelper.isNullOrEmpty(date)) {

                        if (apptsMiniModel != null)
                            apptsMiniModel.setApptDate(date);
                        else if (apptDetailModel != null)
                            apptDetailModel.setApptDate(date);

                        if (!ValidationHelper.isNullOrEmpty(updatedTime))
                            dateTime.setText(updatedTime);

                        long dateLong = LocaleDateHelper.convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss");
                        String dateAgo = TimeAgo.using(dateLong, messages);
                        String dateIn = dateAgo.replaceAll("within", "in");

                        if (!ValidationHelper.isNullOrEmpty(dateIn)) {

                            String days = LocaleDateHelper.getCountOfDays("", date);

                            if (days.equalsIgnoreCase("0")) {
                                apptTime.setText("Today");
                            } else if (days.equalsIgnoreCase("1")) {
                                apptTime.setText("Tomorrow");
                            } else if (days.equalsIgnoreCase("2")) {
                                apptTime.setText("in 2 days");
                            } else
                                apptTime.setText(dateIn);
                        }

                    }

                    sendMessage(apptsMiniModel, true);
                }
                // for finishing previous activity
                sendMessage(apptsMiniModel, true);
                finish();
            }
        } else if (requestCode == RC_RESCHEDULED && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == 113 && resultCode == RESULT_OK) {

            if (data != null) {

                boolean isInsurance = data.getBooleanExtra(Const.IS_INSURANCE_KEY, false);
                long insuranceId = data.getLongExtra(Const.INSURANCE_ID, 0);
                double telePrice = data.getDoubleExtra(Const.TELE_PRICE, 0);


                updateInsurance(insuranceId);
            }
        }
    }

    // Send an Intent with an action named "custom-event-name". The Intent sent should
    // be received by the ReceiverActivity.
    private void sendMessage(ApptsMiniModel apptsMiniModel, boolean isUpdate) {
        Intent intent = new Intent("update_list");
        if (apptsMiniModel != null) {
            // You can also include some extra data.
            intent.putExtra("updated_list", apptsMiniModel);
        } else if (apptDetailModel != null) {
            intent = new Intent("past_appts");
            intent.putExtra("refresh", true);
        }

        intent.putExtra(Const.IS_UPDATE, isUpdate);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @OnClick(R.id.cancel)
    void cancel() {
        long apptId = 0;
        if (apptsMiniModel != null) {
            if (apptsMiniModel.getRadOrder() != null)
                apptId = apptsMiniModel.getRadOrder().getApptId();
            else
                apptId = apptsMiniModel.getApptId();
        } else if (apptDetailModel != null)
            apptId = apptDetailModel.getApptId();

        showWarningMessage(null, apptId, isSecondary);
    }

    void showWarningMessage(String apptNo, long apptId, boolean isSecondary) {
        try {
            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.m_clinic)).setCancelText(getString(R.string.button_cancel))
                    .setContentText(getString(R.string.cancel_appointment)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                        }
                    }).setConfirmText(getString(R.string.yes)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                            if (ConnectionUtil.isInetAvailable(AppointmentDetailActivity.this)) {

                                showWaitDialog();

                                TinyDB tinyDB = new TinyDB(AppointmentDetailActivity.this);

                                String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                                String companyId = tinyDB.getString(Const.COMPANY_ID);

                                ApptCancelDTO apptCancelDTO = new ApptCancelDTO(securityToken, String.valueOf(apptId), isSecondary, companyId);

                                RestClient.getInstance().apptCancel(apptCancelDTO, new OnResultListener() {
                                    @Override
                                    public void onResult(Object result, boolean status, String errorMessage) {

                                        hideWaitDialog();

                                        MobileOpResult mobileOpResult = (MobileOpResult) result;

                                        if (mobileOpResult != null) {

                                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                                sendMessage(apptsMiniModel, false);
                                                SECONDARY_APPT_REFRESH = true;
                                                finish();
                                            } else {
                                                String errorMesg = "";

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                                }

                                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                                }

                                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                            }
                                        } else {
                                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                                        }

                                    }
                                });

                            } else {
                                ErrorMessage.getInstance().showWarning(AppointmentDetailActivity.this, "Internet is not available");
                            }
                        }
                    }).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.reschedule)
    void reschedule() {
//        DatePickerDialog datePicker = new DatePickerDialog(this, dateListener, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH));
//
//        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
//        datePicker.setCanceledOnTouchOutside(false);
//        datePicker.show();
        startActivityForResult(ReScheduleApptActivity.Companion.getLaunchIntent(this, apptsMiniModel), RC_RESCHEDULED);

    }

    @OnClick(R.id.changeInsurance)
    void setChangeInsurance() {
        getInsurance();
    }

    void getInsurance() {
        try {
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            GetInsuranceByIdDTO getInsuranceByIdDTO = new GetInsuranceByIdDTO(securityToken, String.valueOf(patientId));

            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                RestClient.getInstance().getInsuranceByProfileId(getInsuranceByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        GetInsuranceModel getInsuranceModel = (GetInsuranceModel) result;

                        if (getInsuranceModel != null) {

                            MobileOpResult mobileOpResult = getInsuranceModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    ArrayList<InsuranceCardModel> getInsuranceModelsList = getInsuranceModel.getInsuranceCardModels();

                                    if (ValidationHelper.isNullOrEmpty(getInsuranceModelsList)) {
                                        showInsuranceDialog();
                                        return;
                                    }

                                    Intent intent = new Intent(AppointmentDetailActivity.this, InsuranceCardCashCardActivity.class);
                                    intent.putExtra("insuranceCard", getInsuranceModelsList);
                                    intent.putExtra("noinsurance", doctorsProfile.getSpecialtyId());
                                    intent.putExtra("doctorId", doctorsProfile.getDoctorId().toString());
                                    intent.putExtra(Const.ISTELEMEDICINE_KEY, isTelemedcine);
                                    intent.putExtra(Const.Is_CHANGE_INSURANCE, true);
                                    startActivityForResult(intent, 113);
                                    //  }

                                } else {


                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                }

                            } else
                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);

                        } else {
                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                        }
                    }
                });

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getPatientData(boolean tentativeCheck, boolean isFromPayment) {
        try {
            if (ConnectionUtil.isInetAvailable(this)) {

               showWaitDialog();

                if (tinyDB != null) {

                    long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
                    String securityToken = tinyDB.getString(Const.TOKEN_KEY);
                    String oRegId = tinyDB.getString(Const.OREGID_KEY);

                    GetProfileDTO getProfileDTO = new GetProfileDTO(String.valueOf(patientId), securityToken, oRegId);
                    RestClient.getInstance().profileGetById(getProfileDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                           hideWaitDialog();

                            ProfileUpdateModel profileUpdateModel = (ProfileUpdateModel) result;

                            if (profileUpdateModel != null) {
                                INSURANCE_IMAGES = "";

                                MobileOpResult mobileOpResult = profileUpdateModel.getMobileOpResult();

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                        PatientRegistrationUpdate patientRegistrationUpdate = profileUpdateModel.getPatientRegistrationUpdate();

                                        if (patientRegistrationUpdate != null) {
                                            try {

                                                if (tentativeCheck) {
                                                    if (patientRegistrationUpdate.isTentativePatient()) {
                                                        hideCheckInBtn();
                                                    } else {
                                                        if (isFromPayment) {
                                                          //  self_check_in_loading.setVisibility(View.VISIBLE);
                                                            //self_checkin_progress.setVisibility(View.VISIBLE);
                                                            //validationofCheckIn();
                                                            //  selfCheckInEligibility();
                                                            //selfCheckin.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                } else {
                                                    if (patientRegistrationUpdate.getInsurance() != null) {
                                                        INSURANCE_IMAGES = patientRegistrationUpdate.getInsurance().getInsuranceCardScanBase64();
                                                        patientRegistrationUpdate.getInsurance().setInsuranceCardScanBase64("");
                                                    }

                                                    Intent intent = new Intent(AppointmentDetailActivity.this, AddInsuranceActivity.class);
                                                    intent.putExtra(PATIENT_UPDATE_KEY, patientRegistrationUpdate);
                                                    startActivityForResult(intent, 99);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                            if(mobileOpResult.getBusinessErrorMessageEn().
                                                    contains("verified")){
                                                payOnline.setVisibility(View.GONE);
                                            }
                                            if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";

                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                            ErrorMessage.getInstance().showErrorYellow(AppointmentDetailActivity.this, errorMesg);
                                        }else{
                                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                        }

                                      //  ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                                    }
                                } else
                                    ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                            } else
                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                        }
                    });
                }
            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean tentativeHasMRN = false;

    private void hideCheckInBtn() {
        String mrnNo = tinyDB.getString(Const.MRN_NO);

        String btnText = payOnline.getText().toString().trim();
        if (!ValidationHelper.isNullOrEmpty(mrnNo) && !mrnNo.equalsIgnoreCase("0") && !btnText.equals(getString(R.string.pay_online))) {
            //for tentative patient we hide self check and pay online button
            //but in this case if patient has mrn but is tentative then we will also hide check in btn
            tentativeHasMRN = true;
            selfCheckin.setVisibility(View.GONE);
            payOnline.setVisibility(View.GONE);
            //commented
          //  visit_reception.setVisibility(View.VISIBLE);
        }
    }

    void updateInsurance(long insuranceId) {
        try {

            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = new TinyDB(this).getString(Const.COMPANY_ID);

            UpdateInsuranceDTO updateInsuranceByIdDTO = new UpdateInsuranceDTO(securityToken,
                    String.valueOf(apptsMiniModel.getApptId()), insuranceId, companyId);

            if (ConnectionUtil.isInetAvailable(this)) {

                showWaitDialog();

                RestClient.getInstance().ApptInsUpdate(updateInsuranceByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        MobileOpResult mobileOpResult = (MobileOpResult) result;

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                showInsuranceSuccessDialog();

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);

                    }
                });

            } else {
                ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showInsuranceSuccessDialog() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setCancelable(false);
        alertDialog.setTitleText(getString(R.string.my_clininc))
                .setContentText(getString(R.string.change_insurance_success))
                .setConfirmText(getString(R.string.ok)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.dismiss();
                        if (apptsMiniModel != null) {
                            if (apptsMiniModel.getRadOrder() != null) {
                                apptGetTotalOutstanding(apptsMiniModel.getRadOrder().getApptId(), apptsMiniModel.isTelemedicine(), true);
                            } else {
                                apptGetTotalOutstanding(apptsMiniModel.getApptId(), apptsMiniModel.isTelemedicine(), true);
                            }

                        } else if (apptDetailModel != null)
                            apptGetTotalOutstanding(apptDetailModel.getApptId(), apptDetailModel.isTelemedicine(), true);

                    }
                }).show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    public void showInsuranceDialog() {
        try {

           /* final Dialog dialog = new Dialog(this, R.style.NewDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.myclinic_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            RegularTextView dontRemindAgain = (RegularTextView) dialog.findViewById(R.id.dont_remind);

            LightTextView msg = (LightTextView) dialog.findViewById(R.id.add_sugar);

            msg.setText(getString(R.string.change_insurance_msg));
            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getPatientData();
                }
            });

            dialog.show();*/

            String cancel1 = getString(R.string.no);
            String yes = getString(R.string.yes);

            IOSDialog dialogView = new IOSDialog.Builder(this)
                    .setTitle(R.string.my_clininc)
                    .setMessage(getString(R.string.change_insurance_msg))
                    .setPositiveButton(cancel1, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton(yes, (dialog, which) -> {
                        dialog.dismiss();
                        getPatientData(false, false);
                    }).show();

            String font = "GothamMedium.otf";
            String font_bold = "GothamBold.otf";

            Button btnConfirm = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.confirm_btn);
            btnConfirm.setText(cancel1);

            Button cancel = (Button) dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.cancel_btn);
            cancel.setText(yes);

            TextView tvTitle = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.title);
            TextView tvMessage = dialogView.findViewById(com.ligl.android.widget.iosdialog.R.id.message);
            Typeface tf = Typeface.createFromAsset(this.getAssets(), font);
            Typeface tf_title = Typeface.createFromAsset(this.getAssets(), font_bold);
            btnConfirm.setTypeface(tf);
            btnConfirm.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            cancel.setTypeface(tf);
            cancel.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            tvTitle.setTypeface(tf_title);
            tvMessage.setTypeface(tf);
            tvMessage.setTextColor(this.getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
    }

    void confirmAppointment(long mApptId, String apptDate, boolean isChangeInsurance, boolean isBoardingPass) {

        showWaitDialog();

        String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
        String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
        if (!ValidationHelper.isNullOrEmpty(securityToken) && mApptId != 0) {

            ApptPaymentDTO apptPaymentDTO = new ApptPaymentDTO(securityToken, mApptId, companyId);

            RestClient.getInstance().apptPayment(apptPaymentDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                if (!isChangeInsurance) {

                                    if(isBoardingPass){
                                        gotoBoardingPass(doctorsProfile, apptDate, mApptId);
                                    }
                                }
                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(AppointmentDetailActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(AppointmentDetailActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    void apptStatusUI(int status) {
        if (status == 3 || status == 2 || status == 6)
            container_buttons.setVisibility(View.GONE);
        else
            container_buttons.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.add_calendar)
    void addToCalendarEvent() {
        String title = getString(R.string.appointment_with) + " " + doctrNameStr + " " + getString(R.string.at_my_clinic);
        String msg = " " + tinyDB.getString(Const.PATIENT_NAME) + " " + getString(R.string.has_an_appointment_with) + " " + doctrNameStr + " (" + doctrProfession + ") " + getString(R.string.at) + " " + date +
                getString(R.string.please_remember_to_arrive);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_CALENDAR);
            boolean readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_CALENDAR);
            if (!writeGranted && !readGranted) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
                        .check();
            } else {
                writeToCalendar(title, msg);
            }
        } else {
            writeToCalendar(title, msg);
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {

                String title = getString(R.string.appointment_with) + doctrNameStr + getString(R.string.at_my_clinic);
                String msg = tinyDB.getString(Const.PATIENT_NAME) + getString(R.string.has_an_appointment_with) + doctrNameStr + " (" + doctrProfession + ") " + getString(R.string.at) + date +
                        getString(R.string.please_remember_to_arrive);
                writeToCalendar(title, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(AppointmentDetailActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    void writeToCalendar(String title, String comment) {

        try {

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, milliseconds(date));
            values.put(CalendarContract.Events.DTEND, milliseconds(date));
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, comment);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);

            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

/*
            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.EVENT_ID, apptId);
            reminder.put(CalendarContract.Reminders.MINUTES, 15);
            reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder)*/
            ;

            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

            alertDialog.setTitleText(getString(R.string.my_clininc)).setConfirmText(getString(R.string.ok)).setContentText(
                            getString(R.string.event_added_successfully)
                    )
                    .show();

            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a");
        try {

            Date mDate = sdf.parse(date);

            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);

            payOnline.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            payOnline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

            selfCheckin.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            selfCheckin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);


            changeInsurance.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            changeInsurance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);


            add_calendar.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            add_calendar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);

            reschedule.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            reschedule.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);

            cancel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            cancel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.grey_left_arrow, 0, 0, 0);

        }
    }

}
