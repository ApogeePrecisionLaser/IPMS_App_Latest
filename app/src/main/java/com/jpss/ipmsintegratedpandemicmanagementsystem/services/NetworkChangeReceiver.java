package com.jpss.ipmsintegratedpandemicmanagementsystem.services;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;


/***********************************************
 * Created by sandeep on 29/05/2020.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;
    Dialog dialog;
    String check;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Receiver ", "" + status);

        if (status.contains(Constants.NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost
            showalert();
            check="NOT_CONNECT";
        } else {
            Log.e("Receiver ", "connected to internet");//your code when internet connection come back
            check="connected to internet";
        }
       // MainActivity.addLogText(status);

    }

    public void showalert() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.netconnectdialog);
        dialog.setCanceledOnTouchOutside(false);
        Button dialogButton = (Button) dialog.findViewById(R.id.buttonOk);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.contains((Constants.NOT_CONNECT))){
                    dialog.show();
                }else {
                    dialog.dismiss();

                }
            }
        });

        dialog.show();
        // if button is clicked, close the custom dialog

    }
}