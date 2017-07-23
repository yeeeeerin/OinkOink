package com.example.jihyun.oingoing;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

import static com.example.jihyun.oingoing.R.id.setButton;

/**
 * Created by jihyun on 2017-04-30.
 */

public class DailyMoneySet extends AppCompatActivity implements View.OnClickListener{


    private Realm mRealm;


    private Button setbutton;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText startDate, endDate, setMoney;
    private EditText Aim; // 목표

    private AlertDialog.Builder subDialog;//입력 다 안했을때 뜨는 다이얼로




    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailymoneyset);

        mRealm = Realm.getInstance(DailyMoneySet.this);

        dateFormatter = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);
        startDate = (EditText) findViewById(R.id.startdate);
        endDate = (EditText) findViewById(R.id.enddate);
        setMoney = (EditText) findViewById(R.id.setMoney);
        Aim = (EditText)findViewById(R.id.setAim);

        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();
        endDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



        //입력 다 안했을 때 뜨는 다이얼로그
        subDialog = new AlertDialog.Builder(DailyMoneySet.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });


        setbutton=(Button) findViewById(setButton);
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utility.isBlankField(setMoney)) {
                    //금액 가져오기
                    String daily_money = setMoney.getText().toString();
                    String AimName = Aim.getText().toString();
                    int dailymoney= Integer.parseInt(daily_money);
                    String start_date=startDate.getText().toString();
                    String end_date=endDate.getText().toString();




                    //데이터베이스에 추가하기
                    mRealm.beginTransaction();

                    DailyMoneyModel DM = mRealm.createObject(DailyMoneyModel.class);
                    DM.setAimName(AimName);
                    DM.setMoney_set(dailymoney);
                    DM.setStartDate(start_date);
                    DM.setEndDate(end_date);

                    mRealm.commitTransaction();

                    Log.d("ee", AimName +"  " +dailymoney+"  "+start_date);


                    //메인으로 돌아가기
                    Intent intent = new Intent(getApplicationContext(),//현재화면의
                            MainActivity.class);//다음 넘어갈 클래스 지정

                    startActivity(intent);//다음 화면으로 넘어간다
                    finish();
                }
                else{

                    subDialog.show();

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view == startDate) {
            startDatePickerDialog.show();
        } else if(view == endDate) {
            endDatePickerDialog.show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
