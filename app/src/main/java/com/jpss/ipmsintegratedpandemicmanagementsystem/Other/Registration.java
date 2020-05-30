package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass.RouteActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.services.NetworkChangeReceiver;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants.CONNECTIVITY_ACTION;

public class Registration extends AppCompatActivity {
    DatabaseOperation dbTask = new DatabaseOperation(Registration.this);
    List<String>desigName=new ArrayList<>();
    List<String>cityname=new ArrayList<>();
    SharedPreferences.Editor editor;
    int shweta = 0;
    ArrayList<String> imagelist = new ArrayList<String>();
    static int i = 0;
    File mediaFile;
    public static final int RequestPermissionCode = 7;
    EditText name, editText_mobile, email, password, editText_confirmpass;
    Button btnRegister,btnAddfamily,send;
    String imagePath = "";
    Bitmap bitmap;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Cable";
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    String secondimage;
    Uri fileUri;
    File dir;
    public static final String Registernumber = "register";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ProgressDialog dialog;
    Spinner genderSpinner, bloodgrp_spinner,city_spinner,user_id,desig_spinner;
    String gender = "M", blood_grp = "",city_name="",desig_name="";
    String moblieno, url = "",mobilieno=null;
    EditText textIn, editText_Emname,edtaddress,date_ofbirth,fatherName,edtaddress2,edtaddress3;
    ImageButton buttonAdd;
    LinearLayout container, imagelayout, imageLayout1;
    String ip, port;
    String latitude, longitude;
    Map<String, String> map = new HashMap<String, String>();
    ArrayList<String> arrayList = new ArrayList();
    public File f;
    TextView id, photo,officeName;
    String filePath1;
    String familyuserimg,familyidimg,emergy_name;
    String emergy_mobileno,identityNo;
    String username;
    String emailid;
    String mobileno;
    String password1;
    String ids, city, address, revisionno,dob,father,office_id,address2,address3,office_Name;
    String desig_id,city_id;
    DatePickerDialog picker;
    List<String>famuserid=new ArrayList<>();

    Toolbar toolbar;
    Spinner familySpinner,familygenderSpinner,fambloodGroupspin;
    EditText family_userName,family_address,family_no,family_dob,family_email,family_pass,family_cnfPass,family_fatherName;
    String family_member,familyDob,familyAddres,familyName,familyMobile,familyEmail,familyPass,familyFather;
    String famgender = "M",famblood_group = "",fam_userid ,fam_useridentityid,famUseridentityNo;
    ImageView famuserImage, famUserIdimg;
    Spinner famuser_id;
    TextView designationName;
    EditText fam_userIdentityNo;
    Context mcontext;
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
        setContentView(R.layout.activity_registration);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Registration");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();

        if (NetworkUtil.getConnectivityStatus(Registration.this) > 0 ) System.out.println("Connect");
        else System.out.println("No connection");
        editText_mobile = findViewById(R.id.editText_mobile);
        String code = "+91 ";
        editText_mobile.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        editText_mobile.setCompoundDrawablePadding(code.length() * 10);

        city_spinner=findViewById(R.id.citySpinner);
        designationName=findViewById(R.id.desName);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        editText_confirmpass = findViewById(R.id.editText_confirmpass);
        btnRegister = findViewById(R.id.btnRegister);
        genderSpinner = findViewById(R.id.genderSpinner);
        desig_spinner = findViewById(R.id.desigSpinner);
        bloodgrp_spinner = findViewById(R.id.bloodgrp_spinner);
        edtaddress=findViewById(R.id.editText_address);
        textIn = findViewById(R.id.textin);
        editText_Emname = findViewById(R.id.editText_Emname);
        officeName=findViewById(R.id.office_name);
        buttonAdd = findViewById(R.id.add);
        btnAddfamily=findViewById(R.id.btnAddfamily);
        container = findViewById(R.id.container);
        date_ofbirth=findViewById(R.id.editText_dateOfbirth);
        fatherName=findViewById(R.id.editText_fatherName);
        edtaddress2=findViewById(R.id.editText_address2);
        edtaddress3=findViewById(R.id.editText_address3);
        user_id=findViewById(R.id.idSpinner);
        Intent intent = getIntent();
        mobilieno = intent.getStringExtra("mobile_no");
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        moblieno = sharedPreferences.getString(Registernumber, "default value");
        checkRunTimePermission();
        fatherName.addTextChangedListener(textWatcher);
        dbTask.open();

