package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.subsystems.Conveyor;

public class DumpAndDrive extends SequentialCommandGroup {
    
    public DumpAndDrive(Drivetrain drivetrain, Conveyor conveyor, Shooter shooter, IntakeArm intakeArm) {
        addCommands(
            new ParallelCommandGroup(
                new Dump(conveyor, shooter),
                new IntakeArmLowerCommand(intakeArm).withTimeout(1.5)
            ),
            new DriveDistanceCommand(drivetrain, 2.25)); // in meters
    }
}
