package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;

public class qurantinedetails extends AppCompatActivity {
    TextView usrnm, quar_id, reason_id, gender, location, latitude,longitude,day,date_time,reasondec,creadteddate,remark;
    ImageView imageView;
    public static final String LOGINNUMBER = "loginnumber";
    DatabaseOperation dbTask = new DatabaseOperation(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurantinedetails);
        usrnm = findViewById(R.id.username);
        quar_id = findViewById(R.id.quar_id);
        reason_id = findViewById(R.id.resid);
        gender = findViewById(R.id.idgender);
        imageView = findViewById(R.id.qrimg);
        location = findViewById(R.id.locnprsnl);
        reasondec = findViewById(R.id.reasondesc);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        day = findViewById(R.id.day);
        date_time = findViewById(R.id.datetime);
        remark = findViewById(R.id.remark);
//
//        final SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(this);
//
//        String num = sharedPreferences.getString(LOGINNUMBER, "default value");
//quarantime_id,reason_id,reason_desc,created_date,latitude,longitude,image_path,day,date_time,remark
        dbTask.open();
        ArrayList<String> Details = new ArrayList<>();
        Details = dbTask.getquarantinedata();
        if (Details.size() != 0) {
            for (int k = 0; k < Details.size(); k++) {
                String val = Details.get(k);
                String quarn_id = val.split(",")[0];
                String reasonid = val.split(",")[1];
                String reasondesc = val.split(",")[2];
                String created_date = val.split(",")[3];
                String latitude1 = val.split(",")[4];
                String longitude1 = val.split(",")[5];
                String image_path = val.split(",")[6];
                String day1 = val.split(",")[7];
                String datetime1 = val.split(",")[8];
                //    String remarks = val.split(",")[9];


                quar_id.setText(quarn_id);
                reason_id.setText(reasonid);
                reasondec.setText(reasondesc);
                //   gender.setText(created_date);
                imageView.setImageBitmap(BitmapFactory.decodeFile(image_path));
                longitude.setText(longitude1);
                latitude.setText(latitude1);
                day.setText(day1);
                date_time.setText(datetime1);
                // remark.setText(remarks);
                Toast.makeText(this, "Quarantine person record", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }

        dbTask.close();
    }
}



