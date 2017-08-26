package com.example.jihyun.oingoing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
    private Realm myRealm;
    private DataDetailsAdapter dataDetailsAdapter;
    private AlertDialog.Builder subDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;

        String[] content=null;
        String[] content2=null;

        String strMessage = "";
        String sender = "";
        String datee = "";
        String a=""; //지출금액
        String b=""; //지출장소

        Date date = null;

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender += messages[i].getOriginatingAddress();
                strMessage += messages[i].getMessageBody();

                date = new Date(messages[i].getTimestampMillis());

                //날짜format 변경 오류...해결해야함!
                //datee = date.toString();
                Log.d("문자 내용", "수신시간" + datee + "발신자 : " + sender + ", 내용 : " + strMessage);

            }
            Log.d("", "발신자 : " + sender);


            //뷰리스트에 추가하려면 플래그 이용해 던져야함!!
            Intent myIntent = new Intent(context, MainActivity.class);

            // 플래그를 이용해 추가..?
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            myIntent.putExtra("number", sender);
            myIntent.putExtra("price", a);
            myIntent.putExtra("place", b);
            myIntent.putExtra("timestamp", datee);

            context.startActivity(myIntent);

            //카드내역수신문자번호(사용자가 임의 지정) -> 근데 이거 미납중이라던가 그런내용 구분할 필요 있을듯..
            //체크카드랑 신용카드랑 형식 다름 ㅠㅠ
            //국민은행 체킄카드 1588-1688
            if (sender.equals("15881688")) {
                //체크
                content=strMessage.split("\n");
                a=content[4];
                b=content[5];
                //날짜 : content[3], 07/07 13:45 이런형식
                //지출내역에 등록
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();

                //신용카드
                content=strMessage.split("\n");
                content2=content[3].split(" ");
                a=content2[0];
                b=content[4];
                //날짜 : content[3], 2번째 07/07, 3번째 13:45 이런형식
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();
            }
            //신한은행 1544-7200
            else if(sender.equals("15447200")){
                content=strMessage.split("\n");
                content2=content[2].split(" ");
                a=content2[1];
                b=content2[2];
                //날짜 : content2[1], 2번째 07/07, content2[2] 0번째 13:45 이런형식
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();
            }
            //하나은행
            else if(sender.equals("")){
                content=strMessage.split("\n");
                content2=content[2].split(" ");
                a=content2[0];
                b=content2[3];
                //날짜 : content2[2] 1번째 07/07 2번째 13:45 이런형식
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();
            }
            //농협은행
            else if(sender.equals("15881600")){
                content=strMessage.split("\n");
                a=content[2];
                b=content[6];
                //날짜 : content[5], 0번째 07/07 1번째 13:45 이런형식(" "으로구분)
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();
            }
            else if(sender.equals("01049392154")){
                Log.d("ee", "문자왔숑");
                content=strMessage.split("\n");
                content2=content[2].split(" ");
                a=content2[1];
                b=content2[2];
                Toast.makeText(context, "SMS From:" + sender + "\n사용금액: " + a+"\n사용장소: " + b, Toast.LENGTH_LONG).show();
            }
            Log.d("ee", "문자왔숑---");
            //우리
            //기업

            //yerin
            int price = Integer.parseInt(a);
            if(price>0){

                //데이터베이스에 추가
                myRealm.beginTransaction();
                DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
                dataDetailsModel.setId(MainActivity.id);
                dataDetailsModel.setName(b);
                dataDetailsModel.setPrice(price);
                dataDetailsModel.setDate(format.format(date));
                dataDetailsModel.setInOrOut(true); //지출
                //dataDetailsModelArrayList.add(dataDetailsModel);
                myRealm.commitTransaction();
                dataDetailsAdapter.notifyDataSetChanged();
                MainActivity.id++;

            }
            myRealm.close();
        }
    }


}