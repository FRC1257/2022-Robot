package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.conveyor.ConveyorShootCommand;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Conveyor;

import static frc.robot.Constants.Autonomous.CONVEYOR_DUMP_TIME;

public class DumpAndDrive extends SequentialCommandGroup {
    
    public DumpAndDrive(Drivetrain drivetrain, Conveyor conveyor) {
        addCommands(new ConveyorShootCommand(conveyor).withTimeout(CONVEYOR_DUMP_TIME),
            new DriveDistanceCommand(drivetrain, 2)); // in meters
    }
}
