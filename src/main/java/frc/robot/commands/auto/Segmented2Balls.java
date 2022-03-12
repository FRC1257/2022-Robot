package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.Constants.Autonomous.INTAKE_ARM_LOWER_TIME;

public class Segmented2Balls extends SequentialCommandGroup {
    
    public Segmented2Balls(Drivetrain drivetrain, Conveyor conveyor, Shooter shooter, Intake intake, IntakeArm intakeArm) {
        addCommands(
            new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME),
            new ParallelCommandGroup(
                new IntakeIntakeCommand(intake),
                new DriveDistanceCommand(drivetrain, 1.0)
                ),
            new DriveDistanceCommand(drivetrain, -2.8),
            new ParallelCommandGroup(
                new IntakeIntakeCommand(intake), // in case ball isn't fully in conveyor
                new Dump(conveyor, shooter)
            )
        );
    }
}
