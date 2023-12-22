package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class BasicSlidesPID extends LinearOpMode {
    /*

     * Proportional Integral Derivative Controller

     */
    public DcMotor slide;
    public double Kp = 0.02;
    public double Ki = 0;
    public double Kd = 0;
    public double integralSum = 0;
    public double lastError = 0;
    public final int CUTOFF = 20;

    // Elapsed timer class from SDK, please use it, it's epic
    public ElapsedTime timer = new ElapsedTime();
    public Servo door;
    public Servo larm;
    public Servo rarm;
    public CRServo wheel;

    @Override
    public void runOpMode() {

        slide = hardwareMap.dcMotor.get("slide");
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheel = hardwareMap.crservo.get("wheel");
        door = hardwareMap.get(Servo.class, "door");
        larm = hardwareMap.get(Servo.class, "larm");
        rarm = hardwareMap.get(Servo.class, "rarm");
        door.setDirection(Servo.Direction.FORWARD);
        larm.setDirection(Servo.Direction.REVERSE);
        rarm.setDirection(Servo.Direction.FORWARD);
        larm.scaleRange(0.0, 1.0);
        rarm.scaleRange(0.0, 1.0);
        door.setPosition(0.0);
        wheel.setPower(0);
        larm.setPosition(0.0);
        rarm.setPosition(0.0);

        waitForStart();

        boolean wasYPressed = false;
        boolean wasBPressed = false;
        while(opModeIsActive()) {
            if(gamepad1.y && !wasYPressed){
                controlSlides(400);
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
        while (opModeIsActive() && Math.abs(slide.getCurrentPosition() - setpoint) > CUTOFF) {

            // obtain the encoder position
            int encoderPosition = slide.getCurrentPosition();
            // calculate the error
            double error = setpoint - encoderPosition;

            // rate of change of the error
            double derivative = (error - lastError) / timer.seconds();

            // sum of all error over time
            integralSum = integralSum + (error * timer.seconds());

            double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
            out = Range.clip(out, -1, 1);

            slide.setPower(out);

            lastError = error;

            // reset the timer for next time
            timer.reset();
        }

        slide.setPower(0);

    }

}
