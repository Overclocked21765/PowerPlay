package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleeveDetection extends OpenCvPipeline {
    /*
    YELLOW  = Parking Left
    CYAN    = Parking Middle
    MAGENTA = Parking Right
     */

    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT,
    }

    // TOPLEFT anchor point for the bounding box
    private static Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(10, 115);

    // Width and height for the bounding box
    public static int REGION_WIDTH = 50; //200
    public static int REGION_HEIGHT = 50; //300

    // Color definitions
    private final Scalar
            YELLOW  = new Scalar(255, 255, 0), //og one is 255, 255, 0 (other teams magenta,: 255, 64, 128)
            CYAN    = new Scalar(82, 223, 2), //og 0, 255, 255 (other teams green: 89, 153, 13)
            MAGENTA = new Scalar(255, 0, 255); // 255. 0. 255 (other teams orange: 255, 128, 51)
    // Anchor point definitions
    Point sleeve_pointA = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);
    Point sleeve_pointB = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    // Running variable storing the parking position
    private volatile ParkingPosition position = ParkingPosition.LEFT;

    @Override
    public Mat processFrame(Mat input) {
        // Get the submat frame, and then sum all the values
        Mat areaMat = input.submat(new Rect(sleeve_pointA, sleeve_pointB));
        Scalar sumColors = Core.sumElems(areaMat);

        // Get the minimum RGB value from every single channel
        double minColor = Math.min(sumColors.val[0], Math.min(sumColors.val[1], sumColors.val[2]));

        // Change the bounding box color based on the sleeve color
         if (sumColors.val[1] == minColor) {
            position = ParkingPosition.RIGHT;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    MAGENTA,
                    2
            );
        } else if (sumColors.val[2] == minColor) {
            position = ParkingPosition.LEFT;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    YELLOW,
                    2
            );
        } else  {
            position = ParkingPosition.CENTER;
            Imgproc.rectangle(
                    input,
                    sleeve_pointA,
                    sleeve_pointB,
                    CYAN,
                    2
            );
        }

        // Release and return input
        areaMat.release();
        return input;
    }

    // Returns an enum being the current position where the robot will park
    public ParkingPosition getPosition() {
        return position;
    }
    public int getPositionSane() {
        if (position == ParkingPosition.LEFT) {
            return 1;
        }
        else if (position == ParkingPosition.CENTER) {
            return 2;
        }
        else if(position == ParkingPosition.RIGHT) {
            return 3;
        }
        else {
            return 4;
        }
    }
}