package com.telemedicine.myclinic.util;


import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class LocaleDateHelper {

    public static final SimpleDateFormat LOCAL_TIME_FORMAT = new SimpleDateFormat("h aa", Locale.getDefault());
    public static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("h aa", Locale.getDefault());
    public static final SimpleDateFormat MY_BOOKING_TIME_FORMAT = new SimpleDateFormat("h aa", Locale.getDefault());
    public static final SimpleDateFormat DEFAULT_TIME_FORMAT_0 = new SimpleDateFormat("KK:mm aa", Locale.getDefault());
    public static final SimpleDateFormat DEFAULT_TIME_FORMAT_SEARCH = new SimpleDateFormat("KK:mm aa", Locale.getDefault());
    public static final SimpleDateFormat V_CHAT_DATE_FORMATE = new SimpleDateFormat("MM/dd/yyyy KK:mm aa", Locale.getDefault());
    private static final String TIME_TEMPLATE = "%s - %s (%s)";
    private static final String TIME_INTERVAL_TEMPLATE = "%s - %s";
    public static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    public static final String LOCAL_DASH_BOARD_DATE_PATTERN = "dd MMM yyyy";
    public static final String LOCAL_FORMS_DATE_PATTERN = "dd MMM yyyy";
    public static final String NEWS_MONTH_YEAR = "MMM yyyy";
    public static final String NEWS_DAY = "dd";

    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String DATE_PATTERN_2 = "dd-MMM-yyyy";
    public static final String LOCAL_TIME_PATTERN = "HH:mm:ss";

    public static final String V_Message_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";// "yyyy-MM-dd HH:mm:ss";//2016-06-13 12:41:41

    public static final String DATE_PATTERN_LICENSE_EXPIRY = "dd/MM/yyyy";

    public static final SimpleDateFormat LOGS_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy h:mm aa", Locale.getDefault());


    static {
        DEFAULT_TIME_FORMAT.setTimeZone(TimeZone.getDefault());
        DEFAULT_TIME_FORMAT_0.setTimeZone(TimeZone.getDefault());
    }

    public static SimpleDateFormat getDateFormate() {
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
    }

    public static SimpleDateFormat getLicenseDateFormate() {
        return new SimpleDateFormat(DATE_PATTERN_LICENSE_EXPIRY, Locale.getDefault());
    }

    public static SimpleDateFormat getMonthYearFormate() {
        return new SimpleDateFormat(NEWS_MONTH_YEAR, Locale.getDefault());
    }

    public static SimpleDateFormat getDayFormate() {
        return new SimpleDateFormat(NEWS_DAY, Locale.getDefault());
    }

    public static SimpleDateFormat getVmessageFormatter() {
        return new SimpleDateFormat(V_Message_TIME, Locale.getDefault());
    }

    public static SimpleDateFormat getLocalDateFormatter() {
        return new SimpleDateFormat(LOCAL_DATE_PATTERN, Locale.getDefault());
    }

    public static SimpleDateFormat getDashBoardDateFormatter() {
        return new SimpleDateFormat(LOCAL_DASH_BOARD_DATE_PATTERN, Locale.getDefault());
    }

    public static SimpleDateFormat getFormsDateFormatter() {
        return new SimpleDateFormat(LOCAL_FORMS_DATE_PATTERN, Locale.getDefault());
    }


    public static SimpleDateFormat getLocalTimeFormatter() {
        return new SimpleDateFormat(LOCAL_TIME_PATTERN, Locale.getDefault());
    }

    public static Date dateToUTC(Date date) {
//        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(date));
        return gmt;
    }

    public static Date dateTimeToDate(Date date) {
        Date date1 = new Date(date.getTime());
        date1.setHours(0);
        date1.setMinutes(0);
        date1.setSeconds(0);
        return date1;
    }

    public static String getDateForInquiryDetail(Date date) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy h:mm aa");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+03:00")); // missing line
       /* Date date = c.getTime();
        timeFormat.format(date);*/

        return timeFormat.format(date);
    }

    public static Date getDateLcal(Date date) {
        Date date1 = null;
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy h:mm aa", Locale.getDefault());
            timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+03:00")); // missing line
            String dte = timeFormat.format(date);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                date1 = format.parse(dte);
                System.out.println(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

       /* Date date = c.getTime();
        timeFormat.format(date);*/
            // date1 = timeFormat.parse(dte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date1;
    }


    public static String getDateFormat(Date date) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy h:mm aa");
       /* Date date = c.getTime();
        timeFormat.format(date);*/

        return timeFormat.format(date);
    }

    public static String buildOnlineBookingTime(long bookingTime) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(bookingTime);
        final String start = DEFAULT_TIME_FORMAT_0.format(calendar.getTime());
        return String.format(Locale.US, start);
    }

    public static String GET_LOGS_DATE_TIME(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(date.getTime());
        final String start = LOGS_DATE_TIME.format(calendar.getTime());
        return String.format(Locale.getDefault(), start).toUpperCase();
    }

    public static String formatToYesterdayOrToday(Date dateTime) throws ParseException {
        //        Date dateTime = new SimpleDateFormat("EEE hh:mma MMM d, yyyy").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        timeFormatter.setTimeZone(TimeZone.getDefault());
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return timeFormatter.format(dateTime) + " Today ";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return timeFormatter.format(dateTime) + " Yesterday ";
        } else {
            timeFormatter = new SimpleDateFormat("hh:mm a MMM d, yyyy");
            timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            return timeFormatter.format(dateTime);
        }
    }


    public static String getUTCDate(String dateISO) {
        Date d1 = null;
        String displayValue = null;
        Date date = null;
        try {

            TimeZone tz = TimeZone.getTimeZone("UTC");
            Calendar cal = Calendar.getInstance(tz);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            sdf.setCalendar(cal);
            cal.setTime(sdf.parse(dateISO));
            date = cal.getTime();

            SimpleDateFormat timeFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            displayValue = timeFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayValue;
    }


    public static String convertDateStringFormat(String strDate, String fromFormat, String toFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat);
            return dateFormat2.format(sdf.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDateStringFormatWeekDay(String strDate, String fromFormat, String toFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim());
            return dateFormat2.format(sdf.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDateStringFormat(String strDate, String fromFormat, String toFormat, Locale locale) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);


            return dateFormat2.format(sdf.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDateStringFormatWeek(String strDate, String fromFormat, String toFormat, Locale locale) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);
            Date dt = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.DATE, 7);
            return dateFormat2.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Calendar getCalDate(String date, String fromFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            Date dt = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEnableCall(String apptDate, String fromFormat, Locale locale) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, locale);
            Date dt = sdf.parse(apptDate);

            Calendar cal = Calendar.getInstance();
            long currentTime = cal.getTimeInMillis();

            if(cal.getTime().after(dt)){
                return true;
            }else{
                cal.setTime(dt);
                long timeDifference = cal.getTimeInMillis() - currentTime;

                if(timeDifference <= 300000){
                    return  true;
                }else{
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertDateStringFormatWeekAddDay(String strDate, String fromFormat, String toFormat, Locale locale, int increment) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);
            Date dt = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.DATE, increment);
            return dateFormat2.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDateStringFormatWeekAddDayEarliest(String strDate, String format, Locale locale, int increment) {
        try {
            SimpleDateFormat sdf;
            if(locale.getLanguage().equals(new Locale("fr").getLanguage())){
                 sdf = new SimpleDateFormat(format, locale);
            }else{
                 sdf = new SimpleDateFormat(format);
            }

            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            // SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);
            Date dt = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            // cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, increment);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDayOfWeek(String strDate, String format) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            // SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);
            Date dt = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int dayOfWeek =  cal.get(Calendar.DAY_OF_WEEK);

            String day  = "";
            switch (dayOfWeek){
                case 1:{
                    day =  "Sunday";
                    break;
                }
                case 2: {
                    day =  "Monday";
                    break;
                }
                case 3: {
                    day =  "Tuesday";
                    break;
                }
                case 4: {
                    day =  "Wednesday";
                    break;
                }
                case 5: {
                    day =  "Thursday";
                    break;
                }
                case 6: {
                    day =  "Friday";
                    break;
                }
                case 7: {
                    day =  "Saturday";
                }
            }
            //cal.add(Calendar.DATE, increment);
            return day;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String convertDateStringFormatHome(String strDate, String fromFormat, String toFormat, Locale locale) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, locale);
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), locale);
            return dateFormat2.format(sdf.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertDateStringFormatWithZone(String strDate, String fromFormat, String toFormat) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+03"));
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(toFormat.trim(), Locale.getDefault());
            dateFormat2.setTimeZone(TimeZone.getDefault());
            return dateFormat2.format(sdf.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long convertDateFormat(String strDate, String fromFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            return sdf.parse(strDate).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static long convertDateFormatZone(String strDate, String fromFormat) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+03"));
            return sdf.parse(strDate).getTime();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static Date getDateFormat(String strDate, String fromFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
            return sdf.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertArabicToEnglish(String strDate, String fromFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
            Date date = sdf.parse(strDate);
           return sdf.format(date.getTime());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getCountOfDays(String createdDateString, String expireDateString) {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        createdDateString = df.format(c.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount + "");
    }


    public static boolean expiredDate(String date) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);

            if (new Date().after(strDate)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean expiredAptTime(String date) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
            if (strDate.getTime()+420000 < new Date().getTime()) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean isSelfCheckingTimeStart(String date) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
            if (strDate.getTime()-3600000 < new Date().getTime() &&
                    strDate.getTime()+420000 > new Date().getTime()) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean isAppointmentPassedTwoDays(String date) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
            if (strDate.getTime()+ 172800000 < new Date().getTime()) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static long getApptTimeInMilli(String date) {

        long timeInMilli = 0;

        if (!ValidationHelper.isEmailValid(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date mDate = sdf.parse(date);
                timeInMilli = mDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return timeInMilli;
    }

    public static long getApptTimeInMilliOther(String date) {

        long timeInMilli = 0;

        if (!ValidationHelper.isEmailValid(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a");
            try {
                Date mDate = sdf.parse(date);
                timeInMilli = mDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return timeInMilli;
    }
}
