package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be
 * declared globally (i.e. public static). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 * 
 * Each subsystem should have its own static inner class to hold its constants
 */
public final class Constants {

    public static class ElectricalLayout {
        public final static int CONTROLLER_DRIVER_ID = 0;
        public final static int CONTROLLER_OPERATOR_ID = 1;
        public final static int INTAKE_LEFT_MOTOR_ID = 2;
        public static final int INTAKE_RIGHT_MOTOR_ID = 1;
        public static final int INTAKE_ARM_ID = 1;
    }

    public static class Autonomous {
        
    }

    public static class Intake {
      public static double INTAKE_EJECT_SPEED = 1.0;  
      public static double INTAKE_INTAKE_SPEED = 1.0;  
      public static double INTAKE_NEUTRAL_SPEED = 1.0;  
      public static double INTAKE_SERVO_RELEASE_SETPOINT = 1.0;
      
    }

    public static class IntakeArm {
      public static double[] INTAKE_ARM_PID = new double[] {0.1, 0, 0.01, 0.01};
      public static double INTAKE_ARM_PID_TOLERANCE = 0.1;
      public static double INTAKE_ARM_PID_MAX_OUTPUT = 0.7;

      public static double INTAKE_ARM_PROFILE_MAX_VEL = 7.0;
      public static double INTAKE_ARM_PROFILE_MAX_ACC = 7.0;

      public static int INTAKE_ARM_PID_SLOT_VEL = 0; //change later
      public static int INTAKE_ARM_PID_SLOT_ACC = 0; //change later

      public static double INTAKE_SETPOINT_TOP = 7.0;
      public static double INTAKE_SETPOINT_BOT = 1.0;

      public static final double INTAKE_GEAR_FACTOR = 0; // ask build
    }

    public static double PI = 3.14159265;
    public static double UPDATE_PERIOD = 0.010; // seconds
    public final static int NEO_550_CURRENT_LIMIT = 25;
    public final static int NEO_CURRENT_LIMIT = 80;
}
