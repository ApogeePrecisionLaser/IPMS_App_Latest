package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.Epassdashboard;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.Iddetails;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.PersonalDetail;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.PlacesActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class DrashBoardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
      TextView ePass,qurantine,document,userId,revisionNo,number,contactmanagement;
      RelativeLayout re_Epass,re_Quarantine,re_Document,re_contactmanagement,RE_FAQ;
      Toolbar toolbar,toolbar_ipms;
      DrawerLayout drawerLayout;
      NavigationView navigationView;
      SessionManager sessionManager;
      public static final String LOGINNUMBER = "loginnumber";
      public static final String ORGOFCID = "orgofcid";
      public static final String FMLYOFCID = "fmlyofcid";
      public static final String CURRENTLOGIN = "crntlogin";
      DatabaseOperation dbTask = new DatabaseOperation(this);
      String orgofcid,fmlyofcid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ePass=findViewById(R.id.epass);
        qurantine=findViewById(R.id.quarantine);
        contactmanagement=findViewById(R.id.contact);
        document=findViewById(R.id.document);
        re_Epass=findViewById(R.id.re_epass);
        re_Quarantine=findViewById(R.id.re_quarantine);
        re_Document=findViewById(R.id.re_document);
        re_contactmanagement=findViewById(R.id.re_contact);
        RE_FAQ=findViewById(R.id.re_faq);
        re_Epass.setOnClickListener(this);
        re_Quarantine.setOnClickListener(this);
        re_Document.setOnClickListener(this);
        re_contactmanagement.setOnClickListener(this);
        RE_FAQ.setOnClickListener(this);
        sessionManager=new SessionManager(this);
        Intent i= getIntent();
        String num =i.getStringExtra("number");
        String uId=i.getStringExtra("uId");
        String rev=i.getStringExtra("revision");
        drawerLayout=findViewById(R.id.drawer_layout_nav);
        navigationView = findViewById(R.id.nv_view) ;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.about);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageView);
        TextView tv = (TextView)hView.findViewById(R.id.idnm);
        TextView tv2 = (TextView)hView.findViewById(R.id.idml);


        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");

        dbTask.open();
        ArrayList<String> keydetail = new ArrayList<>();
        String kpidandlocation = dbTask.getkpidandlocation(numm);
        String keyprsnid = kpidandlocation.split(",")[0];
        keydetail = dbTask.getkeypersonid(keyprsnid);
        if(keydetail.size()!=0){
            for (int k = 0; k < keydetail.size(); k++) {
                String val = keydetail.get(k);
                String usnm = val.split(",")[0];
                String emaill = val.split(",")[1];
                String image = val.split(",")[4];
                orgofcid = val.split(",")[10];
                fmlyofcid = val.split(",")[11];
                if(!orgofcid.equalsIgnoreCase("0")){
                    editor.putString(ORGOFCID, orgofcid);
                    editor.commit();
                }else{
                    editor.putString(FMLYOFCID,fmlyofcid);
                    editor.commit();
                }


                tv.setText(usnm);
                tv2.setText(emaill);
                imgvw.setImageBitmap(BitmapFactory.decodeFile(image));



            }
        }else{
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }


        toolbar = findViewById(R.id.tool) ;
        setSupportActionBar(toolbar) ;
        /*toolbar =  findViewById(R.id.tool);*/
        toolbar.setTitle("DashBoard");
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat. START )) {
                    drawerLayout.closeDrawer(GravityCompat. START ) ;
                } else {
                    drawerLayout.openDrawer(GravityCompat. START);
                }
            }
        });


        imgvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrashBoardActivity.this, PersonalDetail.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
      finish();
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.re_epass:
             Intent intent=new Intent(DrashBoardActivity.this, Epassdashboard.class);
             startActivity(intent);
             break;

         case R.id.re_quarantine:
             Intent intent1=new Intent(DrashBoardActivity.this,QuaratineActivity.class);
             startActivity(intent1);
             break;

         case R.id.re_document:
             Intent intent2=new Intent(DrashBoardActivity.this,DocumentsActivity.class);
             startActivity(intent2);
             break;

         case R.id.re_contact:
             Intent intent3=new Intent(DrashBoardActivity.this,ContactManagement.class);
             startActivity(intent3);
             break;
         case R.id.re_faq:
             Intent intent4=new Intent(DrashBoardActivity.this,FAQ.class);
             startActivity(intent4);
             break;
     }
    }


    public void alertdialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DrashBoardActivity.this);
        builder1.setTitle("About IPMSApp!");
        builder1.setMessage("IPMS App Version 1.3"+"\n"+
                "IPMS Database Version 1.3"+"\n"+
                "IPMSApp is a Android-based Integrated Pandemic Management System software\n" );
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "oK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void logiasalert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DrashBoardActivity.this);
        builder1.setTitle("Login As");
        String retrnval=null;
        String loginval = sharedPreferences.getString(CURRENTLOGIN, "default value");
        if(!orgofcid.equalsIgnoreCase("0") && loginval.equalsIgnoreCase("2")){
            retrnval = loginas(orgofcid);
            editor.putString(CURRENTLOGIN,"1");
            editor.commit();
        }else{
            retrnval = loginas(fmlyofcid);
            editor.putString(CURRENTLOGIN,"2");
            editor.commit();
        }
        builder1.setMessage("You are login as//"+retrnval+
                "//Continue as//"+ retrnval +"//or you want to change?"+"\n");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Change",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String loginval = sharedPreferences.getString(CURRENTLOGIN, "default value");
                        if(loginval.equalsIgnoreCase("1")){
                            sharedPreferences.edit().remove(ORGOFCID).apply();
                            editor.putString(FMLYOFCID,fmlyofcid);
                            editor.commit();
                            Toast.makeText(DrashBoardActivity.this, "Logged in as Family", Toast.LENGTH_SHORT).show();
                        }else if(loginval.equalsIgnoreCase("2")){
                            if(!orgofcid.equalsIgnoreCase("0")){
                                sharedPreferences.edit().remove(FMLYOFCID).apply();
                                editor.putString(ORGOFCID, orgofcid);
                                editor.commit();
                                Toast.makeText(DrashBoardActivity.this, "Logged in as Organisation", Toast.LENGTH_SHORT).show();
                            }else{
                                editor.putString(FMLYOFCID,fmlyofcid);
                                editor.commit();
                                Toast.makeText(DrashBoardActivity.this, "No any Organisation found", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(DrashBoardActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public String loginas(String val){
        dbTask.open();
        String orgofcnmmm=null;
        String orgnmid = dbTask.getorgnmid(val);
        dbTask.close();
        if(orgnmid!=null){
            String orgofid = orgnmid.split(",")[0];
            orgofcnmmm = orgnmid.split(",")[1];
        }
        return orgofcnmmm;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                alertdialog();
                return true;
           *//*case R.id.it_first:
                Toast.makeText(this, "first_Item", Toast.LENGTH_SHORT).show();
                break;*//*

                default:
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId() ;

        if (id == R.id. about ) {
            alertdialog();
            return true;
        } else if(id == R.id.logout){
            sessionManager.logoutUser();
            sharedPreferences.edit().remove(ORGOFCID).apply();
            return true;
        }else if(id == R.id.iddtls) {
            Intent intent = new Intent(DrashBoardActivity.this, Iddetails.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.loginas) {
            logiasalert();
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat. START ) ;
        return true;

    }



}
