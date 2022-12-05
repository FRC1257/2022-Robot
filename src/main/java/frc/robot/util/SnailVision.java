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
    private double[] targets;

    private static NetworkTableEntry targetX = table.getEntry("tx");
    private static NetworkTableEntry targetY = table.getEntry("ty");
    private static NetworkTableEntry lowerH = table.getEntry("lower-h");
    private static NetworkTableEntry lowerS = table.getEntry("lower-s");
    private static NetworkTableEntry lowerV = table.getEntry("lower-v");
    private static NetworkTableEntry upperH = table.getEntry("upper-h");
    private static NetworkTableEntry upperS = table.getEntry("upper-s");
    private static NetworkTableEntry upperV = table.getEntry("upper-v");
    private static NetworkTableEntry mode = table.getEntry("mode");


    public static void init() {

        targetX.setDefaultDoubleArray(new double[] {0,0,0});
        targetY.setDefaultDoubleArray(new double[] {0,0,0});
        
        lowerH.setDefaultNumber(LOWER[0]);
        lowerS.setDefaultNumber(LOWER[1]);
        lowerV.setDefaultNumber(LOWER[2]);
        upperH.setDefaultNumber(UPPER[0]);
        upperS.setDefaultNumber(UPPER[1]);
        upperV.setDefaultNumber(UPPER[2]);

        mode.setDefaultValue(pipeline); 


        System.out.println("I'm some data " + table.getEntry("lower").getDouble(0));
    }

    public static void printData() {
        System.out.println("I'm some data " + table.getEntry("tx").getDouble(0));
    }

    public static double getTargetX() {
        return table.getEntry("tx").getDouble(0.0);
    }

    public static double getTargetY() {
        return table.getEntry("ty").getDouble(0.0);
    }

    public static double getTargetArea() {
        return table.getEntry("ta").getDouble(0.0);
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
