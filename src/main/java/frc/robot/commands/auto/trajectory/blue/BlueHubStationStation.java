package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.blue.BlueHubToStation;
import frc.robot.commands.auto.trajectory.blue.BlueStationToStation;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;

public class BlueHubStationStation extends SequentialCommandGroup {
    
    public BlueHubStationStation(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake) {
        addCommands(new DumpAndLower(intakeArm, conveyor),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new BlueHubToStation(drivetrain), new BlueStationToStation(drivetrain)), 
                new IntakeIntakeCommand(intake)
            ));
    }
}
