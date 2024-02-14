package com.telemedicine.myclinic.webservices;

import com.telemedicine.myclinic.activities.multiservices.ApptGetServicesByIdDTO;
import com.telemedicine.myclinic.activities.multiservices.ApptPaymentByServiceDTO;
import com.telemedicine.myclinic.activities.multiservices.GetAmountByServiceIdDTO;
import com.telemedicine.myclinic.activities.multiservices.GetAmountByServiceResponseModel;
import com.telemedicine.myclinic.activities.multiservices.MultiServicesResponseModel;
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
import com.telemedicine.myclinic.models.LabReportMainModel;
import com.telemedicine.myclinic.models.LabReportUrlDTO;
import com.telemedicine.myclinic.models.LabReportUrlResponseModel;
import com.telemedicine.myclinic.models.MarkReadNotificationDTO;
import com.telemedicine.myclinic.models.MonthViewDTO;
import com.telemedicine.myclinic.models.NotificationDTO;
import com.telemedicine.myclinic.models.NotificationModel;
import com.telemedicine.myclinic.models.PaymentResponsibilty;
import com.telemedicine.myclinic.models.ReportSharingDTO;
import com.telemedicine.myclinic.models.ReportsDTO;
import com.telemedicine.myclinic.models.SendRadiologyResponseModel;
import com.telemedicine.myclinic.models.TeleInstantEnqueueDTO;
import com.telemedicine.myclinic.models.TeleInstantEnqueueResult;
import com.telemedicine.myclinic.models.TeleInstantGetPriceDTO;
import com.telemedicine.myclinic.models.TeleInstantGetPriceObject;
import com.telemedicine.myclinic.models.UpdateInsuranceDTO;
import com.telemedicine.myclinic.models.appointments.ApptsMiniModel;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsDTO;
import com.telemedicine.myclinic.models.earliestslots.EarliestSlotsResponseModel;
import com.telemedicine.myclinic.models.earliestslots.UpdateUserStatusDTO;
import com.telemedicine.myclinic.models.earliestslotstemp.EarliestSlotsResponseModelTemp;
import com.telemedicine.myclinic.models.favouritedoctors.FavouriteDoctorsResponseModel;
import com.telemedicine.myclinic.models.favouritedoctors.GetFavouriteDTO;
import com.telemedicine.myclinic.models.favouritedoctors.UpdateFavouriteDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookCreateResponse;
import com.telemedicine.myclinic.models.homevisit.AddressBookDeleteDTO;
import com.telemedicine.myclinic.models.homevisit.AddressBookUpdateDTO;
import com.telemedicine.myclinic.models.homevisit.GetDriverTrackDTO;
import com.telemedicine.myclinic.models.homevisit.GetDriverTrackResponse;
import com.telemedicine.myclinic.models.homevisit.GetOrdersByPatientIdDTO;
import com.telemedicine.myclinic.models.homevisit.OrderListByPatientIdResponse;
import com.telemedicine.myclinic.models.homevisit.PatientAddressBookResponse;
import com.telemedicine.myclinic.models.payStage.TeleInstantPayStageOneDTO;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationDTO;
import com.telemedicine.myclinic.models.quickregistration.QuickRegistrationResponseModel;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeDTO;
import com.telemedicine.myclinic.models.topupfee.TopUpFeeResponseModel;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekSchedule;
import com.telemedicine.myclinic.models.weekschedule.DoctorWeekScheduleDTO;
import com.telemedicine.myclinic.models.HomeModel;
import com.telemedicine.myclinic.models.MobileOpResult;
import com.telemedicine.myclinic.models.appointments.AppointmentsDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailDTO;
import com.telemedicine.myclinic.models.appointments.ApptDetailModel;
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
import com.telemedicine.myclinic.models.forgotPassword.ForgetPasswordDTO;
import com.telemedicine.myclinic.models.homemodels.QCancelTicketDTO;
import com.telemedicine.myclinic.models.homevisit.HomeCreateDTO;
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
import com.telemedicine.myclinic.models.weekscheduletemp.DoctorWeekScheduleTemp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("LoadPublicHome")
    Call<HomeModel> loadPublicHome();

    @POST("RegisterStage1")
    Call<RegisterOneModel> registerStage1(@Body RegisterOneDTO registerOneDTO);

    @POST("RegisterStage2")
    Call<RegisterTwoModel> registerStage2(@Body RegisterStageTwoDTO registerOneDTO);

    @POST("RegisterQuick")
    Call<QuickRegistrationResponseModel> quickRegistration(@Body QuickRegistrationDTO quickRegistrationDTO);

    @POST("Logon")
    Call<LogonModel> logOn(@Body LogonDTO logonDTO);

    @POST("GetRefPatientProfile")
    Call<RefPatientProfileModel> refPatientProfile(@Body SecurityTokenDTO securityTokenDTO);

    @POST("UpdateUserStatus")
    Call<MobileOpResult> updateUserStatus (@Body UpdateUserStatusDTO updateUserStatusDTO);

    @POST("GetPushNotificationCampaignInProgressAndCompleted")
    Call<NotificationModel> getNotifications(@Body NotificationDTO notificationDTO);

    @POST("AddPushNotificationCampaignReadDetails")
    Call<MobileOpResult> markNotificationRead(@Body MarkReadNotificationDTO markReadNotificationDTO);

    @POST("ProfileValidate")
    Call<ProfileValidateModel> profileValidate(@Body ProfileValidateDTO profileValidateDTO);

    @POST("ProfileCreate")
    Call<ProfileCreateModel> profileCreate(@Body ProfileCreateDTO profileCreateDTO);

    @POST("ProfileInsuranceValidate")
    Call<ProfileInsuranceValidateModel> profileInsuranceValidate(@Body ProfileInsuranceValidateDTO profileCreateDTO);

    @POST("GetTopUpFeesByDoctor")
    Call<TopUpFeeResponseModel> GetTopUpFeesByDoctor(@Body TopUpFeeDTO topUpFeeDTO);


    @POST("ApptGetSummary")
    Call<ApptGetSummaryModel> apptGetSummary(@Body ApptGetSummaryDTO apptGetSummaryDTO);

    @POST("ApptGetFollowUps")
    Call<ApptMinisModel> apptGetFollowUps(@Body ApptGetSummaryDTO apptGetSummaryDTO);

    @POST("ProfileGetById")
    Call<ProfileUpdateModel> profileGetById(@Body GetProfileDTO getProfileDTO);

    @POST("ProfileUpdate")
    Call<ProfileCreateModel> profileUpdate(@Body ProfileUpdateDTO profileUpdateDTO);

    @POST("ProfileInsuranceAdd")
    Call<ProfileCreateModel> profileInsuranceAdd(@Body ProfileUpdateDTO profileUpdateDTO);

    @POST("GetRefSpecialties")
    Call<GetRefSpecialtiesModel> getRefSpecialties(@Body GetRefSpecialtiesDTO getRefSpecialtiesDTO);

    @POST("GetDoctorProfileById")
    Call<HomeModel> getDoctorProfileById(@Body DoctorProfileDTO doctorProfileDTO);

    @POST("GetMonthView")
    Call<HomeModel> getMonthView(@Body MonthViewDTO monthViewDTO);

    @POST("ApptGetEarlistSlots")
    Call<EarliestSlotsResponseModel> getApptGetEarliestSlots(@Body EarliestSlotsDTO EarliestSlotsDTO);

    @POST("ApptGetEarlistSlots")
    Call<EarliestSlotsResponseModelTemp> getApptGetEarliestSlotsTemp(@Body EarliestSlotsDTO EarliestSlotsDTO);

    @POST("GetRefDoctorsBySpecialty")
    Call<GetRefDoctorsBySpecialtyModel> getRefDoctorsBySpecialty(@Body GetRefBySpecialtyIdDTO getRefSpecialtiesDTO);

    @POST("GetRefDoctors")
    Call<GetRefDoctorsBySpecialtyModel> GetRefDoctors(@Body SecurityTokenDTO securityTokenDTO);

    @POST("ApptGetSlots")
    Call<ApptGetSlotsModel> apptGetSlots(@Body ApptGetSlotsDTO apptGetSlotsDTO);

    @POST("apptgetslotstelemedicine")
    Call<ApptGetSlotsModel> apptGetSlotsMedicine(@Body ApptGetSlotsDTO apptGetSlotsDTO);

    @POST("ApptGetSlotsSecondary")
    Call<ApptGetSlotsModel> apptGetSlotsSecondary(@Body SecondaryTimeSlotApptDTO apptGetSlotsDTO);

    @POST("ApptCreate")
    Call<ApptCreateModel> apptCreate(@Body ApptCreateDTO apptCreateDTO);

    @POST("ApptGetFuzzySearch")
    Call<FuzzySearchResponseModel> apptGetFuzzySearch(@Body FuzzySearchDTO apptCreateDTO);

    @POST("PtUpdateFavoriteDoctor")
    Call<MobileOpResult> PtUpdateFavoriteDoctor(@Body UpdateFavouriteDTO updateFavouriteDTO);

    @POST("PtGetFavoriteDoctors")
    Call<FavouriteDoctorsResponseModel> PtGetFavoriteDoctors(@Body GetFavouriteDTO getFavouriteDTO);

    @POST("ApptCreateSecondary")
    Call<ApptCreateModel> apptCreateSecondary(@Body ApptCreateSecondaryDTO apptCreateSecondaryDTO);

    @POST("ProfileActiveInsuranceGetById")
    Call<GetInsuranceModel> getInsuranceByProfileId(@Body GetInsuranceByIdDTO getInsuranceByIdDTO);

    @POST("ApptInsUpdate")
    Call<MobileOpResult> ApptInsUpdate(@Body UpdateInsuranceDTO getInsuranceByIdDTO);

    @POST("ApptGetTotalOutstanding")
    Call<ApptGetTotalOutstandingModel> apptGetTotalOutstanding(@Body ApptGetTotalOutstandingDTO apptGetTotalOutstandingDTO);

    @POST("ApptCancel")
    Call<MobileOpResult> apptCancel(@Body ApptCancelDTO apptCancelDTO);

    @POST("GetOnlineConfig")
    Call<OnlineConfigModel> getOnlineConfig(@Body OnlineConfigDTO onlineConfigDTO);

    @POST("TeleGetPrice")
    Call<TelePriceResponse> getTelePrice(@Body TelePriceDto telePriceDto);

    @POST("TeleInstantPayStage1")
    Call<PayStageOneModel> teleInstantPayStage1(@Body TeleInstantPayStageOneDTO payStageOneDTO);

    @POST("ApptGetAmountByServiceId")
    Call<GetAmountByServiceResponseModel> apptGetAmountByServiceId(@Body GetAmountByServiceIdDTO getAmountByServiceIdDTO);

    @POST("ApptGetCaseServicesByCaseIds")
    Call<MultiServicesResponseModel> apptGetCaseServicesByCaseIds(@Body GetAmountByServiceIdDTO getAmountByServiceIdDTO);

    @POST("PayStage1")
    Call<PayStageOneModel> payStage1(@Body PayStageOneDTO payStageOneDTO);


    @POST("ApptValidatationForCheckIn")
    Call<ApptValidationResponseModel> apptValidationForChecking(@Body ApptValidationForCheckinDTO apptValidationForCheckinDTO);


    @POST("ApptCheckIn")
    Call<MobileOpResult> apptChecking(@Body ApptValidationForCheckinDTO apptValidationForCheckinDTO);

    @GET("PayStage2")
    Call<PayStageTwoModel> payStage2(@Query("resourcePath") String param1, @Query("oRegId") Long oregId,
                                     @Query("cardBrand") String cardBrand,
                                     @Query("cardNumber") String cardNumber,
                                     @Query("SaveCard") Boolean SaveCard);

    @POST("DeletePaymentCardById")
    Call<MobileOpResult> deletePaymentCardById(@Query("PaymentCardId") int cardId);

    @POST("PaymentCardsByUserId")
    Call<CardsListModel> paymentCardsById(@Body CardDTO cardDTO);

    @POST("ApptPayment")
    Call<MobileOpResult> apptPayment(@Body ApptPaymentDTO payStageOneDTO);

    @POST("ApptPaymentByServices")
    Call<MobileOpResult> ApptPaymentByServices(@Body ApptPaymentByServiceDTO payStageOneDTO);


    @POST("ApptGetSummaryBySType")
    Call<ApptsModel> apptGetSummaryBySType(@Body AppointmentsDTO appointmentsDTO);

    @POST("ApptGetBoardingPassUrl")
    Call<BoardingPassModel> apptGetBoardingPassUrl(@Body BoardingPassDTO boardingPassDTO);

    @POST("ApptReschedule")
    Call<ApptRescheduleModel> apptReschedule(@Body ApptRescheduleDTO apptRescheduleDTO);

    @POST("ApptGetDetails")
    Call<ApptDetailModel> apptGetDetails(@Body ApptDetailDTO apptDetailDTO);

    @POST("ApptGetServicesById")
    Call<MultiServicesResponseModel> ApptGetServicesById(@Body ApptGetServicesByIdDTO apptGetServicesByIdDTO);

    @POST("GetAllLabReportByPatientId")
    Call<LabReportMainModel> GetAllLabReportByPatientId(@Body ReportsDTO reportsDTO);

    @POST("SendAgfaRadiologyImageUrlEmail")
    Call<SendRadiologyResponseModel> SendAgfaRadiologyImageUrlEmail(@Body ReportSharingDTO reportsDTO);

    @POST("GetAllRadReportByPatientId")
    Call<LabReportMainModel> GetAllRadReportByPatientId(@Body ReportsDTO reportsDTO);

    @POST("HomeCreate")
    Call<MobileOpResult> homeCreate(@Body HomeCreateDTO homeCreateDTO);

    @POST("QGetTicket")
    Call<QTicketMiniModel> qGetTicket(@Body QGetTicketDTO qGetTicketDTO);

    @POST("QCancelTicket")
    Call<MobileOpResult> qCancelTicket(@Body QCancelTicketDTO qCancelTicketDTO);

    @POST("TeleCheckInOutAutoAccept")
    Call<TeleCheckInModel> teleCheckInOut(@Body TeleCheckInOutDTO teleCheckInOutDTO);

    @POST("TranslateLabUrl")
    Call<TranslateLabModel> translateLab(@Body TranslateLabDTO translateLabDTO);

    @POST("GetLabRadReportURL")
    Call<LabReportUrlResponseModel> GetLabRadReportURL(@Body LabReportUrlDTO labReportUrlDTO);

    @POST("TranslateRadUrl")
    Call<TranslateLabModel> translateRad(@Body TranslateLabDTO translateLabDTO);

    @POST("GetAgfaRadiologyImageUrl")
    Call<TranslateLabModel> GetAgfaRadiologyImageUrl(@Body AfgaReportDTO translateLabDTO);

    @POST("GetAgfaRadiologyReportUrl")
    Call<TranslateLabModel> GetAgfaRadiologyReportUrl(@Body AfgaReportDTO translateLabDTO);

    @POST("CoEDMPLabResultsGet")
    Call<CoEDMPLabModel> coEDMPLab(@Body CoEDMPLabDTO translateLabDTO);

    @POST("CoEDMPSugarTestResultsGet")
    Call<CoEDMPSugarTestModel> coEDMPSugarTes(@Body CoEDMPLabDTO translateLabDTO);

    @POST("CoEDMPSugarTestInsert")
    Call<MobileOpResult> coEDMPSugarTestInsert(@Body CoEDMPSugarTestInsertDTO translateLabDTO);

    @POST("ForgotPassword")
    Call<MobileOpResult> forgotPassword(@Body ForgetPasswordDTO forgetPasswordDTO);

    @POST("Logout")
    Call<Void> logout(@Query("SecurityToken") String param1);

    @POST("GetSecurityChallenge")
    Call<SecurityChallengeModel> getSecurityChallenge(@Body SecurityChallengeDTO securityChallengeDTO);

    @POST("RenewToken")
    Call<MobileOpResult> getRenewelToken(@Body SecurityTokenDTO param1);

    @POST("PayDisplayPaymentResponsibility")
    Call<PaymentResponsibilty> showPaymentResponsibilty(@Body DisplayPaymentDTO param1);

    @POST("DoctorWeekSchedule")
    Call<DoctorWeekSchedule> getDoctorSchedule(@Body DoctorWeekScheduleDTO param1);

    @POST("ApptGetSlotsWeekForTelemedicine")
    Call<DoctorWeekSchedule> getDoctorScheduleTele(@Body DoctorWeekScheduleTeleDTO param1);

    @POST("ApptGetSlotsWeekForTelemedicine")
    Call<DoctorWeekScheduleTemp> getDoctorScheduleTeleSecond(@Body DoctorWeekScheduleTeleDTO param1);

    @POST("TeleInstantEnqueue")
    Call<TeleInstantEnqueueResult> getTeleInstantEnqueueResult(@Body TeleInstantEnqueueDTO tele);
