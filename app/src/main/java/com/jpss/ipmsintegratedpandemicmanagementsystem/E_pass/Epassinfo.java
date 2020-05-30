package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Epassinfo extends AppCompatActivity {
    ImageView imageView;
    TextView exprydttxt,validfromtxt,validtotxt,worktyptxt,prsnnm,prsnnum,dtstts;
    Toolbar toolbar;
    String epsid,ordrmgmtid,vldfrm,vldto,epasstypeid,keypid;
    boolean result = false;
    boolean result2 = false;
    boolean result3 = false;
    LinearLayout dtsttslay;
    Button dlvrbtn;

    DatabaseOperation dbTask = new DatabaseOperation(this);
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epassinfo);
        dlvrbtn=findViewById(R.id.dlvrbtn);
        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("EPass Info");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        imageView = findViewById(R.id.imgv);
        dtsttslay = findViewById(R.id.dtsttslay);
        dtstts = findViewById(R.id.dtsts);
        exprydttxt = findViewById(R.id.expdt);
        validfromtxt = findViewById(R.id.validfromdt);
        validtotxt = findViewById(R.id.validtodt);
        worktyptxt = findViewById(R.id.worktype);
        prsnnm = findViewById(R.id.prsnnnnn);
        prsnnum = findViewById(R.id.prsnnnnnnum);
        Intent intent = getIntent();
        String id = intent.getStringExtra("values");
        ArrayList<String> tlist = new ArrayList<String>();
        dbTask.open();
            tlist = dbTask.getEpassInfolist(id);
            if(tlist.size()!=0){
                for (int k = 0; k < tlist.size(); k++) {
                    String val = tlist.get(k);
                    epsid = val.split("_")[0];
                    String validfrom = val.split("_")[1];
                    String validto = val.split("_")[2];
                    String image = val.split("_")[3];
                    String worktype = val.split("_")[4];
                    String dlvryprsn = val.split("_")[5];
                    String mobno = val.split("_")[6];
                    ordrmgmtid = val.split("_")[7];
                    vldfrm = val.split("_")[8];
                    vldto = val.split("_")[9];
                    epasstypeid = val.split("_")[10];
                    keypid = val.split("_")[11];

                    if(epasstypeid!=null){
                      String epasstype =  dbTask.getepasstype(epasstypeid);
                      if(epasstype.equalsIgnoreCase("Duty")){
                          dlvrbtn.setVisibility(View.INVISIBLE);
                      }else if(epasstype.equalsIgnoreCase("Transition")){

                      }else if(epasstype.equalsIgnoreCase("Consumer")){
                          dlvrbtn.setVisibility(View.VISIBLE);
                      }else if(epasstype.equalsIgnoreCase("Hospital")){

                      }else{
                          dlvrbtn.setVisibility(View.VISIBLE);
                      }
                    }


                    Date dateone = null;
                    Date datetwo = null;
                    Date datethree = null;
                    try {
                        dateone = sdf.parse(currentDateandTime);
                        datetwo = sdf.parse(validfrom);
                        datethree = sdf.parse(validto);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    result = dateone.after(datetwo);
                    result2 = dateone.before(datethree);
                    result3 = dateone.after(datethree);
                   if(result == true && result2 == true){
                       dtstts.setText("Live");
                       int color = ContextCompat.getColor(this, R.color.colorgreen);
                       dtsttslay.setBackgroundColor(color);
                   }else if(result3 == true){
                       dtstts.setText("Expired");
                       int color = ContextCompat.getColor(this, R.color.colorred);
                       dtsttslay.setBackgroundColor(color);
                   }

                    validfromtxt.setText(validfrom);
                    validtotxt.setText(validto);
                    exprydttxt.setText(validto);
                    worktyptxt.setText(worktype);
                    prsnnm.setText(dlvryprsn);
                    prsnnum.setText(mobno);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(image));
                }
        }else{
                Toast.makeText(this, "No any E_pass Detected!", Toast.LENGTH_SHORT).show();
        }




    }

    public void getroute(View view){
        Intent intent = new Intent(Epassinfo.this,RouteActivity.class);
        if(!vldfrm.equalsIgnoreCase("0.0")  && !vldto.equalsIgnoreCase("0.0")){
            intent.putExtra("FromLoc",vldfrm);
            intent.putExtra("ToLoc",vldto);
            intent.putExtra("epassid",epsid);
            intent.putExtra("kpid",keypid);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Location is Missing", Toast.LENGTH_SHORT).show();
        }


    }

    public void personaldetails(View view){
        Intent intent = new Intent(Epassinfo.this,PersonalDetail.class);
        startActivity(intent);
    }

    public void iddetails(View view){
        Intent intent = new Intent(Epassinfo.this,Iddetails.class);
        startActivity(intent);
    }

    public void dlvrd(View view){
        Callingserver cs=new Callingserver();
        cs.execute();
    }
    JSONObject json2 = new JSONObject();
    private class Callingserver extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                JSONObject jsonObject = new JSONObject();

                GenericModel genericModel = new GenericModel(Epassinfo.this);
                jsonObject.put("order_mgmt_id",ordrmgmtid);
                jsonObject.put("epass_id",epsid);
                jsonObject.put("order_delivery","yes");
                json2 =  genericModel.orderdlvrd(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            if (json2.has("success"))
            {
                Toast.makeText(Epassinfo.this, "Delievery Done", Toast.LENGTH_SHORT).show();

            } else if(json2.has("error")){
                Toast.makeText(Epassinfo.this, "Not updated", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Epassinfo.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Epassinfo.this, "Updating your status", "Please wait ....");
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
