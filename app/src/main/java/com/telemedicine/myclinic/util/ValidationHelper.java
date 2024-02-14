package com.telemedicine.myclinic.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import static org.webrtc.ContextUtils.getApplicationContext;


public class ValidationHelper {

    private static String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
    private static String emailPattern = "\\b[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\b";
//    private static String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\n";

    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpeg|jpg|png|gif|bmp|webp))$)";

    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.trim().length() < 1) {
            return true;
        }
        return false;
    }


    public static boolean isNullOrEmpty(CharSequence s) {
        if (s == null || s.length() < 1 || s.toString().trim().length() < 1) {
            return true;
        }
        return false;
    }


    public static boolean isNullOrEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        if (isNullOrEmpty(password))
            return false;
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher m = pattern.matcher(password);
        return m.matches();
    }

    public static boolean isEmailValid(String email) {
        if (isNullOrEmpty(email))
            return false;
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        Pattern pattern = Pattern.compile(emailPattern);
//        Matcher m = pattern.matcher(email);
//        return m.matches();
    }

    /**
     * <b>Image file that match:</b><br>
     * 1. “a.jpg”, “a.gif”,”a.png”, “a.bmp”,<br>
     * 2. “..jpg”, “..gif”,”..png”, “..bmp”,<br>
     * 3. “a.JPG”, “a.GIF”,”a.PNG”, “a.BMP”,<br>
     * 4. “a.JpG”, “a.GiF”,”a.PnG”, “a.BmP”,<br>
     * 5. “jpg.jpg”, “gif.gif”,”png.png”, “bmp.bmp”<br><br>
     * <b>Image that doesn’t match:</b><br>
     * <p>
     * 1. “.jpg”, “.gif”,”.png”,”.bmp” – image file name is required<br>
     * 2. ” .jpg”, ” .gif”,” .png”,” .bmp” – White space is not allow in first character<br>
     * 3. “a.txt”, “a.exe”,”a.”,”a.mp3″ – Only image file extension is allow<br>
     * 3. “jpg”, “gif”,”png”,”bmp” – image file extension is required<br>
     *
     * @param fileName Image file name with extension
     * @return
     */
    public static boolean isImageFile(String fileName) {
        if (isNullOrEmpty(fileName))
            return false;
        Pattern pattern = Pattern.compile(IMAGE_PATTERN);
        Matcher m = pattern.matcher(fileName);
        return m.matches();
    }

    public static String customerValue(String s) {
        if (s == null || s.trim().length() < 1) {
            return "";
        }
        return s;
    }

    static Ringtone r = null;

    public static void defaultRing(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRingtone() {
        if (r != null)
            r.stop();

    }

}
