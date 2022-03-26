package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;
import frc.robot.commands.auto.trajectory.TrajectoryLoader;

import static frc.robot.Constants.Autonomous.INTAKE_ARM_LOWER_TIME;

public class BlueTwoTop extends SequentialCommandGroup {
    
    public BlueTwoTop(TrajectoryLoader loadedTrajectories, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME), 
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    loadedTrajectories.getCommand("BlueCornerToStation"),
                    loadedTrajectories.getCommand("BlueStationToHub")
                ),
                new IntakeIntakeCommand(intake)
            ),

            new Dump(conveyor, shooter)
        );
    }
}
