package com.example.jihyun.oingoing;

import android.widget.EditText;

/**
 * Created by jihyun on 2017-05-04.
 */

public class Utility {

    public static boolean isBlankField(EditText etPersonData) {
        return etPersonData.getText().toString().trim().equals("");
    }
}