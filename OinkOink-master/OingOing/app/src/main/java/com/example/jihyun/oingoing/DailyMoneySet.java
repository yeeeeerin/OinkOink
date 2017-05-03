package com.example.jihyun.oingoing;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jihyun on 2017-04-30.
 */

public class DailyMoneySet extends AppCompatActivity {

    private Spinner spinnerType;
    ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailymoneyset);

        items = new ArrayList<String>();
        items.add("월별");
        items.add("주별");
        items.add("일별");
        items.add("선택");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }
                return v;
            }

            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spinnerType = (Spinner) findViewById(R.id.spinnerType);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setSelection(adapter.getCount());

    }

}
