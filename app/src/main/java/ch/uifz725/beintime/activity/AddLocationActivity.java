package ch.uifz725.beintime.activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import ch.uifz725.beintime.R;
import ch.uifz725.beintime.model.CreateDatabase;
import ch.uifz725.beintime.model.Location;
import ch.uifz725.beintime.model.LocationDao;

public class AddLocationActivity extends AppCompatActivity {
    CreateDatabase db;
    LatLng myplace;
    Location location = new Location();

    TextView nameInput;
    TextView streetInput;
    TextView cityInput;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        db = Room.databaseBuilder(getApplicationContext(),
                CreateDatabase.class, "InTimeDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        nameInput = findViewById(R.id.nameInput);
        streetInput = findViewById(R.id.streetInput);
        cityInput = findViewById(R.id.cityInput);
    }

    @Override
    protected void onStart() {
        super.onStart();


        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location.setName(nameInput.getText().toString());
                location.setAdresse(streetInput.getText().toString());
                location.setCity(cityInput.getText().toString());


                myplace = getLocationFromAddress(AddLocationActivity.this, location.getAdresse() + location.getCity());

                // TODO: Remove print, just use for Testing
                //System.out.println(streetInput.getText());

                try {
                    location.setLongitudeStr(Double.toString(myplace.longitude));
                    location.setLatitudeStr(Double.toString(myplace.latitude));

                    TextView longitudeInput = findViewById(R.id.longitudeInput);
                    TextView latitudeInput = findViewById(R.id.latitudeInput);

                    longitudeInput.setText(Double.toString(myplace.longitude));
                    latitudeInput.setText(Double.toString(myplace.latitude));

                } catch (Exception e){

                    e.printStackTrace();
                    Toast myToast = Toast.makeText(AddLocationActivity.this, "Suche Ort ...", Toast.LENGTH_LONG);
                    myToast.show();

                }
            }
        });

        //Savebutton to insert location object into the db
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.locationDao().insertLocation(location);
                startActivity(new Intent(AddLocationActivity.this, MainActivity.class));
            }
        });
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
