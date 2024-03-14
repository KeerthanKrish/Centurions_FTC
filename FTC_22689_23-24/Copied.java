package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
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

@TeleOp

public class Copied extends LinearOpMode {
    private DcMotor left_drive_back;
    private DcMotor right_drive_back;
    private DcMotor left_drive_front;
    private DcMotor right_drive_front; // TODO: Need to just copy code from other file into this - get the expansion hub cable set up to test properly
    private DcMotor left_shoulder;
    private DcMotor right_shoulder;
    private Servo wrist;
    private Servo hands;
    
    
    
    @Override
    public void runOpMode() {
        // Front Wheels
        left_drive_back = hardwareMap.get(DcMotor.class, "motor_front_left");
        right_drive_back = hardwareMap.get(DcMotor.class, "motor_front_right");
        
        // Back Wheels
        left_drive_front = hardwareMap.get(DcMotor.class, "motor_back_left");
        right_drive_front = hardwareMap.get(DcMotor.class, "motor_back_right");
        
        // Shoulders
        left_shoulder = hardwareMap.get(DcMotor.class, "left_shoulder");
        right_shoulder = hardwareMap.get(DcMotor.class, "right_shoulder");
        
        //Wrist (Servo Motor)
        wrist = hardwareMap.get(Servo.class, "wrist_servo");
        hands = hardwareMap.get(Servo.class, "hands_servo");
        
        wrist.setPosition(0.5);
        hands.setPosition(0.5);
        
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // Variables for controlling wheel speed/power
        double powerLeft = 0;
        double powerRight = 0;
        double powerLeftX = 0;
        double powerRightX = 0;
        
        // Variables for controlling shoulder power
        double powerLeftShoulder = 0;
        double powerRightShoulder = 0;
        
        //boolean to control hold and release of hands(pixel picker)
        boolean closedHands = false;
        
        while (opModeIsActive()) {
            // Getting input from gamePad
            powerLeft = this.gamepad2.left_stick_y;
            powerRight = this.gamepad2.right_stick_y;
            
            powerLeftX = this.gamepad2.left_stick_x;
            powerRightX = this.gamepad2.right_stick_x;
            
            powerLeftShoulder = this.gamepad1.left_stick_y;
            powerRightShoulder = this.gamepad1.left_stick_y;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(powerLeft);
            left_drive_back.setPower(powerLeft);
            
            // Assigning power for the right wheels
            right_drive_front.setPower(-powerRight);
            right_drive_back.setPower(-powerRight);
            
            if(gamepad2.dpad_right){
                left_drive_front.setPower(-0.9);
                left_drive_back.setPower(1);
                right_drive_front.setPower(-0.9);
                right_drive_back.setPower(1);
            }
            
            if(gamepad2.dpad_left){
                left_drive_front.setPower(0.9);
                left_drive_back.setPower(-1);
                right_drive_front.setPower(0.9);
                right_drive_back.setPower(-1);
            }
            
            //Assigning power for the shoulders
            left_shoulder.setPower(-powerLeftShoulder);
            right_shoulder.setPower(powerRightShoulder);
            
            if (this.gamepad1.x)
            {
                hands.setPosition(0.9);
            }
            if (this.gamepad1.b)
            {
                hands.setPosition(0.5);   
            }
            if (this.gamepad1.y)
            {
                wrist.setPosition(0.5);
            }
            if (this.gamepad1.a)
            {
                wrist.setPosition(0.3);   
            }
            
            /*
            if (this.gamepad2.x && closedHands == false)
            {
                hands.setPosition(30);
                closedHands = true;
            }
            else if (this.gamepad2.x && closedHands == true)
            {
                hands.setPosition(120);
                closedHands = false;
            }
            
            if (this.gamepad2.b)
            {
                hands.setPosition(120); 
                //closedHands = false;

            }
            */
            
        
            
        }
        
       
    }
}
