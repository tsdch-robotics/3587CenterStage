package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;



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

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime; // Import ElapsedTime



@Autonomous(group = "drive")
public class Dectector2 extends LinearOpMode {
   public OpenCvWebcam webcam1 = null;
   public ElapsedTime elapsedTime = new ElapsedTime(); // Add ElapsedTime to track time
   public double totalLeftAvg = 0;
   public double totalRightAvg = 0;
   public double left = 0;
   public double right = 0;
   public int frameCount = 0;
   public int zone = 0;
   public ExamplePipeline examplePipeline;
    public Servo AutoFinger;
    public Servo door;



    @Override
    public void runOpMode() throws InterruptedException {
        //SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

       // Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

      //  drive.setPoseEstimate(startPose);
        AutoFinger = hardwareMap.get(Servo.class, "door");
        AutoFinger.setPosition(0.0);
        AutoFinger.setDirection(Servo.Direction.FORWARD);


        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        examplePipeline = new ExamplePipeline();
        webcam1.setPipeline(examplePipeline);

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam1.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }//TODO: adjust width and height baced on specific camera

            @Override
            public void onError(int errorCode) {

            }
        });
        telemetry.addLine("Waiting to start");
        telemetry.update();
        waitForStart();
        // Run for 10 seconds
        telemetry.addLine("started");
        telemetry.update();
      /*  for (int i = 0; i < 200; i++) { // 200 iterations * 50 milliseconds = 10 seconds
            telemetry.addLine("Measuring Camara stream");
            telemetry.update();
            // Process frames and accumulate color values
            totalLeftAvg += examplePipeline.leftavgfin;
            totalRightAvg += examplePipeline.rightavgfin;
            frameCount++;

            // Sleep for 50 milliseconds
            sleep(50);
        }*/

        left = examplePipeline.leftavgfin;
        right = examplePipeline.rightavgfin;

        telemetry.addLine("done computing");
        telemetry.update();
        // Average color values over ten seconds
        double averageLeft = left;//totalLeftAvg / frameCount;
        double averageRight = right;//totalRightAvg / frameCount;

        telemetry.addLine("Returning Values");
        telemetry.update();
        // Use the average values to determine autonomous steps
        if (left > right && (Math.abs(left - right)) >= 1.5) {
            zone = 1;
            //left
            telemetry.addData("Zone", zone);
            telemetry.addData("Average Left Value", averageLeft);
            telemetry.addData("Average Right Value", averageRight);
            telemetry.update();

            //write your Autonomous specific instructions for this spike mark zone
            AutoFinger.setPosition(0.5);



        } else if (left < right && (Math.abs(left - right)) >= 1.5) {
            zone = 2;
            //middle
            telemetry.addData("Zone", zone);
            telemetry.addData("Average Left Value", averageLeft);
            telemetry.addData("Average Right Value", averageRight);
            telemetry.update();



            //write your Autonomous specific instructions for this spike mark zone



        } else {
            zone = 3;
            //right
            telemetry.addData("Zone", zone);

            telemetry.update();
            //write your Autonomous specific instructions for this spike mark zone




        }



        sleep(4000);





    }



    class ExamplePipeline extends OpenCvPipeline {

        Mat YCbCr = new Mat();
        Mat leftCrop;
        Mat rightCrop;
        double leftavgfin;
        double rightavgfin;
        Mat outPut = new Mat();

        Scalar rectColor = new Scalar(0.0, 0.0, 255.0);

        @Override
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
//specific square size
            Rect leftRect = new Rect(1, 225, 300, 300);
            Rect rightRect = new Rect(640, 225, 400, 300);//midile is 640
            //changing the above 800 to 640

            input.copyTo(outPut);
            Imgproc.rectangle(outPut, leftRect, rectColor, 20);
            Imgproc.rectangle(outPut, rightRect, rectColor, 20);

            leftCrop = YCbCr.submat(leftRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, 1);
            Core.extractChannel(rightCrop, rightCrop, 1);
            //coi 1 is red

            Scalar leftavg = Core.mean(leftCrop);
            Scalar rightavg = Core.mean(rightCrop);

            leftavgfin = leftavg.val[0];
            rightavgfin = rightavg.val[0];

     /*       telemetry.addLine("pipeline running");
            telemetry.addData("LeftValue", leftavgfin);
            telemetry.addData("RightValue", rightavgfin);
*/
            return outPut;
        }
    }





}