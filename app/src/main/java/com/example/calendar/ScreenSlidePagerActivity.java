package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ScreenSlidePagerActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    public static int NUM_PAGES = 0;
    private CalendarLoader calendarLoader;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private List<String> calendarsNames = new ArrayList<>();
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean isLocked = true;
    public float px;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        calendarLoader = new CalendarLoader(getApplicationContext(), getContentResolver());
        calendarsNames.clear();
        for (CalendarColors w : calendarLoader.loadCalendarsInBackground()) {
            if (w.getCalendarName() != null && w.getCalendarName() != "") {
                calendarsNames.add(w.getCalendarName());
            }

        }

//        Resources r = getResources();
//        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, R.id.drawer_textview, calendarsNames));
        // Set the list's click listener

        mPager = (ViewPager) findViewById(R.id.pager);
        updateNumPages();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    @Override
    protected void onResume() {
        this.doubleBackToExitPressedOnce = false;
        super.onResume();

        updateNumPages();
        mPager.getAdapter().notifyDataSetChanged();

        // Instantiate a ViewPager and a PagerAdapter.


        switch (getIntent().getIntExtra("goTo", 0)) {
            case 0: {
                mPager.setCurrentItem(0, true);
                break;
            }

            case -1: {
                mPager.setCurrentItem(NUM_PAGES, true);
                getIntent().putExtra("goTo", 0);
                break;
            }

            default: {
                mPager.setCurrentItem(getIntent().getIntExtra("goTo", 0), true);
                getIntent().putExtra("goTo", 0);
                break;
            }

        }

        bottomButtons();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ScreenSlidePageFragment();
            Bundle args = new Bundle();
            args.putInt("index", position);
            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void bottomButtons() {

        final RelativeLayout unlockingBtn = (RelativeLayout) findViewById(R.id.unlock_btn);
        final ImageView unlockingImg = (ImageView) findViewById(R.id.unlock_image);
        final TextView unlockingTxt = (TextView) findViewById(R.id.unlock_text);

        unlockingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLocked) {
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) f;
                        currentFragment.unlockScreen();
                    }
                    unlockingImg.setBackgroundResource(R.drawable.unlocked);
                    unlockingTxt.setText("Unlocked");

                    isLocked = false;
                } else {
                    for (Fragment f : getSupportFragmentManager().getFragments()) {
                        ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) f;
                        currentFragment.lockScreen();
                    }

                    unlockingImg.setBackgroundResource(R.drawable.locked);
                    unlockingTxt.setText("Locked");
                    isLocked = true;
                }
            }
        });

        final RelativeLayout newCalendar = (RelativeLayout) findViewById(R.id.new_calendar);
        newCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) getSupportFragmentManager()
                        .getFragments().get((mPager.getCurrentItem() + 1) % (getSupportFragmentManager().getFragments().size()));
                currentFragment.newCalendar();
            }
        });

        final RelativeLayout editCalendar = (RelativeLayout) findViewById(R.id.edit_calendar);
        editCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) getVisibleFragment();
                ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) getSupportFragmentManager()
                        .getFragments().get((mPager.getCurrentItem() + 1) % (getSupportFragmentManager().getFragments().size()));
                currentFragment.editCalendar();
            }

        });


    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = ScreenSlidePagerActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public void updateNumPages() {
        NUM_PAGES = 0;
        calendarsNames.clear();
        for (CalendarColors w : calendarLoader.loadCalendarsInBackground()) {
            if (w.getCalendarName() != "null") {
                calendarsNames.add(w.getCalendarName());
                NUM_PAGES++;
            }
        }

        if (NUM_PAGES == 0) {
            Intent intent = new Intent(getApplicationContext(), AddOrEditCalendar.class);
            startActivity(intent);
        }
    }
}
