package com.telemedicine.myclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.views.BoldTextView;
import com.telemedicine.myclinic.views.LightButton;
import com.telemedicine.myclinic.views.LightTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

import static com.telemedicine.myclinic.util.Const.COMPANY_NAME_EN;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_AR;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_NAME_EN;

public class AppointmentConfirmedAnonymousActivity extends BaseActivity {

    @BindView(R.id.doctor_image)
    CircleImageView doctorImageView;

    @BindView(R.id.doctor_name)
    BoldTextView doctorNameTv;

    @BindView(R.id.datetime_value)
    LightTextView datetimeValueTv;

    @BindView(R.id.confirm)
    LightButton confirm;

    @BindView(R.id.add_calendar)
    LightButton add_calendar;


    @BindView(R.id.location_name)
    BoldTextView location_name;

    @BindView(R.id.doctor_profession)
    LightTextView doctor_profession;

    @BindView(R.id.chat_bot_bg)
    RelativeLayout chatBotBg;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chatBot_rounded)
    View chatBot_rounded;

    @BindView(R.id.chat_bot_web_view)
    WebView chatBotWebView;


    String doctorName = "";
    String doctorDate = "";
    String doctorTime = "";
    String doctorImage = "";
    String specialityNameEn = "";
    String specialityNameAr = "";
    String companyName = "";
    boolean isTelemedicine = false;
    boolean isChatBotVisible = false;
    String weekDay = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int layout() {
        return R.layout.activity_appointment_confirmed_anonymous;
    }


    private void init() {

        Intent intent = getIntent();
        tinyDB = new TinyDB(this);
        if (intent != null) {
            doctorName = intent.getStringExtra("DoctorName");
            doctorDate = intent.getStringExtra("Date_local");
            doctorTime = intent.getStringExtra("Time");
            doctorImage = intent.getStringExtra("image");
            weekDay = intent.getStringExtra("week_Day");
            specialityNameEn = intent.getStringExtra(SPECIALITY_NAME_EN);
            specialityNameAr = intent.getStringExtra(SPECIALITY_NAME_AR);
            companyName = intent.getStringExtra(COMPANY_NAME_EN);
            isTelemedicine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
        }
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
        populateViews();
    }

    private void populateViews() {
        Glide.with(this).load(doctorImage).placeholder(R.drawable.doctr).into((ImageView) doctorImageView);
        doctorNameTv.setText(doctorName);
        String time = doctorTime;
        if (TextUtil.getArabic(this)) {
            time = time.replaceAll(" AM", " ص");
            time = time.replaceAll(" PM", " م");
        }
        datetimeValueTv.setText(weekDay+"  "+doctorDate + ", " + time);

        if(TextUtil.getArabic(AppointmentConfirmedAnonymousActivity.this)){
            doctor_profession.setText(specialityNameAr);
        }else{
            doctor_profession.setText(specialityNameEn);
        }

        location_name.setText(companyName);

        if (isTelemedicine) {
            location_name.setVisibility(View.GONE);
        } else {
            location_name.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        if (isChatBotVisible) {
            openChatBotBg(null);
            return;
        } else {
            super.onBackPressed();
        }
    }

    public void openChatBot(View view) {
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

    public void openChatBotBg(View view) {
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

    public void addToCalendarEvent(View view) {
        String title = getString(R.string.appointment_with) + " " + doctorName + " " + getString(R.string.at_my_clinic);
        String msg = "You  " + getString(R.string.has_an_appointment_with) + " " + doctorName + " " + getString(R.string.at) + " " + doctorDate + "," + doctorTime +
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

                String title = getString(R.string.appointment_with) + doctorName + getString(R.string.at_my_clinic);
                String msg = "You  " + getString(R.string.has_an_appointment_with) + " " + doctorName + " " + getString(R.string.at) + " " + doctorDate + "," + doctorTime +
                        getString(R.string.please_remember_to_arrive);
                writeToCalendar(title, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(AppointmentConfirmedAnonymousActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    void writeToCalendar(String title, String comment) {

        try {

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, milliseconds(doctorDate));
            values.put(CalendarContract.Events.DTEND, milliseconds(doctorDate));
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
            ).show();

            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long milliseconds(String date) {
        //String date_ = date;
        // SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        finish();
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    public void DashBoard(View view) {
        finish();
       /* Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }
}