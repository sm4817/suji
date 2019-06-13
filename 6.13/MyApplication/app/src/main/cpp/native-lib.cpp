#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/dnn.hpp>
#include <iostream>


using namespace std;
using namespace cv;
using namespace cv::dnn;



extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing1(JNIEnv *env, jobject instance,
                                                            jlong inputImage, jlong outputImage,
                                                            jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);

    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh)
        {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),-1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing2(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing3(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing4(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing5(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing6(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Camera_imageprocessing7(JNIEnv *env, jobject instance,
                                                       jlong inputImage, jlong outputImage,
                                                       jint th1, jint th2) {

    Mat &img_input = *(Mat *) inputImage;
    Mat &img_output = *(Mat *) outputImage;


    const int POSE_PAIRS[20][2] =
            {
                    {0,  1},
                    {1,  2},
                    {2,  3},
                    {3,  4},         // thumb
                    {0,  5},
                    {5,  6},
                    {6,  7},
                    {7,  8},         // index
                    {0,  9},
                    {9,  10},
                    {10, 11},
                    {11, 12},    // middle
                    {0,  13},
                    {13, 14},
                    {14, 15},
                    {15, 16},  // ring
                    {0,  17},
                    {17, 18},
                    {18, 19},
                    {19, 20}   // small
            };

    string protoFile = "/sdcard/kivy/pose_deploy.prototxt";
    string weightsFile = "/sdcard/kivy/pose_iter_102000.caffemodel";

    int nPoints = 22;

    float thresh = 0.01;

    Mat frame, frameCopy;

    frame = img_input;
    img_output =frame.clone();
    int frameWidth = frame.cols;
    int frameHeight = frame.rows;

    float aspect_ratio = frameWidth / (float) frameHeight;
    int inHeight = 368;
    int inWidth = (int(aspect_ratio * inHeight) * 8) / 8;


    Net net = readNetFromCaffe(protoFile,weightsFile);

    cvtColor(frame, frame, COLOR_BGRA2BGR);

    if (img_input.empty()) {
        cerr << "ERROR! blank frame grabbed\n";
    }


    Mat inpBlob = blobFromImage(frame, 1.0 / 255, Size(inWidth, inHeight), Scalar(0, 0, 0),
                                false, false);
    net.setInput(inpBlob);

    Mat output = net.forward();

    int H = output.size[2];
    int W = output.size[3];

    // find the position of the body parts
    vector<Point> points(nPoints);
    for (int n = 0; n < nPoints; n++) {
        // Probability map of corresponding body's part.
        Mat probMap(H, W, CV_32F, output.ptr(0, n));
        resize(probMap, probMap, Size(frameWidth, frameHeight));

        Point maxLoc;
        double prob;
        minMaxLoc(probMap, 0, &prob, 0, &maxLoc);
        if (prob > thresh) {
            circle(img_output, cv::Point((int) maxLoc.x, (int) maxLoc.y), 8, Scalar(255, 0, 0),
                   -1);
            putText(img_output, cv::format("%d", n),
                    Point((int) maxLoc.x, (int) maxLoc.y), FONT_HERSHEY_COMPLEX, 1,
                    Scalar(255, 0, 0), 2);

        }
        points[n] = maxLoc;
    }

    int nPairs = sizeof(POSE_PAIRS) / sizeof(POSE_PAIRS[0]);

    for (int n = 0; n < nPairs; n++) {
        // lookup 2 connected body/hand parts
        Point2f partA = points[POSE_PAIRS[n][0]];
        Point2f partB = points[POSE_PAIRS[n][1]];

        if (partA.x <= 0 || partA.y <= 0 || partB.x <= 0 || partB.y <= 0)
            continue;

        //line(frame, partA, partB, Scalar(0, 255, 255), 8);
        circle(img_input, partA, 8, Scalar(255, 0, 0), -1);
        circle(img_input, partB, 8, Scalar(255, 0, 0), -1);
    }
}