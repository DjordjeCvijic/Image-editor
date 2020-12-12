package com.example.imageeditor;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int READ_PERM_CODE = 110;
    private static final int WRITE_PERM_CODE = 111;
    private static final int REQUEST_CODE = 200;
    private boolean readGranted = false;
    private boolean writeGranted = false;
    String currentPhotoPath;//za sada samo objekat klase File ka ne kreiranom fileu
    ImageView imageView;
    Button cameraBtn;
    Button galleryBtn;
    Button saveBtn;
    ImageButton showImage1Btn;
    ImageButton showImage2Btn;
    ImageButton showImage3Btn;
    ImageButton playProcess1Btn;
    ImageButton playProcess2Btn;
    ImageButton playProcess3Btn;
    ImageButton pauseProcess1Btn;
    ImageButton pauseProcess2Btn;
    ImageButton pauseProcess3Btn;
    ImageButton downloadImage1Btn;
    ImageButton downloadImage2Btn;
    ImageButton downloadImage3Btn;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.displayImageViewID);
        cameraBtn = findViewById(R.id.cameraBtnID);
        galleryBtn = findViewById(R.id.galleryBtnID);
        saveBtn = findViewById(R.id.saveBtnID);

        showImage1Btn=findViewById(R.id.showImageBtn1ID);
        progressBar1=findViewById(R.id.progressBar1ID);
        playProcess1Btn=findViewById(R.id.playProcessBtn1ID);
        pauseProcess1Btn=findViewById(R.id.pauseProcessBtn1ID);
        downloadImage1Btn=findViewById(R.id.downloadBtn1ID);

        showImage2Btn=findViewById(R.id.showImageBtn2ID);
        progressBar2=findViewById(R.id.progressBar2ID);
        playProcess2Btn=findViewById(R.id.playProcessBtn2ID);
        pauseProcess2Btn=findViewById(R.id.pauseProcessBtn2ID);
        downloadImage2Btn=findViewById(R.id.downloadBtn2ID);


        showImage3Btn=findViewById(R.id.showImageBtn3ID);
        progressBar3=findViewById(R.id.progressBar3ID);
        playProcess3Btn=findViewById(R.id.playProcessBtn3ID);
        pauseProcess3Btn=findViewById(R.id.pauseProcessBtn3ID);
        downloadImage3Btn=findViewById(R.id.downloadBtn3ID);

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
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageInGallery();
            }
        });

        showImage1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "prikaz slike 1", Toast.LENGTH_SHORT).show();
            }
        });
        showImage2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "prikaz slike 2", Toast.LENGTH_SHORT).show();
            }
        });
        showImage3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "prikaz slike 3", Toast.LENGTH_SHORT).show();
            }
        });
        playProcess1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "play 1", Toast.LENGTH_SHORT).show();
            }
        });
        playProcess2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "play 2", Toast.LENGTH_SHORT).show();
            }
        });
        playProcess3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "play 3", Toast.LENGTH_SHORT).show();
            }
        });
        pauseProcess1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "pause 1", Toast.LENGTH_SHORT).show();
            }
        });
        pauseProcess2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "pause 2", Toast.LENGTH_SHORT).show();
            }
        });
        pauseProcess3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "pause 3", Toast.LENGTH_SHORT).show();
            }
        });
        downloadImage1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "download 1", Toast.LENGTH_SHORT).show();
            }
        });
        downloadImage2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "download 2", Toast.LENGTH_SHORT).show();
            }
        });
        downloadImage3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "download 3", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void askPermissions() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissions,
                    REQUEST_CODE);
        }

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Mora biti dozvoljeno koristenje kamera", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == READ_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readGranted = true;
            } else {
                Toast.makeText(this, "Mora biti dozvoljeno citanje iz memorije", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITE_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                writeGranted = true;
            } else {
                Toast.makeText(this, "Mora biti dozvoljeno upisivanje", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == REQUEST_CODE) {
            System.out.println("******************* rezultat");
            System.out.println(grantResults[0]);
            System.out.println(grantResults[0]);
            System.out.println(grantResults[0]);


        }
    }


    /**
     * metoda koja se poziva ako se odobri slika
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//metoda koja se poziva ako se odobri slika
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "URL od uslikane slike :" + Uri.fromFile(f));

            }

        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri=data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "."+getFilesExt(contentUri);
                Log.d("tag", "URL od izabrabe slike :" + imageFileName);
                imageView.setImageURI(contentUri);

            }

        }
    }

    /**
     * Metoda vraca ekstenziju slike
     * @param contentUri
     * @return
     */
    private String getFilesExt(Uri contentUri) {
        ContentResolver c=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));

    }

    /**
     * Metoda koja pravi objekat klase File gdje ce se pohraniti slika
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() {//pravi objekat klase File koji upucuje na adresu na file sistemu
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