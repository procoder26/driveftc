package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Ball Drive")

public class drive extends LinearOpMode {

    private DcMotor leftMotor, rightMotor;
    private Servo claw;

    @Override
    public void runOpMode() {
        // Initialize hardware for motors
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");
        claw = hardwareMap.get(Servo.class, "claw");


        // Set motor directions if needed (e.g., if one side is reversed)
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        // Adjust the turn sensitivity
        final double TURN_SENSITIVITY = 0.5; // Reduce turning sensitivity
        final double closedness = 0; //

        while (opModeIsActive()) {
            // Read joystick inputs
            double forwardBackward = -gamepad1.left_stick_y; // Forward/Backward
            double turn = gamepad1.right_stick_x * TURN_SENSITIVITY; // Scale turning input

            // Calculate motor power
            double leftPower = forwardBackward + turn;
            double rightPower = forwardBackward - turn;

            // Apply power to motors
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);

            if (gamepad2.b) {
                claw.setPosition(1);
            }
            if (gamepad2.x){
                claw.setPosition(closedness);
            }
            // Add telemetry for motor powers
            telemetry.addData("Left Motor Power", leftPower);
            telemetry.addData("Right Motor Power", rightPower);
            telemetry.addData("Turn Sensitivity", TURN_SENSITIVITY);
            telemetry.update();

            sleep(50); // Small delay to avoid overwhelming the control loop
        }
    }
}
