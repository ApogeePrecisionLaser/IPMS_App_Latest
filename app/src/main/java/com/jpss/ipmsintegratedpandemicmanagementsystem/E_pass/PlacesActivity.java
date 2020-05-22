package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jpss.ipmsintegratedpandemicmanagementsystem.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    ArrayList<GooglePlace> venuesList;
    ListView simplelist;

    // the google key

    // ============== YOU SHOULD MAKE NEW KEYS ====================//
    final String GOOGLE_KEY = "AIzaSyDe73ubloUXceYt5TLwkq9S1EgFWgynvKU";

    // we will need to take the latitude and the logntitude from a certain point
    // this is the center of New York

    final String latitude = "28.480269";
    final String longtitude = "77.077895";
    final String radius = "1000";
    final String type = "food";
    final String name = "Mall";

    ArrayAdapter<String> myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        simplelist = findViewById(R.id.lstd);

        // start the AsyncTask that makes the call for the venus search.
        new googleplaces().execute();
    }

        private class googleplaces extends AsyncTask<View, Void, String> {

            String temp;

            @Override
            protected String doInBackground(View... urls) {
                // make Call to the url
                temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longtitude+"&radius="+radius+"&types="+type+"&name="+name+"&key=AIzaSyCrbBOSEAUcnJfoLVochZlp92HZROIzXSQ");
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.480269,%2077.077895&radius=500&types=food&name=Mall&key=AIzaSyCrbBOSEAUcnJfoLVochZlp92HZROIzXSQ
                //print the call in the console
              //  System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyCrbBOSEAUcnJfoLVochZlp92HZROIzXSQ");
                return "";
            }

            @Override
            protected void onPreExecute() {
                // we can start a progress bar here
            }

            @Override
            protected void onPostExecute(String result) {
                if (temp == null) {
                    // we have an error to the call
                    // we can also stop the progress bar
                } else {
                    // all things went right

                    // parse Google places search result
                    venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);

                    List<String> listTitle = new ArrayList<String>();

                    for (int i = 0; i < venuesList.size(); i++) {
                        // make a list of the venus that are loaded in the list.
                        // show the name, the category and the city
                        listTitle.add(i, "Name:"+venuesList.get(i).getName() + "\nOpen Now: " + venuesList.get(i).getOpenNow() + "\nAddress:" + venuesList.get(i).getVicinity() + "\nRating: " + venuesList.get(i).getRating() + "");
                    }

                    // set the results to the list
                    // and show them in the xml
                    myAdapter = new ArrayAdapter<String>(PlacesActivity.this, R.layout.row_layout, R.id.listText, listTitle);
                    simplelist.setAdapter(myAdapter);
                   // setListAdapter(myAdapter);
                }
            }
        }

        public static String makeCall(String url) {

            // string buffers the url
            StringBuffer buffer_string = new StringBuffer(url);
            String replyString = "";

            // instanciate an HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // instanciate an HttpGet
            HttpGet httpget = new HttpGet(buffer_string.toString());

            try {
                // get the responce of the httpclient execution of the url
                HttpResponse response = httpclient.execute(httpget);
                InputStream is = response.getEntity().getContent();

                // buffer input stream the result
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(20);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                // the result as a string is ready for parsing
                replyString = new String(baf.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(replyString);

            // trim the whitespaces
            return replyString.trim();
        }

        private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

            ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
            try {

                // make an jsonObject in order to parse the response
                JSONObject jsonObject = new JSONObject(response);

                // make an jsonObject in order to parse the response
                if (jsonObject.has("results")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        GooglePlace poi = new GooglePlace();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).optString("name"));
                            poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                            poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity", " "));

                            if (jsonArray.getJSONObject(i).has("opening_hours")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                        poi.setOpenNow("YES");
                                    } else {
                                        poi.setOpenNow("NO");
                                    }
                                }
                            } else {
                                poi.setOpenNow("Not Known");
                            }
                            if (jsonArray.getJSONObject(i).has("types")) {
                                JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                                for (int j = 0; j < typesArray.length(); j++) {
                                    poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                                }
                            }
                        }
                        temp.add(poi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<GooglePlace>();
            }
            return temp;

        }
    }
