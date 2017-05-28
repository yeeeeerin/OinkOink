package com.example.jihyun.oingoing;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TabHost tabHost;
    final static String LOG_TAG = "myLogs";
    private static int id = 1;
    //private FloatingActionButton fabAddPerson;
    FloatingActionButton fab1, fab2, fab3, fab4;

    private Realm myRealm;
    private ListView lvPersonNameList;
    private static ArrayList<DataDetailsModel> dataDetailsModelArrayList = new ArrayList<>();
    private DataDetailsAdapter dataDetailsAdapter;
    private AlertDialog.Builder subDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "MainActivity.OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRealm = Realm.getInstance(MainActivity.this);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
        dataDetailsAdapter = new DataDetailsAdapter(MainActivity.this, dataDetailsModelArrayList);
        getAllWidgets();
        getAllUsers();//0528
        bindWidgetsWithEvents();

        tabHost=(TabHost)findViewById(R.id.tabHost);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabMonth).setIndicator("월별"));
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabWeek).setIndicator("주별"));
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabDay).setIndicator("일별"));

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

        ImageView viewList=(ImageView) findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DataList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
//추가
        fab1 = (FloatingActionButton)findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_2);
        fab4 = (FloatingActionButton)findViewById(R.id.fab_4);
        fab3 = (FloatingActionButton)findViewById(R.id.fab_3);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새로추가를 눌렀을 때 null을 줌
                addOrUpdatePersonDetailsDialog(null,-1);
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateSpend.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void ToggleFab() {
        // 버튼들이 보여지고있는 상태인 경우 숨겨줍니다.
        if(fab1.getVisibility() == View.VISIBLE) {
            fab1.hide();
            fab2.hide();
            fab4.hide();
            fab1.animate().translationY(0);
            fab2.animate().translationY(0);
            fab4.animate().translationY(0);
        }
        // 버튼들이 숨겨져있는 상태인 경우 위로 올라오면서 보여줍니다.
        else {
            // 중심이 되는 버튼의 높이 + 마진 만큼 거리를 계산합니다.
            int dy = fab3.getHeight() + 20;
            fab1.show();
            fab2.show();
            fab4.show();
            // 계산된 거리만큼 이동하는 애니메이션을 입력합니다.
            fab4.animate().translationY(-dy*3);
            fab1.animate().translationY(-dy*2);
            fab2.animate().translationY(-dy);
        }
    }
    //동그라미버튼
    private void getAllWidgets() {
        Log.e(LOG_TAG, "MainActivity.getAllWidgets");
        //fabAddPerson = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_2);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
    }
    private void bindWidgetsWithEvents() {
        Log.e(LOG_TAG, "MainActivity.bindWidgetsWithEvents");
        //fabAddPerson.setOnClickListener(this);
        fab2.setOnClickListener(this);

    }

    //수정
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fab_2:
//                addOrUpdatePersonDetailsDialog(null,-1);
//                break;
//            case R.id.fab_1:
//                Toast.makeText(MainActivity.this, "영수증인식", Toast.LENGTH_SHORT).show();
//                //Intent intent = new Intent(getApplicationContext(), UpdateSpend.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //getApplicationContext().startActivity(intent);
//                break;
//
        //       }
    }

    //다이얼로그를 열어 데이터를 추가 + 삭제 하는 함수
    public void addOrUpdatePersonDetailsDialog(final DataDetailsModel model,final int position) {
//subdialog
        Log.e(LOG_TAG, "MainActivity.addOrUpdatePersonDetailsDialog");
        subDialog = new AlertDialog.Builder(MainActivity.this)//입력이 다 되지 아니하였을 때 나타나는 다이얼로그
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
//maindialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);//뷰를 띄워주는 역할
        View promptsView = li.inflate(R.layout.income_dialog, null);//정보를 입력하는 창 연결, 뷰 생성
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);//다이얼 로그 생성하기 위한 빌더 얻기
        mainDialog.setView(promptsView);//알림창 지정된 레이아웃을 띄운다


        //이 변수들은 income_dialog.xml에서 가져온 아이들, 즉 한 엑티비티에 뷰를 두개 가져온 것이다
        //위에서 View promptsViewView이 문장을 통해 뷰를 생성했기 때문에 사용이 가능하다
        final EditText etAddCategory = (EditText) promptsView.findViewById(R.id.setCategory);
        final EditText etAddIncome = (EditText) promptsView.findViewById(R.id.setIncome);

        //모델이 없다면, 즉 새로운 데이터를 입력한다면
        //버튼을 눌렀을 때 이 함수에 null,-l을 매개변수로 주는것을 볼 수 있다. null을 준 의미가 새로운 데이터를 생성하기 위함임
        //뷰를 띄우고 기다림
        if (model != null) {
            etAddCategory.setText(model.getName());
            etAddIncome.setText(String.valueOf(model.getPrice()));
        }
        mainDialog.setCancelable(false)//back키 설정 안함
                .setPositiveButton("Ok", null)//ok버튼 설정
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {// cancel버튼 설정
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = mainDialog.create();//다이얼 로그 객체 얻어오기
        dialog.show();// 다이얼로그 보여주기
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);//ok버튼 누르게 된다면
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력칸이 비어있는지 확인하고 다 채워졌다면 데이터를 추가 or업데이트, 빈 칸이 있다면 채우라는 다이얼로그띄움
                if (!Utility.isBlankField(etAddCategory) && !Utility.isBlankField(etAddIncome)) {
                    DataDetailsModel dataDetailsModel = new DataDetailsModel();
                    dataDetailsModel.setName(etAddCategory.getText().toString());
                    dataDetailsModel.setPrice(Integer.parseInt(etAddIncome.getText().toString()));
                    if (model == null)//데이터베이스를 새로 생성하겠다!!
                        addDataToRealm(dataDetailsModel);
                    else//기존에 있던 데이터를 업데이트하겠다!!
                        updatePersonDetails(dataDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {//다이얼 로그가 비워져 있다면 이것을 보여줘!!
                    subDialog.show();
                }
            }
        });
    }
    //데이터 삽입함수
    private void addDataToRealm(DataDetailsModel model) {
        Log.e(LOG_TAG, "MainActivity.addDataToRealm");
        myRealm.beginTransaction();//Transaction을 이용한 삽입, 성능이 좋아
        DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
        dataDetailsModel.setId(id+dataDetailsModelArrayList.size());//0528
        dataDetailsModel.setName(model.getName());
        dataDetailsModel.setPrice(model.getPrice());
        dataDetailsModelArrayList.add(dataDetailsModel);
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();
        id++;
    }
    //데이터 업데이트 함(수정)
    public void updatePersonDetails(DataDetailsModel model,int position,int personID) {
        Log.e(LOG_TAG, "MainActivity.updatePersonDetails");
        DataDetailsModel editPersonDetails = myRealm.where(DataDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setPrice(model.getPrice());
        myRealm.commitTransaction();
        dataDetailsModelArrayList.set(position, editPersonDetails);
        dataDetailsAdapter.notifyDataSetChanged();
    }

    //0528
    //데이터 리스트 가져오는 함수
    private void getAllUsers() {
        Log.e(LOG_TAG, "DataList.getAllUsers");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            dataDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(DataDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();
    }

    // db삭제
    // 앱이 종료되었을  onCreate와 반대로 액티비티가 종료 될 때 onDestroy가 나타난다
    protected void onDestroy() {
        Log.e(LOG_TAG, "MainActivity.onDestroy");
        super.onDestroy();
        dataDetailsModelArrayList.clear();
        myRealm.close();
    }
}