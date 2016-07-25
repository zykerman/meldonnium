package com.meldonium.volga;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.Typeface;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment {
    static TextView tvLatestHead;
    static TextView edtBoxSummary;
    static TextView edtBoxLatest;
    static EditText edtBoxTotalRun;
    static EditText edtBoxTotalSwim;
    static EditText edtBoxAverageWeight;
    public StatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtBoxTotalRun = (EditText) getView().findViewById(R.id.edtTotalRunVal);
        edtBoxTotalSwim = (EditText) getView().findViewById(R.id.edtTotalSwimVal);
        edtBoxAverageWeight = (EditText) getView().findViewById(R.id.edtAverageWeightVal);
        updateStat();
        //View view1 = MainActivity.getCurrentFocus();
        //if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)MainActivity.context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //}

    }

    public static String formatLine(String name, String val, int n)
    {
        String line = "";
        int i;
        line = name;
        for(i=0;i<n-name.length()-val.length();i++){
          line = line + ".";
        }
        line = line + val;
        return line;
    }

    public static void updateStat()
    {
        int maxLen = 20;
        edtBoxTotalRun.setText(""+MainActivity.totalRunVal);
        edtBoxTotalSwim.setText(""+MainActivity.totalSwimVal);
        edtBoxAverageWeight.setText(""+MainActivity.lastOwnWeightVal);
        // Check if no view has focus:
    }

}
