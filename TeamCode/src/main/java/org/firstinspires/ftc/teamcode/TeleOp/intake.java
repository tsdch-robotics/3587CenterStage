package org.firstinspires.ftc.teamcode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class intake extends OpMode {
   DcMotor intake;
   boolean isTriggerPressed = false;
    @Override
    public void init(){
        intake =  hardwareMap.dcMotor.get("intake");
    }

    public void loop(){
        if (gamepad1.left_trigger > .5){
            intake.setPower(1);
        isTriggerPressed = true;}
        else {
            if (isTriggerPressed){
            intake.setPower(0);
            isTriggerPressed = false;
            }
        }
    }
}