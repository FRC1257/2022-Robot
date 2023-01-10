package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Notifier;
//import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.drivetrain.*;
import frc.robot.commands.ScoreCommand;
import frc.robot.commands.auto.Segmented2Balls;
import frc.robot.commands.auto.trajectory.blue.*;
import frc.robot.commands.auto.trajectory.compounds.DumpAndDrive;
import frc.robot.commands.auto.trajectory.red.*;
import frc.robot.commands.climber.ClimberManualCommand;
import frc.robot.commands.conveyor.ConveyorManualCommand;
import frc.robot.commands.intake.intake.IntakeEjectCommand;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake.IntakeNeutralCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmRaiseCommand;
import frc.robot.commands.shooter.ShooterBackCommand;
import frc.robot.commands.shooter.ShooterNeutralCommand;
import frc.robot.commands.shooter.ShooterShootCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmNeutralCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SnailSubsystem;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.util.Gyro;
import frc.robot.util.SnailController;

import java.util.ArrayList;
import java.util.List;

import static frc.robot.Constants.ElectricalLayout.CONTROLLER_DRIVER_ID;
import static frc.robot.Constants.ElectricalLayout.CONTROLLER_OPERATOR_ID;
import static frc.robot.Constants.UPDATE_PERIOD;
import static frc.robot.Constants.IntakeArm.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the Robot
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private SnailController driveController;
    private SnailController operatorController;

    private Intake intake;
    private IntakeArm intakeArm;
    private Conveyor conveyor;
    private Drivetrain drivetrain;
    private Climber climber;
    private Shooter shooter;
    
    private ArrayList<SnailSubsystem> subsystems;

    private Notifier updateNotifier;
    private int outputCounter;
    
    // put path commands here
    private Command pathDriveOffTarmac; 
    private Command segmented2Ball;
    private Command pathBlueTwoBot;
    private Command pathBlueTwoBotClose;
    private Command pathBlueTwoTop;
    private Command pathBlueThreeBot;
    private Command pathBlueFourBot;
    private Command pathRedTwoTop;
    private Command pathRedTwoBot;
    private Command pathRedThreeTop;
    private Command pathRedFourTop;
    private Command leaveTarmac;
    
    SendableChooser<Command> chooser = new SendableChooser<>();


    private final DriveSubsystem mdrivetrain = new DriveSubsystem(); //virtual drivetrain

    //xbox controller instantiation
    Joystick m_driverController =
    new Joystick(Constants.OIConstants.kDriverControllerPort);
    

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        
        driveController = new SnailController(CONTROLLER_DRIVER_ID);
        operatorController = new SnailController(CONTROLLER_OPERATOR_ID);
        
       
        // TODO not working because of the unhandled exception error, the DIO 31 is previously allocated or something
        //the GUI works, the extensions work, but the unhandled exception messes
        configureSubsystems();
        loadTrajectories();
        configureAutoChoosers();
        //configureButtonBindings();
        
        outputCounter = 0;

        SmartDashboard.putBoolean("Testing", false);

        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(UPDATE_PERIOD);


        //everything about this comment is physical robot

        //robo sim section

        configureButtonBindings();

        // Configure default commands
        // Set the default drive command to split-stick arcade drive
        mdrivetrain.setDefaultCommand(
            // A split-stick arcade command, with forward/backward controlled by the left
            // hand, and turning controlled by the right.
            // If you are using the keyboard as a joystick, it is recommended that you go
            // to the following link to read about editing the keyboard settings.
            // https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-simulation/simulation-gui.html#using-the-keyboard-as-a-joystick
            new RunCommand(
                () ->
                    mdrivetrain.arcadeDrive(
                        -m_driverController.getY(), -m_driverController.getX()),
                mdrivetrain));
    }


    public Command getAutonomousCommand() {
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint =
            new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(
                    Constants.DriveConstants.ksVolts,
                    Constants.DriveConstants.kvVoltSecondsPerMeter,
                    Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
                Constants.DriveConstants.kDriveKinematics,
                7);
    
        // Create config for trajectory
        TrajectoryConfig config =
            new TrajectoryConfig(
                    Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                    Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                // Add kinematics to ensure max speed is actually obeyed
                .setKinematics(Constants.DriveConstants.kDriveKinematics)
                // Apply the voltage constraint
                .addConstraint(autoVoltageConstraint);
    
        // An example trajectory to follow.  All units in meters.
        Trajectory exampleTrajectory =
            TrajectoryGenerator.generateTrajectory(
                // Start at (1, 2) facing the +X direction
                new Pose2d(1, 2, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(new Translation2d(2, 3), new Translation2d(3, 1)),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(4, 2, new Rotation2d(0)),
                // Pass config
                config);
    
        RamseteCommand ramseteCommand =
            new RamseteCommand(
                exampleTrajectory,
                mdrivetrain::getPose,
                new RamseteController(
                    Constants.AutoConstants.kRamseteB, Constants.AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(
                    Constants.DriveConstants.ksVolts,
                    Constants.DriveConstants.kvVoltSecondsPerMeter,
                    Constants.DriveConstants.kaVoltSecondsSquaredPerMeter),
                Constants.DriveConstants.kDriveKinematics,
                mdrivetrain::getWheelSpeeds,
                new PIDController(Constants.DriveConstants.kPDriveVel, 0, 0),
                new PIDController(Constants.DriveConstants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                mdrivetrain::tankDriveVolts,
                mdrivetrain);
    
        // Reset odometry to starting pose of trajectory.
        mdrivetrain.resetOdometry(exampleTrajectory.getInitialPose());
    
        // Run path following command, then stop at the end.
        return ramseteCommand.andThen(() -> mdrivetrain.tankDriveVolts(0, 0));
      }










    
    public DriveSubsystem getRobotDrive() {
        return mdrivetrain;
      }
      //returns what the driving subsystem is
      //is also used in the main robot file


    /**
     * Declare all of our subsystems and their default bindings
     */
    private void configureSubsystems() {
        // declare each of the subsystems here
        drivetrain = new Drivetrain();
        // drivetrain.setDefaultCommand(new ManualDriveCommand(drivetrain, driveController::getDriveForward, driveController::getDriveTurn));
        drivetrain.setDefaultCommand(new VelocityDriveCommand(drivetrain, driveController::getDriveForward, driveController::getDriveTurn,
            driveController.getButton(Button.kLeftBumper.value)::get, true));

        climber = new Climber();
        climber.setDefaultCommand(new ClimberManualCommand(climber, operatorController::getRightY));

        conveyor = new Conveyor();
        conveyor.setDefaultCommand(new ConveyorManualCommand(conveyor, operatorController::getLeftY));
        
        shooter = new Shooter();
        shooter.setDefaultCommand(new ShooterNeutralCommand(shooter));

        intake = new Intake();
        intake.setDefaultCommand(new IntakeNeutralCommand(intake));

        intakeArm = new IntakeArm();
        intakeArm.setDefaultCommand(new IntakeArmNeutralCommand(intakeArm));
        
        subsystems = new ArrayList<>();
        subsystems.add(drivetrain);
        subsystems.add(climber);
        subsystems.add(conveyor);
        subsystems.add(shooter);
        subsystems.add(intake);
        subsystems.add(intakeArm);
    }

    /**
     * Define button -> command mappings.
     */
    private void configureButtonBindings() {
        // Drivetrain bindings
        driveController.getButton(Button.kY.value).whenPressed(new ToggleReverseCommand(drivetrain));
        driveController.getButton(Button.kStart.value).whenPressed(new ToggleSlowModeCommand(drivetrain));
        driveController.getButton(Button.kA.value).whenPressed(new TurnAngleCommand(drivetrain, -90));
        driveController.getButton(Button.kB.value).whenPressed(new TurnAngleCommand(drivetrain, 90));
        driveController.getButton(Button.kX.value).whenPressed(new ResetDriveCommand(drivetrain));

        // Conveyor bindings
        operatorController.getTrigger(false).whileActiveOnce(new ShooterShootCommand(shooter)); // right trigger
        operatorController.getButton(Button.kRightBumper.value).whileActiveOnce(new ScoreCommand(shooter, conveyor));

        // Intake bindings
        operatorController.getButton(Button.kB.value).whileActiveOnce(new IntakeIntakeCommand(intake)); // ACTUALLY INTAKING
        operatorController.getButton(Button.kA.value).whileActiveOnce(new IntakeEjectCommand(intake)); // ACTUALLY EJECTING
        operatorController.getTrigger(true).whileActiveOnce(new IntakeIntakeCommand(intake)); // INTAKE using Left Trigger
        operatorController.getButton(Button.kLeftBumper.value).whileActiveOnce(new IntakeEjectCommand(intake)); // Eject using Left Bumper

        // Climber bindings
        // operatorController.getButton(Button.kStart.value).whileActiveOnce(new C  limberPIDCommand(climber, CLIMBER_SETPOINT_TOP));
        // operatorController.getButton(Button.kX.value).whileActiveOnce(new ClimberPIDCommand(climber, CLIMBER_SETPOINT_BOT));
        
        // Intake Arm bindings
        operatorController.getButton(Button.kX.value).whileActiveOnce(new IntakeArmLowerCommand(intakeArm));
        operatorController.getButton(Button.kY.value).whileActiveOnce(new IntakeArmRaiseCommand(intakeArm));
        operatorController.getDPad(SnailController.DPad.UP).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_TOP));
        operatorController.getDPad(SnailController.DPad.DOWN).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT));
        operatorController.getDPad(SnailController.DPad.LEFT).whileActiveOnce(new ShooterBackCommand(shooter));
    }

    public void loadTrajectories() {
        pathDriveOffTarmac = new DumpAndDrive(drivetrain, conveyor, shooter, intakeArm);
        segmented2Ball = new Segmented2Balls(drivetrain, conveyor, shooter, intake, intakeArm);
        pathBlueTwoBot = new BlueTwoBot(drivetrain, intakeArm, conveyor, intake, shooter);
        pathBlueTwoBotClose = new BlueTwoBotClose(drivetrain, intakeArm, conveyor, intake, shooter);
        pathBlueTwoTop = new BlueTwoTop(drivetrain, intakeArm, conveyor, intake, shooter);
        pathBlueThreeBot = new BlueThreeBot(drivetrain, intakeArm, conveyor, intake, shooter);
        pathBlueFourBot = new BlueFourBot(drivetrain, intakeArm, conveyor, intake, shooter);
        pathRedTwoBot = new RedTwoBot(drivetrain, intakeArm, conveyor, intake, shooter);
        pathRedTwoTop = new RedTwoTop(drivetrain, intakeArm, conveyor, intake, shooter);
        pathRedThreeTop = new RedThreeTop(drivetrain, intakeArm, conveyor, intake, shooter);
        pathRedFourTop = new RedFourTop(drivetrain, intakeArm, conveyor, intake, shooter);
        leaveTarmac = new DriveDistanceCommand(drivetrain, 1.75);
    }

    /**
     * Set up the choosers on shuffleboard for autonomous
     */
    public void configureAutoChoosers() {
        chooser.setDefaultOption("score 1 and leave", pathDriveOffTarmac);
        chooser.addOption("drive dist 1.75 m", leaveTarmac);
        chooser.addOption("2 ball auto", segmented2Ball);
        chooser.addOption("blue 2 wall", pathBlueTwoBot);
        chooser.addOption("blue 2 wall CLOSE", pathBlueTwoBotClose);
        chooser.addOption("blue 2 hangar", pathBlueTwoTop);
        chooser.addOption("blue three (by wall)", pathBlueThreeBot);
        chooser.addOption("blue four (by wall", pathBlueFourBot);
        chooser.addOption("red 2 wall", pathRedTwoTop);
        chooser.addOption("red 2 hangar", pathRedTwoBot);
        chooser.addOption("red three (by wall)", pathRedThreeTop);
        chooser.addOption("red four (by wall)", pathRedFourTop);
        SmartDashboard.putData(chooser);
    }

    /**
     * Do the logic to return the auto command to run
     */
    public Command getAutoCommand() {
        return chooser.getSelected() == null ? null : chooser.getSelected();

        // return pathDriveOffTarmac;
        // return new IntakeArmLowerCommand(intakeArm).withTimeout(1.75);
    }

    /**
     * Update all of the subsystems
     * This is run in a separate loop at a faster rate to:
     * a) update subsystems faster
     * b) prevent packet delay from driver station from delaying response from our robot
     */
    private void update() {
        for(SnailSubsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    public void displayShuffleboard() {
        if(outputCounter % 3 == 0) {
            subsystems.get(outputCounter / 3).displayShuffleboard();
        }

        outputCounter = (outputCounter + 1) % (subsystems.size() * 3);

        Gyro.getInstance().outputValues();
        SmartDashboard.putNumber("Left Joystick", operatorController.getLeftY());
    }


    public void tuningInit() {
        for(SnailSubsystem subsystem : subsystems) {
            subsystem.tuningInit();
        }
    }

    public void tuningPeriodic() {
        if(outputCounter % 3 == 0) {
            subsystems.get(outputCounter / 3).tuningPeriodic();
        }
    }
}
