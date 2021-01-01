# PinDialog
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

PinDalog is a simple library that aims to quickly create a requested pin verification dialog whenever the application is opened, avoiding unauthorized use of it.

![pinDialogGif](https://user-images.githubusercontent.com/76568808/103243749-d5aac900-496b-11eb-815b-edf739039343.gif)

## Get started
Add the following code to your activity or fragment.
````java
new PinDialog.Builder(this)
        .setYourPimCode(yourPimCode)
        .setPinCodeLength(String.valueOf(yourPimCode).length())
        .build();
````

#### Optional Parameters

- ```setDelayTime``` This parameter sets the delay time that the dialog will appear in milliseconds
- ```setHideAnimation``` Boolean parameter that shows or hides the animated gif (default true).
- ```setBottomTextColor``` sets the color of the action text (“Forgot”).
- ```setHeaderBackgroundColor``` sets the background color of the top.
- ```setBackgroundColor``` sets the background color of bottom.
- ```setTitle``` sets the title of the dialog.
- ```setTitleColor``` sets the color of the dialog title.
- ```setGravity``` sets the gravity of the dialog (default: PinDialog.GRAVITY_CENTER).
- ```setGif``` change the dialog gif.
- ```setBottomText``` set the action text title and handle the click event.
- ```setHideForgotPassword``` hides the bottom text (default false).
- ```setError``` set the error message that will be shown if the pin is wrong.

#### Full Example

````java
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
````
#### Dependencies
Add line below to your top level build.gradle.
````
buildscript {
repositories {
        mavenCentral()
    }
}
````
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        mavenCentral()
        
}
```
Add lines below to your app level build.gradle
```
implementation 'com.github.MateusLimaP:PinDialog:0.1.0'
```
