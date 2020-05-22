package com.jpss.ipmsintegratedpandemicmanagementsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseOperation {
    private static final String TAG = "DatabaseOperation.java";
    Context context;
    private DatabaseWrapper dbHelper;
    private SQLiteDatabase database;
    String userId;
    String cityId;
    String contactpointid;
    String chck;
    public DatabaseOperation(Context context) {
        dbHelper = new DatabaseWrapper(context);
        context = context;
    }


/* -----------------------------  Somya Data Operation Starts   -------------------------   */
    public long insertsurvillance(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("sur_id", bleBean.getSur_id());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude",bleBean.getLongitude() );
                values.put("image_path", bleBean.getImage_path());
                values.put("created_date", bleBean.getCreated_date());
                values.put("sync_server", bleBean.getServer_sync());
                result = database.replace("survelliance", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
 public long insertcontactdetails(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("name", bleBean.getName());
                values.put("mobile", bleBean.getMobileno());
                values.put("datetime", bleBean.getDate_time());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude", bleBean.getLongitude());
                values.put("keypersonid", bleBean.getKeypersonid());
                values.put("point_of_contact_id", bleBean.getPointid());
                result = database.replace("corona_contact_list", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    public String getqurantinedata() {
        String res="" ;
        try {


            Cursor cursor = database.rawQuery("SELECT id FROM quarantime ", null);
            cursor.moveToPosition(0);
            int a = cursor.getCount();
            for (int j = 0; j < cursor.getCount(); j++) {
                cursor.moveToPosition(j);
                res= (cursor.getString(0));
            }
        }

        catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }

        return res;
    }
/* Insert Quareantine reason*/
    public long insertquarntine_reason(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("reason_id", bleBean.getReason_id());
                values.put("reason_desc", bleBean.getReason_desc());
                values.put("created_date", bleBean.getCreated_date());
                values.put("created_by", bleBean.getCreated_by());
                values.put("remark", bleBean.getRemark());
//                values.put("remark2", bleBean.getRemark2());
                result = database.replace("quarantinereason", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* Insert Symptoms reason*/
    public long insertsymptoms(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("symptoms_id", bleBean.getSynptom_id());
                values.put("symptoms_desc", bleBean.getSymptoms_desc());
                values.put("remark", bleBean.getRemarks());
                result = database.replace("symptoms", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertpointofcontact(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("point_of_contact_id", bleBean.getPointid());
                values.put("point_of_contact", bleBean.getPoint_of_contact());
                values.put("remark", bleBean.getRemarks());
                values.put("datetime", bleBean.getDate_time());
                result = database.replace("point_of_contacted", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
  /* Get Quarantine reason*/
    public ArrayList<String> getreasonquarantine() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT reason_desc FROM quarantinereason", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            list.add("--select--");
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

    /* Get Symptoms reason*/
    public ArrayList<String> getsymptoms() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT symptoms_desc FROM symptoms", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

 public String getcontactpointid(String name) {
        try {
            String query="SELECT point_of_contact_id FROM point_of_contacted where point_of_contact ='"+name+"'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                contactpointid=(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return contactpointid;
    }

    public ArrayList<String> getsymtomsid(List val) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < val.size(); i++) {
                chck = String.valueOf(val.get(i));


                Cursor cursor = database.rawQuery("SELECT symptoms_id FROM symptoms where symptoms_desc = '" + chck + "' ", null);
                cursor.moveToPosition(0);
                int a = cursor.getCount();
                for (int j = 0; j < cursor.getCount(); j++) {
                    cursor.moveToPosition(j);
                    list.add(cursor.getString(0));
                    // list.add(surveyBean);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }

        return list;
    }
    public long insertmedicalrecord(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("quarantime_id", bleBean.getQuarantime_id());
                values.put("temprature", bleBean.getTemprature());
                values.put("BP", bleBean.getBp());
                values.put("Timestamp", bleBean.getTimestamp());
                result = database.replace("medicalrecord", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    public int getmedid(String val) {
        int res=0;
        try {


            Cursor cursor = database.rawQuery("SELECT med_rec_id FROM medicalrecord where Timestamp = '" + val + "' ", null);
            cursor.moveToPosition(0);
            int a = cursor.getCount();
            for (int j = 0; j < cursor.getCount(); j++) {
                cursor.moveToPosition(j);
                res= Integer.parseInt(cursor.getString(0));
            }
        }

        catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }

        return res;
    }
    public long insertmap(List val, int medid) {
        long result = 0;

        try {
            database.beginTransaction();
            for (int i = 0; i < val.size(); i++) {
                String id= String.valueOf(val.get(i));
                ContentValues values = new ContentValues();
                int ids= Integer.parseInt(id);
                values.put("med_rec_id", medid);
                values.put("symptoms_id", ids);
                values.put("remark","ghh");
                result = database.replace("medical_record_symptoms_mapping", null, values);
                if (result > 0) {
                    database.setTransactionSuccessful();
                }
            }



        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }


/*  -----------------------------  Somya Data Operation Ends -------------------------   */

    public long insertuserid(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("id_type", bleBean.getId_type());
                values.put("id_type_id", bleBean.getId_type_id());
                result = database.replace("USERID", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertorgtype(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("organisation_type_id",bleBean.getOrgtypeid());
                values.put("org_type_name",bleBean.getOrgtypename());
                result = database.replace("organisation_type", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    /* BY SANDEEP*/
    public long insertlocation(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("city_name", bleBean.getCityNane());
                values.put("cityType_id", bleBean.getCityType_id());
                result = database.replace("City", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertorderstatus(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("order_id", bleBean.getOrdrstatusid());
                values.put("order_status", bleBean.getOrdrstatusnm());
                result = database.replace("order_status", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public void open() {
        // to get a db in writable mode
        try {

            database = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.d(TAG, "open db: " + e);
        }
    }


    public long insertOrgvalue() {
        long result = 0;
        try {
            database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("org_type_name","public" );
            values.put("organisation_type_id", "2");
            values.put("description", "hjk");


            result = database.replace("organisation_type", null, values);

            if (result > 0) {
                database.setTransactionSuccessful();
            }
        }
         catch (Exception e) {
            Log.e(TAG, "insertcommand error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertEpassDetails(String epsid ,String vldfrmdt,  String vldtodt  ,String dlvryprsn, String mobno,String wtypid , String qrpath,String ordrmgmtid,String frmlctn,String tolctn) {
        long result = 0;
        try {
            database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("e_pass_id",epsid);
            values.put("valid_from_DATETIME",vldfrmdt);
            values.put("valid_to_DATETIME",vldtodt);
            values.put("delievery_person",dlvryprsn);
            values.put("mobile_no1",mobno);
            values.put("work_type_id",wtypid);
            values.put("qrpath",qrpath);
            values.put("order_mgmt_id",ordrmgmtid);
            values.put("valid_from_location",frmlctn);
            values.put("valid_to_location",tolctn);
            // result = database.replace("E_pass", null, values);

            int id = getepassid(epsid);
            if(id==-1)
                result =   database.insert("E_pass", null, values);
            else
                result =  database.update("E_pass", values, "e_pass_id=?", new String[]{Integer.toString(id)});

            if (result > 0) {
                database.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "insertcommand error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertzone(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("Zone_id", bleBean.getZoneid());
                values.put("Zone_name", bleBean.getZonename());
                values.put("city_id", bleBean.getZoneforeignid());
//                values.put("remark2", bleBean.getRemark2());
                result = database.replace("Zone", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertward(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("ward_id", bleBean.getWardid());
                values.put("ward_name", bleBean.getWardname());
                values.put("Zone_id", bleBean.getWardforeignid());
//                values.put("remark2", bleBean.getRemark2());
                result = database.replace("Ward", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertarea(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("area_id", bleBean.getAreaid());
                values.put("area_name", bleBean.getAreaname());
                values.put("ward_id", bleBean.getAreaforeihnid());
//                values.put("remark2", bleBean.getRemark2());
                result = database.replace("Area", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }


    public long insertcitylocation(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("citylocation_id", bleBean.getCitylocationid());
                values.put("location", bleBean.getCitylocationname());
                values.put("area_id", bleBean.getCitylocationforeignid());
//                values.put("remark2", bleBean.getRemark2());
                result = database.replace("City_location", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }



    public long insertworktype(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("work_type_id",bleBean.getWorktypeid());
                values.put("work_code",bleBean.getWorktypecode());
                values.put("organisationtype_id",bleBean.getOrgtypid());
                values.put("parent_id",bleBean.getParentid());
                values.put("is_super_child",bleBean.getIssuperchild());
                values.put("generation",bleBean.getGeneration());
                result = database.replace("Work_type", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public long insertOrdrmgmtdata(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("order_mngmnt_id",bleBean.getOrder_mgmt_id());
                values.put("e_pass_id",bleBean.getOrdre_pass_id());
                values.put("mobno",bleBean.getOrdrmgmtmnno());
                values.put("finalstatus",bleBean.getFinal_status());
                values.put("order_image_path",bleBean.getOrdrmgmtimg());
                // result = database.update("order_management",values, "order_mngmnt_id = ?",new String[]{bleBean.getOrder_mgmt_id()});
                // result = database.replace("order_management", null, values);
                int id = getid(bleBean.getOrder_mgmt_id());
                if(id==-1)
                    result =   database.insert("order_management", null, values);
                else
                    result =  database.update("order_management", values, "order_mngmnt_id=?", new String[]{Integer.toString(id)});
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public ArrayList<String> getepasslist() {
        ArrayList<String> list = new ArrayList<String>();
        String e_pass_id=null;
        String delievery_person=null;
        String mobile_no1=null;
        try {
            Cursor cursor = database.rawQuery("SELECT e_pass_id,delievery_person,mobile_no1 FROM E_pass ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                e_pass_id=(cursor.getString(0));
                delievery_person=(cursor.getString(1));
                mobile_no1=(cursor.getString(2));

                String val = e_pass_id+","+delievery_person+","+mobile_no1;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }



    public ArrayList<String> getEpassInfolist(String id) {
        ArrayList<String> list = new ArrayList<String>();
        String e_pass_id=null;
        String validfrom=null;
        String validto=null;
        String qrpath=null;
        String worktype=null;
        String delievery_person=null;
        String mobile_no1=null;
        String ordrmgmtid=null;
        String validfromlocation=null;
        String validtolocation=null;
        try {
            Cursor cursor = database.rawQuery("SELECT e_pass_id,valid_from_DATETIME,valid_to_datetime,qrpath,work_type_id,delievery_person,mobile_no1,order_mgmt_id,valid_from_location,valid_to_location FROM E_pass Where e_pass_id =  "+id+"; ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                e_pass_id=(cursor.getString(0));
                validfrom=(cursor.getString(1));
                validto=(cursor.getString(2));
                qrpath=(cursor.getString(3));
                worktype=(cursor.getString(4));
                delievery_person=(cursor.getString(5));
                mobile_no1=(cursor.getString(6));
                ordrmgmtid=(cursor.getString(7));
                validfromlocation=(cursor.getString(8));
                validtolocation=(cursor.getString(9));

                String val = e_pass_id+"_"+validfrom+"_"+validto+"_"+qrpath+"_"+worktype+"_"+delievery_person+"_"+mobile_no1+"_"+ordrmgmtid+"_"+validfromlocation+"_"+validtolocation;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

    public ArrayList<String> getworktype(int id) {
        String nv="";
        ArrayList<String> list = new ArrayList<String>();
        list.add("--select--");
        try {
            Cursor cursor = database.rawQuery("SELECT work_code FROM Work_type Where  generation='1' AND organisationtype_id ="+id+" ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add((cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

    public ArrayList<String> getordrmgmtlist() {
        ArrayList<String> list = new ArrayList<String>();
        String ordrimg;
        String mobno;
        String fnstts;
        String odmid;
        try {
            Cursor cursor = database.rawQuery("SELECT order_image_path,mobno,finalstatus,order_mngmnt_id FROM order_management ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ordrimg=(cursor.getString(0));
                mobno=(cursor.getString(1));
                fnstts=(cursor.getString(2));
                odmid=(cursor.getString(3));

                String val = ordrimg+","+mobno+","+fnstts+","+odmid;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }




    public ArrayList<String> getorderbyid(String id) {
        ArrayList<String> list = new ArrayList<String>();
        String ordrimg;
        String mobno;
        String fnstts;
        try {
            Cursor cursor = database.rawQuery("SELECT order_image_path,mobno,finalstatus FROM order_management Where order_mngmnt_id =  "+id+" ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ordrimg=(cursor.getString(0));
                mobno=(cursor.getString(1));
                fnstts=(cursor.getString(2));


                String val = ordrimg+","+mobno+","+fnstts;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

    public ArrayList<String> getorderstatuslist() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT order_status FROM order_status  ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add((cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }


    public ArrayList<String> getdlvryboylist() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT r.username FROM regisration r\n" +
                    "INNER join designation d ON r.designation_id=d.designation_id\n" +
                    "WHERE d.designation='Delivery Boy'  ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add((cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }

    public String getworktypeid(String wrkcode) {
        String wtyid=null;
        try {
            Cursor cursor = database.rawQuery("SELECT work_type_id FROM Work_type WHERE work_code = '"+wrkcode+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                wtyid = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return wtyid;
    }

    public String getlatlon(String orgofcid) {
        String latitude=null;
        String longitude=null;
        try {
            Cursor cursor = database.rawQuery("SELECT latitude,longitude FROM org_office WHERE org_office_id = "+orgofcid+" ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                latitude = cursor.getString(0);
                longitude = cursor.getString(1);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return latitude+","+longitude;
    }

    public int getid(String id) {
        int ordrmngmnt=-1;
        try {
            Cursor cursor = database.rawQuery("SELECT order_mngmnt_id FROM order_management WHERE order_mngmnt_id = "+id+"", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ordrmngmnt = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return ordrmngmnt;
    }

    public int getepassid(String id) {
        int ordrmngmnt=-1;
        try {
            Cursor cursor = database.rawQuery("SELECT e_pass_id FROM E_pass WHERE e_pass_id = "+id+"", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ordrmngmnt = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return ordrmngmnt;
    }


    public boolean deletetask(String time){
        try {
            database.execSQL("DELETE FROM " + "E_pass" + " WHERE  qr_code = '" + time + "'");
            // database.execSQL("DELETE FROM " + "project_table" + " WHERE " + "Project_name" + "= '" + name + "'");
        }catch (Exception e){
            e.printStackTrace();
        }

       /*
        database.rawQuery("DELETE FROM device_command_map",null);*/
        return true;
    }


    public ArrayList<String> getkeypersonid(String id) {
        ArrayList<String> list = new ArrayList<String>();
        String usenm=null;
        String email=null;
        String mobileno=null;
        String gender=null;
        String imageprsn=null;
        String location=null;
        String address=null;
        String id_type_id=null;
        String identitynumber=null;
        String image_path2=null;
        String org_office_id=null;
        String family_office=null;
        try {
            Cursor cursor = database.rawQuery("SELECT username,email,mobileno,gender,image_path1,c.city_name,address_line_1,u.id_type,identitynumber,image_path2,org_office_id,family_office FROM regisration r \n" +
                    "inner join City c on r.city_id = c.cityType_id\n" +
                    "inner join USERID u on r.id_type_id = u.id_type_id \n" +
                    "WHERE key_person_id = "+id+" ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                usenm=(cursor.getString(0));
                email=(cursor.getString(1));
                mobileno=(cursor.getString(2));
                gender=(cursor.getString(3));
                imageprsn=(cursor.getString(4));
                location=(cursor.getString(5));
                address=(cursor.getString(6));
                id_type_id=(cursor.getString(7));
                identitynumber=(cursor.getString(8));
                image_path2=(cursor.getString(9));
                org_office_id=(cursor.getString(10));
                family_office=(cursor.getString(11));

                String val = usenm+","+email+","+mobileno+","+gender+","+imageprsn+","+location+","+address+","+id_type_id+","+identitynumber+","+image_path2+","+org_office_id+","+family_office;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }


    public String getkpidandlocation(String mobno) {
        String a = null;
        String b = null;
        String c = null;
        String d = null;
        String f = null;
        try {
            Cursor cursor = database.rawQuery("SELECT key_person_id,c.city_name,username,email,mobileno FROM regisration r \n" +
                    "INNER JOIN City c on r.city_id=c.citytype_id\n" +
                    "WHERE mobileno= "+mobno+"", null);
            cursor.moveToPosition(0);
            {
                a = cursor.getString(0);
                b = cursor.getString(1);
                c = cursor.getString(2);
                d = cursor.getString(3);
                f = cursor.getString(4);
                cursor.moveToNext();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return a+","+b+","+c+","+d+","+f;
    }

    public String getorgnm(String kpid) {
        String a =null;
        try {
            Cursor cursor = database.rawQuery("SELECT organisation_name FROM organisation_name WHERE organisation_id= "+kpid+"", null);
            cursor.moveToPosition(0);
            {
                a = cursor.getString(0);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return a;
    }


    public int getorgtypeid(String orgnmid) {
        int a = 0;
        try {
            Cursor cursor = database.rawQuery("SELECT o.organisation_sub_type_id FROM organisation_name o \n" +
                    "INNER JOIN org_office f On o.organisation_id=f.organisation_id\n" +
                    "WHERE f.org_office_id= " + orgnmid + "", null);
            cursor.moveToPosition(0);
            {
                a = cursor.getInt(0);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return a;
    }


    public int wrktyid(String wkcd) {
        int a = 0;
        try {
            Cursor cursor = database.rawQuery("SELECT work_type_id FROM Work_type WHERE work_code= '" + wkcd + "'", null);
            cursor.moveToPosition(0);
            {
                a = cursor.getInt(0);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return a;
    }

    public String issuperchild(String itm) {
        String val = null;
        try {
            Cursor cursor = database.rawQuery("SELECT is_super_child FROM Work_type WHERE work_code= '" + itm + "'", null);
            cursor.moveToPosition(0);
            {
                val = cursor.getString(0);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return val;
    }

    public ArrayList<String> getwrknm(int itm) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("--select--");
        try {
            Cursor cursor = database.rawQuery("SELECT work_code FROM Work_type WHERE parent_id= " + itm + "", null);
            cursor.moveToPosition(0);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add( cursor.getString(0));
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return list;
    }

    public String getorgnmid(String orgofcid) {
        String orgofcidd =null;
        String orgoffice_name =null;
        try {
            Cursor cursor = database.rawQuery("SELECT organisation_id,org_office_name FROM org_office Where org_office_id = "+orgofcid+"", null);
            cursor.moveToPosition(0);
            {
                orgofcidd = cursor.getString(0);
                orgoffice_name = cursor.getString(1);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return orgofcidd+","+orgoffice_name;
    }



    public String ispchld() {
        String a = "";
        String b = "";
        try {
            Cursor cursor = database.rawQuery("SELECT org_type_name FROM organisation_type ", null);
            cursor.moveToPosition(0);
            {
                a = cursor.getString(0);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getUserDtailerror: " + e);
        }
        return a;
    }
    
    /* BY SANDEEP*/
    public long insertOrgOfficeDetails(String id,  String officeName ,String mobilenumber , String cityid ,String address , String orgnameid , String orglat , String orglong, String type  ) {
        long result = 0;
        try {
            database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("org_office_id",id);
            values.put("org_office_name",officeName);
            values.put("mobile_no",mobilenumber);
            values.put("office_type_id",cityid);
            values.put("address",address);
            values.put("organisation_id",orgnameid);
            values.put("latitude",orglat);
            values.put("longitude",orglong);
            values.put("type",type);

            result = database.replace("org_office", null, values);

            if (result > 0) {
                database.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "insertcommand error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertfamOrgOfficeDetails(String famid,  String famofficeName ,String fammobilenumber , String famcityid ,String famaddress , String famorgnameid, String famorglat , String famorglong ) {
        long result = 0;
        try {
            database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("org_office_id",famid);
            values.put("org_office_name",famofficeName);
            values.put("mobile_no",fammobilenumber);
            values.put("office_type_id",famcityid);
            values.put("address",famaddress);
            values.put("organisation_id",famorgnameid);
            values.put("latitude",famorglat);
            values.put("longitude",famorglong);
            result = database.replace("org_office", null, values);

            if (result > 0) {
                database.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "insertcommand error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insetdegDetails(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();

                values.put("designation_id", bleBean.getDesigid());
                values.put("designation", bleBean.getDesigname());
                result = database.replace("designation", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insetdegDetailss(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();

                values.put("designation_id", bleBean.getFamDesigid());
                values.put("designation", bleBean.getFamDesigname());
                result = database.replace("designation", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getdesignationyname() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT designation FROM designation", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    public ArrayList<String> getpointcontactdata() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            list.add("---select---");
            Cursor cursor = database.rawQuery("SELECT point_of_contact FROM point_of_contacted", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    /* BY SANDEEP*/
    public String getdesignameid(String name) {
        try {
            String query="SELECT designation_id FROM designation where designation ='"+name+"'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                cityId=(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return cityId;
    }
    /* BY SANDEEP*/
    public String getorgofficedetail(String mobNo) {
        String orgOfficeid=null;
        String orgOfficename=null;
        try {
            Cursor cursor = database.rawQuery("SELECT org_office_id,org_office_name FROM org_office WHERE mobile_no = '"+mobNo+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                orgOfficeid = cursor.getString(0);
                orgOfficename = cursor.getString(1);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return orgOfficeid+","+orgOfficename;
    }

    public String getorgofficedetal(String mobNo , String tp) {
        String orgOfficeid=null;
        String orgOfficename=null;
        try {
            Cursor cursor = database.rawQuery("SELECT org_office_id,org_office_name FROM org_office WHERE type = '" + tp + "' AND  mobile_no = '"+mobNo+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                orgOfficeid = cursor.getString(0);
                orgOfficename = cursor.getString(1);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return orgOfficeid+","+orgOfficename;
    }
    /* BY SANDEEP*/
    public String getdesigId(String id) {
        String desiId=null;
        try {
            Cursor cursor = database.rawQuery("SELECT designation_id FROM designation WHERE designation_id = '"+id+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                desiId = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return desiId;
    }
    /* BY SANDEEP*/
    public String getfamdesigId(String id) {
        String desiId=null;
        try {
            Cursor cursor = database.rawQuery("SELECT designation_id FROM designation WHERE designation_id = '"+id+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                desiId = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return desiId;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getorgname() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT org_type_name FROM organisation_type  ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add((cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }


    /* Insert Registration reason*/
    public long insetregdetails(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("username", bleBean.getUsername());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude", bleBean.getLongitude());
                values.put("email", bleBean.getEmail());
                values.put("mobileno", bleBean.getMobileno());
                values.put("emergency_name", bleBean.getEmergency_name());
                values.put("emergency_mobilen", bleBean.getEmergency_mobilen());
                values.put("gender", bleBean.getGender());
                values.put("blood_group", bleBean.getBlood_group());
                values.put("password", bleBean.getPassword());
                values.put("key_person_id", bleBean.getKeypersonid());
                values.put("revision_no", bleBean.getRevision_no());
                values.put("city_name",bleBean.getCityNane());
                values.put("address_line_1",bleBean.getAddress());
                result = database.replace("regisration", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }


    public void close() {
        dbHelper.close();
    }

  

    /* Get registrated mobile number */
    public ArrayList<String> getregisteredmobileno() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT mobileno FROM regisration", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    /* BY SANDEEP*/
    public long insertimagefromserver(List<Bean> list,String id) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("image_path1", bleBean.getImage_path1());
                values.put("image_path2", bleBean.getImage_path2());
                result = database.update("regisration", values, "key_person_id = ?",new String[]{id});
              //  result = database.insert("regisration", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getuserid() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT id_type FROM USERID", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getcityname() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT city_name FROM City", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    /* BY SANDEEP*/
    public String getcitynameid(String name) {
        try {
            String query="SELECT cityType_id FROM City where city_name ='"+name+"'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                cityId=(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return cityId;
    }
   
    /* BY SANDEEP*/
    public String getuserids(String val) {
        try {
            Cursor cursor = database.rawQuery("SELECT id_type_id FROM USERID where id_type = '" + val + "'" , null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId=(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return userId;
    }

    public long insertimageregistrationdetails(List<Bean> list) {
    long result = 0;
    try {
        Iterator<Bean> iterator = list.iterator();
        database.beginTransaction();
        while (iterator.hasNext() && result != -1) {
            ContentValues values = new ContentValues();
            Bean bleBean = iterator.next();
            values.put("identitynumber", bleBean.getIdentitynumber());
            values.put("id_no", bleBean.getId_type());
            values.put("id_type_id", bleBean.getId_type_id());
            values.put("image_path1", bleBean.getImage_path1());
            values.put("image_path2", bleBean.getImage_path2());
            values.put("username", bleBean.getUsername());
            values.put("latitude", bleBean.getLatitude());
            values.put("longitude", bleBean.getLongitude());
            values.put("email", bleBean.getEmail());
            values.put("mobileno", bleBean.getMobileno());
            values.put("emergency_name", bleBean.getEmergency_name());
            values.put("emergency_mobilen", bleBean.getEmergency_mobilen());
            values.put("gender", bleBean.getGender());
            values.put("blood_group", bleBean.getBlood_group());
            values.put("password", bleBean.getPassword());
            values.put("org_office_id", bleBean.getOfficeid());
            values.put("fathername", bleBean.getFathername());
            values.put("Date_of_birth", bleBean.getDob());
            values.put("key_person_id", bleBean.getKeypersonid());
            values.put("city_name", bleBean.getCityNane());
            values.put("city_id", bleBean.getCityid());
            values.put("revision_no", bleBean.getRevision_no());
            values.put("designation_id", bleBean.getDesignationid());
            values.put("address_line_1",bleBean.getAddress());
            values.put("address_line_2",bleBean.getAddress2());
            values.put("address_line_3",bleBean.getAddress3());
            result = database.replace("regisration", null, values);
        }
        if (result > 0) {
            database.setTransactionSuccessful();
        }

    } catch (Exception e) {
        Log.e(TAG, "insertNodeDetail error: " + e);
    } finally {
        database.endTransaction();
    }
    return result;
}

    public long insertquarantinedata(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("id", bleBean.getQuarantime_id());
                values.put("reason_id", bleBean.getReason_id());
                values.put("reason_desc", bleBean.getReason_desc());
                values.put("created_date", bleBean.getCreated_date());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude", bleBean.getLongitude());
                values.put("image_path", bleBean.getImage_path());
                values.put("day", bleBean.getDay());
                values.put("date_time", bleBean.getDate_time());

                result = database.replace("quarantime", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public ArrayList<String> getquarantinedata() {
        ArrayList<String> list = new ArrayList<String>();
        String quarantime_id=null;
        String reason_id=null;
        String reason_desc=null;
        String created_date=null;
        String latitude=null;
        String longitude=null;
        String image_path=null;
        String day=null;
        String date_time=null;
        String remark=null;
        try {
            Cursor cursor = database.rawQuery("SELECT id,reason_id,reason_desc,created_date,latitude,longitude,image_path,day,date_time FROM quarantime", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                quarantime_id=cursor.getString(0);
                reason_id=cursor.getString(1);
                reason_desc=cursor.getString(2);
                created_date=cursor.getString(3);
                latitude=cursor.getString(4);
                longitude=cursor.getString(5);
                image_path=cursor.getString(6);
                day=cursor.getString(7);
                date_time=cursor.getString(8);
                //       remark=cursor.getString(9);
                String val = quarantime_id+","+reason_id+","+reason_desc+","+created_date+","+latitude+","+longitude+","+image_path+","+day+","+date_time;

                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    public long insertdatafromserver(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("key_person_id", bleBean.getKeypersonid());
                values.put("username", bleBean.getUsername());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude", bleBean.getLongitude());
                values.put("email", bleBean.getEmail());
                values.put("mobileno", bleBean.getMobileno());
                values.put("city_id", bleBean.getCityid());
                values.put("address_line_1", bleBean.getAddress());
                values.put("fathername", bleBean.getFathername());
                values.put("Date_of_birth", bleBean.getDob());
                values.put("designation_id", bleBean.getDesigid());
                values.put("org_office_id", bleBean.getOfficeid());

                result = database.insert("regisration", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertkplist(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("key_person_id", bleBean.getKeypersonid());
                values.put("username", bleBean.getUsername());
                values.put("latitude", bleBean.getLatitude());
                values.put("longitude", bleBean.getLongitude());
                values.put("email", bleBean.getEmail());
                values.put("mobileno", bleBean.getMobileno());
                values.put("emergency_name", bleBean.getEmergency_name());
                values.put("emergency_mobilen", bleBean.getEmergency_mobilen());
                values.put("blood_group", bleBean.getBlood_group());
                values.put("city_id", bleBean.getCityid());
                values.put("gender", bleBean.getGender());
                values.put("address_line_1", bleBean.getAddress());
                values.put("landline_no1", bleBean.getLandline1());
                values.put("fathername", bleBean.getFathername());
                values.put("id_type_id", bleBean.getId_type_id());
                values.put("Date_of_birth", bleBean.getDob());
                values.put("identitynumber", bleBean.getIdentitynumber());
                values.put("designation_id", bleBean.getDesigid());
                values.put("org_office_id", bleBean.getOfficeid());
                values.put("family_office", bleBean.getFamilyoffice());
                values.put("family_designation", bleBean.getFamilydesig());
                values.put("relation", bleBean.getRelation());
                result = database.replace("regisration", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertfamkplist(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("key_person_id", bleBean.getFamKeypersonid());
                values.put("username", bleBean.getFamUsername());
                values.put("latitude", bleBean.getFamlattitude());
                values.put("longitude", bleBean.getFamlongitude());
                values.put("email", bleBean.getFamEmail());
                values.put("mobileno", bleBean.getFamMobileno());
                values.put("emergency_name", bleBean.getFamEmergency_name());
                values.put("emergency_mobilen", bleBean.getFamEmergency_mobilen());
                values.put("blood_group", bleBean.getFamBlood_group());
                values.put("city_id", bleBean.getFamCityid());
                values.put("gender", bleBean.getFamGender());
                values.put("address_line_1", bleBean.getFamAddress());
                values.put("landline_no1", bleBean.getLandline1());
                values.put("fathername", bleBean.getFamFathername());
                values.put("id_type_id", bleBean.getFamId_type_id());
                values.put("Date_of_birth", bleBean.getFamDob());
                values.put("identitynumber", bleBean.getFamIdentitynumber());
                values.put("designation_id", bleBean.getFamDesigid());
                values.put("org_office_id", bleBean.getFamOfficeid());
                values.put("family_office", bleBean.getFamilyoffice());
                values.put("family_designation", bleBean.getFamilydesig());
                values.put("relation", bleBean.getRelation());

                result = database.replace("regisration", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertorgnamedetails(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("organisation_id",bleBean.getFamorgid());
                values.put("organisation_name",bleBean.getFamorgname());
                values.put("organisation_sub_type_id",bleBean.getFamorgTypeid());
                result = database.replace("organisation_name", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public String getkerpersonid(String mobNo) {
        String kpid=null;
        try {
            Cursor cursor = database.rawQuery("SELECT key_person_id FROM regisration WHERE mobileno = '"+mobNo+"' ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                kpid= cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return kpid;
    }
    public ArrayList<String> getorgofficeid() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT org_office_id FROM org_office", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getString(0));
                // list.add(surveyBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    public String getquarntineid() {
        String res="" ;
        try {


            Cursor cursor = database.rawQuery("SELECT id FROM quarantime ", null);
            cursor.moveToPosition(0);
            int a = cursor.getCount();
            for (int j = 0; j < cursor.getCount(); j++) {
                cursor.moveToPosition(j);
                res= (cursor.getString(0));
            }
        }

        catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }

        return res;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getdesigid(List val) {
        ArrayList<String> res=new ArrayList<String>();
        try {
            for (int i = 0; i < val.size(); i++) {
                String id = String.valueOf(val.get(i));
                Cursor cursor = database.rawQuery("SELECT designation_id FROM regisration where key_person_id = '" + id + "'", null);
                cursor.moveToPosition(0);
                int a = cursor.getCount();
                for (int j = 0; j < cursor.getCount(); j++) {
                    cursor.moveToPosition(j);
                    res.add(cursor.getString(0));
                }
            }
        }catch(Exception e){
            Log.e(TAG, "getItemData error: " + e);
        }


        return res;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getcityid(List val) {
        ArrayList<String> res=new ArrayList<String>();
        try {
            for (int i = 0; i < val.size(); i++) {
                String id = String.valueOf(val.get(i));
                Cursor cursor = database.rawQuery("SELECT city_id FROM regisration where key_person_id = '" + id + "'", null);
                cursor.moveToPosition(0);
                int a = cursor.getCount();
                for (int j = 0; j < cursor.getCount(); j++) {
                    cursor.moveToPosition(i);
                    res.add(cursor.getString(0));
                }
            }
        }catch(Exception e){
            Log.e(TAG, "getItemData error: " + e);
        }


        return res;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getdesigname(List list1) {
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<list1.size();i++){
            String id= String.valueOf(list1.get(i));
            try {
                Cursor cursor = database.rawQuery("SELECT designation FROM designation where designation_id = '"+id+"'", null);
                cursor.moveToPosition(0);
                int a= cursor.getCount();
                for (int j = 0; j < cursor.getCount(); j++) {
                    cursor.moveToPosition(i);
                    list.add(cursor.getString(0));
                    // list.add(surveyBean);
                }
            } catch (Exception e) {
                Log.e(TAG, "getItemData error: " + e);
            }
        }

        return list;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getcityname(List list1) {
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<list1.size();i++){
            String id= String.valueOf(list1.get(i));
            try {
                Cursor cursor = database.rawQuery("SELECT city_name FROM City where cityType_id = '"+id+"'", null);
                cursor.moveToPosition(0);
                int a= cursor.getCount();
                for (int j = 0; j < cursor.getCount(); j++) {
                    cursor.moveToPosition(i);
                    list.add(cursor.getString(0));
                    // list.add(surveyBean);
                }
            } catch (Exception e) {
                Log.e(TAG, "getItemData error: " + e);
            }
        }

        return list;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getkpids() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = database.rawQuery("SELECT key_person_id FROM regisration  ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add((cursor.getString(0)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
    /* BY SANDEEP*/
    public long insertdesignname(List<Bean> list,List ls) {
        long result = 0;
        try {
            for(int i=0;i<ls.size();i++) {
                String id = String.valueOf(ls.get(i));

                Iterator<Bean> iterator = list.iterator();
                database.beginTransaction();
                while (iterator.hasNext() && result != -1) {
                    ContentValues values = new ContentValues();
                    Bean bleBean = iterator.next();
                    values.put("designation", bleBean.getDesigname());
                    result = database.update("regisration", values, "key_person_id = ?", new String[]{id});
                }
                if (result > 0) {

                    database.setTransactionSuccessful();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public long insertcitynme(List<Bean> list,List ls) {
        long result = 0;
        try {
            for(int i=0;i<ls.size();i++) {
                String id = String.valueOf(ls.get(i));

                Iterator<Bean> iterator = list.iterator();
                database.beginTransaction();
                while (iterator.hasNext() && result != -1) {
                    ContentValues values = new ContentValues();
                    Bean bleBean = iterator.next();
                    values.put("city_name", bleBean.getCityNane());
                    result = database.update("regisration", values, "key_person_id = ?", new String[]{id});
                }
                if (result > 0) {

                    database.setTransactionSuccessful();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
   
    /* BY SANDEEP*/
    public long insertfamilydetails(List<Bean> list) {
        long result = 0;
        try {
            Iterator<Bean> iterator = list.iterator();
            database.beginTransaction();
            while (iterator.hasNext() && result != -1) {
                ContentValues values = new ContentValues();
                Bean bleBean = iterator.next();
                values.put("username", bleBean.getFamilyusername());
                values.put("password", bleBean.getFamilypassword());
                values.put("mobileno", bleBean.getFamilymobileno());
                values.put("gender", bleBean.getFamilygender());
                values.put("bloodgroup", bleBean.getBlood_group());
                values.put("dob", bleBean.getFamilydob());
                values.put("fathername", bleBean.getFamilyFather());
                values.put("familymember", bleBean.getFamilymember());
                values.put("image_path1",bleBean.getImage_path1());
                values.put("image_path2",bleBean.getImage_path2());
                values.put("identitynumber",bleBean.getIdentitynumber());
                values.put("id_no",bleBean.getId_type());
                values.put("id_type_id",bleBean.getId_type_id());
                result = database.replace("family", null, values);
            }
            if (result > 0) {
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "insertNodeDetail error: " + e);
        } finally {
            database.endTransaction();
        }
        return result;
    }
    /* BY SANDEEP*/
    public ArrayList<String> getfamilydetails() {
        ArrayList<String> list = new ArrayList<String>();
        String userName=null;
        String password=null;
        String mobileno=null;
        String gender=null;
        String bloodgroup=null;
        String dob=null;
        String fathername=null;
        String familymember=null;
        String image_path1=null;
        String image_path2=null;
        String identitynumber=null;
        String id_type_id=null;
        try {
            Cursor cursor = database.rawQuery("SELECT username,password,mobileno,gender,bloodgroup,dob,fathername ,familymember,image_path1,image_path2,identitynumber,id_type_id  FROM family  ", null);
            cursor.moveToPosition(0);
            int a= cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userName=((cursor.getString(0)));
                password= ((cursor.getString(1)));
                mobileno=((cursor.getString(2)));
                gender=((cursor.getString(3)));
                bloodgroup=((cursor.getString(4)));
                dob=((cursor.getString(5)));
                fathername=((cursor.getString(6)));
                familymember=((cursor.getString(7)));
                image_path1=((cursor.getString(8)));
                image_path2=((cursor.getString(9)));
                identitynumber=((cursor.getString(10)));
                id_type_id=((cursor.getString(11)));
                String val = userName+","+password+","+mobileno+","+gender+","+bloodgroup+","+dob+","+fathername+","+familymember+","+image_path1+","+image_path2+","+identitynumber+","+id_type_id;
                list.add(val);
            }
        } catch (Exception e) {
            Log.e(TAG, "getItemData error: " + e);
        }
        return list;
    }
}