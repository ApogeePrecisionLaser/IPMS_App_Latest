package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.RouteActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.services.NetworkChangeReceiver;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants.CONNECTIVITY_ACTION;


public class MainActivity extends AppCompatActivity {

    Button btn_register, btn_login,tcpip;
    public static final int RequestPermissionCode = 7;
    DatabaseOperation databaseOperation;
    String orgname;
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
        setContentView(R.layout.activity_main);
        RequestMultiplePermission();
        CheckingPermissionIsEnabledOrNot();
        databaseOperation=new DatabaseOperation(MainActivity.this);
        databaseOperation.open();
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();

        if (NetworkUtil.getConnectivityStatus(MainActivity.this) > 0 ) System.out.println("Connect");
        else System.out.println("No connection");

        setorgvalues();
        btn_login =  findViewById(R.id.btnLogin);
        btn_register = findViewById(R.id.btnRegister);
        tcpip = findViewById(R.id.tcpip);

        GenericModel genericModel = new GenericModel(this);
        genericModel.requestorgtype("both");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NumberRegistrationActivity.class);
                intent.putExtra("org",orgname);
                startActivity(intent);
                finish();
            }
        });

        tcpip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, IpportActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    //Permission function starts from here
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {READ_PHONE_STATE,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        CALL_PHONE,
                        ACCESS_COARSE_LOCATION,
                        INTERNET,
                        CAMERA,
                        ACCESS_NETWORK_STATE
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
                    boolean InternetPermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean access_netwrk_state = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (PhoneStatePermission && LocationPermission && RExternalStoragePermission && WExternalStoragePermission
                            && InternetPermission && camera && access_netwrk_state ) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
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
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int seventh = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int eighth = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        int nineth = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                seventh ==  PackageManager.PERMISSION_GRANTED &&
                eighth == PackageManager.PERMISSION_GRANTED &&
                nineth == PackageManager.PERMISSION_GRANTED  ;
    }
    public  void setorgvalues(){
        List<Bean> list = new ArrayList<Bean>();
        Bean bean=new Bean();


        // databaseOperation.insertOrgvalue();
        orgname= databaseOperation.ispchld();
    }


}
