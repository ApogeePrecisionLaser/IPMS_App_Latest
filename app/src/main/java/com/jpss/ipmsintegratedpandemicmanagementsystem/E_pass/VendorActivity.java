package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VendorActivity extends AppCompatActivity {
    JSONObject json1 = new  JSONObject();
    private boolean zoomOut =  false;

    /*Image part*/
    LinearLayout imagelayout;
    File mediaFile;
    String imagePath = "";
    private Uri fileUri;
    public File f;
    String path = "";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ArrayList<String> imagelist = new ArrayList<String>();
    private static final String IMAGE_DIRECTORY_NAME = "Epass";
    public static final int MEDIA_TYPE_VIDEO = 2;
    int counter = 1;
    File dir;


    ImageView imgv;
    TextView cstnm,fnlstts;


     String dvbynm="";
             String odstt,id;
     String orgofcnm,orgofid;

    Spinner ordrstts;
    Spinner dlvryboylisttr;
    DatabaseOperation dbtask = new DatabaseOperation(this);
    Toolbar toolbar;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String ORGOFCID = "orgofcid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        imgv=findViewById(R.id.vndrimg);
        fnlstts=findViewById(R.id.fnlstts);
        cstnm=findViewById(R.id.cstnm);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Vendor");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imagelayout = (LinearLayout) findViewById(R.id.imgLayout);
        ordrstts = findViewById(R.id.ordrsttsid);
        dlvryboylisttr = findViewById(R.id.iddlvrby);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String numm = sharedPreferences.getString(LOGINNUMBER, "default value");
        String orgofcid = sharedPreferences.getString(ORGOFCID, "default value");
        ArrayList<String> keydetail = new ArrayList<>();
        dbtask.open();
        String kpidandlocation = dbtask.getkpidandlocation(numm);
        String keyprsnid = kpidandlocation.split(",")[0];
        String orgnmid = dbtask.getorgnmid(orgofcid);
        if(orgnmid!=null){
            orgofid = orgnmid.split(",")[0];
            orgofcnm = orgnmid.split(",")[1];
        }

       ArrayList ordrsttslist = new ArrayList<>();
       ArrayList dlvryboylist = new ArrayList<>();
        ArrayList<String> vendordbist = new ArrayList<>();


            Intent intent = getIntent();
            id = intent.getStringExtra("values");

        ordrsttslist = dbtask.getorderstatuslist();
        dlvryboylist = dbtask.getdlvryboylist();
        vendordbist = dbtask.getorderbyid(id);
        if(vendordbist.size()!=0){
            for (int k = 0; k < vendordbist.size(); k++) {
                String val = vendordbist.get(k);
                String ordrimg = val.split(",")[0];
                String mobnoo = val.split(",")[1];
                String fnstts = val.split(",")[2];



                fnlstts.setText(fnstts);
                cstnm.setText(mobnoo);
                imgv.setImageBitmap(BitmapFactory.decodeFile(ordrimg));
            }
        }else{
            Toast.makeText(this, "No any order found", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter<String> Adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ordrsttslist);
        Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ordrstts.setAdapter(Adapter1);

        ordrstts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              odstt = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final ArrayAdapter<String> Adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dlvryboylist);
        Adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dlvryboylisttr.setAdapter(Adapter2);

        dlvryboylisttr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dvbynm = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoomOut) {
                    Toast.makeText(getApplicationContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                    imgv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imgv.setAdjustViewBounds(true);
                    zoomOut =false;
                }else{
                    Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
                    imgv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imgv.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut = true;
                }
            }
        });
    }

    public void sendto(View v) {
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

               /* locidval =  locid.getText().toString();
                keyprsnid =  keyprsnidd.getText().toString();*/
                try {

                    for (int i = 0; i < imagelist.size(); i++) {
                        // path = mediaStorageDir + "/" + file2.getName();
                        path = imagelist.get(i);
                        int count = i + 1;
                        //  json2 = uploadImg(mediaStorageDir + "/" + file2.getName());
                        //  for (int i = 1; i <= imagelist.size(); i++) {
                        //   String path = (String) imagelist.get(i-1);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        //String img = "IMG_" + i;
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        String imgname = "IMG_" + "," + timeStamp + ".jpg";
                        String encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                        json1.put("byte_arr", encodedString);
                        json1.put("imgname", imgname);
//                            i=i+1;
                    }
                   String orgnm  = dbtask.getorgnm(orgofid);

                    json1.put("order_status",odstt);
                    json1.put("org_nm",orgnm);
                    json1.put("dlvryboynm",dvbynm);
                    json1.put("order_id",id);
                    json1.put("remark","Abhi");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GenericModel genericModel = new GenericModel(VendorActivity.this);
                json2 = genericModel.sendorderdata(json1);
                result =  json2.get("success").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            if (result.equalsIgnoreCase("updated") )
            {
                Toast.makeText(VendorActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(VendorActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            dialog.cancel();


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(VendorActivity.this, "Sending your details to the server", "Please wait ....");
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

    public void imgbtn(View v) {
        captureImage();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > 23) {
            File file = getCameraFile();
            imagePath = file.getPath();
            // fileUri = FileProvider.getUriForFile(GenericActivity.this, getApplicationContext().getPackageName() + ".provider", file);
            fileUri = FileProvider.getUriForFile(VendorActivity.this, getApplicationContext().getPackageName() + ".provider", file);

        } else {
            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public File getCameraFile() {
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //  dir = new File(Environment.getExternalStorageDirectory() + "/DirName");

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());

        return new File(dir, "IMG_" + "" + timeStamp + ".jpg");
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), IMAGE_DIRECTORY_NAME );
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
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());

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


    @SuppressLint("LongLogTag")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView imgview = new ImageView(this);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //viewImage();
                //viewImage1();
                imagelayout.setVisibility(View.VISIBLE);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                imgview.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),""+bitmap, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Image Captured Successfully..", Toast.LENGTH_SHORT).show();
                imgview.setImageBitmap(bitmap);
                imgview.requestFocus();
                imgview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                imagelayout.addView(imgview);
                imagelist.add(imagePath);

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                //showAlert("Image Cancel");
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            Uri selectedImage = data.getData();
            // h=1;
            //imgui = selectedImage;
            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePath[0]);

            path = c.getString(columnIndex);

            c.close();

            Bitmap thumbnail = (BitmapFactory.decodeFile(path));


            Log.w("path of image from gallery......******************.........", path + "");
            imagelayout.setVisibility(View.VISIBLE);
            imgview.setImageBitmap(thumbnail);
            imagelayout.addView(imgview);
            imagelist.add(imagePath);


        }
    }
   }
