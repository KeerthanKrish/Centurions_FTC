package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class NewControl extends LinearOpMode {
    private DcMotor left_drive_back;
    private DcMotor right_drive_back;
    private DcMotor left_drive_front;
    private DcMotor right_drive_front;
    private DcMotor top_shoulder;
    private DcMotor bottom_shoulder;
    private Servo wrist;
    private Servo finger;
    private DcMotor elbow; // TODO: Need to just copy code from other file into this - get the expansion hub cable set up to test properly
    
    @Override
    public void runOpMode() {
        // Front Wheels
        left_drive_back = hardwareMap.get(DcMotor.class, "motor_front_left");
        right_drive_back = hardwareMap.get(DcMotor.class, "motor_front_right");
        
        // Back Wheels
        left_drive_front = hardwareMap.get(DcMotor.class, "motor_back_left");
        right_drive_front = hardwareMap.get(DcMotor.class, "motor_back_right");
        
        // Shoulder parts
        top_shoulder = hardwareMap.get(DcMotor.class, "top_shoulder");
        bottom_shoulder = hardwareMap.get(DcMotor.class, "bottom_shoulder");
        
        // Elbow joint
        elbow = hardwareMap.get(DcMotor.class, "elbow");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // Variables for controlling wheel speed/power
        double powerLeft = 0;
        double powerRight = 0;

        while (opModeIsActive()) {
            // Getting input from gamePad
            powerLeft = this.gamepad2.left_stick_y;
            powerRight = this.gamepad2.right_stick_y;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(powerLeft);
            left_drive_back.setPower(powerLeft);
            
            // Assigning power for the right wheels
            right_drive_front.setPower(-powerRight);
            right_drive_back.setPower(-powerRight);
            
            //Assigning power for both shoulder components
            if(gamepad1.dpad_down)
            {
                top_shoulder.setPower(1);
            }else if(gamepad1.dpad_up)
            {
                top_shoulder.setPower(-1);
            }else if(gamepad1.dpad_right)
            {
                bottom_shoulder.setPower(0.2);
            }else if(gamepad1.dpad_left)
            {
                bottom_shoulder.setPower(-0.2);
            }else
            {
                top_shoulder.setPower(0);
                bottom_shoulder.setPower(0);
            }
            
            //Assigning power to the elbow movements
            if(gamepad1.right_bumper)
            {
                elbow.setPower(0.5);
            }else if(gamepad1.right_trigger > 0)
            {
                elbow.setPower(-0.5);
            }else
            {
                elbow.setPower(0);
            }
          
            }
            
            if(gamepad1.a)
            {
                elbow.setPower(0.2);
            }else if(gamepad1.right_trigger > 0)
            {
                elbow.setPower(-0.2);
            }
        
        
       
    }
}