package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerTouchlistner;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncomingAppointment extends AppCompatActivity {
    String numm="";
    String keyprsnid="";
    DatabaseOperation dbTask = new DatabaseOperation(this);
    ArrayList<String> myValues = new ArrayList<String>();
    String finalval;
    Toolbar toolbar;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String ORGOFCID = "orgofcid";
    String orgofid;
    GenericModel genericModel = new GenericModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_appointment);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        numm = sharedPreferences.getString(LOGINNUMBER, "default value");
        String orgofcid = sharedPreferences.getString(ORGOFCID, "default value");
         dbTask.open();
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        keyprsnid = kpidandlocation.split(",")[0];

        ArrayList<String> epasslist = new ArrayList<>();
        epasslist = dbTask.getappointmentlist();
        if(epasslist.size()!=0){
            for (int k = 0; k < epasslist.size(); k++) {
                String val = epasslist.get(k);
                String epsid = val.split(",")[0];
                String dlvryboynm = val.split(",")[1];
                String location = val.split(",")[2];
                myValues.add(epsid+"_Person="+dlvryboynm+"_Location="+location);
            }
        }else{
            Toast.makeText(this, "No any Appointment found", Toast.LENGTH_SHORT).show();
        }


        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Incoming Appointment");
        toolbar.inflateMenu(R.menu.refreshmenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rfrsh:
                        Callingserver callingserver = new Callingserver();
                        callingserver.execute();
                        return true;
                    default:
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(myValues);
        final RecyclerView myView =  (RecyclerView)findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        myView.addOnItemTouchListener(
                new RecyclerTouchlistner(getApplicationContext(), new RecyclerTouchlistner.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // get position
                        int pos = position;

                        // check if item still exists
                        if(pos != RecyclerView.NO_POSITION){
                            finalval = myValues.get(pos);
                            String epsid = finalval.split("_")[0];
                            Intent intent = new Intent(IncomingAppointment.this,Appointmentinfo.class);
                            intent.putExtra("values",epsid);
                            startActivity(intent);
                            Toast.makeText(IncomingAppointment.this, finalval, Toast.LENGTH_SHORT).show();
                        }

                    }
                })
        );

    }
    JSONObject json2 = new JSONObject();
    private class Callingserver extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                JSONObject json1 = new  JSONObject();
                json1.put("epass_type","Appointment");
                json1.put("mobile_no",numm);
                json1.put("person_id",keyprsnid);
                json2 = genericModel.requestAppointmentlist(json1);
                result =  json2.get("result").toString();
                finish();
                startActivity(getIntent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            if (result.equalsIgnoreCase("success"))
            {
                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = json2.getJSONArray("data");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        String e_pass_id = jsonObject1.getString("e_pass_id");
                        String loc_id = jsonObject1.getString("loc_id");
                        String key_person_name = jsonObject1.getString("key_person_name");
                        String valid_from = jsonObject1.getString("valid_from");
                        String valid_to = jsonObject1.getString("valid_to");
                        String work_type = jsonObject1.getString("work_type");
                        String status = jsonObject1.getString("status");
                        dbTask.insertincomingapntmnt(e_pass_id,loc_id,key_person_name,valid_from,valid_to,work_type,status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if(result.equalsIgnoreCase("error"))
                Toast.makeText(IncomingAppointment.this, "No Appointment", Toast.LENGTH_SHORT).show();
            dialog.cancel();


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(IncomingAppointment.this, "Refreshing Your list", "Please wait ....");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show();
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
