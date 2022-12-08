package frc.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
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
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SnailSubsystem;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.util.Gyro;
import frc.robot.util.SnailController;
import frc.robot.util.SnailVision;

import java.util.ArrayList;

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

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveController = new SnailController(CONTROLLER_DRIVER_ID);
        operatorController = new SnailController(CONTROLLER_OPERATOR_ID);

        configureSubsystems();
        loadTrajectories();
        configureAutoChoosers();
        configureButtonBindings();
        
        outputCounter = 0;

        SmartDashboard.putBoolean("Testing", false);

        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(UPDATE_PERIOD);
    }

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

        SnailVision.init();
        
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
        // operatorController.getTrigger(false).whileActiveOnce(new ShooterShootCommand(shooter)); // right trigger
        // operatorController.getButton(Button.kRightBumper.value).whileActiveOnce(new ScoreCommand(shooter, conveyor));

        // Intake bindings
        // operatorController.getButton(Button.kB.value).whileActiveOnce(new IntakeIntakeCommand(intake)); // ACTUALLY INTAKING
        // operatorController.getButton(Button.kA.value).whileActiveOnce(new IntakeEjectCommand(intake)); // ACTUALLY EJECTING
        // operatorController.getTrigger(true).whileActiveOnce(new IntakeIntakeCommand(intake)); // INTAKE using Left Trigger
        // operatorController.getButton(Button.kLeftBumper.value).whileActiveOnce(new IntakeEjectCommand(intake)); // Eject using Left Bumper

        // Climber bindings for PID
        // operatorController.getButton(Button.kStart.value).whileActiveOnce(new ClimberPIDCommand(climber, CLIMBER_SETPOINT_TOP));
        // operatorController.getButton(Button.kX.value).whileActiveOnce(new ClimberPIDCommand(climber, CLIMBER_SETPOINT_BOT));
        
        // Intake Arm bindings
        // operatorController.getButton(Button.kX.value).whileActiveOnce(new IntakeArmLowerCommand(intakeArm));
        // operatorController.getButton(Button.kY.value).whileActiveOnce(new IntakeArmRaiseCommand(intakeArm));
        // operatorController.getDPad(SnailController.DPad.UP).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_TOP));
        // operatorController.getDPad(SnailController.DPad.DOWN).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT));
        // operatorController.getDPad(SnailController.DPad.LEFT).whileActiveOnce(new ShooterBackCommand(shooter));
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

        SnailVision.setConstantTuning();
    }

    public void tuningPeriodic() {
        if(outputCounter % 3 == 0) {
            subsystems.get(outputCounter / 3).tuningPeriodic();
        }

        if (outputCounter % 12 == 0) {
            SnailVision.getConstantTuning();
        }
    }
}
