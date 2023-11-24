package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvCameraFactory;




@Autonomous
public class BlueDetector extends OpMode {


    public static final int x1 = 400;
    public static final int y1 = 300;
    public static final int width1 = 500;
    public static final int height1 = 600;

    public static final int x2 = 500;
    public static final int y2 = 400;
    public static final int width2 = 500;
    public static final int height2 = 600;

    OpenCvWebcam webcam1 = null;

    @Override
    public void init(){

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new examplePipeline());

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam1.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }//TODO: adjust width and height based on specific camera

            @Override
            public void onError(int errorCode) {

            }
        });


    }
    @Override
    public void loop() {

    }


    class examplePipeline extends OpenCvPipeline{

        Mat YCbCr = new Mat();
        Mat leftCrop;
        Mat rightCrop;
        double leftavgfin;
        double rightavgfin;
        Mat outPut = new Mat();

        Scalar rectColor = new Scalar(255.0, 0.0, 0.0);


        @Override
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("pipeline running");

             //Rect leftRect = new Rect(1, 1, 959, 1079);
            //Rect rightRect = new Rect(960, 1, 959, 1079);
            Rect leftRect = new Rect(x1, y1, width1 , height1);
            Rect rightRect = new Rect(x2, y2, width2, height2);
            // Print or log an error message
            if (leftRect.x < 0 || leftRect.y < 0 || leftRect.x + leftRect.width > YCbCr.cols() || leftRect.y + leftRect.height > YCbCr.rows()) {
                telemetry.addLine("LeftRect out of bounds");
            }
            if (rightRect.x < 0 || rightRect.y < 0 || rightRect.x + rightRect.width > YCbCr.cols() || rightRect.y + rightRect.height > YCbCr.rows()) {
                telemetry.addLine("RightRect out of bounds");
            }
            input.copyTo(outPut);
            Imgproc.rectangle(outPut, leftRect, rectColor, 20);
            Imgproc.rectangle(outPut, rightRect, rectColor, 20);

            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rightavg = Core.mean(rightCrop);

            leftavgfin = leftavg.val[0];
            rightavgfin = rightavg.val[0];

            if (leftavgfin > rightavgfin && (Math.abs(leftavgfin-rightavgfin)) >= 1.5 ){
                telemetry.addLine("A");
                telemetry.addData("LeftValue", leftavgfin);
                telemetry.addData("RightValue", rightavgfin);
            }
            else if (rightavgfin > leftavgfin &&  (Math.abs(leftavgfin-rightavgfin)) >= 1.5){
                telemetry.addLine("B");
                telemetry.addData("LeftValue", leftavgfin);
                telemetry.addData("RightValue", rightavgfin);
            }

            else{
                telemetry.addLine("C");
                telemetry.addData("LeftValue", leftavgfin);
                telemetry.addData("RightValue", rightavgfin);
            }

            return outPut;
        }



    }
}