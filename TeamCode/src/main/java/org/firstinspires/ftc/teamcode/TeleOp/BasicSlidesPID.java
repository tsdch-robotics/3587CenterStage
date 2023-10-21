package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class BasicSlidesPID extends LinearOpMode {
    /*

     * Proportional Integral Derivative Controller

     */
    DcMotor lslide;
    DcMotor rslide;
    double Kp = 0.02;
    double Ki = 0;
    double Kd = 0;

    double integralSum = 0;

    double lastError = 0;
    final int CUTOFF = 20;

    // Elapsed timer class from SDK, please use it, it's epic
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {

        lslide = hardwareMap.dcMotor.get("lslide");
        rslide = hardwareMap.dcMotor.get("rslide");
        lslide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rslide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rslide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();

        boolean wasYPressed = false;
        boolean wasBPressed = false;
        while(opModeIsActive()) {
            if(gamepad1.y && !wasYPressed){
                controlSlides(100);
            }
            wasYPressed = gamepad1.y;

            if (gamepad1.b && !wasBPressed){
                controlSlides(0);
            }
            wasBPressed = gamepad1.b;
        }
        sleep(2000);
    }

    public void controlSlides(int setpoint) {
        while (opModeIsActive() && Math.abs(lslide.getCurrentPosition() - setpoint) > CUTOFF) {

            // obtain the encoder position
            int encoderPosition = lslide.getCurrentPosition();
            // calculate the error
            double error = setpoint - encoderPosition;

            // rate of change of the error
            double derivative = (error - lastError) / timer.seconds();

            // sum of all error over time
            integralSum = integralSum + (error * timer.seconds());

            double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
            out = Range.clip(out, -1, 1);

            lslide.setPower(out);
            rslide.setPower(out);

            lastError = error;

            // reset the timer for next time
            timer.reset();
        }

        lslide.setPower(0);
        rslide.setPower(0);
    }

}
