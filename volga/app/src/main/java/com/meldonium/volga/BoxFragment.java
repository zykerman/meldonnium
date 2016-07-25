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

public class BoxFragment extends Fragment {
    public static int isCreated = 0;
    public static Button[]   cell_btn;
    private LinearLayout[] horizContainers;
    int boxR[];
    int boxG[];
    int boxB[];
    private addTrainFragment dialogTrain;
    public static int newBoxNumber = -1;
    public static int boxIdx = 0;
    public BoxFragment() {
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Context ctx = getActivity();
        final View rootView = inflater.inflate(R.layout.fragment_box, container, false);

        RelativeLayout relativeContainer = new RelativeLayout(ctx);
        relativeContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        ScrollView scrollContainer = new ScrollView(ctx);
        scrollContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        dialogTrain        = new addTrainFragment();
        LinearLayout lay = (LinearLayout) rootView.findViewById(R.id.box_layout);
        lay.setOrientation(LinearLayout.VERTICAL);

        createBoxTable(ctx, lay);
        updateBox();
        scrollContainer.addView(lay);
        relativeContainer.addView(scrollContainer);
        return relativeContainer;
    }

    private void createBoxTable(Context ctx, LinearLayout layout)
    {
        Log.i(MainActivity.AppName, "[createBoxTable]");
        Button btnNewTrain = new Button(getActivity());
        int newTrainId = -1000000;
        layout.addView(btnNewTrain);
        btnNewTrain.setText("New training...");
        final int maxG = 255;
        final int minG = 70;
        final int maxR = 255;
        final int minR = 70;
       /*click on box*/
        OnClickListener oclAddTrain = new OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxFragment.boxIdx = v.getId();
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                dialogTrain.show(fm, "Add new train");
            }
        };
        horizContainers = new LinearLayout[2*MainActivity.nCellsByY];

        int countButtons = MainActivity.nCellsByX*MainActivity.nCellsByY, i, j, n;
        cell_btn = new Button[countButtons];
        boxR = new int[countButtons];
        boxG = new int[countButtons];
        boxB = new int[countButtons];
        int vcnt=0;/*view count*/
        n = 0;
        for(j=0;j<MainActivity.nCellsByY;j++){
            if( n > (MainActivity.lastBoxNumber-MainActivity.firstBoxNumber)) {
               break;
            }
            horizContainers[j] = new LinearLayout(ctx); //for box values
            horizContainers[j].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            horizContainers[j].setOrientation(LinearLayout.HORIZONTAL);
            for(i=0;i<MainActivity.nCellsByX;i++){
                    cell_btn[n] = new Button(ctx);
                    cell_btn[n].setId(vcnt++);
                    int cellIdx   = MainActivity.firstBoxNumber + n;
                    cell_btn[n].setTextSize(11);
                    horizContainers[j].addView(cell_btn[n]);
                    LinearLayout.LayoutParams paramsB = (LinearLayout.LayoutParams) cell_btn[n].getLayoutParams();
                    paramsB.weight = 1 ;
                    paramsB.width  = 2 ;
                    paramsB.height = 80;
                    paramsB.setMargins(6, 6, 6, 6);
                    cell_btn[n].setLayoutParams(paramsB);
                    cell_btn[n].setGravity(/*Gravity.START |*/ Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    cell_btn[n].invalidate();
                    cell_btn[n].setPadding(0, 0, 1, 1);               /*align text inside button*/
                    if( n > (MainActivity.lastBoxNumber-MainActivity.firstBoxNumber)) {
                        //cell_lbl[n].setText("");
                    } else {
                        cell_btn[n].setOnClickListener(oclAddTrain);
                    }

                n = n + 1;
            }
            //layout.addView(horizContainers[2*j+0]);
            layout.addView(horizContainers[j]);
        }
        btnNewTrain.setOnClickListener(oclAddTrain);
        btnNewTrain.setId(newTrainId);
        isCreated = 1;
    }
    /*calls in some times when box created but*/
    /*needs update after loading or adding new train*/
    public static void updateBox() {
        int j, i, n;
        Log.i(MainActivity.AppName, "updateBox");
        if(isCreated == 1) {
            final int maxG = 255;
            final int minG = 70;
            final int maxR = 255;
            final int minR = 70;
            n = 0;
            for (j = 0; j < MainActivity.nCellsByY; j++) {
                for (i = 0; i < MainActivity.nCellsByX; i++) {
                    if (n <= (MainActivity.lastBoxNumber - MainActivity.firstBoxNumber)){
                        int btnColorR = maxR - (int) ((float) (maxR - minR) / (MainActivity.nCellsByX * MainActivity.nCellsByY) * n);
                        if (MainActivity.boxVal[n] > 0) {
                            cell_btn[n].setText(String.valueOf(n+MainActivity.firstBoxNumber) + "|"+
                                                String.valueOf(MainActivity.boxVal[n]));
                            if (MainActivity.boxVal[n] > 0 && MainActivity.boxVal[n] <=5)   { cell_btn[n].setBackgroundResource(R.drawable.border_box_1); }
                            if (MainActivity.boxVal[n] > 5 && MainActivity.boxVal[n] <=10)  { cell_btn[n].setBackgroundResource(R.drawable.border_box_2); }
                            if (MainActivity.boxVal[n] > 10 && MainActivity.boxVal[n] <=15) { cell_btn[n].setBackgroundResource(R.drawable.border_box_3); }
                            if (MainActivity.boxVal[n] > 15 && MainActivity.boxVal[n] <=20) { cell_btn[n].setBackgroundResource(R.drawable.border_box_4); }
                            if (MainActivity.boxVal[n] > 20 ) {                               cell_btn[n].setBackgroundResource(R.drawable.border_box_5); }
                        } else {
                            cell_btn[n].setText(""+String.valueOf(n+MainActivity.firstBoxNumber));
                            cell_btn[n].setBackgroundResource(R.drawable.border_box_0);
                        }
                    }
                    n++;
                }
            }
        }
    }
}
