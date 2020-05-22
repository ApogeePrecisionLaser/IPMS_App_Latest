package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

public class IpportActivity extends AppCompatActivity {

    public static final String IP = "Ipkey";
    public static final String PORT = "Portkey";
    EditText e1,e2;
    Button b1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipport);
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(IpportActivity.this);

        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        b1=findViewById(R.id.b1);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Ip/Port");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        String ipp = sharedPreferences.getString(IP, "default value");
        String pprt = sharedPreferences.getString(PORT, "default value");
        if(!pprt.equalsIgnoreCase("default value") || !ipp.equalsIgnoreCase("default value")){
            e1.setText(ipp);
            e2.setText(pprt);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = e1.getText().toString();
                String port = e2.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(IP, ip);
                editor.putString(PORT, port);
                editor.commit();
                Toast.makeText(IpportActivity.this, ip+"_"+port, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(IpportActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
