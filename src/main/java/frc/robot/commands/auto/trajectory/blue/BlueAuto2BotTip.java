package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;

import static frc.robot.Constants.IntakeArm.INTAKE_SETPOINT_BOT;

public class BlueAuto2BotTip extends SequentialCommandGroup {
    
    public BlueAuto2BotTip(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            // new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT),
            new IntakeArmLowerCommand(intakeArm).withTimeout(2.0), 
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new BlueCornerToWall2(drivetrain), new BlueWallToHub2(drivetrain)),
                new IntakeIntakeCommand(intake)
            ),
            new Dump(conveyor, shooter)
        );
    }
}
