package com.example.parkjunghun.where.where.Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.MenuItem;

import com.example.parkjunghun.where.where.Fragment.Map2Fragment;
import com.example.parkjunghun.where.where.Fragment.MapFragment;

/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MapFragment();
            case 1:
                return new Map2Fragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }

}
