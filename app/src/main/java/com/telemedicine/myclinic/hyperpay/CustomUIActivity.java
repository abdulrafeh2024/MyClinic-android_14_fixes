package com.telemedicine.myclinic.hyperpay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.payment.BrandsValidation;
import com.oppwa.mobile.connect.payment.CheckoutInfo;
import com.oppwa.mobile.connect.payment.ImagesRequest;
import com.oppwa.mobile.connect.payment.PaymentParams;
import com.oppwa.mobile.connect.payment.card.CardPaymentParams;
import com.oppwa.mobile.connect.payment.token.TokenPaymentParams;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.ITransactionListener;
import com.oppwa.mobile.connect.provider.OppPaymentProvider;
import com.oppwa.mobile.connect.provider.ThreeDSWorkflowListener;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
/*import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;*/
import com.oppwa.mobile.connect.threeds.OppThreeDSConfig;
import com.telemedicine.myclinic.UtilProgressBar;
import com.telemedicine.myclinic.activities.BrowserActivity;
import com.telemedicine.myclinic.activities.CardListActivity;
import com.telemedicine.myclinic.activities.multiservices.ApptPaymentByServiceDTO;
import com.telemedicine.myclinic.eenum.Success;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.ApptPayServicesDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptPaymentDTO;
import com.telemedicine.myclinic.models.payStage.PayStageTwoModel;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.util.ConnectionUtil;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.LocaleDateHelper;
import com.telemedicine.myclinic.util.TextUtil;
import com.telemedicine.myclinic.util.TinyDB;
import com.telemedicine.myclinic.util.ValidationHelper;
import com.telemedicine.myclinic.views.RegularEdittext;
import com.telemedicine.myclinic.views.RegularTextView;
import com.telemedicine.myclinic.webservices.RestClient;

import java.util.ArrayList;
import java.util.Locale;

import br.com.moip.validators.CreditCard;

import static com.telemedicine.myclinic.util.Const.APPT_ID;
import static com.telemedicine.myclinic.util.Const.CARD_BRAND_NAME;
import static com.telemedicine.myclinic.util.Const.CARD_HOLDER_NAME;
import static com.telemedicine.myclinic.util.Const.CARD_NUMBER;
import static com.telemedicine.myclinic.util.Const.DOCTOR_ID;
import static com.telemedicine.myclinic.util.Const.EXPIRY_MONTH;
import static com.telemedicine.myclinic.util.Const.EXPIRY_YEAR;
import static com.telemedicine.myclinic.util.Const.OREGID_KEY;
import static com.telemedicine.myclinic.util.Const.PAYMENT_TOKEN_ID;
import static com.telemedicine.myclinic.util.Const.SERVICE_MODEL;
import static com.telemedicine.myclinic.util.Const.SPECIALITY_ID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an activity for show the integration of mobile sdk and custom UI.
 */
public class CustomUIActivity extends BasePaymentActivity implements ITransactionListener {

    private RegularEdittext cardHolderEditText;
    private RegularEdittext cardNumberEditText;
    private RegularEdittext cardExpiryMonthEditText;
    private RegularEdittext cardExpiryYearEditText;
    private RegularEdittext cardCvvEditText;
    private RegularTextView mAmountValuel;
    LinearLayout payment_container;
    RadioButton master_radion, visa_radio;
    private String checkoutId;
    long mApptId = 0;
    String speciality, doctorName, finalDate, amount, date;
    long apptBookType;
    String specialityId = "";
    String doctorId = "";
    boolean isTelemedcine = false;
    boolean isTeleInstant = false;
    boolean isInsurance = false;
    long insuranceId;
    String cardNumberComplete = "";

    ImageView toolbar_left_iv;
    private ITransactionListener transactionListener;

