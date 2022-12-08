package frc.robot.util;
//ani was here (:
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SnailSubsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import static frc.robot.Constants.Vision.*;

public class SnailVision {
    private static NetworkTable table = NetworkTableInstance.getDefault().getTable("Vision");
    private static int pipeline = 0;
    private static double[] targetsX = new double[] {0.0,0.0,0.0};
    private static double[] targetsY = new double[] {0.0,0.0,0.0};

    // Pi sends this data
    private static NetworkTableEntry targetX = table.getEntry("tx");
    private static NetworkTableEntry targetY = table.getEntry("ty");
    private static NetworkTableEntry targetValid = table.getEntry("tv");
    private static NetworkTableEntry targetArea = table.getEntry("ta");

    // Driver Station sends this data
    private static NetworkTableEntry lowerH = table.getEntry("lower-h");
    private static NetworkTableEntry lowerS = table.getEntry("lower-s");
    private static NetworkTableEntry lowerV = table.getEntry("lower-v");
    private static NetworkTableEntry upperH = table.getEntry("upper-h");
    private static NetworkTableEntry upperS = table.getEntry("upper-s");
    private static NetworkTableEntry upperV = table.getEntry("upper-v");
    private static NetworkTableEntry mode = table.getEntry("mode");


    public static void init() {
        // initialize variables on shuffleboard
        targetX.setDefaultDoubleArray(new double[] {0,0,0});
        targetY.setDefaultDoubleArray(new double[] {0,0,0});
        
        lowerH.setDefaultNumber(LOWER[0]);
        lowerS.setDefaultNumber(LOWER[1]);
        lowerV.setDefaultNumber(LOWER[2]);
        upperH.setDefaultNumber(UPPER[0]);
        upperS.setDefaultNumber(UPPER[1]);
        upperV.setDefaultNumber(UPPER[2]);

        mode.setDefaultValue(pipeline); 

        targetValid.setDefaultNumber(0.0);
        targetArea.setDefaultNumber(0.0);

        System.out.println("I'm some data " + table.getEntry("lower").getDouble(0));
    }

    public static void printData() {
        System.out.println("I'm some data " + table.getEntry("tx").getDouble(0));
    }

    public static double getTargetX() {
        return targetX.getDouble(0.0);
    }

    public static double getTargetY() {
        return targetY.getDouble(0.0);
    }

    public static void updateTargets() {
        targetsX = targetX.getDoubleArray(new double[] {0.0});
        targetsY = targetY.getDoubleArray(new double[] {0.0});
    }

    // percent likelihood it is the target
    public static double getTargetValid() {
        return targetValid.getDouble(0.0);
    }

    public static double getTargetArea() {
        return targetArea.getDouble(0.0);
    }

    public static boolean isTargetValid () {
        return getTargetValid() > 0;
    }

    public static double getVisionAdd() {
        double visionAdd = 0.0;

        if (isTargetValid()) { 
            visionAdd = Math.copySign(VISION_FEEDFORWARD, getTargetX());
            visionAdd += VISION_KP * getTargetX();
        }

        return visionAdd;
    }

    public static void setConstantTuning() {
        SmartDashboard.putNumber("Vision Feedforward", VISION_FEEDFORWARD);
        SmartDashboard.putNumber("Vision kP", VISION_KP);
    }

    public static void getConstantTuning() {
        VISION_FEEDFORWARD = SmartDashboard.getNumber("Vision Feedforward", VISION_FEEDFORWARD);
        VISION_KP = SmartDashboard.getNumber("Vision kP", VISION_KP);
    }
    
}
