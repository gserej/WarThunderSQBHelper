package com.github.gserej.warthundersqbhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String ID_HIGH = "ID_HIGH";
    private static final String TAG = "MainActivity";
    private static final Vehicles vehicles = new Vehicles();
    private static String ipAddress = "192.168.1.1";
    private static String fullUrl = "http://" + ipAddress + ":8111/hudmsg?lastEvt=0&lastDmg=0";
    private static String status;
    private static int idHigh;
    private TextView displayFriendlyTanks;
    private TextView displayFriendlyPlanes;
    private TextView displayHostileTanks;
    private TextView displayHostilePlanes;
    private TextView mConnectionStatus;

    public static void setStatus(String status) {
        MainActivity.status = status;
    }

    public static void setIpAddressAndFullUrl(String ipAddress) {
        MainActivity.ipAddress = ipAddress;
        fullUrl = "http://" + ipAddress + ":8111/hudmsg?lastEvt=0&lastDmg=0";
    }

    public static String getFullUrl() {
        return fullUrl;
    }

    public static int getIdHigh() {
        return idHigh;
    }

    public static void setIdHigh(int idHigh) {
        MainActivity.idHigh = idHigh;
    }

    public static void lowerHostileTanks() {
        vehicles.hostileTankDown();
    }

    public static void lowerFriendlyTanks() {
        vehicles.friendlyTankDown();
    }

    public static void lowerHostilePlanes() {
        vehicles.hostilePlaneDown();
    }

    public static void lowerFriendlyPlanes() {
        vehicles.friendlyPlaneDown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        displayFriendlyTanks = findViewById(R.id.friendlyTanksView);
        displayFriendlyPlanes = findViewById(R.id.friendlyPlanesView);
        displayHostileTanks = findViewById(R.id.hostileTanksView);
        displayHostilePlanes = findViewById(R.id.hostilePlanesView);

        mConnectionStatus = findViewById(R.id.textViewStatus);

        Button buttonReset = findViewById(R.id.buttonReset);
        View.OnClickListener resetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vehicles.resetVehicles();
                displayFriendlyTanks.setText(String.valueOf(vehicles.getFriendlyTanks()));
                displayFriendlyPlanes.setText(String.valueOf(vehicles.getFriendlyPlanes()));
                displayHostileTanks.setText(String.valueOf(vehicles.getHostileTanks()));
                displayHostilePlanes.setText(String.valueOf(vehicles.getHostilePlanes()));
                Log.d(TAG, "onClick: Reset Called. Id High = " + getIdHigh());
            }
        };
        buttonReset.setOnClickListener(resetListener);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SettingsActivity.MY_PREFS_NAME, MODE_PRIVATE);
        ParseJSON.setTagName(pref.getString("squadronTag", "ABCDE"));
        setIpAddressAndFullUrl(pref.getString("ipAddress", "192.168.1.1"));

        final Handler handlerDisplayData = new Handler();
        final int displayDelay = 200; //milliseconds
        handlerDisplayData.postDelayed(new Runnable() {
            public void run() {
                displayFriendlyTanks.setText(String.valueOf(vehicles.getFriendlyTanks()));
                displayFriendlyPlanes.setText(String.valueOf(vehicles.getFriendlyPlanes()));
                displayHostileTanks.setText(String.valueOf(vehicles.getHostileTanks()));
                displayHostilePlanes.setText(String.valueOf(vehicles.getHostilePlanes()));
                mConnectionStatus.setText(status);
                handlerDisplayData.postDelayed(this, displayDelay);
            }
        }, displayDelay);

        final Handler handlerDownloadData = new Handler();
        final int downloadDelay = 1000;
        handlerDownloadData.postDelayed(new Runnable() {
            public void run() {
                syncTasks();
                handlerDownloadData.postDelayed(this, downloadDelay);
            }
        }, downloadDelay);
    }

    private void syncTasks() {

        ParseJSON parseJSON = new ParseJSON();
        try {
            if (parseJSON.getStatus() != AsyncTask.Status.RUNNING) {
                parseJSON.cancel(true);
                parseJSON = new ParseJSON();
                parseJSON.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity_TSK", "Error: " + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ID_HIGH, getIdHigh());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setIdHigh(savedInstanceState.getInt(ID_HIGH));
        super.onRestoreInstanceState(savedInstanceState);
    }
}