package ch.uifz725.beintime.activity;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ch.uifz725.beintime.R;
import ch.uifz725.beintime.model.Action;
import ch.uifz725.beintime.model.CreateDatabase;

public class MainActivity extends AppCompatActivity {

    CreateDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                CreateDatabase.class, "InTimeDB").allowMainThreadQueries().build();

        final Double myLongitude;

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


        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);

                //TextView longitudeTxt = findViewById(R.id.longitudeTxt);

                //longitudeTxt.setText(Double.toString(location.getLatitude()));


                System.out.println(location.getLatitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);






        // Database insert Tester
        Action action1 = new Action();
        action1.setDecsription("Rundgang");
        action1.setStart("start");
        action1.setEnd("end");
        db.actionDao().insertAction(action1);

        // Database tester Querry
        Button locationBtn = findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Action> allActions = db.actionDao().getAllActions();

                String tester = allActions.get(0).getDecsription();

                //TextView myView = findViewById(R.id.textView);
                //myView.setText(tester);



                System.out.println();


            }
        });

/*
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                Action action1 = new Action();

                action1.setDecsription("Rundgang");
                action1.setStart("start");
                action1.setEnd("end");


                db.actionDao().insertAction(action1);


            }
        });
*/


    }


}
