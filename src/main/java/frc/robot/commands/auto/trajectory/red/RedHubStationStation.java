package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.red.RedHubToStation;
import frc.robot.commands.auto.trajectory.red.RedStationToStation;

public class RedHubStationStation extends SequentialCommandGroup {
    
    public RedHubStationStation(Drivetrain drivetrain) {
        addCommands(new RedHubToStation(drivetrain), new RedStationToStation(drivetrain));
    }
}
