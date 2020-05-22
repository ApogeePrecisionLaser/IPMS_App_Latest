package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;

import org.json.JSONObject;

public class BasicData {
    Context mcontext;
  public  BasicData(Context context){
      this.mcontext=context;
      Qurantinedata callingnew=new Qurantinedata();
      callingnew.execute();
  }

    public class Qurantinedata extends AsyncTask<String, String, JSONObject> {

        ProgressDialog dialog;

        @Override
        protected JSONObject doInBackground(String... params) {

            GenericModel prepaidDocModel = new GenericModel(mcontext);

            JSONObject result = new JSONObject();

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // execution of result of Long time consuming operation
            try {
                dialog.dismiss();


            } catch(Exception ex){
                ex.printStackTrace();
            }


        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mcontext, "", "Proccessing....Please wait");
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }



}
