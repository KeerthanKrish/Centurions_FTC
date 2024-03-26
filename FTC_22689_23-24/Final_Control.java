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
    private DcMotor right_drive_front; // TODO: Need to just copy code from other file into this - get the expansion hub cable set up to test properly
    private DcMotor left_shoulder;
    private DcMotor right_shoulder;
    private Servo wrist;
    private Servo hands;

    public void runOpMode() {
        // Front Wheels
        left_drive_front = hardwareMap.get(DcMotor.class, "motor_front_left");
        right_drive_front = hardwareMap.get(DcMotor.class, "motor_front_right");
        
        // Back Wheels
        left_drive_back = hardwareMap.get(DcMotor.class, "motor_back_left");
        right_drive_back = hardwareMap.get(DcMotor.class, "motor_back_right");
        
        // Shoulders
        left_shoulder = hardwareMap.get(DcMotor.class, "left_shoulder");
        right_shoulder = hardwareMap.get(DcMotor.class, "right_shoulder");
        
        //Wrist (Servo Motor)
        wrist = hardwareMap.get(Servo.class, "wrist_servo");
        hands = hardwareMap.get(Servo.class, "hands_servo");
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // Variables for controlling wheel speed/power
        double powerLeft = 0;
        double powerRight = 0;
        
        while (opModeIsActive()) {
            telemetry.update();
            
            // Getting input from gamePad
            powerLeft = this.gamepad2.left_stick_y;
            powerRight = this.gamepad2.right_stick_y;
            
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
            
            if (this.gamepad1.x)
            {
                hands.setPosition(0.98);
            }
            if (this.gamepad1.b)
            {
                hands.setPosition(0.7);   
            }
            if (this.gamepad1.y)
            {
                wrist.setPosition(1);
            }
            if (this.gamepad1.a)
            {
                wrist.setPosition(0);   
            }
        }
    }
}
