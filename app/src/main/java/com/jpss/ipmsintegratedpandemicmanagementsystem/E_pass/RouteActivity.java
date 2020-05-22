package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Other.DrashBoardActivity;
import com.jpss.ipmsintegratedpandemicmanagementsystem.Other.GPSTrack;
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

import org.json.JSONException;
import org.json.JSONObject;

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

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();
    Marker marker;
    ArrayList<LatLng> points = null;
    ArrayList <String> locations = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String frmlc = intent.getStringExtra("FromLoc");
        String Tolc = intent.getStringExtra("ToLoc");
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

        for(int i=0; i<locations.size();i++){
            String val = locations.get(i);
            String lat = val.split(",")[0];
            String lon = val.split(",")[1];
            drawpoint(Double.parseDouble(lat),Double.parseDouble(lon));
        }




       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {*/



          /*  }
        });*/

    }

    public void drawpoint(Double lati,Double longi){

        LatLng posi = new LatLng(lati,longi);
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
            String duration=null;
            ArrayList<LatLng> points;
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
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0) {    // Get distance from the list
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


            alertdialog(distance,duration);
            /*if (lastPolyLine != null) {
                lastPolyLine.remove();
            }
            // Drawing polyline in the Google Map for the i-th route
            lastPolyLine = mMap.addPolyline(lineOptions);*/
            mMap.addPolyline(lineOptions);

        }
    }

    public void alertdialog(String distance,String duration) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RouteActivity.this);
        builder1.setTitle("Your Estimated Time and Distance is ");
        builder1.setMessage("Time="+duration +"\n"+
                "Distance="+distance);
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

                                double longitude = gpsTrack.getLongitude();
                                double latitude = gpsTrack.getLatitude();

                                if (latitude != 0.0 || longitude != 0.0) {
                                    int height = 80;
                                    int width = 80;
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.logo);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                    LatLng india = new LatLng(latitude, longitude);
                                    marker = mMap.addMarker(new MarkerOptions().position(india).title("yourlocation").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                    marker.showInfoWindow();
                                   // points.add(india);

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 20));
                                } else {
                                    Toast.makeText(RouteActivity.this, "Null Value", Toast.LENGTH_SHORT).show();
                                }
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
                                //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 20));

                            }

                        });
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                }

            }
        };
        t.start();

    }

    public String CalculationByDistance(double lat1, double lat2, double lon1, double lon2) {
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
        String distance =  kmInDec + "Km" + meterInDec +"Meter";
       // distancekm.setText(distance);
        String dism = String.valueOf(meterInDec);
        return dism;
    }
}
