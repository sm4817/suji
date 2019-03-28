#include <jni.h>
#include "com_sm_myapplication_CameraActivity.h"

#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"{

    JNIEXPORT void JNICALL
   Java_com_sm_myapplication_CameraActivity_ConvertRGBtoGray(
            JNIEnv *env,
            jobject  instance,
            jlong matAddrInput,
            jlong matAddrResult){


        Mat &matInput = *(Mat *)matAddrInput;
        Mat &matResult = *(Mat *)matAddrResult;

        cvtColor(matInput, matResult, COLOR_RGBA2GRAY);
      }
}