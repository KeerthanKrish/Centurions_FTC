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
        
        wrist.setPosition(0.5);
        hands.setPosition(0.7);

        initTfod();

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
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

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        //if (pixel == false){
            for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
            telemetry.addData("Is pixel: ", pixel);
            
            //Move forward
            if (pixelLoc == 0){
                left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
                right_drive_back.setPower(0.5);
                sleep(2000);
                left_drive_front.setPower(0);
                left_drive_back.setPower(0);
                right_drive_front.setPower(0);
                right_drive_back.setPower(0);
                sleep(100);
                if(recognition.getConfidence()>=0.80 && x >= 400){
                    pixelLoc = 1;
                } else {
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
                    if(recognition.getConfidence()>=0.80 && x >= 400){
                        pixelLoc = 2;
                    } else {
                        pixelLoc = 3;
                    }
                }
            }
            
            if (pixelLoc == 1){
                //Move forward
                left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
                right_drive_back.setPower(0.5);
                sleep(3000);
                //Turn to left
                // Assigning power for the left wheels
                left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
                left_drive_back.setPower(0.5);
                // Assigning power for the right wheels
                right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
                right_drive_back.setPower(0.5);
                sleep(500);
                // Put purple pixel down
                left_drive_front.setPower(0);
                left_drive_back.setPower(0);
                right_drive_front.setPower(0);
                right_drive_back.setPower(0);
                sleep(1000);
                //Turn to right
                // Assigning power for the left wheels
                left_drive_front.setPower(-0.5); //use NEGATIVE value for moving forward
                left_drive_back.setPower(-0.5);
                // Assigning power for the right wheels
                right_drive_front.setPower(-0.5); //use POSITIVE value for moving backward
                right_drive_back.setPower(-0.5);
                sleep(500);
                // Back up to first spot
                left_drive_front.setPower(0.5);
                left_drive_back.setPower(0.5);
                right_drive_front.setPower(-0.5);
                right_drive_back.setPower(-0.5);
                sleep(900);
                // Turn to right
                left_drive_front.setPower(-0.5);
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(-0.5);
                right_drive_back.setPower(-0.5);
                sleep(1350);
                // Go forward
                left_drive_front.setPower(-0.5);
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(0.5);
                right_drive_back.setPower(0.5);
                sleep(3600);
                //turn left
                left_drive_front.setPower(0.5); //use NEGATIVE value for moving forward
                left_drive_back.setPower(0.5);
                // Assigning power for the right wheels
                right_drive_front.setPower(0.5); //use POSITIVE value for moving backward
                right_drive_back.setPower(0.5);
                sleep(1350);
                // Go forward
                left_drive_front.setPower(-0.5);
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(0.5);
                right_drive_back.setPower(0.5);
                sleep(1200);
                //Turn right
                left_drive_front.setPower(-0.5);
                left_drive_back.setPower(-0.5);
                right_drive_front.setPower(-0.5);
                right_drive_back.setPower(-0.5);
                sleep(1350);
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
    }   // end runOpMode()
