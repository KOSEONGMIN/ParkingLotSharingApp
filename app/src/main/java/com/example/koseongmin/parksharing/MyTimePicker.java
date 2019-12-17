package com.example.koseongmin.parksharing;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Calendar;

public class MyTimePicker extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    public Calendar cal = Calendar.getInstance();

    final int M_MIN = 0;	//입력가능 최소값
    final int M_MAX = 59;	//입력가능 최대값
    final int STEP = 15;	//선택가능 값들의 간격


    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    public void setCalendar(Calendar calendar){
        this.cal = calendar;
    }
    Button btnConfirm;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.my_time_picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.picker_hour);
        final NumberPicker minutePicker = (NumberPicker) dialog.findViewById(R.id.picker_minute);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyTimePicker.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onTimeSet(null, hourPicker.getValue(), minutePicker.getValue());
                MyTimePicker.this.getDialog().cancel();
            }
        });


        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(cal.get(Calendar.HOUR_OF_DAY));
        hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //String[] mValues = getArrayWithSteps(M_MIN, M_MAX, STEP);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue((M_MAX - M_MIN) / STEP);
        minutePicker.setValue(cal.get(Calendar.MINUTE) / STEP);
        minutePicker.setDisplayedValues(new String[] {"00", "15", "30", "45"});
        minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);



        builder.setView(dialog)
        // Add action buttons
        /*
        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        })
        */
        ;

        return builder.create();
    }

    public String[] getArrayWithSteps(int min, int max, int step) {

        int number_of_array = (max - min) / step + 1;

        String[] result = new String[number_of_array];

        for (int i = 0; i < number_of_array; i++) {
            result[i] = String.valueOf(min + step * i);
        }
        return result;
    }
}
