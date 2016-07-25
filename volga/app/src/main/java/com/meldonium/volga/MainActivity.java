package com.meldonium.volga;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.app.DialogFragment;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.Button;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import 	java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.content.Intent;
import android.net.Uri;
import android.content.BroadcastReceiver;
public class MainActivity extends AppCompatActivity {
    public static Context context;

    public static String AppName = "Meldonium";
    public static String FolderName = "meldonnium";
    public static String SettingsName = "/meldonnium/settings.txt";
    public static String HistoryName = "/meldonnium/history.txt";
    ViewPager pager;
    TabLayout tabLayout;
    public static int newBoxIdx    = 0;
    public static int firstBoxNumber   =   1;
    public static int lastBoxNumber   =   300;
    public static int firstBoxNumber_new   =   1;
    public static int lastBoxNumber_new   =  300;

    public static int nCellsByX=8;
    public static int nCellsByY=100;

    public static int lastBoxVal   = 0;
    public static int lastRunVal   = 0;
    public static int lastSwimVal  = 0;
    public static int lastLiftVal  = 0;
    public static int lastPullVal  = 0;
    public static int lastOwnWeightVal     = 0;

    public static String lastTngDate = "no ";
    public static String lastNoteVal = "";

    public static int totalRunVal   = 0;
    public static int totalSwimVal   = 0;
    public static int totalWeightVal   = 0;

    public static AlertDialog alertDialog;

