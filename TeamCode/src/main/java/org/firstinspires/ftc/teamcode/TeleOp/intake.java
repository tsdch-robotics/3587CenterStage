package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class intake extends OpMode {
    //DcMotor intake;
    boolean isTriggerPressed = false;
    public Servo wheel;
    public Servo door;
   public Servo larm;
   public Servo rarm;

    @Override
    public void init() {
        //intake = hardwareMap.dcMotor.get("intake");
        wheel = hardwareMap.get(Servo.class, "wheel");
        door = hardwareMap.get(Servo.class, "door");
        larm = hardwareMap.get(Servo.class, "larm");
        rarm = hardwareMap.get(Servo.class, "rarm");


        larm.setDirection(Servo.Direction.REVERSE);
        rarm.setDirection(Servo.Direction.FORWARD);
        larm.setPosition(0.0);
        rarm.setPosition(0.0);
    }

    public void setwheelPosition(double position) {
        wheel.setPosition(position);

    }

    public void loop() {
        // if (gamepad1.left_trigger > .5) {
        //   intake.setPower(1);
        //    isTriggerPressed = true;
        //} else {
        //   if (isTriggerPressed) {
        //      intake.setPower(0);
        //      isTriggerPressed = false;
        //   }
        //  }
        if (gamepad1.dpad_left) {
            larm.setPosition(0.0);
            rarm.setPosition(0.0);
        } else {
            telemetry.addData("testing", "true");
        }
        if (gamepad1.dpad_right) {
            rarm.setPosition(1.0);
            larm.setPosition(1.0);
        } else {
            telemetry.addData("testing2", "true");
        }
    }
}
