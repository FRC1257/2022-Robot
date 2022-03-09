package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;

import frc.robot.commands.auto.trajectory.red.RedCornerToStation;
import frc.robot.commands.auto.trajectory.red.RedStationToHub;

public class RedAuto2Bot extends SequentialCommandGroup {
    
    public RedAuto2Bot(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new RedHubToHangar(drivetrain), new RedHangarToStation(drivetrain)),
                new IntakeIntakeCommand(intake)
            )
        );
    }
}
