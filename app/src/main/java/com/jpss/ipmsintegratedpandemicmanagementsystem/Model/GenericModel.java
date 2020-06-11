package com.jpss.ipmsintegratedpandemicmanagementsystem.Model;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import androidx.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class GenericModel
{
    private static final String TAG = "GenericModel.java";
    Context context;
    String server_ip = "";
    String port="";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCE = "Otp" ;
    public static final String IP = "Ipkey";
    public static final String PORT = "Portkey";
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    private static final String IMAGE_DIRECTORY = "/IPMS/VendorImage";

    /* BY SANDEEP */
    public String sendData(JSONObject jsonObject) throws IOException, JSONException {
        long result=0;
        String res="";
//        SharedPreferences settings;
        HttpPost httppost = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
//        if(!pprt.equalsIgnoreCase("default value") || !ipp.equalsIgnoreCase("default value")){
        server_ip = ipp;
        port = pprt;
        //     }
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/insertImage");
            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/insertImage");
            }

        }else {

            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/insertImage");
        }


        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }

    /* BY SANDEEP */
    public String getotp(String mobileno) throws IOException, JSONException {
        long result=0;
        String res="";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/sendOTP");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/sendOTP");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/sendOTP");

        }




        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(mobileno, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }

    /* BY SANDEEP */
    public String verifyotp() throws IOException, JSONException {
        long result=0;
        JSONObject receivedJsonObj=null;
        String res="";

        SharedPreferences settings;
        HttpPost httppost = null;
        JSONObject jsonObject=new JSONObject();
        settings = context.getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE); //1
        String mobilenumber = settings.getString("number", null);
        String otp = settings.getString("otp", null);
        jsonObject.put("number",mobilenumber);
        jsonObject.put("otp",otp);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/checkOTP");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/checkOTP");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checkOTP");

        }


        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);
        httpClient.getConnectionManager().shutdown();
        return res;
    }

    /* BY SANDEEP */

    public String verify_orgOtp() throws IOException, JSONException {
        long result=0;
        JSONObject receivedJsonObj=null;
        String res="";

        SharedPreferences settings;
        HttpPost httppost = null;
        JSONObject jsonObject=new JSONObject();
        settings = context.getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE); //1
        String mobilenumber = settings.getString("number", null);
        String otp = settings.getString("otp", null);
        jsonObject.put("number",mobilenumber);
        jsonObject.put("otp",otp);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;


        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/checkOTPOffice");
            }else{
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checkOTPOffice");
            }

        }else {

            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checkOTPOffice");
        }


        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);
        httpClient.getConnectionManager().shutdown();
        return res;
    }


    /* BY SANDEEP START*/
    public String verifylogindetail() throws IOException, JSONException {
        long result=0;
        String res="";
        SharedPreferences settings;
        JSONObject jsonObject=new JSONObject();
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); //1
        String number = settings.getString("Login_number", null);
        String pass = settings.getString("login_password", null);
        jsonObject.put("number",number);
        jsonObject.put("password",pass);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/loginmobile");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/loginmobile");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/loginmobile");

        }

        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    /* BY SANDEEP END*/

    /* BY SANDEEP */

    public String sendOrgtype(String orgMobile) throws IOException, JSONException {
        long result=0;
        String res="";
//        SharedPreferences settings;
        HttpPost httppost = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
//        if(!pprt.equalsIgnoreCase("default value") || !ipp.equalsIgnoreCase("default value")){
        server_ip = ipp;
        port = pprt;
        //     }
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/checkofficenumber");
            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/checkofficenumber");
            }

        }else {

            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checkofficenumber");
        }


        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(orgMobile, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }

    /* BY SANDEEP*/
    public JSONObject requestorgtype(String val) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/idandcity");
                }else{
                    httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/idandcity");
                }

            }else {

                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/idandcity");
            }

            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(val , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                result =  getorgtypedatafromjson(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }

    public long getorgtypedatafromjson(JSONObject jsonObject) {
        long result=0;
        long res=0,res1=0;

        try {
            //{"result":"success","idlist":[{"id":1,"name":"adhaar"},{"id":2,"name":"pan"}],"citylist":[{"id":2,"name":"जबलपुर"},{"id":3,"name":"vns"},{"id":6,"name":"jb"},{"id":7,"name":"jabalpur"}],"orgtypelist":[{"orgtypeid":1,"orgtypename":"Private"},{"orgtypeid":3,"orgtypename":"Government"},{"orgtypeid":4,"orgtypename":"Public"}]}
            JSONArray jsonArray2 = jsonObject.getJSONArray("idlist");
            List<Bean> id = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                Bean bleBean = new Bean();
                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                bleBean.setId_type_id(jsonObject2.getString("id"));
                bleBean.setId_type(jsonObject2.getString("name"));
                id.add(bleBean);
            }
            JSONArray jsonArray3 = jsonObject.getJSONArray("citylist");
            List<Bean> city_name = new ArrayList<Bean>();

            for (int i = 0; i < jsonArray3.length(); i++) {
                Bean bean = new Bean();
                JSONObject jsonObject3 = jsonArray3.getJSONObject(i);
                bean.setCityNane(jsonObject3.getString("name"));
                bean.setCityType_id(jsonObject3.getString("id"));
                city_name.add(bean);
            }
            JSONArray jsonArray1 = jsonObject.getJSONArray("orgtypelist");
            List<Bean> worktype = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setOrgtypeid(jsonObject1.getString("orgtypeid"));
                bleBean.setOrgtypename(jsonObject1.getString("orgtypename"));
                worktype.add(bleBean);
            }
            JSONArray jsonArray6 = jsonObject.getJSONArray("OrderStatus");
            List<Bean> orderstatus = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray6.length(); i++) {
                JSONObject jsonObject1 = jsonArray6.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setOrdrstatusid(jsonObject1.getString("id"));
                bleBean.setOrdrstatusnm(jsonObject1.getString("name"));
                orderstatus.add(bleBean);
            }
                JSONArray jsonArray = jsonObject.getJSONArray("Quarantine_reason");
                List<Bean> reason = new ArrayList<Bean>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Bean bleBean = new Bean();

                    bleBean.setReason_id(jsonObject1.getInt("id"));
                    bleBean.setReason_desc(jsonObject1.getString("reason_desc"));
                    bleBean.setRemark(jsonObject1.getString("remark"));
                    bleBean.setCreated_by(jsonObject1.getString("created_by"));
                    reason.add(bleBean);
                }

                JSONArray jsonArray4 = jsonObject.getJSONArray("Symptoms");
                List<Bean> val = new ArrayList<Bean>();
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject jsonObject1 = jsonArray4.getJSONObject(i);
                    Bean bleBean = new Bean();

                    bleBean.setSynptom_id(jsonObject1.getInt("id"));
                    bleBean.setSymptoms_desc(jsonObject1.getString("symptoms_desc"));
                    bleBean.setCreated_by(jsonObject1.getString("created_by"));
                    bleBean.setRemarks(jsonObject1.getString("remark"));
                    val.add(bleBean);
                }
            JSONArray jsonArray5 = jsonObject.getJSONArray("pointofcontact");
            List<Bean> val1 = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray5.length(); i++) {
                JSONObject jsonObject1 = jsonArray5.getJSONObject(i);
                Bean bleBean = new Bean();

                bleBean.setPointid(jsonObject1.getString("id"));
                bleBean.setPoint_of_contact(jsonObject1.getString("point_of_contact"));
                bleBean.setDate_time(jsonObject1.getString("datetime"));
                bleBean.setRemarks(jsonObject1.getString("remark"));
                val1.add(bleBean);
            }
                DatabaseOperation dbTask = new DatabaseOperation(context);
            dbTask.open();
            result = dbTask.insertorgtype(worktype);
            res=  dbTask.insertuserid(id);
            res1=   dbTask.insertlocation(city_name);
            res1=   dbTask.insertorderstatus(orderstatus);
            result = dbTask.insertquarntine_reason(reason);
            result = dbTask.insertsymptoms(val);
            result = dbTask.insertpointofcontact(val1);

            dbTask.close();

        } catch (Exception e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }


    public static JSONObject processHttpResponse(HttpResponse response)
            throws UnsupportedEncodingException, IllegalStateException, IOException, JSONException {
        JSONObject top = null;
        StringBuilder builder = new StringBuilder();
        String dec="";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            String decoded = new String(builder.toString().getBytes(), "UTF-8");
            Log.d(TAG, "decoded http response: " + decoded);
            JSONTokener tokener = new JSONTokener(Uri.decode(builder.toString()));
            top = new JSONObject(tokener);

        } catch (JSONException t) {
            Log.w(TAG, "<processHttpResponse> caught: " + t + ", handling as string...");

        } catch (IOException e) {
            Log.e(TAG, "caught processHttpResponse IOException : " + e, e);
        } catch (Throwable t) {
            Log.e(TAG, "caught processHttpResponse Throwable : " + t, t);
        }
        return top;
    }

    /* BY SANDEEP */
    public String numberregister(String mobileno) throws IOException, JSONException {
        long result=0;
        String res="";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/checknumber");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/checknumber");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checknumber");

        }


        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(mobileno, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }

    public String sendid(String id) throws IOException, JSONException {
        long result=0;
        String res="";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/sendimg");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/sendimg");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/sendimg");

        }


        httppost.setHeader("Content-type", "application/json");

        httppost.setEntity(new StringEntity(id, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    public String getCitydata( String city) throws IOException, JSONException {
        long result=0;
        String res="";
//        SharedPreferences settings;
        HttpPost httppost = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
//        if(!pprt.equalsIgnoreCase("default value") || !ipp.equalsIgnoreCase("default value")){
        server_ip = ipp;
        port = pprt;
        //     }
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/idandcity");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/idandcity");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/idandcity");

        }



        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(city, "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    /* BY SANDEEP */
    public String sendimagedata( JSONObject jsonObject) throws IOException, JSONException {
        long result=0;
        String res="";
//        SharedPreferences settings;
        HttpPost httppost = null;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
//        if(!pprt.equalsIgnoreCase("default value") || !ipp.equalsIgnoreCase("default value")){
        server_ip = ipp;
        port = pprt;
        //     }
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/insertdatakp");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/insertdatakp");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/insertdatakp");

        }

        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    public JSONObject requestEpass(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/generateEPass");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/generateEPass");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/generateEPass");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
      return jsonObject;
    }

    public JSONObject orderdlvrd(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/saveOrderDelivered");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/saveOrderDelivered");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/saveOrderDelivered");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }
    public JSONObject sendorderdata(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/saveOrderMgmt");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/saveOrderMgmt");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/saveOrderMgmt");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }


    public String sendepasslivedata(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            //http://120.138.10.146:8084/apiIPMS/apiServices/saveEPassLiveData
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/saveEPassLiveData");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/saveEPassLiveData");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/saveEPassLiveData");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
               /* HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);*/
                data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return data;
    }

    public JSONObject requestAppointmentlist(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            //http://120.138.10.146:8080/ipms/apiIPMS/apiServices/getOrderMgmt
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getAppointmentEPass");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getAppointmentEPass");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getAppointmentEPass");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                result =  getordrmngmntdatafromjson(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }


    public JSONObject checkKeyPersonExist(JSONObject json) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            //http://120.138.10.146:8080/ipms/apiIPMS/apiServices/getOrderMgmt
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/checkKeyPersonExist");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/checkKeyPersonExist");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/checkKeyPersonExist");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(json.toString() , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                result =  getordrmngmntdatafromjson(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }

    public JSONObject requestordrmngmntdata(String orgtpnm) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            //http://120.138.10.146:8080/ipms/apiIPMS/apiServices/getOrderMgmt
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getOrderMgmt");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getOrderMgmt");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getOrderMgmt");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(orgtpnm, "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                result =  getordrmngmntdatafromjson(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }

    public long getordrmngmntdatafromjson(JSONObject jsonObject) {
        long result=0;
        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("order_mgmt");
            List<Bean> ordrmgmt = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setOrdrmgmtmnno(jsonObject1.getString("mobile_no"));
                bleBean.setOrder_mgmt_id(jsonObject1.getString("order_mgmt_id"));
                bleBean.setFinal_status(jsonObject1.getString("final_status"));
                bleBean.setOrdre_pass_id(jsonObject1.getString("e_pass_id"));
                String img = (jsonObject1.getString("image_base64"));
                byte[] imageAsBytes = Base64.decode(img.getBytes(), Base64.DEFAULT);
                Bitmap bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                String imgpath = saveImage(bitmap);
                bleBean.setOrdrmgmtimg(imgpath);
                ordrmgmt.add(bleBean);
            }

            DatabaseOperation dbTask = new DatabaseOperation(context);
            dbTask.open();
            result = dbTask.insertOrdrmgmtdata(ordrmgmt);

            dbTask.close();

        } catch (Exception e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }


    public JSONObject requestepasslistdata(String kpid) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            //http://120.138.10.146:8080/ipms/apiIPMS/apiServices/getOrderMgmt
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getEPass");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getEPass");
                }
            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getEPass");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(kpid, "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                // result =  getepasslistdatajson(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }


  /*  public long getepasslistdatajson(JSONObject jsonObject) {
        long result=0;
        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("order_mgmt");
            List<Bean> ordrmgmt = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setOrdrmgmtmnno(jsonObject1.getString("mobile_no"));
                bleBean.setOrder_mgmt_id(jsonObject1.getString("order_mgmt_id"));
                bleBean.setFinal_status(jsonObject1.getString("final_status"));
                bleBean.setOrdre_pass_id(jsonObject1.getString("e_pass_id"));
                String img = (jsonObject1.getString("image_base64"));
                byte[] imageAsBytes = Base64.decode(img.getBytes(), Base64.DEFAULT);
                Bitmap bitmap = (BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                String imgpath = saveImage(bitmap);
                bleBean.setOrdrmgmtimg(imgpath);
                ordrmgmt.add(bleBean);
            }

            DatabaseOperation dbTask = new DatabaseOperation(context);
            dbTask.open();
            result = dbTask.insertOrdrmgmtdata(ordrmgmt);

            dbTask.close();

        } catch (Exception e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }*/

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

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
            MediaScannerConnection.scanFile(context,
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

    public String requestworktype(int orgtpnm) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String val = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getWorktype");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getWorktype");
                }

            }else {
                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getWorktype");
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(String.valueOf(orgtpnm), "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                val = jsonObject.getString("result");
                if(val.equalsIgnoreCase("success")){
                    result =  getworktypedatafromjson(jsonObject);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return val;
    }

    public long getworktypedatafromjson(JSONObject jsonObject) {
        long result=0;
        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("work_type");
            List<Bean> worktype = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setWorktypeid(jsonObject1.getString("work_type_id"));
                bleBean.setWorktypecode(jsonObject1.getString("work_code"));
                bleBean.setParentid(jsonObject1.getString("parent_id"));
                bleBean.setIssuperchild(jsonObject1.getString("is_super_child"));
                bleBean.setGeneration(jsonObject1.getString("generation"));
                bleBean.setOrgtypid(jsonObject1.getString("organisation_type_id"));
                worktype.add(bleBean);
            }

            DatabaseOperation dbTask = new DatabaseOperation(context);
            dbTask.open();
            result = dbTask.insertworktype(worktype);

            dbTask.close();

        } catch (JSONException e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }


    public JSONObject requestLocationData(String city) {
        long result = 0;
        HttpResponse response = null;
        JSONObject jsonObject = null;
        String data = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpPost httppost = null;

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


            String ipp = sharedPreferences.getString(IP, null);
            String pprt = sharedPreferences.getString(PORT, null);
            server_ip = ipp;
            port = pprt;
            if (server_ip!=null || port!=null){
                if(port.equalsIgnoreCase("8084")){
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/citydata");
                }else{
                    httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/citydata");
                }

            }else {

                httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/citydata");
            }

            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(new StringEntity(city , "UTF-8"));
            try {
                response = httpClient.execute(httppost);
                //data = EntityUtils.toString(response.getEntity());
                jsonObject = processHttpResponse(response);
                result= getjsonlocationdata(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Error in http execute " + e);
            }
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        return jsonObject;
    }

    public long getjsonlocationdata(JSONObject jsonObject) {
        long result=0;
        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("zone");
            List<Bean> zone = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setZoneid(jsonObject1.getInt("id"));
                bleBean.setZonename(jsonObject1.getString("name"));
                bleBean.setZoneforeignid(jsonObject1.getInt("foreignid"));
                zone.add(bleBean);
            }
            JSONArray jsonArray2 = jsonObject.getJSONArray("ward");
            List<Bean> ward = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setWardid(jsonObject1.getInt("id"));
                bleBean.setWardname(jsonObject1.getString("name"));
                bleBean.setWardforeignid(jsonObject1.getInt("foreignid"));
                ward.add(bleBean);
            }
            JSONArray jsonArray3 = jsonObject.getJSONArray("area");
            List<Bean> area = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray3.length(); i++) {
                JSONObject jsonObject1 = jsonArray3.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setAreaid(jsonObject1.getInt("id"));
                bleBean.setAreaname(jsonObject1.getString("name"));
                bleBean.setAreaforeihnid(jsonObject1.getInt("foreignid"));
                area.add(bleBean);
            }
            JSONArray jsonArray4 = jsonObject.getJSONArray("citylocation");
            List<Bean> citylocation = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray4.length(); i++) {
                JSONObject jsonObject1 = jsonArray4.getJSONObject(i);
                Bean bleBean = new Bean();
                bleBean.setCitylocationid(jsonObject1.getInt("id"));
                bleBean.setCitylocationname(jsonObject1.getString("name"));
                bleBean.setCitylocationforeignid(jsonObject1.getInt("foreignid"));
                citylocation.add(bleBean);
            }

            DatabaseOperation dbTask = new DatabaseOperation(context);
            dbTask.open();
            result = dbTask.insertzone(zone);
            result = dbTask.insertward(ward);
            result = dbTask.insertarea(area);
            result = dbTask.insertcitylocation(citylocation);

            dbTask.close();

        } catch (Exception e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }
    public GenericModel(Context context)
    {
        this.context = context;
    }
    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }
      public void setPort(String port) {
        this.port = port;
    }


