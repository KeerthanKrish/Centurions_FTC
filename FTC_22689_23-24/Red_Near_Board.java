package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
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

import java.util.List;

/*
 * This OpMode illustrates the basics of TensorFlow Object Detection, using
 * the easiest way.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@Autonomous(name = "Red_Near_Board")

public class Red_Near_Board extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;
    
    private boolean pixel;
    private DcMotor left_drive_back;
    private DcMotor right_drive_back;
    private DcMotor left_drive_front;
    private DcMotor right_drive_front; // TODO: Need to just copy code from other file into this - get the expansion hub cable set up to test properly
    private DcMotor left_shoulder;
    private DcMotor right_shoulder;
    private Servo wrist;
    private Servo hands;
    
    private int pixelLoc = 0;
    private int tics = 0;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {
        
        // Front Wheels
        left_drive_back = hardwareMap.get(DcMotor.class, "motor_back_left");
        right_drive_back = hardwareMap.get(DcMotor.class, "motor_back_right");
        
        // Back Wheels
        left_drive_front = hardwareMap.get(DcMotor.class, "motor_front_left");
        right_drive_front = hardwareMap.get(DcMotor.class, "motor_front_right");
        
        // Shoulders
        left_shoulder = hardwareMap.get(DcMotor.class, "left_shoulder");
        right_shoulder = hardwareMap.get(DcMotor.class, "right_shoulder");
        
        //Wrist (Servo Motor)
        wrist = hardwareMap.get(Servo.class, "wrist_servo");
        hands = hardwareMap.get(Servo.class, "hands_servo");
        
        wrist.setPosition(0.7);
        hands.setPosition(0.8);

        initTfod();

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            
            // move to the lines
            left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(0.5);
            sleep(2100);
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            while (opModeIsActive()) {

                telemetryTfod();

                // Push telemetry to the Driver Station.
                telemetry.update();

                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();
    }
    
        /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), tfod);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                BuiltinCameraDirection.BACK, tfod);
        }

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {
        pixelLoc = 0;
        tics++;
        telemetry.addLine("Pixel Location variable value: " + pixelLoc);
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());
        telemetry.addData("Tics ", tics);
        if (pixelLoc == 0){
            if ((currentRecognitions.size() > 0 && currentRecognitions.get(0).getConfidence()>=0.50 && (currentRecognitions.get(0).getLeft() + currentRecognitions.get(0).getRight() / 2) >= 400) && tics < 130){
                telemetry.addData("# Objects Detected", currentRecognitions.size());
                pixelLoc = 1;
            } else if (tics >= 130){
                if (tics == 130){
                    sleep(200);
                    //Turn to left
                    left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
                    left_drive_back.setPower(0.5);
                    right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
                    right_drive_back.setPower(0.5);
                    sleep(200);
                    left_drive_front.setPower(0);
                    left_drive_back.setPower(0);
                    right_drive_front.setPower(0);
                    right_drive_back.setPower(0);
                    sleep(100);
                    left_drive_front.setPower(-0.5);
                    left_drive_back.setPower(-0.5);
                    right_drive_front.setPower(0.5);
                    right_drive_back.setPower(0.5);
                    sleep(300);
                    left_drive_front.setPower(0);
                    left_drive_back.setPower(0);
                    right_drive_front.setPower(0);
                    right_drive_back.setPower(0);
                    sleep(200);
                }
                if(currentRecognitions.size() > 0 && currentRecognitions.get(0).getConfidence()>=0.50 && (tics > 130 && tics <= 260)){
                    pixelLoc = 2;
                    left_drive_front.setPower(0.5);
                    left_drive_back.setPower(0.5);
                    right_drive_front.setPower(-0.5);
                    right_drive_back.setPower(-0.5);
                    sleep(300);
                    left_drive_front.setPower(0);
                    left_drive_back.setPower(0);
                    right_drive_front.setPower(0);
                    right_drive_back.setPower(0);
                    sleep(200);
                } else if (tics > 260) {
                    pixelLoc = 3;
                }
            }
        }

        if (pixelLoc == 1){

            //Turn to right
            // Assigning power for the left wheels
            left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(-0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(-0.5);
            sleep(1000);

            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);

            // Open the hand and put purple pixel down
            hands.setPosition(0.5);
            sleep(1000);

            //Turn to left (facing left side of the map)
            // Assigning power for the left wheels
            left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(0.5);
            sleep(1000);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            /* 
                Back up to first spot
                Back up to the tile behind - get out of the pixel area
            */
            left_drive_front.setPower(0.5);
            left_drive_back.setPower(0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(900);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            // Turn to right (facing board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(1450);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(300);

            // Go forward (all the way to the trapezoid in front of the board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(3000);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(300);
            
            /*

            // turn left (to face perpendicular to the board)
            left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(0.5);
            sleep(1350);

            // Go forward (to the opposite side of the board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(1200);

            //Turn right (facing board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(1350);

            // Go forward (closest to the board)
            // TODO: may need to change this
            left_drive_front.setPower(-0.2);
            left_drive_back.setPower(-0.2);
            right_drive_front.setPower(-0.2);
            right_drive_back.setPower(-0.2);
            sleep(500);
            */
            
            //Pick pixel from robot
            wrist.setPosition(0);
            sleep(1000);
            hands.setPosition(0.8);
            sleep(1000);
            wrist.setPosition(0.2);
            sleep(500);
            wrist.setPosition(0.4);
            sleep(500);
            wrist.setPosition(0.8);
            sleep(1000);
            
            // Dropping the pixel to the board (arms and wrist)
            left_shoulder.setTargetPosition(650);
            right_shoulder.setTargetPosition(-650);
            left_shoulder.setPower(0.5);
            right_shoulder.setPower(0.5);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(900);

            // stop
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(200);
            
            //Turn right into board
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(350);
            
            // Go forward into board
            left_drive_front.setPower(-0.3);
            left_drive_back.setPower(-0.3);
            right_drive_front.setPower(0.3);
            right_drive_back.setPower(0.3);
            sleep(1000);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0.3); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.3);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.3); //use POSITIVE value for moving negative
            right_drive_back.setPower(-0.3);
            sleep(730);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            left_shoulder.setTargetPosition(350);
            right_shoulder.setTargetPosition(-350);
            left_shoulder.setPower(0.3);
            right_shoulder.setPower(0.3);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(500);
            
            hands.setPosition(0.5);
            sleep(200);
            
            // reset pixel location 
            pixelLoc = -1;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(1); //use NEGATIVE value for moving forward
            left_drive_back.setPower(1);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(3000);
        } else if (pixelLoc == 2) {
            //Turn to right
            left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(-0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(-0.5);
            sleep(200);
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(100);
            
            //Move forward
            // Assigning power for the left wheels
            left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(-0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(0.5);
            sleep(700);

            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);

            // Open the hand and put purple pixel down
            hands.setPosition(0.5);
            sleep(1000);

            //Move backwards (facing left side of the map)
            // Assigning power for the left wheels
            left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(-0.5);
            sleep(700);

            /* 
                Back up to first spot
                Back up to the tile behind - get out of the pixel area
            */
            left_drive_front.setPower(0.5);
            left_drive_back.setPower(0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(900);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            // Turn to left ( facimng away from board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(1500);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(300);

            // Go forward (all the way to the trapezoid in front of the board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(3000);

            // stop
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(450);
            
            //Pick pixel from robot
            wrist.setPosition(0);
            sleep(1000);
            hands.setPosition(0.8);
            sleep(1000);
            wrist.setPosition(0.2);
            sleep(500);
            wrist.setPosition(0.4);
            sleep(500);
            wrist.setPosition(0.8);
            sleep(1000);
            
            // Dropping the pixel to the board (arms and wrist)
            left_shoulder.setTargetPosition(650);
            right_shoulder.setTargetPosition(-650);
            left_shoulder.setPower(0.5);
            right_shoulder.setPower(0.5);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(900);

            // stop
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(200);
            
            //Turn right into board
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(350);
            
            // Go forward into board
            left_drive_front.setPower(-0.3);
            left_drive_back.setPower(-0.3);
            right_drive_front.setPower(0.3);
            right_drive_back.setPower(0.3);
            sleep(1000);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0.3); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.3);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.3); //use POSITIVE value for moving negative
            right_drive_back.setPower(-0.3);
            sleep(730);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            left_shoulder.setTargetPosition(350);
            right_shoulder.setTargetPosition(-350);
            left_shoulder.setPower(0.3);
            right_shoulder.setPower(0.3);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(500);
            
            hands.setPosition(0.5);
            sleep(200);
            
            // reset pixel location 
            pixelLoc = -1;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(1); //use NEGATIVE value for moving forward
            left_drive_back.setPower(1);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(3000);
        } else if (pixelLoc == 3) {
            //Turn to left
            // Assigning power for the left wheels
            left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(0.5);
            sleep(1000);

            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);

            // Open the hand and put purple pixel down
            hands.setPosition(0.5);
            sleep(1000);

            //Turn to right (facing left side of the map)
            // Assigning power for the left wheels
            left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
            left_drive_back.setPower(-0.5);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.5); //use POSITIVE value for moving backward
            right_drive_back.setPower(-0.5);
            sleep(1000);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            /* 
                Back up to first spot
                Back up to the tile behind - get out of the pixel area
            */
            left_drive_front.setPower(0.5);
            left_drive_back.setPower(0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(900);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(500);

            // Turn to left ( facimng away from board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(-0.5);
            right_drive_back.setPower(-0.5);
            sleep(1500);
            
            left_drive_front.setPower(0);
            left_drive_back.setPower(0);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(300);

            // Go forward (all the way to the trapezoid in front of the board)
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(3000);

            // stop
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(450);
            
            //Pick pixel from robot
            wrist.setPosition(0);
            sleep(1000);
            hands.setPosition(0.8);
            sleep(1000);
            wrist.setPosition(0.2);
            sleep(500);
            wrist.setPosition(0.4);
            sleep(500);
            wrist.setPosition(0.8);
            sleep(1000);
            
            // Dropping the pixel to the board (arms and wrist)
            left_shoulder.setTargetPosition(650);
            right_shoulder.setTargetPosition(-650);
            left_shoulder.setPower(0.5);
            right_shoulder.setPower(0.5);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0.5);
            right_drive_back.setPower(0.5);
            sleep(900);

            // stop
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(200);
            
            //Turn right into board
            left_drive_front.setPower(-0.5);
            left_drive_back.setPower(-0.5);
            right_drive_front.setPower(0);
            right_drive_back.setPower(0);
            sleep(350);
            
            // Go forward into board
            left_drive_front.setPower(-0.3);
            left_drive_back.setPower(-0.3);
            right_drive_front.setPower(0.3);
            right_drive_back.setPower(0.3);
            sleep(1000);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            // Assigning power for the left wheels
            left_drive_front.setPower(0.3); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0.3);
            // Assigning power for the right wheels
            right_drive_front.setPower(-0.3); //use POSITIVE value for moving negative
            right_drive_back.setPower(-0.3);
            sleep(730);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(100);
            
            left_shoulder.setTargetPosition(350);
            right_shoulder.setTargetPosition(-350);
            left_shoulder.setPower(0.3);
            right_shoulder.setPower(0.3);
            left_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            left_drive_front.setPower(0); //use NEGATIVE value for moving forward
            left_drive_back.setPower(0);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(500);
            
            hands.setPosition(0.5);
            sleep(200);
            
            // reset pixel location 
            pixelLoc = -1;
            
            // Assigning power for the left wheels
            left_drive_front.setPower(1); //use NEGATIVE value for moving forward
            left_drive_back.setPower(1);
            // Assigning power for the right wheels
            right_drive_front.setPower(0); //use POSITIVE value for moving negative
            right_drive_back.setPower(0);
            sleep(3000);
        }
        
        // Assigning power for the left wheels
        left_drive_front.setPower(0); //use NEGATIVE value for moving forward
        left_drive_back.setPower(0);
        // Assigning power for the right wheels
        right_drive_front.setPower(0); //use POSITIVE value for moving negative
        right_drive_back.setPower(0);
        }   // end for() loop

    }   // end method telemetryTfod()

//}   // end class
//    }   // end runOpMode()
