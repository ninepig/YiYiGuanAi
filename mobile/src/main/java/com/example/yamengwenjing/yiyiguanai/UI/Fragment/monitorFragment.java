package com.example.yamengwenjing.yiyiguanai.UI.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yamengwenjing.yiyiguanai.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class monitorFragment extends Fragment {


    private Spinner monitorSpinner ;
    private ListView dateListView;
    Context currentContext;

    //adapter for spinner
    private ArrayAdapter<String> spinnerAdapter = null;
    private static final String[] monitorCategory  = {"","moa","fitbit","pregenant Daily"};
    private ArrayAdapter<String> listViewSimpleAdapter;
    private String[] dateStingArray = {"2015/4/26","2015/4/27","2015/4/28","2015/4/29"};

    public monitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_monitor,container,false);
        currentContext = getContext();
        initView(thisView);
        initEvent();

        return thisView;
    }


    private void initView(View thisView) {
         monitorSpinner = (Spinner) thisView.findViewById(R.id.moniter_fragment_chosen_spinner);
        spinnerAdapter = new ArrayAdapter<String>(currentContext,R.layout.moniter_fragment_spinner_item,monitorCategory);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monitorSpinner.setAdapter(spinnerAdapter);
//        monitorSpinner.setVisibility(View.VISIBLE);

        dateListView = (ListView)thisView.findViewById(R.id.moniter_fragment_chosen_listView);
        listViewSimpleAdapter = new ArrayAdapter<String>(currentContext,android.R.layout.simple_list_item_1,android.R.id.text1,dateStingArray);
        dateListView.setAdapter(listViewSimpleAdapter);


    }

    private void initEvent() {
        monitorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0) {
                    Toast.makeText(currentContext, "your choice is " + monitorCategory[position], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
