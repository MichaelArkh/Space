package com.example.space_final.ui.settings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * The date picker dialog
 */
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * Instantiates the dialog
     * @param savedInstanceState
     * @return the required dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), DateFragment.this, year, month, day);
    }

    /**
     * This method is called when a new date is selected
     * @param view the view
     * @param year the year
     * @param month the month
     * @param dayOfMonth the day of the month
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra("selectedCal", c));
    }
}
