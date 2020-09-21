package in.maiddo.task1location;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapShow extends FragmentActivity{

    private static final int REQUEST_CODE = 101;

    private double latitide,longitude;
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private Button atm,bank,rest,host;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

//        atm = findViewById(R.id.atm);
//        bank = findViewById(R.id.bank);
//        host = findViewById(R.id.host);
//        rest = findViewById(R.id.rest);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );
        }


//
//        atm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://maps.googleapis.com/maps/api/..." + "?location=" + latitide + "," + longitude + "&radius=5000" + "&type=" + "atm" + "&sensor=true" + "&key=" + getResources().getString(R.string.map_key);
//                new PlaceTask().execute(url);
//            }
//        });

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){

                    latitide = location.getLatitude();
                    longitude = location.getLongitude();

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            LatLng latLng1 = new LatLng(latitide, longitude);
                            LatLng latLng2 = new LatLng(23.231246, 77.432540);
                            LatLng latLng3 = new LatLng(23.219814,77.440172);
                            LatLng latLng4 = new LatLng(23.231519,77.458849);

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);

                            MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).title("HackerKernel").icon(icon);

                            MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title("MP Nagar").icon(icon);

                            MarkerOptions markerOptions3 = new MarkerOptions().position(latLng3).title("Habibganj Station").icon(icon);

                            MarkerOptions markerOptions4 = new MarkerOptions().position(latLng4).title("BHEL").icon(icon);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,5));

                            float zoomLevel = 14.0f;
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, zoomLevel));

                            googleMap.addMarker(markerOptions1);
                            googleMap.addMarker(markerOptions2);
                            googleMap.addMarker(markerOptions3);
                            googleMap.addMarker(markerOptions4);
                            PolylineOptions polylineOptions = new PolylineOptions();

                            ArrayList points = new ArrayList();
                            points.add(new LatLng(23.201919, 77.435578));
                            points.add(new LatLng(23.201910, 77.435764));
                            points.add(new LatLng(23.202430, 77.435727));
                            points.add(new LatLng(23.202483, 77.437216));

                            points.add(new LatLng(23.204795, 77.437157));
                            points.add(new LatLng(23.211855, 77.436514));
                            points.add(new LatLng(23.213182, 77.440317));
                            points.add(new LatLng(23.218005, 77.439740));

                            points.add(new LatLng(23.231246, 77.432540));


                            polylineOptions.addAll(points);
                            polylineOptions.width(15f);
                            polylineOptions.color(0xFF157DEC);
                            polylineOptions.geodesic(true);

                            if (polylineOptions != null) {
                                map.addPolyline(polylineOptions);
                            } else {
                                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
                            }



                            //new TaskDirectionRequest().execute(getRequestedUrl(latLng1,latLng2));
                        }
                    });

                }
            }
        });
    }

//
//
//    private String getRequestedUrl(LatLng origin, LatLng destination) {
//        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
//        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
//        String sensor = "sensor=false";
//        String mode = "mode=driving";
//
//        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
//        String output = "json";
//        String APIKEY = getResources().getString(R.string.map_key);
//
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + APIKEY;
//        return url;
//    }
//
//    private String requestDirection(String requestedUrl) {
//        String responseString = "";
//        InputStream inputStream = null;
//        HttpURLConnection httpURLConnection = null;
//        try {
//            URL url = new URL(requestedUrl);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.connect();
//
//            inputStream = httpURLConnection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//            responseString = stringBuffer.toString();
//            bufferedReader.close();
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        httpURLConnection.disconnect();
//        return responseString;
//    }
//
//    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            String responseString = "";
//            try {
//                responseString = requestDirection(strings[0]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String responseString) {
//            super.onPostExecute(responseString);
//            //Json object parsing
//            TaskParseDirection parseResult = new TaskParseDirection();
//            parseResult.execute(responseString);
//        }
//    }
//
//    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
//            List<List<HashMap<String, String>>> routes = null;
//            JSONObject jsonObject = null;
//
//            try {
//                jsonObject = new JSONObject(jsonString[0]);
//                DirectionParser parser = new DirectionParser();
//                routes = parser.parse(jsonObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
//            super.onPostExecute(lists);
//            ArrayList points = null;
//            PolylineOptions polylineOptions = null;
//
//            for (List<HashMap<String, String>> path : lists) {
//                points = new ArrayList();
//                polylineOptions = new PolylineOptions();
//
//                for (HashMap<String, String> point : path) {
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lon = Double.parseDouble(point.get("lng"));
//
//                    points.add(new LatLng(lat, lon));
//                }
//                polylineOptions.addAll(points);
//                polylineOptions.width(15f);
//                polylineOptions.color(Color.BLUE);
//                polylineOptions.geodesic(true);
//            }
//            if (polylineOptions != null) {
//                map.addPolyline(polylineOptions);
//            } else {
//                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//

    ////////////////////// ALALALALALALALALLALALA/////////////////




//    private class PlaceTask extends AsyncTask<String,Integer,String> {
//
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String data = null;
//            try {
//                data = downloadUrl(strings[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            new ParserTask().execute(s);
//        }
//    }
//
//    private String downloadUrl(String string) throws IOException {
//        URL url = new URL(string);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.connect();
//
//        InputStream stream = connection.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//
//        StringBuilder builder = new StringBuilder();
//
//        String line ="";
//
//        while ((line = reader.readLine())!=null){
//            builder.append(line);
//        }
//
//        String data = builder.toString();
//
//        reader.close();
//        return data;
//
//
//    }
//
//    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
//
//        @Override
//        protected List<HashMap<String, String>> doInBackground(String... strings) {
//            JsonParserClass jsonParserClass = new JsonParserClass();
//
//            List<HashMap<String,String>> mapList = null;
//
//            try {
//                JSONObject object = new JSONObject(strings[0]);
//                mapList = jsonParserClass.parseResult(object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return mapList;
//        }
//
//        @Override
//        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
//            map.clear();
//
//            for (int i = 0 ; i <hashMaps.size();i++){
//                HashMap<String, String> hashMapList = hashMaps.get(i);
//                double lat = Double.parseDouble(hashMapList.get("lat"));
//                double lng = Double.parseDouble(hashMapList.get("lng"));
//
//
//                String name = hashMapList.get("name");
//                LatLng latLng = new LatLng(lat,lng);
//
//                MarkerOptions options = new MarkerOptions();
//
//                options.position(latLng);
//                options.title(name);
//                map.addMarker(options);
//            }
//        }
//    }



}