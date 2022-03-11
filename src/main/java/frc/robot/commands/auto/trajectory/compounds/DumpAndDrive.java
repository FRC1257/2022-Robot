package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Conveyor;

public class DumpAndDrive extends SequentialCommandGroup {
    
    public DumpAndDrive(Drivetrain drivetrain, Conveyor conveyor, Shooter shooter) {
        addCommands(
            new Dump(conveyor, shooter),
            new DriveDistanceCommand(drivetrain, 2.25)); // in meters

    }
}
