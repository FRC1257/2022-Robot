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

        public final static int CONVEYOR_BOTTOM_ID = 14;

        public final static int CLIMBER_PRIMARY_ID = 11;
        public final static int CLIMBER_LIMIT_SWITCH_PORT_ID = 0;

        public final static int DRIVE_FRONT_LEFT = 8;
        public final static int DRIVE_FRONT_RIGHT = 10;
        public final static int DRIVE_BACK_LEFT = 7;
        public final static int DRIVE_BACK_RIGHT = 9;

        public final static int INTAKE_LEFT_MOTOR_ID = 13;
        // public final static int INTAKE_RIGHT_MOTOR_ID = 1;
        
        public final static int INTAKE_ARM_ID = 12;
        public final static int INTAKE_BUMP_SWITCH_ID = 0;

        public final static int SHOOTER_ID = 15;
    }

    public static class Autonomous {
        public static double CONVEYOR_DUMP_TIME = 2.0;
        public static double INTAKE_ARM_LOWER_TIME = 1.7;
    }

    public static class Intake {
        public static double INTAKE_EJECT_SPEED = 0.85;  
        public static double INTAKE_INTAKE_SPEED = -0.85;  
        public static double INTAKE_NEUTRAL_SPEED = 0.0;  
    }

    public static class IntakeArm {
        public static double[] INTAKE_ARM_PID = new double[] {0.1, 0, 0.01, 0.01};
        public static double INTAKE_ARM_PID_TOLERANCE = 0.1;
        public static double INTAKE_ARM_PID_MAX_OUTPUT = 0.3;

        public static double INTAKE_ARM_PROFILE_MAX_VEL = 7.0;
        public static double INTAKE_ARM_PROFILE_MAX_ACC = 7.0;

        public static int INTAKE_ARM_PID_SLOT_VEL = 0; //change later
        public static int INTAKE_ARM_PID_SLOT_ACC = 0; //change later

        public static double INTAKE_SETPOINT_TOP = 50.0; // in encoder revs
        public static double INTAKE_SETPOINT_BOT = -8273.0; // in encoder revs

        public static final double INTAKE_ARM_RAISE_SPEED = 0.4;
        public static final double INTAKE_ARM_NEUTRAL_SPEED = 0.0;
        public static final double INTAKE_ARM_LOWER_SPEED = -0.3;
        public static final double INTAKE_ARM_GEAR_FACTOR = 162; // 162:1
    }

    public static class Conveyor {
        public static double CONVEYOR_BOTTOM_SHOOT_SPEED = 0.8;
        public static double CONVEYOR_BOTTOM_RAISE_SPEED = 0.5;
        public static double CONVEYOR_BOTTOM_NEUTRAL_SPEED = 0.0;
        public static double CONVEYOR_BOTTOM_LOWER_SPEED = -0.2;
    }

    public static class Shooter {
        public static double SHOOTER_SHOOT_SPEED = 0.8;
        public static double SHOOTER_NEUTRAL_SPEED = 0.0;
        public static double SHOOTER_BACKING_SPEED = -0.4;
    }

    public static class Climber {
        public final static double CLIMBER_GEAR_FACTOR = 43.39 * (2 * Math.PI * 0.095); // gear ratio * gear circumf in meters

        public static double[] CLIMBER_PID = new double[] {0.1, 0, 0.01, 0.01};
        public static double CLIMBER_PID_TOLERANCE = 0.1;
        public static double CLIMBER_PID_MAX_OUTPUT = 0.7;

        public static double CLIMBER_PROFILE_MAX_VEL = 7.0;
        public static double CLIMBER_PROFILE_MAX_ACC = 7.0;

        public static int CLIMBER_PID_SLOT_VEL = 0; //change later
        public static int CLIMBER_PID_SLOT_ACC = 0; //change later

        public static double CLIMBER_SETPOINT_TOP = 7.0;
        public static double CLIMBER_SETPOINT_BOT = 1.0;
    }

    public static class Drivetrain {
        // drivetrain constants
        public static double DRIVE_TRACK_WIDTH_M = 0.66; // m
        public static double DRIVE_WHEEL_DIAM_M = 0.1524; // m
        public static double DRIVE_GEARBOX_REDUCTION = 10.71;

        // driving modifiers
        public static double DRIVE_SLOW_TURN_MULT = 0.45;

        // closed loop driving
        public static double DRIVE_CLOSED_MAX_VEL = 4.0; // m/s
        public static double DRIVE_CLOSED_MAX_ROT_TELEOP = 360.00; //
        public static double DRIVE_CLOSED_MAX_ROT_AUTO = 100.0; // deg/s
        public static double DRIVE_CLOSED_MAX_ACC = 1.5; // m/s^2

        // trajectory following
        public static double DRIVE_TRAJ_MAX_VEL = 1.0; // m/s
        public static double DRIVE_TRAJ_MAX_ACC = 1.25; // m/s^2
        public static double DRIVE_TRAJ_RAMSETE_B = 2.0;
        public static double DRIVE_TRAJ_RAMSETE_ZETA = 0.7;

        // linear position PID
        public static double[] DRIVE_DIST_PID = {3.50, 0.0, 0.0};
        public static double DRIVE_DIST_ANGLE_P = 0.1;
        public static double DRIVE_DIST_TOLERANCE = 0.01;
        public static double DRIVE_DIST_MAX_OUTPUT = 0.6;

        // angular position PID
        public static double[] DRIVE_ANGLE_PID = {0.1, 0.0, 0.01};
        public static double DRIVE_ANGLE_TOLERANCE = 0.075;
        public static double DRIVE_ANGLE_MAX_OUTPUT = 0.5;

        // velocity PID (for closed loop, profiling, and trajectory)
        public static int DRIVE_VEL_SLOT = 0;
        public static double DRIVE_VEL_LEFT_P = 0.25;
        public static double DRIVE_VEL_LEFT_F = 0.25;
        public static double DRIVE_VEL_RIGHT_P = 0.25;
        public static double DRIVE_VEL_RIGHT_F = 0.25;

        // profiling position PID (for further refinement of tracking)
        public static double DRIVE_PROFILE_LEFT_P = 0.1;
        public static double DRIVE_PROFILE_RIGHT_P = 0.1;
    }

    public static double PI = 3.14159265;
    public static double UPDATE_PERIOD = 0.010; // seconds
    public final static int NEO_550_CURRENT_LIMIT = 25; // amps
    public final static int NEO_CURRENT_LIMIT = 80; // amps
}
