package com.mateuslima.myfirstlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mateuslima.pindialog.PinDialog;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int yourPimCode = 1234;
        new PinDialog.Builder(this)
                .setYourPimCode(yourPimCode)
                .setPinCodeLength(String.valueOf(yourPimCode).length())
                .setBottomText("Forgot?", new PinDialog.PasswordDialog() {
                    @Override
                    public void onClickForgotPin() {
                        // do something
                    }
                })
                .build();


    }


}