    //private IProviderBinder providerBinder;
 /*   private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            *//* we have a connection to the service *//*
            providerBinder = (IProviderBinder) service;
            providerBinder.addTransactionListener(CustomUIActivity.this);

            try {
                if (BuildConfig.IS_DEVELOPMENT) {
                    providerBinder.initializeProvider(Connect.ProviderMode.TEST);
                } else {
                    providerBinder.initializeProvider(Connect.ProviderMode.LIVE);
                }

            } catch (PaymentException ee) {
                hideWaitDialog();
                showErrorDialog(ee.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            providerBinder = null;
        }
    };*/

    ThreeDSWorkflowListener threeDSWorkflowListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            disableAutofill();

        paymentProvider = new OppPaymentProvider(this, Connect.ProviderMode.LIVE);
        paymentProvider.setThreeDSWorkflowListener(new ThreeDSWorkflowListener() {
            @Override
            public Activity onThreeDSChallengeRequired() {
                return CustomUIActivity.this;
            }
        });

        transactionListener = this;
    }

       /* threeDSWorkflowListener = new ThreeDSWorkflowListener() {
            @Override
            public Activity onThreeDSChallengeRequired() {
                // provide your Activity
                return getCurrentActivity();
            }
        };*/
    //  paymentProvider.setThreeDSWorkflowListener(this);
    // paymentProvider.setThreeDSWorkflowListener((ThreeDSWorkflowListener) this);

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Intent intent = new Intent(this, ConnectService.class);
        ///  startService(intent);
        //   bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //  unbindService(serviceConnection);
        //    stopService(new Intent(this, ConnectService.class));
    }

    String brandNameParam = "";
    String tokenId = "";
    String cardHolderIntent = "";
    String cardNumberIntent = "";
    String expiryMonthIntent = "";
    String expiryYearIntent = "";
    String cardBrandIntent = "";
    Boolean isFromCardListing = false;
    Boolean savedCard = false;

    ArrayList<ApptPayServicesDTO> apptPayServicesDTO = new ArrayList<>();
    String serviceModel = null;

    private void initViews() {

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("payment"));

        Intent intent = getIntent();
        if (intent != null) {
            checkoutId = intent.getStringExtra(Const.CHECK_OUT_ID);
            tokenId = intent.getStringExtra(PAYMENT_TOKEN_ID);
            serviceModel = intent.getStringExtra(SERVICE_MODEL);
            if (serviceModel != null) {
                apptPayServicesDTO = new Gson().fromJson(serviceModel, new TypeToken<ArrayList<ApptPayServicesDTO>>() {
                }.getType());
            }
            cardHolderIntent = intent.getStringExtra(CARD_HOLDER_NAME);
            cardNumberIntent = intent.getStringExtra(CARD_NUMBER);
            expiryMonthIntent = intent.getStringExtra(EXPIRY_MONTH);
            expiryYearIntent = intent.getStringExtra(EXPIRY_YEAR);
            cardBrandIntent = intent.getStringExtra(CARD_BRAND_NAME);
            speciality = intent.getStringExtra("speciality");
            doctorName = intent.getStringExtra("doctorName");
            finalDate = intent.getStringExtra("finalDate");
            amount = intent.getStringExtra("amount");
            mApptId = intent.getLongExtra(APPT_ID, 0);
            date = intent.getStringExtra(Const.DATE);
            apptBookType = intent.getLongExtra(Const.APPT_BOOK_TYPE, 0);
            specialityId = intent.getStringExtra(SPECIALITY_ID);
            doctorId = intent.getStringExtra(DOCTOR_ID);
            isTelemedcine = intent.getBooleanExtra(Const.ISTELEMEDICINE_KEY, false);
            isTeleInstant = intent.getBooleanExtra(Const.ISTELEINSTANT_KEY, false);
            isInsurance = intent.getBooleanExtra(Const.IS_INSURANCE_KEY, isInsurance);
            insuranceId = intent.getLongExtra(Const.INSURANCE_ID, insuranceId);
            cardNumberComplete = intent.getStringExtra("card_number_intent");
            isFromCardListing = intent.getBooleanExtra(Const.IS_FROM_CARD_LISTING, false);
        }

        cardHolderEditText = findViewById(R.id.holder_edit_text);
        if (cardHolderIntent != null) {
            cardHolderEditText.setText(cardHolderIntent);
        }

        // cardHolderEditText.setText(Constants.Config.CARD_HOLDER_NAME);

        cardNumberEditText = findViewById(R.id.number_edit_text);
        if (cardNumberIntent != null) {
            cardNumberEditText.setText(cardNumberIntent);
        }

        //cardNumberEditText.setText(Constants.Config.CARD_NUMBER);

        cardExpiryMonthEditText = findViewById(R.id.expiry_month_edit_text);
        if (expiryMonthIntent != null) {
            cardExpiryMonthEditText.setText(expiryMonthIntent);
        }
        //cardExpiryMonthEditText.setText(Constants.Config.CARD_EXPIRY_MONTH);

        cardExpiryYearEditText = findViewById(R.id.expiry_year_edit_text);
        if (expiryYearIntent != null) {
            cardExpiryYearEditText.setText(expiryYearIntent.substring(2, 4));
        }

        // cardExpiryYearEditText.setText(Constants.Config.CARD_EXPIRY_YEAR);

        cardCvvEditText = findViewById(R.id.cvv_edit_text);
        // cardCvvEditText.setText(Constants.Config.CARD_CVV);
        mAmountValuel = findViewById(R.id.amount_value);

        master_radion = findViewById(R.id.master_radio);

        visa_radio = findViewById(R.id.visa_radio);

        mAmountValuel.setText(amount + " " + getString(R.string.sar));

        if (cardBrandIntent != null) {
            if (cardBrandIntent.equals("VISA")) {
                visa_radio.setChecked(true);
                master_radion.setChecked(false);
            } else {
                visa_radio.setChecked(false);
                master_radion.setChecked(true);
            }
        }

        findViewById(R.id.button_pay_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (providerBinder != null && checkFields()) {

                if (!isFromCardListing) {
                    showCardSavingDialog();
                } else {

                    if (cardNumberComplete != null) {
                        if (CardListActivity.Companion.isCardExists(cardNumberComplete)) {
                            savedCard = false;
                            checkValidations();
                        } else {
                            showCardSavingDialog();
                        }
                    } else {
                        if (CardListActivity.Companion.isCardExists(cardNumberEditText.getText().toString())) {
                            savedCard = false;
                            checkValidations();
                        } else {
                            showCardSavingDialog();
                        }
                    }
                }
            }
        });

        findViewById(R.id.visa_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                master_radion.setChecked(false);
            }
        });

        findViewById(R.id.master_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visa_radio.setChecked(false);
            }
        });
        toolbar_left_iv = findViewById(R.id.toolbar_left_iv);
        findViewById(R.id.toolbar_left_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        payment_container = findViewById(R.id.payment_container);

        transformView();

    }

    private void checkValidations() {
        if (checkFields()) {

            String brandName = "";

            if (tokenId != null) {
                if (tokenId.isEmpty()) {
                    try {
                        brandName = new CreditCard(cardNumberEditText.getText().toString()).getBrand().name();
                    } catch (Exception e) {
                        ErrorMessage.getInstance().showValidationMessage(CustomUIActivity.this, visa_radio, getString(R.string.card_number_is_not_valid));

                    }
                } else {
                    brandName = cardBrandIntent;
                }
            } else {
                if (cardNumberComplete != null) {
                    brandName = new CreditCard(cardNumberComplete).getBrand().name();
                } else {
                    try {
                        brandName = new CreditCard(cardNumberEditText.getText().toString()).getBrand().name();
                    } catch (Exception e) {
                        ErrorMessage.getInstance().showValidationMessage(CustomUIActivity.this, visa_radio, getString(R.string.card_number_is_not_valid));
                    }
                }
            }

            String cardBrand = "";

            if (master_radion.isChecked()) {
                if (brandName.equalsIgnoreCase("MASTERCARD")) {
                    brandNameParam = "MASTERCARD";
                } else {

                    ErrorMessage.getInstance().showValidationMessage(CustomUIActivity.this, visa_radio, getString(R.string.please_select_correct_brand));

                    return;
                }
            } else if (visa_radio.isChecked()) {
                if (brandName.equalsIgnoreCase("VISA")) {
                    brandNameParam = "VISA";
                } else {

                    ErrorMessage.getInstance().showValidationMessage(CustomUIActivity.this, master_radion, getString(R.string.please_select_correct_brand));
                    return;
                }
            }

            if (!TextUtils.isEmpty(checkoutId)) {

//                        new IOSDialog.Builder(CustomUIActivity.this)
//
//                                .setTitle(getString(R.string.my_clininc))
//                                .setMessage(getString(R.string.msg_appoinment_payment_refund))
//                                .setPositiveButton(getString(R.string._continue), (dialogInterface, i) -> requestCheckoutInfo(checkoutId))
//                                .setNegativeButton(getString(R.string.cancel), null).show();

               /* TextUtil.dialgouePayment(CustomUIActivity.this, getString(R.string.msg_appoinment_payment_refund), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestCheckoutInfo(checkoutId);
                    }
                });*/

                requestCheckoutInfo(checkoutId);
                // pay(checkoutId);
            }
        }
    }

    private void showCardSavingDialog() {
        TextUtil.dialogCardSaving(CustomUIActivity.this, getString(R.string.want_to_save_card), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savedCard = true;
                checkValidations();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savedCard = false;
                checkValidations();
            }
        });
    }

    private boolean checkFields() {
        if (cardHolderEditText.getText().length() == 0 ||
                cardNumberEditText.getText().length() == 0 ||
                cardExpiryMonthEditText.getText().length() == 0 ||
                cardExpiryYearEditText.getText().length() == 0 ||
                cardCvvEditText.getText().length() == 0) {

            ErrorMessage.getInstance().showError(this, getResources().getString(R.string.error_empty_fields));

            //  showAlertDialog(R.string.error_empty_fields);

            return false;
        }

        return true;
    }

    private OppPaymentProvider paymentProvider;
    TokenPaymentParams tokenPaymentParams;
    PaymentParams paymentParams;
    Transaction transaction;

    private void pay(String checkoutId) {
        try {

            if (tokenId != null) {
                if (!tokenId.isEmpty()) {
                    tokenPaymentParams = new TokenPaymentParams(checkoutId, tokenId, brandNameParam, cardCvvEditText.getText().toString());
                    tokenPaymentParams.setShopperResultUrl(getString(R.string.checkout_ui_callback_scheme) + "://result");
                    transaction = new Transaction(tokenPaymentParams);
                } else {
                    paymentParams = createPaymentParams(checkoutId);
                    paymentParams.setShopperResultUrl(getString(R.string.checkout_ui_callback_scheme) + "://result");
                    transaction = new Transaction(paymentParams);
                }
            } else {
                paymentParams = createPaymentParams(checkoutId);
                // paymentParams.setShopperResultUrl(getString(R.string.checkout_ui_callback_scheme) + "://result");
                paymentParams.setShopperResultUrl(getString(R.string.checkout_ui_callback_scheme) + "://callback");
                transaction = new Transaction(paymentParams);
            }
            paymentProvider.submitTransaction(transaction, transactionListener);
            showWaitDialog();
        } catch (PaymentException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideWaitDialog();
                   /* ErrorMessage.getInstance().showError(CustomUIActivity.this, "payment Exception Error Code:" +
                            e.getError().getErrorCode() + "Payment Error msg" + e.getError().getErrorMessage() +
                            "Payment Error info" + e.getError().getErrorInfo() +
                            e.getMessage() + "." + e.getLocalizedMessage());*/
                    if(e.getError().getErrorInfo() != null){
                        try {
                            JSONObject object = new JSONObject(e.getError().getErrorInfo());
                            if(object.has("description")){
                                String errorDescription = object.getString("description");
                                ErrorMessage.getInstance().showError(CustomUIActivity.this,errorDescription);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CustomUIActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else if(e.getError().getErrorMessage() != null){
                        ErrorMessage.getInstance().showError(CustomUIActivity.this,e.getError().getErrorMessage());
                    }
                }
            });

            //   UtilProgressBar.showErrorMessage(this, e.getMessage());
        }
    }

    private PaymentParams createPaymentParams(String checkoutId) throws PaymentException {
        String cardHolder = cardHolderEditText.getText().toString();
        String cardNumber = "";
        if (cardNumberComplete != null) {
            cardNumber = cardNumberComplete;
        } else {
            cardNumber = cardNumberEditText.getText().toString();
        }
        String cardExpiryMonth = cardExpiryMonthEditText.getText().toString();
        String cardExpiryYear = cardExpiryYearEditText.getText().toString();
        String cardCVV = cardCvvEditText.getText().toString();

        String cardBrand = "";

        if (master_radion.isChecked())
            cardBrand = Constants.Config.CARD_MASTER;
        else if (visa_radio.isChecked())
            cardBrand = Constants.Config.CARD_VISA;

        return new CardPaymentParams(
                checkoutId,
                cardBrand,
                cardNumber,
                cardHolder,
                cardExpiryMonth,
                "20" + cardExpiryYear,
                cardCVV
        ).setTokenizationEnabled(true);
    }

    @Override
    public void brandsValidationRequestSucceeded(BrandsValidation brandsValidation) {

    }

    @Override
    public void brandsValidationRequestFailed(PaymentError paymentError) {

    }

    @Override
    public void imagesRequestSucceeded(ImagesRequest imagesRequest) {

    }

    @Override
    public void imagesRequestFailed() {

    }

    private void requestCheckoutInfo(String checkoutId) {
        pay(checkoutId);
    }

    @Override
    public void paymentConfigRequestSucceeded(final CheckoutInfo checkoutInfo) {

        if (checkoutInfo == null) {
            //UtilProgressBar.showErrorMessage(this, getString(R.string.error_message));
            ErrorMessage.getInstance().showError(CustomUIActivity.this, getString(R.string.cardProblem));
            return;
        }

        /* Get the resource path from checkout info to request the payment status later. */
        resourcePath = checkoutInfo.getResourcePath();
        telePayStage2(resourcePath);
    }

    @Override
    public void paymentConfigRequestFailed(PaymentError paymentError) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
              /*  ErrorMessage.getInstance().showError(CustomUIActivity.this, paymentError.getErrorMessage() + "\n"
                        + "Error Info Config:" + paymentError.getErrorInfo() + ",Error Code:" + paymentError.getErrorCode());*/

                if(paymentError.getErrorInfo() != null){
                    try {
                        JSONObject object = new JSONObject(paymentError.getErrorInfo());
                        if(object.has("description")){
                            String errorDescription = object.getString("description");
                            ErrorMessage.getInstance().showError(CustomUIActivity.this,errorDescription);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CustomUIActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else if (paymentError.getErrorMessage() != null){
                    ErrorMessage.getInstance().showError(CustomUIActivity.this,paymentError.getErrorMessage());
                }
            }
        });
    }

    @Override
    public void transactionCompleted(Transaction transaction) {
        hideWaitDialog();
        if (transaction == null) {
            ErrorMessage.getInstance().showError(this, getString(R.string.error_message));
            return;
        }

        if (transaction.getTransactionType() == TransactionType.SYNC) {
            if (resourcePath != null) {
                telePayStage2(resourcePath);
            } else {
                try {
                    paymentProvider.requestCheckoutInfo(checkoutId, transactionListener);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        } else if (transaction.getTransactionType() == TransactionType.ASYNC) {

            hideWaitDialog();
            //UtilProgressBar.disMissDialogue();

            String url = transaction.getRedirectUrl();

            Intent browserIntent = new Intent(this, BrowserActivity.class);
            browserIntent.putExtra("url", url);
            startActivity(browserIntent);
            isReceived = false;
        } else {
            /* wait for the callback in the onNewIntent() */
            //   UtilProgressBar.dialogueShow(this, this.getResources().getString(R.string.progress_message_please_wait));
        }
    }

    @Override
    public void transactionFailed(Transaction transaction, PaymentError paymentError) {

        // UtilProgressBar.disMissDialogue();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
              //  ErrorMessage.getInstance().showError(CustomUIActivity.this, getString(R.string.cardProblem));
              //  ErrorMessage.getInstance().showError(CustomUIActivity.this, paymentError.getErrorMessage() + "\n"
                //        + "Error Info Failed:" + paymentError.getErrorInfo() + ",Error Code:" + paymentError.getErrorCode());
                // UtilProgressBar.showErrorMessage(CustomUIActivity.this, paymentError.getErrorMessage());

                if(paymentError.getErrorInfo() != null){
                    try {
                        JSONObject object = new JSONObject(paymentError.getErrorInfo());
                        if(object.has("description")){
                            String errorDescription = object.getString("description");
                            ErrorMessage.getInstance().showError(CustomUIActivity.this,errorDescription);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CustomUIActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else if (paymentError.getErrorMessage() != null){
                    ErrorMessage.getInstance().showError(CustomUIActivity.this,paymentError.getErrorMessage());
                }
            }
        });
    }

    private void showErrorDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ErrorMessage.getInstance().showError(CustomUIActivity.this, getString(R.string.cardProblem));
                //   UtilProgressBar.showErrorMessage(CustomUIActivity.this, message);
                // showAlertDialog(message);
            }
        });
    }

    private void showErrorDialog(PaymentError paymentError) {
        UtilProgressBar.showErrorMessage(this, paymentError.getErrorMessage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        // this will call when we use native chromer browser
        /* Check if the intent contains the callback scheme. */

        if (resourcePath != null && hasCallbackScheme(intent)) {
            telePayStage2(resourcePath);
        } else {
            try {
                paymentProvider.requestCheckoutInfo(checkoutId, transactionListener);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    void telePayStage2(String resourcePath) {

        TinyDB tinyDB = new TinyDB(this);
        if (BuildConfig.IS_DEVELOPMENT) {
          // resourcePath = "https://test.oppwa.com" + resourcePath;
          resourcePath = "https://oppwa.com" + resourcePath;
        } else {
            resourcePath = "https://oppwa.com" + resourcePath;
        }
        // resourcePath = TextUtil.urlStart(tinyDB) + resourcePath;

        String oRegId = tinyDB.getString(OREGID_KEY);
        Long OregIdLong = Long.parseLong(oRegId);

        showWaitDialog();
        // UtilProgressBar.dialogueShow(this, getResources().getString(R.string.progress_message_please_wait));

        if (cardNumberComplete != null) {
            cardNumberIntent = cardNumberComplete;
        } else {
            cardNumberIntent = cardNumberEditText.getText().toString();
        }
        RestClient.getInstance().payStage2(resourcePath, OregIdLong, brandNameParam, cardNumberIntent, savedCard, new OnResultListener() {
            @Override
            public void onResult(Object result, boolean status, String errorMessage) {

                PayStageTwoModel payStage2Result = (PayStageTwoModel) result;

                if (payStage2Result != null) {

                    MobileOpResult mobileOpResult = payStage2Result.getMobileOpResult();

                    if (mobileOpResult != null) {

                        if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                            String code = payStage2Result.getResultVal();

                            if (successfulCodes(code)) {

                                if (serviceModel != null) {
                                    multiservicesPayment(getString(R.string.thank_you_for_payment));
                                } else if (isTelemedcine) {
                                    bookApptOnline(isInsurance, insuranceId, amount);
                                } else if (isTeleInstant) {
                                    Intent intent = new Intent();
                                    intent.putExtra(APPT_ID, -1);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    confirmAppointment(getString(R.string.thank_you_for_payment));
                                }
                            } else {
                                hideWaitDialog();
                                //    UtilProgressBar.disMissDialogue();
                                ErrorMessage.getInstance().showError(CustomUIActivity.this, payStage2Result.getDescription());
                            }

                        } else {
                            hideWaitDialog();
                            // UtilProgressBar.disMissDialogue();

                            String errorMesg = "";

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                if (TextUtil.getEnglish(CustomUIActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                else if (TextUtil.getArabic(CustomUIActivity.this))
                                    errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                            }

                            if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                            }

                            ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMesg);
                        }

                    } else {
                        hideWaitDialog();
                        // UtilProgressBar.disMissDialogue();
                        ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                    }
                } else {
                    hideWaitDialog();
                    //UtilProgressBar.disMissDialogue();
                    ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                }
            }
        });
    }

    void confirmAppointment(String des) {

        String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
        String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
        if (!ValidationHelper.isNullOrEmpty(securityToken) && mApptId != 0) {

            ApptPaymentDTO apptPaymentDTO = new ApptPaymentDTO(securityToken, mApptId, companyId);

            RestClient.getInstance().apptPayment(apptPaymentDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    // UtilProgressBar.disMissDialogue();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                               /* UtilProgressBar.closeDialogue(CustomUIActivity.this, des, new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {*/

                                showPaymentSuccessAlert(des);
                                /*    }
                                });*/

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    void multiservicesPayment(String des) {

        String securityToken = new TinyDB(this).getString(Const.TOKEN_KEY);
        String companyId = new TinyDB(this).getString(Const.COMPANY_ID);
        if (!ValidationHelper.isNullOrEmpty(securityToken) && mApptId != 0) {

            ApptPaymentByServiceDTO apptPaymentByServiceDTO = new
                    ApptPaymentByServiceDTO(securityToken, mApptId, companyId, apptPayServicesDTO);

            RestClient.getInstance().ApptPaymentByServices(apptPaymentByServiceDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    hideWaitDialog();
                    // UtilProgressBar.disMissDialogue();

                    MobileOpResult mobileOpResult = (MobileOpResult) result;

                    if (mobileOpResult != null) {

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                               /* UtilProgressBar.closeDialogue(CustomUIActivity.this, des, new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {*/

                                showPaymentSuccessAlert(des);
                                /*    }
                                });*/

                            } else {

                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }

                                ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMesg);
                            }

                        } else
                            ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);

                    } else {
                        ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                    }
                }
            });
        }
    }

    IOSDialog iosDialog2 = null;

    private void showPaymentSuccessAlert(String des) {

        iosDialog2 = TextUtil.dialgoue(CustomUIActivity.this, getString(R.string.thank_you_for_payment), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Const.CONFIRM, true);
                intent.putExtra(APPT_ID, mApptId);
                setResult(RESULT_OK, intent);
                finish();
                iosDialog2.dismiss();
            }
        });

    }

    protected boolean successfulCodes(String code) {

        return getString(R.string.success).equals(code) || getString(R.string.success1).equals(code) ||
                getString(R.string.success2).equals(code) || getString(R.string.success3).equals(code) ||
                getString(R.string.success4).equals(code) || getString(R.string.success5).equals(code);
    }

    void bookApptOnline(boolean isInsurance, long insuranceId, String price) {
        String date1;
        date1 = LocaleDateHelper.convertDateStringFormat(date, "EEEE, dd MMMM yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);


        if (date1.isEmpty()) {
            date1 = date;
        }
        TinyDB tinyDB = new TinyDB(this);
        long patientId = tinyDB.getLong(Const.PATIENT_ID, 0);
        String securityToken = tinyDB.getString(Const.TOKEN_KEY);
        String companyId = tinyDB.getString(Const.COMPANY_ID);

        ApptCreateDTO apptCreateDTO = new ApptCreateDTO(securityToken, String.valueOf(patientId), specialityId, doctorId,
                String.valueOf(apptBookType), date1, isTelemedcine, isInsurance, insuranceId, companyId);

        if (ConnectionUtil.isInetAvailable(this)) {

            RestClient.getInstance().apptCreate(apptCreateDTO, new OnResultListener() {
                @Override
                public void onResult(Object result, boolean status, String errorMessage) {

                    ApptCreateModel apptCreateModel = (ApptCreateModel) result;

                    if (apptCreateModel != null) {

                        MobileOpResult mobileOpResult = apptCreateModel.getMobileOpResult();

                        if (mobileOpResult != null) {

                            if (mobileOpResult.getResult() == Success.SUCCESSCODE.getValue()) {

                                String apptNo = apptCreateModel.getApptNo();
                                long apptId = apptCreateModel.getApptId();
                                mApptId = apptId;
                                sendBroadCast();
                                sendBroadCastToPast();
                                confirmAppointment(getString(R.string.thank_you_for_payment));

                            } else {

                                hideWaitDialog();
                                //   UtilProgressBar.disMissDialogue();
                                String errorMesg = "";

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getBusinessErrorMessageEn())) {
                                    if (TextUtil.getEnglish(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageEn() + "\n";
                                    else if (TextUtil.getArabic(CustomUIActivity.this))
                                        errorMesg = mobileOpResult.getBusinessErrorMessageAr() + "\n";
                                }

                                if (!ValidationHelper.isNullOrEmpty(mobileOpResult.getTechnicalErrorMessage())) {
                                    errorMesg = errorMesg + "Technical Info : " + "\n" + mobileOpResult.getTechnicalErrorMessage();
                                }
                                if(mobileOpResult.getResult() == 600 || mobileOpResult.getResult() == 700){
                                    ErrorMessage.getInstance().showErrorYellow(CustomUIActivity.this, errorMesg);
                                }else{
                                    ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMesg);
                                }
                              //  ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMesg);
                            }

                        } else {
                            hideWaitDialog();
                            // UtilProgressBar.disMissDialogue();
                            ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                        }

                    } else {
                        hideWaitDialog();
                        // UtilProgressBar.disMissDialogue();
                        ErrorMessage.getInstance().showError(CustomUIActivity.this, errorMessage);
                    }
                }
            });

        } else {
            ErrorMessage.getInstance().showWarning(this, getString(R.string.internet_connection));
        }
    }

    boolean isReceived = false;
    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent != null) {
                /* Check if the intent contains the callback scheme. */
                String callBackScheme = intent.getStringExtra("payment");

                if (resourcePath != null && callBackScheme.contains(getString(R.string.checkout_ui_callback_scheme)) && !isReceived) {
                    isReceived = true;
                    telePayStage2(resourcePath);
                } else {
                    try {
                        paymentProvider.requestCheckoutInfo(checkoutId, transactionListener);
                    } catch (Exception e) {
                        Toast.makeText(CustomUIActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    void sendBroadCast() {
        Intent intent = new Intent("update_appts");
        // You can also include some extra data.
        intent.putExtra("refresh", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    void sendBroadCastToPast() {
        Intent intent = new Intent("past_appts");
        // You can also include some extra data.
        intent.putExtra("refresh", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    void transformView() {
        if (TextUtil.getArabic(this)) {
            toolbar_left_iv.setRotation(180);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) payment_container.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, R.id.cvv_text_input_layout);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            payment_container.setLayoutParams(params);
        }
        if (TextUtil.getEnglish(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) payment_container.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, R.id.cvv_text_input_layout);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            payment_container.setLayoutParams(params);
        }
    }

  /*  @Override
    public Activity onThreeDSChallengeRequired() {
        return getCurrentActivity();
    }*/
}

