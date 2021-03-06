package com.jpss.ipmsintegratedpandemicmanagementsystem.E_pass;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser {

    public static final String ROUTES = "routes";
    public static final String LEGS = "legs";
    public static final String STEPS = "steps";
    public static final String POLYLINE = "polyline";
    public static final String POINTS = "points";


    /**
     * Receives a JSONObject and returns a list of lists containing latitude and longitude
     * @param jObject JSONObject
     * @return List of routes
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<>();

        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray(ROUTES);
            int jRoutesLength = jRoutes.length();
            // Traversing all routes
            for(int i=0; i<jRoutesLength; i++){

                jLegs = ((JSONObject)jRoutes.get(i)).getJSONArray(LEGS);

                List path = new ArrayList<HashMap<String, String>>();

                int jLegsLength = jLegs.length();
                // Traversing all legs
                for (int j = 0; j<jLegsLength; j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray(STEPS);
                    JSONObject jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    JSONObject jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    HashMap<String, String> nm = new HashMap<>();
                    String distance = jDistance.getString("text");
                    String duration = jDuration.getString("text");
                    nm.put("distance",distance);
                    nm.put("duration",duration);
                    path.add(nm);

                    int jStepsLength = jSteps.length();
                    // Traversing all steps
                    for (int k = 0; k<jStepsLength; k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get(POLYLINE)).get(POINTS);
                        List<LatLng> list = decodePoly(polyline);

                        for (int l = 0; l<list.size(); l++){
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat",Double.toString((list.get(l)).latitude));
                            hm.put("lng",Double.toString((list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                }
                routes.add(path);
            }


        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return routes;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
