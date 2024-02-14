package com.telemedicine.myclinic.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.telemedicine.myclinic.AppointmentDetailActivity;
import com.telemedicine.myclinic.activities.multiservices.ProcedureAdapter;
import com.telemedicine.myclinic.activities.multiservices.ServiceHeaderAdapter;
import com.telemedicine.myclinic.activities.multiservices.ServiceItems;
import com.telemedicine.myclinic.activities.multiservices.ServicesGroupAdapter;
import com.telemedicine.myclinic.activities.multiservices.ServicesListActivity;
import com.telemedicine.myclinic.activities.multiservices.SpecialTestAdapter;
import com.telemedicine.myclinic.activities.otherservices.OtherServicesHeaderAdapter;
import com.telemedicine.myclinic.activities.otherservices.OtherServicesListAdapter;
import com.telemedicine.myclinic.adapters.LabTestAdapter;
import com.telemedicine.myclinic.adapters.MedicationAdapter;
import com.telemedicine.myclinic.adapters.RadiologyAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.AfgaReportDTO;
import com.telemedicine.myclinic.models.LabReportsResponseModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.ApptDetailDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailModel;
import com.telemedicine.myclinic.models.appointments.ApptModel;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.OrdersLabModel;
import com.telemedicine.myclinic.models.appointments.OrdersMedModel;
import com.telemedicine.myclinic.models.appointments.OrdersRadModel;
import com.telemedicine.myclinic.models.appointments.QTicketLabModel;
import com.telemedicine.myclinic.models.appointments.QTicketPharmModel;
import com.telemedicine.myclinic.models.homemodels.DoctorsProfile;
import com.telemedicine.myclinic.models.homemodels.QCancelTicketDTO;
import com.telemedicine.myclinic.models.homevisit.QGetTicketDTO;
import com.telemedicine.myclinic.models.homevisit.QTicketMini;
import com.telemedicine.myclinic.models.homevisit.QTicketMiniModel;
import com.telemedicine.myclinic.models.humanTest.TranslateLabDTO;
import com.telemedicine.myclinic.models.humanTest.TranslateLabModel;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeDTO;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeModel;
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
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightTextView;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;
import com.xwray.groupie.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_ID;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.IS_INSURANCE_KEY;
import static com.telemedicine.myclinic.util.Const.SECONDARY_APPT_REFRESH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ServicesOrdersActivity extends BaseActivity implements LabTestAdapter.OnCardClickListner, RadiologyAdapter.OnCardClickListner {


    @BindView(R.id.lab_test_recycler)
    RecyclerView labTestRecycler;

    @BindView(R.id.radiology_recycler)
    RecyclerView radiologyRecycler;

    @BindView(R.id.procedure_recycler)
    RecyclerView procedureRecycler;

    @BindView(R.id.special_recycler)
    RecyclerView specialRecycler;

    @BindView(R.id.medication_recycler)
    RecyclerView medRecycler;

    @BindView(R.id.other_services_recycler)
    RecyclerView otherServicesRecycler;

    @BindView(R.id.doctor_image)
    CircleImageView doctorImage;

    @BindView(R.id.doctor_name)
    BoldTextView doctorName;

    @BindView(R.id.doctor_profession)
    LightTextView doctorProfession;

    @BindView(R.id.dateTime)
    LightTextView dateTime;

    @BindView(R.id.doctor_image1)
    CircleImageView doctorImage1;

    @BindView(R.id.doctor_name1)
    BoldTextView doctorName1;

    @BindView(R.id.doctor_profession1)
    LightTextView doctorProfession1;

    @BindView(R.id.dateTime1)
    LightTextView dateTime1;

    @BindView(R.id.qticket_pharm_container)
    LinearLayout qticketPharmContainer;

    @BindView(R.id.queueTicketPharm)
    BoldTextView queueTicketPharm;

    @BindView(R.id.qticket_lab_container)
    LinearLayout qticketLabContainer;

    @BindView(R.id.queueTicketLab)
    BoldTextView queueTicketLab;

    @BindView(R.id.lab_container)
    RelativeLayout labContainer;

    @BindView(R.id.container_radiology)
    RelativeLayout containerRadiology;

    @BindView(R.id.container_Procedures)
    RelativeLayout containerProcedures;

    @BindView(R.id.container_special)
    RelativeLayout containerSpecial;


    @BindView(R.id.container_medication)
    RelativeLayout containerMedication;

    @BindView(R.id.container_approved)
    RelativeLayout containerApproved;

    @BindView(R.id.approve_id)
    BoldTextView approveId;

    @BindView(R.id.lab_place_holder)
    RegularTextView labPlaceHolder;

    @BindView(R.id.lab_test_inner_container)
    RelativeLayout labTestInnerContainer;

    @BindView(R.id.inner_container_radiology)
    RelativeLayout innerContainerRadiology;

    @BindView(R.id.inner_container_procedure)
    RelativeLayout innerContainerProcedure;

    @BindView(R.id.inner_container_special)
    RelativeLayout innerContainerSpecial;

    @BindView(R.id.radiology_place_holder)
    RegularTextView radiologyPlaceHolder;

    @BindView(R.id.inner_container_medication)
    RelativeLayout innerContainerMedication;

    @BindView(R.id.medication_place_holder)
    RegularTextView medicationPlaceHolder;

    @BindView(R.id.queueTicketLabBtn)
    LightButton queueTicketLabBtn;

    @BindView(R.id.cancelQticketLab)
    LightButton cancelQticketLabBtn;

    @BindView(R.id.get_queue_ticket)
    LightButton getMedQueueTicket;

    @BindView(R.id.cancelMedTicket)
    LightButton cancelMedTQicket;

    @BindView(R.id.book_home_visit)
    LightButton book_home_visit;


    @BindView(R.id.pay_services_btn)
    LightButton pay_services_btn;

    @BindView(R.id.waiting_med)
    LightTextView waiting_med;

    @BindView(R.id.waiting_lab)
    LightTextView waiting_lab;

    @BindView(R.id.container_btn)
    RelativeLayout containerHomeLabBtn;

    @BindView(R.id.container_q_tickets)
    RelativeLayout containerMedtickets;

    @BindView(R.id.no_services)
    RegularTextView noServices;

    @BindView(R.id.refresh)
    ImageView refresh;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.container_card)
    RelativeLayout container_Card;

    @BindView(R.id.container_card1)
    RelativeLayout container_card1;

    boolean isPreauthApproved = false;
    private GroupAdapter<ViewHolder> groupAdapter;

    TinyDB tinyDB = null;

    ArrayList<OrdersLabModel> OrdersLab = null;

    ApptsMiniModel apptsMiniModel = null;

    String QlineIdLab = "0", QlineIdMed = "0";

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        apptGetDetails();
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
       /* LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("past_appts"));*/
    }

    @Override
    protected int layout() {
        return R.layout.activity_services_orders;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    void init() {
        transformViews();

        tinyDB = new TinyDB(this);

        labTestRecycler.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();

        if (intent != null) {

            apptId = intent.getLongExtra(Const.APPT_ID, 0);

            //apptsMiniModel = intent.getParcelableExtra(Const.APPTMODEL);

            String data =intent.getStringExtra(Const.APPTMODEL);
            apptsMiniModel = new GsonBuilder().create().fromJson(data, ApptsMiniModel.class);

            if (apptsMiniModel != null) {
                loadData(apptsMiniModel);
            }
        }

        groupAdapter = new GroupAdapter();
        otherServicesRecycler.setLayoutManager(new LinearLayoutManager(this));
        otherServicesRecycler.setAdapter(groupAdapter);

        radiologyRecycler.setLayoutManager(new LinearLayoutManager(this));
        medRecycler.setLayoutManager(new LinearLayoutManager(this));
        procedureRecycler.setLayoutManager(new LinearLayoutManager(this));
        specialRecycler.setLayoutManager(new LinearLayoutManager(this));

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

    String date = "";

    private void updateDOB() {
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        date = sdf.format(myCalendar.getTime());

        if (ordersRadModel != null) {
            Intent intent = new Intent(this, BookAppointmentTwoActivity.class);
            intent.putExtra(ISTELEMEDICINE_KEY, false);
            intent.putExtra(Const.ORDER_RAD, ordersRadModel);
            intent.putExtra(Const.IS_SECONDARY, true);
            intent.putExtra(Const.DATE, date);
            intent.putExtra(IS_INSURANCE_KEY, apptsMiniModel.isInsurance());
            startActivity(intent);
        }
    }

    private void showPayServiceBtn() {
        if (isPreauthApproved && !companyId.equals("prc")) {
            pay_services_btn.setVisibility(View.VISIBLE);
        } else {
            pay_services_btn.setVisibility(View.GONE);
        }
    }

    private void hidePayServiceBtn() {
        pay_services_btn.setVisibility(View.GONE);
    }

    void loadData(ApptsMiniModel model) {

        DoctorsProfile doctorProfile = model.getDoctorProfile();
        if (doctorProfile != null) {
            String doctName = "";
            String speciality = "";
            if (TextUtil.getEnglish(this)) {
                doctName = doctorProfile.getNameEn();
                speciality = doctorProfile.getTitleEn();
            } else if (TextUtil.getArabic(this)) {
                doctName = doctorProfile.getNameAr();
                speciality = doctorProfile.getSpecialtyAr();
            }


            String apptDate = model.getApptDate();
            String DoctorUrl = doctorProfile.getProfilePicUrl();

            if (!ValidationHelper.isNullOrEmpty(doctName))
                this.doctorName.setText(doctName);

            if (!ValidationHelper.isNullOrEmpty(speciality))
                this.doctorProfession.setText(speciality);

            if (!ValidationHelper.isNullOrEmpty(apptDate)) {
                String date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm aa");
                if (!ValidationHelper.isNullOrEmpty(date))
                    this.dateTime.setText(date);
            }

            if (!ValidationHelper.isNullOrEmpty(DoctorUrl))
                Glide.with(this).load(DoctorUrl).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage);

            //arabic

            if (!ValidationHelper.isNullOrEmpty(doctName))
                this.doctorName1.setText(doctName);

            if (!ValidationHelper.isNullOrEmpty(speciality))
                this.doctorProfession1.setText(speciality);

            if (!ValidationHelper.isNullOrEmpty(apptDate)) {
                String date = LocaleDateHelper.convertDateStringFormat(apptDate, "yyyy-MM-dd'T'HH:mm:ss", "EEEE, dd MMMM yyyy hh:mm aa");
                if (!ValidationHelper.isNullOrEmpty(date))
                    this.dateTime1.setText(date);
            }

            if (!ValidationHelper.isNullOrEmpty(DoctorUrl))
                Glide.with(this).load(DoctorUrl).placeholder(R.drawable.doctr).into((CircleImageView) doctorImage1);

        }

    }

    public void HomeVisit(View view) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean accessFineLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_FINE_LOCATION);
            boolean accessCourseLocation = PermissionUtils.isGranted(this, PermissionEnum.ACCESS_COARSE_LOCATION);

            if (!accessFineLocation || !accessCourseLocation) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .check();
            } else {
                if (isLocationEnabled(this)) {
                    gotoHome();
                } else {
                    showGPSDisabledAlertToUser();
                }
            }
        } else {
            if (isLocationEnabled(this)) {
                gotoHome();
            } else {
                showGPSDisabledAlertToUser();
            }
        }
    }

    public void payServices(View view) {
        Intent intent = new Intent(this, ServicesListActivity.class);
        intent.putExtra(Const.APPT_ID, apptId);
        intent.putExtra(COMPANY_ID, companyId);
        intent.putExtra(IS_INSURANCE_KEY, isInsuranceAppt);
        startActivityForResult(intent, 101);
    }

    void gotoHome() {
        Intent intent = new Intent(this, HomeVisitActivity.class);
        intent.putParcelableArrayListExtra("orderLab", OrdersLab);
        startActivity(intent);
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.gps_disable_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.enable_gps,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        gotoHome();

                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static boolean isLocationEnabled(Context context) {
        return getLocationMode(context) != Settings.Secure.LOCATION_MODE_OFF;
    }

    private static int getLocationMode(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
    }

    long apptId = 0;
    String companyId = "";
boolean isInsuranceAppt = false;
    void apptGetDetails() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            companyId = tinyDB.getString(Const.COMPANY_ID);
            Intent intent = getIntent();

            if (intent != null) {

                apptId = intent.getLongExtra(Const.APPT_ID, 0);

                if (apptId != 0) {
//5639679631l
                    ApptDetailDTO apptDetailDTO = new ApptDetailDTO(securityToken, apptId, companyId);

                    RestClient.getInstance().apptGetDetails(apptDetailDTO, new OnResultListener() {
                        @Override
                        public void onResult(Object result, boolean status, String errorMessage) {

                            hideWaitDialog();

                            ApptDetailModel apptModel = (ApptDetailModel) result;

                            if (apptModel != null) {

                                MobileOpResult mobileOpResult = apptModel.getMobileOpResult();

                                if (mobileOpResult != null) {

                                    if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                        if (apptModel.getAppt() != null) {
                                            isInsuranceAppt = apptModel.getAppt().isInsurance();

                                            preAuthStatus(apptModel.getAppt().getPreAuthStatus(), apptModel.getAppt());
                                            ordersLab(apptModel.getAppt().getOrdersLab());
                                            ordersMed(apptModel.getAppt().getOrdersMed());
                                            ordersRad(apptModel.getAppt().getOrdersRad());
                                            ordersOtherServices(apptModel.getAppt().getOtherServices());

                                            qTicketPharm(apptModel.getAppt().getQTicketPharm());
                                            qTicketLab(apptModel.getAppt().getQTicketLab());

                                            if (ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersLab())
                                                    && ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersMed())
                                                    && ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersRad())
                                                    && ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOtherServices())) {
// && ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOtherServices())
                                                noServices.setVisibility(View.VISIBLE);
                                            } else {
                                                if (!apptModel.getAppt().isInsurance()) {
                                                    isPreauthApproved = true;
                                                    showPayServiceBtn();
                                                }
                                                noServices.setVisibility(View.GONE);
                                            }

                                            if (ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersLab())
                                                    && ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersMed())) {
                                                qticketLabContainer.setVisibility(View.GONE);
                                                qticketPharmContainer.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        String errorMesg = "";

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                            if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                            else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                                errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                        }

                                        if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                            errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                        }

                                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                                    }
                                } else {
                                    ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                            }
                        }
                    });
                }
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    private void qTicketPharm(QTicketPharmModel qTicketPharm) {

        if (qTicketPharm != null) {

            QlineIdMed = String.valueOf(qTicketPharm.getQLineId());
            qticketPharmContainer.setVisibility(View.VISIBLE);
            queueTicketPharm.setText("" + qTicketPharm.getTicketNo());
            waiting_med.setText(qTicketPharm.getTicketsWaiting() + " " + getString(R.string.currently_waiting));
            qTickeMedDisable();

        } else {
            if (qticketLabContainer.getVisibility() == View.VISIBLE)
                qticketPharmContainer.setVisibility(View.INVISIBLE);
            else
                qticketPharmContainer.setVisibility(View.GONE);
            qTickeMedEnable();
        }
    }

    private void qTicketLab(QTicketLabModel qTicketLab) {

        if (qTicketLab != null) {
            QlineIdLab = String.valueOf(qTicketLab.getQLineId());
            qticketLabContainer.setVisibility(View.VISIBLE);
            queueTicketLab.setText("" + qTicketLab.getTicketNo());
            waiting_lab.setText(qTicketLab.getTicketsWaiting() + " " + getString(R.string.currently_waiting));
            qTickeLabDisable();
        } else {
            if (qticketPharmContainer.getVisibility() == View.VISIBLE)
                qticketLabContainer.setVisibility(View.INVISIBLE);
            else
                qticketLabContainer.setVisibility(View.GONE);

            qTickeLabEnable();
        }
    }

    void ordersLab(ArrayList<OrdersLabModel> OrdersLab) {
        this.OrdersLab = OrdersLab;

        if (!ValidationHelper.isNullOrEmpty(OrdersLab)) {


            LabTestAdapter confirmationAdapter = new LabTestAdapter(this, OrdersLab);
            labTestRecycler.setAdapter(confirmationAdapter);
            confirmationAdapter.setOnCardClickListner(this);

            labPlaceHolder.setVisibility(View.GONE);
            labTestInnerContainer.setVisibility(View.VISIBLE);
            labContainer.setVisibility(View.VISIBLE);
            containerHomeLabBtn.setVisibility(View.VISIBLE);

        } else {

            labContainer.setVisibility(View.GONE);
            labPlaceHolder.setVisibility(View.VISIBLE);
            labTestInnerContainer.setVisibility(View.GONE);
            containerHomeLabBtn.setVisibility(View.GONE);
        }
    }

    void ordersProcedure(ArrayList<ServiceItems> ordersProcedure) {

        if (!ValidationHelper.isNullOrEmpty(ordersProcedure)) {

            innerContainerProcedure.setVisibility(View.VISIBLE);
            containerProcedures.setVisibility(View.VISIBLE);
            ProcedureAdapter procedureAdapter = new ProcedureAdapter(this, ordersProcedure);
            procedureRecycler.setAdapter(procedureAdapter);

        } else {
            containerProcedures.setVisibility(View.GONE);
            innerContainerProcedure.setVisibility(View.GONE);
        }

    }

    void ordersSpecial(ArrayList<ServiceItems> ordersSpecial) {

        if (!ValidationHelper.isNullOrEmpty(ordersSpecial)) {

            innerContainerSpecial.setVisibility(View.VISIBLE);
            containerSpecial.setVisibility(View.VISIBLE);
            SpecialTestAdapter procedureAdapter = new SpecialTestAdapter(this, ordersSpecial);
            specialRecycler.setAdapter(procedureAdapter);

        } else {
            containerSpecial.setVisibility(View.GONE);
            innerContainerSpecial.setVisibility(View.GONE);
        }

    }

    ArrayList<String> productTypes = new ArrayList<>();

    void ordersOtherServices(ArrayList<ServiceItems> otherServices) {

        if (!productTypes.isEmpty()) {
            groupAdapter.clear();
            productTypes.clear();
            groupAdapter.notifyDataSetChanged();
        }

        if (!ValidationHelper.isNullOrEmpty(otherServices)) {
            otherServicesRecycler.setVisibility(View.VISIBLE);

            for (ServiceItems item : otherServices) {
                if (item.getProductType() != null) {
                    if (!productTypes.contains(item.getProductType())) {
                        productTypes.add(item.getProductType());
                        Section section = new Section();

                        if (TextUtil.getEnglish(this)) {
                            section.setHeader(new OtherServicesHeaderAdapter(item.getProductType()));
                        } else {
                            if(item.getProductTypeAr() != null){
                                section.setHeader(new OtherServicesHeaderAdapter(item.getProductTypeAr()));
                            }else{
                                section.setHeader(new OtherServicesHeaderAdapter(item.getProductType()));
                            }

                        }
                        for (ServiceItems nestedItem : otherServices) {
                            if (nestedItem.getProductType().equals(item.getProductType())) {
                                section.add(new OtherServicesListAdapter(nestedItem, this));
                            }
                        }
                        groupAdapter.add(section);
                    }
                }

            }

        } else {
            otherServicesRecycler.setVisibility(View.GONE);

        }

    }

    void ordersRad(ArrayList<OrdersRadModel> ordersRad) {

        if (!ValidationHelper.isNullOrEmpty(ordersRad)) {

            innerContainerRadiology.setVisibility(View.VISIBLE);
            radiologyPlaceHolder.setVisibility(View.GONE);
            containerRadiology.setVisibility(View.VISIBLE);
            RadiologyAdapter radiologyAdapter = new RadiologyAdapter(this, ordersRad);
            radiologyRecycler.setAdapter(radiologyAdapter);
            radiologyAdapter.setOnCardClickListner(this);

        } else {
            containerRadiology.setVisibility(View.GONE);
            innerContainerRadiology.setVisibility(View.GONE);
            radiologyPlaceHolder.setVisibility(View.VISIBLE);
        }

    }

    void ordersMed(ArrayList<OrdersMedModel> ordersMed) {

        if (!ValidationHelper.isNullOrEmpty(ordersMed)) {

            innerContainerMedication.setVisibility(View.VISIBLE);
            medicationPlaceHolder.setVisibility(View.GONE);
            containerMedication.setVisibility(View.VISIBLE);
            containerMedtickets.setVisibility(View.VISIBLE);

            MedicationAdapter radiologyAdapter = new MedicationAdapter(this, ordersMed);
            medRecycler.setAdapter(radiologyAdapter);
        } else {

            innerContainerMedication.setVisibility(View.GONE);
            medicationPlaceHolder.setVisibility(View.VISIBLE);
            containerMedtickets.setVisibility(View.GONE);
            containerMedication.setVisibility(View.GONE);
        }

    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {
                if (isLocationEnabled(ServicesOrdersActivity.this)) {
                    gotoHome();
                } else {
                    showGPSDisabledAlertToUser();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            if (isLocationEnabled(ServicesOrdersActivity.this)) {
                gotoHome();
            } else {
                showGPSDisabledAlertToUser();
            }
        }
    };

    void qTickeLabEnable() {
        queueTicketLabBtn.setEnabled(true);
        queueTicketLabBtn.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        cancelQticketLabBtn.setEnabled(false);
        cancelQticketLabBtn.setBackgroundColor(this.getResources().getColor(R.color.colorGrey2));

        if (qticketPharmContainer.getVisibility() != View.VISIBLE && qticketLabContainer.getVisibility() != View.VISIBLE) {
            qticketPharmContainer.setVisibility(View.GONE);
            qticketLabContainer.setVisibility(View.GONE);
        }
    }

    void qTickeLabDisable() {
        queueTicketLabBtn.setEnabled(false);
        queueTicketLabBtn.setBackgroundColor(this.getResources().getColor(R.color.colorGrey2));
        cancelQticketLabBtn.setEnabled(true);
        cancelQticketLabBtn.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));

        if (qticketPharmContainer.getVisibility() != View.VISIBLE && qticketLabContainer.getVisibility() != View.VISIBLE) {
            qticketPharmContainer.setVisibility(View.GONE);
            qticketLabContainer.setVisibility(View.GONE);
        }
    }

    void qTickeMedEnable() {
        getMedQueueTicket.setEnabled(true);
        getMedQueueTicket.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        cancelMedTQicket.setEnabled(false);
        cancelMedTQicket.setBackgroundColor(this.getResources().getColor(R.color.colorGrey2));

        if (qticketPharmContainer.getVisibility() != View.VISIBLE && qticketLabContainer.getVisibility() != View.VISIBLE) {
            qticketPharmContainer.setVisibility(View.GONE);
            qticketLabContainer.setVisibility(View.GONE);
        }
    }

    void qTickeMedDisable() {
        getMedQueueTicket.setEnabled(false);
        getMedQueueTicket.setBackgroundColor(this.getResources().getColor(R.color.colorGrey2));
        cancelMedTQicket.setEnabled(true);
        cancelMedTQicket.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));

        if (qticketPharmContainer.getVisibility() != View.VISIBLE && qticketLabContainer.getVisibility() != View.VISIBLE) {
            qticketPharmContainer.setVisibility(View.GONE);
            qticketLabContainer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.queueTicketLabBtn)
    void getQueueLabTicket() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String qSiteTypeLab = "30";
            String apptId = "";

            if (apptsMiniModel != null)
                apptId = String.valueOf(apptsMiniModel.getApptId());

            QGetTicketDTO getTicketDTO = new QGetTicketDTO(securityToken, qSiteTypeLab, apptId, companyId);

            RestClient.getInstance().qGetTicket(getTicketDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    QTicketMiniModel qTicketMiniModel = (QTicketMiniModel) result;

                    if (qTicketMiniModel != null) {

                        MobileOpResult mobileOpResult = qTicketMiniModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {

                                QTicketMini qTicketMini = qTicketMiniModel.getqTicketMini();

                                if (qTicketMini != null) {

                                    QlineIdLab = String.valueOf(qTicketMini.getQLineId());

                                    if (qticketPharmContainer.getVisibility() != View.VISIBLE)
                                        qticketPharmContainer.setVisibility(View.INVISIBLE);

                                    qticketLabContainer.setVisibility(View.VISIBLE);
                                    queueTicketLab.setText("" + qTicketMini.getTicketNo());
                                    waiting_lab.setText(qTicketMini.getTicketsWaiting() + " " + getString(R.string.currently_waiting));
                                    qTickeLabDisable();
                                } else {
                                    qticketLabContainer.setVisibility(View.INVISIBLE);
                                    qTickeLabEnable();
                                }

                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);

                            }

                        } else {
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                    }


                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void showWarningMessage(String msg, boolean lab) {
        try {

            String ms = getString(R.string.Are_you_sure_do_you_wish_to_cancel_the_booked) + " " + msg + " " + getString(R.string.queue_ticket);

            if (TextUtil.getArabic(this)) {
                if (!lab) {
                    ms = getString(R.string.pharmacy_msg);
                } else if (lab) {
                    ms = getString(R.string.lab_msg);
                }
            }

            SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            alertDialog.setCancelable(false);
            alertDialog.setTitleText(getString(R.string.my_clininc)).setCancelText(getString(R.string.no_thanks))
                    .setContentText(ms).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                        }
                    }).setConfirmText(getString(R.string.yes_please)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alertDialog.dismiss();
                            if (lab)
                                cancelQueueLabTicket();
                            else
                                cancelQueueMedTicket();

                        }
                    }).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.cancelQticketLab)
    void cancelLabTick() {
        showWarningMessage(getString(R.string.lab), true);
    }

    void cancelQueueLabTicket() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            QCancelTicketDTO qCancelTicketDTO = new QCancelTicketDTO(securityToken, QlineIdLab, companyId);

            RestClient.getInstance().qCancelTicket(qCancelTicketDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            if (qticketPharmContainer.getVisibility() == View.VISIBLE)
                                qticketLabContainer.setVisibility(View.INVISIBLE);
                            else
                                qticketLabContainer.setVisibility(View.GONE);
                            qTickeLabEnable();
                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                    }
                }
            });


        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    @OnClick(R.id.get_queue_ticket)
    void getQueueMedTicket() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String qSiteTypeLab = "20";
            String apptId = "";
            if (apptsMiniModel != null)
                apptId = String.valueOf(apptsMiniModel.getApptId());

            QGetTicketDTO getTicketDTO = new QGetTicketDTO(securityToken, qSiteTypeLab, apptId, companyId);

            RestClient.getInstance().qGetTicket(getTicketDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    QTicketMiniModel qTicketMiniModel = (QTicketMiniModel) result;

                    if (qTicketMiniModel != null) {

                        MobileOpResult mobileOpResult = qTicketMiniModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if ((mobileOpResult.getResult() == Success.SUCCESSCODE.getValue())) {

                                QTicketMini qTicketMini = qTicketMiniModel.getqTicketMini();

                                if (qTicketMini != null) {
                                    QlineIdMed = String.valueOf(qTicketMini.getQLineId());
                                    qticketPharmContainer.setVisibility(View.VISIBLE);


                                    if (qticketLabContainer.getVisibility() != View.VISIBLE)
                                        qticketLabContainer.setVisibility(View.INVISIBLE);

                                    queueTicketPharm.setText("" + qTicketMini.getTicketNo());
                                    waiting_med.setText(qTicketMini.getTicketsWaiting() + " " + getString(R.string.currently_waiting));
                                    qTickeMedDisable();

                                } else {
                                    qticketPharmContainer.setVisibility(View.INVISIBLE);
                                    qTickeMedEnable();
                                }

                            } else {
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);

                            }

                        } else {
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                        }
                    } else {
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                    }


                }
            });

        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    @OnClick(R.id.cancelMedTicket)
    void cancelMedTicket() {
        showWarningMessage(getString(R.string.pharmacy), false);
    }

    void cancelQueueMedTicket() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            QCancelTicketDTO qCancelTicketDTO = new QCancelTicketDTO(securityToken, QlineIdMed, companyId);

            RestClient.getInstance().qCancelTicket(qCancelTicketDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            if (qticketLabContainer.getVisibility() == View.VISIBLE)
                                qticketPharmContainer.setVisibility(View.INVISIBLE);
                            else
                                qticketPharmContainer.setVisibility(View.GONE);

                            // qticketPharmContainer.setVisibility(View.INVISIBLE);
                            qTickeMedEnable();
                        } else {
                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                    }
                }
            });


        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");

        }


    }

    @OnClick(R.id.refresh)
    void refresh() {
        apptGetDetails();
    }

    void preAuthStatus(long status, ApptModel apptModel) {

        if (ValidationHelper.isNullOrEmpty(apptModel.getOrdersLab()) &&
                ValidationHelper.isNullOrEmpty(apptModel.getOrdersMed())
                &&
                ValidationHelper.isNullOrEmpty(apptModel.getOrdersRad())
                &&   ValidationHelper.isNullOrEmpty(apptModel.getOtherServices())
        ) {
            /* &&   ValidationHelper.isNullOrEmpty(apptModel.getOtherServices())*/

            containerApproved.setVisibility(View.GONE);
        } else {
            containerApproved.setVisibility(View.VISIBLE);

            approveId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            switch (String.valueOf(status)) {

                case "0":
                    containerApproved.setVisibility(View.GONE);
                    break;
                case "1":
                    approveId.setText(R.string.pending);
                    containerApproved.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
                case "2":
                    approveId.setText(R.string.in_review);
                    containerApproved.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
                case "3":
                    approveId.setText(getString(R.string.approved));
                    isPreauthApproved = true;
                    showPayServiceBtn();
                    containerApproved.setBackgroundColor(getResources().getColor(R.color.colorAquaSpring));
                    if (TextUtil.getEnglish(this))
                        approveId.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.success), null);
                    else
                        approveId.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.success), null, null, null);

                    break;
                case "4":
                    approveId.setText(R.string.partialy_approved);
                    containerApproved.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
                case "5":
                    approveId.setText(R.string.cancelled);
                    isPreauthApproved = true;
                    showPayServiceBtn();
                    containerApproved.setBackgroundColor(getResources().getColor(R.color.zm_v1_red_500));
                    break;

            }
        }


    }

    String orderDate = "";

    @Override
    public void OnCardClicked(OrdersLabModel model, int pos) {

        if (model != null) {

            String patientName = tinyDB.getString(Const.PATIENT_NAME);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String patientKey = patientId + "_key";

            String existPatient = tinyDB.getString(patientKey);
            orderDate = model.getOrderDate();
            if (ValidationHelper.isNullOrEmpty(existPatient)) {
                getSecurityChallenge(model.getId(), "", true, false, model.getName());
            } else {
                labReport(model.getId(), model.getName());
            }
        }
    }

    void labReport(long id, String name) {
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyID = tinyDB.getString(Const.COMPANY_ID);
        TranslateLabDTO translateLabDTO = new TranslateLabDTO(securityToken, id, companyID);

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            RestClient.getInstance().translateLab(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();

                                String doc = "http://docs.google.com/viewer?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(orderDate, "yyyy-MM-dd'T'HH:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(ServicesOrdersActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", doc);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", name);
                                startActivity(intent);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    OrdersRadModel ordersRadModel = null;

    @Override
    public void OnCardClicked(OrdersRadModel model, int pos, boolean isBook, boolean isView, boolean isDownload) {

        orderDate = model.getOrderDate();
        if (isView) {
            apptGetDetailsView(model.getApptId(), pos);
        } else if (isBook) {
            ordersRadModel = model;
            DatePickerDialog datePicker = new DatePickerDialog(this, dateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            datePicker.setCanceledOnTouchOutside(false);
            datePicker.show();
        } else if (isDownload) {

            String patientName = tinyDB.getString(Const.PATIENT_NAME);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            String patientKey = patientId + "_key";

            String existPatient = tinyDB.getString(patientKey);
            if (ValidationHelper.isNullOrEmpty(existPatient)) {
                getSecurityChallenge(0L, model.getOrderNumber(), false, true, model.getName());
            } else {
                showReportSelectionDialog(model.getOrderNumber(), model.getName());
            }
        }
    }

    private void getSecurityChallenge(long id, String orderNumer, boolean isLab, boolean isRad, String name) {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
            SecurityChallengeDTO securityChallengeDTO = new SecurityChallengeDTO(securityToken, patientId);

            RestClient.getInstance().getSecurityChallenge(securityChallengeDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {
                    hideWaitDialog();

                    SecurityChallengeModel securityChallengeModel = (SecurityChallengeModel) result;

                    if (securityChallengeModel != null) {

                        MobileOpResult mobileOpResult = securityChallengeModel.getMobileOpResult();

                        if (mobileOpResult != null) {
                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String question = "";

                                if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                    question = securityChallengeModel.getQuestionAr();
                                else if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                    question = securityChallengeModel.getQuestionEn();

                                String answer = securityChallengeModel.getAnswer();

                                showDialog(question, answer, id, orderNumer, isLab, isRad, name);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                        }


                    } else
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);

                }
            });

        } else
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");


    }

    void getRadReport(String id, String name) {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyID = tinyDB.getString(Const.COMPANY_ID);
        // TranslateLabDTO translateLabDTO = new TranslateLabDTO(securityToken, id, companyID);
        AfgaReportDTO translateLabDTO = new AfgaReportDTO(securityToken, id, "");
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().GetAgfaRadiologyReportUrl(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();
                              /*  String doc = "http://docs.google.com/gview?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(orderDate, "yyyy-MM-dd'T'hh:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(ServicesOrdersActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", url);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", name);
                                startActivity(intent);*/

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void getRadReportImageUrl(String id, String name) {

        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyID = tinyDB.getString(Const.COMPANY_ID);
        // TranslateLabDTO translateLabDTO = new TranslateLabDTO(securityToken, id, companyID);
        AfgaReportDTO translateLabDTO = new AfgaReportDTO(securityToken, id, "");
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            RestClient.getInstance().GetAgfaRadiologyImageUrl(translateLabDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    TranslateLabModel translateLabModel = (TranslateLabModel) result;

                    if (translateLabModel != null) {

                        MobileOpResult mobileOpResult = translateLabModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String url = translateLabModel.getResultVal();
                              /*  String doc = "http://docs.google.com/gview?embedded=true&url=" + url;
                                String date = LocaleDateHelper.convertDateStringFormat(orderDate, "yyyy-MM-dd'T'hh:mm:ss", "dd_MM_yyyy");
                                Intent intent = new Intent(ServicesOrdersActivity.this, PdfViewBrowser.class);
                                intent.putExtra("url", url);
                                intent.putExtra("date_report", date);
                                intent.putExtra("name", name);
                                startActivity(intent);*/

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                    } else
                        ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);


                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void apptGetDetailsView(long apptId, int position) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);
            Intent intent = getIntent();

            if (intent != null) {

                ApptDetailDTO apptDetailDTO = new ApptDetailDTO(securityToken, apptId, companyId);

                RestClient.getInstance().apptGetDetails(apptDetailDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        ApptDetailModel apptModel = (ApptDetailModel) result;

                        if (apptModel != null) {

                            MobileOpResult mobileOpResult = apptModel.getMobileOpResult();

                            if (mobileOpResult != null) {

                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                    if (apptModel.getAppt() != null) {
                                        /*if (!ValidationHelper.isNullOrEmpty(apptModel.getAppt().getOrdersRad())) {*/
                                        Intent intent = new Intent(ServicesOrdersActivity.this, AppointmentDetailActivity.class);
                                        String jsonObject  = new GsonBuilder().create().toJson(apptModel.getAppt());
                                        intent.putExtra(Const.APPTDETAILMODEL, jsonObject);
                                     //   intent.putExtra(Const.APPTDETAILMODEL, apptModel.getAppt());
                                        intent.putExtra(Const.ORDER_RAD_POSITION, position);
                                        startActivity(intent);
                                       /* } else {
                                            ErrorMessage.getInstance().showWarning(ServicesOrdersActivity.this, "No Orders Rad Found");
                                        }*/
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(ServicesOrdersActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(ServicesOrdersActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(ServicesOrdersActivity.this, errorMessage);
                        }
                    }
                });

            }
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available.");
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent != null) {
                boolean isUpdate = intent.getBooleanExtra("refresh", false);
                if (isUpdate)
                    apptGetDetails();
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        /*LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);*/
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        if (SECONDARY_APPT_REFRESH) {
            SECONDARY_APPT_REFRESH = false;
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            // apptGetDetails();

        }
        super.onResume();

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
                ErrorMessage.getInstance().showSuccessGreen(ServicesOrdersActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(ServicesOrdersActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(ServicesOrdersActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    public void showDialog(String question, String answer, long id, String orderNumber, boolean isLab, boolean isRad, String name) {

        try {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.question_dialogue);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LightEdittext input = (LightEdittext) dialog.findViewById(R.id.add_sugar);
            RegularTextView questionTxt = (RegularTextView) dialog.findViewById(R.id.question_txt);
            questionTxt.setText(question);

            RegularTextView dialogButton = dialog.findViewById(R.id.cancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RegularTextView dialogButton1 = dialog.findViewById(R.id.add);
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String value = input.getText().toString();

                    if (!ValidationHelper.isNullOrEmpty(value)) {

                        if (value.equalsIgnoreCase(answer)) {
                            if (isRad)
                                showReportSelectionDialog(orderNumber, name);
                            else if (isLab)
                                labReport(id, name);

                            patientUsedSecurityQuestion();

                        } else {
                            ErrorMessage.getInstance().showValidationMessage(ServicesOrdersActivity.this, input, getString(R.string.sorry_not_match));
                        }

                    } else
                        ErrorMessage.getInstance().showValidationMessage(ServicesOrdersActivity.this, input, "Please enter value ");

                    dialog.dismiss();

                }
            });

            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showReportSelectionDialog(String id, String name) {
        // getRadReport(model.getId(), model.getName());
        // getRadReport(id, name);
        Dialog dialog = new Dialog(this, R.style.ios_dialog_style);
        dialog.setContentView(R.layout.dialog_report_selection_layout);
        dialog.setCancelable(true);
        LightButton viewReportBtn = dialog.findViewById(R.id.view_report_btn);
        LightButton viewImageBtn = dialog.findViewById(R.id.view_image_btn);
        LightButton doNotRemindBtn = dialog.findViewById(R.id.do_not_remind_btn);
//        // if button is clicked, close the custom dialog

        viewReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReport(id, name);
            }
        });

        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReportImageUrl(id, name);
            }
        });

        doNotRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getRadReportImageUrl("4304552", "");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void patientUsedSecurityQuestion() {

        String patientName = tinyDB.getString(Const.PATIENT_NAME);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String patientKey = patientId + "_key";
        tinyDB.putString(patientKey, String.valueOf(patientId));
    }

    void transformViews() {

        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            refresh.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params2);
            toolbar_left_iv.setRotation(180);

            container_Card.setVisibility(View.INVISIBLE);
            container_card1.setVisibility(View.VISIBLE);

            book_home_visit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            pay_services_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            queueTicketLabBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            cancelQticketLabBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            getMedQueueTicket.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            cancelMedTQicket.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);

        } else if (TextUtil.getEnglish(this)) {
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            refresh.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolbar_left_iv.setLayoutParams(params2);

            container_Card.setVisibility(View.VISIBLE);
            container_card1.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            apptGetDetails();
        }
    }
}
