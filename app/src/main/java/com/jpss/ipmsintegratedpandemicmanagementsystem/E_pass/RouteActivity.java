package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.android.PolyUtil;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Bean.Bean;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Database.DatabaseOperation;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Model.GenericModel;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Other.DrashBoardActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Other.GPSTrack;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Other.MainActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jpss.ipmsintegratedpandemicmanagementsystem.services.NetworkChangeReceiver;
import com.jpss.ipmsintegratedpandemicmanagementsystem.utils.NetworkUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.jpss.ipmsintegratedpandemicmanagementsystem.data.Constants.CONNECTIVITY_ACTION;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList markerPoints = new ArrayList();
    Marker marker;
    LatLng india;
    ArrayList<LatLng> points = null;
    ArrayList<String> locations = new ArrayList<>();
    String epsid, keypid;
    double lat1 = 0.0;
    double lat2 = 0.0;
    double lon1 = 0.0;
    double lon2 = 0.0;
    String violtnid = "";
    LocationManager lm;
    String onpath = null;
    static String violationid = null;
    String inbuildig = null;
    String ismovement = null;
    boolean checktime = false;
    boolean offroute = false;
    private static String log_str;

    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
        violationtypedataentry();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        addLogText(NetworkUtil.getConnectivityStatusString(RouteActivity.this));
        Intent intent = getIntent();
        String frmlc = intent.getStringExtra("FromLoc");
        String Tolc = intent.getStringExtra("ToLoc");
        epsid = intent.getStringExtra("epassid");
        keypid = intent.getStringExtra("kpid");
        locations.add(frmlc);
        locations.add(Tolc);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng sydney = new LatLng(28.471485, 77.131438);

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        // currentlocation();
       /* locations.add("28.471485, 77.131438");
        locations.add("28.472079, 77.129282");
        locations.add("28.472381, 77.134196");
        locations.add("28.472853, 77.133306");*/

        for (int i = 0; i < locations.size(); i++) {
            String val = locations.get(i);
            String lat = val.split(",")[0];
            String lon = val.split(",")[1];
            drawpoint(Double.parseDouble(lat), Double.parseDouble(lon));
        }




       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {*/

          /*  }
        });*/

    }

    public static void addLogText(String log) {
        System.out.print(log);
        if (log.contains("NOT_CONNECT")) {
            violationid = "1";
        } else {
            violationid = "3";
        }
    }

    public void drawpoint(Double lati, Double longi) {

        LatLng posi = new LatLng(lati, longi);
      /*  if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }*/

        // Adding new item to the ArrayList
        markerPoints.add(posi);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(posi);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);


        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            LatLng origin = (LatLng) markerPoints.get(0);
            LatLng dest = (LatLng) markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        //https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=YOUR_API_KEY
        //Origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Destination
        String str_destination = "destination=" + destination.latitude + "," + destination.longitude;
        // Set output format
        String output = "json";

        String api_key = "key=" + getString(R.string.google_maps_key);

        //Building the parameters to the web service
        String parameters = output + "?" + str_origin + "&" + str_destination + "&" + api_key;

        String url = "https://maps.googleapis.com/maps/api/directions/" + parameters;
        // String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=AIzaSyCrbBOSEAUcnJfoLVochZlp92HZROIzXSQ";

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(strUrl);

            // Creating an HTTP Connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            //Connecting to URL
            urlConnection.connect();

            // Reading data from URL
            iStream = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            bufferedReader.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-UI thread
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for pars;ing JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            String distance = null;
            String duration = null;
            PolylineOptions lineOptions = null;
            int resultSize = result.size();

            //Traversing through all the routes
            for (int i = 0; i < resultSize; i++) {

                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                //Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                int pathSize = path.size();
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }
            alertdialog(distance, duration);
            /*if (lastPolyLine != null) {
                lastPolyLine.remove();
            }
            // Drawing polyline in the Google Map for the i-th route
            lastPolyLine = mMap.addPolyline(lineOptions);*/
            mMap.addPolyline(lineOptions);
            epassviolationentry();
            locationchange();
            //  currentlocation();

        }
    }


    public void alertdialog(String distance, String duration) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RouteActivity.this);
        builder1.setTitle("Your Estimated Time and Distance is ");
        builder1.setMessage("Time=" + duration + "\n" +
                "Distance=" + distance);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "oK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void currentlocation() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (marker != null) {
                                    marker.remove();
                                }
                                GPSTrack gpsTrack = new GPSTrack(RouteActivity.this);
                                lat2 = lat1;
                                lon2 = lon1;
                                double longitude = gpsTrack.getLongitude();
                                double latitude = gpsTrack.getLatitude();
                                double accuracy = gpsTrack.getAccuracy();

                                if (latitude != 0.0 || longitude != 0.0) {
                                    int height = 80;
                                    int width = 80;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.logo);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                    india = new LatLng(latitude, longitude);
                                    marker = mMap.addMarker(new MarkerOptions().position(india).title("yourlocation").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                    marker.showInfoWindow();

                                    lat1 = latitude;
                                    lon1 = longitude;
                                    String distance = calculatedistance(lat1, lat2, lon1, lon2);
                                    Double dist = Double.parseDouble(distance);
                                    if (dist >= 50) {
                                        //  sendjsonepasslive(epsid,keypid,latitude,longitude,accuracy,"1","y");
                                    }
                                    // points.add(india);
                                    double tolerance = 70; // meters
                                    boolean isLocationOnPath = PolyUtil.isLocationOnPath(india, points, true, tolerance);
                                    if (isLocationOnPath == false) {
                                        showCustomDialog();
                                    }
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 20));
                                } else {
                                    Toast.makeText(RouteActivity.this, "Null Value", Toast.LENGTH_SHORT).show();
                                }
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
                                //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20));

                            }

                        });
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                }

            }
        };
        t.start();

    }

    private void epassviolationentry() {
        if (checktime == true) {
            ismovement = "y";
            onpath = "1";
        } else {
            ismovement = "n";
            onpath = "2";
        }

    }

    private void sendjsonepasslive(String epid, String kid, Double lat, Double lon, Double acc, String epsttsid, String ismvmnt) {
        JSONObject jsonObject = new JSONObject();
        GenericModel genericModel = new GenericModel(this);
        try {
            jsonObject.put("epass_id", epid);
            jsonObject.put("keyprsnid", kid);
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lon);
            jsonObject.put("accuracy", acc);
            jsonObject.put("epass_statusid", epsttsid);
            jsonObject.put("ismovement", ismvmnt);
            jsonObject.put("violation_type_id", violtnid);

            String result = genericModel.sendepasslivedata(jsonObject);
            if (result.equalsIgnoreCase("success")) {
                DatabaseOperation dbtask = new DatabaseOperation(this);
                dbtask.open();
                dbtask.insertepasslivedata(epid, kid, lat, lon, acc, epsttsid, ismvmnt);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void violationtypedataentry() {
        DatabaseOperation dbtask = new DatabaseOperation(this);
        dbtask.open();
        int epslvid = dbtask.getlastindex();
        dbtask.insertviolation(violationid, String.valueOf(epslvid));

    }

    public String calculatedistance(double lat1, double lat2, double lon1, double lon2) {
        int Radius = 6378;// radius of earth in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        // Toast.makeText(this, "Radius Value"+ "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec, Toast.LENGTH_SHORT).show();
        String distance = kmInDec + "Km" + meterInDec + "Meter";
        // distancekm.setText(distance);
        String dism = String.valueOf(meterInDec);
        return dism;
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mydialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.start();
        Button gpsButton = (Button) alertDialog.findViewById(R.id.buttonOk);
        // if GPS button is clicked, it send gps location
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                alertDialog.dismiss();
                mediaPlayer.stop();
            }
        });

    }


    private void locationchange() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                timer();
                epassviolationentry();
                double latttt = location.getLatitude();
                double lon = location.getLongitude();
                double speed = location.getSpeed();
                double accuracy = location.getAccuracy();
                double alt = location.getAltitude();
                india = new LatLng(latttt, lon);
                sendjsonepasslive(epsid, keypid, latttt, lon, accuracy, onpath, ismovement);
                double tolerance = 70; // meters
                boolean isLocationOnPath = PolyUtil.isLocationOnPath(india, points, true, tolerance);
                if (isLocationOnPath == false) {
                    offroute = true;
                    violationid = "2";
                    violationtypedataentry();
                    showCustomDialog();
                } else if (isLocationOnPath == true) {
                    offroute = false;
                }
                checktime = true;
                //  sendjsonepasslive(epsid,keypid,latttt,lon,accuracy,"1","y");
                Toast.makeText(RouteActivity.this, "location change", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                System.out.print(provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                System.out.print(provider);
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
                System.out.print(provider);
            }
        });
    }

    private void timer(){
        Hourglass hourglass = new Hourglass(5000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                // Update UI
               // Toast.makeText(RouteActivity.this, String.valueOf(timeRemaining), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimerFinish() {
                // Timer finished
                checktime=false;
                epassviolationentry();
               // Toast.makeText(RouteActivity.this, "Timer finished", Toast.LENGTH_SHORT).show();


            }
        };
        hourglass.startTimer();
    }


}
