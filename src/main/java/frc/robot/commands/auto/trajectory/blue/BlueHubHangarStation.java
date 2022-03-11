package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;

public class BlueHubHangarStation extends SequentialCommandGroup {
    
    public BlueHubHangarStation(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(new DumpAndLower(intakeArm, conveyor, shooter),
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new BlueHubToHangar(drivetrain), new BlueHangarToStation(drivetrain)), 
                new IntakeIntakeCommand(intake)
            ));
    }
}