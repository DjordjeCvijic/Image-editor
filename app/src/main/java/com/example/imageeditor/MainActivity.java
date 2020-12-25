package com.example.imageeditor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private String currentPhotoPath;
    private ImageView originalImage;


    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton showImage1Btn, playProcess1Btn, pauseProcess1Btn, saveImage1Btn;
        ImageButton showImage2Btn, playProcess2Btn, pauseProcess2Btn, saveImage2Btn;
        ImageButton showImage3Btn, playProcess3Btn, pauseProcess3Btn, saveImage3Btn;
        Button cameraBtn;
        Button galleryBtn;
        
        originalImage = findViewById(R.id.displayImageViewID);
        cameraBtn = findViewById(R.id.cameraBtnID);
        galleryBtn = findViewById(R.id.galleryBtnID);


        showImage1Btn = findViewById(R.id.showImageBtn1ID);
        progressBar1 = findViewById(R.id.progressBar1ID);
        playProcess1Btn = findViewById(R.id.playProcessBtn1ID);
        pauseProcess1Btn = findViewById(R.id.pauseProcessBtn1ID);
        saveImage1Btn = findViewById(R.id.saveBtn1ID);

        showImage2Btn = findViewById(R.id.showImageBtn2ID);
        progressBar2 = findViewById(R.id.progressBar2ID);
        playProcess2Btn = findViewById(R.id.playProcessBtn2ID);
        pauseProcess2Btn = findViewById(R.id.pauseProcessBtn2ID);
        saveImage2Btn = findViewById(R.id.saveBtn2ID);


        showImage3Btn = findViewById(R.id.showImageBtn3ID);
        progressBar3 = findViewById(R.id.progressBar3ID);
        playProcess3Btn = findViewById(R.id.playProcessBtn3ID);
        pauseProcess3Btn = findViewById(R.id.pauseProcessBtn3ID);
        saveImage3Btn = findViewById(R.id.saveBtn3ID);

        askPermissions();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearGUI();
                askCameraPermissions();
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
        saveImage1Btn.setOnClickListener(new View.OnClickListener() {
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
        saveImage2Btn.setOnClickListener(new View.OnClickListener() {
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
        saveImage3Btn.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(this, "Process of editing not done", Toast.LENGTH_SHORT).show();

        }
    }


    private void playProcess(int i, ProgressBar progressBar) {
        ProgressThread progressThread = mapOfThreads.get(i);
        if (progressThread != null) {
            if (progressThread.isRunning()) {
                Toast.makeText(MainActivity.this, "Process of editing is already in the process", Toast.LENGTH_SHORT).show();
            } else {
                synchronized (progressThread.getLockObject()) {
                    progressThread.getLockObject().notify();
                }
            }

        } else {
            if (originalImageExists) {
                Object lockObject = new Object();
                progressThread = new ProgressThread(i, progressBar, lockObject, MainActivity.this, originalImage);
                mapOfThreads.put(i, progressThread);
                progressThread.start();
            } else {
                Toast.makeText(MainActivity.this, "Image for editing is not selected", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void pauseProcess(int i) {
        ProgressThread progressThread = mapOfThreads.get(i);
        if (progressThread != null) {
            if (!progressThread.isRunning())
                Toast.makeText(MainActivity.this, "Process of editing is already paused", Toast.LENGTH_SHORT).show();
            else {

                progressThread.setRunning(false);

            }
        } else {
            Toast.makeText(MainActivity.this, "Process of editing is not started", Toast.LENGTH_SHORT).show();
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
        } else dispatchTakePictureIntent();
    }

    private void askReadPermissions() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, READ_PERM_CODE);
        } else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        }
    }

    private void askWritePermissions(int i) {
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, WRITE_PERM_CODE);
        } else saveImageInGallery(i);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera access required", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera access allowed", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == READ_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage access required", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage access allowed", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITE_PERM_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage access required", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage access allowed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                originalImage.setImageURI(Uri.fromFile(f));
                originalImageExists = true;


            }

        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                originalImage.setImageURI(contentUri);
                originalImageExists = true;
            }

        }
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private void saveImageInGallery(int index) {
        Bitmap image = mapOfEditImage.get(index);
        if (image != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = getImageUri(MainActivity.this, image);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            Toast.makeText(this, "Selected image saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Process of editing not done", Toast.LENGTH_SHORT).show();
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