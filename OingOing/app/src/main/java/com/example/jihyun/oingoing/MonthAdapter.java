package com.example.jihyun.oingoing;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


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

    //일일설정액과 비교하여 클리어했으면 true를 아니면 false
    private boolean Clear_or_NoneClear(String SetDate){

        int price_sum = 0;
        int money_set=0;
        boolean flag_moneysumExist=false;
        Realm myRealm = Realm.getInstance(context);

        //moneyset가져오는 부분
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-M-d").parse(SetDate);
            RealmResults<DailyMoneyModel> results_moneyset = myRealm.where(DailyMoneyModel.class)
                    .lessThanOrEqualTo("startDate",d)
                    .greaterThanOrEqualTo("endDate",d)
                    .findAll();

            myRealm.beginTransaction();

            if (results_moneyset.size()>0) {
                flag_moneysumExist=true;
                money_set = results_moneyset.get(0).getMoney_set();
            }
            Log.d("clear2",String.valueOf(money_set));

            myRealm.commitTransaction();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //moneyset이 존재하다면 그 날짜에 해당하는 데이터를 가져와 moneyset과 비교
        if(flag_moneysumExist) {

            RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("date", SetDate).findAll();
            myRealm.beginTransaction();
            for (int i = 0; i < results.size(); i++) {
                price_sum += results.get(i).getPrice();
            }
            myRealm.commitTransaction();

            Log.d("clear2","금액 합산"+String.valueOf(price_sum));

            if(money_set>price_sum)
                return true;


        }

        myRealm.close();
        //myRealm2.close();
        return false;
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
                GridView.LayoutParams.MATCH_PARENT, 120);






        //일요일에 빨간표시
        if (columnIndex == 0) {
            view.textView.setTextColor(Color.RED);
        } else {
            view.textView.setTextColor(Color.BLACK);
        }

        //클리어 했을 시 //꼭 이 위치에 넣어야 함
        if(position>0 && position<32) {
            String setDate = getCurrentYear() + "-" + getCurrentMonth() + "-" + position;
            try {
                Date md_cal = new SimpleDateFormat("yyyy-M-d").parse(setDate);
                Date md_local=new Date(); // 현재 날짜
               // String fd=new SimpleDateFormat("yyyy-M-d").format(md_local);
               // md_local= new SimpleDateFormat("yyyy-M-d").parse(fd);


                if(Clear_or_NoneClear(setDate)==true && md_cal.compareTo(md_local)<0){
                    view.textView.setTextColor(Color.GREEN);
                    Log.d("clear2", setDate);
                }
            }catch (ParseException e){

            }




        }

        // 오늘표시
        if(items[position].date==0) {
            view.setLayoutParams(params);
        }
        view.setDate(items[position].date);


        //달력이 안움직임
       // Date d=new Date();
       // calendar=Calendar.getInstance();
       // calendar.setTime(d);
        //if(items[position].date==calendar.get(Calendar.DAY_OF_MONTH)){
         //   view.textView.setTextColor(Color.BLUE);
       // }


        return view;
    }

}