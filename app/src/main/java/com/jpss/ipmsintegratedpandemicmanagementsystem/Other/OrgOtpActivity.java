package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrgOtpActivity extends AppCompatActivity {
    EditText editText_otp;
    Button button_submit, btnLinkToResendOtp;
    String otp = "";
    String s;
    String mobileno, password_registered,orgtypNam,famorgtypNam;
    String desigId,famdesigId;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCE = "Otp";
    String number,id,revisionno,loginmobile,password,blood_group,gender,cityloc,address1,
            emergency_mobilen,emergency_name,email,longitude,latitude,username,dob,officeid,
            cityid1,mobile,landlineno,fathername,idnumber,userid,desgid,typeid;
    List<Bean> details= new ArrayList<Bean>();
    ProgressDialog dialog;
    Toolbar toolbar;
    public static final String orgPREFERENCES = "orgpref" ;
    SharedPreferences sharedPreferences;
    public static final String ORGTYPENAME = "orgnm";
    public static final String ORGTYPEID = "orgid";
    List<Bean> familyorgnameList = new ArrayList<Bean>();
    List<Bean> familykplist = new ArrayList<Bean>();
   long resultts;
    List<Bean> familydesignation = new ArrayList<Bean>();
    List<Bean> designation = new ArrayList<Bean>();

    String famid,famofficeName,fammobilenumber,famcityid,famaddress,famorgnmid,famorglat,famorglong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgotp);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Org otp");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

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
            GenericModel prepaidDocModel = new GenericModel(OrgOtpActivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = new String();
            try {
                try {
                    result = prepaidDocModel.verify_orgOtp();
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
            try {
                String orglat="0.0";
                String orglong="0.0";
                long results=0,res=0,res1=0,resultss=0,ress=0,res11,resultt=0;
                DatabaseOperation databaseOperation=new DatabaseOperation(OrgOtpActivity.this);
                databaseOperation.open();
                JSONObject jsonObject=new JSONObject(result);

                //{"result":"success","kplist":[{"id":140,"name":"testing","officeid":19,"city_id":2,"address":"abc","mobile":"7248858155","landline":"","email":"sonaya@hmail.com","fathername":"test","dob":"23\/09\/98","lat":"23.136680686453023","long":"80.07686097343752","bloodgroup":"A+","emergencyname":"abc","emergencynumber":"123456789","idnumber":"1223445567896422","typeid":1,"desigid":19}],"OrgOffice":{"id":19,"officename":"MediNeed","mobilenumber":"8287874165","cityid":7,"address":"Wright Town","orgnameid":12},"designation":[{"desigid":18,"designame":"Owner"},{"desigid":15,"designame":"Manager"},{"desigid":19,"designame":"Sales Officer"},{"desigid":20,"designame":"Delivery Boy"}],"orgname":[{"id":12,"orgname":"MediNeed","orgtype":10}],"orgtype":[{"id":1,"orgtypename":"Private"}]}
//{"result":"success","kplist":[],"OrgOffice":{"id":20,"officename":"Testing","mobilenumber":"7905635621","cityid":7,"address":"hoi","orgnameid":13},"designation":[{"desigid":18,"designame":"Owner"},{"desigid":15,"designame":"Manager"},{"desigid":19,"designame":"Sales Officer"},{"desigid":20,"designame":"Delivery Boy"}],"orgname":[{"id":13,"orgname":"Demo","orgtype":10}],"orgtype":[{"id":1,"orgtypename":"Private"}],"familyorgtype":[{"id":4,"orgtypename":"Public"}],"familyorgname":[{"id":18,"orgname":"Family","orgtype":12}],"familydesignation":[{"desigid":18,"designame":"Owner"},{"desigid":15,"designame":"Manager"},{"desigid":19,"designame":"Sales Officer"},{"desigid":20,"designame":"Delivery Boy"},{"desigid":21,"designame":"Family Head"},{"desigid":22,"designame":"Family Member"}],"familyOrgOffice":{"id":47,"officename":"Family7905635621","mobilenumber":"7905635621","cityid":2,"address":"Noida","orgnameid":18},"familykplist":[]}
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
                        resultt = databaseOperation.insertOrgOfficeDetails(id, officeName, mobilenumber,cityid,address,orgnmid,orglat,orglong,"office");

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
                            desigId=databaseOperation.getdesigId(bleBean.getDesigid());

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }


                    /*keypersonlist*/
                    JSONArray jsonArray2 = jsonObject.getJSONArray("kplist");
                    List<Bean> kplist = new ArrayList<Bean>();

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        try {
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
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                    try {
                        JSONArray jsonArray3 = jsonObject.getJSONArray("orgname");
                        List<Bean> orgnameList = new ArrayList<Bean>();

                        for (int i = 0; i < jsonArray3.length(); i++) {
                            JSONObject jsonObject4 = jsonArray3.getJSONObject(i);
                            Bean bleBean = new Bean();

                            bleBean.setOrgid(jsonObject4.getString("id"));
                            bleBean.setOrgName(jsonObject4.getString("orgname"));
                            bleBean.setOrgTypeid(jsonObject4.getString("orgtype"));
                            orgnameList.add(bleBean);
                        }
                        res1=databaseOperation.insertorgnamedetails(orgnameList);

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

                    try {
                    JSONArray jsonArray5 = jsonObject.getJSONArray("familyorgtype");

                    for (int i = 0; i < jsonArray5.length(); i++) {

                            JSONObject jsonObject3 = jsonArray5.getJSONObject(i);
                            String familyorgtypid = jsonObject3.getString("id");
                            famorgtypNam = jsonObject3.getString("orgtypename");
                        }
                    }
                        catch(JSONException e){
                        e.printStackTrace();
                        }
                    try {
                    JSONArray jsonArray6 = jsonObject.getJSONArray("familyorgname");

                        for (int i = 0; i < jsonArray6.length(); i++) {
                            JSONObject jsonObject4 = jsonArray6.getJSONObject(i);
                            Bean bleBean = new Bean();
                            bleBean.setFamorgid(jsonObject4.getString("id"));
                            bleBean.setFamorgname(jsonObject4.getString("orgname"));
                            bleBean.setFamorgTypeid(jsonObject4.getString("orgtype"));
                            familyorgnameList.add(bleBean);
                        }

                    JSONArray jsonArray7 = jsonObject.getJSONArray("familydesignation");

                    for (int i = 0; i < jsonArray7.length(); i++) {
                        JSONObject jsonObject5 = jsonArray7.getJSONObject(i);
                        Bean bleBean = new Bean();
                        bleBean.setFamDesigid(jsonObject5.getString("desigid"));
                        bleBean.setFamDesigname(jsonObject5.getString("designame"));
                        familydesignation.add(bleBean);
                        famdesigId=databaseOperation.getfamdesigId(bleBean.getDesigid());
                    }
                    JSONObject jsonObject6 = jsonObject.getJSONObject("familyOrgOffice");
                     famid=jsonObject6.getString("id");
                     famofficeName=jsonObject6.getString("officename");
                     fammobilenumber=jsonObject6.getString("mobilenumber");
                     famcityid =jsonObject6.getString("cityid");
                     famaddress=jsonObject6.getString("address");
                     famorgnmid=jsonObject6.getString("orgnameid");
                        try{
                            famorglat=jsonObject6.getString("orglat");
                            famorglong=jsonObject6.getString("orglong");
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        resultts = databaseOperation.insertfamOrgOfficeDetails(famid, famofficeName, fammobilenumber,famcityid,famaddress, famorgnmid,famorglat,famorglong);


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    try {
                    JSONArray jsonArray8 = jsonObject.getJSONArray("familykplist");

                        for (int i = 0; i < jsonArray8.length(); i++) {

                            JSONObject jsonObject7 = jsonArray8.getJSONObject(i);
                            Bean bleBean = new Bean();
                            bleBean.setFamKeypersonid(jsonObject7.getString("id"));
                            bleBean.setFamUsername(jsonObject7.getString("name"));
                            bleBean.setFamlattitude(jsonObject7.getString("lat"));
                            bleBean.setFamlongitude(jsonObject7.getString("long"));
                            bleBean.setFamEmail(jsonObject7.getString("email"));
                            bleBean.setFamEmergency_name(jsonObject7.getString("emergencyname"));
                            bleBean.setFamEmergency_mobilen(jsonObject7.getString("emergencynumber"));
                            bleBean.setFamBlood_group(jsonObject7.getString("bloodgroup"));
                            bleBean.setFamAddress(jsonObject7.getString("address"));
                            bleBean.setFamDob(jsonObject7.getString("dob"));
                            bleBean.setFamOfficeid(jsonObject7.getString("officeid"));
                            bleBean.setFamCityid(jsonObject7.getString("city_id"));
                            bleBean.setFamMobileno(jsonObject7.getString("mobile"));
                            bleBean.setFamGender(jsonObject7.getString("gender"));
//                            bleBean.setLandline1(jsonObject3.getString("landline"));
                            bleBean.setFamFathername(jsonObject7.getString("fathername"));
                            bleBean.setFamIdentitynumber(jsonObject7.getString("idnumber"));
                            bleBean.setFamId_type_id(jsonObject7.getString("typeid"));
                            bleBean.setFamDesigid(jsonObject7.getString("desigid"));
                            bleBean.setFamilyoffice(jsonObject7.getString("family_office"));
                            bleBean.setFamilydesig(jsonObject7.getString("family_designation"));
                            bleBean.setRelation(jsonObject7.getString("relation"));
                            familykplist.add(bleBean);

                    }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    final SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(OrgOtpActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ORGTYPENAME, orgtypNam);
                    editor.commit();

                    results = databaseOperation.insetdegDetails(designation);
                    resultss = databaseOperation.insetdegDetailss(familydesignation);
                    res = databaseOperation.insertkplist(kplist);
                    ress = databaseOperation.insertfamkplist(familykplist);
                    res11=databaseOperation.insertorgnamedetails(familyorgnameList);

                    if (resultt > 0) {
                        System.out.println("Data inserted");
                    } else {
                        Toast.makeText(OrgOtpActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                    if (resultts > 0) {
                        System.out.println("Data inserted");
                    } else {
                        Toast.makeText(OrgOtpActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(OrgOtpActivity.this, Registration.class);
                    intent.putExtra("mobile_no",mobileno);
                    startActivity(intent);


                } else if (jsonObject.getString("result").contains("failure")) {
                    Toast.makeText(OrgOtpActivity.this, "---Otp mismatch...!!!!Please Enter Verify Otp Again---", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OrgOtpActivity.this, Login.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }






        @Override
        protected void onPreExecute() {

            dialog = ProgressDialog.show(OrgOtpActivity.this, "", "Data is inserting....Please wait");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //firstBar.
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

    }

}
