package com.telemedicine.myclinic.webservices;

import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telemedicine.myclinic.App;
import com.telemedicine.myclinic.activities.multiservices.ApptGetServicesByIdDTO;
import com.telemedicine.myclinic.activities.multiservices.ApptPaymentByServiceDTO;
import com.telemedicine.myclinic.activities.multiservices.GetAmountByServiceIdDTO;
import com.telemedicine.myclinic.activities.multiservices.GetAmountByServiceResponseModel;
import com.telemedicine.myclinic.activities.multiservices.MultiServicesResponseModel;
import com.telemedicine.myclinic.listeners.OnResultListener;
import com.telemedicine.myclinic.models.AfgaReportDTO;
import com.telemedicine.myclinic.models.ApptValidationForCheckinDTO;
import com.telemedicine.myclinic.models.ApptValidationResponseModel;
import com.telemedicine.myclinic.models.CardDTO;
import com.telemedicine.myclinic.models.CardListResponseModel;
import com.telemedicine.myclinic.models.CardsListModel;
import com.telemedicine.myclinic.models.DisplayPaymentDTO;
import com.telemedicine.myclinic.models.DoctorProfileDTO;
import com.telemedicine.myclinic.models.DoctorWeekScheduleTeleDTO;
import com.telemedicine.myclinic.models.FuzzySearchDTO;
import com.telemedicine.myclinic.models.FuzzySearchResponseModel;
import com.telemedicine.myclinic.models.HomeModel;
import com.telemedicine.myclinic.models.LabReportMainModel;
import com.telemedicine.myclinic.models.LabReportUrlDTO;
import com.telemedicine.myclinic.models.LabReportUrlResponseModel;
import com.telemedicine.myclinic.models.MarkReadNotificationDTO;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.MonthViewDTO;
import com.telemedicine.myclinic.models.NotificationDTO;
import com.telemedicine.myclinic.models.NotificationModel;
import com.telemedicine.myclinic.models.PaymentResponsibilty;
import com.telemedicine.myclinic.models.RenewSecurityToken;
import com.telemedicine.myclinic.models.ReportSharingDTO;
import com.telemedicine.myclinic.models.ReportsDTO;
import com.telemedicine.myclinic.models.SendRadiologyResponseModel;
import com.telemedicine.myclinic.models.TeleInstantEnqueueDTO;
import com.telemedicine.myclinic.models.TeleInstantEnqueueResult;
import com.telemedicine.myclinic.models.TeleInstantGetPriceDTO;
import com.telemedicine.myclinic.models.TeleInstantGetPriceObject;
import com.telemedicine.myclinic.models.UpdateInsuranceDTO;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailModel;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.appointments.ApptsModel;
import com.telemedicine.myclinic.models.boardingPass.BoardingPassDTO;
import com.telemedicine.myclinic.models.boardingPass.BoardingPassModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptCreateModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetSlotsModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingDTO;
import com.telemedicine.myclinic.models.bookAppointment.ApptGetTotalOutstandingModel;
import com.telemedicine.myclinic.models.bookAppointment.ApptPaymentDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceByIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetInsuranceModel;
import com.telemedicine.myclinic.models.bookAppointment.GetRefBySpecialtyIdDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefDoctorsBySpecialtyModel;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesDTO;
import com.telemedicine.myclinic.models.bookAppointment.GetRefSpecialtiesModel;
import com.telemedicine.myclinic.models.cancelAppointment.ApptCancelDTO;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPLabDTO;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPLabModel;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPSugarTestInsertDTO;
import com.telemedicine.myclinic.models.centerOfExcellency.CoEDMPSugarTestModel;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetSummaryDTO;
import com.telemedicine.myclinic.models.dashboardModels.ApptGetSummaryModel;
import com.telemedicine.myclinic.models.dashboardModels.ApptMinisModel;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsDTO;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsResponseModel;
import com.telemedicine.myclinic.models.earliestslots.UpdateUserStatusDTO;
import com.telemedicine.myclinic.models.earliestslotstemp.EarliestSlotsResponseModelTemp;
import com.telemedicine.myclinic.models.favouritedoctors.FavouriteDoctorsResponseModel;
import com.telemedicine.myclinic.models.favouritedoctors.GetFavouriteDTO;
import com.telemedicine.myclinic.models.favouritedoctors.UpdateFavouriteDTO;
import com.telemedicine.myclinic.models.forgotPassword.ForgetPasswordDTO;
import com.telemedicine.myclinic.models.homemodels.QCancelTicketDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateResponse;
import com.telemedicine.myclinic.models.homevisit.AddressBookDeleteDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookUpdateDTO;
import com.telemedicine.myclinic.models.homevisit.GetDriverTrackDTO;
import com.telemedicine.myclinic.models.homevisit.GetDriverTrackResponse;
import com.telemedicine.myclinic.models.homevisit.GetOrdersByPatientIdDTO;
import com.telemedicine.myclinic.models.homevisit.HomeCreateDTO;
import com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse;
import com.telemedicine.myclinic.models.homevisit.PatientAddressBookResponse;
import com.telemedicine.myclinic.models.homevisit.QGetTicketDTO;
import com.telemedicine.myclinic.models.homevisit.QTicketMiniModel;
import com.telemedicine.myclinic.models.humanTest.TranslateLabDTO;
import com.telemedicine.myclinic.models.humanTest.TranslateLabModel;
import com.telemedicine.myclinic.models.logonmodels.LogonDTO;
import com.telemedicine.myclinic.models.logonmodels.LogonModel;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigDTO;
import com.telemedicine.myclinic.models.onlineConfig.OnlineConfigModel;
import com.telemedicine.myclinic.models.payStage.PayStageOneDTO;
import com.telemedicine.myclinic.models.payStage.PayStageOneModel;
import com.telemedicine.myclinic.models.payStage.PayStageTwoModel;
import com.telemedicine.myclinic.models.payStage.TeleInstantPayStageOneDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateDTO;
import com.telemedicine.myclinic.models.profileInsuranceValidate.ProfileInsuranceValidateModel;
import com.telemedicine.myclinic.models.profileUpdate.GetProfileDTO;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateDTO;
import com.telemedicine.myclinic.models.profileUpdate.ProfileUpdateModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateDTO;
import com.telemedicine.myclinic.models.profilecreatoinmodels.ProfileCreateModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.RefPatientProfileModel;
import com.telemedicine.myclinic.models.profilecreatoinmodels.SecurityTokenDTO;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateDTO;
import com.telemedicine.myclinic.models.profilevalidatemodels.ProfileValidateModel;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationDTO;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationResponseModel;
import com.telemedicine.myclinic.models.registermodels.RegisterOneModel;
import com.telemedicine.myclinic.models.registermodels.RegisterOneDTO;
import com.telemedicine.myclinic.models.registermodels.RegisterStageTwoDTO;
import com.telemedicine.myclinic.models.registermodels.RegisterTwoModel;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleDTO;
import com.telemedicine.myclinic.models.reschedule.ApptRescheduleModel;
import com.telemedicine.myclinic.models.secondaryAppt.ApptCreateSecondaryDTO;
import com.telemedicine.myclinic.models.secondaryAppt.SecondaryTimeSlotApptDTO;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeDTO;
import com.telemedicine.myclinic.models.securityChallenge.SecurityChallengeModel;
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInModel;
import com.telemedicine.myclinic.models.teleCheckin.TeleCheckInOutDTO;
import com.telemedicine.myclinic.models.teleprice.TelePriceDto;
import com.telemedicine.myclinic.models.teleprice.TelePriceResponse;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeDTO;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeResponseModel;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekSchedule;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekScheduleDTO;
import com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp;
import com.telemedicine.myclinic.myapplication.BuildConfig;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RestClient {
    APIService apiService;
    static RestClient instance;

    private OkHttpClient getClient() {
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance()));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();

        return client;
    }

    public static synchronized RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public RestClient() {


        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .client(getClient())
                .build();

        apiService = retrofit.create(APIService.class);
    }


    public void loadPublicHome(final OnResultListener onResultListener) {
        Call<HomeModel> user = apiService.loadPublicHome();
        user.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.isSuccessful()) {
                    HomeModel responseUser = response.body();
                    onResultListener.onResult(responseUser, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void registerStageOne(RegisterOneDTO registerOneDTO, final OnResultListener onResultListener) {
        Call<RegisterOneModel> user = apiService.registerStage1(registerOneDTO);
        user.enqueue(new Callback<RegisterOneModel>() {
            @Override
            public void onResponse(Call<RegisterOneModel> call, Response<RegisterOneModel> response) {
                if (response.isSuccessful()) {
                    RegisterOneModel responseUser = response.body();
                    onResultListener.onResult(responseUser, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterOneModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void registerStageTwo(RegisterStageTwoDTO registerOneDTO, final OnResultListener onResultListener) {
        Call<RegisterTwoModel> user = apiService.registerStage2(registerOneDTO);
        user.enqueue(new Callback<RegisterTwoModel>() {
            @Override
            public void onResponse(Call<RegisterTwoModel> call, Response<RegisterTwoModel> response) {
                if (response.isSuccessful()) {
                    RegisterTwoModel responseUser = response.body();
                    onResultListener.onResult(responseUser, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterTwoModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void logOn(LogonDTO logonDTO, final OnResultListener onResultListener) {
        Call<LogonModel> user = apiService.logOn(logonDTO);
        user.enqueue(new Callback<LogonModel>() {
            @Override
            public void onResponse(Call<LogonModel> call, Response<LogonModel> response) {
                if (response.isSuccessful()) {
                    LogonModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogonModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void quickRegistration(QuickRegistrationDTO quickRegistrationDTO, final OnResultListener onResultListener) {
        Call<QuickRegistrationResponseModel> user = apiService.quickRegistration(quickRegistrationDTO);
        user.enqueue(new Callback<QuickRegistrationResponseModel>() {
            @Override
            public void onResponse(Call<QuickRegistrationResponseModel> call, Response<QuickRegistrationResponseModel> response) {
                if (response.isSuccessful()) {
                    QuickRegistrationResponseModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<QuickRegistrationResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void updateUserStatus(UpdateUserStatusDTO updateUserStatusDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.updateUserStatus(updateUserStatusDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void refPatientProfile(SecurityTokenDTO logonDTO, final OnResultListener onResultListener) {
        Call<RefPatientProfileModel> user = apiService.refPatientProfile(logonDTO);
        user.enqueue(new Callback<RefPatientProfileModel>() {
            @Override
            public void onResponse(Call<RefPatientProfileModel> call, Response<RefPatientProfileModel> response) {
                if (response.isSuccessful()) {
                    RefPatientProfileModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RefPatientProfileModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void profileValidate(ProfileValidateDTO profileValidateDTO, final OnResultListener onResultListener) {
        Call<ProfileValidateModel> user = apiService.profileValidate(profileValidateDTO);
        user.enqueue(new Callback<ProfileValidateModel>() {
            @Override
            public void onResponse(Call<ProfileValidateModel> call, Response<ProfileValidateModel> response) {
                if (response.isSuccessful()) {
                    ProfileValidateModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileValidateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void profileCreate(ProfileCreateDTO profileCreateDTO, final OnResultListener onResultListener) {
        Call<ProfileCreateModel> user = apiService.profileCreate(profileCreateDTO);
        user.enqueue(new Callback<ProfileCreateModel>() {
            @Override
            public void onResponse(Call<ProfileCreateModel> call, Response<ProfileCreateModel> response) {
                if (response.isSuccessful()) {
                    ProfileCreateModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileCreateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void profileInsuranceValidate(ProfileInsuranceValidateDTO profileInsuranceValidateDTO, final OnResultListener onResultListener) {
        Call<ProfileInsuranceValidateModel> user = apiService.profileInsuranceValidate(profileInsuranceValidateDTO);
        user.enqueue(new Callback<ProfileInsuranceValidateModel>() {
            @Override
            public void onResponse(Call<ProfileInsuranceValidateModel> call, Response<ProfileInsuranceValidateModel> response) {
                if (response.isSuccessful()) {
                    ProfileInsuranceValidateModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileInsuranceValidateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void GetTopUpFeesByDoctor(TopUpFeeDTO topUpFeeDTO, final OnResultListener onResultListener) {
        Call<TopUpFeeResponseModel> user = apiService.GetTopUpFeesByDoctor(topUpFeeDTO);
        user.enqueue(new Callback<TopUpFeeResponseModel>() {
            @Override
            public void onResponse(Call<TopUpFeeResponseModel> call, Response<TopUpFeeResponseModel> response) {
                if (response.isSuccessful()) {
                    TopUpFeeResponseModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TopUpFeeResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptGetSummary(ApptGetSummaryDTO apptGetSummaryDTO, final OnResultListener onResultListener) {
        Call<ApptGetSummaryModel> user = apiService.apptGetSummary(apptGetSummaryDTO);
        user.enqueue(new Callback<ApptGetSummaryModel>() {
            @Override
            public void onResponse(Call<ApptGetSummaryModel> call, Response<ApptGetSummaryModel> response) {
                if (response.isSuccessful()) {
                    ApptGetSummaryModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptGetSummaryModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptGetFollowUps(ApptGetSummaryDTO apptGetSummaryDTO, final OnResultListener onResultListener) {
        Call<ApptMinisModel> user = apiService.apptGetFollowUps(apptGetSummaryDTO);
        user.enqueue(new Callback<ApptMinisModel>() {
            @Override
            public void onResponse(Call<ApptMinisModel> call, Response<ApptMinisModel> response) {
                if (response.isSuccessful()) {
                    ApptMinisModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptMinisModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void profileGetById(GetProfileDTO getProfileDTO, final OnResultListener onResultListener) {
        Call<ProfileUpdateModel> user = apiService.profileGetById(getProfileDTO);
        user.enqueue(new Callback<ProfileUpdateModel>() {
            @Override
            public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {
                if (response.isSuccessful()) {
                    ProfileUpdateModel profileUpdateModel = response.body();
                    onResultListener.onResult(profileUpdateModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void profileUpdate(ProfileUpdateDTO profileUpdateDTO, final OnResultListener onResultListener) {
        Call<ProfileCreateModel> user = apiService.profileUpdate(profileUpdateDTO);
        user.enqueue(new Callback<ProfileCreateModel>() {
            @Override
            public void onResponse(Call<ProfileCreateModel> call, Response<ProfileCreateModel> response) {
                if (response.isSuccessful()) {
                    ProfileCreateModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileCreateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void profileInsuranceAdd(ProfileUpdateDTO profileUpdateDTO, final OnResultListener onResultListener) {
        Call<ProfileCreateModel> user = apiService.profileInsuranceAdd(profileUpdateDTO);
        user.enqueue(new Callback<ProfileCreateModel>() {
            @Override
            public void onResponse(Call<ProfileCreateModel> call, Response<ProfileCreateModel> response) {
                if (response.isSuccessful()) {
                    ProfileCreateModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileCreateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }



    public void getRefSpecialties(GetRefSpecialtiesDTO getRefSpecialtiesDTO, final OnResultListener onResultListener) {
        Call<GetRefSpecialtiesModel> user = apiService.getRefSpecialties(getRefSpecialtiesDTO);
        user.enqueue(new Callback<GetRefSpecialtiesModel>() {
            @Override
            public void onResponse(Call<GetRefSpecialtiesModel> call, Response<GetRefSpecialtiesModel> response) {
                if (response.isSuccessful()) {
                    GetRefSpecialtiesModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRefSpecialtiesModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getDoctorProfileById(DoctorProfileDTO doctorProfileDTO, final OnResultListener onResultListener) {
        Call<HomeModel> user = apiService.getDoctorProfileById(doctorProfileDTO);
        user.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.isSuccessful()) {
                    HomeModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getMonthView(MonthViewDTO monthViewDTO, final OnResultListener onResultListener) {
        Call<HomeModel> user = apiService.getMonthView(monthViewDTO);
        user.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.isSuccessful()) {
                    HomeModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getApptGetEarliestSlots(EarliestSlotsDTO earliestSlotsDTO, final OnResultListener onResultListener) {
        Call<EarliestSlotsResponseModel> user = apiService.getApptGetEarliestSlots(earliestSlotsDTO);
        user.enqueue(new Callback<EarliestSlotsResponseModel>() {
            @Override
            public void onResponse(Call<EarliestSlotsResponseModel> call, Response<EarliestSlotsResponseModel> response) {
                if (response.isSuccessful()) {
                    EarliestSlotsResponseModel earliestSlotsResponseModel = response.body();
                    onResultListener.onResult(earliestSlotsResponseModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EarliestSlotsResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getApptGetEarliestSlotsTemp(EarliestSlotsDTO earliestSlotsDTO, final OnResultListener onResultListener) {
        Call<EarliestSlotsResponseModelTemp> user = apiService.getApptGetEarliestSlotsTemp(earliestSlotsDTO);
        user.enqueue(new Callback<EarliestSlotsResponseModelTemp>() {
            @Override
            public void onResponse(Call<EarliestSlotsResponseModelTemp> call, Response<EarliestSlotsResponseModelTemp> response) {
                if (response.isSuccessful()) {
                    EarliestSlotsResponseModelTemp earliestSlotsResponseModel = response.body();
                    onResultListener.onResult(earliestSlotsResponseModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EarliestSlotsResponseModelTemp> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getRefDoctorsBySpecialty(GetRefBySpecialtyIdDTO getRefSpecialtiesDTO, final OnResultListener onResultListener) {
        Call<GetRefDoctorsBySpecialtyModel> user = apiService.getRefDoctorsBySpecialty(getRefSpecialtiesDTO);
        user.enqueue(new Callback<GetRefDoctorsBySpecialtyModel>() {
            @Override
            public void onResponse(Call<GetRefDoctorsBySpecialtyModel> call, Response<GetRefDoctorsBySpecialtyModel> response) {
                if (response.isSuccessful()) {
                    GetRefDoctorsBySpecialtyModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRefDoctorsBySpecialtyModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getRefDoctors(SecurityTokenDTO securityTokenDTO, final OnResultListener onResultListener) {
        Call<GetRefDoctorsBySpecialtyModel> user = apiService.GetRefDoctors(securityTokenDTO);
        user.enqueue(new Callback<GetRefDoctorsBySpecialtyModel>() {
            @Override
            public void onResponse(Call<GetRefDoctorsBySpecialtyModel> call, Response<GetRefDoctorsBySpecialtyModel> response) {
                if (response.isSuccessful()) {
                    GetRefDoctorsBySpecialtyModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRefDoctorsBySpecialtyModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getApptGetSlots(ApptGetSlotsDTO getRefSpecialtiesDTO, final OnResultListener onResultListener) {
        Call<ApptGetSlotsModel> user = apiService.apptGetSlots(getRefSpecialtiesDTO);
        user.enqueue(new Callback<ApptGetSlotsModel>() {
            @Override
            public void onResponse(Call<ApptGetSlotsModel> call, Response<ApptGetSlotsModel> response) {
                if (response.isSuccessful()) {
                    ApptGetSlotsModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptGetSlotsModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getApptGetSlotsMedicine(ApptGetSlotsDTO getRefSpecialtiesDTO, final OnResultListener onResultListener) {
        Call<ApptGetSlotsModel> user = apiService.apptGetSlotsMedicine(getRefSpecialtiesDTO);
        user.enqueue(new Callback<ApptGetSlotsModel>() {
            @Override
            public void onResponse(Call<ApptGetSlotsModel> call, Response<ApptGetSlotsModel> response) {
                if (response.isSuccessful()) {
                    ApptGetSlotsModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptGetSlotsModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptGetSlotsSecondary(SecondaryTimeSlotApptDTO secondaryTimeSlotApptDTO, final OnResultListener onResultListener) {
        Call<ApptGetSlotsModel> user = apiService.apptGetSlotsSecondary(secondaryTimeSlotApptDTO);
        user.enqueue(new Callback<ApptGetSlotsModel>() {
            @Override
            public void onResponse(Call<ApptGetSlotsModel> call, Response<ApptGetSlotsModel> response) {
                if (response.isSuccessful()) {
                    ApptGetSlotsModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptGetSlotsModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptCreate(ApptCreateDTO apptCreateDTO, final OnResultListener onResultListener) {
        Call<ApptCreateModel> user = apiService.apptCreate(apptCreateDTO);
        user.enqueue(new Callback<ApptCreateModel>() {
            @Override
            public void onResponse(Call<ApptCreateModel> call, Response<ApptCreateModel> response) {
                if (response.isSuccessful()) {
                    ApptCreateModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptCreateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptCreateSecondary(ApptCreateSecondaryDTO apptCreateSecondaryDTO, final OnResultListener onResultListener) {
        Call<ApptCreateModel> user = apiService.apptCreateSecondary(apptCreateSecondaryDTO);
        user.enqueue(new Callback<ApptCreateModel>() {
            @Override
            public void onResponse(Call<ApptCreateModel> call, Response<ApptCreateModel> response) {
                if (response.isSuccessful()) {
                    ApptCreateModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptCreateModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getInsuranceByProfileId(GetInsuranceByIdDTO getInsuranceByIdDTO, final OnResultListener onResultListener) {
        Call<GetInsuranceModel> user = apiService.getInsuranceByProfileId(getInsuranceByIdDTO);
        user.enqueue(new Callback<GetInsuranceModel>() {
            @Override
            public void onResponse(Call<GetInsuranceModel> call, Response<GetInsuranceModel> response) {
                if (response.isSuccessful()) {
                    GetInsuranceModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetInsuranceModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void ApptInsUpdate(UpdateInsuranceDTO getInsuranceByIdDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.ApptInsUpdate(getInsuranceByIdDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptGetTotalOutstanding(ApptGetTotalOutstandingDTO apptGetTotalOutstandingDTO, final OnResultListener onResultListener) {
        Call<ApptGetTotalOutstandingModel> user = apiService.apptGetTotalOutstanding(apptGetTotalOutstandingDTO);
        user.enqueue(new Callback<ApptGetTotalOutstandingModel>() {
            @Override
            public void onResponse(Call<ApptGetTotalOutstandingModel> call, Response<ApptGetTotalOutstandingModel> response) {
                if (response.isSuccessful()) {
                    ApptGetTotalOutstandingModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptGetTotalOutstandingModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptCancel(ApptCancelDTO apptCancelDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.apptCancel(apptCancelDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void onlineConfig(OnlineConfigDTO onlineConfigDTO, final OnResultListener onResultListener) {
        Call<OnlineConfigModel> user = apiService.getOnlineConfig(onlineConfigDTO);
        user.enqueue(new Callback<OnlineConfigModel>() {
            @Override
            public void onResponse(Call<OnlineConfigModel> call, Response<OnlineConfigModel> response) {
                if (response.isSuccessful()) {
                    OnlineConfigModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OnlineConfigModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void telePrice(TelePriceDto telePriceDto, final OnResultListener onResultListener) {
        Call<TelePriceResponse> user = apiService.getTelePrice(telePriceDto);
        user.enqueue(new Callback<TelePriceResponse>() {
            @Override
            public void onResponse(Call<TelePriceResponse> call, Response<TelePriceResponse> response) {
                if (response.isSuccessful()) {
                    TelePriceResponse getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TelePriceResponse> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void teleInstantPayStage1(TeleInstantPayStageOneDTO payStageOneDTO, final OnResultListener onResultListener) {
        Call<PayStageOneModel> user = apiService.teleInstantPayStage1(payStageOneDTO);
        user.enqueue(new Callback<PayStageOneModel>() {
            @Override
            public void onResponse(Call<PayStageOneModel> call, Response<PayStageOneModel> response) {
                if (response.isSuccessful()) {
                    PayStageOneModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayStageOneModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptGetAmountByServiceId(GetAmountByServiceIdDTO getAmountByServiceIdDTO, final OnResultListener onResultListener) {
        Call<MultiServicesResponseModel> user = apiService.apptGetCaseServicesByCaseIds(getAmountByServiceIdDTO);
        user.enqueue(new Callback<MultiServicesResponseModel>() {
            @Override
            public void onResponse(Call<MultiServicesResponseModel> call, Response<MultiServicesResponseModel> response) {
                if (response.isSuccessful()) {
                    MultiServicesResponseModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MultiServicesResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void payStage1(PayStageOneDTO payStageOneDTO, final OnResultListener onResultListener) {
        Call<PayStageOneModel> user = apiService.payStage1(payStageOneDTO);
        user.enqueue(new Callback<PayStageOneModel>() {
            @Override
            public void onResponse(Call<PayStageOneModel> call, Response<PayStageOneModel> response) {
                if (response.isSuccessful()) {
                    PayStageOneModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayStageOneModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptValidationForChecking(ApptValidationForCheckinDTO apptValidationForCheckinDTO, final OnResultListener onResultListener) {
        Call<ApptValidationResponseModel> user = apiService.apptValidationForChecking(apptValidationForCheckinDTO);
        user.enqueue(new Callback<ApptValidationResponseModel>() {
            @Override
            public void onResponse(Call<ApptValidationResponseModel> call, Response<ApptValidationResponseModel> response) {
                if (response.isSuccessful()) {
                    ApptValidationResponseModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptValidationResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptChecking(ApptValidationForCheckinDTO apptValidationForCheckinDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.apptChecking(apptValidationForCheckinDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void payStage2(String resourcePath,Long oRegId, String brandName,String cardNumber,Boolean savedCard,  final OnResultListener onResultListener) {
        Call<PayStageTwoModel> user = apiService.payStage2(resourcePath, oRegId, brandName, cardNumber, savedCard);
        user.enqueue(new Callback<PayStageTwoModel>() {
            @Override
            public void onResponse(Call<PayStageTwoModel> call, Response<PayStageTwoModel> response) {
                if (response.isSuccessful()) {
                    PayStageTwoModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayStageTwoModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void deletePaymentCardById(int cardId, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.deletePaymentCardById(cardId);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptGetFuzzySearch(FuzzySearchDTO fuzzySearchDTO, final OnResultListener onResultListener) {
        Call<FuzzySearchResponseModel> user = apiService.apptGetFuzzySearch(fuzzySearchDTO);
        user.enqueue(new Callback<FuzzySearchResponseModel>() {
            @Override
            public void onResponse(Call<FuzzySearchResponseModel> call, Response<FuzzySearchResponseModel> response) {
                if (response.isSuccessful()) {
                    FuzzySearchResponseModel fuzzySearchResponseModel = response.body();
                    onResultListener.onResult(fuzzySearchResponseModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FuzzySearchResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void updateFavDoctor(UpdateFavouriteDTO updateFavouriteDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.PtUpdateFavoriteDoctor(updateFavouriteDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult fuzzySearchResponseModel = response.body();
                    onResultListener.onResult(fuzzySearchResponseModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getFavouriteDoctors(GetFavouriteDTO getFavouriteDTO, final OnResultListener onResultListener) {
        Call<FavouriteDoctorsResponseModel> user = apiService.PtGetFavoriteDoctors(getFavouriteDTO);
        user.enqueue(new Callback<FavouriteDoctorsResponseModel>() {
            @Override
            public void onResponse(Call<FavouriteDoctorsResponseModel> call, Response<FavouriteDoctorsResponseModel> response) {
                if (response.isSuccessful()) {
                    FavouriteDoctorsResponseModel fuzzySearchResponseModel = response.body();
                    onResultListener.onResult(fuzzySearchResponseModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavouriteDoctorsResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptPayment(ApptPaymentDTO apptPaymentDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.apptPayment(apptPaymentDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void ApptPaymentByServices(ApptPaymentByServiceDTO apptPaymentDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.ApptPaymentByServices(apptPaymentDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void paymentCardById(CardDTO cardDTO, final OnResultListener onResultListener) {
        Call<CardsListModel> user = apiService.paymentCardsById(cardDTO);
        user.enqueue(new Callback<CardsListModel>() {
            @Override
            public void onResponse(Call<CardsListModel> call, Response<CardsListModel> response) {
                if (response.isSuccessful()) {
                    CardsListModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CardsListModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptGetSummaryBySType(AppointmentsDTO appointmentsDTO, final OnResultListener onResultListener) {
        Call<ApptsModel> user = apiService.apptGetSummaryBySType(appointmentsDTO);
        user.enqueue(new Callback<ApptsModel>() {
            @Override
            public void onResponse(Call<ApptsModel> call, Response<ApptsModel> response) {
                if (response.isSuccessful()) {
                    ApptsModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptsModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getNotificationList(NotificationDTO notificationDTO, final OnResultListener onResultListener) {
        Call<NotificationModel> user = apiService.getNotifications(notificationDTO);
        user.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful()) {
                    NotificationModel logonModel = response.body();
                    onResultListener.onResult(logonModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void markNotificationRead(MarkReadNotificationDTO markReadNotificationDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.markNotificationRead(markReadNotificationDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void apptGetBoardingPassUrl(BoardingPassDTO boardingPassDTO, final OnResultListener onResultListener) {
        Call<BoardingPassModel> user = apiService.apptGetBoardingPassUrl(boardingPassDTO);
        user.enqueue(new Callback<BoardingPassModel>() {
            @Override
            public void onResponse(Call<BoardingPassModel> call, Response<BoardingPassModel> response) {
                if (response.isSuccessful()) {
                    BoardingPassModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BoardingPassModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptReschedule(ApptRescheduleDTO apptRescheduleDTO, final OnResultListener onResultListener) {
        Call<ApptRescheduleModel> user = apiService.apptReschedule(apptRescheduleDTO);
        user.enqueue(new Callback<ApptRescheduleModel>() {
            @Override
            public void onResponse(Call<ApptRescheduleModel> call, Response<ApptRescheduleModel> response) {
                if (response.isSuccessful()) {
                    ApptRescheduleModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptRescheduleModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void apptGetDetails(ApptDetailDTO apptDetailDTO, final OnResultListener onResultListener) {
        Call<ApptDetailModel> user = apiService.apptGetDetails(apptDetailDTO);
        user.enqueue(new Callback<ApptDetailModel>() {
            @Override
            public void onResponse(Call<ApptDetailModel> call, Response<ApptDetailModel> response) {
                if (response.isSuccessful()) {
                    ApptDetailModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApptDetailModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }
    public void ApptGetServicesById(ApptGetServicesByIdDTO apptGetServicesByIdDTO, final OnResultListener onResultListener) {
        Call<MultiServicesResponseModel> user = apiService.ApptGetServicesById(apptGetServicesByIdDTO);
        user.enqueue(new Callback<MultiServicesResponseModel>() {
            @Override
            public void onResponse(Call<MultiServicesResponseModel> call, Response<MultiServicesResponseModel> response) {
                if (response.isSuccessful()) {
                    MultiServicesResponseModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MultiServicesResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }



    public void GetAllLabReportByPatientId(ReportsDTO reportsDTO, final OnResultListener onResultListener) {
        Call<LabReportMainModel> user = apiService.GetAllLabReportByPatientId(reportsDTO);
        user.enqueue(new Callback<LabReportMainModel>() {
            @Override
            public void onResponse(Call<LabReportMainModel> call, Response<LabReportMainModel> response) {
                if (response.isSuccessful()) {
                    LabReportMainModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LabReportMainModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void sendAgfaRadiologyImageUrlEmail(ReportSharingDTO reportsDTO, final OnResultListener onResultListener) {
        Call<SendRadiologyResponseModel> user = apiService.SendAgfaRadiologyImageUrlEmail(reportsDTO);
        user.enqueue(new Callback<SendRadiologyResponseModel>() {
            @Override
            public void onResponse(Call<SendRadiologyResponseModel> call, Response<SendRadiologyResponseModel> response) {
                if (response.isSuccessful()) {
                    SendRadiologyResponseModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendRadiologyResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void GetAllRadReportByPatientId(ReportsDTO reportsDTO, final OnResultListener onResultListener) {
        Call<LabReportMainModel> user = apiService.GetAllRadReportByPatientId(reportsDTO);
        user.enqueue(new Callback<LabReportMainModel>() {
            @Override
            public void onResponse(Call<LabReportMainModel> call, Response<LabReportMainModel> response) {
                if (response.isSuccessful()) {
                    LabReportMainModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LabReportMainModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void homeCreate(HomeCreateDTO homeCreateDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.homeCreate(homeCreateDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void qGetTicket(QGetTicketDTO qGetTicketDTO, final OnResultListener onResultListener) {
        Call<QTicketMiniModel> user = apiService.qGetTicket(qGetTicketDTO);
        user.enqueue(new Callback<QTicketMiniModel>() {
            @Override
            public void onResponse(Call<QTicketMiniModel> call, Response<QTicketMiniModel> response) {
                if (response.isSuccessful()) {
                    QTicketMiniModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<QTicketMiniModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void qCancelTicket(QCancelTicketDTO qCancelTicketDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.qCancelTicket(qCancelTicketDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void teleCheckInOut(TeleCheckInOutDTO teleCheckInOutDTO, final OnResultListener onResultListener) {
        Call<TeleCheckInModel> user = apiService.teleCheckInOut(teleCheckInOutDTO);
        user.enqueue(new Callback<TeleCheckInModel>() {
            @Override
            public void onResponse(Call<TeleCheckInModel> call, Response<TeleCheckInModel> response) {
                if (response.isSuccessful()) {
                    TeleCheckInModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeleCheckInModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void translateLab(TranslateLabDTO translateLabDTO, final OnResultListener onResultListener) {
        Call<TranslateLabModel> user = apiService.translateLab(translateLabDTO);
        user.enqueue(new Callback<TranslateLabModel>() {
            @Override
            public void onResponse(Call<TranslateLabModel> call, Response<TranslateLabModel> response) {
                if (response.isSuccessful()) {
                    TranslateLabModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslateLabModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getLabRadReportURL(LabReportUrlDTO labReportUrlDTO, final OnResultListener onResultListener) {
        Call<LabReportUrlResponseModel> user = apiService.GetLabRadReportURL(labReportUrlDTO);
        user.enqueue(new Callback<LabReportUrlResponseModel>() {
            @Override
            public void onResponse(Call<LabReportUrlResponseModel> call, Response<LabReportUrlResponseModel> response) {
                if (response.isSuccessful()) {
                    LabReportUrlResponseModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LabReportUrlResponseModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void translateRad(TranslateLabDTO translateLabDTO, final OnResultListener onResultListener) {
        Call<TranslateLabModel> user = apiService.translateRad(translateLabDTO);
        user.enqueue(new Callback<TranslateLabModel>() {
            @Override
            public void onResponse(Call<TranslateLabModel> call, Response<TranslateLabModel> response) {
                if (response.isSuccessful()) {
                    TranslateLabModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslateLabModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void GetAgfaRadiologyImageUrl(AfgaReportDTO translateLabDTO, final OnResultListener onResultListener) {
        Call<TranslateLabModel> user = apiService.GetAgfaRadiologyImageUrl(translateLabDTO);
        user.enqueue(new Callback<TranslateLabModel>() {
            @Override
            public void onResponse(Call<TranslateLabModel> call, Response<TranslateLabModel> response) {
                if (response.isSuccessful()) {
                    TranslateLabModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslateLabModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void GetAgfaRadiologyReportUrl(AfgaReportDTO translateLabDTO, final OnResultListener onResultListener) {
        Call<TranslateLabModel> user = apiService.GetAgfaRadiologyReportUrl(translateLabDTO);
        user.enqueue(new Callback<TranslateLabModel>() {
            @Override
            public void onResponse(Call<TranslateLabModel> call, Response<TranslateLabModel> response) {
                if (response.isSuccessful()) {
                    TranslateLabModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslateLabModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void coEDMPLab(CoEDMPLabDTO coEDMPLabDTO, final OnResultListener onResultListener) {
        Call<CoEDMPLabModel> user = apiService.coEDMPLab(coEDMPLabDTO);
        user.enqueue(new Callback<CoEDMPLabModel>() {
            @Override
            public void onResponse(Call<CoEDMPLabModel> call, Response<CoEDMPLabModel> response) {
                if (response.isSuccessful()) {
                    CoEDMPLabModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CoEDMPLabModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void coEDMPSugarTest(CoEDMPLabDTO coEDMPLabDTO, final OnResultListener onResultListener) {
        Call<CoEDMPSugarTestModel> user = apiService.coEDMPSugarTes(coEDMPLabDTO);
        user.enqueue(new Callback<CoEDMPSugarTestModel>() {
            @Override
            public void onResponse(Call<CoEDMPSugarTestModel> call, Response<CoEDMPSugarTestModel> response) {
                if (response.isSuccessful()) {
                    CoEDMPSugarTestModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CoEDMPSugarTestModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void coEDMPSugarTestInsert(CoEDMPSugarTestInsertDTO coEDMPLabDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.coEDMPSugarTestInsert(coEDMPLabDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void forgotPassword(ForgetPasswordDTO forgetPasswordDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.forgotPassword(forgetPasswordDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void logout(String resourcePath, final OnResultListener onResultListener) {
        Call<Void> user = apiService.logout(resourcePath);
        user.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onResultListener.onResult(null, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getSecurityChallenge(SecurityChallengeDTO securityChallengeDTO, final OnResultListener onResultListener) {
        Call<SecurityChallengeModel> user = apiService.getSecurityChallenge(securityChallengeDTO);
        user.enqueue(new Callback<SecurityChallengeModel>() {
            @Override
            public void onResponse(Call<SecurityChallengeModel> call, Response<SecurityChallengeModel> response) {
                if (response.isSuccessful()) {
                    SecurityChallengeModel getRefSpecialtiesModel = response.body();
                    onResultListener.onResult(getRefSpecialtiesModel, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SecurityChallengeModel> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getRewelToken(SecurityTokenDTO securityToken, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.getRenewelToken(securityToken);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void displayPaymentRes(DisplayPaymentDTO displayPaymentDTO, final OnResultListener onResultListener) {
        Call<PaymentResponsibilty> user = apiService.showPaymentResponsibilty(displayPaymentDTO);
        user.enqueue(new Callback<PaymentResponsibilty>() {
            @Override
            public void onResponse(Call<PaymentResponsibilty> call, Response<PaymentResponsibilty> response) {
                if (response.isSuccessful()) {
                    PaymentResponsibilty mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponsibilty> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getDoctorSchedule(DoctorWeekScheduleDTO securityToken, final OnResultListener onResultListener) {
        Call<DoctorWeekSchedule> user = apiService.getDoctorSchedule(securityToken);
        user.enqueue(new Callback<DoctorWeekSchedule>() {
            @Override
            public void onResponse(Call<DoctorWeekSchedule> call, Response<DoctorWeekSchedule> response) {
                if (response.isSuccessful()) {
                    DoctorWeekSchedule mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorWeekSchedule> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getDoctorScheduleTele(DoctorWeekScheduleTeleDTO securityToken, final OnResultListener onResultListener) {
        Call<DoctorWeekSchedule> user = apiService.getDoctorScheduleTele(securityToken);
        user.enqueue(new Callback<DoctorWeekSchedule>() {
            @Override
            public void onResponse(Call<DoctorWeekSchedule> call, Response<DoctorWeekSchedule> response) {
                if (response.isSuccessful()) {
                    DoctorWeekSchedule mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorWeekSchedule> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getDoctorScheduleTeleSecond(DoctorWeekScheduleTeleDTO securityToken, final OnResultListener onResultListener) {
        Call<DoctorWeekScheduleTemp> user = apiService.getDoctorScheduleTeleSecond(securityToken);
        user.enqueue(new Callback<DoctorWeekScheduleTemp>() {
            @Override
            public void onResponse(Call<DoctorWeekScheduleTemp> call, Response<DoctorWeekScheduleTemp> response) {
                if (response.isSuccessful()) {
                    DoctorWeekScheduleTemp mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorWeekScheduleTemp> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getTeleInstantEnqueueResult(TeleInstantEnqueueDTO tele, final OnResultListener onResultListener) {
        Call<TeleInstantEnqueueResult> user = apiService.getTeleInstantEnqueueResult(tele);
        user.enqueue(new Callback<TeleInstantEnqueueResult>() {
            @Override
            public void onResponse(Call<TeleInstantEnqueueResult> call, Response<TeleInstantEnqueueResult> response) {
                if (response.isSuccessful()) {
                    TeleInstantEnqueueResult mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeleInstantEnqueueResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getTeleInstantGetPrice(TeleInstantGetPriceDTO tele, final OnResultListener onResultListener) {
        Call<TeleInstantGetPriceObject> user = apiService.getTeleInstantGetPrice(tele);
        user.enqueue(new Callback<TeleInstantGetPriceObject>() {
            @Override
            public void onResponse(Call<TeleInstantGetPriceObject> call, Response<TeleInstantGetPriceObject> response) {
                if (response.isSuccessful()) {
                    Object mobileOpResult = response.body();
                    onResultListener.onResult(mobileOpResult, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeleInstantGetPriceObject> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


    public void getOrdersbyPatientId(GetOrdersByPatientIdDTO getOrdersByPatientIdDTO, final OnResultListener onResultListener) {
        Call<OrderListByPatientIdResponse> user = apiService.getOrdersbyPatientId(getOrdersByPatientIdDTO);
        user.enqueue(new Callback<OrderListByPatientIdResponse>() {
            @Override
            public void onResponse(Call<OrderListByPatientIdResponse> call, Response<OrderListByPatientIdResponse> response) {
                if (response.isSuccessful()) {
                    OrderListByPatientIdResponse orderListByPatientIdResponse = response.body();
                    onResultListener.onResult(orderListByPatientIdResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderListByPatientIdResponse> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void getPatientAddressBook(GetOrdersByPatientIdDTO getOrdersByPatientIdDTO, final OnResultListener onResultListener) {
        Call<PatientAddressBookResponse> user = apiService.getPatientAddressBook(getOrdersByPatientIdDTO);
        user.enqueue(new Callback<PatientAddressBookResponse>() {
            @Override
            public void onResponse(Call<PatientAddressBookResponse> call, Response<PatientAddressBookResponse> response) {
                if (response.isSuccessful()) {
                    PatientAddressBookResponse orderListByPatientIdResponse = response.body();
                    onResultListener.onResult(orderListByPatientIdResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientAddressBookResponse> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    public void AddressBookDelete(AddressBookDeleteDTO addressBookDeleteDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> user = apiService.AddressBookDelete(addressBookDeleteDTO);
        user.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult orderListByPatientIdResponse = response.body();
                    onResultListener.onResult(orderListByPatientIdResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    //AddressBookCreate
    public void addressBookCreate(AddressBookCreateDTO addressBookCreateDTO, final OnResultListener onResultListener) {
        Call<AddressBookCreateResponse> user = apiService.addressBookCreate(addressBookCreateDTO);
        user.enqueue(new Callback<AddressBookCreateResponse>() {
            @Override
            public void onResponse(Call<AddressBookCreateResponse> call, Response<AddressBookCreateResponse> response) {
                if (response.isSuccessful()) {
                    AddressBookCreateResponse createResponse = response.body();
                    onResultListener.onResult(createResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressBookCreateResponse> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    //AddressBookUpdate
    public void addressBookUpdate(AddressBookUpdateDTO addressBookUpdateDTO, final OnResultListener onResultListener) {
        Call<MobileOpResult> update = apiService.addressBookUpdate(addressBookUpdateDTO);
        update.enqueue(new Callback<MobileOpResult>() {
            @Override
            public void onResponse(Call<MobileOpResult> call, Response<MobileOpResult> response) {
                if (response.isSuccessful()) {
                    MobileOpResult updateResponse = response.body();
                    onResultListener.onResult(updateResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MobileOpResult> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }

    // DriverTrackGet
    public void driverTrackGet(GetDriverTrackDTO getDriverTrackDTO, final OnResultListener onResultListener) {
        Call<GetDriverTrackResponse> trackGet = apiService.DriverTrackGet(getDriverTrackDTO);
        trackGet.enqueue(new Callback<GetDriverTrackResponse>() {
            @Override
            public void onResponse(Call<GetDriverTrackResponse> call, Response<GetDriverTrackResponse> response) {
                if (response.isSuccessful()) {
                    GetDriverTrackResponse trackResponse = response.body();
                    onResultListener.onResult(trackResponse, true, null);
                } else if (response.errorBody() != null) {
                    try {
                        onResultListener.onResult(null, false, response.errorBody().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDriverTrackResponse> call, Throwable t) {
                Log.e("REST", t.getMessage());
                onResultListener.onResult(null, false, t.getMessage());
            }
        });
    }


}
