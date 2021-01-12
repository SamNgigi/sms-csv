package com.example.hai_tracker_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {


    /*
    * REMEMBER TO ADD THE LINE BELOW IN THE ANDROID MANIFEST FILE.
    * android:requestLegacyExternalStorage="true"
    *
    * ALSO ALLOW READ_SMS PERMISSIONS ON THE APP IS INSTALLED.
    * (Long press app icon > Click app info > go to permissions > allow sms and storage permissions)
    *
    * THIS ABOVE WILL BE PROVIDED FOR IN A BETTER FASHION LATER ON
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Calling our Sms Reader class*/


        String[] permissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, permissions, 1);

        final TextView portView = (TextView) this.findViewById(R.id.log);

        portView.setMovementMethod(new ScrollingMovementMethod());
        Button btnBackup = (Button) this.findViewById(R.id.btnBackup);

        btnBackup.setOnClickListener(new View.OnClickListener() {

            SmsReader smsReader = new SmsReader();

            @Override
            public void onClick(View v) {
                smsReader.backupSms(MainActivity.this, getBaseContext());
                portView.setText("Success!");
            }
        });
    }
}