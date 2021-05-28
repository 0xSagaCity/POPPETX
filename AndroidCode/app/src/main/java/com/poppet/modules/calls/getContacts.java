package com.poppet.modules.calls;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class getContacts {

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
}
