package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {


    ImageView imageVIewInput;
    ImageView imageVIewOuput;
    private Mat img_input;
    private Mat img_output;

    private String imageFilePath;
    private Uri photoUri;

    private int threshold1 = 50;
    private int threshold2 = 150;
    private static final String TAG = "opencv";
    static final int REQUEST_IMAGE_CAPTURE = 672;
    boolean isReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageVIewInput = (ImageView) findViewById(R.id.imageViewInput);
        imageVIewOuput = (ImageView) findViewById(R.id.imageViewOutput);

        Button Button = (Button) findViewById(R.id.button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imageprocess_and_showResult(threshold1, threshold2);
            }
        });
        // 이미지 를 클릭 시 카메라
        imageVIewInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });

        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isReady = true;
    }

    public native void imageprocessing1(long inputImage, long outputImage, int th1, int th2); // 스트레스
    public native void imageprocessing2(long inputImage, long outputImage, int th1, int th2); // 폐 (가래,천식)
    public native void imageprocessing3(long inputImage, long outputImage, int th1, int th2); //비염
    public native void imageprocessing4(long inputImage, long outputImage, int th1, int th2); //소화불량
    public native void imageprocessing5(long inputImage, long outputImage, int th1, int th2); //무릎이 아플때
    public native void imageprocessing6(long inputImage, long outputImage, int th1, int th2); //담 걸렸을때
    public native void imageprocessing7(long inputImage, long outputImage, int th1, int th2); //만성피로

    private void imageprocess_and_showResult(int th1, int th2) {

        if (isReady == false) return;

        if (img_output == null)
            img_output = new Mat();

        Intent result_select_pain= getIntent();
        String str_pain = result_select_pain.getStringExtra("pain");

        if(str_pain.equals("스트레스"))
        {
            imageprocessing1(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("가래"))
        {
            imageprocessing2(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("비염"))
        {
            imageprocessing3(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("소화불량"))
        {
            imageprocessing4(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("무릎"))
        {
            imageprocessing5(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("담"))
        {
            imageprocessing6(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }
        else if(str_pain.equals("만성피로"))
        {
            imageprocessing7(img_input.getNativeObjAddr(), img_output.getNativeObjAddr(), th1, th2);
        }

        final Bitmap bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        imageVIewOuput.setImageBitmap(bitmapOutput);

        imageVIewOuput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent fulloutput = new Intent(Camera.this, fullscreen.class);
                Bitmap bitmap = bitmapOutput;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                fulloutput.putExtra("image", byteArray);
                startActivity(fulloutput);
                */
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            img_input = new Mat();
            Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, img_input);
            ((ImageView) findViewById(R.id.imageViewInput)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void sendTakePhotoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 퍼미션 코드
    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    private boolean hasPermissions(String[] permissions) {
        int ret = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            ret = checkCallingOrSelfPermission(perms);
            if (!(ret == PackageManager.PERMISSION_GRANTED)) {
                //퍼미션 허가 안된 경우
                return false;
            }

        }
        //모든 퍼미션이 허가된 경우
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permsRequestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!writeAccepted) {
                            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(Camera.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        myDialog.show();
    }
}
