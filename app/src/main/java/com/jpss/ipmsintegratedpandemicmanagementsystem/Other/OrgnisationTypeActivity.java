package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.services.NetworkChangeReceiver;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants.CONNECTIVITY_ACTION;

public class OrgnisationTypeActivity extends AppCompatActivity {
    DatabaseOperation dbTask=new DatabaseOperation(this);
    EditText enterNo;
    Spinner typeSpinner;
    Button proceed,proceeds;
    String orgMobile,org_type="",moblieno;
    ProgressDialog dialog;
    List<String>orgType=new ArrayList<>();
    Toolbar toolbar;
    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgnisationtype);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("organisationType");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();

        if (NetworkUtil.getConnectivityStatus(OrgnisationTypeActivity.this) > 0 ) System.out.println("Connect");
        else System.out.println("No connection");
        enterNo=findViewById(R.id.edt_orgmobile);
        typeSpinner=findViewById(R.id.orgTypeSpinner);
        proceed=findViewById(R.id.btnproceeds);
        proceeds=findViewById(R.id.btnproceedss);
        dbTask.open();
        getOrgtype();
        Intent intent = getIntent();
        moblieno = intent.getStringExtra("mobile_no");
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("select"))
                    org_type = "";
                else
                    org_type = item;
                if(item.equalsIgnoreCase("Public")){
                    proceeds.setVisibility(View.VISIBLE);
                    enterNo.setVisibility(View.INVISIBLE);
                    proceed.setVisibility(View.INVISIBLE);
                }
                else if(item.equalsIgnoreCase("Private")) {
                    enterNo.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.VISIBLE);
                    proceeds.setVisibility(View.INVISIBLE);

                }else if(item.equalsIgnoreCase("Government")){
                    enterNo.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.VISIBLE);
                    proceeds.setVisibility(View.INVISIBLE);
                }else {
                    enterNo.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.VISIBLE);
                    proceeds.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        proceeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgnisationTypeActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getData();
                orgMobile=enterNo.getText().toString().trim();
                Callingregistration callingservcie = new Callingregistration();
                callingservcie.execute();
            }
        });
    }

    private void getOrgtype() {
        DatabaseOperation dbTask=new DatabaseOperation(this);
        dbTask.open();
        orgType.add("select");
        List<String> orgType=dbTask.getorgname();
        dbTask.close();

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, orgType);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(arrayAdapter1);
    }

    public class Callingregistration extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(OrgnisationTypeActivity.this);
            // prepaidDocModel.setDbTask(dbTask);
            String result = new String();
            try {
                try {
                    result = prepaidDocModel.sendOrgtype(orgMobile);
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

            if (result.equals("success")) {
                getmobile();

            } else if (result.equals("failure")) {
                Toast.makeText(OrgnisationTypeActivity.this, "No is not linked with any Orgnisation office...", Toast.LENGTH_SHORT).show();

            }

        }





        @Override
        protected void onPreExecute() {

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


    public void getmobile(){
        Otpverify otpverify=new Otpverify();
        otpverify.execute();
    }
    public class Otpverify extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //publishProgress("processing...");
            //  String address = dbTask.getServerIp();
            //   String server_ip=address.split("_")[0];
            //  String port=address.split("_")[1];

            GenericModel prepaidDocModel = new GenericModel(OrgnisationTypeActivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    result = prepaidDocModel.getotp(orgMobile);
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
            dialog.dismiss();
            try {
                if (result.contains("true")) {
                    Intent intent = new Intent(OrgnisationTypeActivity.this, OrgOtpActivity.class);
                    intent.putExtra("mobile_no",orgMobile);
                    // intent.putExtra("id",ids);
                    startActivity(intent);
                    finish();

                } else if (result.contains("false")) {
                    Intent intent = new Intent(OrgnisationTypeActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(OrgnisationTypeActivity.this, "", "Proccessing....Please wait");
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
    public JSONObject getData() {
        JSONObject jsonObject = new JSONObject();
        orgMobile=enterNo.getText().toString().trim();
        try {
            jsonObject.put("org_mobilenumber", orgMobile);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    
}
