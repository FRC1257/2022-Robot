package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import frc.robot.commands.auto.trajectory.red.RedHubToHangar;
import frc.robot.commands.auto.trajectory.red.RedHangarToStation;

public class RedHubHangarStation extends SequentialCommandGroup {
    
    public RedHubHangarStation(Drivetrain drivetrain) {
        addCommands(new RedHubToHangar(drivetrain), new RedHangarToStation(drivetrain));
    }
}
