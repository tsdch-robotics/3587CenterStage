package org.firstinspires.ftc.teamcode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class TeleOpTest extends OpMode {

    DcMotor FL;
    DcMotor FR;
    DcMotor BL;
    DcMotor BR;

    @Override
    public void init() {

        FL = hardwareMap.get(DcMotor.class,  "FL");
        FR = hardwareMap.get(DcMotor.class,  "FR");
        BL = hardwareMap.get(DcMotor.class,  "BL");
        BR = hardwareMap.get(DcMotor.class,  "BR");

        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        FL.setPower(gamepad1.left_stick_y);
        FR.setPower(gamepad1.left_stick_y);
        BR.setPower(gamepad1.left_stick_y);
        BL.setPower(gamepad1.left_stick_y);



;

    }
}


