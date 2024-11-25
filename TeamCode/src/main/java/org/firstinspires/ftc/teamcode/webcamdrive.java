package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Webcam Streaming with Controls", group = "Example")
public class webcamdrive extends LinearOpMode {

    private OpenCvCamera webcam;
    private DcMotor leftMotor, rightMotor;

    @Override
    public void runOpMode() {
        // Initialize hardware for motors
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");

        // Set motor directions if needed (e.g., if one side is reversed)
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Retrieve webcam from hardware map
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        // Set the pipeline
        webcam.setPipeline(new SamplePipeline());

        // Open the camera asynchronously
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Start streaming when the camera opens
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error", "Camera failed to open with error code: " + errorCode);
                telemetry.update();
            }
        });

        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Read joystick inputs
            double forwardBackward = -gamepad1.left_stick_y; // Forward/Backward
            double turn = gamepad1.right_stick_x;           // Left/Right turn

            // Calculate motor power
            double leftPower = forwardBackward + turn;
            double rightPower = forwardBackward - turn;

            // Apply power to motors
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);

            // Add webcam telemetry
            telemetry.addData("Frame Count", webcam.getFrameCount());
            telemetry.addData("FPS", webcam.getFps());
            telemetry.addData("Left Motor Power", leftPower);
            telemetry.addData("Right Motor Power", rightPower);
            telemetry.update();

            sleep(50); // Small delay to avoid overwhelming the control loop
        }
    }

    // Pipeline implementation
    static class SamplePipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Example: Convert the input frame to grayscale
            Mat grayFrame = new Mat();
            Imgproc.cvtColor(input, grayFrame, Imgproc.COLOR_RGB2GRAY);
            return grayFrame; // Return the processed frame
        }
    }
}