package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.red.RedHubToWall;
import frc.robot.commands.auto.trajectory.red.RedWallToStation;

public class RedHubWallStation extends SequentialCommandGroup {
    
    public RedHubWallStation(Drivetrain drivetrain) {
        addCommands(new RedHubToWall(drivetrain), new RedWallToStation(drivetrain));
    }
}
