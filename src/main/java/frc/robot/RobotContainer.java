package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.drivetrain.*;
import frc.robot.commands.ScoreCommand;
import frc.robot.commands.auto.trajectory.blue.*;
import frc.robot.commands.auto.trajectory.compounds.DumpAndDrive;
import frc.robot.commands.auto.trajectory.red.*;
import frc.robot.commands.climber.ClimberManualCommand;
import frc.robot.commands.climber.ClimberPIDCommand;
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
import frc.robot.util.SnailController;

import java.util.ArrayList;

import static frc.robot.Constants.ElectricalLayout.CONTROLLER_DRIVER_ID;
import static frc.robot.Constants.ElectricalLayout.CONTROLLER_OPERATOR_ID;
import static frc.robot.Constants.UPDATE_PERIOD;
import static frc.robot.Constants.IntakeArm.*;
import static frc.robot.Constants.Climber.*;

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
    private final Command pathDriveOffTarmac; 
    // private final Command pathBlueHubHangarStation; 
    // private final Command pathBlueHubStationStation;
    // private final Command pathBlueHubWallStation;
    // private final Command pathRedHubHangarStation;
    // private final Command pathRedHubStationStation;
    // private final Command pathRedHubWallStation;
    // private final Command pathTest;
    
    // SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveController = new SnailController(CONTROLLER_DRIVER_ID);
        operatorController = new SnailController(CONTROLLER_OPERATOR_ID);

        configureSubsystems();
        // configureAutoChoosers();
        configureButtonBindings();
        
        outputCounter = 0;

        SmartDashboard.putBoolean("Testing", false);

        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(UPDATE_PERIOD);

        pathDriveOffTarmac = new DumpAndDrive(drivetrain, conveyor, shooter);
        // pathBlueHubHangarStation = new BlueHubHangarStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathBlueHubStationStation = new BlueHubStationStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathBlueHubWallStation = new BlueHubWallStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathRedHubHangarStation = new RedHubHangarStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathRedHubStationStation = new RedHubStationStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathRedHubWallStation = new RedHubWallStation(drivetrain, intakeArm, conveyor, intake, shooter);
        // pathTest = new BlueHubToStation(drivetrain);
    }

    /**
     * Declare all of our subsystems and their default bindings
     */
    private void configureSubsystems() {
        // declare each of the subsystems here
        drivetrain = new Drivetrain();
        drivetrain.setDefaultCommand(new ManualDriveCommand(drivetrain, driveController::getDriveForward, driveController::getDriveTurn));
        // drivetrain.setDefaultCommand(new VelocityDriveCommand(drivetrain, driveController::getDriveForward, driveController::getDriveTurn));
        
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
        driveController.getButton(Button.kA.value).whenPressed(new TurnAngleCommand(drivetrain, -90));
        driveController.getButton(Button.kB.value).whenPressed(new TurnAngleCommand(drivetrain, 90));

        // driveController.getButton(Button.kX.value).whenPressed(new DriveDistanceCommand(drivetrain, 2));

        // Conveyor bindings
        operatorController.getTrigger(false).whileActiveOnce(new ShooterShootCommand(shooter)); // right trigger
        operatorController.getButton(Button.kRightBumper.value).whileActiveOnce(new ScoreCommand(shooter, conveyor));

        // Intake bindings
        operatorController.getButton(Button.kB.value).whileActiveOnce(new IntakeEjectCommand(intake));
        operatorController.getButton(Button.kA.value).whileActiveOnce(new IntakeIntakeCommand(intake));

        // Climber bindings
        operatorController.getButton(Button.kStart.value).whileActiveOnce(new ClimberPIDCommand(climber, CLIMBER_SETPOINT_TOP));
        // operatorController.getButton(Button.kX.value).whileActiveOnce(new ClimberPIDCommand(climber, CLIMBER_SETPOINT_BOT));
        
        // Intake Arm bindings
        operatorController.getButton(Button.kX.value).whileActiveOnce(new IntakeArmLowerCommand(intakeArm));
        operatorController.getButton(Button.kY.value).whileActiveOnce(new IntakeArmRaiseCommand(intakeArm));
        operatorController.getDPad(SnailController.DPad.UP).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_TOP));
        // operatorController.getDPad(SnailController.DPad.DOWN).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT));
        operatorController.getDPad(SnailController.DPad.DOWN).whileActiveOnce(new ShooterBackCommand(shooter));
    }

    /**
     * Set up the choosers on shuffleboard for autonomous
     */
    // public void configureAutoChoosers() {
    //     chooser.setDefaultOption("get off tarmac", pathDriveOffTarmac);
    //     chooser.addOption("blue hub hangar station", pathBlueHubHangarStation);
    //     chooser.addOption("blue hub station station", pathBlueHubStationStation);
    //     chooser.addOption("blue hub wall station", pathBlueHubWallStation);
    //     chooser.addOption("red hub hangar station", pathRedHubHangarStation);
    //     chooser.addOption("red hub station station", pathRedHubStationStation);
    //     chooser.addOption("red hub wall station", pathRedHubWallStation);
    //     chooser.addOption("test path", pathTest);
    //     SmartDashboard.putData(chooser);
    // }

    /**
     * Do the logic to return the auto command to run
     */
    public Command getAutoCommand() {
        // return chooser.getSelected() == null ? null : chooser.getSelected();

        return pathDriveOffTarmac;

        // return new DriveDistanceCommand(drivetrain, 2.0);
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

        SmartDashboard.putNumber("Climb joystick", operatorController.getRightY());
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
