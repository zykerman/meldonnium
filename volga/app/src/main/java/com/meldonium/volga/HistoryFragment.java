package com.meldonium.volga;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import android.graphics.Typeface;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Color;
import android.os.Environment;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import 	java.io.FileInputStream;
import android.media.MediaScannerConnection;
import android.content.Intent;
import android.net.Uri;
import android.content.BroadcastReceiver;

public class HistoryFragment extends Fragment {
    static String FILENAME = "file";
    static ArrayAdapter<String> adapter;
    static ListView lvMain;
    static List<String> history_list;
    public HistoryFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMain = (ListView) getView().findViewById(R.id.lvMain);
        history_list = new ArrayList<String>();
        adapter      = new ArrayAdapter<String>(MainActivity.context, R.layout.history_item, history_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if((position % 2 == 1)) {
                    textView.setTextColor(Color.BLUE);
                } else {
                    //textView.setTextColor(Color.WHITE);
                    textView.setTextColor(Color.BLUE);
                }
                return textView;
            }
        };
        lvMain.setAdapter(adapter);
        showHistory();
    }
    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(MainActivity.AppName, "Problem creating " + Environment.getExternalStorageDirectory() + "/" + path);
                ret = false;
            }
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            Log.i(MainActivity.AppName, "created " + Environment.getExternalStorageDirectory() + "/" + path);
        } else {
            Log.i(MainActivity.AppName, "folder " + Environment.getExternalStorageDirectory() + "/" + path + " exists");

        }
        return ret;
    }
    public static void writeFile(String addLine) {
        //createDirIfNotExists("meldonnium");
        try {
            File myFile           = new File(Environment.getExternalStorageDirectory() + MainActivity.HistoryName);
            FileOutputStream fOut = new FileOutputStream(myFile, true);//true means append
            BufferedWriter bw     = new BufferedWriter(new OutputStreamWriter(fOut));
            bw.write(addLine);
            bw.newLine();
            bw.flush();
            bw.close();
            Log.d(MainActivity.AppName, "added to history:" + addLine + "\n");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readFile() {
        try {
            FileInputStream fstream = new FileInputStream(Environment.getExternalStorageDirectory() + MainActivity.HistoryName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = "";
            String file_text = "";
            history_list.clear();
            while ((str = br.readLine()) != null) {
                Log.d(MainActivity.AppName, str);
                file_text = file_text + "\n" + str;
                adapter.add(str);
            }
            lvMain.setSelection(adapter.getCount() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateHistory(String addLine){
        writeFile(addLine);
        showHistory();
    }
    public static void showHistory(){
        readFile();
    }
}
