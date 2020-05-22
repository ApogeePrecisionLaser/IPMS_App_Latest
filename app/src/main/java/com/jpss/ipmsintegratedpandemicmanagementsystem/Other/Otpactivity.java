package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

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

public class Otpactivity extends AppCompatActivity {
    EditText editText_otp;
    Button button_submit, btnLinkToResendOtp;
    String otp = "";
    String s,kpid;
    String mobileno, password_registered;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCE = "Otp";
    String id,loginmobile;
    List<Bean>details= new ArrayList<Bean>();
    ProgressDialog dialog;
    String path,paths,orgtypNam,famorgtypNam;
    public static final String ORGTYPENAME = "orgnm";
    public static final String LOGINKPID = "loginid";
    private static final String IMAGE_DIRECTORY = "/userIPMS";
    DatabaseOperation dbtask= new DatabaseOperation(this);
    List<Bean> designation = new ArrayList<Bean>();
    long resultts;
    Toolbar toolbar;
    String desigId,famdesigId;
    List<String>kpersonid=new ArrayList<String>();
    List<String> desigid=new ArrayList<String>();
    List<String> city_id=new ArrayList<String>();
    List<Bean> kplist = new ArrayList<Bean>();
    String famorglat,famorglong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Otp");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        dbtask.open();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        mobileno = intent.getStringExtra("mobile_no");
        password_registered = intent.getStringExtra("password");
        loginmobile=intent.getStringExtra("loginmobile");
        //id=intent.getStringExtra("id");
         //-------------------------------------------------------------------------------------------

