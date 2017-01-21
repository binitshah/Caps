package com.binitshah.caps;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends FragmentActivity {

    Button anonStart;
    Button signinStart;
    ViewPager welcomeviewPager;
    private static final int NUM_PAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeviewPager = (ViewPager) findViewById(R.id.welcome_pager);
        welcomeviewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        welcomeviewPager.setClipToPadding(false);
        welcomeviewPager.setPadding(130,0,130,0);
        welcomeviewPager.setPageMargin(30);

        anonStart = (Button) findViewById(R.id.anonStart);
        anonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("CAPS", MODE_PRIVATE).edit();
                editor.putBoolean("firsttimeboot", false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signinStart = (Button) findViewById(R.id.signinStart);
        signinStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("CAPS", MODE_PRIVATE).edit();
                editor.putBoolean("firsttimeboot", false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            welcomeFragment.setArguments(bundle);
            return welcomeFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
