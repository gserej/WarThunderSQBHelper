package com.github.gserej.warthundersqbhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ParseJSON.OnDataAvailable {
    public static final String ID_HIGH = "ID_HIGH";
    private static final String TAG = "MainActivity";
    public static String ipAddress = "192.168.1.1";
    static Units units = new Units();
    private static String status;
    private static int idHigh;
    private TextView displayOurTanks;
    private TextView displayOurPlanes;
    private TextView displayEnemyTanks;
    private TextView displayEnemyPlanes;
    private TextView mConnectionStatus;

    public static void setStatus(String status) {
        MainActivity.status = status;
    }

    public static void setIpAddress(String ipAddress) {
        MainActivity.ipAddress = ipAddress;
    }

    public static int getIdHigh() {
        return idHigh;
    }

    public static void setIdHigh(int idHigh) {
        MainActivity.idHigh = idHigh;
    }

    public static void lowerEnemyTanks() {
        units.enemyTankDown();
    }

    public static void lowerOurTanks() {
        units.ourTankDown();
    }

    public static void lowerEnemyPlanes() {
        units.enemyPlaneDown();
    }

    public static void lowerOurPlanes() {
        units.ourPlaneDown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        displayOurTanks = findViewById(R.id.ourTanksView);
        displayOurPlanes = findViewById(R.id.ourPlanesView);
        displayEnemyTanks = findViewById(R.id.enemyTanksView);
        displayEnemyPlanes = findViewById(R.id.enemyPlanesView);

        mConnectionStatus = findViewById(R.id.textViewStatus);


        Button buttonReset = findViewById(R.id.buttonReset);
        View.OnClickListener resetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                units.resetUnits();
                displayOurTanks.setText(String.valueOf(units.getOurTanks()));
                displayOurPlanes.setText(String.valueOf(units.getOurPlanes()));
                displayEnemyTanks.setText(String.valueOf(units.getEnemyTanks()));
                displayEnemyPlanes.setText(String.valueOf(units.getEnemyPlanes()));
                Log.d(TAG, "onClick: Reset Called. Id High = " + getIdHigh());
            }
        };
        buttonReset.setOnClickListener(resetListener);


        final Handler handlerRefresh = new Handler();
        final int delayRefresh = 200; //milliseconds
        handlerRefresh.postDelayed(new Runnable() {
            public void run() {
                displayOurTanks.setText(String.valueOf(units.getOurTanks()));
                displayOurPlanes.setText(String.valueOf(units.getOurPlanes()));
                displayEnemyTanks.setText(String.valueOf(units.getEnemyTanks()));
                displayEnemyPlanes.setText(String.valueOf(units.getEnemyPlanes()));
                mConnectionStatus.setText(status);
                handlerRefresh.postDelayed(this, delayRefresh);
            }
        }, delayRefresh);

        final Handler handlerDownloadData = new Handler();
        final int downloadRefresh = 1000;
        handlerDownloadData.postDelayed(new Runnable() {
            public void run() {
                syncTasks();
                handlerDownloadData.postDelayed(this, downloadRefresh);
            }
        }, downloadRefresh);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(SettingsActivity.MY_PREFS_NAME, MODE_PRIVATE);
        ParseJSON.setTagName(pref.getString("squadronTag", "ACEpl"));
        setIpAddress(pref.getString("ipAddress", "192.168.1.1"));
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

    private void syncTasks() {
        ParseJSON parseJSON = new ParseJSON(this, "http://" + ipAddress + ":8111/hudmsg?lastEvt=0&lastDmg=0");

        try {
            if (parseJSON.getStatus() != AsyncTask.Status.RUNNING) {
                parseJSON.cancel(true);
                parseJSON = new ParseJSON(this, "http://" + ipAddress + ":8111/hudmsg?lastEvt=0&lastDmg=0"); //
                parseJSON.execute();
//                Log.d(TAG, "syncTasks: IP Address is "+ipAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity_TSK", "Error: " + e.toString());
        }
    }

    @Override
    public void onDataAvailable(List<Lines> data, DownloadStatus status) {
//        if (status == DownloadStatus.OK) {
//            if(data!=null) {
//                Log.d(TAG, "onDataAvailable: data is " + data);
//            }
//        } else {
//            Log.e(TAG, "onDataAvailable failed with status " + status);
//        }
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
