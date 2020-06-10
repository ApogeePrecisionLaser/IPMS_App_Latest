package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {
    String keyprsnid="";
    DatabaseOperation dbTask = new DatabaseOperation(this);
    EditText nm,num,frmdt,frmtm,todt,totm;
    DatePickerDialog picker;
    Toolbar toolbar;
    String todat,frmdat,frmtime,totim;
    Button fnvrfy;
    String id="";
    TextView txt2;
    CardView frmcd,tocd,frmcdtm,tocdtm;
    public static final String LOGINNUMBER = "loginnumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        frmdt = findViewById(R.id.fromdate);
        todt = findViewById(R.id.todate);
        frmtm = findViewById(R.id.fromtime);
        totm = findViewById(R.id.totime);
        num = findViewById(R.id.mob);
        fnvrfy = findViewById(R.id.fnvrfy);
        toolbar =  findViewById(R.id.tool);
        txt2 =  findViewById(R.id.txt2);
        frmcd =  findViewById(R.id.frmcd);
        tocd =  findViewById(R.id.tocd);
        frmcdtm =  findViewById(R.id.frmcdtm);
        tocdtm =  findViewById(R.id.tocdtm);
        toolbar.setTitle("Create your Appointment");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");
        dbTask.open();
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        keyprsnid = kpidandlocation.split(",")[0];

        frmdt.setVisibility(View.INVISIBLE);
        frmtm.setVisibility(View.INVISIBLE);
        todt.setVisibility(View.INVISIBLE);
        totm.setVisibility(View.INVISIBLE);
        fnvrfy.setVisibility(View.INVISIBLE);
        txt2.setVisibility(View.INVISIBLE);
        frmcd.setVisibility(View.INVISIBLE);
        tocd.setVisibility(View.INVISIBLE);
        frmcdtm.setVisibility(View.INVISIBLE);
        tocdtm.setVisibility(View.INVISIBLE);

        frmdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                frmdt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                frmdat = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                frmdt.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        todt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                todt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                todat = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                todt.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        frmtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int  mMinute = c.get(Calendar.MINUTE);
                final int  AM_pm = c.get(Calendar.AM_PM);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                frmtime = String.valueOf(hourOfDay+":"+minute+":"+"00");
                                frmtm.setText(frmtime);
                                frmtm.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });



        totm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int  mMinute = c.get(Calendar.MINUTE);
                final int  AM_pm = c.get(Calendar.AM_PM);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                totim = String.valueOf(hourOfDay+":"+minute+":"+"00");
                                totm.setText(totim);
                                totm.setTextColor(Color.parseColor("#4CAF50"));

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }
    JSONObject json2 = new JSONObject();
    public void check(View view){
        JSONObject json1 = new  JSONObject();
        String result="";
        GenericModel genericModel = new GenericModel(this);
        String nm = num.getText().toString().trim();
        try {
            json1.put("epass_type","Appointment");
            json1.put("mobile_no",nm);
         json2 =   genericModel.checkKeyPersonExist(json1);
         result = json2.get("result").toString();
         if(result.equalsIgnoreCase("success")){
             id = json2.getString("id");
             if(id.equalsIgnoreCase(keyprsnid)){
                 Toast.makeText(this, "This is You", Toast.LENGTH_SHORT).show();
             }else{
                 frmdt.setVisibility(View.VISIBLE);
                 frmtm.setVisibility(View.VISIBLE);
                 todt.setVisibility(View.VISIBLE);
                 totm.setVisibility(View.VISIBLE);
                 fnvrfy.setVisibility(View.VISIBLE);
                 txt2.setVisibility(View.VISIBLE);
                 frmcd.setVisibility(View.VISIBLE);
                 tocd.setVisibility(View.VISIBLE);
                 frmcdtm.setVisibility(View.VISIBLE);
                 tocdtm.setVisibility(View.VISIBLE);
             }
         }else {
             Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
         }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
