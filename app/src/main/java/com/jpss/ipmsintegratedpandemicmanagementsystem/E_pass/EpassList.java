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
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerTouchlistner;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Recyclerview.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class EpassList extends AppCompatActivity {

    DatabaseOperation dbTask = new DatabaseOperation(this);
    ArrayList<String> myValues = new ArrayList<String>();
    String finalval,frmlctn;
    Toolbar toolbar;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String ORGOFCID = "orgofcid";

    String kpid;
    GenericModel genericModel = new GenericModel(this);
    private static final String IMAGE_DIRECTORY = "/IPMS/Epass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epass_list);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");
        ArrayList<String> keydetail = new ArrayList<>();
        dbTask.open();
        String orgofcid = sharedPreferences.getString(ORGOFCID, "0");
        if(!orgofcid.equalsIgnoreCase("0")){
            dbTask.open();
            frmlctn =  dbTask.getlatlon(orgofcid);

        }
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        kpid = kpidandlocation.split(",")[0];

        ArrayList<String> epasslist = new ArrayList<>();
        epasslist = dbTask.getepasslist();
        if(epasslist.size()!=0){
            for (int k = 0; k < epasslist.size(); k++) {
                String val = epasslist.get(k);
                String epsid = val.split(",")[0];
                String dlvryboynm = val.split(",")[1];
                String mobno = val.split(",")[2];
                myValues.add(epsid+"_Delivery Boy="+dlvryboynm+"_Mob no="+mobno);
            }
        }else{
            Toast.makeText(this, "No any Epass found", Toast.LENGTH_SHORT).show();
        }



        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Epass List");
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
                            Intent intent = new Intent(EpassList.this,Epassinfo.class);
                            intent.putExtra("values",epsid);
                            startActivity(intent);
                            Toast.makeText(EpassList.this, finalval, Toast.LENGTH_SHORT).show();
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
               json2 = genericModel.requestepasslistdata(kpid);
                result =  json2.get("result").toString();
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
                try {
                    String fromdate = json2.getString("from_date");
                    String ePassID = json2.getString("e_passID");
                    String to_date = json2.getString("to_date");
                    String from_location = json2.getString("from_location");
                    String to_location = json2.getString("to_location");
                    String delivery_boy = json2.getString("delivery_boy");
                    String mobile_no = json2.getString("mobile_no");
                    String work_type = json2.getString("work_type");
                    String qr_base64 = json2.getString("qr_base64");
                    String ordrmgmtid = json2.getString("order_mgmt_id");
                    byte[] imageAsBytes = Base64.decode(qr_base64.getBytes(), Base64.DEFAULT);
                    Bitmap bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    String imgpath = saveImage(bitmap);

                    long resultt = dbTask.insertEpassDetails(ePassID, fromdate, to_date,delivery_boy,mobile_no,work_type,imgpath,ordrmgmtid,frmlctn,to_location);
                    if (resultt > 0) {
                        System.out.println("Data inserted");
                        finish();
                        startActivity(getIntent());

                    }else{
                        System.out.println("Data not inserted");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(EpassList.this, "No e-pass Availabel", Toast.LENGTH_SHORT).show();
            dialog.cancel();


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(EpassList.this, "Refreshing Your list", "Please wait ....");
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

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(EpassList.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);

            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
}
