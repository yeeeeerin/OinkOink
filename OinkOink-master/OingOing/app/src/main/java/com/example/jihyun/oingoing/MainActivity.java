package com.example.jihyun.oingoing;

import android.app.TabActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UpdateSpend.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }

        });



        TabHost tabHost=getTabHost() ;
        TabHost.TabSpec tabSpecMonth=tabHost.newTabSpec("tabMonth").setIndicator("월별");
        tabSpecMonth.setContent(R.id.tabMonth);
        tabHost.addTab(tabSpecMonth);


        TabHost.TabSpec tabSpecWeek=tabHost.newTabSpec("tabWeek").setIndicator("주별");
        tabSpecWeek.setContent(R.id.tabWeek);
        tabHost.addTab(tabSpecWeek);

        TabHost.TabSpec tabSpecDay=tabHost.newTabSpec("tabDay").setIndicator("일별");
        tabSpecDay.setContent(R.id.tabDay);
        tabHost.addTab(tabSpecDay);

        tabHost.setCurrentTab(0);

        ImageView addbtn=(ImageView) findViewById(R.id.addBtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DailyMoneySet.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                getApplicationContext().startActivity(intent);
            }
        });

    }


    public boolean onCreateOptionMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        SubMenu sMenu1 = menu.addSubMenu("통계 조회 >>");
        sMenu1.add(0,1,0,"월별 조회");
        sMenu1.add(0,2,0,"주별 조회");
        sMenu1.add(0,3,0,"일별 조회");

        SubMenu sMenu2 = menu.addSubMenu("도장 개수 >>");
        sMenu2.add(0,4,0,"월별 조회");
        sMenu2.add(0,5,0,"연간 조회");

        return true;
    }



}
