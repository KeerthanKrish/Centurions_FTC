package org.firstinspires.ftc.teamcode;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.motors.TetrixMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

public class Final_Control extends LinearOpMode {
    
    private DcMotor left_drive_back;
    private DcMotor right_drive_back;
    private DcMotor left_drive_front;
    private DcMotor right_drive_front;
    private DcMotor left_shoulder;
    private DcMotor right_shoulder;
    private Servo wrist;
    private Servo hands;
    private DcMotor Drone_Shooter;

    public void runOpMode() {
        // Drone Shooter 
        Drone_Shooter = hardwareMap.get(DcMotor.class,"Drone_Shooter");
        // Front Wheels
        left_drive_front = hardwareMap.get(DcMotor.class, "motor_front_left");
        right_drive_front = hardwareMap.get(DcMotor.class, "motor_front_right");
        
        // Back Wheels
        left_drive_back = hardwareMap.get(DcMotor.class, "motor_back_left");
        right_drive_back = hardwareMap.get(DcMotor.class, "motor_back_right");
        
        // Shoulders (w/ encoders)
        left_shoulder = hardwareMap.get(DcMotor.class, "left_shoulder");
        left_shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_shoulder = hardwareMap.get(DcMotor.class, "right_shoulder");
        right_shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //Wrist & Hand(Servo Motor)
        wrist = hardwareMap.get(Servo.class, "wrist_servo");
        hands = hardwareMap.get(Servo.class, "hands_servo");
        
        //Setting initial positions
        wrist.setPosition(1);
        hands.setPosition(0.5);
        left_shoulder.setTargetPosition(0);
        right_shoulder.setTargetPosition(0);
        left_shoulder.setPower(-0.2);
        right_shoulder.setPower(0.2);
        left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // Variables for controlling wheel speed/power
        double powerLeft = 0;
        double powerRight = 0;
        
        // declaring variable for powerForWrist
        double powerForWrist = 0;
        
        while (opModeIsActive()) {
            
            //Displaying encoder ticks for shoulder
            telemetry.update();
            telemetry.addData("RIGHT Motor Ticks: ", right_shoulder.getCurrentPosition());
            telemetry.addData("LEFT Motor Ticks: ", left_shoulder.getCurrentPosition());
            // getting input from gamepad 1  
            powerForWrist = this.gamepad1.right_stick_y;
            
            // Getting input from gamePad
            powerLeft = this.gamepad2.left_stick_y;
            powerRight = this.gamepad2.right_stick_y;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(powerLeft);
            left_drive_back.setPower(powerLeft);
            
            // Assigning power for the right wheels
            right_drive_front.setPower(-powerRight);
            right_drive_back.setPower(-powerRight);
            
            // adding power to move the wrist up/down
            /*while (powerForWrist < 0) { // stick up
                double currPosWrist = wrist.getPosition();
                double newPosWrist = currPosWrist + 0.05;
                
                telemetry.addData("New position: ", newPosWrist);
                telemetry.update();
                wrist.setPosition(newPosWrist);
            }*/
            

            // Shooting the plane
            if (gamepad2.a){
                Drone_Shooter.setPower(-1);
            }
            else {
                Drone_Shooter.setPower(0);
            }

            if(gamepad2.dpad_left){
                left_drive_front.setPower(-0.9);
                left_drive_back.setPower(1);
                right_drive_front.setPower(-0.9);
                right_drive_back.setPower(1);
            }
            
            //Crabbing left
            if(gamepad2.dpad_right){
                left_drive_front.setPower(0.9);
                left_drive_back.setPower(-1);
                right_drive_front.setPower(0.9);
                right_drive_back.setPower(-1);
            }
            
            //Close hand
            if (this.gamepad1.x)
            {
                wrist.setPosition(1);
            }
            
            //Open hand
            if (this.gamepad1.b)
            {
                hands.setPosition(0.5);
                
            }
            
            //Put wrist down    
            if (this.gamepad1.a)
            {
                hands.setPosition(0.8);
                   
               
            }
            
            //Pull wrist up
            if (this.gamepad1.y)
            {
                wrist.setPosition(0); 
                
                  
            }
            
            //Move arm up
            if(gamepad1.dpad_up && left_shoulder.getCurrentPosition() < 1400 && right_shoulder.getCurrentPosition() > -1400){
                int curPosL = left_shoulder.getCurrentPosition();
                int curPosR = right_shoulder.getCurrentPosition();
                left_shoulder.setTargetPosition((int)curPosL+20);
                right_shoulder.setTargetPosition((int)curPosR-20);
                left_shoulder.setPower(1);
                right_shoulder.setPower(-1);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            
            //Move arm down
            if(gamepad1.dpad_down && left_shoulder.getCurrentPosition() > -2 && right_shoulder.getCurrentPosition() < 2){
                int curPosL = left_shoulder.getCurrentPosition();
                int curPosR = right_shoulder.getCurrentPosition();
                left_shoulder.setTargetPosition((int)curPosL-20);
                right_shoulder.setTargetPosition((int)curPosR+20);
                left_shoulder.setPower(-1);
                right_shoulder.setPower(1);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            
            //Move arm and wrist position to board position
            if(gamepad1.dpad_right){
                left_shoulder.setTargetPosition(540);
                right_shoulder.setTargetPosition(-540);
                left_shoulder.setPower(0.3);
                right_shoulder.setPower(0.3);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                wrist.setPosition(0.8);
            }
            
            //Send arm to home (down) position
            if(gamepad1.left_stick_y == 1){
                left_shoulder.setTargetPosition(0);
                right_shoulder.setTargetPosition(0);
                left_shoulder.setPower(-0.2);
                right_shoulder.setPower(0.2);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
            }
            
            //Send arm to max (up) position
            if(gamepad1.left_stick_y == -1){
                // Move arm
                left_shoulder.setTargetPosition(1399);
                right_shoulder.setTargetPosition(-1399);
                left_shoulder.setPower(0.2);
                right_shoulder.setPower(0.2);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            
            // Freaky Mode (move arm past 1400 & adjust wrist)
            if (gamepad1.dpad_left) {
                // Move wrist
                wrist.setPosition(0.3);
                
                left_shoulder.setTargetPosition(1800);
                right_shoulder.setTargetPosition(-1800);
                left_shoulder.setPower(0.15);
                right_shoulder.setPower(0.15);
                left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
    }
}