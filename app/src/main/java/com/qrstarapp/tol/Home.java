package com.qrstarapp.tol;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;


import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;


public class Home  extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.

    private Activity mContext;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
private static String Data="Data";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final ActionBar actionBar = getActionBar();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        OneSignal.startInit(this).init();



        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4035233662996005~4115556575");

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }


        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);






    }

        private void handleSendText(Intent intent)
        {

            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                // Update UI to reflect text being shared
                Home.Data=sharedText;
            }

        }
    public static String DataGetter()
    {
    return Home.Data;
    }


}


