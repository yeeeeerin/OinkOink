package com.example.jihyun.oingoing;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jihyun on 2017-05-04.
 */

public class DataDetailsAdapter extends BaseAdapter {

    final static String LOG_TAG = "myLogs";
    private ArrayList<DataDetailsModel> dataDetailsArrayList;
    private Context context;
    private LayoutInflater inflater;

    public DataDetailsAdapter(Context context, ArrayList<DataDetailsModel> dataDetailsArrayList) {
        this.context = context;
        this.dataDetailsArrayList = dataDetailsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.e(LOG_TAG, "DataDetailsAdapter.getCount");
        return dataDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e(LOG_TAG, "DataDetailsAdapter.getItem");
        return dataDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.e(LOG_TAG, "DataDetailsAdapter.getItemId");
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(LOG_TAG, "DataDetailsAdapter.getView");
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.inflate_list_item, null);
            holder = new Holder();
            holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.tvPersonPrice=(TextView) v.findViewById(R.id.tvPersonPrice);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        holder.tvPersonName.setText(dataDetailsArrayList.get(position).getName());
        holder.tvPersonPrice.setText(""+dataDetailsArrayList.get(position).getPrice());
        holder.ivEditPesonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, "DataDetailsAdapter.getView.ivEditPesonDetail.onClick");
                DataDetailsModel dataToEditModel= DataList.getInstance().searchData(dataDetailsArrayList.get(position).getId());
                DataList.getInstance().addOrUpdatePersonDetailsDialog(dataToEditModel,position);
            }
        });
        holder.ivDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, "DataDetailsAdapter.getView.ivDeletePerson.onClick");
                ShowConfirmDialog(context,dataDetailsArrayList.get(position).getId(), position);
            }
        });
        return v;
    }
    class Holder {
        TextView tvPersonName, tvPersonPrice;
        ImageView ivDeletePerson, ivEditPesonDetail;
    }
    public static void ShowConfirmDialog(Context context,final int personId,final int position)
    {
        Log.e(LOG_TAG, "DataDetailsAdapter.getView.ShowConfirmDialog");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("정말 삭제하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DataList.getInstance().deleteData(personId,position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
