package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import java.util.ArrayList;

public class PersonalDetail extends AppCompatActivity {
    TextView usrnm,mobno,email,gender,location,address,orgtypenm;
    ImageView imageView;
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    Toolbar toolbar;
    DatabaseOperation dbTask = new DatabaseOperation(this);
    public static final String ORGTYPENAME = "orgnm";
    String usnm,mobnoo,lctn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);

        toolbar =  findViewById(R.id.tool);
        toolbar.setTitle("Personal Details");
        toolbar.inflateMenu(R.menu.profieqrmenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.qr:
                        Intent intent = new Intent(PersonalDetail.this,ProfileQrActivity.class);
                        intent.putExtra("Person Name",usnm);
                        intent.putExtra("Contact",mobnoo);
                        intent.putExtra("Location",lctn);
                        startActivity(intent);
                        return true;
                    default:
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        usrnm = findViewById(R.id.prsnlname);
        mobno = findViewById(R.id.numprsnl);
        email = findViewById(R.id.gmail);
        gender = findViewById(R.id.idgender);
        imageView = findViewById(R.id.prsnlimg);
        location = findViewById(R.id.locnprsnl);
        address = findViewById(R.id.prsnlad);
        orgtypenm = findViewById(R.id.idorgtpnm);

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        String num = sharedPreferences.getString(LOGINNUMBER, "default value");
        String orgtpnm = sharedPreferences.getString(ORGTYPENAME, "default value");
        orgtypenm.setText(orgtpnm);
        dbTask.open();
        ArrayList<String> keydetail = new ArrayList<>();
        String kpidandlocation = dbTask.getkpidandlocation(num);
        String keyprsnid = kpidandlocation.split(",")[0];
        keydetail = dbTask.getkeypersonid(keyprsnid);
        if(keydetail.size()!=0){
            for (int k = 0; k < keydetail.size(); k++) {
                String val = keydetail.get(k);
                usnm = val.split(",")[0];
                String emaill = val.split(",")[1];
                mobnoo = val.split(",")[2];
                String genderr = val.split(",")[3];
                String image = val.split(",")[4];
                lctn = val.split(",")[5];
                String addres = val.split(",")[6];


                usrnm.setText(usnm);
                mobno.setText(emaill);
                email.setText(mobnoo);
                gender.setText(genderr);
                imageView.setImageBitmap(BitmapFactory.decodeFile(image));
                location.setText(lctn);
                address.setText(addres);



            }
        }else{
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }

        dbTask.close();
    }
}
