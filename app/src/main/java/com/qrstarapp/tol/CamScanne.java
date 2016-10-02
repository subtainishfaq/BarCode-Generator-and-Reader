package com.qrstarapp.tol;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.Result;


import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CamScanne extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private Activity context;
    private Dialog dialog;
    private Button copy;
    private Button share;
    private Button done;
    private Button readAgain;
    private Button openurl;
    private EditText result;
    private String decoded;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        // Programmatically initialize the scanner view
        if(checkPermission())
            setContentView(mScannerView);                // Set the scanner view as the content view
        else
            requestPermission();


        dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        openurl= (Button) dialog.findViewById(R.id.openurl);
        copy= (Button) dialog.findViewById(R.id.copyButton);
        share= (Button) dialog.findViewById(R.id.shareButtonc);
        done= (Button) dialog.findViewById(R.id.DoneButtonc);
        readAgain= (Button) dialog.findViewById(R.id.ReadAgain);
        result=(EditText)dialog.findViewById(R.id.resultText);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4035233662996005/7069022975");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                dialog.show();
            }
        });



        requestNewInterstitial();




        readAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mScannerView.resumeCameraPreview(CamScanne.this);
                   }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(decoded);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", decoded);
                    clipboard.setPrimaryClip(clip);
                }



            }
        });

        openurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String url = decoded;
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);



            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, decoded);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.Share)));

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });





    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    // copy text to clipboard
    private void setClipboard(Context context1, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here

        decoded=rawResult.getText();
        Log.v("TEST", rawResult.getText()); // Prints scan results
        //Log.v("Test", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

      //  Toast.makeText(context, getResources().getString(R.string.tostcopy),
             //   Toast.LENGTH_LONG).show();


        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.tostcopy);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        result.setText(decoded);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            dialog.show();
        }





        // If you would like to resume scanning, call this method below:


    }



    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

            Toast.makeText(this,getResources().getString(R.string.perm),Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Intent intent;
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setContentView(mScannerView);                // Set the scanner view as the content view


                } else {

finish();
                }
                break;
        }
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}