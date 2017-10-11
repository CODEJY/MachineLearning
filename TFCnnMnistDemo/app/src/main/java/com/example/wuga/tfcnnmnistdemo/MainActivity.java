package com.example.wuga.tfcnnmnistdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private ImageView imgView;
    private TextView textView;
    private final int MY_PERMISSION_REQUEST = 1;
    private final int PHOTO_REQUEST = 2;
    private RecognizeImg recognizeImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        imgView = (ImageView)findViewById(R.id.image);
        textView = (TextView)findViewById(R.id.text);
        textView.setText("预测结果");
    }

    @Override
    public void onClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
        } else {
            //已经授权
            Log.d("已经授权","执行到这");
            choosePhoto();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSION_REQUEST)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                choosePhoto();
            } else
            {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //选择照片
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PHOTO_REQUEST);
    }
    public void onActivityResult(int req,int res,Intent data) {
        try {
            Log.d("图片识别：","图片识别");
            Uri uri = data.getData();
            Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            imgView.setImageBitmap(bit);
            recognizeImg = new RecognizeImg(MainActivity.this);
            float[] f = recognizeImg.recognize(bit);
            int n = recognizeImg.argmax(f);
            textView.setText(Integer.toString(n));
        } catch (Exception e) {
            Log.d("图片识别异常",e.getMessage());
        }
    }
}
