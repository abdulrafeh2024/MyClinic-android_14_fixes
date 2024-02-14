package com.telemedicine.myclinic.activities.multiservices;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.COMPANY_ID;
import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.IS_INSURANCE_KEY;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.SERVICE_MODEL;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.telemedicine.myclinic.activities.BaseActivity;
import com.telemedicine.myclinic.activities.BookAppointmentThreeActivity;
import com.telemedicine.myclinic.activities.CardListActivity;
import com.telemedicine.myclinic.activities.NotificationsActivity;
import com.telemedicine.myclinic.activities.UpcomingAppointmentsActivity;
import com.telemedicine.myclinic.adapters.AdapterUpcomingAppointments;
import com.telemedicine.myclinic.adapters.NotificationsAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.hyperpay.CustomUIActivity;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.ApptPayServicesDTO;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.NotificationDTO;
import com.telemedicine.myclinic.models.NotificationMiniModel;
import com.telemedicine.myclinic.models.NotificationModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneDTO;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class ServicesListActivity extends BaseActivity implements ServicesListAdapter.OnCardClickListner, ServicesGroupAdapter.OnCardClickListner {

    @BindView(R.id.services_recycler_view)
    RecyclerView servicesRecyclerView;

    @BindView(R.id.refresh)
    ImageView refresh;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.amountTv)
    BoldTextView totalAmountTv;

    @BindView(R.id.total_amount)
    BoldTextView total_amount;

    @BindView(R.id.payService)
    LightButton payService;

    @BindView(R.id.no_services)
    RegularTextView noServices;

    private GroupAdapter<ViewHolder> groupAdapter;
    private LinearLayoutCompat linearLayoutManager;
    TinyDB tinyDB;

    long apptId = 0;
    String companyId = "";
    boolean isInsurance = false;

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        // onBackPressed();
        if (isRefreshed) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (isRefreshed) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh.setVisibility(View.GONE);
        tinyDB = new TinyDB(this);

        init();
        // String[] value = totalAmountTv.getText().toString().split("\\s+");
        // payStageOne(apptId, Double.parseDouble(value[0]), false, 0L);
        //String.format("%.2f", totalAmountDb, Locale.ENGLISH);
        payService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!apptPayServicesDTO.isEmpty()) {
                    if (totalAmountDb == 0) {
                        ErrorMessage.getInstance().showWarning(ServicesListActivity.this, getString(R.string.no_payment_required));
                    } else {
                        if(isInsurance){
                            markedServicePaidDialog();
                        }else{
                            BigDecimal amount = new BigDecimal(totalAmountDb);
                            BigDecimal amountBig = amount.setScale(2, RoundingMode.HALF_UP);
                            payStageOne(apptId, amountBig.doubleValue(), false, 0L);
                        }

                    }
                } else {
                    ErrorMessage.getInstance().showWarning(ServicesListActivity.this, getString(R.string.please_select_your_service));
                }
            }
        });
    }


    private void markedServicePaidDialog(){
        TextUtil.dialgouePayment(ServicesListActivity.this, getString(R.string.marked_paid_services), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BigDecimal amount = new BigDecimal(totalAmountDb);
                BigDecimal amountBig = amount.setScale(2, RoundingMode.HALF_UP);
                payStageOne(apptId, amountBig.doubleValue(), false, 0L);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    protected int layout() {
        return R.layout.activity_services_list;
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
                ErrorMessage.getInstance().showSuccessGreen(ServicesListActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameAr() + ". "+getString(R.string.please_press_to_continue));
            }else{
                ErrorMessage.getInstance().showSuccessGreen(ServicesListActivity.this,
                        getString(R.string.check_in_available_text)+ " " + appointmentEvent.getDoctorNameEn() + ". "+getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(ServicesListActivity.this, appointmentEvent.getErrorMSg());
        }
    }

    private void init() {
        groupAdapter = new GroupAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        servicesRecyclerView.setLayoutManager(linearLayoutManager);
        servicesRecyclerView.setAdapter(groupAdapter);
        Intent intent = getIntent();
        transformViews();
        if (intent != null) {
            apptId = intent.getLongExtra(APPT_ID, 0);
            companyId = intent.getStringExtra(COMPANY_ID);
            isInsurance = intent.getBooleanExtra(IS_INSURANCE_KEY, false);
        }

        getServices();
    }


    public void getServices() {
        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);

            if (!ValidationHelper.isNullOrEmpty(securityToken)) {

                ApptGetServicesByIdDTO apptGetServicesByIdDTO = new ApptGetServicesByIdDTO(securityToken,
                        String.valueOf(apptId),
                        companyId);

                RestClient.getInstance().ApptGetServicesById(apptGetServicesByIdDTO, new OnResultListener() {
                    @Override
                    public void onResult(Object result, boolean status, String errorMessage) {

                        hideWaitDialog();

                        apptPayServicesDTO.clear();
                        totalAmountDb = 0.0;
                        totalAmountTv.setText("");
                        productTypes.clear();
                        groupAdapter.clear();
                        groupAdapter.notifyDataSetChanged();

                        MultiServicesResponseModel multiServicesResponseModel = (MultiServicesResponseModel) result;

                        if (multiServicesResponseModel != null) {

                            MobileOpResult mobileOpResult = multiServicesResponseModel.getMobileOpResult();

                            if (mobileOpResult != null) {


                                if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                    ArrayList<ServiceItems> notificationMiniModel = multiServicesResponseModel.paymentOrder.serviceItems;

                                    if (!ValidationHelper.isNullOrEmpty(notificationMiniModel)) {
                                   /*   noApptmnt.setVisibility(View.GONE);
                                        loadNotificationList(notificationMiniModel);*/
                                        noServices.setVisibility(View.GONE);
                                        total_amount.setVisibility(View.VISIBLE);
                                        totalAmountTv.setVisibility(View.VISIBLE);
                                        payService.setVisibility(View.VISIBLE);
                                        loadServicesList(notificationMiniModel, multiServicesResponseModel.paymentOrder);
                                    }else{
                                        noServices.setVisibility(View.VISIBLE);
                                        total_amount.setVisibility(View.INVISIBLE);
                                        totalAmountTv.setVisibility(View.INVISIBLE);
                                        payService.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(ServicesListActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(ServicesListActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMesg);
                                }
                            } else {
                                ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                            }
                        } else {
                            ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showError(this, getString(R.string.internet_connection));
        }
    }

    ServicesListAdapter adapterServices = null;

    MultiServicesResponseModel multiServicesResponseModel = null;
    ArrayList<ApptPayServicesDTO> apptPayServicesDTO = new ArrayList<>();
    ArrayList<String> productTypes = new ArrayList<>();
    ArrayList<Long> caseTransferIds = new ArrayList<>();
    ArrayList<Long> unMarkCaseTransferIds = new ArrayList<>();
double deductibleAmount = 0.0;
    void loadServicesList(ArrayList<ServiceItems> serviceItems, PaymentOrder paymentOrder) {

        for (ServiceItems item : serviceItems) {
            if (item.ProductType != null) {
                if (!productTypes.contains(item.ProductType)) {
                    productTypes.add(item.ProductType);
                    Section section = new Section();
                    if (TextUtil.getEnglish(this)) {
                        section.setHeader(new ServiceHeaderAdapter(item.ProductType));
                    } else {
                        if(item.getProductTypeAr() != null){
                            section.setHeader(new ServiceHeaderAdapter(item.ProductTypeAr));
                        }else{
                            section.setHeader(new ServiceHeaderAdapter(item.getProductType()));
                        }

                    }

                    for (ServiceItems nestedItem : serviceItems) {
                        if (nestedItem.getProductType().equals(item.getProductType())) {

                            if (multiServicesResponseModel != null) {
                                if (!ValidationHelper.isNullOrEmpty(multiServicesResponseModel.paymentOrder.serviceItems)) {
                                    for (ServiceItems selectedService : multiServicesResponseModel.paymentOrder.serviceItems) {
                                        if (selectedService.getCaseTransId() == nestedItem.getCaseTransId()) {
                                               /* if(unmarkedId != nestedItem.getCaseTransId()){
                                                    nestedItem.setSelected(true);
                                                }*/
                                            nestedItem.setAmount(selectedService.Amount);
                                                /*apptPayServicesDTO.add(new ApptPayServicesDTO(nestedItem.getCaseTransId(),
                                                        nestedItem.getAmount(),
                                                        companyId, "HyperPay/Mobile"));*/
                                        }
                                    }
                                    for (Long caseId : caseTransferIds) {
                                        if (caseId == nestedItem.getCaseTransId()) {
                                            if (unmarkedId != nestedItem.getCaseTransId()) {
                                                nestedItem.setSelected(true);
                                                apptPayServicesDTO.add(new ApptPayServicesDTO(nestedItem.getCaseTransId(),
                                                        nestedItem.getAmount(),
                                                        companyId, "HyperPay/Mobile"));
                                            }
                                        }
                                    }
                                    section.add(new ServicesGroupAdapter(nestedItem, this, this));
                                } else {
                                    section.add(new ServicesGroupAdapter(nestedItem, this, this));
                                }

                            } else {
                                section.add(new ServicesGroupAdapter(nestedItem, this, this));
                            }
                        }
                    }
                    groupAdapter.add(section);
                    deductibleAmount = paymentOrder.getDeductibleAmount();
                    groupAdapter.notifyDataSetChanged();
                    // totalAmountTv.setText(String.format("%.2f", totalAmountDb) + " " + getString(R.string.sar));
                    if (multiServicesResponseModel != null) {
                        totalAmountTv.setText(multiServicesResponseModel.paymentOrder.AmountDue + " " + getString(R.string.sar));
                        totalAmountDb = multiServicesResponseModel.paymentOrder.AmountDue;
                    } else {
                        totalAmountTv.setText("0.0" + " " + getString(R.string.sar));
                       /* if(!caseTransferIds.isEmpty()){
                            totalAmountTv.setText(paymentOrder.AmountDue + " " + getString(R.string.sar));
                        }else{
                            totalAmountTv.setText("0.0" + " " + getString(R.string.sar));
                        }*/
                    }

                }
            }
        }

        /* for (ApptPayServicesDTO apptPayServicesDTO : apptPayServicesDTO) {
                                if (apptPayServicesDTO.getCaseTransRecId() == nestedItem.getCaseTransId()) {
                                    nestedItem.setSelected(true);
                                    totalAmountDb = totalAmountDb + nestedItem.getAmount();
                                }
                            }*/

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Section section = new Section();
            section.setHeader(new ServiceHeaderAdapter("Laboratory"));
            serviceItems.forEach(service->
                    section.add(new ServicesGroupAdapter(service, this::layout))
            );
            groupAdapter.add(section);
        }*/

       /* adapterServices = new ServicesListAdapter(this, serviceItems);
        servicesRecyclerView.setAdapter(adapterServices);
        servicesRecyclerView.setHasFixedSize(true);
        adapterServices.setOnCardClickListner(this);*/
    }


    double totalAmountDb = 0.0;

    long unmarkedId = 0L;

    @Override
    public void OnCardClicked(ServiceItems model, int pos, boolean isChecked) {

        if (isChecked) {
            unmarkedId = 0L;
            caseTransferIds.add(model.CaseTransId);
            getAmountByServiceId(apptId, caseTransferIds, unMarkCaseTransferIds, companyId);

        } else {
            caseTransferIds.remove(model.CaseTransId);
            unMarkCaseTransferIds.add(model.getCaseTransId());
            unmarkedId = model.getCaseTransId();
            getAmountByServiceId(apptId, caseTransferIds, unMarkCaseTransferIds, companyId);
        }


     /*   if (!apptPayServicesDTO.isEmpty()) {
            boolean isItemFound = false;
            for (int index = 0; index < apptPayServicesDTO.size(); index++) {
                if (apptPayServicesDTO.get(index).getCaseTransRecId() == model.getCaseTransId()) {
                    apptPayServicesDTO.remove(index);
                    totalAmountDb = totalAmountDb - model.getAmount();
                    isItemFound = true;
                    if (model.getResponsibility().equals("insurance")) {
                        getAmountByServiceId(apptId, "", String.valueOf(model.getCaseTransId()), companyId);
                    }
                    break;
                }
            }

            if (!isItemFound) {
                totalAmountDb = totalAmountDb + model.getAmount();
                apptPayServicesDTO.add(new ApptPayServicesDTO(model.CaseTransId,
                        model.getAmount(),
                        companyId, "HyperPay/Mobile"));
                if (model.getResponsibility().equals("insurance")) {
                    getAmountByServiceId(apptId, String.valueOf(model.getCaseTransId()), "", companyId);
                }
            }
        } else {
            totalAmountDb = totalAmountDb + model.getAmount();
            apptPayServicesDTO.add(new ApptPayServicesDTO(model.CaseTransId,
                    model.getAmount(),
                    companyId, "HyperPay/Mobile"));
            if (model.getResponsibility().equals("insurance")) {
                getAmountByServiceId(apptId, String.valueOf(model.getCaseTransId()), "", companyId);
            }
        }

        groupAdapter.notifyItemChanged(pos);
        totalAmountTv.setText(String.format("%.2f", totalAmountDb) + " " + getString(R.string.sar));*/
    }


    void testFunction() {
        /*if (!apptPayServicesDTO.isEmpty()) {
            boolean isItemFound = false;
            for (int index = 0; index < apptPayServicesDTO.size(); index++) {
                if (apptPayServicesDTO.get(index).getCaseTransRecId() == model.getCaseTransId()) {
                    apptPayServicesDTO.remove(index);
                    totalAmountDb = totalAmountDb - model.getAmount();
                    isItemFound = true;
                    if (model.getResponsibility().equals("insurance")) {
                        getAmountByServiceId(apptId, "", String.valueOf(model.getCaseTransId()), companyId);
                    }
                    break;
                }
            }

            if (!isItemFound) {
                totalAmountDb = totalAmountDb + model.getAmount();
                apptPayServicesDTO.add(new ApptPayServicesDTO(model.CaseTransId,
                        model.getAmount(),
                        companyId, "HyperPay/Mobile"));
                if (model.getResponsibility().equals("insurance")) {
                    getAmountByServiceId(apptId, String.valueOf(model.getCaseTransId()), "", companyId);
                }
            }
        } else {
            totalAmountDb = totalAmountDb + model.getAmount();
            apptPayServicesDTO.add(new ApptPayServicesDTO(model.CaseTransId,
                    model.getAmount(),
                    companyId, "HyperPay/Mobile"));
            if (model.getResponsibility().equals("insurance")) {
                getAmountByServiceId(apptId, String.valueOf(model.getCaseTransId()), "", companyId);
            }
        }

        groupAdapter.notifyItemChanged(pos);
        totalAmountTv.setText(String.format("%.2f", totalAmountDb) + " " + getString(R.string.sar));*/
    }

    boolean isDTORefresh = true;

    void getAmountByServiceId(long apptId, ArrayList<Long> caseTransferId, ArrayList<Long>
            unMarkedCaseTransRecId, String companyId) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();
            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            GetAmountByServiceIdDTO payStageOneDTO =
                    new GetAmountByServiceIdDTO(securityToken, apptId, caseTransferId, unMarkedCaseTransRecId, companyId);

            RestClient.getInstance().apptGetAmountByServiceId(payStageOneDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    MultiServicesResponseModel mMultiServicesResponseModel = (MultiServicesResponseModel) result;

                    if (mMultiServicesResponseModel != null) {

                        MobileOpResult mobileOpResult = mMultiServicesResponseModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {
                                multiServicesResponseModel = mMultiServicesResponseModel;
                                refreshServices();

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesListActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesListActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMesg);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                        }

                    } else {
                        ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void payStageOne(long apptId, double ammountDue, boolean isInsurance, long insuranceId) {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            if (ammountDue > 0) {
                //String companyId = tinyDB.getString(Const.COMPANY_ID);
                String customerEmail = tinyDB.getString(Const.EMAIL_KEY);
                String oregId = tinyDB.getString(Const.OREGID_KEY);
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

                                    getCards(checkOutId, String.valueOf(ammountDue), apptId);
                                } else {

                                    String errorMesg = "";

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                        if (TextUtil.getEnglish(ServicesListActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                        else if (TextUtil.getArabic(ServicesListActivity.this))
                                            errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                    if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                        errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                    }

                                    ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMesg);
                                }

                            } else {
                                ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                            }

                        } else {
                            ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                        }
                    }
                });
            }
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    void getCards(String checkOutId, String mAmount, long apptId) {

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

                                    //  Intent intent = new Intent(UpcomingAppointmentsActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(ServicesListActivity.this, CustomUIActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(Const.IS_FROM_CARD_LISTING, false);
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    intent.putExtra(SERVICE_MODEL, new Gson().toJson(apptPayServicesDTO));
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicineKey);
                                    // intent.putExtra(SPECIALITY_ID, specialtyId);
                                    //intent.putExtra(DATE, apptDate);
                                    //intent.putExtra(DOCTOR_ID,  String.valueOf(doctorId));
                                    //intent.putExtra(Const.APPT_BOOK_TYPE, apptsMiniModel.getApptBookType());
                                    /*            intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);*/
                                    //  intent.putExtra(Const.APPT_ID, 0);
                                  /*  intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);*/
                                    startActivityForResult(intent, 500);
                                } else {
                                    //  Intent intent = new Intent(UpcomingAppointmentsActivity.this, CustomUIActivity.class);
                                    Intent intent = new Intent(ServicesListActivity.this, CardListActivity.class);
                                    intent.putExtra("amount", mAmount);
                                    intent.putExtra(Const.APPT_ID, apptId);
                                    intent.putExtra(SERVICE_MODEL, new Gson().toJson(apptPayServicesDTO));
                                    //   intent.putExtra(ISTELEMEDICINE_KEY, isTelemedicineKey);
                                    //   intent.putExtra(SPECIALITY_ID, specialtyId);
                                    //   intent.putExtra(DATE, apptDate);
                                    //   intent.putExtra(DOCTOR_ID, String.valueOf(doctorId));
                                    //  intent.putExtra(Const.APPT_BOOK_TYPE, apptsMiniModel.getApptBookType());
                                /*    intent.putExtra(Const.IS_INSURANCE_KEY, mIsInsurance);
                                    intent.putExtra(Const.INSURANCE_ID, mInsuranceId);*/
                                    intent.putExtra(Const.CHECK_OUT_ID, checkOutId);
                                    startActivityForResult(intent, 500);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(ServicesListActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(ServicesListActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMesg);

                            }

                        } else {

                            ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                        }

                    } else {

                        ErrorMessage.getInstance().showError(ServicesListActivity.this, errorMessage);
                    }
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }

    }

    boolean isRefreshed = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == RESULT_OK) {
            isRefreshed = true;
            unmarkedId = 0L;
            caseTransferIds.clear();
            unMarkCaseTransferIds.clear();
            multiServicesResponseModel = null;
            refreshServices();
        }
    }

    private void refreshServices() {
      /*  totalAmountDb = 0.0;
        apptPayServicesDTO.clear();
        productTypes.clear();
        isRefreshed = true;
        totalAmountTv.setText ("");
        groupAdapter.clear();
        groupAdapter.notifyDataSetChanged();
*/


        getServices();
    }

    void transformViews() {
        if (TextUtil.getArabic(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar_left_iv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolbar_left_iv.setLayoutParams(params);
            toolbar_left_iv.setRotation(180);

        }
    }
}
