//package com.telemedicine.myclinic.vseeActivities;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Handler;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import android.util.Log;
//import android.view.MenuItem;
//
//import com.telemedicine.myclinic.App;
//import com.telemedicine.myclinic.util.Const;
//import com.vsee.kit.VSeeActivityResultCallback;
//import com.vsee.kit.VSeeMenuItemLifecycleCallback;
//import com.vsee.kit.VSeeVideoManager;
//
//import java.util.ArrayList;
//
//import cn.pedant.SweetAlert.SweetAlertDialog;
//
///**
// * There are many ways to customize the video activity so that it fits well with your
// * user experience requirement:
// * <p>
// * 1) UI customization
// * - Change top action bar color by setting colorPrimary, colorPrimaryDark and
// * colorAccent in your theme. See "VSeeKitExampleVideo" theme in "styles.xml" for
// * more info how to set up.
// * - Change background with a drawable object (image or color).
// * - Change action bar overlay mode so it may be persisted on top of the activity
// * or hidden so it has more space for video.
// * - You can set your own waiting view, enable or disable back button on top action bar.
// * - Enable or disable the dialing view
// * - Control if VSeeKit should ask user to confirm when they click on end call button.
// * <p>
// * 2) Add or remove menu items. You can fully customize the menu item with icons, action
// * view, change visibility or handle when it is selected. You can also launch another
// * activity for result and access that result when the activity returned.
// * <p>
// * 3) Listen to various events when local video is shown, when receive new call from
// * other users, etc.
// */
//public class VideoCustomizer {
//    private static final int CONTACT_PICKER_REQ = 1;
//
//    public static void customize() {
//        customizeUI();
//        addMenuItems();
//        setupVideoReceiver();
//    }
//
//    // This method shows how to customize UI. You can change the variables to see how it would
//    // affect the user experience.
//    private static void customizeUI() {
//        VSeeVideoManager videoManager = VSeeVideoManager.instance();
//
//        videoManager.setTitle("Doctor in Session");
//
//        // Enable confirm end call, so when user clicks on hang up button, an alert will
//        // be shown to user to confirm if he or she wants to end the call. By default,
//        // the call is end immediately without the alert.
//        videoManager.setConfirmEndCall(true);
//
//        // Show waiting view on top of user local video. By default, this waiting view
//        // is not set, and user local video is aligned to top of the screen.
//        //   videoManager.setWaitingForCallView(getWaitingMessageView());
//
//        // Change background with a drawable object. It can be plain color or an image.
//        // By default, the background is black
//        videoManager.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80D0F1")));
//
//        // Enable back button. By default, the back button is disabled (but user still can
//        // navigate back using Back button on the phone).
//        videoManager.setDisplayHomeAsUpEnabled(true);
//
//        // By default, dialing view is visible, and when user clicks on it, a popup
//        // is shown with all users being dialed and user can cancelled the dial. You can
//        // hide it by setting dialing view visible to false
//        videoManager.setDialingViewVisible(false);
//    }
//
//    private static void addMenuItems() {
//        VSeeVideoManager videoManager = VSeeVideoManager.instance();
//
//        // Extra menu items can be dynamically added/removed to the call activity.
//        // You should always set menu item life cycle callback before adding/removing
//        // menu items.
//        videoManager.setMenuItemLifecycleCallback(new VSeeMenuItemLifecycleCallback() {
//
//            // onCreate is only called once when menu item is first created. You should
//            // use it to customize the menu item with icon, action view, or visibility.
//            @Override
//            public void onCreate(MenuItem menuItem) {
//                handleMenuItemCreated(menuItem);
//            }
//
//            // If menu item is just created, onPrepare will be called after onCreate.
//            // If menu item is already created, onPrepare will be called whenever the
//            // activity calls "onPrepareOptionsMenu" method, or when you call
//            // "VSeeVideoManager.instance().refreshMenuItems()". You should use it to
//            // further customize the menu item or its visibility.
//            @Override
//            public void onPrepare(MenuItem menuItem) {
//                handleMenuItemPrepared(menuItem);
//            }
//
//            // onSelect is called whenever user selects the menu item
//            @Override
//            public boolean onSelect(MenuItem menuItem) {
//                handleMenuItemSelected(menuItem);
//                return false;
//            }
//        });
//
//        // You can add menu item with an id and a string. You can further customize it
//        // with icons, action view, etc, using above menu item lifecycle callback
//        // videoManager.addMenuItem(R.id.menu_item_video_add_contact, "Add User");
//
//        // If you want to start an activity from call activity and receive the result
//        // later, you can use activity result callback to be notified when result is
//        // returned
//        videoManager.setOnActivityResultCallback(new VSeeActivityResultCallback() {
//            @Override
//            public void onActivityResult(int requestCode, int resultCode, Intent data) {
//                if (requestCode == CONTACT_PICKER_REQ && resultCode == Activity.RESULT_OK) {
//                    handleResultsFromContactPicker(data);
//                }
//            }
//        });
//    }
//
//    private static void handleMenuItemCreated(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//           /* case R.id.menu_item_video_add_contact:
//                menuItem.setIcon(R.drawable.ic_add_white);
//                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
//            //   break;
//        }
//    }
//
//    private static void handleMenuItemPrepared(MenuItem menuItem) {
//        // You can further change menu item visibility here
//    }
//
//    private static void handleMenuItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            /*case R.id.menu_item_video_add_contact:
//                addContactsToCall();
//                break;*/
//        }
//    }
//
//    // Show another activity for result
//   /* private static void addContactsToCall() {
//        Intent intent = new Intent(ExampleApplication.instance(), ContactPickerActivity.class);
//        VSeeVideoManager.instance().startActivityForResult(intent, CONTACT_PICKER_REQ);
//    }*/
//
//    // Access the result when ContactPickerActivity returns. "SELECTED_CONTACT" is a custom key
//    // in the Intent data, which is an array list of string and contains user names selected
//    // by user. See ContactPickerActivity for more information.
//    private static void handleResultsFromContactPicker(Intent intent) {
//        ArrayList<String> userNames = intent.getStringArrayListExtra("SELECTED_CONTACTS");
//        VSeeVideoManager.instance().startVideoCall(userNames);
//    }
//
//    //
//    // Get feedback about adding and removing windows, app share.  For this example, we'll just
//    // log it when we get each. By default, when you receive a call from other contact, the
//    // method "onShowRemoteVideoView" will be called, and you can decide how to start the
//    // video activity. In this example, we show an alert to tell user that there's an incoming
//    // call, and if they click "OK", the video activity will be launched. If you don't need
//    // to ask user, you can just simply launch the video activity.
//    //
//    // Also, whenever a call ends (as defined by the last remote video window closing), close
//    // the call activity if it is currently open.
//    //
//    private static void setupVideoReceiver() {
//
//        VSeeVideoManager.instance().addReceiver(new VSeeVideoManager.SimpleVSeeVideoManagerReceiver() {
//            @Override
//            public void onShowLocalVideoView() {
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        // send broadcast to cancel the expiry notification.
//                        Intent intentWaiting = new Intent("waiting_room");
//                        // You can also include some extra data.
//                        intentWaiting.putExtra("cancel_alarm", true);
//                        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intentWaiting);
//
//                        Const.CALL_ACTIVE = true;
//                        Intent intent = VSeeVideoManager.instance().getVideoLaunchIntent();
//                        App.getInstance().startActivity(intent);
//                        //Do something after 100ms
//                    }
//                }, 3000);
//
//                Log.d("VSeeExample", "showing local video view " + videoCounts());
//            }
//
//            @Override
//            public void onShowRemoteVideoView(String username, boolean isFirst) {
//                Log.d("VSeeExample", String.format("show %s video view for %s %s", isFirst ? "first" : "subsequent", username, videoCounts()));
//
//                // Intent intent = new Intent()
//
//                if (isFirst) {
//                    final Activity currentActivity = App.getCurrentActivity();
//                    if (currentActivity == null) return;
//
//
//                    new SweetAlertDialog(currentActivity, SweetAlertDialog.PROGRESS_TYPE)
//                            .setTitleText("Switch to video call now?")
//                            .setContentText("Incoming Video Call From My Clinic")
//                            .setCancelText("Enter Clinic Conference").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog.dismiss();
//                            Intent intent = VSeeVideoManager.instance().getVideoLaunchIntent();
//                            currentActivity.startActivity(intent);
//                        }
//                    }).show();
//
//
//                  /* AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
//                    builder.setTitle("Incoming Video Call From TeleMedcine");
//                    builder.setMessage("Switch to video call now?");
//                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = VSeeVideoManager.instance().getVideoLaunchIntent();
//                            currentActivity.startActivity(intent);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", null);
//                    builder.create().show();*/
//
//                }
//            }
//
//            @Override
//            public void onAppShareChange(boolean isActive) {
//                Log.d("VSeeExample", String.format("app share change (open or close).  App share %s active now.  %s", isActive ? "is" : "is not", videoCounts()));
//            }
//
//            @Override
//            public void onRemoveRemoteVideoView(String username, boolean isLast) {
//                Const.CALL_ACTIVE = false;
//                Log.d("VSeeExample", String.format("close %s video view for %s %s", isLast ? "last" : "a", username, videoCounts()));
//                if (isLast) {
//                    // video call is over... end the call activity if it is open
//                    VSeeVideoManager.instance().finishVideoActivity();
//                }
//            }
//        });
//
//    }
//
//
//    @SuppressLint("DefaultLocale")
//    private static String videoCounts() {
//        return String.format("[num windows: %d, num minimized: %d, num active users: %d]",
//                VSeeVideoManager.instance().getNumVideoViews(),
//                VSeeVideoManager.instance().getNumMinimizedVideoViews(),
//                VSeeVideoManager.instance().getNumActiveUsers());
//    }
//
//    // Setup a custom waiting view to be displayed on top of the video activity.
//  /*  @SuppressLint("InflateParams")
//    private static View getWaitingMessageView() {
//        return LayoutInflater.from(ExampleApplication.instance()).inflate(R.layout.waitingmessage, null);
//    }*/
//}
