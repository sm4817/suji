package com.example.myapplication;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;


public class Camera extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat matInput;
    private Mat matResult;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //퍼미션 상태 확인
            if (!hasPermissions(PERMISSIONS)) {

                //퍼미션 허가 안되어있다면 사용자에게 요청
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    public native void imageprocessing1(long matAddrInput, long matAddrResult, int frameWidths, int frameHeights);  // 9 소화불량
    public native void imageprocessing2(long matAddrInput, long matAddrResult, int frameWidths, int frameHeights);  // 11 비염
    public native void imageprocessing3(long matAddrInput, long matAddrResult, int frameWidths, int frameHeights);  // 12 스트레스
    public native void imageprocessing4(long matAddrInput, long matAddrResult, int frameWidths, int frameHeights);  // 18 무릎

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();
        //if ( matResult != null ) matResult.release(); fix 2018. 8. 18
        Intent result_select_pain= getIntent();
        String str_pain = result_select_pain.getStringExtra("pain");

        int frameWidth = matInput.width();
        int frameHeight = matInput.height();

        if (matResult == null) {
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        }

        if(str_pain.equals("스트레스"))
        {
            imageprocessing3(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), frameWidth, frameHeight);
        }
        else if(str_pain.equals("비염"))
        {
            imageprocessing2(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), frameWidth, frameHeight);
        }
        else if(str_pain.equals("소화불량"))
        {
            imageprocessing1(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), frameWidth, frameHeight);
        }
        else if(str_pain.equals("무릎"))
        {
            imageprocessing4(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), frameWidth, frameHeight);
        }

        return matResult;
    }


    //여기서부턴 퍼미션 관련 메소드
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.CAMERA"};


    private boolean hasPermissions(String[] permissions) {
        int result;

        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {

            result = ContextCompat.checkSelfPermission(this, perms);

            if (result == PackageManager.PERMISSION_DENIED) {
                //허가 안된 퍼미션 발견
                return false;
            }
        }

        //모든 퍼미션이 허가되었음
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted)
                        showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                }
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Camera.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

}
