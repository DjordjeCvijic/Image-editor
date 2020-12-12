package com.example.imageeditor;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_PERM_CODE  = 101 ;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int READ_PERM_CODE =110 ;
    private static final int WRITE_PERM_CODE =111 ;
    private static final int REQUEST_CODE =200 ;
    private boolean readGranted=false;
    private boolean writeGranted=false;
    String currentPhotoPath;//za sada samo objekat klase File ka ne kreiranom fileu
    ImageView imageView;
    Button cameraBtn;
    Button galleryBtn;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.displayImageViewID);
        cameraBtn=findViewById(R.id.cameraBtnID);
        galleryBtn=findViewById(R.id.galleryBtnID);
        saveBtn=findViewById(R.id.saveBtnID);

        askPermissions();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "KAMERA", Toast.LENGTH_SHORT).show();
                //askCameraPermissions();
                dispatchTakePictureIntent();


            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FILE_NAME = "example.txt";
                String text ="smjesteno u memoriju";
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(text.getBytes());
                    Toast.makeText(MainActivity.this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_SHORT).show();
                    System.out.println("********************* "+getFilesDir());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageInGallery();
            }
        });


    }

    private void askPermissions() {

                String [] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissions,
                    REQUEST_CODE);
        }

    }




    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==CAMERA_PERM_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                dispatchTakePictureIntent();
            }else{
                Toast.makeText(this, "Mora biti dozvoljeno koristenje kamera", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==READ_PERM_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                readGranted=true;
            }else{
                Toast.makeText(this, "Mora biti dozvoljeno citanje iz memorije", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==WRITE_PERM_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                writeGranted=true;
            }else{
                Toast.makeText(this, "Mora biti dozvoljeno upisivanje", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode==REQUEST_CODE ){
            System.out.println("******************* rezultat");
            System.out.println(grantResults[0]);
            System.out.println(grantResults[0]);
            System.out.println(grantResults[0]);


        }
    }



    /**
     * metoda koja se poziva ako se odobri slika
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//metoda koja se poziva ako se odobri slika
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode== Activity.RESULT_OK){
                File f=new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag","URL od slike :"+Uri.fromFile(f));



            }

        }
    }

    /**
     * Metoda koja pravi objekat klase File gdje ce se pohraniti slika
     * @return
     * @throws IOException
     */
    private File createImageFile()  {//pravi objekat klase File koji upucuje na adresu na file sistemu
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//ovu je prvo korisito

        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        //sada cu provjeriti permisije za upis/ispis

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            // Continue only if the File was successfully created

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//ako sam dobro razumio,nakon sto odobrim uslikano stavi ju na taj url
                System.out.println("****************** start kamera");
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * Metoda koja sacuvava ucitanu sliku u galeriju
     */
    private void saveImageInGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "SLIKA POHRANJENA", Toast.LENGTH_SHORT).show();
    }
}