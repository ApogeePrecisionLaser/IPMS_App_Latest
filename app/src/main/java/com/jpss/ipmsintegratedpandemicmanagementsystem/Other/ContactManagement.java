package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContactManagement extends AppCompatActivity {
     ImageView date,time;
     DatePickerDialog picker;
      String dob;
      String value,pointid;
      EditText date_ofbirth,currenttime,mobileno,name;
      Spinner pointmeet;
      ArrayList<String> pointdata=new ArrayList<>();

     DatabaseOperation databaseOperation=new DatabaseOperation(this);
    ProgressDialog dialog;
    Button submit;
    String currentTime;
    String latitude,longitude;
    public static final String LOGINKPID = "loginid";
    SharedPreferences sharedPreferences;
    String kpid ,number,names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_management);
        databaseOperation.open();
        date=findViewById(R.id.date);
        time=findViewById(R.id.timing);
        date_ofbirth=findViewById(R.id.dateedittext);
        currenttime=findViewById(R.id.timeedittext);
        pointmeet=findViewById(R.id.contactplace);
        submit=findViewById(R.id.Submit);
        mobileno=findViewById(R.id.mobbile);
        name=findViewById(R.id.personname);
        getdetails();
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ContactManagement.this);

         kpid = sharedPreferences.getString(LOGINKPID, "default value");
        GPSTrack gpsTrack=new GPSTrack(ContactManagement.this);
        Double lattitude = gpsTrack.getLatitude();
        latitude = String.valueOf(lattitude);
        Double longitude1 = gpsTrack.getLongitude();
        longitude = String.valueOf(longitude1);
        connection();


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ContactManagement.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_ofbirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dob = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date_ofbirth.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                 currentTime = sdf.format(new Date());
                currenttime.setVisibility(View.VISIBLE);
                currenttime.setText(currentTime);

            }
        });
        pointmeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item != "---select---") {
                    value = item;
                    databaseOperation.open();
                    pointid = databaseOperation.getcontactpointid(value);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationSuccess()) {

                    Contactdetails contactdetails = new Contactdetails();
                    contactdetails.execute();
                }
            }
        });
    }
    private void getdetails(){

        databaseOperation.open();


        pointdata=databaseOperation.getpointcontactdata();
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pointdata);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointmeet.setAdapter(arrayAdapter2);


    }
    public class Contactdetails extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            GenericModel prepaidDocModel = new GenericModel(ContactManagement.this);
            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    JSONObject jsonObject=getdata();
                    result = prepaidDocModel.contactdetails(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            try {
                     dialog.dismiss();
                if (result.contains("success")) {
                    Toast.makeText(ContactManagement.this, "Data send successfully", Toast.LENGTH_LONG).show();
                  Intent intent=new Intent(ContactManagement.this,ContactManagement.class);
                  startActivity(intent);
                  finish();
                }
                else{
                    Toast.makeText(ContactManagement.this, "Data NOT Send successfully", Toast.LENGTH_LONG).show();

                }



            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ContactManagement.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //firstBar.
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

    }
    private  JSONObject getdata() throws JSONException {
        JSONObject jsonObject = new JSONObject();

                  number = mobileno.getText().toString();
            names = name.getText().toString();
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
            jsonObject.put("pointcontactid", pointid);
            jsonObject.put("mobileno", number);
            jsonObject.put("name", names);
            jsonObject.put("datetime", dob + currentTime);
            jsonObject.put("key_person_id", kpid);
            jsonObject.put("remark", "okkkk");

            List<Bean> data = new ArrayList<Bean>();
            Bean bleBean = new Bean();
            bleBean.setLatitude(latitude);
            bleBean.setLongitude(longitude);
            bleBean.setCreated_date(dob + currentTime);
            bleBean.setPointid(pointid);
            bleBean.setMobileno(number);
            bleBean.setName(names);
            bleBean.setKeypersonid(kpid);
            data.add(bleBean);

            databaseOperation.insertcontactdetails(data);

        return jsonObject;

    }

    private void connection() {
        if (checkConnection()) {

        }else {
            Toast.makeText(this, "Check your enternet connection", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReciever.isConnected();
        if (isConnected) {
            return true;
        } else {
            return false;

        }
    }
    private Boolean validationSuccess(){


         if(mobileno.getText().toString().equalsIgnoreCase("")){
            mobileno.setError("Cannot be empty");
            return false;
        }

         if(name.getText().toString().equalsIgnoreCase("")){
            name.setError("Cannot be empty");
            return false;
        }
         if(currenttime.getText().toString().equalsIgnoreCase("")){
            currenttime.setError("Cannot be empty");
            return false;
        }
         if(date_ofbirth.getText().toString().equalsIgnoreCase("")){
            date_ofbirth.setError("Cannot be empty");
            return false;
        }

        return true;
    }

}
