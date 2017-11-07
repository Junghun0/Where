package com.example.parkjunghun.where.where.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Adapter.ViewPagerAdapter;
import com.example.parkjunghun.where.where.Fragment.InfoFragment;
import com.example.parkjunghun.where.where.Fragment.LogoutFragment;
import com.example.parkjunghun.where.where.Fragment.Map2Fragment;
import com.example.parkjunghun.where.where.Fragment.MapFragment;
import com.example.parkjunghun.where.where.Fragment.MyInfoFragment;
import com.example.parkjunghun.where.where.Fragment.SettingFragment;
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemselectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.mymap) {
                getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new MapFragment()).addToBackStack(null).commit();
            }
            if (item.getItemId() == R.id.mymap2) {
                getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new Map2Fragment()).disallowAddToBackStack().commit();
            }
            return true;
        }
    };
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //상단에 상태줄 없애는 코드!
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemselectedListener);


        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.myinfo) {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new MyInfoFragment()).commit();
                }

                if (item.getItemId() == R.id.first) {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new SettingFragment()).commit();
                }

                if (item.getItemId() == R.id.second) {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new SettingFragment()).commit();

                }
                if (item.getItemId() == R.id.howuse) {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new InfoFragment()).commit();
                }

                if (item.getItemId() == R.id.logout) {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new LogoutFragment()).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(viewPagerAdapter);
    }

}
