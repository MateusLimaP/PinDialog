package com.mateuslima.pindialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;


public class PinDialog {


    private Context context;
    private SharedPreferences myPreferences;
    public static final int GRAVITY_CENTER = 100;
    public static final int GRAVITY_BOTTOM = 200;
    public static final int GRAVITY_TOP = 300;
    // password
    private int pinCodeLength;
    private EditText editPassword;
    private GifImageView gifImage;
    private int headerBackgroundColor;
    private LinearLayout linearHeaderPassword, linearDialogAnimated;
    private TextView textHeader, textForgotPassword;
    private PasswordDialog passwordDialog;
    private int titleColor;
    private String title, error, bottomText;
    private int yourPimCode, backgroundColor, gif, bottomTexColor, gravity, delayTime;
    private boolean hideForgetPassword, hideAnimation;


    private PinDialog(Builder builder){ // PASSWORD
        context = builder.context;
        pinCodeLength = builder.pinCodeLength;
        passwordDialog = builder.passwordDialog;
        headerBackgroundColor = builder.headerBackgroundColor;
        titleColor = builder.titleColor;
        title = builder.title;
        hideForgetPassword = builder.hideForgotPassword;
        yourPimCode = builder.yourPimCode;
        hideAnimation = builder.hideAnimation;
        gif = builder.gif;
        bottomText = builder.bottomText;
        bottomTexColor = builder.bottomTexColor;
        gravity = builder.gravity;
        backgroundColor = builder.backgroundColor;
        error = builder.error;
        delayTime = builder.delayTime;


        myPreferences = context.getSharedPreferences("_",Context.MODE_PRIVATE);
        configPinDialog();



    }

    private void initUi(View view){
        editPassword = view.findViewById(R.id.editPassword);
        linearHeaderPassword = view.findViewById(R.id.linearHeaderPassword);
        textHeader = view.findViewById(R.id.textHeader);
        textForgotPassword = view.findViewById(R.id.textForgotPassword);
        linearDialogAnimated = view.findViewById(R.id.linearDialogAnimated);
        gifImage = view.findViewById(R.id.gifCadeado);
    }



    private void configPinDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        initUi(view);
        dialog.setCancelable(false);
        dialog.setView(view);




        if (shouldShowDialog()){ // in case of the orientation change the dialog will appear immediately
            if (contOpenedDialog() == 1)
                 dialog.show();
            setContOpenedDialog(2);
        }

        if (hideForgetPassword)
            textForgotPassword.setVisibility(View.GONE);
        else
            textForgotPassword.setVisibility(View.VISIBLE);

        // max line password
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(pinCodeLength);
        editPassword.setFilters(filters);

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.length() == pinCodeLength){
                  if (Integer.parseInt(s.toString()) == yourPimCode){ // pin is correct
                      setShowDialog(false);
                      setContOpenedDialog(0);

                      dialog.dismiss();
                  }else{
                      if (error == null)
                      editPassword.setError(context.getResources().getString(R.string.Wrong_pin));
                      else editPassword.setError(error);
                  }

               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // change background color header
        if (headerBackgroundColor != 0)
            linearHeaderPassword.setBackgroundColor(headerBackgroundColor);

        // change text color header
        if (titleColor != 0)
            textHeader.setTextColor(titleColor);

        // change text header
        if (title != null)
            textHeader.setText(title);

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordDialog != null)
                passwordDialog.onClickForgotPin();
            }
        });

        if (backgroundColor != 0)
        linearDialogAnimated.setBackgroundColor(backgroundColor);


        //hide gif
        if (hideAnimation)  gifImage.setVisibility(View.GONE);
        else gifImage.setVisibility(View.VISIBLE);

        if (gif != 0)
        gifImage.setImageResource(gif);

        if (bottomText != null)
            textForgotPassword.setText(bottomText);

        if (bottomTexColor != 0)
            textForgotPassword.setTextColor(bottomTexColor);

        if (gravity == GRAVITY_CENTER){
            dialog.getWindow().setGravity(Gravity.CENTER);
        }else if (gravity == GRAVITY_BOTTOM){
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }else if (gravity == GRAVITY_TOP){
            dialog.getWindow().setGravity(Gravity.TOP);
        }


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (!dialog.isShowing()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setShowDialog(true); // in case of the orientation change the dialog will appear immediately
                    if (contOpenedDialog() == 0)
                        dialog.show();
                    setContOpenedDialog(1);

                }
            },getInterval());
        }


      /*  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setShowDialog(true);
                if (contOpenedDialog() == 0)
                    dialog.show();
                setContOpenedDialog(1);

            }
        };

        context.registerReceiver(broadcastReceiver, new IntentFilter("openDialog"));
        openAlarm();*/

       // dialog.show();
    }

    private void openAlarm(){
        Intent intent = new Intent(context, PinAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        long firstTime = calendar.getTimeInMillis();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, firstTime, pendingIntent);

    }

    private boolean shouldShowDialog(){
        return myPreferences.getBoolean("dialog", false);
    }

    private int contOpenedDialog(){
        return myPreferences.getInt("dialogOpened",0);
    }

    private boolean pinIsCorrect(){
        return myPreferences.getBoolean("pinOk", false);
    }

    private void setShowDialog(boolean showDialog){
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putBoolean("dialog",showDialog);
        editor.apply();
    }

    private void setContOpenedDialog(int cont){
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putInt("dialogOpened",cont);
        editor.apply();
    }

    private void setPinIsCorrect(boolean pinIsCorrect){
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putBoolean("pinOk",pinIsCorrect);
        editor.apply();
    }



    private int getInterval(){

        int repeatMS;
        if (delayTime != 0)
            repeatMS = delayTime;
        else repeatMS = 400;
       /* if (minutes != 0)
         repeatMS = 60000 * minutes;
        if (hours != 0)
            repeatMS = 3600000 * hours;
        if (days != 0)
            repeatMS = 86400000 * days;*/
        return repeatMS;
    }



    public static class Builder{


        private Context context;
        private int pinCodeLength;
        private int headerBackgroundColor;
        private int titleColor;
        private String title, error, bottomText;
        private boolean hideForgotPassword, hideAnimation;
        private PasswordDialog passwordDialog;
        private int yourPimCode, backgroundColor,  bottomTexColor, gravity;
        private int gif, delayTime;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setPinCodeLength(int pinCodeLength) {
            this.pinCodeLength = pinCodeLength;
            return this;
        }

        public Builder setHeaderBackgroundColor(int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setYourPimCode(int yourPimCode) {
            this.yourPimCode = yourPimCode;
            return this;
        }


        public Builder setHideForgotPassword(boolean hideForgotPassword) {
            this.hideForgotPassword = hideForgotPassword;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public Builder setHideAnimation(boolean hideAnimation) {
            this.hideAnimation = hideAnimation;
            return this;
        }

        public Builder setGif(int gif) {
            this.gif = gif;
            return this;
        }

        public Builder setBottomText(String bottomText, PasswordDialog passwordDialog) {
            this.bottomText = bottomText;
            this.passwordDialog = passwordDialog;
            return this;
        }

        public Builder setBottomTexColor(int bottomTexColor) {
            this.bottomTexColor = bottomTexColor;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setDelayTime(int delayTime) {
            this.delayTime = delayTime;
            return this;
        }


        public PinDialog build(){
            return new PinDialog(this);
        }
    }

    public interface PasswordDialog{

        void onClickForgotPin();
    }


}
