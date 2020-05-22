package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;

public class Organisation extends AppCompatActivity {
    Spinner spinner;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        spinner = findViewById(R.id.spinner);
        Intent i=getIntent();
        value=i.getStringExtra("org");
        ArrayList<String> data=new ArrayList();
        data.add("------select------");
        data.add(value);
        data.add("Private");
        data.add("Government");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Organisation.this,android.R.layout.simple_spinner_item, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        /* ------------------------selection from spinner -----------------------  */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getSelectedItem().toString();
                if(!item.contains("------select------")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Organisation.this);
                    builder.setMessage("Are your Family registered  ??????")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent=new Intent(Organisation.this,Otpactivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // some code if you want
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