        if(mobilieno!=null){
            editText_mobile.setText(moblieno);
            editText_mobile.setEnabled(false);
            String officeDetail=dbTask.getorgofficedetal(mobilieno,"office");
            office_id = officeDetail.split(",")[0];
            office_Name = officeDetail.split(",")[1];
            desig_spinner.setVisibility(View.VISIBLE);
            officeName.setText(office_Name);
        }else {
            editText_mobile.setEnabled(true);
            officeName.setText("family");
            designationName.setVisibility(View.VISIBLE);
            designationName.setText("familyhead");
        }
        date_ofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_ofbirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dob = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date_ofbirth.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();

            }
        });
        moblieno = sharedPreferences.getString(Registernumber, "default value");
        getdetails();
        Button btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationSuccess()) {
                    connection();
                    GPSTrack gpsTrack = new GPSTrack(Registration.this);
                    latitude = String.valueOf(gpsTrack.getLatitude());
                    longitude= String.valueOf(gpsTrack.getLongitude());
                    //  getdata();
                    if(latitude.equals("0.0") && longitude.equals("0.0")){
                        Toast.makeText(Registration.this, "Please on your GPS location!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Callingservcie callingservcie = new Callingservcie();
                        callingservcie.execute();
                    }

                }

                else {
                    Toast.makeText(Registration.this, "Please fill above field", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnAddfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address=edtaddress.getText().toString();
                showAlertDialog();

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Female"))
                    famgender = "F";
                else
                    famgender = "M";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bloodgrp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Blood Group"))
                    famblood_group = "";
                else
                    famblood_group = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("select"))
                    city_name = "";
                else
                    city_name = item;
                dbTask.open();
                city_id=dbTask.getcitynameid(city_name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        desig_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("select"))
                    desig_name = "";
                else
                    desig_name = item;
                dbTask.open();
                desig_id=dbTask.getdesignameid(desig_name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }) ;

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!editText_Emname.getText().toString().isEmpty() && !textIn.getText().toString().isEmpty()) {
                    if (Validation.isPhoneNumber(textIn, true)) {
                        LayoutInflater layoutInflater =
                                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        textOut.setText(editText_Emname.getText().toString() + "," + textIn.getText().toString());
                        map.put(textIn.getText().toString().trim(), editText_Emname.getText().toString());
                        textIn.setText("");
                        editText_Emname.setText("");
                        ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                try {
                                    TextView textOut1 = (TextView) addView.findViewById(R.id.textout);
                                    String val = textOut1.getText().toString().split(",")[1];
                                    if (!map.isEmpty()) {
                                        for (Map.Entry<String, String> entry : map.entrySet()) {
                                            if (entry.getKey().trim().equals(val.trim())) {
                                                map.remove(entry.getKey());

                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("err" + e);
                                }
                            }
                        });

                        container.addView(addView);
                    }
                } else {
                    if (editText_Emname.getText().toString().isEmpty())
                        editText_Emname.setError("required");
                    if (textIn.getText().toString().isEmpty())
                        textIn.setError("required");
                }
            }

        });

    }
    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Registration.this,  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                    ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                GPSTrack gpsTrack = new GPSTrack(Registration.this);

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        } else {
            GPSTrack gpsTrack = new GPSTrack(Registration.this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GPSTrack gpsTrack = new GPSTrack(Registration.this);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // If User Checked 'Don't Show Again' checkbox for runtime permission, then navigate user to Settings
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Registration.this);
                    dialog.setTitle("Permission Required");
                    dialog.setCancelable(false);
                    dialog.setMessage("You have to Allow permission to access user location");
                    dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",
                                    Registration.this.getPackageName(), null));
                            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i, 1001);
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                //code for deny
            }
        }
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case 1001:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        GPSTrack gpsTrack = new GPSTrack(Registration.this);
                        if (gpsTrack.canGetLocation()) {
                            latitude = String.valueOf(gpsTrack.getLatitude());
                            longitude = String.valueOf(gpsTrack.getLongitude());

                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},10);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void getdetail() {
        DatabaseOperation dbTask=new DatabaseOperation(this);
        dbTask.open();
        famuserid.add("select");
        List<String> userids=dbTask.getuserid();
        dbTask.close();

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userids);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        famuser_id.setAdapter(arrayAdapter2);


    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Registration.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_addfamily, null);
        alertDialog.setView(customLayout);

        familySpinner=customLayout.findViewById(R.id.familySpinner);
        familygenderSpinner=customLayout.findViewById(R.id.famgenderSpinner);
        fambloodGroupspin=customLayout.findViewById(R.id.fambloodgrp_spinner);
        famuser_id=customLayout.findViewById(R.id.fam_idSpinner);
        family_address=customLayout.findViewById(R.id.edtfamily_address);
        family_no=customLayout.findViewById(R.id.edtfamily_mobile);
        family_dob=customLayout.findViewById(R.id.edtfamily_dob);
        family_email=customLayout.findViewById(R.id.edtfamily_email);
        family_pass=customLayout.findViewById(R.id.fampassword);
        family_userName=customLayout.findViewById(R.id.edtfamily_name);
        family_cnfPass=customLayout.findViewById(R.id.editText_famconfirmpass);
        family_fatherName=customLayout.findViewById(R.id.editText_famfatherName);
        fam_userIdentityNo=customLayout.findViewById(R.id.fam_identity_No);
        famuserImage=customLayout.findViewById(R.id.famuserPhoto);
        famUserIdimg=customLayout.findViewById(R.id.famuserId);
        id =customLayout.findViewById(R.id.famid);
        photo =customLayout.findViewById(R.id.famphoto);
        // send =customLayout.findViewById(R.id.btnfamilysend);
        if(fam_userIdentityNo.getText()!=null) {
            fam_userIdentityNo.setError("Cannot be empty");
        }
        if(edtaddress!=null){
            family_address.setText(address);
            family_address.setEnabled(false);


        }else {
            family_address.setEnabled(true);

        }
        getdetail();
        famuser_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("select"))
                    fam_userid = "";
                else
                    fam_userid = item;
                fam_useridentityid=dbTask.getuserids(fam_userid);
                dbTask.close();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familygenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Female"))
                    gender = "F";
                else
                    gender = "M";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fambloodGroupspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Blood Group"))
                    blood_grp = "";
                else
                    blood_grp = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        family_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                family_dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                familyDob = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                family_dob.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase(" Select Family Member-->"))
                    family_member = "";
                else
                    family_member = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        /*AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();*/
        AlertDialog alert = alertDialog.create();
        alert.show();
        Button theButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alert));


    }
    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            // put your code here
            if (validationSuccessss()) {
                sendData();
                dialog.dismiss();
            }

            else {
                Toast.makeText(Registration.this, "Please fill above field", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void imageclicks(View v) {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(Registration.this, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
        } else {
            if (checkPermission()) {
                if (shweta > 1) {
                    Toast.makeText(Registration.this, "cancot click more than 2 image", Toast.LENGTH_LONG).show();
                } else {
                    captureImage();

                }

            } else {

                requestPermission();
            }
        }
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA);
        int result3 = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.INTERNET);
        int result5 = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_NETWORK_STATE);

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Registration.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, RequestPermissionCode);


