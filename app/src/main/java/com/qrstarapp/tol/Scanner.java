package com.qrstarapp.tol;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class Scanner extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final int PERMISSION_REQUEST_CODE = 1;

    ImageButton fileicon;
    Button      filemanger;
    ImageButton scannericon;
    Button      scanner;
    private Activity mContext;

    public Scanner()
    {
    }


    public static Scanner newInstance(String param1, String param2) {
        Scanner fragment = new Scanner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_scanner, container, false);
        fileicon= (ImageButton) rootView.findViewById(R.id.imagefile);
        filemanger= (Button) rootView.findViewById(R.id.buttonfile);
        scanner= (Button) rootView.findViewById(R.id.buttonCamera);
        scannericon= (ImageButton) rootView.findViewById(R.id.imageCamera);

        fileicon.setOnClickListener(this);
        filemanger.setOnClickListener(this);
        scanner.setOnClickListener(this);
        scannericon.setOnClickListener(this);



      //  AdView mAdView = (AdView) rootView.findViewById(R.id.adVieScanner);
     //   AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);


        return rootView;
    }


    @Override
    public void onClick(View v)
    {
        int id= v.getId();
        Intent intent=null;
        switch (id)
        {
            case R.id.imageCamera :
                  intent=new Intent(getActivity(),CamScanne.class);
                    startActivity(intent);


                break;
            case R.id.buttonCamera :
                intent=new Intent(getActivity(),CamScanne.class);
                    startActivity(intent);

                break;
            case R.id.imagefile :

                intent=new Intent(getActivity(),FileManger.class);
                startActivity(intent);
                break;
            case R.id.buttonfile :

                intent=new Intent(getActivity(),FileManger.class);
                startActivity(intent);
                break;
        }
    }



    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){

            Toast.makeText(mContext,getResources().getString(R.string.perm),Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Intent intent;
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Permission Granted, Now you can access Scanner .",Toast.LENGTH_LONG).show();
                    intent=new Intent(getActivity(),CamScanne.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(getActivity(),"Permission Denied, You cannot access Scanner",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}

