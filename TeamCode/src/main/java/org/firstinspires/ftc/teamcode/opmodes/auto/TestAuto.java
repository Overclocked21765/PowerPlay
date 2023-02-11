package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.SignalZone;
import org.firstinspires.ftc.teamcode.mechanisms.AutoDriveTrain;
import org.firstinspires.ftc.teamcode.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.mechanisms.beta.Camera;
import org.firstinspires.ftc.teamcode.vision.SleeveDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous()
public class TestAuto extends OpMode{
    private ElapsedTime runtime = new ElapsedTime();

    AutoDriveTrain autoDriveTrain = new AutoDriveTrain();
    Camera camera = new Camera();
    Claw claw = new Claw();
    double lastRuntTime;

    SignalZone signalZone;

    @Override
    public void init(){
        autoDriveTrain.init(hardwareMap);
        camera.init(hardwareMap);
        telemetry.addData("Initialization ", "Complete");
        lastRuntTime = 0;

    }

    @Override
    public void init_loop(){
        switch (camera.returnZoneEnumerated()){
            case LEFT:
                signalZone = SignalZone.ZONE_ONE;
                break;
            case CENTER:
                signalZone = SignalZone.ZONE_TWO;
                break;
            case RIGHT:
                signalZone = SignalZone.ZONE_THREE;
                break;
        }
        telemetry.addData("Signal zone: ", signalZone);
    }

    @Override
    public void start(){

        //grab
        //vision code and getting signal zone

        runtime.reset();

    }

    @Override
    public void loop() {
        if (runtime.time() <= (1.75 * Constants.Auto.ONE_TILE_FORWARD)) {
            autoDriveTrain.strafeLeft();
        } else if (runtime.time() <= (2.5 * Constants.Auto.ONE_TILE_FORWARD)) {
            autoDriveTrain.strafeRight();
        } else if (runtime.time() < (2.76 * Constants.Auto.ONE_SECOND)) {
                autoDriveTrain.stopDriving();
        } else {
            switch (signalZone) {
                case ZONE_TWO:
                    if (runtime.time() <= (3.65 * Constants.Auto.ONE_SECOND)) {
                        autoDriveTrain.rotateCounterClockWise();
                    } else if (runtime.time() < (3.9 * Constants.Auto.ONE_SECOND)){
                        autoDriveTrain.strafeLeft();
                    } else {
                        autoDriveTrain.stopDriving();
                    }
                    break;
                case ZONE_ONE:
                    if (runtime.time() <= (3.55 * Constants.Auto.ONE_SECOND)) {
                        autoDriveTrain.driveBackward();
                    } else if (runtime.time() < (4.5 * Constants.Auto.ONE_SECOND)) {
                        autoDriveTrain.rotateCounterClockWise();
                    } else {
                        autoDriveTrain.stopDriving();
                    }
                    break;
                case ZONE_THREE:
                    if (runtime.time() <= (3.5 * Constants.Auto.ONE_SECOND)) {
                        autoDriveTrain.driveForward();
                    } else if (runtime.time() < (4.5 * Constants.Auto.ONE_SECOND)) {
                        autoDriveTrain.rotateCounterClockWise();
                    } else {
                        autoDriveTrain.stopDriving();
                    }

            }

            telemetry.addData("Runtime: ", runtime.time());
            telemetry.addData("Parking Zone: ", signalZone);
        }
    }
}