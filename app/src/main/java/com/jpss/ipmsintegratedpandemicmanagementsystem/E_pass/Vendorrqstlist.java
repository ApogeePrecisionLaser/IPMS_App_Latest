package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerTouchlistner;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerViewAdapter;

import java.util.ArrayList;

public class Vendorrqstlist extends AppCompatActivity {

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
        setContentView(R.layout.activity_vendorrqstlist);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");
        String orgofcid = sharedPreferences.getString(ORGOFCID, "default value");
        ArrayList<String> keydetail = new ArrayList<>();
        dbTask.open();
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        String keyprsnid = kpidandlocation.split(",")[0];
        String orgnmid  = dbTask.getorgnmid(orgofcid);
        if(orgnmid!=null){
            orgofid = orgnmid.split(",")[0];
            String orgofcnm = orgnmid.split(",")[1];
        }




        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Order List");
        toolbar.inflateMenu(R.menu.refreshmenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rfrsh:
                        Callingserver cs=new Callingserver();
                        cs.execute();
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

        ArrayList<String> vendordbist = new ArrayList<>();
        dbTask.open();
        vendordbist = dbTask.getordrmgmtlist();
        if(vendordbist.size()!=0){
            for (int k = 0; k < vendordbist.size(); k++) {
                String val = vendordbist.get(k);
                String ordrimg = val.split(",")[0];
                String mobnoo = val.split(",")[1];
                String fnstts = val.split(",")[2];
                String odmid = val.split(",")[3];
                myValues.add(odmid+"_Person mob="+mobnoo+"_Order Status="+fnstts);
            }
        }else{
            Toast.makeText(this, "No any order found", Toast.LENGTH_SHORT).show();
        }
        dbTask.close();
        //Populate the ArrayList with your own values


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
                                String ordrid = finalval.split("_")[0];
                                Intent intent = new Intent(Vendorrqstlist.this,VendorActivity.class);
                                intent.putExtra("values",ordrid);
                                startActivity(intent);
                                Toast.makeText(Vendorrqstlist.this, finalval, Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
            );

    }

    private class Callingserver extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                genericModel.requestordrmngmntdata(orgofid);
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
            if (result.equalsIgnoreCase("updated"))
            {
                Toast.makeText(Vendorrqstlist.this, "Updated", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(Vendorrqstlist.this, "Refreshed", Toast.LENGTH_SHORT).show();
            dialog.cancel();


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Vendorrqstlist.this, "Refreshing Your list", "Please wait ....");
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
