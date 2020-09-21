package in.maiddo.task1location;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class LocationService extends Service {

    private Location currentLocation = null;

    private final IBinder binder = new LocalBinder();
    private LocationCallback locationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult!=null && locationResult.getLastLocation() !=null){
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                currentLocation = locationResult.getLastLocation();
                Log.d("LOCATION_UPDATE", latitude+","+longitude);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {



                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);

                    Intent notificationIntent = new Intent(getApplicationContext() , MainActivity.class ) ;
                    notificationIntent.putExtra( "NotificationMessage" , "I am from Notification" ) ;
                    notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
                    notificationIntent.setAction(Intent. ACTION_MAIN ) ;
                    notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                    PendingIntent resultIntent = PendingIntent. getActivity (getApplicationContext() , 0 , notificationIntent , 0 ) ;


                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


                    Notification.Builder builder = new Notification.Builder
                                (getApplicationContext());

                    builder.setContentTitle("GPS");
                    builder.setContentTitle("Current Location");
                    builder.setSmallIcon(R.drawable.ic_action_name);
                    builder.setContentIntent(resultIntent);
                    builder.setPriority(Notification.PRIORITY_MAX);
                    builder.setCategory(Notification.CATEGORY_CALL);
                    builder.setDefaults(DEFAULT_SOUND|DEFAULT_VIBRATE);
                    builder.setStyle(new Notification.BigTextStyle()
                            .bigText("Address"+address));
                    Notification notify= builder.build();

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        LocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationService.this;
        }
    }
    @SuppressLint("MissingPermission")
    private void startLocationService(){



        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "1");

        builder.setContentTitle("GPS");



        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICES_ID, builder.build());
    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            String action = intent.getAction();
            if (action!=null){
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
    public Location getCurrentLocation(){
        return currentLocation;
    }



}
