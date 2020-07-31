package com.example.quickjobs.view.camera;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.view.splash.SplashActivity;
import com.google.common.util.concurrent.ListenableFuture;

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


    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    /*
    Joseph Permission Manager
     */
    private final int CAMERA_REQUEST_ID = 201;
    PermissionManager permissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraExecutor = Executors.newSingleThreadExecutor();
        //CameraXConfig cameraX = new CameraXConfig().;


        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        image_capture_Button = (ImageButton) findViewById(R.id.cameraActivity_capture_imageButton);
       // imageview = (ImageView) findViewById(R.id.cameraActivity_imageView);


        previewView = (PreviewView) findViewById(R.id.cameraActivity_viewFinder);


        checkCameraPermission();


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


                imageCapture.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                       super.onCaptureSuccess(image);
                       Toast.makeText(v.getContext(), "Image Capture papaw.", Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = convertImageProxytoBitmap(image);
                        saveImageIntoArray(bitmap);
                        displayImages();
                        image.close();





                        /*NEEDS WORK
                        *
                        NEEDS WORK
                        *
                        TODO: NEEDS CODE FOR CAPTURE
                        *
                        NEEDS WORK
                        *
                        *
                        NEEDS WORK*/
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        super.onError(exception);
                        Toast.makeText(v.getContext(), "ke pedo k pedo.", Toast.LENGTH_SHORT).show();

                    }

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
        cameraPreview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build();


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

    /*
    Joseph Permission Architecture
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissionResults();
    }

    private void initPermissionManger(){
        permissionManager = new PermissionManager(getApplicationContext());
    }

    private void checkCameraPermission(){
        initPermissionManger();
        permissionManager.checkPermission(this, Manifest.permission.CAMERA, new PermissionManager.PermissionAskListnener() {
            @Override
            public void onNeedPermission() {
                Log.println(Log.ERROR, TAG, "Requesting location permission");
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[] { Manifest.permission.CAMERA}, CAMERA_REQUEST_ID);
            }

            @Override
            public void onPermssionPreviouslyDenied() {
                Log.println(Log.ERROR, TAG, "informing user why we need location services");
                showRationale();
            }

            @Override
            public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                Log.println(Log.ERROR, TAG, "sending user to settings to change location permission");
                dialogForSettings();
            }

            @Override
            public void onPermissionGranted() {
                Log.println(Log.ERROR, TAG, "checkLocationPermission -> onPermissionGranted -> checkIfLocationIsAvailable");
                startCamera();
            }
        });
    }

    private void checkPermissionResults(){
        permissionManager.handlePermissionRequestResults(getApplicationContext(), Manifest.permission.CAMERA, new PermissionManager.PermissionRequestResultListener() {
            @Override
            public void onPermissionGranted() {
                startCamera();
            }

            @Override
            public void onPermissionDenied() {
                checkCameraPermission();
            }
        });
    }

    private void showRationale(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("Without this permission this app is unable to find jobs. Are you sure you want to Deny this message")
                .setCancelable(false)
                .setNegativeButton("IM SURE", (dialogInterface, i ) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setPositiveButton("RETRY", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]
                            { Manifest.permission.CAMERA}, CAMERA_REQUEST_ID);
                }).show();
    }

    private void dialogForSettings(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("Now you must allow location access from settings.")
                .setCancelable(false)
                .setNegativeButton("NOT NOW", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }))
                .setPositiveButton("SETTINGS", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    goToSettings();
                }).show();
    }

    private void goToSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }
}
