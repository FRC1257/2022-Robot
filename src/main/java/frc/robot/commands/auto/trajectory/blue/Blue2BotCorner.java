package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.auto.trajectory.compounds.DumpAndLower;
import frc.robot.commands.intake.intake.IntakeEjectCommand;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;

import static frc.robot.Constants.Autonomous.INTAKE_ARM_LOWER_TIME;

public class Blue2BotCorner extends SequentialCommandGroup {
    
    public Blue2BotCorner(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            // Intake Arm Down
            new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME), 
            new ParallelDeadlineGroup(
                // Drive
                new SequentialCommandGroup(new BlueCornerToWall(drivetrain), new BlueWallToHub(drivetrain)),
                // intake
                new IntakeEjectCommand(intake).withTimeout(10.0)
            ),

            // Score
            new Dump(conveyor, shooter)
        );
    }
}
