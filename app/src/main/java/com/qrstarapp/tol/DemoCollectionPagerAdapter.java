package com.qrstarapp.tol;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

import com.qrstarapp.tol.Generator;
import com.qrstarapp.tol.R;
import com.qrstarapp.tol.Scanner;

/**
 * Created by subtainishfaq on 6/12/16.
 */
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    Context cont;
    Activity activity;
    public DemoCollectionPagerAdapter(FragmentManager fm , Context context)
    {
        super(fm);
        cont=context;
        this.activity=activity;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        switch(i)
        {
            case 0:
                fragment = new Generator();
                return fragment;
            case 1 :
                fragment = new Scanner();
                return fragment;

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:

                return cont.getResources().getString(R.string.Generator);
            case 1 :
                return cont.getResources().getString(R.string.scan);


        }
        return null;
    }
}