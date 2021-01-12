package com.example.hai_tracker_mobile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Objects;

public class SmsReader {
    /*Array to store the sms*/
    public ArrayList<String> smsBuffer = new ArrayList<String>();
    /*Our file*/
    String smsFile = "sms_backup" + ".csv";

    /*
    Method to read our sms inbox
    Takes in the activity it will be called on and the context.
    */
    public void backupSms(Activity activity, Context context){
        /* Clear the array*/
        smsBuffer.clear();

        /*Content resolver that will be used to query our inbox*/
        ContentResolver cr = context.getContentResolver();

        /* Uri pointing to our Inbox*/
        Uri smsInboxUri = Telephony.Sms.Inbox.CONTENT_URI;

        /*String array that contains the fields we desire*/
        String[] content = new String[] {
                Telephony.Sms.Inbox._ID,
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.BODY,
        };

        /*Cursor object to read our inbox. We pass our uri and content we want back*/
        Cursor cursor = cr.query(smsInboxUri, content, null, null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);

        /*Make sure count is greater than 0*/
        assert cursor != null;
        if (cursor.getCount() > 0){
            String count = Integer.toString(cursor.getCount());
            Log.d("Count", count);
            Log.v("Count", count);

            /*While loop to iterate through our inbox*/
            while(cursor.moveToNext()){
                String msgId = cursor.getString(cursor.getColumnIndex(content[0]));
                String date = cursor.getString(cursor.getColumnIndex(content[1]));
                String address = cursor.getString(cursor.getColumnIndex(content[2]));
                String body = cursor.getString(cursor.getColumnIndex(content[3]));

                /*Append comma separated values to our ArrayList*/
                smsBuffer.add(msgId + "" + date + ","+ address + ","+ body);
            }
            /*Pass list to our csv generating function together with the activity calling this method*/
            generateCSVFileForSms(activity, smsBuffer);

        }
    }

    /*
    Function to create csv file and save to the Documents folder of our internal storage
    Takes in an activity and ArrayList as params
    */
    private void generateCSVFileForSms(Activity activity, ArrayList<String> lst){
        try{
            /*Path to the Documents folder in our internal storage*/
            String storage_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + File.separator + smsFile;
            /*Instantiating a writer object*/
            FileWriter write = new FileWriter(storage_path);

            /*We append the header column and add new line*/
            write.append("msgId,date,address,body");
            write.append("\n");

            /*Appending contents of our list*/
            for (String s: lst ){
                write.append(s);
                write.append("\n");
            }

            /*Closing our writer object*/
            write.flush();
            write.close();

            /*Indicate the file has been successfully saved*/
            Toast.makeText(activity.getBaseContext(),
                    "File Saved Successfully",
                    Toast.LENGTH_LONG).show();

        } catch (NullPointerException e){
            Log.d("NullPointer Exception: ", Objects.requireNonNull(e.getMessage()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
