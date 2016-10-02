package com.qrstarapp.tol;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import zxing.BarcodeFormat;
import zxing.EncodeHintType;
import zxing.MultiFormatWriter;
import zxing.WriterException;
import zxing.common.BitMatrix;


public  class Generator extends Fragment {
    Bitmap bitmap=null;
    ImageView iv;
    String barcode_data=null;
    Button generator;
    EditText data;
    Button Share;
    Button helpp;
    Button Save;
    File file;
    private String root;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private Activity mContext;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.generator, container, false);

                   iv = (ImageView) rootView.findViewById(R.id.barCodeImage);

        generator = (Button) rootView.findViewById(R.id.generatorButton);
        helpp = (Button) rootView.findViewById(R.id.help);
        data      = (EditText) rootView.findViewById(R.id.datatext);
        Share     = (Button) rootView.findViewById(R.id.shareButton);
        Save      = (Button) rootView.findViewById(R.id.SaveButton);
        final ScrollView scrollView= (ScrollView) rootView.findViewById(R.id.bottom);

       // AdView mAdView = (AdView) rootView.findViewById(R.id.adViewgen);
    //    AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);


        if(checkPermission()) {

        }
        else{
            requestPermission();
    }










      // data.setText(Home.DataGetter());

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    barcode_data=data.getText().toString();

                         if(!barcode_data.matches(""))
                         {
                            barcode_data=data.getText().toString();
                            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.QR_CODE, 600, 400);

                             Canvas canvas = new Canvas(bitmap);
                             Paint paint = new Paint();
                             paint.setColor(Color.BLACK);
                             paint.setTextSize(40);
                             canvas.drawText("By QR Star", 210, 390, paint);


                            iv.setImageBitmap(bitmap);
                             scrollView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Empty Data",Toast.LENGTH_SHORT).show();
                        }

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iinent= new Intent(getActivity(),helpact.class);
                startActivity(iinent);



            }
        });



        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkPermission())
                {
                    saving();
                }
                else
                    requestPermission();
            }
        });

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(bitmap!=null)
            {
                String pathofBmp =root;
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/png");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                startActivity(Intent.createChooser(shareIntent, "Share image using"));

            }
            }
        });

        return rootView;
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(file.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void saving()
    {
        if(bitmap!=null)
        {

            Share.setVisibility(View.VISIBLE);
            root = Environment.getExternalStorageDirectory()+ "/qr_codes";
            File myDir = new File(root) ;
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".png";
            file = new File(myDir, fname);
            Log.i("test", "" + file);
            if (file.exists())
                file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                out.flush();
                out.close();
                Toast.makeText(getActivity(),getResources().getString(R.string.saved),Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),getResources().getString(R.string.nosaved),Toast.LENGTH_SHORT).show();
            }
            galleryAddPic();


        }

    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){

           // Toast.makeText(getActivity(),"GPS permission allows us to access External Data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(),getResources().getString(R.string.perm),Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Permission Granted, Now you can access Storage .",Toast.LENGTH_LONG).show();
                    saving();

                } else {

                    Toast.makeText(getActivity(),"Permission Denied, You cannot access Storage",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }




}

