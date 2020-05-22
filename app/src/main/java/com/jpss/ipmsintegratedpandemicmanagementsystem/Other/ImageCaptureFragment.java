package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageCaptureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageCaptureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageCaptureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences.Editor editor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int i=0;
    String s;
    private OnFragmentInteractionListener mListener;

    String latitude, longitude;
    private GPSTrack gpsTrack;
    ArrayList<String> imagelist = new ArrayList<String>();
    File mediaFile;
    public static final int RequestPermissionCode = 7;
    LinearLayout imagelayout;
    String imagePath = "";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Cable";
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    String secondimage="",thirdimg;
    private Uri fileUri;
    File dir;
    SharedPreferences sharedpreferences;
    ImageButton imageButton;
    String server_ip = "";
    String port="";
    public static final String MyPREFERENCES = "MyPrefs" ;
    JSONObject jsonObject=new JSONObject();
    public File f;
    Button submit;
    public static final String IP = "Ipkey";
    public static final String PORT = "Portkey";
    DatabaseOperation databaseOperation;
    List<String> reason = new ArrayList<>();
    public static final String LOGINNUMBER = "loginnumber";
    public static final String LOGINKPID = "loginid";
    JSONObject jsonObject1=new JSONObject();
    public ImageCaptureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageCaptureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageCaptureFragment newInstance(String param1, String param2) {
        ImageCaptureFragment fragment = new ImageCaptureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        databaseOperation = new DatabaseOperation(getActivity());
        databaseOperation.open();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image_capture, container, false);
        imageButton=rootView.findViewById(R.id.btn_image);
        imagelayout = rootView.findViewById(R.id.imgLayout);
        submit = rootView.findViewById(R.id.submit);
        submit.setEnabled(false);
        getLocation();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageclick();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callingserve callingserve=new Callingserve();
                callingserve.execute();            }
        });
//        Callingnew callingnew=new Callingnew();
//        callingnew.execute();
        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    private void getLocation() {
        gpsTrack = new GPSTrack(getContext());
        Double lattitude = gpsTrack.getLatitude();
        latitude = String.valueOf(lattitude);
        Double longitude1 = gpsTrack.getLongitude();
        longitude = String.valueOf(longitude1);

    }
    public void imageclick() {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getActivity(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
        } else {
          //  if(checkPermission()) {

                captureImage();

//            }
//            else
//            {
//
//                requestPermission();
//            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > 23) {
            File file = getCameraFile();
            imagePath = file.getPath();
            fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file);

        }
        else
        {
            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public File getCameraFile() {
        dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir,  "IMG_"+""+System.currentTimeMillis()+".jpg");
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), IMAGE_DIRECTORY_NAME);
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+","+timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        imagePath = mediaFile.getAbsolutePath();
        return mediaFile;
        //return mediaStorageDir;
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET);
        int result5 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE);

        return result == PackageManager.PERMISSION_GRANTED &&  result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED  &&  result4 == PackageManager.PERMISSION_GRANTED &&  result5 == PackageManager.PERMISSION_GRANTED  ;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CAMERA,Manifest.permission.INTERNET}, RequestPermissionCode);


