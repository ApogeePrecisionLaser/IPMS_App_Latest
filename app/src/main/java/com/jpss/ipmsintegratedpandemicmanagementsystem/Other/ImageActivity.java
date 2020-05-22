package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ImageActivity extends AppCompatActivity {
    ImageView userImage, UserIdimg;
    Spinner user_id;
    EditText userIdentityNo;
    Button submit;
    int shweta = 0;
    ArrayList<String> imagelist = new ArrayList<String>();
    static int i = 0;
    File mediaFile;
    public static final int RequestPermissionCode = 7;
    String imagePath = "";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Cable";
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    File dir;
    TextView id, photo;
    public File f;
    String filePath1,secondimage,user_identity;
    String useridentityid,city_id,identityNo;
    List<String>userid=new ArrayList<>();
    List<String>cityname=new ArrayList<>();
    DatabaseOperation dbTask=new DatabaseOperation(this);
    String mobileno, password_registered,username,emailid,blood_group,latitude,longitude,gender,emergency_name,emergency_no,revisionno,city,address,ids;
    String father,dob,officeId,degId,address2,address3;
    ProgressDialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("ID/Image");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userImage = findViewById(R.id.userPhoto);
        UserIdimg = findViewById(R.id.userId);
        id = findViewById(R.id.id);
        photo = findViewById(R.id.photo);
        user_id = findViewById(R.id.idSpinner);
        userIdentityNo = findViewById(R.id.identity_No);
        submit = findViewById(R.id.sendServer);
        Intent intent = getIntent();
        mobileno = intent.getStringExtra("mobile_no");
        ids = intent.getStringExtra("ids");
        password_registered = intent.getStringExtra("password");
        username = intent.getStringExtra("userName");
        emailid = intent.getStringExtra("emailid");
        blood_group = intent.getStringExtra("blood_grp");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        gender = intent.getStringExtra("gender");
        emergency_name = intent.getStringExtra("emergy_name");
        emergency_no = intent.getStringExtra("emergy_mobileno");
        revisionno = intent.getStringExtra("revisionno");
        city = intent.getStringExtra("city");
        city_id = intent.getStringExtra("city_id");
        address = intent.getStringExtra("address");
        address2 = intent.getStringExtra("address2");
        address3 = intent.getStringExtra("address3");
        father = intent.getStringExtra("father_name");
        dob = intent.getStringExtra("dob");
        officeId = intent.getStringExtra("office_id");
        degId = intent.getStringExtra("designation");
        dbTask.open();
        getdetails();
        submit.setEnabled(false);
        user_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("select"))
                    user_identity = "";
                else
                    user_identity = item;
                useridentityid=dbTask.getuserids(user_identity);
                dbTask.close();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(userIdentityNo.getText()!=null) {
            userIdentityNo.setError("Cannot be empty");
            submit.setEnabled(false);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    quarantineidrequest quarantineidrequest = new quarantineidrequest();
                    quarantineidrequest.execute();

            }
        });
    }

    public void imageclick(View v) {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(ImageActivity.this, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
        } else {
            if (checkPermission()) {
                if (shweta > 1) {
                    Toast.makeText(ImageActivity.this, "cancot click more than 2 image", Toast.LENGTH_LONG).show();
                } else {
                    captureImage();

                }

            } else {

                requestPermission();
            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > 23) {

            File file = getCameraFile();
            imagePath = file.getPath();
            fileUri = FileProvider.getUriForFile(ImageActivity.this, ImageActivity.this.getPackageName() + ".provider", file);

        } else {
            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public File getCameraFile() {
        dir = ImageActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + "," + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        imagePath = mediaFile.getAbsolutePath();
        return mediaFile;
        //return mediaStorageDir;
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.CAMERA);
        int result3 = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.INTERNET);
        int result5 = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.ACCESS_NETWORK_STATE);

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ImageActivity.this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, RequestPermissionCode);


