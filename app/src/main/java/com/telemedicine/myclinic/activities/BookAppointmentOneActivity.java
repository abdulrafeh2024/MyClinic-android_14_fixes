package com.telemedicine.myclinic.activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.reginald.editspinner.EditSpinner;
import com.telemedicine.myclinic.adapters.DoctorsProfileAdapter;
import com.telemedicine.myclinic.adapters.SpinnerCustomAdapter;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.bookAppointment.GetRefBySpecialtyIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefDoctorsBySpecialtyModel;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesModel;
import com.telemedicine.myclinic.models.bookAppointment.SpecialtiesModel;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.BoldCheckBox;
import com.telemedicine.myclinic.views.BoldTextInputLayout;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightEdittext;
import com.telemedicine.myclinic.views.LightSpinnerEdittext;
import com.telemedicine.myclinic.webservices.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.telemedicine.myclinic.util.Const.DATE;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.DOCTOR_IMAGE_URL;
import static com.telemedicine.myclinic.util.Const.DOCTOR_NAME;
import static com.telemedicine.myclinic.util.Const.DOCTOR_SPECIALITY;
import static com.telemedicine.myclinic.util.Const.ISTELEMEDICINE_KEY;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

public class BookAppointmentOneActivity extends BaseActivity implements DoctorsProfileAdapter.OnCardClickListner {

    @BindView(R.id.appt_date_value)
    LightEdittext apptDateValue;

    @BindView(R.id.select_specialities_spinner)
    LightSpinnerEdittext selectSpecialitiesSpinner;

    @BindView(R.id.select_doctor_spinner)
    LightSpinnerEdittext selectDoctorSpinner;

    @BindView(R.id.listOfDoctors)
    RecyclerView listOfDoctors;

    @BindView(R.id.date_appointment)
    BoldTextInputLayout date_appointment;

    @BindView(R.id.department_select)
    BoldTextInputLayout department_select;

    @BindView(R.id.select_doctor)
    BoldTextInputLayout select_doctor;

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    @BindView(R.id.next_book_one)
    LightButton next_book_one;

    @BindView(R.id.container_)
    RelativeLayout container_;

    @BindView(R.id.available_chk)
    BoldCheckBox available_chk;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TinyDB tinyDB = null;

    boolean isTelemedcine = false;
    View viewOfDr = null;

    @BindView(R.id.chevron_left)
    ImageView chevronLeft;

    @BindView(R.id.chevron_right)
    ImageView chevronRight;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;

    boolean isChatBotVisible = false;


    LinearLayoutManager linearLayoutManager = null;

