package com.example.imageeditor;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_PERM_CODE  = 101 ;
    ImageView imageView;
    Button cameraBtn;
    Button galleryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.displayImageViewID);
        cameraBtn=findViewById(R.id.cameraBtnID);
        galleryBtn=findViewById(R.id.galleryBtnID);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "dugme kliknuto", Toast.LENGTH_LONG).show();
//                //verifyPermissions();
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                try {
//                    startActivityForResult(takePictureIntent, REQUEST_CODE);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            openCamera();

        }
    }

    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            //imageView.setImageBitmap(imageBitmap);
//            System.out.println("*********************** : "+imageBitmap);
//        }
//    }
//    private void verifyPermissions(){
//        String [] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
//
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                permissions[0]) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                permissions[1]) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                permissions[2]) == PackageManager.PERMISSION_GRANTED){
//
//        }else{
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    permissions,
//                    REQUEST_CODE);
//        }
//    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //verifyPermissions(); prvi video

       // super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==CAMERA_PERM_CODE){
            if(grantResults.length<0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(this, "Mora biti dozvoljeno koristenje kamera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Toast.makeText(this, "kamera se otvara", Toast.LENGTH_SHORT).show();
    }


}