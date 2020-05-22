package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.Calendar;

public class AddFamilyActivity extends AppCompatActivity {
    Spinner familySpinner;
    EditText family_userName,family_address,family_no,family_dob,family_email;
    String family_member,familyDob,familyAddres,familyName,familyMobile,familyEmail;
    DatePickerDialog picker;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfamily);
        familySpinner=findViewById(R.id.familySpinner);
        family_userName=findViewById(R.id.edtfamily_name);
        family_address=findViewById(R.id.edtfamily_address);
        family_no=findViewById(R.id.edtfamily_mobile);
        family_dob=findViewById(R.id.edtfamily_dob);
        family_email=findViewById(R.id.edtfamily_email);
        //send=findViewById(R.id.btnfamilysend);
        Intent intent = getIntent();
        familyAddres = intent.getStringExtra("address");
        if(familyAddres!=null){
            family_address.setText(familyAddres);
            family_address.setEnabled(false);


        }else {
            family_address.setEnabled(true);

        }

        family_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddFamilyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                family_dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                familyDob = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                family_dob.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Family Member"))
                    family_member = "";
                else
                    family_member = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationSuccess()) {
                    sendData();
                    Intent intent=new Intent(AddFamilyActivity.this,Registration.class);
                    intent.putExtra("family_member",family_member);
                    intent.putExtra("familyDob",familyDob);
                    intent.putExtra("familyAddres",familyAddres);
                    intent.putExtra("familyName",familyName);
                    intent.putExtra("familyMobile",familyMobile);
                    intent.putExtra("familyEmail",familyEmail);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(AddFamilyActivity.this, "Please fill above field", Toast.LENGTH_SHORT).show();
                }

            }
        });*/
    }
    private Boolean validationSuccess(){
        if(family_userName.getText().toString().equalsIgnoreCase("")){
            family_userName.setError("Cannot be empty");
            return false;
        }

        else if(family_address.getText().toString().equalsIgnoreCase("")){
            family_address.setError("Cannot be empty");
            return false;
        }

        else if(!family_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
            family_email.setError("Invalid Email Address");
            return false;
        }
        else if(family_no.getText().toString().equalsIgnoreCase("")){
            family_no.setError("Cannot be empty");
            return false;
        }
        else if(family_dob.getText().toString().equalsIgnoreCase("")){
            family_dob.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private void sendData() {
        familyAddres=family_address.getText().toString();
        familyName=family_userName.getText().toString();
        familyMobile=family_no.getText().toString();
        familyEmail=family_email.getText().toString();
    }
}
