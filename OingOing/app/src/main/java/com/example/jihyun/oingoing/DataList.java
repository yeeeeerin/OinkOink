package com.example.jihyun.oingoing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jihyun on 2017-05-04.
 */

public class DataList extends AppCompatActivity {
    private DataDetailsAdapter dataDetailsAdapter;
    private static int id = 1;
    private ListView lvPersonNameList;
    private static ArrayList<DataDetailsModel> dataDetailsModelArrayList = new ArrayList<>();
    final static String LOG_TAG = "myLogs";
    private Realm myRealm;
    private static DataList instance;
    private AlertDialog.Builder subDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
        myRealm = Realm.getInstance(DataList.this);
        instance=this;
        setPersonDetailsAdapter();
        getAllUsers();
    }

    public static DataList getInstance() {
        Log.e(LOG_TAG, "DataList.getInstance");
        return instance;
    }
    private void setPersonDetailsAdapter() {
        Log.e(LOG_TAG, "DataList.setPersonDetailsAdapter");
        dataDetailsAdapter = new DataDetailsAdapter(DataList.this, dataDetailsModelArrayList);
        lvPersonNameList.setAdapter(dataDetailsAdapter);
    }
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
    public void deleteData(int personId, int position) {
        Log.e(LOG_TAG, "DataList.deletePerson");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        dataDetailsModelArrayList.remove(position);
        dataDetailsAdapter.notifyDataSetChanged();
    }
    public DataDetailsModel searchData(int personId) {
        Log.e(LOG_TAG, "DataList.searchPerson");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }
    public void addOrUpdatePersonDetailsDialog(final DataDetailsModel model,final int position) {
//subdialog
        Log.e(LOG_TAG, "DataList.addOrUpdatePersonDetailsDialog");
        subDialog = new AlertDialog.Builder(DataList.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
//maindialog
        LayoutInflater li = LayoutInflater.from(DataList.this);
        View promptsView = li.inflate(R.layout.income_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(DataList.this);
        mainDialog.setView(promptsView);
        final EditText etAddPersonName = (EditText) promptsView.findViewById(R.id.setCategory);
        final EditText etAddPersonAge = (EditText) promptsView.findViewById(R.id.setIncome);
        if (model != null) {
            etAddPersonName.setText(model.getName());
            etAddPersonAge.setText(String.valueOf(model.getPrice()));
        }
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = mainDialog.create();
        dialog.show();
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddPersonName) && !Utility.isBlankField(etAddPersonAge)) {
                    DataDetailsModel dataDetailsModel = new DataDetailsModel();
                    dataDetailsModel.setName(etAddPersonName.getText().toString());
                    dataDetailsModel.setPrice(Integer.parseInt(etAddPersonAge.getText().toString()));
                    if (model == null)
                        addDataToRealm(dataDetailsModel);
                    else
                        updatePersonDetails(dataDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }
    private void addDataToRealm(DataDetailsModel model) {
        Log.e(LOG_TAG, "DataList.addDataToRealm");
        myRealm.beginTransaction();
        DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
        dataDetailsModel.setId(id+dataDetailsModelArrayList.size()); //id+남아있는리스트개수를 해줘야해
        dataDetailsModel.setName(model.getName());
        dataDetailsModel.setPrice(model.getPrice());
        dataDetailsModelArrayList.add(dataDetailsModel);
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();
        id++;
    }

    public void updatePersonDetails(DataDetailsModel model,int position,int personID) {
        Log.e(LOG_TAG, "DataList.updatePersonDetails");
        DataDetailsModel editPersonDetails = myRealm.where(DataDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setPrice(model.getPrice());
        myRealm.commitTransaction();
        dataDetailsModelArrayList.set(position, editPersonDetails);
        dataDetailsAdapter.notifyDataSetChanged();
    }
    protected void onDestroy() {
        Log.e(LOG_TAG, "DataList.onDestroy");
        super.onDestroy();
        dataDetailsModelArrayList.clear();
        myRealm.close();
    }
}