package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drivetrain.DriveTrajectoryCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.auto.trajectory.Trajectories;

public class BlueStationToHub extends SequentialCommandGroup {
    
    public BlueStationToHub(Drivetrain drivetrain) {
        Trajectory trajectory = Trajectories.loadTrajectoryFromFile("paths/output/BlueStationToHub.wpilib.json");
        
        addCommands(new DriveTrajectoryCommand(drivetrain, trajectory));
    }
}
