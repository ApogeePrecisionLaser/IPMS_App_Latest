package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.services.NetworkChangeReceiver;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants.CONNECTIVITY_ACTION;

public class NumberRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnProceed,btnBack;
    ProgressDialog dialog;

    EditText enterMobile;
    String enterNumber,idType,cityname;
    DatabaseOperation databaseOperation=new DatabaseOperation(this);
    Toolbar toolbar;
    String num;
    List mobileno =new ArrayList();
    public static final String Registernumber = "register";
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
        setContentView(R.layout.activity_numberregistration);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Number Registration");
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

        if (NetworkUtil.getConnectivityStatus(NumberRegistrationActivity.this) > 0 ) System.out.println("Connect");
        else System.out.println("No connection");
        btnProceed=findViewById(R.id.proceeds);
        enterMobile=findViewById(R.id.edt_mobile);
        btnProceed.setOnClickListener(this);
        databaseOperation.open();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.proceeds:
                if(validationSuccess()){
                    webService();
                }

                break;


        }
    }
    private Boolean validationSuccess() {
        if(enterMobile.getText().toString().equalsIgnoreCase("")){
            enterMobile.setError("Cannot be empty");
            return false;
        }

        return true;
    }
    private void webService() {
        enterNumber=enterMobile.getText().toString();
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(NumberRegistrationActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Registernumber, enterNumber);
        editor.commit();
        database();

    }
    public class Callingregistration extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(NumberRegistrationActivity.this);
            // prepaidDocModel.setDbTask(dbTask);
            String result = new String();
            try {
                try {
                    result = prepaidDocModel.numberregister(enterNumber);
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
            long res=0,res1=0;
            Bean bean=new Bean();
            if (result.equals("success")) {
                Otpverify otpverify=new Otpverify();
                otpverify.execute();

            } else if (result.equals("failure")) {
                Intent intent = new Intent(NumberRegistrationActivity.this, OrgnisationTypeActivity.class);
                intent.putExtra("mobile_no", enterNumber);
                startActivity(intent);
               /* Citydata citydata=new Citydata();
                citydata.execute();*/
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


    private void showalert() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NumberRegistrationActivity.this);
        alertDialogBuilder.setMessage("Do you want to proceed to login.. ");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(NumberRegistrationActivity.this, Otpactivity.class);
                        intent.putExtra("mobile_no", enterNumber);
                        startActivity(intent);


                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void database(){
        DatabaseOperation databaseOperation=new DatabaseOperation(NumberRegistrationActivity.this);
        databaseOperation.open();
        mobileno = databaseOperation.getregisteredmobileno();
        if(mobileno.contains(enterNumber)){
            Intent intent =new Intent(NumberRegistrationActivity.this,Login.class);
            startActivity(intent);
        }else{
            Callingregistration callingservcie = new Callingregistration();
            callingservcie.execute();

        }

    }
    public class Citydata extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(NumberRegistrationActivity.this);


            String result = new String();
            try {
                try {
                    result = prepaidDocModel.getCitydata("both");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            try {
//{"result":"success","idlist":[{"id":1,"name":"adhaar"},{"id":2,"name":"pan"}],"citylist":[{"id":2,"name":"à¤à¤¬à¤²à¤ªà¥à¤°"},{"id":3,"name":"vns"},{"id":6,"name":"jb"},{"id":7,"name":"jabalpur"}]}
                if (result.contains("success")) {
                    dialog.dismiss();
                    long res=0,res1=0;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("result").contains("success")) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("idlist");
                            List<Bean> id = new ArrayList<Bean>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                Bean bleBean = new Bean();
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                bleBean.setId_type_id(jsonObject1.getString("id"));
                                bleBean.setId_type(jsonObject1.getString("name"));
                                id.add(bleBean);
                            }
                            JSONArray jsonArray2 = jsonObject.getJSONArray("citylist");
                            List<Bean> city_name = new ArrayList<Bean>();

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                Bean bean = new Bean();
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                bean.setCityNane(jsonObject2.getString("name"));
                                bean.setCityType_id(jsonObject2.getString("id"));
                                city_name.add(bean);
                            }
                            res=  databaseOperation.insertuserid(id);
                            res1=   databaseOperation.insertlocation(city_name);
                            /*Intent intent = new Intent(NumberRegistrationActivity.this, Registration.class);
                            intent.putExtra("mobile_no", enterNumber);
                            startActivity(intent);*/
                        }

                    }catch (Exception e){

                    }

                }
                else{
                    if(result.contains("failure")){
                        dialog.dismiss();
                        Toast.makeText(NumberRegistrationActivity.this,"-- No City data-- ",Toast.LENGTH_LONG).show();
                    }
                }


            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(NumberRegistrationActivity.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }
    public class Otpverify extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            GenericModel prepaidDocModel = new GenericModel(NumberRegistrationActivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    result = prepaidDocModel.getotp(enterNumber);
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
                    showalert();

                } else if (result.contains("false")) {
                    Intent intent = new Intent(NumberRegistrationActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(NumberRegistrationActivity.this, "", "Proccessing....Please wait");
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


}
