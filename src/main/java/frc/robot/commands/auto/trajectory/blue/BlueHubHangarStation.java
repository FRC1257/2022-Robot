package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.blue.BlueHubToHangar;
import frc.robot.commands.auto.trajectory.blue.BlueHangarToStation;

public class BlueHubHangarStation extends SequentialCommandGroup {
    
    public BlueHubHangarStation(Drivetrain drivetrain) {
        addCommands(new BlueHubToHangar(drivetrain), new BlueHangarToStation(drivetrain));
    }
}
