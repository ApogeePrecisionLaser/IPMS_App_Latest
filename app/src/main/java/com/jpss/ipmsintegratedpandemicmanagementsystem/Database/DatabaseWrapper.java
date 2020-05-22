package com.jpss.ipmsintegratedpandemicmanagementsystem.Database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseWrapper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseWrapper.java";
    private static final String DATABASE_NAME = "imps.db";
    private static final int DATABASE_VERSION = 1;
    private static final SQLiteDatabase.CursorFactory CURSOR_FACTORY = null;
    Context context;
    /* BY SANDEEP*/
    private static final String organisationtypequery = " CREATE TABLE organisation_type ("
            + " organisation_type_id  INTEGER PRIMARY KEY autoincrement,"
            + " org_type_name TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";
    /* BY SANDEEP*/
    private static final String organisationnamequery = " CREATE TABLE organisation_name ("
            + " organisation_id  INTEGER PRIMARY KEY autoincrement,"
            + " organisation_name TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " organisation_sub_type_id INTEGER DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";
    /* BY SANDEEP*/
    private static final String organisationsubtypequery = " CREATE TABLE organisation_sub_type ("
            + " organisation_sub_type_id  INTEGER PRIMARY KEY autoincrement,"
            + " organisation_sub_type_name TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " organisation_type_id INTEGER DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";
    /* BY SANDEEP*/
    private static final String organisationofficetypequery = " CREATE TABLE org_office_type ("
            + " office_typeid  INTEGER PRIMARY KEY autoincrement,"
            + " office_type TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " description TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";
    /* BY SANDEEP*/
    private static final String organisationofficequery = " CREATE TABLE org_office ("
            + " org_office_id  INTEGER PRIMARY KEY autoincrement,"
            + " org_office_name TEXT DEFAULT NULL,"
            + " address TEXT DEFAULT NULL,"
            + " mobile_no TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " description TEXT DEFAULT NULL,"
            + " latitude TEXT DEFAULT NULL,"
            + " longitude TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + " org_office_code TEXT DEFAULT NULL,"
            + " organisation_id INTEGER DEFAULT NULL,"
            + " type TEXT DEFAULT NULL,"
            + " office_type_id INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";



