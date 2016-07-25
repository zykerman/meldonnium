package com.meldonium.volga;
import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.support.v4.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import java.util.Calendar;

/*AddTrain window*/
public class addTrainFragment extends DialogFragment {
    //public TextView edtBox;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addtrain_dialog, container, false);
        SimpleDateFormat sdf   = new SimpleDateFormat("dd.MM.yy HH:mm");
        SimpleDateFormat sdfd  = new SimpleDateFormat("dd.MM.yy");
        String curDate  = sdf.format(new Date());
        MainActivity.lastDateD = sdfd.format(new Date());

        getDialog().setTitle("Add new training " + MainActivity.lastDateD);
        final TextView edtDateVal      = (TextView) rootView.findViewById(R.id.tvDateVal);
        final TextView edtBoxVal       = (TextView) rootView.findViewById(R.id.tvBoxVal);
        final TextView edtBoxName      = (TextView) rootView.findViewById(R.id.tvBoxName);

        final EditText edtRunVal       = (EditText) rootView.findViewById(R.id.edtRunVal);
        final TextView edtSwimVal      = (TextView) rootView.findViewById(R.id.edtSwimVal);
        final TextView edtLiftVal      = (TextView) rootView.findViewById(R.id.edtLiftVal);
        final TextView edtPullVal      = (TextView) rootView.findViewById(R.id.edtPullVal);
        final TextView edtOwnWeightVal = (TextView) rootView.findViewById(R.id.edtOwnWeightVal);
        final TextView edtNoteVal      = (TextView) rootView.findViewById(R.id.edtNoteVal);

        Button   btnOk        = (Button)   rootView.findViewById(R.id.dismiss);
        Button   btnCancel    = (Button)   rootView.findViewById(R.id.cancel);
        edtDateVal.setText(MainActivity.lastDateD);

        DatePickerDialog.OnDateSetListener myCallBack = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //edtDateVal.setText("" + dayOfMonth + "." + (monthOfYear+1) + "." +  year);
            }
        };
        Calendar c = Calendar.getInstance();
        final DatePickerDialog dialogDate = new DatePickerDialog(getActivity(), myCallBack,
                 c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH) );
        edtDateVal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogDate.show();
            }
        });
        edtBoxVal.setText(Integer.toString(MainActivity.firstBoxNumber+BoxFragment.boxIdx));
        edtRunVal.setText("0", TextView.BufferType.EDITABLE);
        edtSwimVal.setText("0");
        edtLiftVal.setText("0");
        edtPullVal.setText("0");
        edtOwnWeightVal.setText(""+MainActivity.lastOwnWeightVal);
        edtNoteVal.setText("");
        if(BoxFragment.boxIdx < 0) {
            edtBoxVal.setVisibility(rootView.GONE);
            edtBoxName.setVisibility(rootView.GONE);
        }
        edtRunVal.setSelection(edtRunVal.getText().length());
        btnOk.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        MainActivity.lastBoxVal         = Integer.parseInt(edtBoxVal.getText().toString());
                        MainActivity.lastRunVal         = Integer.parseInt(edtRunVal.getText().toString());
                        MainActivity.lastSwimVal        = Integer.parseInt(edtSwimVal.getText().toString());
                        MainActivity.lastLiftVal        = Integer.parseInt(edtLiftVal.getText().toString());
                        MainActivity.lastPullVal        = Integer.parseInt(edtPullVal.getText().toString());
                        MainActivity.lastOwnWeightVal   = Integer.parseInt(edtOwnWeightVal.getText().toString());
                        MainActivity.lastNoteVal        = edtNoteVal.getText().toString();

                        MainActivity.totalRunVal  = MainActivity.totalRunVal  + MainActivity.lastRunVal;
                        MainActivity.totalSwimVal = MainActivity.totalSwimVal + MainActivity.lastSwimVal;
                        dismiss();
                        if(BoxFragment.boxIdx >=0) {
                            MainActivity.boxVal[BoxFragment.boxIdx]++;
                        }
                        Log.i(MainActivity.AppName, "boxIdx="+BoxFragment.boxIdx+", ");
                        BoxFragment.updateBox();
                        StatFragment.updateStat();
                        String strBox    = "box " + MainActivity.lastBoxVal +", ";
                        String strRun    = "";
                        String strSwim   = "";
                        String strLift   = "";
                        String strPull   = "";
                        String strWeight = "";
                        String strNote   = "";

                        if(MainActivity.lastRunVal >=1)  strRun  = "run " +   MainActivity.lastRunVal + " km, ";
                        if(MainActivity.lastSwimVal >=1) strSwim = "swim " +  MainActivity.lastSwimVal + " m, ";
                        if(MainActivity.lastLiftVal >=1) strLift = "lift " +  MainActivity.lastLiftVal + " kg, ";
                        if(MainActivity.lastPullVal >=1) strPull = "pull " +  MainActivity.lastPullVal + ", ";
                        if(MainActivity.lastOwnWeightVal >=1 ) strWeight = "your weight " + MainActivity.lastOwnWeightVal + " kg";

                        HistoryFragment.updateHistory(MainActivity.lastDateD + " "
                        + strBox
                        + strRun
                        + strSwim
                        + strLift
                        + strPull
                        + strWeight
                        + " " + MainActivity.lastNoteVal
                        );
                        MainActivity.saveSettings();
                    }
                }
        );
        //cancel button processing
        btnCancel.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override  public void onClick(View v) {
                        dismiss();
                    }
                }
        );
        return rootView;
    }
}
