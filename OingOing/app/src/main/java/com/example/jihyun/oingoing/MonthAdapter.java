package com.example.jihyun.oingoing;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Calendar;
import java.util.Date;


public class MonthAdapter extends BaseAdapter {

    Context context;
    // 하루
    dateitem[] items;
    Calendar calendar;

    private int countColumn = 7;

    int firstDay;
    int lastDay;
    int curYear;
    int curMonth;

    // 생성자
    public MonthAdapter(Context context) {
        super();
        this.context = context;
        init();
    }

    public MonthAdapter(Context context, AttributeSet attrs) {
        super();
        this.context = context;
        init();
    }

    // 초기화
    private void init()  {
        items = new dateitem[7 * 6];

        Date date = new Date();
        // calendar에 현재 시간 설정
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        recalculate();
        resetDayNumbers();
    }

    // 이전월로 설정
    public void setPriviousMonth() {
        //이전월로
        calendar.add(Calendar.MONTH, -1);
        // 계산 다시
        recalculate();
        // items에 내용설정
        resetDayNumbers();

    }

    public void setNextMonth() {
        calendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
    }

    public int getCurrentYear() {
        return curYear;
    }

    public int getCurrentMonth() {
        return curMonth + 1;
    }

    //시작하는요일
    public void recalculate() {
        // 현재 월에 1일자로 설정
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 해당되는 주의 몇번째 day?
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        firstDay = getFistDay(dayOfWeek);

        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        lastDay = getLastDay(curYear,curMonth);
    }

    public void resetDayNumbers() {
        for (int i = 0; i < 42; i++) {
            int dayNumber = (i + 1) - firstDay;
            if (dayNumber < 1 || dayNumber > lastDay) {
                dayNumber = 0;
            }
            // date를 설정
            items[i] = new dateitem(dayNumber);
        }
    }

    public int getFistDay(int dayOfWeek) {
        int result = 0;
        if (dayOfWeek == Calendar.SUNDAY) {
            result = 0;
        } else if (dayOfWeek == Calendar.MONDAY) {
            result = 1;
        } else if (dayOfWeek == Calendar.TUESDAY) {
            result = 2;
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            result = 3;
        } else if (dayOfWeek == Calendar.THURSDAY) {
            result = 4;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            result = 5;
        } else if (dayOfWeek == Calendar.SATURDAY) {
            result = 6;
        }
        return result;
    }

    public int getLastDay(int curYear, int curMonth) {
        switch (curMonth) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                //윤달
                if ((curYear % 4 == 0) && (curYear % 100 != 0) || (curYear % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
        }
    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dateItemView view = null;
        //열
        int columnIndex = position % countColumn;

        if (convertView == null) {
            view = new dateItemView(context.getApplicationContext());
        } else {
            view = (dateItemView) convertView;
        }

        // create a params
        GridView.LayoutParams params = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                120);


        //일요일에 빨간표시
        if (columnIndex == 0) {
            view.textView.setTextColor(Color.RED);
        } else {
            view.textView.setTextColor(Color.BLACK);
        }

        // 오늘표시
        if(items[position].date==0)

            view.setLayoutParams(params);
        view.setDate(items[position].date);
        return view;
    }

}
