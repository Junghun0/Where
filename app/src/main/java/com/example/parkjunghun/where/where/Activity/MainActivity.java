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
import com.example.parkjunghun.where.where.Fragment.MyInfoFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;

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
        //bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemselectedListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mymap) {
                    viewPager.setCurrentItem(0);
                    return true;
                }
                if (item.getItemId() == R.id.mymap2) {
                    viewPager.setCurrentItem(1);
                    return true;
                }
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.myinfo) {
                    getFragmentManager().beginTransaction().addToBackStack("TEXT_VIEWER_BACKSTACK").replace(R.id.main_framelayout, new MyInfoFragment()).commit();
                }

                if (item.getItemId() == R.id.howuse) {
                    getFragmentManager().beginTransaction().addToBackStack("TEXT_VIEWER_BACKSTACK").replace(R.id.main_framelayout, new InfoFragment()).commit();
                }

                if (item.getItemId() == R.id.logout) {
                    getFragmentManager().beginTransaction().addToBackStack("TEXT_VIEWER_BACKSTACK").replace(R.id.main_framelayout, new LogoutFragment()).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.destroyDrawingCache();
        viewPager.setAdapter(viewPagerAdapter);
    }

}
