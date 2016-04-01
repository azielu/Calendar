package com.example.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScreenSlidePageFragment extends Fragment {

    final Calendar mCalendar = Calendar.getInstance();
    final Calendar mActualCalendar = Calendar.getInstance();
    private CalendarLoader calendarLoader;
    private List<Day> dayList;

    ViewGroup rootView;

    GridLayout gridLayout;


    private int index;
    String calendarName = "";
    String calendarDescriptions = "";
    List<Integer> colorsArray = new ArrayList<>();

    TextView calendarNameTextView;
    TextView calendarDescriptionTextView;
    public boolean isLocked = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        gridLayout = (GridLayout) rootView.findViewById(R.id.calendar_gridlayout);
        index = getArguments().getInt("index");

        calendarLoader = new CalendarLoader(getActivity().getApplicationContext(), getActivity().getContentResolver());
        calendarNameTextView = (TextView) rootView.findViewById(R.id.calendar_name);
        calendarDescriptionTextView = (TextView) rootView.findViewById(R.id.calendar_description);


        DisplayCalendar();

        RelativeLayout prevBtn = (RelativeLayout) rootView.findViewById(R.id.prev_btn);
        RelativeLayout nextBtn = (RelativeLayout) rootView.findViewById(R.id.next_btn);


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarLoader.saveToDatabase(dayList);
                mCalendar.add(Calendar.MONTH, -1);
                DisplayCalendar();

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarLoader.saveToDatabase(dayList);
                mCalendar.add(Calendar.MONTH, 1);
                DisplayCalendar();

            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getCalendarsFromDatabase();
        calendarNameTextView.setText(calendarName);
        calendarDescriptionTextView.setVisibility(View.VISIBLE);
        calendarDescriptionTextView.setText(calendarDescriptions);
        if (calendarDescriptions.equals("")) {
            calendarDescriptionTextView.setVisibility(View.GONE);
        }
        DisplayCalendar();
    }

    public void DisplayCalendar() {
        getCalendarsFromDatabase();
        GridLayout gridLayout = (GridLayout) rootView.findViewById(R.id.calendar_gridlayout);

        dayList = calendarLoader.loadInBackground(mCalendar, calendarName);
        // Toast.makeText(getActivity().getApplicationContext(), String.valueOf(dayList.size()), Toast.LENGTH_SHORT).show();
        int today = mActualCalendar.get(Calendar.DAY_OF_MONTH);

        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int beginningDay = mCalendar.get(Calendar.DAY_OF_WEEK);
        int dayDisplayed = 1;
        if (gridLayout.getChildCount() > 0) {
            gridLayout.removeAllViews();
        }


//        String[] dayOfWeek = new String[]{"Mo.", "Tu.", "We.", "Th.", "Fr.", "Sa.", "Su."};
//
//        for (int i = 0; i < 7; i++) {
//            TextView text = new TextView(getActivity().getApplicationContext());
//            text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
//            text.setText(dayOfWeek[i]);
//            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//            text.setGravity(Gravity.CENTER);
//            text.setBackgroundColor(Color.BLUE);
//
//            LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.CENTER);
//            linearLayout.setLayoutParams(params);
//            //linearLayout.setPadding(0, 20, 0, 20);
//            linearLayout.setGravity(Gravity.CENTER);
//            gridLayout.addView(linearLayout);
//            linearLayout.addView(text);
//
//        }

        TextView yearText = (TextView) rootView.findViewById(R.id.year_textview);
        TextView monthText = (TextView) rootView.findViewById(R.id.month_textview);
        LinearLayout resetDate = (LinearLayout) rootView.findViewById(R.id.reset_date);
        resetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarLoader.saveToDatabase(dayList);
                mCalendar.set(mActualCalendar.get(Calendar.YEAR), mActualCalendar.get(Calendar.MONTH), mActualCalendar.get(Calendar.DAY_OF_MONTH));
                DisplayCalendar();
            }
        });
        yearText.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
        monthText.setText(String.valueOf(mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)));


        for (int i = 0; i < beginningDay + mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 2; i++) {
            final LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());

           // getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(params);
            final TextView text = new TextView(getActivity().getApplicationContext());
            text.setTextColor(Color.BLACK);
            linearLayout.setBackgroundResource(R.drawable.shape);
            GradientDrawable bgShape = (GradientDrawable) linearLayout.getBackground();

            if (i < beginningDay - 2) {
                text.setText("");
                bgShape.setColor(Color.TRANSPARENT);
                bgShape.setStroke(0, Color.TRANSPARENT);

            } else {
                bgShape.setColor(colorsArray.get(0));
                bgShape.setStroke(2, ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                text.setId(dayDisplayed);
                linearLayout.setId(1);
                for (int j = 0; j < dayList.size(); j++) {
                    if (dayList.get(j).getYear() == mCalendar.get(Calendar.YEAR)
                            && dayList.get(j).getMonth() == mCalendar.get(Calendar.MONTH)
                            && dayList.get(j).getDay() == dayDisplayed) {
                        if (dayList.get(j).getColor() < colorsArray.size()) {
                            bgShape.setColor(colorsArray.get(dayList.get(j).getColor()));
                        } else {
                            bgShape.setColor(colorsArray.get(0));
                            dayList.get(j).setColor(0);
                        }
                    }
                }
                if (dayDisplayed == today && mActualCalendar.get(Calendar.YEAR) == mCalendar.get(Calendar.YEAR)
                        && mActualCalendar.get(Calendar.MONTH) == mCalendar.get(Calendar.MONTH)) {
                    text.setText(String.valueOf(dayDisplayed));
                    text.setTypeface(null, Typeface.BOLD);
                    linearLayout.setEnabled(true);
                    linearLayout.setId(0);

                    bgShape.setStroke(6,ContextCompat.getColor(getContext(), R.color.colorTextSecondary));

                } else {

                    text.setText(String.valueOf(dayDisplayed));

                    bgShape.setStroke(2, !isLocked ? Color.BLACK : Color.GRAY);
                    linearLayout.setEnabled(!isLocked);
                }
                dayDisplayed++;
            }
            linearLayout.addView(text);
            gridLayout.addView(linearLayout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (text.getText() != "") {
                        int year = mCalendar.get(Calendar.YEAR);
                        int month = mCalendar.get(Calendar.MONTH);
                        int day = text.getId();
                        int _id = -1;
                        for (int j = 0; j < dayList.size(); j++) {
                            if (dayList.get(j).getYear() == mCalendar.get(Calendar.YEAR)
                                    && dayList.get(j).getMonth() == mCalendar.get(Calendar.MONTH)
                                    && dayList.get(j).getDay() == day) {
                                _id = j;
                            }
                        }
                        if (_id > -1) {
                            dayList.get(_id).setColor((dayList.get(_id).getColor() + 1) % colorsArray.size());
                            GradientDrawable bgShape = (GradientDrawable) linearLayout.getBackground();
                            bgShape.setColor(colorsArray.get(dayList.get(_id).getColor()));
                            // calendarLoader.saveToDatabase(dayList);

                        } else {
                            dayList.add(new Day(calendarName, year, month, day, 1));
                            GradientDrawable bgShape = (GradientDrawable) linearLayout.getBackground();

                            bgShape.setColor(colorsArray.get(1 % colorsArray.size()));
                            // calendarLoader.saveToDatabase(dayList);

                        }
                    }
                }
            });
        }

    }

    private void getCalendarsFromDatabase() {
        List<CalendarColors> calendarColorsList = calendarLoader.loadCalendarsInBackground();
        calendarName = calendarColorsList.get(index).getCalendarName();
        calendarDescriptions = calendarColorsList.get(index).getDescription();
        List<String> colorsArrayString = Arrays.asList(calendarColorsList.get(index).getColor().split("`"));
        if (colorsArray != null) {
            colorsArray.clear();
        }
        colorsArray.add(Color.TRANSPARENT);

        for (String s : colorsArrayString) {
            colorsArray.add(Integer.valueOf(s));
        }

    }

    @Override
    public void onPause() {
        calendarLoader.saveToDatabase(dayList);
        super.onPause();

    }

    @Override
    public void onStop() {
        calendarLoader.saveToDatabase(dayList);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        calendarLoader.saveToDatabase(dayList);
        super.onDestroy();
    }

    public void unlockScreen() {
        isLocked=false;
        GradientDrawable bgShape;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {

            if (gridLayout.getChildAt(i).getId() > -1) {
                bgShape = (GradientDrawable) gridLayout.getChildAt(i).getBackground();
                if (gridLayout.getChildAt(i).getId() == 0) {
                    bgShape.setStroke(6, Color.BLACK);
                } else {
                    gridLayout.getChildAt(i).setEnabled(true);

                    bgShape.setStroke(2, Color.BLACK);

                }
                gridLayout.getChildAt(i).setEnabled(true);

            }
        }

    }

    public void lockScreen() {

        isLocked=true;
        GradientDrawable bgShape;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            if (gridLayout.getChildAt(i).getId() > -1) {
                bgShape = (GradientDrawable) gridLayout.getChildAt(i).getBackground();
                if (gridLayout.getChildAt(i).getId() == 0) {
                    bgShape.setStroke(6, Color.BLACK);
                    gridLayout.getChildAt(i).setEnabled(true);
                } else {
                    gridLayout.getChildAt(i).setEnabled(false);
                    bgShape.setStroke(2, Color.GRAY);

                }
            }
        }
    }

    public void newCalendar() {
        calendarLoader.saveToDatabase(dayList);
        Intent intent = new Intent(getActivity().getApplicationContext(), AddOrEditCalendar.class);
        startActivity(intent);
    }

    public void editCalendar() {
        calendarLoader.saveToDatabase(dayList);
        Intent intent = new Intent(getActivity().getApplicationContext(), AddOrEditCalendar.class);
        intent.putExtra("edit", calendarName);
        startActivity(intent);
    }
}
