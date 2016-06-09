package com.example.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by Adas on 22.03.2016.
 */
public class AddOrEditCalendar extends AppCompatActivity {

    List<Integer> colorsArray = new ArrayList<>();
    List<String> colorsDescriptionArray = new ArrayList<>();
    int newColor = Color.WHITE;
    boolean anythingChanged = false;
    private String mCalendarName;
    private String mCalendarDescription;
    private String mCalendarColorsString;
    private String mCalendarColorsDescriptionString;
    private Toolbar toolbar;

    private List<CalendarColors> calendarsList = Collections.emptyList();

    private CalendarLoader calendarLoader;
    private ColorsDetailsRecyclerViewAdapter colorsDetailsRecyclerViewAdapter;

    private RecyclerView colorsRecyclerView;
    private EditText name;
    private EditText description;
    private TextView addNewCalendarButton;
    private TextView removeCalendarButton;
    private TextView cancelCalendarButton;
    private ImageView colorCircle;
    private GradientDrawable bgShape;
    private EditText colorDescriptionEdit;
    private TextView addColorButton;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_calendar_activity);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        calendarLoader = new CalendarLoader(getApplicationContext(), getContentResolver());
        
        calendarsList = calendarLoader.loadCalendarsInBackground();
        colorsDetailsRecyclerViewAdapter =
                new ColorsDetailsRecyclerViewAdapter(
                        AddOrEditCalendar.this,
                        colorsArray,
                        colorsDescriptionArray);

        initLayoutViews();
        initAddColorButton();

        index = (getIntent().getIntExtra("edit", -1));

        if (index > -1) {
            initEditMode();

        } else {
            initAddNewMode();
        }
    }

    private void initLayoutViews(){
        colorsRecyclerView = (RecyclerView) findViewById(R.id.colors_recycler_view);
        name = (EditText) findViewById(R.id.new_calendar_name);
        description = (EditText) findViewById(R.id.new_calendar_description);
        addNewCalendarButton = (TextView) findViewById(R.id.new_calendar_add_button);
        removeCalendarButton = (TextView) findViewById(R.id.remove_calendar_button);
        cancelCalendarButton = (TextView) findViewById(R.id.new_calendar_cancel_button);
        colorCircle = (ImageView) findViewById(R.id.circle);
        bgShape = (GradientDrawable) colorCircle.getBackground();
        colorDescriptionEdit = (EditText) findViewById(R.id.color_description_edit);
        addColorButton = (TextView) findViewById(R.id.add_color);


        colorsRecyclerView.setAdapter(colorsDetailsRecyclerViewAdapter);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        colorsRecyclerView.setLayoutManager(linearLayoutManager);

        cancelCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                onBackPressed();
            }
        });


        bgShape.setColor(Color.WHITE);
        colorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChromaDialog.Builder()
                        .initialColor(newColor)
                        .onColorSelected(
                                new ColorSelectListener() {
                                    @Override
                                    public void onColorSelected(int color) {
                                        newColor = color;
                                        bgShape.setColor(color);
                                        anythingChanged = true;
                                    }
                                })
                        .create()
                        .show(getSupportFragmentManager(), "ChromaDialog");
            }
        });

    }
    private void splitColorsToArray() {
        List<String> colorsArrayString = Arrays.asList(mCalendarColorsString.split("`"));

        if (colorsArray != null) {
            colorsArray.clear();
        }

        for (String s : colorsArrayString) {
            colorsArray.add(Integer.valueOf(s));
        }


        colorsArrayString = Arrays.asList(mCalendarColorsDescriptionString.split("`"));
        if (colorsDescriptionArray != null) {
            colorsDescriptionArray.clear();
        }

        for (String s : colorsArrayString) {
            colorsDescriptionArray.add(s);
        }
    }

    private void packColorsToString() {
        mCalendarColorsString = "";

        for (int s : colorsArray) {
            mCalendarColorsString = mCalendarColorsString + String.valueOf(s) + "`";
        }

        mCalendarColorsDescriptionString = "";

        for (String s : colorsDescriptionArray) {
            mCalendarColorsDescriptionString = mCalendarColorsDescriptionString + s + "`";
        }
    }

    public void updateColor(int position, String newColorDescription, int newColor) {

        colorsDescriptionArray.set(position, newColorDescription);
        colorsArray.set(position, newColor);
    }

    public void deleteColor(int position) {


        colorsDescriptionArray.remove(position);
        colorsArray.remove(position);
    }

    @Override
    public void onBackPressed() {
        if (!calendarsList.isEmpty()) {
            if (!anythingChanged) {
                super.onBackPressed();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditCalendar.this);
                builder.setTitle("CHANGES WILL NOT BE SAVED")
                        .setMessage("Are you sure about leaving?")
                        .setCancelable(true)
                        .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        })
                        .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        anythingChanged = false;
                                        AddOrEditCalendar.super.onBackPressed();

                                    }
                                }
                        )
                        .create().show();

            }
        }
    }


    private void initEditMode() {

        calendarsList.get(index);
        mCalendarName = calendarsList.get(index).getCalendarName();
        removeCalendarButton.setVisibility(View.VISIBLE);
        addNewCalendarButton.setText("SAVE");


        for (CalendarColors w : calendarsList) {

            if (w.getCalendarName().equals(mCalendarName)) {

                mCalendarDescription = w.getDescription();
                mCalendarColorsString = w.getColor();
                mCalendarColorsDescriptionString = w.getColorDescription();

                name.setText(mCalendarName);
                if (mCalendarDescription != null) {
                    description.setText(mCalendarDescription);
                }

                break;
            }
        }
        splitColorsToArray();
        colorsDetailsRecyclerViewAdapter.setData(colorsArray, colorsDescriptionArray);
        colorsDetailsRecyclerViewAdapter.notifyDataSetChanged();


        name.setText(mCalendarName);
        description.setText(mCalendarDescription);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anythingChanged = true;
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anythingChanged = true;
            }
        });

        addNewCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("")) {
                    boolean isDuplicated = false;
                    if (name.getText().toString().equals(mCalendarName)) {
                    } else {
                        for (CalendarColors w : calendarsList) {
                            if (w.getCalendarName().equals(name.getText().toString())) {
                                isDuplicated = true;
                                break;
                            }
                        }
                    }
                    if (!isDuplicated) {
                        packColorsToString();
                        calendarLoader.saveCalendarsToDatabase(mCalendarName, name.getText().toString(),
                                description.getText().toString(),
                                mCalendarColorsString, mCalendarColorsDescriptionString);

                        Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
                        intent.putExtra("goTo", index);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Calendar's name has to be unique.", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Calendar's name cannot be empty.", Toast.LENGTH_LONG).show();
            }
        });

        removeCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditCalendar.this);
                builder.setTitle("REMOVING CALENDAR")
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        calendarLoader.deleteFromDatabase(mCalendarName);
                                        Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
                                        intent.putExtra("goTo", -1);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();

            }

        });
    }

    private void initAddNewMode() {

        mCalendarColorsString = Color.parseColor("#45b70d") + "`" + Color.parseColor("#f5fc1e") + "`" + Color.parseColor("#ff1817") + "`";
        mCalendarColorsDescriptionString = "Done`Almost Done`Failed";
        splitColorsToArray();

        colorsDetailsRecyclerViewAdapter.setData(colorsArray, colorsDescriptionArray);
        colorsDetailsRecyclerViewAdapter.notifyDataSetChanged();

        addNewCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("")) {
                    boolean isDuplicated = false;
                    for (CalendarColors w : calendarsList) {
                        if (w.getCalendarName().equals(name.getText().toString())) {
                            isDuplicated = true;
                            break;
                        }
                    }
                    if (!isDuplicated) {

                        packColorsToString();
//                            Toast.makeText(AddOrEditCalendar.this, "aaa"+mCalendarColorsString+"aaaaa", Toast.LENGTH_LONG).show();
//                            Toast.makeText(AddOrEditCalendar.this, colorsArray.toString(), Toast.LENGTH_LONG).show();
                        calendarLoader.saveCalendarsToDatabase(null, name.getText().toString(),
                                description.getText().toString(),
                                mCalendarColorsString, mCalendarColorsDescriptionString);
                        Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
                        intent.putExtra("goTo", -1);
                        startActivity(intent);
                        finish();


                    } else
                        Toast.makeText(getApplicationContext(), "Calendar's name has to be unique.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Calendar's name cannot be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initAddColorButton(){
        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorDescriptionEdit.getVisibility() == View.VISIBLE && !colorDescriptionEdit.getText().toString().equals("")) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    colorsArray.add(newColor);
                    colorsDescriptionArray.add(colorDescriptionEdit.getText().toString());
                    colorsDetailsRecyclerViewAdapter.setData(colorsArray, colorsDescriptionArray);
                    colorsDetailsRecyclerViewAdapter.notifyDataSetChanged();

                    colorDescriptionEdit.setText("");
                    colorsRecyclerView.smoothScrollToPosition(colorsArray.size() - 1);
                    anythingChanged = true;

                } else {

                    colorDescriptionEdit.setVisibility(View.VISIBLE);
                    Toast.makeText(AddOrEditCalendar.this, "Color description cannot be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
