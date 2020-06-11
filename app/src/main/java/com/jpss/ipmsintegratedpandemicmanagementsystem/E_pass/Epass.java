package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Epass extends AppCompatActivity {
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
    String item = "";
    String subitem = "";
    Toolbar toolbar;
    int val=0;
    private static final String IMAGE_DIRECTORY = "/IPMS/Epass";
    Bitmap bitmap ;
    Spinner rsnspnr,sbwrktyp;
    Button btn;
    ArrayList<String> rsnspnrlist;
    DatePickerDialog picker;
    TextView usernm,locat,mob,mail;
    TextView frmdt,frmtm;
    TextView todt,totm;
    JSONObject json1 = new  JSONObject();
    String todat,frmdat,frmtime,totim,rsn,locidval,keyprsnid,paths,wid,num,worktypeid;
    DatabaseOperation dbtask = new DatabaseOperation(this);
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    public static final String FMLYOFCID = "fmlyofcid";
    public static final String ORGOFCID = "orgofcid";
    GenericModel genericModel = new GenericModel(Epass.this);
    private ArrayAdapter<String> arrayAdapter4;
    private ArrayList<String> list3;
    List<String> suboprtnlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epass);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle(getString(R.string.E_pass));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rsnspnr = findViewById(R.id.rsnspnr);
        sbwrktyp = findViewById(R.id.sbwrktyp);
        frmdt = findViewById(R.id.frmdt);
        todt = findViewById(R.id.todt);
        frmtm = findViewById(R.id.frmtm);
        totm = findViewById(R.id.totm);
        usernm = findViewById(R.id.usrnm);
        locat = findViewById(R.id.loc);
        mob = findViewById(R.id.mobno);
        mail = findViewById(R.id.gmail);
        btn = findViewById(R.id.rqstt);
        list3 = new ArrayList<>();
        list3.add("--select--");
        arrayAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);
        imagelayout = (LinearLayout) findViewById(R.id.imgLayout);

        dbtask.open();
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        num = sharedPreferences.getString(LOGINNUMBER, "default value");
        String kpid = sharedPreferences.getString(LOGINKPID, "default value");
        String orgofcid = sharedPreferences.getString(ORGOFCID, "0");
        String fmlyofcid = sharedPreferences.getString(FMLYOFCID, "0");
        if(!orgofcid.equalsIgnoreCase("0")) {
          val =   dbtask.getorgtypeid(orgofcid);
        }else{
            val =   dbtask.getorgtypeid(fmlyofcid);
        }

       String kpidandlocation = dbtask.getkpidandlocation(num);
       if(kpidandlocation!=null){
           keyprsnid = kpidandlocation.split(",")[0];
           locidval = kpidandlocation.split(",")[1];
           locat.setText(locidval);
           String username = kpidandlocation.split(",")[2];
           usernm.setText(username);
           String email = kpidandlocation.split(",")[3];
           mail.setText(email);
           String mobileno = kpidandlocation.split(",")[4];
           mob.setText(mobileno);
       }else{
           Toast.makeText(this, "User Data not found", Toast.LENGTH_SHORT).show();
       }


        genericModel.requestLocationData(locidval);


        rsnspnrlist = new ArrayList<>();
        rsnspnrlist = dbtask.getworktype(val);

        final ArrayAdapter<String> Adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rsnspnrlist);
        Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rsnspnr.setAdapter(Adapter1);

        rsnspnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                if(!item.equalsIgnoreCase("--select--")){
                    dbtask.open();
                    int opd = dbtask.wrktyid(item);
                    superchildvalidation(item, opd);
                }

                // worktypeid =    dbtask.getworktypeid(rsn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> suboperationAdapter = (arrayAdapter4);
        suboperationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sbwrktyp.setAdapter(suboperationAdapter);
        sbwrktyp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   subitem = parent.getItemAtPosition(position).toString();
                   if(!subitem.equalsIgnoreCase("--select--")){
                       int subopid = dbtask.wrktyid(subitem);
                       superchildvalidation(subitem, subopid);
                   }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        frmdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Epass.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                frmdt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                frmdat = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                frmdt.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });



        todt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Epass.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                todt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                todat = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                todt.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        frmtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
               int mHour = c.get(Calendar.HOUR_OF_DAY);
              int  mMinute = c.get(Calendar.MINUTE);
              final int  AM_pm = c.get(Calendar.AM_PM);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Epass.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                frmtime = String.valueOf(hourOfDay+":"+minute+":"+"00");
                                frmtm.setText(frmtime);
                                frmtm.setTextColor(Color.parseColor("#4CAF50"));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });



        totm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int  mMinute = c.get(Calendar.MINUTE);
                final int  AM_pm = c.get(Calendar.AM_PM);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Epass.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                totim = String.valueOf(hourOfDay+":"+minute+":"+"00");
                                totm.setText(totim);
                                totm.setTextColor(Color.parseColor("#4CAF50"));

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });




    }

    public void superchildvalidation(String item, int opd) {
        dbtask.open();
        if(!item.equalsIgnoreCase("--select--")){
            String issuperchild = dbtask.issuperchild(item);
            suboprtnlist = new ArrayList<>();
            if (issuperchild.equalsIgnoreCase("N")) {
                suboprtnlist = dbtask.getwrknm(opd);
                updateAdapter4(suboprtnlist);
                Toast.makeText(this, "Select one more time", Toast.LENGTH_SHORT).show();
            } else if (issuperchild.equalsIgnoreCase("Y")) {
                worktypeid =    dbtask.getworktypeid(item);
                // opid = dbtask.superchildid(item);
                Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void epass(String base){
     //   String base = "iVBORw0KGgoAAAANSUhEUgAAAH0AAAB9CAIAAAAA4vtyAAABfklEQVR42u3byQ3DMAwEQPffdNKCA3KpI7NPwxCkYQBFh5+PrMiDgDt34c5duHMX7tyFO3fhzp27cOcu3LkL9/9yfwJ50/6b/kz2kzt37txvdk+30+W+w3i5c+fO/VT3yvy56nmln9y5c+fOvccrMd9y586dO/c17pW9Ku7cuXPnvu++2GX95M6dO/dj3NPn2un9L/cJuHPnzn1NBixGh8OdO3fuO7tP7ltV5s+uWp76f4Y7d+7cd6jBr+OsWHTVjzt37txvdk+vfSbn2IEacOfOnfsx7pNnCF3rmvT73Llz536ze9f7XXNg+hsH7ty5c7/NfYd1Fnfu3Llz73Hf8L5nSz8nflLcuXPnfor7ZDtpr5Pui3Hnzp37pHv6vmdi3TRZV+7cuXP/d/fKeqdrn4s7d+7cuWfdK3PgQl/u3Llzv8p9sn6V2px6L5U7d+7cJ90T59qJ5wNnGty5c+d+vLtw5y7cuQt37tyFO3fhzl24cxfu3IU7d+4IuHOXcL6oB7UJwG7KUQAAAABJRU5ErkJggg==";
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
        bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        try {
            paths = saveImage(bitmap);  //give read write permission

            Toast.makeText(Epass.this, "Epass saved to -> "+paths, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.epassmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.iddtl:
                return true;
            case R.id.idprsnl:
                Intent intent = new Intent(Epass.this,PersonalDetail.class);
                startActivity(intent);
                return true;
            case R.id.oflnpss:
                return true;
            case R.id.epasinfo:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void request(View view){
        dbtask.open();
        ArrayList<String> tlist = new ArrayList<String>();
        String kpidandlocation = dbtask.getkpidandlocation(num);
        String keyprsnid = kpidandlocation.split(",")[0];
        tlist = dbtask.getEpassInfolist(keyprsnid);

        if(tlist.size()!=0){
            for (int k = 0; k < tlist.size(); k++) {
                String val = tlist.get(k);
                String location = val.split("_")[0];
                String validfrom = val.split("_")[1];
                if(locidval.equalsIgnoreCase(location)&& validfrom.contains(frmdat)){
                    epassalert(locidval,frmdat);
                }else{
                    Callingserver cs=new Callingserver();
                    cs.execute();
                }

            }
        }else{
            Toast.makeText(this, "No any E_pass Detected!", Toast.LENGTH_SHORT).show();
            Callingserver cs=new Callingserver();
            cs.execute();
        }

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
                        json1.put("totalImg", count);
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
                        json1.put("byte_arr" + count, encodedString);
                        json1.put("imgname" + count, imgname);

//                            i=i+1;
                    }

                    json1.put("work_type",worktypeid);
                    json1.put("loc_id",locidval);
                    json1.put("valid_from",frmdat+" "+frmtime);
                    json1.put("valid_to",todat+" "+totim);
                    json1.put("key_person_id",keyprsnid);
                    json1.put("remark","Abhijeet");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json2 = genericModel.requestEpass(json1);
                result =  json2.get("result").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            if (result.equalsIgnoreCase("success") )
            {
                try {
                   String stts = json2.getString("status");
                    btn.setText(stts);
                    Toast.makeText(Epass.this, stts, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else
                Toast.makeText(Epass.this, "Request not Approved/Something went wrong", Toast.LENGTH_SHORT).show();
                dialog.cancel();


        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Epass.this, "Generating your E-pass", "Please wait ....");
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
            fileUri = FileProvider.getUriForFile(Epass.this, getApplicationContext().getPackageName() + ".provider", file);

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

    public void epassalert(String location , String fromdate) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Epass.this);
        builder1.setTitle("About your Epass");
        builder1.setMessage("E_pass already generated at this date="+fromdate+ "and location=" +
                location);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Try another one",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });





        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void updateAdapter4(List<String> singleAddressy) {
        arrayAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, singleAddressy);
        sbwrktyp.setAdapter(arrayAdapter4);
    }
}
