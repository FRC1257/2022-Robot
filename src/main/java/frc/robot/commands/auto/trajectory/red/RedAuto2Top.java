package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeEjectCommand;

public class RedAuto2Top extends SequentialCommandGroup {
    
    public RedAuto2Top(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new DumpAndLower(intakeArm, conveyor, shooter),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new RedCornerToWall(drivetrain), new RedWallToHub(drivetrain)),
                new IntakeEjectCommand(intake).withTimeout(10.0)
            ),
            new DumpAndLower(intakeArm, conveyor, shooter)
        );
    }
}
