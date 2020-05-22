package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class QuaratineActivity extends AppCompatActivity implements HealthUpdateFragment.OnFragmentInteractionListener, ImageCaptureFragment.OnFragmentInteractionListener {
    private PendingIntent pendingIntent;
    public static final String ALARM_TYPE_REPEAT = "ALARM\n_TYPE_REPEAT";
    public static final String ALARM_DESCRIPTION = "ALARM_DESCRIPTION";
    public static final int RequestPermissionCode = 7;
    public static final String ALARM_TYPE = "ALARM_TYPE";
    AlarmManager alarmManager;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    public static final String Quarantineid = "qid";
    String path;
    private static final String IMAGE_DIRECTORY = "/userIPMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quaratine);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //   getSupportActionBar().setIcon(R.drawable.ic_local_phone_black_24dp);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               ? Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                paniccall paniccall=new paniccall();
                paniccall.execute();
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "recieved";
                        if (!task.isSuccessful()) {
                            msg = "notification failed";
                        }

                        Toast.makeText(QuaratineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        quarantineidrequest quarantineidrequest=new quarantineidrequest();
        quarantineidrequest.execute();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            {
                Toast.makeText(this, "you press a button!, success ", Toast.LENGTH_LONG).show();
                Intent act = new Intent(QuaratineActivity.this, qurantinedetails.class);
                startActivity(act);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(QuaratineActivity.this, new String[]
                {READ_PHONE_STATE,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        CALL_PHONE,
                        ACCESS_COARSE_LOCATION
                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean PhoneStatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean LocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean RExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean WExternalStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean PhoneCallPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean Location2Permission = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (PhoneStatePermission && LocationPermission && RExternalStoragePermission && WExternalStoragePermission && PhoneCallPermission && Location2Permission) {
                        Toast.makeText(QuaratineActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(QuaratineActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    //  Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public class paniccall extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(QuaratineActivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = new String();
            try {
                try {
                    result = prepaidDocModel.paniccall();
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

                if (result.contains("success")) {
                    dialog.dismiss();
                    Toast.makeText(QuaratineActivity.this,"Request send successfully for panic call----",Toast.LENGTH_LONG).show();
                }
                else{
                    if(result.contains("failure")){
                        dialog.dismiss();
                        Toast.makeText(QuaratineActivity.this,"Panic Call not send sucessfully Try again----",Toast.LENGTH_LONG).show();
                    }
                }


            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(QuaratineActivity.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }
    public class quarantineidrequest extends AsyncTask<String, String, JSONObject> {
        ProgressDialog dialog;

        @Override
        protected JSONObject doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(QuaratineActivity.this);
            final SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(QuaratineActivity.this);

            String num = sharedPreferences.getString(LOGINNUMBER, "default value");
            String kpid = sharedPreferences.getString(LOGINKPID, "default value");

            JSONObject result = new JSONObject();
            try {
                try {
                    result = prepaidDocModel.qurantinerequest(kpid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                dialog.dismiss();
                if(result.getString("Quarantine_data").contains("response_data")) {
                    getregistrationdata(result);

                }
                else{
                    if(result.getString("Quarantine_data").contains("[]")){
                        showalert();
                    }
                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(QuaratineActivity.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

    public long getregistrationdata(JSONObject jsonObject) throws JSONException {
        long result=0;
        List<Bean> item = new ArrayList<Bean>();

        DatabaseOperation dbTask = new DatabaseOperation(QuaratineActivity.this);
        dbTask.open();

        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("Quarantine_data");
            List<Bean> reason = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();

                bleBean.setQuarantime_id(jsonObject1.getString("quarantime_id"));
                bleBean.setReason_id(Integer.parseInt(jsonObject1.getString("reason_id")));
                bleBean.setReason_desc(jsonObject1.getString("reason_desc"));
                bleBean.setRemarks(jsonObject1.getString("remark"));
                bleBean.setLatitude(jsonObject1.getString("latitude"));
                bleBean.setLongitude(jsonObject1.getString("longitude"));
                bleBean.setDay(jsonObject1.getString("day"));
                bleBean.setDate_time(jsonObject1.getString("date_time"));
                bleBean.setCreated_date(jsonObject1.getString("created_date"));
                String id_img = jsonObject1.getString("data");
                /*==================  image convert      ===========*/
                byte[] imageAsBytes = Base64.decode(id_img.getBytes(), Base64.DEFAULT);
                Bitmap bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                path = saveImage(bitmap);
                bleBean.setImage_path(path);
                reason.add(bleBean);
                String value=dbTask.getqurantinedata();
                if(value.equals("")){
                    result = dbTask.insertquarantinedata(reason);

                }
            else
                {
                    Toast.makeText(QuaratineActivity.this, "Qurantine Data Exist", Toast.LENGTH_SHORT).show();
                }
                dbTask.close();

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return  result;

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        // have the object build the directory structure, if needed.

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
            MediaScannerConnection.scanFile(this,
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
    private void showalert() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuaratineActivity.this);
        alertDialogBuilder.setMessage("Congrats You Are Not Quarantine ......... ");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(QuaratineActivity.this, DrashBoardActivity.class);
                        startActivity(intent);


                    }
                });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

