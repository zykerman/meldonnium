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

/*Settings window*/
public class SettingsFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        getDialog().setTitle("Settings ");
        final TextView edtBoxFirstVal = (TextView) rootView.findViewById(R.id.edtBoxFirstVal);
        final TextView edtBoxLastVal  = (TextView) rootView.findViewById(R.id.edtBoxLastVal);

        Button btnOk = (Button) rootView.findViewById(R.id.dismiss);
        Button btnCancel = (Button) rootView.findViewById(R.id.cancel);
        edtBoxFirstVal.setText(Integer.toString(MainActivity.firstBoxNumber));
        edtBoxLastVal.setText(Integer.toString(MainActivity.lastBoxNumber));
        //settings window ok button processing
        btnOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int fbox = Integer.parseInt(edtBoxFirstVal.getText().toString());
                        int lbox = Integer.parseInt(edtBoxLastVal.getText().toString());
                        if(lbox>=fbox) {
                            MainActivity.firstBoxNumber_new = fbox;
                            MainActivity.lastBoxNumber_new  = lbox;
                            dismiss();
                            MainActivity.saveSettings();
                            MainActivity.alertDialog.setTitle("Warning");
                            MainActivity.alertDialog.setMessage("Changes will take affect after restart\n");
                            MainActivity.alertDialog.show();
                        } else {
                            MainActivity.alertDialog.setTitle("Error");
                            MainActivity.alertDialog.setMessage("First box index should be less or equal last box index");
                            MainActivity.alertDialog.show();
                        }
                    }
                }
        );
        //cancel button processing
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );
        return rootView;
    }
}
