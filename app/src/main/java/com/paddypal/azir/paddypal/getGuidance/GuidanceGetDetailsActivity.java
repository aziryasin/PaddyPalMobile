package com.paddypal.azir.paddypal.getGuidance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.paddypal.azir.paddypal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class GuidanceGetDetailsActivity extends AppCompatActivity {
    CardView Next;
    EditText startDate;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String ageGroup,sDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance_get_details);

        final MaterialSpinner cropAgeGroups=findViewById(R.id.cropTypesList);
        cropAgeGroups.setItems("3 month", "3 1/2 month");

        cropAgeGroups.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position ==0){
                    ageGroup="3m";
                }
                if(position ==1){
                    ageGroup="3.5m";
                }
            }
        });
        myCalendar = Calendar.getInstance();
        startDate=findViewById(R.id.startDate);
        Next=findViewById(R.id.BtnNext);

        date= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                sDate=getStartDate();
                startDate.setText(sDate);
            }

        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GuidanceGetDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ageGroup != null && sDate != null){
                    Intent intent=new Intent(getApplicationContext(),GuidanceActivity.class);
                    intent.putExtra("ageGroup",ageGroup);
                    intent.putExtra("startDate",sDate);

                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please select age group and starting date",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private String getStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        String startDate=sdf.format(myCalendar.getTime());
        return  startDate;
    }


}
