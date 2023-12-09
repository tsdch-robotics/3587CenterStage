package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class intake extends OpMode {
    DcMotor intake;
    boolean isTriggerPressed = false;
    public Servo door;
    public Servo larm;
    public Servo rarm;
    public CRServo wheel;

    @Override
    public void init() {
        intake = hardwareMap.dcMotor.get("BL");
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

    }


    public void loop() {
        if (gamepad1.left_trigger > .5) {
            intake.setPower(1);
            wheel.setPower(-2);
            isTriggerPressed = true;
        } else {
            if (isTriggerPressed) {
                intake.setPower(0);
                wheel.setPower(0);
                isTriggerPressed = false;
            }
            if (gamepad1.right_trigger > .5) {
                intake.setPower(1);
                wheel.setPower(-3);
                isTriggerPressed = true;
            } else {
                if (isTriggerPressed) {
                    intake.setPower(0);
                    wheel.setPower(0);
                    isTriggerPressed = false;
                }
            }
            if (gamepad1.left_bumper) {
                door.setPosition(0.0);

            } else {
                telemetry.addData("testing", "true");
            }
            if (gamepad1.right_bumper) {
                door.setPosition(1.0);

            } else {
                telemetry.addData("testing2", "true");
            }
            if (gamepad1.dpad_left) {
                larm.setPosition(0.0);
                rarm.setPosition(0.0);
            } else {
                telemetry.addData("testing", "true");
            }
            if (gamepad1.dpad_right) {
                larm.setPosition(1.0);
                rarm.setPosition(1.0);

            } else {
                telemetry.addData("testing", "true");
            }
        }
    }
}
