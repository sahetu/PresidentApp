package president.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeActivity extends AppCompatActivity {

    EditText dateEdit,timeEdit;
    Calendar cal;

    int iHour=0,iMinute=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        dateEdit = findViewById(R.id.date_time_datepicker);
        timeEdit = findViewById(R.id.date_time_timepicker);

        datePickerMethod();
        timePickerMethod();

    }

    private void timePickerMethod() {

        TimePickerDialog.OnTimeSetListener timeClick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                iHour = i;
                iMinute = i1;

                String sAMPM;
                if(i==12){
                    sAMPM = "PM";
                }
                else if(i>12){
                    iHour -=12;
                    sAMPM = "PM";
                }
                else if(i==0){
                    iHour = 12;
                    sAMPM = "AM";
                }
                else{
                    sAMPM = "AM";
                }

                String sMin;
                if(iMinute<10){
                    sMin = "0"+iMinute;
                }
                else{
                    sMin = String.valueOf(iMinute);
                }

                timeEdit.setText(iHour+" : "+sMin+" "+sAMPM);

            }
        };

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(
                        DateTimeActivity.this,
                        timeClick,
                        iHour,
                        iMinute,
                        false
                ).show();
            }
        });
    }

    private void datePickerMethod() {
        cal = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener datePickerClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                cal.set(Calendar.YEAR,i);
                cal.set(Calendar.MONTH,i1);
                cal.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateEdit.setText(dateFormat.format(cal.getTime()));

            }
        };

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        DateTimeActivity.this,
                        datePickerClick,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );

                //Disable Past Date
                //dialog.getDatePicker().setMinDate(System.currentTimeMillis());

                //Disable Future Date
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                dialog.show();
            }
        });
    }
}