//        ActivityCompat.requestPermissions(CameraActivity.this, new
//                String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);


    }

    private boolean isDeviceSupportCamera() {
        if (ImageActivity.this.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //viewImage();
                //viewImage1();
                ImageView imgview = new ImageView(ImageActivity.this);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                imgview.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),""+bitmap, Toast.LENGTH_SHORT).show();
                Toast.makeText(ImageActivity.this, "Image Captured Successfully..", Toast.LENGTH_SHORT).show();
                imgview.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
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
                    userImage.setImageBitmap(bitmap);
                    photo.setText("USER PHOTO");
                    Toast.makeText(ImageActivity.this, " Now Please Capture Your Identity Image", Toast.LENGTH_SHORT).show();


                } else {
                    if (shweta == 1) {
                        imagelist.add(imagePath);
                        UserIdimg.setImageBitmap(bitmap);
                        id.setText("VOTER/AADHAR ID");
                        if(UserIdimg==null)
                        {
                            submit.setEnabled(false);
                            Toast.makeText(this, "Please Select User Id Photo...", Toast.LENGTH_SHORT).show();
                        }else{
                            submit.setEnabled(true);

                        }

                    }
                }
                shweta++;

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(ImageActivity.this, "User cancelled image capture", Toast.LENGTH_SHORT).show();
                //showAlert("Image Cancel");
            } else {
                // failed to capture image
                Toast.makeText(ImageActivity.this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class quarantineidrequest extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(ImageActivity.this);


            String result = new String();
            try {
                try {
                    JSONObject json=getobj();
                    result = prepaidDocModel.sendimagedata(json);
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
                    databaseinsertion();
                    getmobile();


                 } else {
                    if (result.contains("failure")) {
                        dialog.dismiss();
                        Toast.makeText(ImageActivity.this, "Not registered .....Register your self.....", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ImageActivity.this, "", "Proccessing....Please wait");
            dialog.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }


    }
    private JSONObject getobj(){
        JSONObject jsonObject=new JSONObject();

        identityNo=userIdentityNo.getText().toString();

        try {
            filePath1 = imagelist.get(0);
            secondimage = imagelist.get(1);
        }catch (Exception e){

        }



        try {

            jsonObject.put("identityNo", identityNo);
            jsonObject.put("totalImg", "2");
            jsonObject.put("id_type_id",  useridentityid);
            jsonObject.put("id",  ids);
            int i = 1;
            List list = new ArrayList();
            try {
                list.add(filePath1);
                list.add(secondimage);

                Iterator itrr = list.listIterator();
                while (itrr.hasNext()) {
                    String filePath = (String) itrr.next();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    byte[] byte_arr = stream.toByteArray();
                    String encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    String imgname = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                    jsonObject.put("byte_arr" + i, encodedString);

                    if (i == 1) {
                        jsonObject.put("imgname" + i, "keyperson");
                    } else if (i == 2) {
                        jsonObject.put("imgname" + i, "id");
                    }
                    i++;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }
        return jsonObject;
    }
    private void getdetails(){
        DatabaseOperation dbTask=new DatabaseOperation(this);
        dbTask.open();
        userid.add("select");
        List<String> userid=dbTask.getuserid();
        dbTask.close();

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, userid);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_id.setAdapter(arrayAdapter1);

    }
    public void getmobile(){
        Otpverify otpverify=new Otpverify();
        otpverify.execute();
    }
    public class Otpverify extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            //publishProgress("processing...");
            //  String address = dbTask.getServerIp();
            //   String server_ip=address.split("_")[0];
            //  String port=address.split("_")[1];

            GenericModel prepaidDocModel = new GenericModel(ImageActivity.this);

            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    result = prepaidDocModel.getotp(mobileno);
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
                if (result.contains("true")) {
                    Intent intent = new Intent(ImageActivity.this, Otpactivity.class);
                    intent.putExtra("mobile_no",mobileno);
                    intent.putExtra("password", password_registered);
                    intent.putExtra("id",ids);
                    startActivity(intent);
                } else if (result.contains("false")) {
                    Intent intent = new Intent(ImageActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ImageActivity.this, "", "Proccessing....Please wait");
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

    private  void databaseinsertion() {
        /*database insertion*/
        long result=0;

        List<Bean> detail = new ArrayList<Bean>();
        for (int i = 0; i<1;i++) {
            Bean bleBean = new Bean();
            bleBean.setUsername(username);
            bleBean.setEmail(emailid);
            bleBean.setMobileno(mobileno);
            bleBean.setBlood_group(blood_group);
            bleBean.setLatitude(latitude);
            bleBean.setLongitude(longitude);
            bleBean.setGender(gender);
            bleBean.setEmergency_name(emergency_name);
            bleBean.setEmergency_mobilen(emergency_no);
            bleBean.setPassword(password_registered);
            bleBean.setKeypersonid(ids);
            bleBean.setRevision_no(revisionno);
            bleBean.setCityNane(city);
            bleBean.setCityid(city_id);
            bleBean.setAddress(address);
            bleBean.setAddress2(address2);
            bleBean.setAddress3(address3);
            bleBean.setImage_path2(secondimage);
            bleBean.setImage_path1(filePath1);
            bleBean.setId_type_id(useridentityid);
            bleBean.setId_type(user_identity);
            bleBean.setIdentitynumber(identityNo);
            bleBean.setDesigid(degId);
            bleBean.setOfficeid(officeId);
            bleBean.setFathername(father);
            bleBean.setDob(dob);
            detail.add(bleBean);
        }
        dbTask.open();
        result = dbTask.insertimageregistrationdetails(detail);
        if(result > 0){
            Toast.makeText(ImageActivity.this, "Image  inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ImageActivity.this, "Data insertion failed", Toast.LENGTH_SHORT).show();
        }
    }

}