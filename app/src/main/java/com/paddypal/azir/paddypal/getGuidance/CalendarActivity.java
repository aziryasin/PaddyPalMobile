package com.paddypal.azir.paddypal.getGuidance;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.paddypal.azir.paddypal.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.lang.String;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    TextView monthYear,eventsTxt;
    String guidanceString;
    GuidanceResponse guidanceResponse;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final CompactCalendarView compactCalendarView=findViewById(R.id.compactcalendar_view);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        guidanceString=sharedPreferences.getString("guidance","");

        ObjectMapper objectMapper=new ObjectMapper();
        try {
            guidanceResponse=objectMapper.readValue(guidanceString,GuidanceResponse.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        List<Guidance> guidanceList=guidanceResponse.getObject();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Guidance guidance:guidanceList){
            try{
                Date date = formatter.parse(guidance.getDate());
                long dateInLong = date.getTime();
                Event event=new Event(Color.GREEN,dateInLong,guidance.getDetailedDescription());
                compactCalendarView.addEvent(event);
            }catch (ParseException e){
                e.printStackTrace();
            }

        }
        monthYear=findViewById(R.id.monthYearTxt);
        eventsTxt=findViewById(R.id.eventsTxt);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.scrollLeft();
        compactCalendarView.scrollRight();


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d("Calendar", "Day was clicked: " + dateClicked + " with events " + events);
                try{
                    eventsTxt.setText("Events \n\n"+dateFormatForDay.format(dateClicked)+"\n"+events.get(0).getData());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("Calendar", "Month was scrolled to: " + firstDayOfNewMonth);
                monthYear.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }
}
