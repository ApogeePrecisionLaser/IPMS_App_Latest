package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText edit_mobile, edit_password;
    Button btnLogin;
    // Session Manager Class
    ProgressDialog dialog;
    String id,revisionno;
    String mobileno, password_registered;
    String url;
    List mobilenumberlist =new ArrayList();
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    String number,mobile;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    SessionManager sessionManager;
    Toolbar toolbar;
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Login");
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
        edit_mobile = (EditText) findViewById(R.id.mobile);
        String code = "+91 ";
        edit_mobile.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        edit_mobile.setCompoundDrawablePadding(code.length() * 10);
        edit_password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        sessionManager=new SessionManager(this);
        boolean islogin=sessionManager.checkLogin();
        if(islogin){
            HashMap<String,String> userDetail=sessionManager.getUserDetails();
            String ph=userDetail.get(SessionManager.KEY_MOBILE);
            Intent i=new Intent(this,DrashBoardActivity.class);
            i.putExtra("mobile_no",ph);
            finish();
            startActivity(i);
        }

        Button btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Login_number", edit_mobile.getText().toString().trim());
                     num=  edit_mobile.getText().toString().trim();
                    editor.putString("login_password", edit_password.getText().toString().trim());
                    editor.commit();
                    database();

                } catch (Exception e) {


                    e.printStackTrace();
                }
            }
        });
      }



    public void showErrorAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Message");
        // Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }


    public class Logindetailverfiy extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            GenericModel prepaidDocModel = new GenericModel(Login.this);
            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    result = prepaidDocModel.verifylogindetail();
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

                //  String str = response.body().toString();
                JSONObject jsonObject=new JSONObject(result);
                if (jsonObject.getString("result").contains("success")) {
                 id=jsonObject.getString("id");
                 number=jsonObject.getString("mobile");
//                   try{
//                       JSONArray jsonArray2 = jsonObject.getJSONArray("kplist");
//                       for (int i = 0; i < jsonArray2.length(); i++) {
//                           JSONObject jsonObject3 = jsonArray2.getJSONObject(i);
//                           id=jsonObject3.getString("id");
//                            number=jsonObject3.getString("mobile");
////
//                       }
//                   }catch (Exception e){
//                       e.printStackTrace();
//                   }
                    if (number != null) {
                        Intent intent = new Intent(Login.this, DrashBoardActivity.class);
                        intent.putExtra("number", number);
                        intent.putExtra("uId", id);
                 //       intent.putExtra("revision", revisionno);
                        final SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(LOGINNUMBER, number);
                        editor.putString(LOGINKPID, id);
                        editor.commit();
                        sessionManager.createLoginSession(number);
                        startActivity(intent);

                        finish();

                    }

                } else if (jsonObject.getString("result").contains("failure")) {
                    Toast.makeText(Login.this, "---Login Details Mismatch...!!!!Please Enter your Login Details---", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);
                }
                else if (jsonObject.getString("result").contains("OTP")) {
                   getmobile();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Login.this, "", "Proccessing....Please wait");
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
    public  void getmobile(){
        mobile=edit_mobile.getText().toString();
        sessionManager.createLoginSession(number);
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Login.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGINNUMBER, mobile);
        editor.commit();
        Otpverify otpverify=new Otpverify();
        otpverify.execute();
    }
    public class Otpverify extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            GenericModel prepaidDocModel = new GenericModel(Login.this);
            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    result = prepaidDocModel.getotp(mobile);
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

                //  String str = response.body().toString();
                if (result.contains("true")) {
                    Intent intent = new Intent(Login.this, Otpactivity.class);
                    intent.putExtra("loginmobile",mobile);
                    startActivity(intent);

                } else if (result.contains("false")) {
                    Toast.makeText(Login.this, "!Please Enter Valid Otp Again---", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Login.this, "", "Proccessing....Please wait");
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
    private void database(){
        DatabaseOperation databaseOperation=new DatabaseOperation(Login.this);
        databaseOperation.open();
        mobilenumberlist = databaseOperation.getregisteredmobileno();
        if(mobilenumberlist.contains(num)){
            Logindetailverfiy logindetailverfiy=new Logindetailverfiy();
            logindetailverfiy.execute();
        }else{
//             to send otp on mobile number....
            getmobile();

        }

    }
}
