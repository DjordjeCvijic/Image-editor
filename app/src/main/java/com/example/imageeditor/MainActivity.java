package com.example.imageeditor;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int ALL_PERM_CODE = 200;
    private static final int READ_PERM_CODE = 110;
    private static final int WRITE_PERM_CODE = 111;
    private static final int CAMERA_PERM_CODE = 101;

    private boolean originalImageExists = false;
    private Map<Integer, ProgressThread> mapOfThreads = new HashMap<>();
    public static Map<Integer, Bitmap> mapOfEditImage = new HashMap<>();
    String currentPhotoPath;//za sada samo objekat klase File ka ne kreiranom fileu
    ImageView originalImage;
    Button cameraBtn;
    Button galleryBtn;
    ImageButton showImage1Btn, playProcess1Btn, pauseProcess1Btn, downloadImage1Btn;
    ImageButton showImage2Btn, playProcess2Btn, pauseProcess2Btn, downloadImage2Btn;
    ImageButton showImage3Btn, playProcess3Btn, pauseProcess3Btn, downloadImage3Btn;

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        originalImage = findViewById(R.id.displayImageViewID);
        cameraBtn = findViewById(R.id.cameraBtnID);
        galleryBtn = findViewById(R.id.galleryBtnID);


        showImage1Btn = findViewById(R.id.showImageBtn1ID);
        progressBar1 = findViewById(R.id.progressBar1ID);
        playProcess1Btn = findViewById(R.id.playProcessBtn1ID);
        pauseProcess1Btn = findViewById(R.id.pauseProcessBtn1ID);
        downloadImage1Btn = findViewById(R.id.downloadBtn1ID);

        showImage2Btn = findViewById(R.id.showImageBtn2ID);
        progressBar2 = findViewById(R.id.progressBar2ID);
        playProcess2Btn = findViewById(R.id.playProcessBtn2ID);
        pauseProcess2Btn = findViewById(R.id.pauseProcessBtn2ID);
        downloadImage2Btn = findViewById(R.id.downloadBtn2ID);


        showImage3Btn = findViewById(R.id.showImageBtn3ID);
        progressBar3 = findViewById(R.id.progressBar3ID);
        playProcess3Btn = findViewById(R.id.playProcessBtn3ID);
        pauseProcess3Btn = findViewById(R.id.pauseProcessBtn3ID);
        downloadImage3Btn = findViewById(R.id.downloadBtn3ID);

        askPermissions();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGUI();
                askCameraPermissions();
//                dispatchTakePictureIntent();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGUI();
                askReadPermissions();


            }
        });


        showImage1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(1);
            }
        });
        playProcess1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playProcess(1, progressBar1);

            }
        });
        pauseProcess1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseProcess(1);

            }


        });
        downloadImage1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               askWritePermissions(1);
            }
        });

        showImage2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(2);
            }
        });
        playProcess2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playProcess(2, progressBar2);
            }
        });
        pauseProcess2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseProcess(2);
            }
        });
        downloadImage2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askWritePermissions(2);
            }
        });

        showImage3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(3);
            }
        });
        playProcess3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playProcess(3, progressBar3);
            }
        });
        pauseProcess3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseProcess(3);
            }
        });
        downloadImage3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               askWritePermissions(3);
            }
        });

    }

    private void showImagePopup(int i) {
        Bitmap image = mapOfEditImage.get(i);
        if (image != null) {
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            dialogBuilder = new AlertDialog.Builder(MainActivity.this);//ISPRED KOG CONTEXT-A DA PRIKAZE POPUP
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View contactPopupView = inflater.inflate(R.layout.filtered_image_popup, null);

            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.show();

            ImageView filteredImage = (ImageView) contactPopupView.findViewById(R.id.filteredImageViewID);
            filteredImage.setImageBitmap(image);


        } else {
            Toast.makeText(this, "Filtriranje nije obavljeno", Toast.LENGTH_SHORT).show();

        }
    }


    private void playProcess(int i, ProgressBar progressBar) {
        ProgressThread progressThread = mapOfThreads.get(Integer.valueOf(i));
        if (progressThread != null) {
            if (progressThread.isRunning()) {
                Toast.makeText(MainActivity.this, "Proces je u toku", Toast.LENGTH_SHORT).show();
            } else {
                synchronized (progressThread.getLockObject()) {
                    progressThread.getLockObject().notify();
                }
            }

        } else {
            if (originalImageExists) {
                Object lockObject = new Object();
                progressThread = new ProgressThread(i, progressBar, lockObject, MainActivity.this, originalImage);
                mapOfThreads.put(Integer.valueOf(i), progressThread);
                progressThread.start();
            } else {
                Toast.makeText(MainActivity.this, "Slika za obradu nije izabrana", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void pauseProcess(int i) {
        ProgressThread progressThread = mapOfThreads.get(Integer.valueOf(i));
        if (progressThread != null) {
            if (!progressThread.isRunning())
                Toast.makeText(MainActivity.this, "Proces je vec pauziran", Toast.LENGTH_SHORT).show();
            else {

                progressThread.setRunning(false);

            }
        } else {
            Toast.makeText(MainActivity.this, "Proces nije ni pokrenut", Toast.LENGTH_SHORT).show();
        }
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
                    ALL_PERM_CODE);
        }

    }

    private void askCameraPermissions() {
        String[] permission = {Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, CAMERA_PERM_CODE);
        }
        else dispatchTakePictureIntent();
    }

    private void askReadPermissions() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, READ_PERM_CODE);
        }
        else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        }
    }

    private void askWritePermissions(int i) {
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, WRITE_PERM_CODE);
        }
        else saveImageInGallery(i);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == ALL_PERM_CODE) {

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Mora biti dozvoljeno citanje", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Mora biti dozvoljeno  poisanje", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Mora biti dozvoljeno koristenje kamera", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Mora biti dozvoljeno koristenje kamera", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "koristenje kamere dozvljeno", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == READ_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Mora biti dozvoljeno citanje", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "citanje iz meorije dozvoljeno", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITE_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Mora biti dozvoljeno pisanje", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "pisanje u memoriju dozvoljeno", Toast.LENGTH_SHORT).show();
            }
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
                originalImage.setImageURI(Uri.fromFile(f));
                originalImageExists = true;
                Log.d("tag", "URL od uslikane slike :" + Uri.fromFile(f));

            }

        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFilesExt(contentUri);
                Log.d("tag", "URL od izabrabe slike :" + imageFileName);
                originalImage.setImageURI(contentUri);
                originalImageExists = true;


            }

        }
    }

    /**
     * Metoda vraca ekstenziju slike
     *
     * @param contentUri
     * @return
     */
    private String getFilesExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
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
    private void saveImageInGallery(int index) {
        Bitmap image = mapOfEditImage.get(index);
        if (image != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = getImageUri(MainActivity.this, image);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            Toast.makeText(this, "SLIKA POHRANJENA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Editovanje nije odradjeno", Toast.LENGTH_SHORT).show();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageFileName, null);
        return Uri.parse(path);
    }

    public void clearGUI() {
        mapOfThreads.clear();
        mapOfEditImage.clear();
        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar3.setProgress(0);
    }

}