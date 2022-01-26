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
    }

    public static class Climber {
        public final static int climber_PRIMARY_ID = 0;
        public final static int climber_FOLLOWER_ID = 0;

        public static double[] CLIMBER_PID = new double[] {0.1, 0, 0.01};
        public static double CLIMBER_PID_TOLERANCE = 0.1;
        public static double CLIMBER_PID_MAX_OUTPUT = 0.7;


    }

    public static int NEO_CURRENT_LIMIT = 70;

    public static class Autonomous {
        
    }

    public static double PI = 3.14159265;
    public static double UPDATE_PERIOD = 0.010; // seconds

}