//        ActivityCompat.requestPermissions(CameraActivity.this, new
//                String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);


    }

    private boolean isDeviceSupportCamera() {
        if (Registration.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > 23) {

            File file = getCameraFile();
            imagePath = file.getPath();
            fileUri = FileProvider.getUriForFile(Registration.this, Registration.this.getPackageName() + ".provider", file);

        } else {
            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public File getCameraFile() {
        dir = Registration.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, "IMG_" + "" + System.currentTimeMillis() + ".jpg");
    }
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), IMAGE_DIRECTORY_NAME);
        // File  mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString());
        f = mediaStorageDir;
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + "*" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        imagePath = mediaFile.getAbsolutePath();
        return mediaFile;
        //return mediaStorageDir;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //viewImage();
                //viewImage1();
                ImageView imgview = new ImageView(Registration.this);
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                bitmap = BitmapFactory.decodeFile(imagePath, options);
                imgview.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),""+bitmap, Toast.LENGTH_SHORT).show();
                Toast.makeText(Registration.this, "Image Captured Successfully..", Toast.LENGTH_SHORT).show();
                imgview.setImageBitmap(bitmap);*/

                BitmapFactory.Options options;

                try {
                    options=new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(imagePath, options);
                    imgview.setImageBitmap(bitmap);
                } catch (OutOfMemoryError e) {
                    try {
                        options=new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = 7;
                        bitmap = BitmapFactory.decodeFile(imagePath, options);
                        imgview.setImageBitmap(bitmap);
                    } catch (Exception excepetion) {
                    }
                }
                ByteArrayOutputStream stream;
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                String encodedString = Base64.encodeToString(byte_arr, 1);

                imgview.requestFocus();
                imgview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                if (shweta == 0) {
                    imagelist.add(imagePath);
                    famuserImage.setImageBitmap(bitmap);
                    photo.setText("USER PHOTO");
                    Toast.makeText(Registration.this, " Now Please Capture Your Identity Image", Toast.LENGTH_SHORT).show();


                } else {
                    if (shweta == 1) {
                        imagelist.add(imagePath);
                        famUserIdimg.setImageBitmap(bitmap);
                        id.setText("VOTER/AADHAR ID");
                        if(famUserIdimg==null)
                        {
                            Toast.makeText(this, "Please Select User Id Photo...", Toast.LENGTH_SHORT).show();
                        }else{

                        }

                    }
                }
                shweta++;

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(Registration.this, "User cancelled image capture", Toast.LENGTH_SHORT).show();
                //showAlert("Image Cancel");
            } else {
                // failed to capture image
                Toast.makeText(Registration.this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendData() {
        long result=0;
        try {
            familyuserimg = imagelist.get(0);
            familyidimg = imagelist.get(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        famUseridentityNo=fam_userIdentityNo.getText().toString();
        familyAddres=family_address.getText().toString();
        familyName=family_userName.getText().toString();
        familyMobile=family_no.getText().toString();
        familyEmail=family_email.getText().toString();
        familyPass=family_pass.getText().toString();
        familyFather=family_fatherName.getText().toString();
        dbTask.open();
        List<Bean>familydata=new ArrayList<>();
        Bean bean=new Bean();
        bean.setFamilyusername(familyName);
        bean.setFamilydob(familyDob);
        bean.setFamilygender(famgender);
        bean.setFamilymember(family_member);
        bean.setFamilyFather(familyFather);
        bean.setFamilypassword(familyPass);
        bean.setEmail(familyEmail);
        bean.setFamilymobileno(familyMobile);
        bean.setBlood_group(famblood_group);
        bean.setAddress(familyAddres);
        bean.setImage_path1(familyuserimg);
        bean.setImage_path2(familyidimg);
        bean.setIdentitynumber(famUseridentityNo);
        bean.setId_type(fam_userid);
        bean.setId_type_id(fam_useridentityid);
        familydata.add(bean);
        result= dbTask.insertfamilydetails(familydata);
        if(result>0){
            shweta=0;
            imagelist.clear();
            familyuserimg="";
            familyidimg="";
            imagePath="";
            familydata.clear();
            Toast.makeText(Registration.this,"Family data stored in application",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Registration.this,"Family data not stored in application",Toast.LENGTH_LONG).show();

        }
    }

    private void connection() {
        if (checkConnection()) {

        }else {
            Toast.makeText(this, "Check your enternet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class Callingservcie extends AsyncTask<String, String, String> {

        List list;

        public Callingservcie() {

        }

        @Override
        protected String doInBackground(String... params) {

            GenericModel prepaidDocModel = new GenericModel(Registration.this);
            // prepaidDocModel.setDbTask(dbTask);
            JSONObject json = new JSONObject();
            json = getdata();

            String result = null;
            try {
                result = prepaidDocModel.sendData(json);
            } catch (JSONException e) {
                e.printStackTrace();
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
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("result").contains("exist")) {
                    showErrorAlert("Mobile Number Already Registered!!!!....Please Enter Otp!!!!!......");
                    Intent intent = new Intent(Registration.this, ImageActivity.class);
                    intent.putExtra("ids",ids);
                    intent.putExtra("mobile_no",mobileno);
                    intent.putExtra("password",password1);
                    startActivity(intent);
                } else if (jsonObject.getString("result").contains("failure")) {
                    showErrorAlert("Process could not be completed...Please try again");
                    Intent intent = new Intent(Registration.this, Registration.class);
                    startActivity(intent);
                } else {
                    if (jsonObject.getString("result").contains("success")) {
                        ids = jsonObject.getString("id");
                        revisionno = jsonObject.getString("revisionno");
                        Intent intent = new Intent(Registration.this, ImageActivity.class);
                        intent.putExtra("ids",ids);
                        intent.putExtra("mobile_no",mobileno);
                        intent.putExtra("password",password1);
                        intent.putExtra("userName",username);
                        intent.putExtra("emailid",emailid);
                        intent.putExtra("blood_grp",blood_grp);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);
                        intent.putExtra("gender",gender);
                        intent.putExtra("emergy_name",emergy_name);
                        intent.putExtra("emergy_mobileno",emergy_mobileno);
                        intent.putExtra("password1",password1);
                        intent.putExtra("revisionno",revisionno);
                        intent.putExtra("city",city_name);
                        intent.putExtra("city_id",city_id);
                        intent.putExtra("office_id",office_id);
                        intent.putExtra("address",address);
                        intent.putExtra("designation",desig_id);
                        intent.putExtra("dob",dob);
                        intent.putExtra("father_name",father);
                        intent.putExtra("address2",address2);
                        intent.putExtra("address3",address3);
                        startActivity(intent);

                    }
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }



        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Registration.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }



    private boolean checkConnection() {
        boolean isConnected = ConnectivityReciever.isConnected();
        if (isConnected) {
            return true;
        } else {
            return false;

        }
    }
    private Boolean validationSuccess(){
        if(name.getText().toString().equalsIgnoreCase("")){
            name.setError("Cannot be empty");
            return false;
        }

        else if(editText_mobile.getText().toString().equalsIgnoreCase("")){
            editText_mobile.setError("Cannot be empty");
            return false;
        }

        else if(!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
            email.setError("Invalid Email Address");
            return false;
        }
        else if(password.getText().toString().equalsIgnoreCase("")){
            password.setError("Cannot be empty");
            return false;
        }
        else if(editText_confirmpass.getText().toString().equalsIgnoreCase("")){
            editText_confirmpass.setError("Cannot be empty");
            return false;
        }
        else if(edtaddress.getText().toString().equalsIgnoreCase("")){
            edtaddress.setError("Cannot be empty");
            return false;
        }
        else if(edtaddress2.getText().toString().equalsIgnoreCase("")){
            edtaddress2.setError("Cannot be empty");
            return false;
        }
        else if(edtaddress3.getText().toString().equalsIgnoreCase("")){
            edtaddress3.setError("Cannot be empty");
            return false;
        }


        else if(fatherName.getText().toString().equalsIgnoreCase("")){
            fatherName.setError("Cannot be empty");
            return false;
        }
        else if(date_ofbirth.getText().toString().equalsIgnoreCase("")){
            date_ofbirth.setError("Cannot be empty");
            return false;
        }

        return true;
    }
    private Boolean validationSuccessss(){
        if(family_userName.getText().toString().equalsIgnoreCase("")){
            family_userName.setError("Cannot be empty");
            return false;
        }

        else if(family_address.getText().toString().equalsIgnoreCase("")){
            family_address.setError("Cannot be empty");
            return false;
        }

        else if(!family_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
            family_email.setError("Invalid Email Address");
            return false;
        }
        else if(family_no.getText().toString().equalsIgnoreCase("")){
            family_no.setError("Cannot be empty");
            return false;
        }
        else if(family_dob.getText().toString().equalsIgnoreCase("")){
            family_dob.setError("Cannot be empty");
            return false;
        }
        else if(family_fatherName.getText().toString().equalsIgnoreCase("")){
            family_fatherName.setError("Cannot be empty");
            return false;
        }
        else if(family_pass.getText().toString().equalsIgnoreCase("")){
            family_pass.setError("Cannot be empty");
            return false;
        }
        else if(family_member.matches("")){
            familySpinner.setPrompt("Cannot be empty");
            return false;
        }

        return true;
    }

    private void showconnAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        // Setting Dialog Title
        alertDialog.setTitle("Message");
        // Setting Dialog Message
        alertDialog.setMessage("Sorry! Not connected to internet");
        alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }
    public JSONObject getdata() {
        username = name.getText().toString().trim();
        emailid = email.getText().toString();
        mobileno = editText_mobile.getText().toString();
        password1=password.getText().toString();
        emergy_name=editText_Emname.getText().toString();
        emergy_mobileno=textIn.getText().toString();
        address=edtaddress.getText().toString();
        address2=edtaddress2.getText().toString();
        address3=edtaddress3.getText().toString();
        father=fatherName.getText().toString();
        ArrayList<String> familydetails = new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        familydetails=dbTask.getfamilydetails();

        if (familydetails.size() != 0) {
            for (int k = 0; k < familydetails.size(); k++) {
                JSONObject json=new JSONObject();
                String val = familydetails.get(k);
                String name = val.split(",")[0];
                String password = val.split(",")[1];
                String mobileno = val.split(",")[2];
                String gender = val.split(",")[3];
                String bloodgrp = val.split(",")[4];
                String dob = val.split(",")[5];
                String fathername = val.split(",")[6];
                String familymember = val.split(",")[7];
                String userimage = val.split(",")[8];
                String idimage = val.split(",")[9];
                String identitynumber = val.split(",")[10];
                String id_type_id = val.split(",")[11];

                try {
                    filePath1 = userimage;
                    secondimage = idimage;
                }catch (Exception e){
                    e.printStackTrace();

                }

                try {

                    json.put("f_username", name);
                    json.put("f_mobilenumber", mobileno);
                    json.put("f_password", password);
                    json.put("f_gender", gender);
                    json.put("f_dob", dob );
                    json.put("f_bloodgrp", bloodgrp);
                    json.put("f_fathername", fathername);
                    json.put("f_familymember", familymember);
                    json.put("f_identityNo", identitynumber);
                    json.put("f_id_typeid", id_type_id);
                    int i = 1;
                    List list = new ArrayList();
                    list.add(filePath1);
                    list.add(secondimage);

                    Iterator itrr = list.listIterator();
                    while (itrr.hasNext()) {
                        String filePath = (String) itrr.next();
                        bitmap = BitmapFactory.decodeFile(filePath);
                        ByteArrayOutputStream stream;
                        stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        String encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                        String imgname = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                        json.put("byte_arr" + i, encodedString);

                        if (i == 1) {
                            json.put("imgname" +  i, "keyperson");
                        } else if (i == 2) {
                            json.put("imgname" +  i , "id");
                        }
                        i++;

                    }
                    jsonArray.put(json);
                }catch (Exception e){
//[{"f_username":"hi","f_mobilenumber":"8787887778","f_password":"1234","f_gender":"M","f_dob":"O+","f_bloodgrp":"2020-4-24","f_fathername":"hello","f_familymember":"Husband"}]
                }
            }
        }
        Registration.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Registration.this, "Family user found", Toast.LENGTH_SHORT).show();
            }
        });
        /*else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }*/

        JSONObject jsonObject = new JSONObject();
        try {
            if(desig_id ==null){
                desig_id="0";
            }if(office_id==null){
                office_id="0";
            }
            jsonObject.put("familydata",jsonArray);
            jsonObject.put("mobilenumber", mobileno);
            jsonObject.put("gender", gender);
            jsonObject.put("password", password1);
            jsonObject.put("name", username);
            jsonObject.put("email_id",emailid);
            jsonObject.put("blood_group", blood_grp);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("emergency_name",emergy_name );
            jsonObject.put("emergency_number", emergy_mobileno);
            jsonObject.put("city_id", city_id);
            jsonObject.put("addresss", address);
            jsonObject.put("addresss2", address2);
            jsonObject.put("addresss3", address3);
            jsonObject.put("dob", dob);
            jsonObject.put("father_name", father);
            jsonObject.put("designation", desig_id);
            jsonObject.put("office_id", office_id);




        }catch ( JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }



    public void showErrorAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        // Setting Dialog Title
        alertDialog.setTitle("Message");
        // Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    private void getdetails(){
        dbTask.open();
        // userid.add("select");
        cityname.add("select");
        desigName.add("select");
        List<String> desigName=dbTask.getdesignationyname();
        List<String> cityname=dbTask.getcityname();
        dbTask.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cityname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, desigName);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desig_spinner.setAdapter(arrayAdapter2);


    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            btnAddfamily.setVisibility(View.GONE);

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btnAddfamily.setVisibility(View.VISIBLE);
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}




