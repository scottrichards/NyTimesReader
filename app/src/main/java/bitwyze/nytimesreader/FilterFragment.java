package bitwyze.nytimesreader;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Date;

/**
 * Created by srichard on 2/13/16.
 */
public class FilterFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private String selectedCategory = "All";
    private ArrayAdapter categoryAdapter;
    private Spinner categorySpinner;

    public interface FilterDialogListener {
        void onFinishFilterDialog(String inputText,String sortCriteria,Date startDate);
    }

    public  FilterFragment() {

    }

    public static FilterFragment newInstance(String title) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_filter, container);
        // Create an adapter from the string array resource and use
        // android's inbuilt layout file simple_spinner_item
        // that represents the default spinner in the UI
        categoryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_categories, android.R.layout.simple_spinner_item);
        // Set the layout to use for each dropdown item
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);
        Button onOkBtn = (Button)view.findViewById(R.id.filterOK);
        onOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButtonNewest = (RadioButton)view.findViewById(R.id.radioButtonNewest);
                RadioButton radioButtonOldest = (RadioButton)view.findViewById(R.id.radioButtonOldest);
                FilterDialogListener activity = (FilterDialogListener) getActivity();
                Date now = new Date();
                Boolean isNewChecked = radioButtonNewest.isChecked();
                Boolean isOldestChecked = radioButtonOldest.isChecked();
                String sortCriteria = "";
                if (isNewChecked)
                    sortCriteria = "newest";
                if (isOldestChecked)
                    sortCriteria = "oldest";
                activity.onFinishFilterDialog(selectedCategory,sortCriteria,null);
                dismiss();
            }
        });

        Button onCancelBtn = (Button)view.findViewById(R.id.filterCancel);
        onCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        selectedCategory =  parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }




}
