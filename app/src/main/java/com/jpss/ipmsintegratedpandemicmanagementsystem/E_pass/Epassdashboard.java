package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;

public class Epassdashboard extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout re_Epassrqst,re_epassshow,re_vendor;
    CardView cd3;
    Toolbar toolbar;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String ORGOFCID = "orgofcid";
    private static final String TAG = "EPASSDASHBOARD.java";
    String orgofcid=null;
    DatabaseOperation dbTask = new DatabaseOperation(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epassdashboard);
        epasstype();
        violationtype();
        epassstatus();
        re_Epassrqst=findViewById(R.id.re_epass);
        re_epassshow=findViewById(R.id.re_quarantine);
        re_vendor=findViewById(R.id.re_document);
        re_Epassrqst.setOnClickListener(this);
        re_epassshow.setOnClickListener(this);
        re_vendor.setOnClickListener(this);
        cd3=findViewById(R.id.cd3);
        GenericModel genericModel = new GenericModel(this);
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");

        ArrayList<String> keydetail = new ArrayList<>();
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        String keyprsnid = kpidandlocation.split(",")[0];
        String result = genericModel.requestworktype(Integer.parseInt(keyprsnid));
        orgofcid = sharedPreferences.getString(ORGOFCID, "0");
        if(!orgofcid.equalsIgnoreCase("0")){
            re_vendor.setVisibility(View.VISIBLE);
            cd3.setVisibility(View.VISIBLE);
        }else{
            re_vendor.setVisibility(View.INVISIBLE);
            cd3.setVisibility(View.INVISIBLE);
        }
        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Epass Dashboard");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_epass:
                Intent intent=new Intent(Epassdashboard.this, Epass.class);
                startActivity(intent);
                break;

            case R.id.re_quarantine:
                Intent intent1=new Intent(Epassdashboard.this, EpassList.class);
                startActivity(intent1);
                break;

            case R.id.re_document:
                Intent intent2=new Intent(Epassdashboard.this, Vendorrqstlist.class);
                startActivity(intent2);
                break;

        }
    }

    private void epasstype(){
        dbTask.open();
        ArrayList<String> myValues = new ArrayList<String>();
        myValues.add("1,Duty,Abhi");
        myValues.add("2,Transition,Abhi");
        myValues.add("3,Consumer,Abhi");
        myValues.add("4,Hospital,Abhi");

        for(int i = 0; i < myValues.size(); i++){
            String val = myValues.get(i);
            String epasstypeid = val.split(",")[0];
            String epasstype = val.split(",")[1];
            String remark = val.split(",")[2];

            dbTask.insertEpasstype(epasstypeid,epasstype,remark);

        }
    }

    private void violationtype(){
        dbTask.open();
        ArrayList<String> myValues = new ArrayList<String>();
        myValues.add("1,Off Road");
        myValues.add("2,Network issue");

        for(int i = 0; i < myValues.size(); i++){
            String val = myValues.get(i);
            String violationtypeid = val.split(",")[0];
            String violationname = val.split(",")[1];

            dbTask.insertviolationtype(violationtypeid,violationname);

        }
    }

    private void epassstatus(){
        dbTask.open();
        ArrayList<String> myValues = new ArrayList<String>();
        myValues.add("1,On Road");
        myValues.add("2,In building");

        for(int i = 0; i < myValues.size(); i++){
            String val = myValues.get(i);
            String epassstatusid = val.split(",")[0];
            String epassstatus = val.split(",")[1];

            dbTask.insertepassstatus(epassstatusid,epassstatus);

        }
    }
}
