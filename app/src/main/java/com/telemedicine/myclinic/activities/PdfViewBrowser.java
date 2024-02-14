package com.telemedicine.myclinic.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.telemedicine.myclinic.myapplication.BuildConfig;
import com.telemedicine.myclinic.myapplication.R;
import com.telemedicine.myclinic.pdfViewer.PDFHelper;
import com.telemedicine.myclinic.util.AppointmentEvent;
import com.telemedicine.myclinic.util.Const;
import com.telemedicine.myclinic.util.ErrorMessage;
import com.telemedicine.myclinic.util.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

public class PdfViewBrowser extends BaseActivity {

    @BindView(R.id.pdfView)
    PDFView pdfView;
    String fileName1 = "";

    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent != null) {
            String url = intent.getStringExtra("url");
            name = intent.getStringExtra("name");
            String date = intent.getStringExtra("date_report");
            String patientName = tinyDB.getString(Const.PATIENT_NAME);
            //name = patientName + "_" + name;
            name = name + "_" + date;
            download(url);
        }

        if (TextUtil.getArabic(this))
            toolbar_left_iv.setRotation(180);
    }

    @Override
    protected int layout() {
        return R.layout.activity_pdf_view_browser;
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
            if (TextUtil.getArabic(this)) {
                ErrorMessage.getInstance().showSuccessGreen(PdfViewBrowser.this,
                        getString(R.string.check_in_available_text) + " " + appointmentEvent.getDoctorNameAr() + ". " + getString(R.string.please_press_to_continue));
            } else {
                ErrorMessage.getInstance().showSuccessGreen(PdfViewBrowser.this,
                        getString(R.string.check_in_available_text) + " " + appointmentEvent.getDoctorNameEn() + ". " + getString(R.string.please_press_to_continue));
            }
        } else {
            ErrorMessage.getInstance().showErrorYellow(PdfViewBrowser.this, appointmentEvent.getErrorMSg());
        }
    }

    @OnClick(R.id.toolbar_left_iv)
    void back() {
        onBackPressed();
    }

    private Bitmap takeScreenShot() {
        //this line commented
        /*View view = activity.getWindow().getDecorView();*/
        //2
        pdfView.setDrawingCacheEnabled(true);
        pdfView.buildDrawingCache();
        Bitmap b1 = pdfView.getDrawingCache();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        Bitmap result = Bitmap.createBitmap(b1, 0, 0, pdfView.getWidth(), pdfView.getHeight());

        pdfView.destroyDrawingCache();
        return result;
        // return null;
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @OnClick(R.id.share)
    void logo() {

        shareImageUri();
        // permission();
    }


    //this code commended from here
/*    void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE);
            boolean readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE);
            if (!writeGranted || !readGranted) {
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            } else {
                Uri uri = getImageUri(takeScreenShot());
                if (uri != null)
                    shareImageUri(uri);
            }
        } else {
            Uri uri = getImageUri(takeScreenShot());
            if (uri != null)
                shareImageUri(uri);
        }

    }*/


/*     PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {

                Uri uri = getImageUri(takeScreenShot());
                if (uri != null)
                    shareImageUri(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(PdfViewBrowser.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };*/

    //code commented till here

    private void shareImageUri() {
        File file1 = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic" + File.separator + name.trim() + ".pdf");
     /*   Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("file://" + file1.getAbsolutePath());
        //this line commented
        // sharingIntent.setType("application/pdf");
        sharingIntent.setType("application/pdf");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share Report Using"));*/

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(PdfViewBrowser.this, "com.telemedicine.myclinic.myapplication.labProvider", file1);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    public void showPDF() {
        //Getting the saved PDF
        // File file = new File(this.getExternalFilesDir("pdfs") + File.separator + fileName1);
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic" + File.separator + fileName1);
        //Loading the PDF
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

        File file1 = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic" + File.separator + name.trim() + ".pdf");

        rename(file, file1);
    }

    public void showError() {
        Toast.makeText(this, "Error downloading ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //these three lines commendted
        //Deleting the PDF that was saved
       /* new File(this.getExternalFilesDir("pdfs")
                + File.separator + "Report.pdf").delete();*/
    }

    void download(String url) {

        showWaitDialog();
        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

        String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
        fileName1 = fileNameWithoutExtn + ".pdf";
        ButterKnife.bind(this);

        String baseUrl = "";
//https://bamc.myclinic.com.sa/OS.Test.WebAPI.External/Reports/70234.pdf
        //https://bamc.myclinic.com.sa/OS.WebAPI.External.OSlots/Reports/34351.pdf
        if (url.contains("bamc")) {
            baseUrl = (url.contains("Images")) ? "https://bamc.myclinic.com.sa/OS.WebAPI.External/Images/" : "https://bamc.myclinic.com.sa/OS.WebAPI.External/Reports/";
        } else {
            baseUrl = (url.contains("Images")) ? "https://dtexweb.myclinic.com.sa/OS.WebAPI.External/Images/" : "https://dtexweb.myclinic.com.sa/OS.WebAPI.External/Reports/";
        }
        //https://dtexweb.myclinic.com.sa/OS.WebAPI.ExternalLabRad/Images/
        //String baseUrl = (url.contains("Images"))? "https://bamc.myclinic.com.sa/OS.WebAPI.External/Images/" : "https://bamc.myclinic.com.sa/OS.WebAPI.External/Reports/";
        //New instance of PDFHelper
        new PDFHelper(this, baseUrl, fileName1, new Callable<Void>() {
            @Override
            public Void call() {
                hideWaitDialog();
                //Callable function if download_1 is successful
                showPDF();
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() {
                hideWaitDialog();
                //Callable function if download_1 isn't successful
                showError();
                return null;
            }
        });
    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }

            String sdcardPath = "";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sdcardPath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
            } else {
                sdcardPath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
            }

            File file = new File(sdcardPath + "/" + "MyClinic/" + name.trim() + ".pdf");
            File file1 = new File(sdcardPath + "/" + "MyClinic/" + name.trim() + ".pdf");
            rename(file, file1);
        }
    }

    public void Save(View view) {
        permissions();
    }

    void permissions() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean writeGranted = PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE);
            boolean readGranted = PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE);
            if (!writeGranted || !readGranted) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    writeData();
                } else {
                    TedPermission.with(this)
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                            ).check();
                }
            } else {
                writeData();
            }
        } else
            writeData();


    }

    void writeData() {
        //  File file1 = new File(this.getExternalFilesDir("pdfs") + File.separator + name.trim() + ".pdf");

        File file1 = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic" + File.separator + name.trim() + ".pdf");

        try {
            String path = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                path = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic";
                //path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + "MyClinic";
            } else {
                path = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + "MyClinic";
            }

            // Create the parent path
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sdcardPath = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            sdcardPath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
            //  sdcardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        } else {
            sdcardPath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        }

        File file = new File(sdcardPath + "/" + "MyClinic");

        copyFileOrDirectory(file1.getPath(), file.getPath());

        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

        alertDialog.setTitleText(getString(R.string.my_clininc)).setConfirmText(getString(R.string.ok)).setContentText(
                        getString(R.string.download_successfully)
                )
                .show();

        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {
                writeData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(PdfViewBrowser.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