//        ActivityCompat.requestPermissions(CameraActivity.this, new
//                String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, RequestPermissionCode);


    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //viewImage();
                //viewImage1();
                imagelayout.setVisibility(View.VISIBLE);
                ImageView imgview=new ImageView(getActivity());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                imgview.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),""+bitmap, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Image Captured Successfully..", Toast.LENGTH_SHORT).show();
                imgview.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                String encodedString = Base64.encodeToString(byte_arr,1);

                imgview.requestFocus();
                imgview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                imagelayout.addView(imgview);

                imagelist.add(imagePath);
                submit.setEnabled(true);

                for(int i=0;i<imagelist.size();i++) {
                    try {

                        jsonObject1.put("totalImg", String.valueOf(i+1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                //showAlert("Image Cancel");
            } else {
                // failed to capture image
                Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private  JSONObject imagedatabase() throws JSONException {
        long result=0;
        connection();
/*
//
//        GPSTrack gpsTrack = new GPSTrack(getContext());
//        Double lattitude = gpsTrack.getLatitude();
//        latitude = String.valueOf(lattitude);
//        Double longitude1 = gpsTrack.getLongitude();
//        longitude = String.valueOf(longitude1);
*/

        try {
            if(i==0)
            {
                i++;
            }
            if(i>0)
            {
                secondimage = imagelist.get(1);

            }
        }catch ( Exception e){
            e.printStackTrace();
        }

        String filePath1 = (String) imagelist.get(0);

        jsonObject1.put("latitude",latitude);
        jsonObject1.put("longitude",longitude);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf1.format(new Date());
        List<Bean> survillance = new ArrayList<Bean>();
        for (int i = 0; i < imagelist.size(); i++) {
            Bean bleBean = new Bean();
            bleBean.setSur_id("1");
            bleBean.setLatitude(latitude);
            bleBean.setLongitude(longitude);
            bleBean.setCreated_date(currentDateandTime);
            bleBean.setImage_path(imagePath);
            bleBean.setServer_sync("N");
            survillance.add(bleBean);
        }
        DatabaseOperation dbTask = new DatabaseOperation(getActivity());
        dbTask.open();
        result = dbTask.insertsurvillance(survillance);
        databaseOperation.close();
        int i = 1;
        List list = new ArrayList();
        try {
            list.add(filePath1);
            list.add(secondimage);


            Iterator itrr = list.listIterator();
            while (itrr.hasNext()) {
                String filePath = (String) itrr.next();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                String encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                String imgname = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                jsonObject1.put("byte_arr" + i, encodedString);
                jsonObject1.put("imgname" + i, imgname);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        String num = sharedPreferences.getString(LOGINNUMBER, "default value");
        String kpid = sharedPreferences.getString(LOGINKPID, "default value");
        //      databaseOperation.open();
//           String kpidandlocation = databaseOperation.getkpidandlocation(num);
//         String keyprsnid = kpidandlocation.split(",")[0];
        String currentDateandTime1 = sdf1.format(new Date());
        jsonObject1.put("currentdatetime",currentDateandTime1);
        jsonObject1.put("person_id",kpid);

        return jsonObject1;
    }
    public class Callingserve extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            GenericModel prepaidDocModel = new GenericModel(getActivity());
            prepaidDocModel.setServer_ip(server_ip);
            prepaidDocModel.setPort(port);
            // prepaidDocModel.setDbTask(dbTask);
            String result = null;
            try {
                try {
                    JSONObject jsonObject=imagedatabase();
                    imagelist.clear();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

                    result = prepaidDocModel.sendimage(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            try {
                dialog.dismiss();
                if (result.equals("success")) {
                    Toast.makeText(getActivity(),"Image send successfully",Toast.LENGTH_LONG).show();
                    imagelayout.removeAllViews();
                    imagelist.clear();
                    jsonObject1=new JSONObject();
                } else {
                    if (result.equals("failure")) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Image Not send", Toast.LENGTH_LONG).show();
                        imagelayout.removeAllViews();

                    }

                }
            }catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "", "Proccessing....Please wait");
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
//    public class Callingnew extends AsyncTask<String, String, JSONObject> {
//
//        ProgressDialog dialog;
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//
//            GenericModel prepaidDocModel = new GenericModel(getActivity());
//            prepaidDocModel.setServer_ip(server_ip);
//            prepaidDocModel.setPort(port);
//            JSONObject result = new JSONObject();
//            try {
//                result = prepaidDocModel.getmobile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject result) {
//            // execution of result of Long time consuming operation
//            try {
//                dialog.dismiss();
////                    if (result > 0) {
////                        Toast.makeText(getActivity(), "Data  recieved", Toast.LENGTH_LONG).show();
////                    } else {
////                        Toast.makeText(getActivity(), " no updation  Found", Toast.LENGTH_LONG).show();
////                    }
//
//            } catch(Exception ex){
//                ex.printStackTrace();
//            }
//
//
//        }
//        @Override
//        protected void onPreExecute() {
//            dialog = ProgressDialog.show(getActivity(), "", "Proccessing....Please wait");
//            dialog.show();
//            // Things to be done before execution of long running operation. For
//            // example showing ProgessDialog
//
//        }
//
//        @Override
//        protected void onProgressUpdate(String... text) {
//            //firstBar.
//            // Things to be done while execution of long running operation is in
//            // progress. For example updating ProgessDialog
//        }
//
//    }
//
//

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void connection() {

        if (checkConnection()) {

        }else {
            Toast.makeText(getActivity(), "Check your enternet connection", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReciever.isConnected();
        if (isConnected) {
            return true;
        } else {
            return false;

        }
    }

}