    public static String lastDateD = "";
    private SettingsFragment dialogSettings;
    public static int totalRunDistance  = 0;
    public static int boxVal[];
    public static void createBox(){
        boxVal = new int[nCellsByX*nCellsByY];
        int n = 0;
        for(n=0;n<nCellsByX*nCellsByY;n++){
            boxVal[n] = 0;
        }
    }
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager= (ViewPager) findViewById(R.id.view_pager);
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);

        FragmentManager manager=getSupportFragmentManager();
        PagerAdapter adapter=new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        context          = getApplicationContext();
        dialogSettings   = new SettingsFragment();
        alertDialog      = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Log.i(AppName, "create " + FolderName);
        HistoryFragment.createDirIfNotExists(FolderName);
        MainActivity.createBox();
        loadSettings();
        saveSettings();
        /*create visible folder for windows explorer*/
        {
            File file1 = new File(Environment.getExternalStorageDirectory(), SettingsName);
            File file2 = new File(Environment.getExternalStorageDirectory(), HistoryName);
            MediaScannerConnection.scanFile(MainActivity.context, new String[] { file1.getAbsolutePath() }, new String[] {"image/jpg"}, null);
            MediaScannerConnection.scanFile(MainActivity.context, new String[] { file2.getAbsolutePath() }, new String[] {"image/jpg"}, null);
        }
    }
    public static Context getAppContext() {
        return context;
    }
    protected void onResume() {
        Log.i(AppName, "[onResume]");
        super.onResume();
        BoxFragment.updateBox();
    }
    protected void onStop() {
        Log.i(AppName, "[onStop]");
        saveSettings();
        super.onStop();
    }
    public static void saveSettings() {
        //Log.i(AppName, "saveSettings");
        OutputStream output = null;
        Properties prop = new Properties();
        try {
            int j, i, n;
            output = new FileOutputStream(Environment.getExternalStorageDirectory() + SettingsName);
            n = 0;
            for (j = 0; j < nCellsByY; j++) {
                for (i = 0; i < nCellsByX; i++) {
                    String key = "btn_" + j + "_" + i;
                    String val = "" + boxVal[n];
                    if(boxVal[n]>0) {
                        prop.setProperty(key, val);
                    }
                    n++;
                }
            }
            prop.setProperty("$firstBoxNumber"    , "" + MainActivity.firstBoxNumber);
            prop.setProperty("$lastBoxNumber"     , "" + MainActivity.lastBoxNumber);
            prop.setProperty("$firstBoxNumber_new", "" + MainActivity.firstBoxNumber_new);
            prop.setProperty("$lastBoxNumber_new" , "" + MainActivity.lastBoxNumber_new);
            prop.setProperty("$lastRunVal"        , "" + MainActivity.lastRunVal);
            prop.setProperty("$lastSwimVal"       , "" + MainActivity.lastSwimVal);
            prop.setProperty("$lastLiftVal"       , "" + MainActivity.lastLiftVal);
            prop.setProperty("$lastPullVal"       , "" + MainActivity.lastPullVal);
            prop.setProperty("$lastOwnWeightVal"  , "" + MainActivity.lastOwnWeightVal);
            prop.setProperty("$lastNoteVal"       , "" + MainActivity.lastNoteVal);
            prop.setProperty("$totalRunVal"       , "" + MainActivity.totalRunVal);
            prop.setProperty("$totalSwimVal"      , "" + MainActivity.totalSwimVal);
            prop.setProperty("$totalWeightVal"    , "" + MainActivity.totalWeightVal);
            prop.setProperty("$lastDateD"         , "" + MainActivity.lastDateD);
            prop.store(output, "settings file");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void loadSettings() {
        SharedPreferences settingsActivity = getAppContext().getSharedPreferences(AppName, MODE_PRIVATE);
        Properties props = new Properties();
        String key, val;
        Log.i(AppName, "loadSettings");
        int j, i, n;
        try
        {
            props.load(new FileInputStream(new File(Environment.getExternalStorageDirectory() + SettingsName)));
            n = 0;
            for (j = 0; j < nCellsByY; j++) {
                for (i = 0; i < nCellsByX; i++) {
                    key = "btn_" + j + "_" + i;
                    val = props.getProperty(key, "0");
                    boxVal[n] = Integer.parseInt(val);
                    //Log.i(AppName, "loadSettings:" + key + " = " + val);
                    n++;
                }
            }
            MainActivity.firstBoxNumber_new = Integer.parseInt(props.getProperty("$firstBoxNumber_new", "" + MainActivity.firstBoxNumber_new));
            MainActivity.lastBoxNumber_new  = Integer.parseInt(props.getProperty("$lastBoxNumber_new", "" + MainActivity.lastBoxNumber_new));
            MainActivity.firstBoxNumber     = MainActivity.firstBoxNumber_new;
            MainActivity.lastBoxNumber      = MainActivity.lastBoxNumber_new;
            MainActivity.lastRunVal         = Integer.parseInt(props.getProperty("$lastRunVal", "" + MainActivity.lastRunVal));
            MainActivity.lastSwimVal        = Integer.parseInt(props.getProperty("$lastSwimVal", "" + MainActivity.lastSwimVal));
            MainActivity.lastLiftVal        = Integer.parseInt(props.getProperty("$lastLiftVal", "" + MainActivity.lastLiftVal));
            MainActivity.lastPullVal        = Integer.parseInt(props.getProperty("$lastPullVal", "" + MainActivity.lastPullVal));
            MainActivity.lastOwnWeightVal   = Integer.parseInt(props.getProperty("$lastOwnWeightVal", "" + MainActivity.lastOwnWeightVal));
            MainActivity.lastNoteVal        = props.getProperty("$lastNoteVal", "" + MainActivity.lastNoteVal);
            MainActivity.totalRunVal        = Integer.parseInt(props.getProperty("$totalRunVal", "" + MainActivity.totalRunVal));
            MainActivity.totalSwimVal       = Integer.parseInt(props.getProperty("$totalSwimVal", "" + MainActivity.totalSwimVal));
            MainActivity.totalWeightVal     = Integer.parseInt(props.getProperty("$totalWeightVal", "" + MainActivity.totalWeightVal));
            MainActivity.lastDateD          = props.getProperty("$lastDateD", "" + MainActivity.lastDateD);

        }
        catch (IOException ex) {
                ex.printStackTrace();
        }

    }
    @Override public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
                dialogSettings.show(fm, "Settings");
                return true;
            case R.id.about:
                alertDialog.setTitle("About");
                alertDialog.setMessage("Fitness tracker\n" + "(c) Bakshaev Andrey\n Nizhny Novgorod");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            case R.id.exit:
                saveSettings();
                this.finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

