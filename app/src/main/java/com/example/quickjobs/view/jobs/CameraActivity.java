package com.example.quickjobs.view.jobs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quickjobs.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import android.Manifest

public class CameraActivity extends AppCompatActivity {
    private final String TAG = "CameraActivity";
    private Preview cameraPreview;
    private ImageCapture imageCapture;
    private Camera camera;
    private ImageButton image_capture_Button;
    private Bitmap images[] = new Bitmap[10];
    private ImageView imageview;

    private static int REQUESTED_CODE_PERMISSIONS = 10;
    private static String[] REQUIRED_PERSMISSIONS = {Manifest.permission.CAMERA};
    private ExecutorService cameraExecutor;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private OutputStream outputStream;
    private ImageCapture.Metadata metadata;


    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraExecutor = Executors.newSingleThreadExecutor();
        //CameraXConfig cameraX = new CameraXConfig().;


        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        image_capture_Button = (ImageButton) findViewById(R.id.cameraActivity_capture_imageButton);
        imageview = (ImageView) findViewById(R.id.cameraActivity_imageView);


        previewView = (PreviewView) findViewById(R.id.cameraActivity_viewFinder);


        cameraPermission();


        setButtonClickListener();



    }

    private void cameraPermission() {
        if (allPermissionGranted()) {
            startCamera();
            Toast.makeText(this, "Permissions Granted.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERSMISSIONS, REQUESTED_CODE_PERMISSIONS);
            Toast.makeText(this, "Not all permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonClickListener() {

        image_capture_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Button works.", Toast.LENGTH_SHORT).show();

                outputStream = new ByteArrayOutputStream();
                metadata = new ImageCapture.Metadata();
                ImageCapture.OutputFileOptions outputOptions =
                        new ImageCapture.OutputFileOptions
                                .Builder(outputStream)
                                .setMetadata(metadata)
                                .build();

                imageCapture.takePicture(outputOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                      //  byte bytes[] = new byte[].equals(outputStream);
                        //  ByteBuffer buffer = new ByteBuffer().
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(v.getContext(), "ke pedo k pedo.", Toast.LENGTH_SHORT).show();
                    }



 /*                   @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                       super.onCaptureSuccess(image);

                        Bitmap bitmap = convertImageProxytoBitmap(image);
                        saveImageIntoArray(bitmap);
                        displayImages();
                        image.close();

                        *//*NEEDS WORK
                        *
                        NEEDS WORK
                        *
                        TODO: NEEDS CODE FOR CAPTURE
                        *
                        NEEDS WORK
                        *
                        *
                        NEEDS WORK*//*
                    }
*/

                    ;
                });
            }

            ;
        });
    }

    private void displayImages(){
        Toast.makeText(this, "Image Capture papaw.", Toast.LENGTH_SHORT).show();

        imageview.setVisibility(View.VISIBLE);
        imageview.setImageBitmap(images[0]);
    }


    private void saveImageIntoArray(Bitmap tempBitmap) {
        END:
        for (int i = 0; i < 10; i++) {
            if (images[i] == null) {
                images[i] = tempBitmap;
                break END;
            }
        }
    }

    private Bitmap convertImageProxytoBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] tempBytes = bytes.clone();

        return BitmapFactory.decodeByteArray(tempBytes, 0, tempBytes.length);
    }

    private void startCamera() {


        cameraProviderFuture.addListener(() -> {
            try {

                cameraProvider = cameraProviderFuture.get();
                bindPreview();

            } catch (ExecutionException | InterruptedException e) {

            }

        }, ActivityCompat.getMainExecutor(this));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTED_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionGranted() {
        int permissions_count = REQUIRED_PERSMISSIONS.length;
        for (int i = 0; i < permissions_count; i++) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), REQUIRED_PERSMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions failed.", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }

    private void bindPreview() {
       //Intitial build of CameraSelector, Preview, and ImageCapture
        cameraPreview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        try {
            Toast.makeText(this, "Binding.", Toast.LENGTH_SHORT).show();
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, cameraPreview);


            previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.TEXTURE_VIEW);
            cameraPreview.setSurfaceProvider(previewView.createSurfaceProvider());



        } catch (Exception e) {

        }
    }
}
