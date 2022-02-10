package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.blue.BlueHubToWall;
import frc.robot.commands.auto.trajectory.blue.BlueWallToStation;

public class BlueHubWallStation extends SequentialCommandGroup {
    
    public BlueHubWallStation(Drivetrain drivetrain) {
        addCommands(new BlueHubToWall(drivetrain), new BlueWallToStation(drivetrain));
    }
}
