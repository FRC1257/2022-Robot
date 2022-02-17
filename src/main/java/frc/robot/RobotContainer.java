package frc.robot;
import frc.robot.commands.intake.intake.IntakeEjectCommand;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake.IntakeNeutralCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmManualCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;

import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ConveyorLowerCommand;
import frc.robot.commands.ConveyorNeutralCommand;
import frc.robot.commands.ConveyorRaiseCommand;
import frc.robot.commands.ConveyorShootCommand;
import frc.robot.commands.drivetrain.*;
import frc.robot.commands.auto.trajectory.blue.*;
import frc.robot.commands.auto.trajectory.red.*;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.SnailSubsystem;
import frc.robot.util.SnailController;

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

    private ArrayList<SnailSubsystem> subsystems;

    private Notifier updateNotifier;
    private int outputCounter;

    // put path commands here
    
    private final Command pathDriveOffTarmac; 
    private final Command pathBlueHubHangarStation; 
    private final Command pathBlueHubStationStation;
    private final Command pathBlueHubWallStation;
    private final Command pathRedHubHangarStation;
    private final Command pathRedHubStationStation;
    private final Command pathRedHubWallStation;
    private final Command pathTest;
    
    SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveController = new SnailController(CONTROLLER_DRIVER_ID);
        operatorController = new SnailController(CONTROLLER_OPERATOR_ID);

        configureSubsystems();
        configureAutoChoosers();
        configureButtonBindings();
        
        outputCounter = 0;

        SmartDashboard.putBoolean("Testing", false);

        updateNotifier = new Notifier(this::update);
        updateNotifier.startPeriodic(UPDATE_PERIOD);

        pathDriveOffTarmac = new DriveDistanceCommand(drivetrain, 2.0);
        pathBlueHubHangarStation = new BlueHubHangarStation(drivetrain);
        pathBlueHubStationStation = new BlueHubStationStation(drivetrain);
        pathBlueHubWallStation = new BlueHubWallStation(drivetrain);
        pathRedHubHangarStation = new RedHubHangarStation(drivetrain);
        pathRedHubStationStation = new RedHubStationStation(drivetrain);
        pathRedHubWallStation = new RedHubWallStation(drivetrain);
        pathTest = new BlueHubToStation(drivetrain);
    }

    /**
     * Declare all of our subsystems and their default bindings
     */
    private void configureSubsystems() {
        // declare each of the subsystems here
        drivetrain = new Drivetrain();
        drivetrain.setDefaultCommand(new VelocityDriveCommand(drivetrain, driveController::getDriveForward, driveController::getDriveTurn));

        conveyor = new Conveyor();
        conveyor.setDefaultCommand(new ConveyorNeutralCommand(conveyor));
        
        intake = new Intake();
        intake.setDefaultCommand(new IntakeNeutralCommand(intake));

        intakeArm = new IntakeArm();
        intakeArm.setDefaultCommand(new IntakeArmManualCommand(intakeArm, operatorController::getLeftY));
        
        subsystems = new ArrayList<>();
        subsystems.add(drivetrain);
        subsystems.add(conveyor);
        subsystems.add(intake);
        subsystems.add(intakeArm);
    }

    /**
     * Define button -> command mappings.
     */
    private void configureButtonBindings() {
        operatorController.getButton(Button.kA.value).whileActiveOnce(new ConveyorShootCommand(conveyor));
        operatorController.getButton(Button.kX.value).whileActiveOnce(new ConveyorLowerCommand(conveyor));
        operatorController.getButton(Button.kY.value).whileActiveOnce(new ConveyorRaiseCommand(conveyor));
        operatorController.getButton(Button.kB.value).whileActiveOnce(new IntakeEjectCommand(intake));
        operatorController.getButton(Button.kA.value).whileActiveOnce(new IntakeIntakeCommand(intake));
        
        operatorController.getDPad(SnailController.DPad.UP).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_TOP));
        operatorController.getDPad(SnailController.DPad.DOWN).whileActiveOnce(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT));
    }

    /**
     * Set up the choosers on shuffleboard for autonomous
     */
    public void configureAutoChoosers() {
        
        chooser.setDefaultOption("get off tarmac", pathDriveOffTarmac);
        chooser.addOption("blue hub hangar station", pathBlueHubHangarStation);
        chooser.addOption("blue hub station station", pathBlueHubStationStation);
        chooser.addOption("blue hub wall station", pathBlueHubWallStation);
        chooser.addOption("red hub hangar station", pathRedHubHangarStation);
        chooser.addOption("red hub station station", pathRedHubStationStation);
        chooser.addOption("red hub wall station", pathRedHubWallStation);
        chooser.addOption("test path", pathTest);
        SmartDashboard.putData(chooser);
    }

    /**
     * Do the logic to return the auto command to run
     */
    public Command getAutoCommand() {
        return chooser.getSelected();
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
