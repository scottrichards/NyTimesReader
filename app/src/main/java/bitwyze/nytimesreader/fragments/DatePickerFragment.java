package bitwyze.nytimesreader.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by srichard on 2/14/16.
 */
public class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

    private String formattedDate;
    private Boolean setDate = false;
    private Date selectedDate;

    public interface EditNameDialogListener {
        void onPickDate(Boolean setDate,String formattedDate,Date selectedDate);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setDate = true;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        selectedDate = cal.getTime();
        formattedDate = sdf.format(c.getTime());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        FilterFragment listener = (FilterFragment) getActivity();
//        listener.onFinishEditDialog(setDate,formattedDate,selectedDate);

        super.onDismiss(dialog);
    }




}
