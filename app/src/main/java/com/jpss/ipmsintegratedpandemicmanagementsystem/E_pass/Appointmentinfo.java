package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Appointmentinfo extends AppCompatActivity {

    TextView nm,loc,validto,validfrom,wrktyp;
    DatabaseOperation dbTask = new DatabaseOperation(this);
    String epsid,vldfrm,vldto;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentinfo);

        nm=findViewById(R.id.nm);
        loc=findViewById(R.id.loc);
        validfrom=findViewById(R.id.validfrm);
        validto=findViewById(R.id.vldto);
        wrktyp=findViewById(R.id.wrktyp);
        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Appointment Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String id = intent.getStringExtra("values");
        ArrayList<String> tlist = new ArrayList<String>();
        dbTask.open();
        tlist = dbTask.getappointmentlistinfo(id);
        if(tlist.size()!=0) {
            for (int k = 0; k < tlist.size(); k++) {
                String val = tlist.get(k);
                epsid = val.split(",")[0];
                String nmm = val.split(",")[1];
                String locc = val.split(",")[2];
                vldfrm = val.split(",")[3];
                vldto = val.split(",")[4];
                String wrktypp = val.split(",")[5];

                nm.setText(nmm);
                loc.setText(locc);
                validfrom.setText(vldfrm);
                validto.setText(vldto);
                wrktyp.setText(wrktypp);

            }
        }
    }

    public void confirm(View view){
        JSONObject json1 = new  JSONObject();
        JSONObject json2 = new JSONObject();
        GenericModel genericModel = new GenericModel(this);
        try {
            json1.put("e_pass_id",epsid);
            json1.put("epass_type","Appointment");
            json1.put("valid_from",vldfrm);
            json1.put("valid_to",vldto);
            json1.put("status","Confirmed");
           json2 = genericModel.requestEpass(json1);
          String  result =  json2.get("result").toString();
            if (result.equalsIgnoreCase("success") )
            {
                Toast.makeText(Appointmentinfo.this, "Updated", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(Appointmentinfo.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
