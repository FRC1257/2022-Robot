package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.blue.BlueHubToStation;
import frc.robot.commands.auto.trajectory.blue.BlueStationToStation;

public class BlueHubStationStation extends SequentialCommandGroup {
    
    public BlueHubStationStation(Drivetrain drivetrain) {
        addCommands(new BlueHubToStation(drivetrain), new BlueStationToStation(drivetrain));
    }
}
