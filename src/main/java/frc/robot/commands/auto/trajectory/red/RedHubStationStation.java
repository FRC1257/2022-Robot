package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;

import frc.robot.commands.auto.trajectory.red.RedHubToStation;
import frc.robot.commands.auto.trajectory.red.RedStationToStation;

public class RedHubStationStation extends SequentialCommandGroup {
    
    public RedHubStationStation(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake) {
        addCommands(
            new DumpAndLower(intakeArm, conveyor),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new RedHubToStation(drivetrain), new RedStationToStation(drivetrain)),
                new IntakeIntakeCommand(intake)
            )
        );
    }
}
