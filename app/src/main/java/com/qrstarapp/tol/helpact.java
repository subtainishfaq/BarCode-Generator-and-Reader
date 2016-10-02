package com.qrstarapp.tol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zxing.BarcodeFormat;
import zxing.WriterException;

public class helpact extends Activity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);


        Button shareapp;
        Button rateapp;
        shareapp = (Button) findViewById(R.id.shareapp);
        rateapp = (Button) findViewById(R.id.rateapp);


        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "qr star https://play.google.com/store/apps/details?id=com.qrstarapp.tol");
                startActivity(shareIntent);



            }
        });



        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String url = "https://play.google.com/store/apps/details?id=com.qrstarapp.tol";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });




    }


}


