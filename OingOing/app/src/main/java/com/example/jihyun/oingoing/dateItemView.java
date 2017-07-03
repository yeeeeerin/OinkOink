package com.example.jihyun.oingoing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by samsung on 2017-05-26.
 */

public class dateItemView extends RelativeLayout {

    TextView textView;
    //ImageView imageView;

    public dateItemView(Context context) {
        super (context);
        init(context);
    }

    public dateItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context){
        // month_item뷰 불러오기
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.month_item,this,true);
        textView = (TextView)findViewById(R.id.textView);
        //imageView=(ImageView)findViewById(R.id.imageView);
    }

    public void setDate(int date){
        if(date==0){
            textView.setText("");
        }
        else {
            // day를 문자로 바꿔서
            textView.setText(String.valueOf(date));
        }
    }

}
