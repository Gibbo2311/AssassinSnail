package com.scottwgibson.assassinsnail;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;


public class StartHunt extends Activity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
     GoogleApiClient.OnConnectionFailedListener, LocationListener {

     //Location
     private GoogleApiClient mGoogleApiClient;
     protected LocationRequest mLocationRequest;
    private LatLng mPosition;

     //Map stuff
     private GoogleMap mMap;
     private Button mStartButton;

     public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
     public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                 UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static int minDistance = 1;
    private static int maxDistance = 2;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_start_hunt);

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener(this)
                 .addApi(LocationServices.API)
                 .build();

         mLocationRequest = new LocationRequest();
         mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
         mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
         mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_start_hunt, menu);
         return true;
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Find button
        ((MapFragment)getFragmentManager().findFragmentById(R.id.mapfragment)).getMapAsync(this);
        mStartButton = (Button)findViewById(R.id.startbutton);
        //Connect services
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
       LocationServices.FusedLocationApi.requestLocationUpdates(
               mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        //TODO:Suspend anything that requires on services
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "FAILED CONNECTION",
                Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location location) {
        if (location == null)
        {
            return;
        }

        mPosition = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 15.0f));
        mStartButton.setEnabled(true);
    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        this.onLocationChanged(mMap.getMyLocation());
    }

    public void beHuntedPressed(View view)
    {
        this.generateSnailPosition();
        Intent intent = new Intent(this, AssassinSnail.class);
        intent.putExtra("SnailLat", Double.toString(mPosition.latitude));
        intent.putExtra("SnailLng", Double.toString(mPosition.longitude));
        startActivity(intent);
        finish();
    }

    private void generateSnailPosition()
    {
        Random rand = new Random();
        double moveLat = (rand.nextDouble() + 1.0f);
        if(rand.nextBoolean())
        {
            moveLat *= 1;
        }

        double moveLng = (rand.nextDouble() + 1.0f);
        if(rand.nextBoolean())
        {
            moveLng *= 1;
        }

        mPosition = new LatLng(mPosition.latitude + moveLat, mPosition.longitude + moveLng);
    }
}
