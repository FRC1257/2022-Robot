package frc.robot.commands.auto.trajectory.red;

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

public class RedAuto2Bot extends SequentialCommandGroup {
    
    public RedAuto2Bot(TrajectoryLoader loadedTrajectories, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME), 
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    loadedTrajectories.getCommand("RedCornerToStation"),
                    loadedTrajectories.getCommand("RedStationToHub")
                ),
                new IntakeIntakeCommand(intake).withTimeout(10.0)
            ),
            new Dump(conveyor, shooter)
        );
    }
}
