package ch.uifz725.beintime.activity;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.dynamic.IFragmentWrapper;

import java.util.List;

import ch.uifz725.beintime.R;
import ch.uifz725.beintime.model.Action;
import ch.uifz725.beintime.model.CreateDatabase;

public class MainActivity extends AppCompatActivity {

    // global variable for the database
    CreateDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Creates the Database
        db = Room.databaseBuilder(getApplicationContext(),
                CreateDatabase.class, "InTimeDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        final Double myLongitude;

        // DONT TOUCH
        //////////////////////////////////////////////////////
        // Block to get Permission to Access_FINE_LOCATION
        //////////////////////////////////////////////////////
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        ///////////////////////////////
        // END BLOCK PERMISSION ///////
        ///////////////////////////////


        // Change Activity and Layout
        Button registerLocationBtn = findViewById(R.id.locationBtn);
        registerLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddLocationActivity.class));
            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location locationNow) {

                handleNewLocation(locationNow);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}

            public void handleNewLocation(Location locationNow) {


                Chronometer myChronometer = findViewById(R.id.chronometer);

                List<ch.uifz725.beintime.model.Location> locations = db.locationDao().getAllLocation();

                for (ch.uifz725.beintime.model.Location location: locations ){

                    TextView nametxt = findViewById(R.id.nametxt);



                    if (Double.parseDouble(location.getLatitudeStr()) + 10 > locationNow.getLatitude() && Double.parseDouble(location.getLatitudeStr()) - 10 < locationNow.getLatitude() &&
                            Double.parseDouble(location.getLongitudeStr()) + 10 > locationNow.getLongitude() && Double.parseDouble(location.getLongitudeStr()) - 10 < locationNow.getLongitude()
                    ){
                        nametxt.setText(location.getName() + ": ");
                        myChronometer.start();

                    } else {

                        myChronometer.stop();
                        nametxt.setText("");

                        //Action myAction = new Action();

                        //db.actionDao().insertAction(myAction);


                    }

                    Log.d("stored longitude", location.getLongitudeStr());
                    Log.d("stored latitude", location.getLatitudeStr());
                    Log.d("NOW longitude", Double.toString(locationNow.getLongitude()));
                    Log.d("NOW latitude", Double.toString(locationNow.getLatitude()));


                }



            }
        };



        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);

    }


}
