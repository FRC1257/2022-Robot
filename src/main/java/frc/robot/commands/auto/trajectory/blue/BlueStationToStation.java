package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drivetrain.DriveTrajectoryCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.auto.trajectory.Trajectories;

public class BlueStationToStation extends SequentialCommandGroup {
    
    public BlueStationToStation(Drivetrain drivetrain) {
        Trajectory trajectory = Trajectories.loadTrajectoryFromFile("Pathweaver/output/BlueStationToStation.wpilib.json");
        
        
        addCommands(new DriveTrajectoryCommand(drivetrain, trajectory));
    }
}
