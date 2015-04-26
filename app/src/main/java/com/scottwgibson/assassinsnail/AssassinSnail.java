package com.scottwgibson.assassinsnail;

import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class AssassinSnail extends Activity  {

    LatLng mSnailPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_screen);

        Intent intent = getIntent();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if( intent != null && intent.hasExtra("SnailLat") && intent.hasExtra("SnailLng"))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("SnailLat", intent.getStringExtra("SnailLat"));
            editor.putString("SnailLng", intent.getStringExtra("SnailLng"));
            editor.commit();
        }

        if(prefs.contains("SnailLat") && prefs.contains("SnailLng"))
        {
            mSnailPosition = new LatLng(Double.parseDouble(prefs.getString("SnailLat", "0")),
                    Double.parseDouble(prefs.getString("SnailLng", "0")));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial_screen, menu);
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

        if(mSnailPosition == null)
        {
            Intent intent = new Intent(this, StartHunt.class);
            startActivity(intent);
            finish();
        }
    }
}