//AmountDue
    @POST("TeleInstantGetPrice")
    Call<TeleInstantGetPriceObject> getTeleInstantGetPrice(@Body TeleInstantGetPriceDTO tele);

    @POST("GetOrdersbyPatientId")
    Call<OrderListByPatientIdResponse> getOrdersbyPatientId(@Body GetOrdersByPatientIdDTO getOrdersByPatientIdDTO);

    @POST("GetPatientAddressBook")
    Call<PatientAddressBookResponse> getPatientAddressBook(@Body GetOrdersByPatientIdDTO getOrdersByPatientIdDTO);

    @POST("AddressBookCreate")
    Call<AddressBookCreateResponse> addressBookCreate(@Body AddressBookCreateDTO addressBookCreateDTO);

    @POST("AddressBookUpdate")
    Call<MobileOpResult> addressBookUpdate(@Body AddressBookUpdateDTO addressBookCreateDTO);

    /*@POST("AddressBookDelete")*/
    @POST("AddressBookDelete")
    Call<MobileOpResult> AddressBookDelete(@Body AddressBookDeleteDTO addressBookDeleteDTO);

    @POST("DriverTrackGet")
    Call<GetDriverTrackResponse> DriverTrackGet(@Body GetDriverTrackDTO getDriverTrackDTO);


}
