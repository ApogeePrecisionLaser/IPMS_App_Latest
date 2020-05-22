package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;

public class Iddetails extends AppCompatActivity {

    TextView usrnm,mobno,email,idnum,typofid,address;
    ImageView imageView;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    Toolbar toolbar;

    DatabaseOperation dbTask = new DatabaseOperation(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iddetails);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Id details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        usrnm = findViewById(R.id.nm);
        mobno = findViewById(R.id.nump);
        email = findViewById(R.id.eml);
        idnum = findViewById(R.id.idnmbr);
        imageView = findViewById(R.id.idimg);
        typofid = findViewById(R.id.typofid);
        address = findViewById(R.id.snlad);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String num = sharedPreferences.getString(LOGINNUMBER, "default value");

        dbTask.open();
        ArrayList<String> keydetail = new ArrayList<>();
        String kpidandlocation = dbTask.getkpidandlocation(num);
        String keyprsnid = kpidandlocation.split(",")[0];
        keydetail = dbTask.getkeypersonid(keyprsnid);
        try{
            if(keydetail.size()!=0){
                for (int k = 0; k < keydetail.size(); k++) {
                    String val = keydetail.get(k);
                    String usnm = val.split(",")[0];
                    String emaill = val.split(",")[1];
                    String mobnoo = val.split(",")[2];
                    String addres = val.split(",")[6];
                    String idtype = val.split(",")[7];
                    String idnumm = val.split(",")[8];
                    String imageid = val.split(",")[9];


                    usrnm.setText(usnm);
                    mobno.setText(mobnoo);
                    email.setText(emaill);
                    idnum.setText(idnumm);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imageid));
                    typofid.setText(idtype);
                    address.setText(addres);



                }
            }else{
                Toast.makeText(this, "No ID found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        dbTask.close();
    }
}
