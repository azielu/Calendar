package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
    public float px;
    private CalendarLoader calendarLoader;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    public ViewPager mPager;
    private DrawerLayout mDrawerLayout;
    public RecyclerView mDrawerList;
    private List<String> calendarsNames = new ArrayList<>();
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean isLocked = true;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private RelativeLayout unlockingBtn = (RelativeLayout) findViewById(R.id.unlock_btn);
    private ImageView unlockingImg = (ImageView) findViewById(R.id.unlock_image);
    private TextView unlockingTxt = (TextView) findViewById(R.id.unlock_text);


    private void initCalendarsNames() {
        calendarsNames.clear();
        for (CalendarColors w : calendarLoader.loadCalendarsInBackground()) {
            if (w.getCalendarName() != null && !w.getCalendarName().equals("")) {
                calendarsNames.add(w.getCalendarName());
            }

        }
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.drawer_list_view);
        mTitle = mDrawerTitle = getTitle();
        DrawerRecyclerViewAdapter drawerRecyclerViewAdapter =
                new DrawerRecyclerViewAdapter(
                        ScreenSlidePagerActivity.this,
                        calendarsNames);

        mDrawerList.setAdapter(drawerRecyclerViewAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(linearLayoutManager);


        mDrawerList.addOnItemTouchListener(new RecyclerItemClickListener(this, mDrawerList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        mPager.setCurrentItem(position, true);
                        mDrawerLayout.closeDrawers();
                        mDrawerList.scrollToPosition(0);
                    }
                }
        ));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                mToolbar,         /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initPageViewer() {
        mPager = (ViewPager) findViewById(R.id.pager);

        updateNumPages();

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(NUM_PAGES);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

                                       {
                                           @Override
                                           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                           }

                                           @Override
                                           public void onPageSelected(int position) {
                                               mDrawerList.getAdapter().notifyDataSetChanged();
                                           }

                                           @Override
                                           public void onPageScrollStateChanged(int state) {

                                           }
                                       }

        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);


        calendarLoader = new CalendarLoader(getApplicationContext(), getContentResolver());

        initCalendarsNames();
        initDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setHomeButtonEnabled(true);

        initPageViewer();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT) || mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
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
    }

    @Override
    protected void onResume() {
        this.doubleBackToExitPressedOnce = false;
        super.onResume();

        updateNumPages();
        calendarsNames.clear();
        for (CalendarColors w : calendarLoader.loadCalendarsInBackground()) {
            if (w.getCalendarName() != null && !w.getCalendarName().equals("")) {
                calendarsNames.add(w.getCalendarName());
            }

        }
        mPager.getAdapter().notifyDataSetChanged();

        DrawerRecyclerViewAdapter drawerRecyclerViewAdapter =
                new DrawerRecyclerViewAdapter(
                        ScreenSlidePagerActivity.this,
                        calendarsNames);

        mDrawerList.setAdapter(drawerRecyclerViewAdapter);


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


        initBottomButtons();
    }

    public void initBottomButtons() {

        unlockingBtn = (RelativeLayout) findViewById(R.id.unlock_btn);
        unlockingImg = (ImageView) findViewById(R.id.unlock_image);
        unlockingTxt = (TextView) findViewById(R.id.unlock_text);

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

                ScreenSlidePageFragment currentFragment = (ScreenSlidePageFragment) getSupportFragmentManager()
                        .getFragments().get((mPager.getCurrentItem() + 1) % (getSupportFragmentManager().getFragments().size()));
                currentFragment.editCalendar();
            }

        });
    }

    public void updateNumPages() {

        NUM_PAGES = 0;
        calendarsNames.clear();
        for (CalendarColors w : calendarLoader.loadCalendarsInBackground()) {
            if (!w.getCalendarName().equals(null)) {
                calendarsNames.add(w.getCalendarName());
                NUM_PAGES++;
            }
        }

        if (NUM_PAGES == 0) {
            Intent intent = new Intent(getApplicationContext(), AddOrEditCalendar.class);
            startActivity(intent);
        }
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

}
