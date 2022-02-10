package frc.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SnailSubsystem;
import frc.robot.util.SnailController;

import java.util.ArrayList;

import static frc.robot.Constants.ElectricalLayout.CONTROLLER_DRIVER_ID;
import static frc.robot.Constants.ElectricalLayout.CONTROLLER_OPERATOR_ID;
import static frc.robot.Constants.UPDATE_PERIOD;

import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.drivetrain.*;
import frc.robot.commands.auto.trajectory.blue.*;
import frc.robot.commands.auto.trajectory.red.*;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the Robot
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private SnailController driveController;
    private SnailController operatorController;
    
    private ArrayList<SnailSubsystem> subsystems;

    private Drivetrain drivetrain;

    private Notifier updateNotifier;
    private int outputCounter;

    // put path commands here
    
    private final Command path1; // get robot off tarmac
    private final Command path2; // put the actual paths
    private final Command path3;
    private final Command path4;
    private final Command path5;
    private final Command path6;
    private final Command path7;
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

        path1 = new DriveDistanceCommand(drivetrain, 2.0);
        path2 = new BlueHubHangarStation(drivetrain);
        path3 = new BlueHubStationStation(drivetrain);
        path4 = new BlueHubWallStation(drivetrain);
        path5 = new RedHubHangarStation(drivetrain);
        path6 = new RedHubStationStation(drivetrain);
        path7 = new RedHubWallStation(drivetrain);
        pathTest = new BlueHubToStation(drivetrain);
    }

    /**
     * Declare all of our subsystems and their default bindings
     */
    private void configureSubsystems() {
        // declare each of the subsystems here

        subsystems = new ArrayList<>();
        // add each of the subsystems to the arraylist here
    }

    /**
     * Define button -> command mappings.
     */
    private void configureButtonBindings() {
        
    }

    /**
     * Set up the choosers on shuffleboard for autonomous
     */
    public void configureAutoChoosers() {
        
        chooser.setDefaultOption("get off tarmac", path1);
        chooser.addOption("blue hub hangar station", path2);
        chooser.addOption("blue hub station station", path3);
        chooser.addOption("blue hub wall station", path4);
        chooser.addOption("red hub hangar station", path5);
        chooser.addOption("red hub station station", path6);
        chooser.addOption("red hub wall station", path7);
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
