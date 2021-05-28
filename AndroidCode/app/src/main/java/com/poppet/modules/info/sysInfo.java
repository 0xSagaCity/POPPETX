package com.poppet.modules.info;

import android.os.Build;
import android.util.Log;

import static com.poppet.config.NetworkConfig.END_HEADER;

public class sysInfo {

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
}
