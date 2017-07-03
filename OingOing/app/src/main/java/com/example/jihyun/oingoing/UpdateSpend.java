package com.example.jihyun.oingoing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jihyun on 2017-04-30.
 */

public class UpdateSpend extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CROP = 2;

    EditText editText; //돈 입력
    Button setSepend; //선택
    Button m_cancel; //취소
    Spinner category; //스피너 선택
    ArrayList<String> items;//스피너목록
    ArrayAdapter adapter;

    //카메라 관련
    Button mButton_camera; //카메라 선택
    Button mButton_album; //앨번 선택
    ImageView mPhotoImageView; //선택한 이미지 보여주는 창
    Uri photoURI, albumURI = null;
    Boolean album = false;
    String mCurrentPhotoPath;

    //데이터베이스부분
    private static int id=1;
    private Realm myRealm;
    private static ArrayList<DataDetailsModel> dataDetailsModelArrayList = new ArrayList<>();
    private DataDetailsAdapter dataDetailsAdapter;
    private AlertDialog.Builder subDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatespend);

        myRealm = Realm.getInstance(UpdateSpend.this);
        dataDetailsAdapter = new DataDetailsAdapter(UpdateSpend.this, dataDetailsModelArrayList);
        getAllUsers(); // 리스트에 데이터베이스의 모든 데이터 넣기

        //입력 다 안했을 때 뜨는 다이얼로그
        subDialog = new AlertDialog.Builder(UpdateSpend.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });

        editText = (EditText) findViewById(R.id.money);


        //view의 선택버튼과 연결 - > 데이터베이스에 지출 항목이 들어간다. ->mainActivity와 연결
        setSepend = (Button) findViewById(R.id.setSpendMoney);

        setSepend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //만약 칸이 비워져있지 않다면?
                if(!Utility.isBlankField(editText)) {
                    //금액 가져오기
                    String inPutMoney = editText.getText().toString();
                    int money= Integer.parseInt(inPutMoney);



                    //스피너 값 받아오기
                    String selItem = (String)category.getSelectedItem();


                    //데이터베이스에 추가하기
                    Date d = new Date();

                    myRealm.beginTransaction();
                    DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
                    dataDetailsModel.setId(id+dataDetailsModelArrayList.size());
                    dataDetailsModel.setName(selItem);
                    dataDetailsModel.setPrice(money);
                    dataDetailsModel.setDate(d);
                    dataDetailsModel.setInOrOut(true); //지출
                    dataDetailsModelArrayList.add(dataDetailsModel);
                    myRealm.commitTransaction();
                    dataDetailsAdapter.notifyDataSetChanged();
                    id++;

                    //메인으로 돌아가기
                    Intent intent = new Intent(getApplicationContext(),//현재화면의
                            MainActivity.class);//다음 넘어갈 클래스 지정

                    startActivity(intent);//다음 화면으로 넘어간다
                }
                else{
                    subDialog.show();
                }
                //test//
                //Toast.makeText(UpdateSpend.this,selItem,Toast.LENGTH_SHORT).show();




            }
        });

        //view의 취소버튼과 연결 -> mainActicity와 연결
        m_cancel = (Button) findViewById(R.id.out_page);

        m_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),//현재화면의
                        MainActivity.class);//다음 넘어갈 클래스 지정

                startActivity(intent);//다음 화면으로 넘어간다
            }
        });


        //스피너 처리
        category = (Spinner) findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.USSpinner,//배열 가져온다
                android.R.layout.simple_spinner_item);//어떤형식으로

        category.setAdapter(adapter);//스피너와 연결!!




        //카메라 (사진찍기)
        mButton_camera = (Button) findViewById(R.id.camera);
        mButton_album = (Button) findViewById(R.id.album);
        mPhotoImageView = (ImageView) findViewById(R.id.cameraView);

        mButton_album.setOnClickListener(this);
        mButton_camera.setOnClickListener(this);
        //mButton.setOnClickListener(this);


    }

    //데이터베이스의 리스트를 저장
    private void getAllUsers() {
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

    public void onClick(View v) {

        switch (v.getId()){

            case (R.id.camera):
                dispatchTakePictureIntent();

                break;
            case (R.id.album):
                doTakeAlbumAction();

                break;
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(UpdateSpend.this,"ee",Toast.LENGTH_SHORT).show();
            File photoFile = null;
            try {

                photoFile = createImageFile(); // 사진찍은 후 저장할 임시 파일
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "createImageFile Failed",
                        Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile); // 임시 파일의 위치,경로 가져옴
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // 임시 파일 위치에 저장
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


            }
        }

    }


    //저장할 폴더 생성
    private File createImageFile() throws IOException {


        // 특정 경로와 폴더를 지정하지 않고, 메모리 최상 위치에 저장 방법
        String imageFileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory(), imageFileName);
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }


    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }


    private void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(photoURI, "image/*");
        cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
        cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기

        if (album == false) {
            cropIntent.putExtra("output", photoURI); // 크랍된 이미지를 해당 경로에 저장
        }
        else if (album == true) {
            cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        }
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }


    // ActivityResult = 가져온 사진 뿌리기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "onActivityResult : RESULT_NOT_OK", Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: // 앨범 이미지 가져오기 album = true;
                    album = true;
                    File albumFile = null;
                    try {
                        albumFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (albumFile != null) {
                        albumURI = Uri.fromFile(albumFile); // 앨범 이미지 Crop한 결과는 새로운 위치 저장
                    }
                    photoURI = data.getData(); // 앨범 이미지의 경로



                case REQUEST_IMAGE_CAPTURE:
                    cropImage();

                    break;

                case REQUEST_IMAGE_CROP:
                    final Bundle extras = data.getExtras();

                    if (extras != null) {
                        Bitmap photo;
                        if(album)
                            photo = BitmapFactory.decodeFile(albumURI.getPath());
                        else
                            photo = BitmapFactory.decodeFile(photoURI.getPath());

                        mPhotoImageView.setImageBitmap(photo);
                    }


                    // 임시 파일 삭제
                    File f = new File(photoURI.getPath());
                    if (f.exists()) {
                        f.delete();
                    }

                    break;

//
            }


        }
    }

    // 앱이 종료되었을  onCreate와 반대로 액티비티가 종료 될 때 onDestroy가 나타난다
    protected void onDestroy() {
        Log.e("ee", "MainActivity.onDestroy");
        super.onDestroy();
        dataDetailsModelArrayList.clear();
        myRealm.close();
    }
}