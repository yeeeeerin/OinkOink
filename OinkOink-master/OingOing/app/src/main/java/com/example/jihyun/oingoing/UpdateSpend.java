package com.example.jihyun.oingoing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jihyun on 2017-04-30.
 */

public class UpdateSpend extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_IMAGE_CROP = 3;

    EditText editText;
    Button setSepend;
    Button m_cancel;
    Spinner category;
    ArrayAdapter adapter;

    //카메라 관련
    Button mButton_camera;
    Button mButton_album;
    ImageView mPhotoImageView;
    Uri photoURI, albumURI = null;
    Boolean album = false;
    String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatespend);


        editText = (EditText) findViewById(R.id.money);


        //view의 선택버튼과 연결 - > 데이터베이스에 지출 항목이 들어간다.
        setSepend = (Button) findViewById(R.id.setSpendMoney);

        setSepend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inPutMoney = editText.getText().toString();

                //스피너 값 받아오기
               String selItem = (String)category.getSelectedItem();
                //test//
                Toast.makeText(UpdateSpend.this,selItem,Toast.LENGTH_SHORT).show();
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

    public void onClick(View v) {

        switch (v.getId()){

            case (R.id.camera):
                dispatchTakePictureIntent();
                Toast.makeText(UpdateSpend.this,"dd",Toast.LENGTH_SHORT).show();
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

                    //이미지 크롭//cropImage();

                    //이미지띄우기


/*
                    try {
                        Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                        photoURI);

                        mPhotoImageView.setImageBitmap(image_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

*/


                case REQUEST_IMAGE_CAPTURE:
                    cropImage();

                    break;

                case REQUEST_IMAGE_CROP:
                    Bitmap photo = BitmapFactory.decodeFile(photoURI.getPath());
                    mPhotoImageView.setImageBitmap(photo);
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 동기화
                    if (album == false) {
                        mediaScanIntent.setData(photoURI); // 동기화
                    } else if (album == true) {
                        album = false;
                        mediaScanIntent.setData(albumURI); // 동기화
                    }
                    this.sendBroadcast(mediaScanIntent); // 동기화
                    break;
            }

            //mPhotoImageView.setImageDrawable();
        }
    }
}