    String doctorImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transformView();
        init();
        getSpecilities();
    }

    private void init() {

        chatBotWebView.getSettings().setJavaScriptEnabled(true);
        chatBotWebView.setHorizontalScrollBarEnabled(false);

        chatBotWebView.getSettings().setLoadWithOverviewMode(true);
        chatBotWebView.getSettings().setUseWideViewPort(true);

        chatBotWebView.getSettings().setSupportZoom(true);
        chatBotWebView.getSettings().setBuiltInZoomControls(true);
        chatBotWebView.getSettings().setDisplayZoomControls(false);

        chatBotWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        chatBotWebView.setScrollbarFadingEnabled(false);
        loadChatBot();

        setDefaultDate();
        navigationInVisible();
        tinyDB = new TinyDB(this);
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

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        listOfDoctors.setLayoutManager
                (linearLayoutManager);
        listOfDoctors.setHasFixedSize(true);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(listOfDoctors);

        // it converts the item in the list to a string shown in EditText.
        selectSpecialitiesSpinner.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String selectedItem1 = (String) selectedItem.toString();

                getDoctorBySpecilityId(getSpecialitiesId(selectedItem1));

                return selectedItem1;
            }
        });


        selectDoctorSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int finalPosition = position - 1;

                listOfDoctors.scrollToPosition(finalPosition);

                listOfDoctors.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String select = selectDoctorSpinner.getText().toString();

                        if (viewOfDr != null)
                            viewOfDr.setBackgroundResource(R.color.colorWhite);

                        if (select.equalsIgnoreCase(getString(R.string.all_doctors))) {
                            selectDoctorSpinner.setText("");
                            selectDoctorSpinner.clearFocus();
                        }

                        RecyclerView.ViewHolder holder =
                                listOfDoctors.findViewHolderForAdapterPosition(finalPosition);

                        if (null != holder) {
                            holder.itemView.findViewById(R.id.doctor_container).setBackgroundResource(R.color.colorGrey);
                            viewOfDr = holder.itemView.findViewById(R.id.doctor_container);
                        }
                    }
                }, 50);

            }

        });

        Intent intent = getIntent();

        if (intent != null) {
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
        }


        selectSpecialitiesSpinner.setDropDownHeight(585);

    }

    @Override
    protected int layout() {
        return R.layout.activity_book_appointment_one;
    }



    public void openChatBot(View view){

        chatBotBg.setVisibility(View.VISIBLE);
        isChatBotVisible = true;
        loadChatBot();
     /*   Intent intent = new Intent(this, BrowserActivityPayment.class);
        intent.putExtra("calendar", false);
        intent.putExtra("url", "https://myclinic.hellotars.com/conv/Rg2PDe/");
        //  intent.putExtra("url", getString(R.string.contact_us_url));
        intent.putExtra(Const.TERMS_CONDITIONS_KEY, termsConditionTxt);
        startActivity(intent);*/
    }

    public void openChatBotBg(View view){
        chatBotBg.setVisibility(View.GONE);
        isChatBotVisible = false;
    }


    private void loadChatBot() {

        chatBotWebView.loadUrl("https://myclinic.hellotars.com/conv/Rg2PDe/");
        chatBotWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {

        if(isChatBotVisible){
            openChatBotBg(null);
            return;
        }
        super.onBackPressed();
    }

    public void Next(View view) {

        boolean isAvailableCheck = available_chk.isChecked();
        String apptDate = apptDateValue.getText().toString();
        String speciality = selectSpecialitiesSpinner.getText().toString();
        String selectedDoctor = selectDoctorSpinner.getText().toString();

        if (ValidationHelper.isNullOrEmpty(apptDate)) {
            TextUtil.tILErrorOne(this, date_appointment, apptDateValue);
        }

        if (ValidationHelper.isNullOrEmpty(speciality)) {
            TextUtil.tILErrorOne(this, department_select, selectSpecialitiesSpinner);
        }

      /*  if (ValidationHelper.isNullOrEmpty(selectedDoctor) && !isAvailableCheck) {
            TextUtil.tILErrorOne(this, select_doctor, selectDoctorSpinner);
        }*/

        if (ValidationHelper.isNullOrEmpty(apptDate)) {
            ErrorMessage.getInstance().showValidationMessage(this, apptDateValue, getString(R.string.select_date_of_appointment));
            return;
        }

        if (ValidationHelper.isNullOrEmpty(speciality)) {
            ErrorMessage.getInstance().showValidationMessage(this, selectSpecialitiesSpinner, getString(R.string.please_select_the_speciality));
            return;
        }

      /*  if (ValidationHelper.isNullOrEmpty(selectedDoctor) && !isAvailableCheck) {
            ErrorMessage.getInstance().showValidationMessage(this, selectDoctorSpinner, getString(R.string.please_select_doctor));
            return;
        }*/

        Intent intent;
        if (isAvailableCheck || ValidationHelper.isNullOrEmpty(selectedDoctor))
            intent = new Intent(this, WeekScheduleActivity.class);
        else
            intent = new Intent(this, BookAppointmentTwoActivity.class);
        intent.putExtra(DOCTOR_NAME, selectedDoctor);
        intent.putExtra(DOCTOR_SPECIALITY, speciality);
        intent.putExtra(DATE, apptDate);
        intent.putExtra(SPECIALITY_ID, getSpecialitiesId(speciality));
        intent.putExtra(DOCTOR_ID, getDoctorId(selectedDoctor));
        intent.putExtra(ISTELEMEDICINE_KEY, isTelemedcine);
        intent.putExtra(DOCTOR_IMAGE_URL, doctorImageUrl);
        startActivityForResult(intent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @OnClick(R.id.appt_date_value)
    void apptDate() {
        DatePickerDialog datePicker = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.setCanceledOnTouchOutside(false);
        datePicker.show();
    }

    private void updateDOB() {
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        apptDateValue.setText(sdf.format(myCalendar.getTime()));
    }

    ArrayList<SpecialtiesModel> specialtiesList = null;

    void getSpecilities() {

        if (ConnectionUtil.isInetAvailable(this)) {

            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);

            if(isTelemedcine){
                companyId = "";
            }
            GetRefSpecialtiesDTO getRefSpecialtiesDTO = new GetRefSpecialtiesDTO(securityToken, isTelemedcine,companyId);

            RestClient.getInstance().getRefSpecialties(getRefSpecialtiesDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    GetRefSpecialtiesModel getRefSpecialtiesModel = (GetRefSpecialtiesModel) result;

                    if (getRefSpecialtiesModel != null) {

                        MobileOpResult mobileOpResult = getRefSpecialtiesModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                specialtiesList = getRefSpecialtiesModel.getSpecialtiesList();

                                if (!ValidationHelper.isNullOrEmpty(specialtiesList)) {
                                    setSpecialities(specialtiesList);
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {

                                    if (TextUtil.getEnglish(BookAppointmentOneActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(BookAppointmentOneActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }

                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    ArrayList<com.telemedicine.myclinic.models.homemodels.DoctorsProfile> doctorsProfiles = null;

    void getDoctorBySpecilityId(String specialityId) {

        if (ConnectionUtil.isInetAvailable(this)) {
            showWaitDialog();

            String securityToken = tinyDB.getString(Const.TOKEN_KEY);
            String companyId = tinyDB.getString(Const.COMPANY_ID);

            if(isTelemedcine){
                companyId = "";
            }
            GetRefBySpecialtyIdDTO getRefSpecialtiesDTO = new GetRefBySpecialtyIdDTO(securityToken, specialityId, isTelemedcine,companyId);
            RestClient.getInstance().getRefDoctorsBySpecialty(getRefSpecialtiesDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();

                    GetRefDoctorsBySpecialtyModel getRefDoctorsBySpecialtyModel = (GetRefDoctorsBySpecialtyModel) result;

                    if (getRefDoctorsBySpecialtyModel != null) {

                        MobileOpResult mobileOpResult = getRefDoctorsBySpecialtyModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                doctorsProfiles = getRefDoctorsBySpecialtyModel.getDoctorsProfileArrayList();

                                if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {
                                    setDoctors(doctorsProfiles);
                                } else {
                                    clearDoctorsSpinner();
                                }

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(BookAppointmentOneActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    } else if (TextUtil.getArabic(BookAppointmentOneActivity.this)) {
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                    }
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMessage);

                    } else
                        ErrorMessage.getInstance().showError(BookAppointmentOneActivity.this, errorMessage);
                }
            });
        } else {
            ErrorMessage.getInstance().showWarning(this, "Internet is not available");
        }
    }

    private void clearDoctorsSpinner() {
        ArrayList<com.telemedicine.myclinic.models.homemodels.DoctorsProfile> doctorsProfiles = new ArrayList<>();
        DoctorsProfileAdapter doctorsProfileAdapter = new DoctorsProfileAdapter(BookAppointmentOneActivity.this, doctorsProfiles);
        listOfDoctors.setAdapter(doctorsProfileAdapter);
        ArrayList<String> doctorsSpeciality = new ArrayList<>();
        android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(BookAppointmentOneActivity.this, R.layout.spinner_item_list, doctorsSpeciality);
        selectDoctorSpinner.setAdapter(cityAdapter);
        selectDoctorSpinner.setText("");
    }

    void setSpecialities(ArrayList<SpecialtiesModel> specialtiesList) {

        ArrayList<String> specialitiesStr = new ArrayList<>();
        String gender = tinyDB.getString(Const.PATIENT_GENDER);

        for (SpecialtiesModel specialtiesModel : specialtiesList) {


            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                    specialitiesStr.add(specialtiesModel.getNameEn());
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                    specialitiesStr.add(specialtiesModel.getNameAr());
                }
            }

            if (gender.equalsIgnoreCase("1")) {
                if (String.valueOf(specialtiesModel.getId()).equalsIgnoreCase("5637144584")) {
                    if (TextUtil.getEnglish(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                            specialitiesStr.remove(specialtiesModel.getNameEn());
                        }
                    } else if (TextUtil.getArabic(this)) {
                        if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                            specialitiesStr.remove(specialtiesModel.getNameAr());
                        }
                    }
                }
            }
        }


        android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, specialitiesStr);
        selectSpecialitiesSpinner.setAdapter(cityAdapter);
    }

    void setDoctors
            (ArrayList<com.telemedicine.myclinic.models.homemodels.DoctorsProfile> doctorsProfiles) {

        ArrayList<String> doctorsSpeciality = new ArrayList<>();
        doctorsSpeciality.add(getString(R.string.all_doctors));

        for (com.telemedicine.myclinic.models.homemodels.DoctorsProfile doctorsProfile : doctorsProfiles) {

            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(doctorsProfile.getNameEn())) {
                    doctorsSpeciality.add(doctorsProfile.getNameEn());
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(doctorsProfile.getNameAr())) {
                    doctorsSpeciality.add(doctorsProfile.getNameAr());
                }
            }
        }

        android.widget.ListAdapter cityAdapter = new SpinnerCustomAdapter(this, R.layout.spinner_item_list, doctorsSpeciality);
        selectDoctorSpinner.setAdapter(cityAdapter);
        selectDoctorSpinner.setText("");

        setDoctorOnList(doctorsProfiles);
    }

    DoctorsProfileAdapter doctorsProfileAdapter = null;

    void setDoctorOnList
            (ArrayList<com.telemedicine.myclinic.models.homemodels.DoctorsProfile> doctorsProfiles) {

        if (!ValidationHelper.isNullOrEmpty(doctorsProfiles)) {
            container_.setVisibility(View.VISIBLE);
            doctorsProfileAdapter = new DoctorsProfileAdapter(this, doctorsProfiles);
            listOfDoctors.setAdapter(doctorsProfileAdapter);

            doctorsProfileAdapter.setOnCardClickListner(this);
            if (doctorsProfiles.size() == 1) {

                if (TextUtil.getEnglish(this))
                    selectDoctorSpinner.setText(doctorsProfiles.get(0).getNameEn());
                else if (TextUtil.getArabic(this))
                    selectDoctorSpinner.setText(doctorsProfiles.get(0).getNameAr());

                navigationInVisible();
            } else {
                navigationVisible();
            }
        } else {
            navigationInVisible();
        }

    }

    String getSpecialitiesId(String specialitiesStr) {

        String specialitiesId = "";

        for (SpecialtiesModel specialtiesModel : specialtiesList) {

            if (TextUtil.getEnglish(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameEn())) {
                    if (specialitiesStr.trim().equalsIgnoreCase(specialtiesModel.getNameEn().trim())) {
                        specialitiesId = String.valueOf(specialtiesModel.getId());
                    }
                }
            } else if (TextUtil.getArabic(this)) {
                if (!ValidationHelper.isNullOrEmpty(specialtiesModel.getNameAr())) {
                    if (specialitiesStr.trim().equalsIgnoreCase(specialtiesModel.getNameAr().trim())) {
                        specialitiesId = String.valueOf(specialtiesModel.getId());
                    }
                }
            }
        }

        return specialitiesId;
    }

    String getDoctorId(String doctorName) {

        String doctorId = "";
        for (com.telemedicine.myclinic.models.homemodels.DoctorsProfile doctorsProfile : doctorsProfiles) {

            if (doctorsProfile != null) {
                if (TextUtil.getEnglish(this)) {
                    if (doctorsProfile.getNameEn().trim().equalsIgnoreCase(doctorName.trim())) {
                        doctorId = String.valueOf(doctorsProfile.getDoctorId());
                        doctorImageUrl = doctorsProfile.getProfilePicUrl();
                    }
                } else if (TextUtil.getArabic(this)) {
                    if (doctorsProfile.getNameAr().trim().equalsIgnoreCase(doctorName.trim())) {
                        doctorId = String.valueOf(doctorsProfile.getDoctorId());
                        doctorImageUrl = doctorsProfile.getProfilePicUrl();
                    }
                }
            }
        }
        return doctorId;
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    @Override
    public void OnCardClicked(com.telemedicine.myclinic.models.homemodels.DoctorsProfile model,
                              int pos) {

        if (viewOfDr != null)
            viewOfDr.setBackgroundResource(R.color.colorWhite);

        if (model != null) {
            if (TextUtil.getEnglish(this))
                selectDoctorSpinner.setText(model.getNameEn());
            else if (TextUtil.getArabic(this))
                selectDoctorSpinner.setText(model.getNameAr());
        }
    }

    void setDefaultDate() {
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        apptDateValue.setText(sdf.format(myCalendar.getTime()));
    }

    void navigationVisible() {
        chevronLeft.setVisibility(View.VISIBLE);
        chevronRight.setVisibility(View.VISIBLE);
    }

    void navigationInVisible() {
        chevronLeft.setVisibility(View.INVISIBLE);
        chevronRight.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.chevron_left)
    void left() {
        if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
            listOfDoctors.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
        } else {
            listOfDoctors.smoothScrollToPosition(0);
        }
    }

    @OnClick(R.id.chevron_right)
    void right() {
        listOfDoctors.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            selectSpecialitiesSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            selectDoctorSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
            next_book_one.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            apptDateValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
        }
    }

}

