package ch.uifz725.beintime.activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import ch.uifz725.beintime.R;
import ch.uifz725.beintime.model.CreateDatabase;

public class AddLocationActivity extends AppCompatActivity {
    CreateDatabase db;
    LatLng myplace;

    String name;
    String adresse;
    String city;
    String longitudeStr;
    String latitudeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        db = Room.databaseBuilder(getApplicationContext(),
                CreateDatabase.class, "InTimeDB").allowMainThreadQueries().build();

        myplace = getLocationFromAddress(this, "Hauptstrasse 70");

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(myplace.latitude);
                System.out.println(myplace.longitude);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



    }



    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast myToast = Toast.makeText(this, "Ort nicht Gefunden", Toast.LENGTH_LONG);
            myToast.show();
        }

        return p1;
    }




}