/*==========================SOMYA WEB SERVICES STARTS=======================*/
    public long getPipeDataFromJson(JSONObject jsonObject) {
        long result=0;
        List<Bean> item = new ArrayList<Bean>();

        DatabaseOperation dbTask = new DatabaseOperation(context);
        dbTask.open();

        try {
            JSONArray jsonArray1 = jsonObject.getJSONArray("Quarantine_reason");
            List<Bean> reason = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                Bean bleBean = new Bean();

                bleBean.setReason_id(jsonObject1.getInt("id"));
                bleBean.setReason_desc(jsonObject1.getString("reason_desc"));
                bleBean.setRemark(jsonObject1.getString("remark"));
                bleBean.setCreated_by(jsonObject1.getString("created_by"));
                reason.add(bleBean);
            }

            JSONArray jsonArray2 = jsonObject.getJSONArray("Symptoms");
            List<Bean> val = new ArrayList<Bean>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                Bean bleBean = new Bean();

                bleBean.setSynptom_id(jsonObject1.getInt("id"));
                bleBean.setSymptoms_desc(jsonObject1.getString("symptoms_desc"));
                bleBean.setCreated_by(jsonObject1.getString("created_by"));
                bleBean.setRemarks(jsonObject1.getString("remark"));
                val.add(bleBean);
            }
            result = dbTask.insertquarntine_reason(reason);
            result = dbTask.insertsymptoms(val);

            dbTask.close();

        } catch (Exception e) {
            Log.e(TAG, "Error in getPipeDataFromJson " + e.toString());
        }
        return  result;
    }

    public JSONObject qurantinerequest(String id) throws IOException, JSONException {
        long result=0;
        String res="";
        JSONObject receivedJsonObj=null;
        HttpResponse response = null;

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("key_person_id",id);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/sendQuarantineData");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/sendQuarantineData");
            }

        }else {

            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/sendQuarantineData");

        }

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString() , "UTF-8"));
        try
        {
            response = httpClient.execute(httppost);
            receivedJsonObj = processHttpResponse(response);

        } catch (Exception e) {
            Log.e(TAG, "Error in http execute " + e);
        }
        httpClient.getConnectionManager().shutdown();
        return receivedJsonObj;
    }
    public String contactdetails(JSONObject json) throws IOException, JSONException {

        long result=0;
        String res="";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getCronaContactedData");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getCronaContactedData");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getCronaContactedData");
           // httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8084" + "/apiIPMS/apiServices/getCronaContactedData");

        }

        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(json.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    public String sendimage(JSONObject json) throws IOException, JSONException {
        long result=0;
        SharedPreferences settings;
        HttpPost httppost = null;
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); //1
        String res="";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getSomyaImage");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getSomyaImage");
            }

        }else {
          //  httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getSomyaImage");
                   httppost = new HttpPost("http://" + "192.168.43.120" + ":" + "8088" + "/apiIPMS/apiServices/getSomyaImage");

        }

        HttpParams httpParameters = new BasicHttpParams();

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(json.toString(), "UTF-8"));

        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }


    public String paniccall() throws JSONException, IOException {
        long result=0;
        SharedPreferences settings;
        HttpPost httppost = null;
        settings = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); //1
        String res="";
        JSONObject jsonObject=new JSONObject();
        DatabaseOperation databaseOperation=new DatabaseOperation(context);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf1.format(new Date());

        jsonObject.put("currentdatetime",currentDateandTime);
        jsonObject.put("panic","SOS");
        jsonObject.put("remark","somya");
        jsonObject.put("result","shweta");
        final SharedPreferences sharedPreference = PreferenceManager
                .getDefaultSharedPreferences(context);

        String num = sharedPreference.getString(LOGINNUMBER, "default value");
        String kpid = sharedPreference.getString(LOGINKPID, "default value");
        databaseOperation.open();
        String kpidandlocation = databaseOperation.getkpidandlocation(num);
        String keyprsnid = kpidandlocation.split(",")[0];
        jsonObject.put("key_person_id",keyprsnid);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/getPanic");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/getPanic");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/getPanic");

        }

        HttpParams httpParameters = new BasicHttpParams();

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));

        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);

        httpClient.getConnectionManager().shutdown();
        return res;
    }
    public String sendmedicalrecord(JSONObject jsonObject) throws IOException, JSONException {
        long result=0;
        String res="";
        JSONObject receivedJsonObj=null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = null;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ipp = sharedPreferences.getString(IP, null);
        String pprt = sharedPreferences.getString(PORT, null);
        server_ip = ipp;
        port = pprt;
        if (server_ip!=null || port!=null){
            if(port.equalsIgnoreCase("8084")){
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/apiIPMS/apiServices/DetailList");

            }else{
                httppost = new HttpPost("http://" + server_ip + ":" + port + "/ipms/apiIPMS/apiServices/DetailList");
            }

        }else {
            httppost = new HttpPost("http://" + "120.138.10.146" + ":" + "8080" + "/ipms/apiIPMS/apiServices/DetailList");
            //      httppost = new HttpPost("http://" + "192.168.43.120" + ":" + "8088" + "/apiIPMS/apiServices/DetailList");


        }


        httppost.setHeader("Content-type", "application/json");

        httppost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httppost);
        //  JSONObject receivedJsonObj = processHttpResponse(response);
        System.out.println("Web Service Response: " + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            result = 1;
        }
        HttpEntity entity = response.getEntity();
        res = EntityUtils.toString(entity);
        httpClient.getConnectionManager().shutdown();
        return res;
    }

    /*----------------------SOMYA WEB SERVICES ENDS------------------*/

}