        editText_otp = findViewById(R.id.editText_otp);
        button_submit = findViewById(R.id.button_submit);
        btnLinkToResendOtp = findViewById(R.id.btnLinkToResendOtp);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mobileno!=null){
                        otp = editText_otp.getText().toString();
                        s = mobileno + "_" + otp;
                    }else {
                        otp = editText_otp.getText().toString();
                        s=loginmobile + "_" +otp;
                    }


                    sharedpreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    if (mobileno!=null){
                        editor.putString("number", mobileno);

                    }else {
                        editor.putString("number", loginmobile);

                    }
                    editor.putString("otp", otp);
                    editor.commit();
                    Setotp setotp = new Setotp();
                    setotp.execute();
                } catch (Exception e) {
                }
            }
        });

    }

    public class Setotp extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(Otpactivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = new String();
            try {
                try {
                    result = prepaidDocModel.verifyotp();
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
                dialog.cancel();
                String orglat="0.0";
                String orglong="0.0";
                long results = 0, res = 0,res1=0,resultss=0,ress=0,res11=0;
                long resultt=0;
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(result);
                    dbtask.open();

                    //{"result":"success","kplist":[{"id":140,"name":"testing","officeid":19,"city_id":2,"address":"abc","mobile":"7248858155","landline":"","email":"sonaya@hmail.com","fathername":"test","dob":"23\/09\/98","lat":"23.136680686453023","long":"80.07686097343752","bloodgroup":"A+","emergencyname":"abc","emergencynumber":"123456789","idnumber":"1223445567896422","typeid":1,"desigid":19}],"OrgOffice":{"id":19,"officename":"MediNeed","mobilenumber":"8287874165","cityid":7,"address":"Wright Town","orgnameid":12},"designation":[{"desigid":18,"designame":"Owner"},{"desigid":15,"designame":"Manager"},{"desigid":19,"designame":"Sales Officer"},{"desigid":20,"designame":"Delivery Boy"}],"orgname":[{"id":12,"orgname":"MediNeed","orgtype":10}],"orgtype":[{"id":1,"orgtypename":"Private"}]}
// Orgtype,Orgname,Designation,OrgOffice and kplist for Family with name of familyorgtype,familyorgname,familydesignation,familyOrgOffice,familykplist
 //{"result":"success","kplist":[{"id":236,"name":"Vinay Kumar","officeid":49,"city_id":7,"address":"Noida","mobile":"7905635621","email":"s@g.com","fathername":"father","dob":"2020-5-1","lat":"0.0","long":"0.0","bloodgroup":"O-","emergencyname":"emergency","emergencynumber":"5757679998","gender":"M","idnumber":"12345","typeid":1,"desigid":15,"relation":"self","family_office":50,"family_designation":"21"}],"OrgOffice":{"id":49,"officename":"Family7905635621","mobilenumber":"7905635621","cityid":2,"address":"Noida","orgnameid":18},"designation":[{"desigid":21,"designame":"Family Head"},{"desigid":22,"designame":"Family Member"}],"orgname":[{"id":18,"orgname":"Family","orgtype":12}],"orgtype":[{"id":4,"orgtypename":"Public"}],"familyorgtype":[{"id":4,"orgtypename":"Public"}],"familyorgname":[{"id":18,"orgname":"Family","orgtype":12}],"familydesignation":[{"desigid":21,"designame":"Family Head"},{"desigid":22,"designame":"Family Member"},{"desigid":21,"designame":"Family Head"},{"desigid":22,"designame":"Family Member"}],"familyOrgOffice":{"id":50,"officename":"Family7905635621","mobilenumber":"7905635621","cityid":7,"address":"Noida","orgnameid":18},"familykplist":[]}
                    if (jsonObject.getString("result").contains("success")) {
                         try {
                             JSONObject jsonObject1 = jsonObject.getJSONObject("OrgOffice");

                             String id=jsonObject1.getString("id");
                             String officeName=jsonObject1.getString("officename");
                             String mobilenumber=jsonObject1.getString("mobilenumber");
                             String cityid =jsonObject1.getString("cityid");
                             String address=jsonObject1.getString("address");
                             String orgnmid=jsonObject1.getString("orgnameid");
                             try{
                                 orglat=jsonObject1.getString("orglat");
                                 orglong=jsonObject1.getString("orglong");
                             }catch(JSONException e){
                                 e.printStackTrace();
                             }
                              resultt = dbtask.insertOrgOfficeDetails(id, officeName, mobilenumber,cityid,address,orgnmid,orglat,orglong,"office");

                         }catch (JSONException e){
                             e.printStackTrace();
                         }

                        try {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("designation");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                Bean bleBean = new Bean();
                                bleBean.setDesigid(jsonObject2.getString("desigid"));
                                bleBean.setDesigname(jsonObject2.getString("designame"));
                                designation.add(bleBean);
//                            desigId=dbtask.getdesigId(bleBean.getDesigid());

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                        /*keypersonlist*/
                        try {
                            JSONArray jsonArray2 = jsonObject.getJSONArray("kplist");

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(i);
                                Bean bleBean = new Bean();
                                bleBean.setKeypersonid(jsonObject3.getString("id"));
                                bleBean.setUsername(jsonObject3.getString("name"));
                                bleBean.setLatitude(jsonObject3.getString("lat"));
                                bleBean.setLongitude (jsonObject3.getString("long"));
                                bleBean.setEmail(jsonObject3.getString("email"));
                                bleBean.setEmergency_name (jsonObject3.getString("emergencyname"));
                                bleBean.setEmergency_mobilen(jsonObject3.getString("emergencynumber"));
                                bleBean.setBlood_group (jsonObject3.getString("bloodgroup"));
                                bleBean.setAddress (jsonObject3.getString("address"));
                                bleBean.setDob (jsonObject3.getString("dob"));
                                bleBean.setOfficeid(jsonObject3.getString("officeid"));
                                bleBean.setCityid(jsonObject3.getString("city_id"));
                                bleBean.setMobileno(jsonObject3.getString("mobile"));
                                //    bleBean.setLandline1(jsonObject3.getString("landline"));
                                bleBean.setGender(jsonObject3.getString("gender"));
                                bleBean.setFathername(jsonObject3.getString("fathername"));
                                bleBean.setIdentitynumber(jsonObject3.getString("idnumber"));
                                bleBean.setId_type_id(jsonObject3.getString("typeid"));
                                bleBean.setDesigid(jsonObject3.getString("desigid"));
                                bleBean.setFamilyoffice(jsonObject3.getString("family_office"));
                                bleBean.setFamilydesig(jsonObject3.getString("family_designation"));
                                bleBean.setRelation(jsonObject3.getString("relation"));
                                kplist.add(bleBean);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                       try {
                           JSONArray jsonArray3 = jsonObject.getJSONArray("orgname");
                           List<Bean> orgnameList = new ArrayList<Bean>();

                           for (int i = 0; i < jsonArray3.length(); i++) {
                               JSONObject jsonObject4 = jsonArray3.getJSONObject(i);
                               Bean bleBean = new Bean();
                               bleBean.setFamorgid(jsonObject4.getString("id"));
                               bleBean.setFamorgname(jsonObject4.getString("orgname"));
                               bleBean.setFamorgTypeid(jsonObject4.getString("orgtype"));
                               orgnameList.add(bleBean);
                           }
                           res1=dbtask.insertorgnamedetails(orgnameList);

                       }catch (JSONException e){
                           e.printStackTrace();
                       }
                        try {
                            JSONArray jsonArray4 = jsonObject.getJSONArray("orgtype");
                            for (int i = 0; i < jsonArray4.length(); i++) {
                                JSONObject jsonObject2 = jsonArray4.getJSONObject(i);
                                String orgtypid=jsonObject2.getString("id");
                                orgtypNam=jsonObject2.getString("orgtypename");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        JSONArray jsonArray5 = jsonObject.getJSONArray("familyorgtype");
                        for (int i = 0; i < jsonArray5.length(); i++) {
                            JSONObject jsonObject3 = jsonArray5.getJSONObject(i);
                            String familyorgtypid=jsonObject3.getString("id");
                            famorgtypNam=jsonObject3.getString("orgtypename");
                        }
                        JSONArray jsonArray6 = jsonObject.getJSONArray("familyorgname");
                        List<Bean> familyorgnameList = new ArrayList<Bean>();

                        for (int i = 0; i < jsonArray6.length(); i++) {
                            JSONObject jsonObject4 = jsonArray6.getJSONObject(i);
                            Bean bleBean = new Bean();
                            bleBean.setFamorgid(jsonObject4.getString("id"));
                            bleBean.setFamorgname(jsonObject4.getString("orgname"));
                            bleBean.setFamorgTypeid(jsonObject4.getString("orgtype"));
                            familyorgnameList.add(bleBean);
                        }
                        JSONArray jsonArray7 = jsonObject.getJSONArray("familydesignation");
                        List<Bean> familydesignation = new ArrayList<Bean>();

                        for (int i = 0; i < jsonArray7.length(); i++) {
                            JSONObject jsonObject5 = jsonArray7.getJSONObject(i);
                            Bean bleBean = new Bean();
                            bleBean.setDesigid(jsonObject5.getString("desigid"));
                            bleBean.setDesigname(jsonObject5.getString("designame"));
                            designation.add(bleBean);
//                            famdesigId=dbtask.getfamdesigId(bleBean.getDesigid());
                        }
                        try {
                            JSONObject jsonObject6 = jsonObject.getJSONObject("familyOrgOffice");

                            String famid=jsonObject6.getString("id");
                            String famofficeName=jsonObject6.getString("officename");
                            String fammobilenumber=jsonObject6.getString("mobilenumber");
                            String famcityid =jsonObject6.getString("cityid");
                            String famaddress=jsonObject6.getString("address");
                            String famorgnmid=jsonObject6.getString("orgnameid");
                            try{
                                 famorglat=jsonObject6.getString("orglat");
                                 famorglong=jsonObject6.getString("orglong");
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                             resultts = dbtask.insertfamOrgOfficeDetails(famid, famofficeName, fammobilenumber,famcityid,famaddress, famorgnmid,famorglat,famorglong);

                        }

                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        JSONArray jsonArray8 = jsonObject.getJSONArray("familykplist");

                        for (int i = 0; i < jsonArray8.length(); i++) {
                            try {
                                JSONObject jsonObject7 = jsonArray8.getJSONObject(i);
                                Bean bleBean = new Bean();
                                bleBean.setKeypersonid(jsonObject7.getString("id"));
                                bleBean.setUsername(jsonObject7.getString("name"));
                                bleBean.setLatitude(jsonObject7.getString("lat"));
                                bleBean.setLongitude (jsonObject7.getString("long"));
                                bleBean.setEmail(jsonObject7.getString("email"));
                                bleBean.setEmergency_name (jsonObject7.getString("emergencyname"));
                                bleBean.setEmergency_mobilen(jsonObject7.getString("emergencynumber"));
                                bleBean.setBlood_group (jsonObject7.getString("bloodgroup"));
                                bleBean.setAddress (jsonObject7.getString("address"));
                                bleBean.setDob (jsonObject7.getString("dob"));
                                bleBean.setOfficeid(jsonObject7.getString("officeid"));
                                bleBean.setCityid(jsonObject7.getString("city_id"));
                                bleBean.setMobileno(jsonObject7.getString("mobile"));
                                bleBean.setGender(jsonObject7.getString("gender"));
                                bleBean.setFathername(jsonObject7.getString("fathername"));
                                bleBean.setIdentitynumber(jsonObject7.getString("idnumber"));
                                bleBean.setId_type_id(jsonObject7.getString("typeid"));
                                bleBean.setDesigid(jsonObject7.getString("desigid"));
                                bleBean.setFamilyoffice(jsonObject7.getString("family_office"));
                                bleBean.setFamilydesig(jsonObject7.getString("family_designation"));
                                bleBean.setRelation(jsonObject7.getString("relation"));
                                kplist.add(bleBean);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                        }

                        final SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(Otpactivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(ORGTYPENAME, orgtypNam);
                        editor.commit();

                        results = dbtask.insetdegDetails(designation);
                       // resultss = dbtask.insetdegDetailss(familydesignation);
                        res = dbtask.insertkplist(kplist);

                     //   ress = dbtask.insertfamkplist(familykplist);
                        res11=dbtask.insertorgnamedetails(familyorgnameList);


                        if ( resultts>0) {
                            System.out.println("Data inserted");
                            if(mobileno!=null) {
                                kpid = dbtask.getkerpersonid(mobileno);
                            }else
                            {
                                kpid=dbtask.getkerpersonid(loginmobile);
                            }
                            /*----get keyperson id ----  */

                            kpersonid =dbtask.getkpids();

                            /*--------get designation id on bases of keyperson id---- */

                            desigid =dbtask.getdesigid(kpersonid);
                            List<String> des0igName=new ArrayList<String>();
                            List<Bean> desgingname=new ArrayList<Bean>();

                           /* ------  get designation name on bases of designation id-------*/
                             des0igName =dbtask.getdesigname(desigid);
                            for(int i=0;i<des0igName.size();i++){
                                Bean bleBean = new Bean();
                                String designname= String.valueOf(des0igName.get(i));
                                bleBean.setDesigname(designname);
                                desgingname.add(bleBean);

                            }
                            /*----------insert designation name in registration table---- */

 //                             dbtask.insertdesignname(desgingname,kpersonid);


                            /*---------get city id on baes of keyperson id----------*/
//                            city_id =dbtask.getcityid(kpersonid);
//                            List<String> city_name=new ArrayList<String>();
//                            List<Bean> citynames=new ArrayList<Bean>();
//
//                          /*------------get city name on bases of city id----*/

//                            city_name =dbtask.getcityname(city_id);
//                            for(int i=0;i<city_name.size();i++){
//                                Bean bleBean = new Bean();
//                                String designname= String.valueOf(city_name.get(i));
//                                bleBean.setCityNane(designname);
//                                citynames.add(bleBean);
//
//                            }

//                            /*------------insert cityname in regisrtaion table------*/

//                            dbtask.insertcitynme(citynames,kpersonid);

                            imagecall imagecall=new imagecall();
                            imagecall.execute();
                        }
//

                    } else if (jsonObject.getString("result").contains("failure")) {
                        Toast.makeText(Otpactivity.this, "---Otp mismatch...!!!!Please Enter Verify Otp Again---", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Otpactivity.this, Login.class);
                        startActivity(intent);
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();

                }


        }


        @Override
        protected void onPreExecute() {

             dialog = ProgressDialog.show(Otpactivity.this, "", "Data is inserting....Please wait");
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //firstBar.
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

    }

    public class imagecall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("processing...");
            //  String address = dbTask.getServerIp();
            //   String server_ip=address.split("_")[0];
            //  String port=address.split("_")[1];

            GenericModel prepaidDocModel = new GenericModel(Otpactivity.this);

            String result = null;
            try {
                try {
                    final SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(Otpactivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LOGINKPID, kpid);
                    editor.commit();

                    result = prepaidDocModel.sendid(kpid);
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

            long result1=0;
            try {
                JSONObject jsonObject=new JSONObject(result);
                if (jsonObject.getString("result").contains("success")) {
                    String kp_img=jsonObject.getString("kp_img");
                    String id_img=jsonObject.getString("id_img");
                    //String img ="";
                    byte[] imageAsBytes = Base64.decode(kp_img.getBytes(), Base64.DEFAULT);
                    Bitmap bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    byte[] image1AsBytes = Base64.decode(id_img.getBytes(), Base64.DEFAULT);
                    Bitmap bitmaps = (BitmapFactory.decodeByteArray(image1AsBytes, 0, image1AsBytes.length));
                    //  imageView.setImageBitmap(bitmap);
                    try {
                        path = saveImage(bitmap);
                        paths=saveImage2(bitmaps);//give read write permission
                        // data bsse path.
                        List<Bean> imagepath=new ArrayList<Bean>();

                        for (int i = 0; i<1;i++) {
                            Bean bleBean = new Bean();
                            bleBean.setImage_path1(path);
                            bleBean.setImage_path2(paths);
                            imagepath.add(bleBean);
                        }
                        result1= dbtask.insertimagefromserver(imagepath,kpid);

                        Toast.makeText(Otpactivity.this, "Image path -> "+path, Toast.LENGTH_SHORT).show();
                        dbtask.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Otpactivity.this, Login.class);
                    startActivity(intent);

                }
            else if (result.contains("false")) {
                    Intent intent = new Intent(Otpactivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Otpactivity.this, "", "Image is inserting....Please wait");
            dialog.show();


        }

        @Override
        protected void onProgressUpdate(String... text) {

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
    public String saveImage2(Bitmap myBitmap) {
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
}