/*-----  Somya Table Starts ----*/
    private static final String quarantinequery = " CREATE TABLE quarantime ( "
            + "  id INTEGER PRIMARY KEY , "
            + "  reason_id TEXTDEFAULT DEFAULT NULL,"
            + "  key_person_id INTEGER DEFAULT NULL,"
            + "  reason_desc TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  latitude TEXT DEFAULT NULL, "
            + "  longitude TEXT DEFAULT NULL,"
            + "  image_path TEXT DEFAULT NULL,"
            + "  day TEXT DEFAULT NULL,"
            + "  date_time TEXT DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + " FOREIGN KEY (key_person_id) REFERENCES keyperson(key_person_id)," +
            " FOREIGN KEY (reason_id) REFERENCES reason(reason_id)); ";

    private static final String quarantinereasonquery = " CREATE TABLE quarantinereason ( "
            + "  reason_id INTEGER PRIMARY KEY autoincrement, "
            + "  reason_desc TEXT DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  created_by TEXT DEFAULT NULL" +
            "); ";


      private static final String medicalrecordquery = " CREATE TABLE medicalrecord ( "
            + "  med_rec_id INTEGER PRIMARY KEY autoincrement, "
            + "  temprature TEXT DEFAULT NULL ,"
            + "  quarantime_id INTEGER DEFAULT NULL,"
            + "  BP TEXT DEFAULT NULL,"
              + "  Timestamp TEXT DEFAULT NULL,"
              + " FOREIGN KEY (quarantime_id) REFERENCES quarantime(quarantime_id)" +
            "); ";

    private static final String survelliancequery = " CREATE TABLE survelliance ( "
            + "  sur_id INTEGER PRIMARY KEY autoincrement, "
            + "  image_path TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  result TEXT DEFAULT NULL,"
            + "  actioon INTEGER DEFAULT NULL, "
            + "  remark TEXT DEFAULT NULL,"
            + "  latitude TEXT DEFAULT NULL,"
            + "  longitude TEXT DEFAULT NULL,"
            + "  sync_server TEXT NOT NULL DEFAULT 'N'); ";


    private static final String symptomsquery = " CREATE TABLE symptoms( "
            + "  symptoms_id INTEGER PRIMARY KEY autoincrement, "
            + "  symptoms_desc TEXT DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  created_by TEXT DEFAULT NULL " +
            ");";

    private static final String medical_record_symptoms_mapping = " CREATE TABLE medical_record_symptoms_mapping ( "
            + "  mrsm_id INTEGER PRIMARY KEY autoincrement, "
            + "  symptoms_id INTEGER DEFAULT NULL,"
            + "  med_rec_id INTEGER DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + " FOREIGN KEY (symptoms_id) REFERENCES symptoms(symptoms_id)," +
            " FOREIGN KEY (med_rec_id) REFERENCES medicalrecord(med_rec_id)" +
            "); ";
    /* BY SANDEEP*/
 private static final String corona_contact_listquery = " CREATE TABLE corona_contact_list ("
            + " corona_contact_id  INTEGER PRIMARY KEY ,"
            + " quarantine_id TEXT DEFAULT NULL,"
            + " name TEXT DEFAULT NULL,"
            + " mobile TEXT DEFAULT NULL,"
            + " contacted TEXT  NOT NULL DEFAULT 'Y',"
            + " created_date TEXT DEFAULT NULL,"
            + " created_by TEXT DEFAULT NULL,"
            + " remark TEXT DEFAULT NULL,"
            + " point_of_contact_id TEXT DEFAULT NULL,"
            + " datetime TEXT DEFAULT NULL,"
            + " latitude TEXT DEFAULT NULL,"
            + " longitude TEXT DEFAULT NULL,"
            + " keypersonid TEXT DEFAULT NULL"
            + ");";

    private static final String point_of_contactquery = " CREATE TABLE point_of_contacted ("
            + " point_of_contact_id  INTEGER PRIMARY KEY ,"
            + " point_of_contact TEXT DEFAULT NULL,"
            + " datetime TEXT DEFAULT NULL,"
            + " created_date TEXT DEFAULT NULL,"
            + " created_by TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL"
            + ");";
    /*--------------------------------  Somya Table Ends------------------*/
    private static final String registrationquery = " CREATE TABLE regisration ("
            + " key_person_id  INTEGER PRIMARY KEY,"
            + " username TEXT DEFAULT NULL,"
            + " latitude TEXT DEFAULT NULL,"
            + " longitude TEXT DEFAULT NULL ,"
            + " image_path1 TEXT DEFAULT NULL,"
            + " image_path2 TEXT DEFAULT NULL,"
            + " email TEXT DEFAULT NULL,"
            + " mobileno TEXT DEFAULT NULL,"
            + " emergency_name TEXT DEFAULT NULL,"
            + " emergency_mobilen TEXT DEFAULT NULL,"
            + " gender TEXT DEFAULT NULL,"
            + " blood_group TEXT DEFAULT NULL,"
            + " password TEXT DEFAULT NULL,"
            + " designation TEXT DEFAULT NULL,"
            + " org_office_id INTEGER DEFAULT NULL,"
            + " city_id INTEGER DEFAULT NULL,"
            + " city_name TEXT DEFAULT NULL,"
            + " address_line_1 TEXT DEFAULT NULL,"
            + " address_line_2 TEXT DEFAULT NULL ,"
            + " address_line_3 TEXT DEFAULT NULL,"
            + " landline_no1 TEXT DEFAULT NULL,"
            + " landline_no2 TEXT DEFAULT NULL,"
            + " email_id2 TEXT DEFAULT NULL,"
            + " salutaion TEXT DEFAULT NULL,"
            + " designation_id INTEGER DEFAULT NULL,"
            + " emp_code INTEGER DEFAULT NULL,"
            + " general_image_details_id TEXT DEFAULT NULL,"
            + " is_home TEXT DEFAULT NULL,"
            + " fathername TEXT DEFAULT NULL,"
            + " Date_of_birth TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " id_type_id INTEGER DEFAULT NULL,"
            + " family_office INTEGER DEFAULT NULL,"
            + " family_designation INTEGER DEFAULT NULL,"
            + " relation TEXT DEFAULT NULL,"
            + " id_no TEXT DEFAULT NULL,"
            + " identitynumber TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL"
            + ");";

    /* BY SANDEEP START*/
    private static final String idquery = " CREATE TABLE USERID( "
            + "  id_type_id INTEGER PRIMARY KEY autoincrement , "
            + "  id_type TEXT DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  created_by TEXT DEFAULT NULL " +
            ");";
    /* BY SANDEEP END*/
    /* BY SANDEEP START*/
    private static final String regCityLocation = " CREATE TABLE City( "
            + "  cityType_id INTEGER PRIMARY KEY autoincrement,"
            + "  city_name TEXT DEFAULT NULL,"
            + "  remark TEXT DEFAULT NULL,"
            + "  created_date TEXT DEFAULT NULL,"
            + "  created_by TEXT DEFAULT NULL " +
            ");";
    /* BY SANDEEP END*/
    private static final String citylocationquery = " CREATE TABLE City_location ("
            + " citylocation_id  INTEGER PRIMARY KEY autoincrement,"
            + " location TEXT DEFAULT NULL,"
            + " location_no TEXT DEFAULT NULL,"
            + " latitude TEXT DEFAULT NULL,"
            + " longitude TEXT DEFAULT NULL,"
            + " area_id INTEGER DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (area_id) REFERENCES Area (area_id)"
            + ");";

    private static final String worktypequery = " CREATE TABLE Work_type ("
            + " work_type_id  INTEGER PRIMARY KEY autoincrement,"
            + " organisationtype_id INTEGER DEFAULT NULL,"
            + " work_code TEXT DEFAULT NULL,"
            + " parent_id TEXT DEFAULT NULL,"
            + " is_super_child TEXT DEFAULT NULL,"
            + " generation TEXT DEFAULT NULL,"
            + " remark TEXT DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + " FOREIGN KEY (organisationtype_id) REFERENCES organisation_type(organisation_type_id)"
            + ");";

    private static final String epassquery = " CREATE TABLE E_pass ("
            + " e_pass_id  INTEGER PRIMARY KEY autoincrement,"
            + " key_person_id INTEGER DEFAULT NULL,"
            + " loc_id TEXT DEFAULT NULL,"
            + " valid_from_DATETIME TEXT DEFAULT NULL,"
            + " valid_from_location TEXT DEFAULT NULL,"
            + " valid_to_DATETIME TEXT DEFAULT NULL,"
            + " valid_to_location TEXT DEFAULT NULL,"
            + " work_type_id INTEGER DEFAULT NULL,"
            + " delievery_person TEXT DEFAULT NULL,"
            + " qrpath TEXT DEFAULT NULL,"
            + " status_id INTEGER DEFAULT NULL,"
            + " order_mgmt_id INTEGER DEFAULT NULL,"
            + " mobile_no1 TEXT DEFAULT NULL,"
            + " android_image_path TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (work_type_id) REFERENCES Work_type (work_type_id),"
            + "  FOREIGN KEY (key_person_id) REFERENCES Key_person (key_person_id),"
            + "  FOREIGN KEY (status_id) REFERENCES status (status_id)"
            + ");";


    private static final String statusquery = " CREATE TABLE status ("
            + " status_id  INTEGER PRIMARY KEY autoincrement,"
            + " status TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";



    private static final String ordermanagementquery = " CREATE TABLE order_management ("
            + " order_mngmnt_id  INTEGER PRIMARY KEY autoincrement,"
            + " office_id_1 INTEGER DEFAULT NULL,"
            + " delivery_person_id INTEGER DEFAULT NULL,"
            + " order_id INTEGER DEFAULT NULL,"
            + " e_pass_id INTEGER DEFAULT NULL,"
            + " office_id_2 INTEGER DEFAULT NULL,"
            + " office_id_3 INTEGER DEFAULT NULL,"
            + " office_id_4 INTEGER DEFAULT NULL,"
            + " office_id_5 INTEGER DEFAULT NULL,"
            + " order_image_path TEXT DEFAULT NULL,"
            + " mobno TEXT DEFAULT NULL,"
            + " finalstatus TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (order_id) REFERENCES order_status (order_id),"
            + "  FOREIGN KEY (e_pass_id) REFERENCES E_pass (e_pass_id)"
            + ");";

    private static final String orderstatusquery = " CREATE TABLE order_status ("
            + " order_id  INTEGER PRIMARY KEY autoincrement,"
            + " order_status TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String zonequery = " CREATE TABLE Zone ("
            + " Zone_id  INTEGER PRIMARY KEY autoincrement,"
            + " Zone_name TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " zone_no TEXT DEFAULT NULL,"
            + " city_id INTEGER DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (city_id) REFERENCES City (city_id)"
            + ");";

    private static final String wardquery = " CREATE TABLE Ward ("
            + " ward_id  INTEGER PRIMARY KEY autoincrement,"
            + " ward_name TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " ward_no TEXT DEFAULT NULL,"
            + " Zone_id INTEGER DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (Zone_id) REFERENCES Zone (Zone_id)"
            + ");";

    private static final String areaquery = " CREATE TABLE Area ("
            + " area_id  INTEGER PRIMARY KEY autoincrement,"
            + " area_name TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " area_no TEXT DEFAULT NULL,"
            + " ward_id INTEGER DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (ward_id) REFERENCES Ward (ward_id)"
            + ");";
    /* BY SANDEEP START*/
    private static final String designationquery = " CREATE TABLE designation ("
            + " designation_id  INTEGER PRIMARY KEY autoincrement,"
            + " designation TEXT DEFAULT NULL,"
            + " description TEXT DEFAULT NULL,"
            + " designation_code TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP"
            + ");";
    /* BY SANDEEP END*/
   
    /* BY SANDEEP*/
    private static final String family_tablequery = " CREATE TABLE family ("
            + " family_id  INTEGER PRIMARY KEY autoincrement,"
            + " username TEXT DEFAULT NULL,"
            + " password TEXT DEFAULT NULL,"
            + " mobileno TEXT DEFAULT NULL,"
            + " image_path1 TEXT DEFAULT NULL,"
            + " image_path2 TEXT DEFAULT NULL,"
            + " gender TEXT DEFAULT NULL,"
            + " bloodgroup TEXT DEFAULT NULL,"
            + " dob TEXT DEFAULT NULL,"
            + " key_person_id TEXT DEFAULT NULL,"
            + " fathername TEXT DEFAULT NULL,"
            + " familymember TEXT DEFAULT NULL,"
            + " active TEXT  NOT NULL DEFAULT 'Y',"
            + " remark TEXT DEFAULT NULL,"
            + " identitynumber TEXT DEFAULT NULL,"
            + " id_type_id INTEGER DEFAULT NULL,"
            + " id_no TEXT DEFAULT NULL,"
            + " revision_no INTEGER DEFAULT NULL,"
            + "  timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + "  FOREIGN KEY (key_person_id) REFERENCES registration (key_person_id)"
            + ");";

    public DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, CURSOR_FACTORY, DATABASE_VERSION);
       this. context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        {
            boolean result = false;
            try {
                File dbFile = new ContextWrapper(context).getDatabasePath(DATABASE_NAME);
                String path = dbFile.getAbsolutePath();
                Log.d("Info", "db path : " + dbFile.getAbsolutePath());
                try {
//                                db.execSQL(prescriptionQuery);
                    db.execSQL(organisationtypequery);
                    db.execSQL(worktypequery);
                    db.execSQL(epassquery);
                    db.execSQL(quarantinequery);
                    db.execSQL(quarantinereasonquery);
                    db.execSQL(medical_record_symptoms_mapping);
                    db.execSQL(survelliancequery);
                    db.execSQL(medicalrecordquery);
                    db.execSQL(registrationquery);
                    db.execSQL(symptomsquery);
                    db.execSQL(idquery);
                    db.execSQL(citylocationquery);
                    db.execSQL(wardquery);
                    db.execSQL(areaquery);
                    db.execSQL(zonequery);
                    db.execSQL(regCityLocation);
                    db.execSQL(organisationnamequery);
                    db.execSQL(organisationsubtypequery);
                    db.execSQL(organisationofficetypequery);
                    db.execSQL(orderstatusquery);
                    db.execSQL(statusquery);
                    db.execSQL(ordermanagementquery);
                    db.execSQL(organisationofficequery);
                    db.execSQL(designationquery);
                    db.execSQL(corona_contact_listquery);
                    db.execSQL(point_of_contactquery);
                    db.execSQL(family_tablequery);
                    result = true;
                } catch (Exception e) {
                    result = false;
                }
                if (result) {
                    result = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "personQuery execution error " + e);
                result = false;
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS organisation_type");
        db.execSQL("DROP TABLE IF EXISTS Work_type");
        db.execSQL("DROP TABLE IF EXISTS E_pass");
        db.execSQL("DROP TABLE IF EXISTS Key_person");
        db.execSQL("DROP TABLE IF EXISTS quarantime");
        db.execSQL("DROP TABLE IF EXISTS quarantinereason");
        db.execSQL("DROP TABLE IF EXISTS medical_record_symptoms_mapping");
        db.execSQL("DROP TABLE IF EXISTS symptoms");
        db.execSQL("DROP TABLE IF EXISTS survelliance");
        db.execSQL("DROP TABLE IF EXISTS medicalrecord");
        db.execSQL("DROP TABLE IF EXISTS regisration");
        db.execSQL("DROP TABLE IF EXISTS USERID");
        db.execSQL("DROP TABLE IF EXISTS City_location");
        db.execSQL("DROP TABLE IF EXISTS Area");
        db.execSQL("DROP TABLE IF EXISTS Ward");
        db.execSQL("DROP TABLE IF EXISTS Zone");
        db.execSQL("DROP TABLE IF EXISTS City");
        db.execSQL("DROP TABLE IF EXISTS organisation_name");
        db.execSQL("DROP TABLE IF EXISTS organisation_sub_type");
        db.execSQL("DROP TABLE IF EXISTS org_office_type");
        db.execSQL("DROP TABLE IF EXISTS org_office");
        db.execSQL("DROP TABLE IF EXISTS status");
        db.execSQL("DROP TABLE IF EXISTS order_management");
        db.execSQL("DROP TABLE IF EXISTS order_status");
        db.execSQL("DROP TABLE IF EXISTS designation");
        db.execSQL("DROP TABLE IF EXISTS corona_contact_list");
        db.execSQL("DROP TABLE IF EXISTS point_of_contacted");
        db.execSQL("DROP TABLE IF EXISTS family");
        onCreate(db);
    }
}
