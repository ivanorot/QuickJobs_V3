package com.example.quickjobs.view.jobs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.quickjobs.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import android.Manifest

public class CameraActivity extends AppCompatActivity {
    private Preview cameraPreview;
    private ImageCapture imageCapture;
    private Camera camera;
    private Button image_capture_Button;
    private Bitmap images[] = new Bitmap[10];

    private static int REQUESTED_CODE_PERMISSIONS = 10;
    private static String[] REQUIRED_PERSMISSIONS = {Manifest.permission.CAMERA};
    private ExecutorService initCameraExecutor;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPermission();
        previewView.findViewById(R.id.cameraActivity_FrameLayout);

        image_capture_Button.findViewById(R.id.cameraActivity_capture_imageButton);
        setButtonClickListener();

        ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();


    }

    private void cameraPermission(){
        if(allPermissionGranted()){
            startCamera();
        }
        else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERSMISSIONS, REQUESTED_CODE_PERMISSIONS);
        }
    }

    private void setButtonClickListener(){
        if(image_capture_Button!=null){
            image_capture_Button.setOnClickListener(takePhoto());
        }
    }

    private View.OnClickListener takePhoto(){

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);

                Bitmap bitmap = convertImageProxytoBitmap(image);
                saveImageIntoArray(bitmap);


            }
        });

        return null;
    }

    private void saveImageIntoArray(Bitmap tempBitmap){
        END:
        for(int i = 0; i<10; i++){
           if(images[i]==null) {
               images[i] = tempBitmap;
               break END;
           }
        }
    }

    private Bitmap convertImageProxytoBitmap(ImageProxy image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte [] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] tempBytes = bytes.clone();

        return BitmapFactory.decodeByteArray(tempBytes, 0, tempBytes.length);
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(()->{
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview();

            }
            catch(ExecutionException|InterruptedException e){
            }

        }, ContextCompat.getMainExecutor(this));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTED_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                startCamera();
            }else{
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionGranted(){
        int permissions_count = REQUIRED_PERSMISSIONS.length;
        for (int i = 0; i<permissions_count; i++)
            if(ContextCompat.checkSelfPermission(getBaseContext(), REQUIRED_PERSMISSIONS[i])!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        return true;
    }

    private void bindPreview(){
        cameraPreview = new Preview.Builder().build();
        CameraSelector camerSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        cameraPreview.setSurfaceProvider(previewView.createSurfaceProvider(camera.getCameraInfo()));
        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, camerSelector, cameraPreview);

    }




}