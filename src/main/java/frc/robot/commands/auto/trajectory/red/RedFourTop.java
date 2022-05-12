
package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.blue.BlueCornerToWall;
import frc.robot.commands.auto.trajectory.blue.BlueStationToStat2nd;
import frc.robot.commands.auto.trajectory.blue.BlueTermToHub;
import frc.robot.commands.auto.trajectory.blue.BlueWallToHub;
import frc.robot.commands.auto.trajectory.blue.ThreeBlueHubToSide;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.intake.intake.IntakeEjectCommand;
import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;

import static frc.robot.Constants.Autonomous.INTAKE_ARM_LOWER_TIME;

public class RedFourTop extends SequentialCommandGroup {
    
    public RedFourTop(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new BlueCornerToWall(drivetrain),
                    new BlueWallToHub(drivetrain),
                    new Dump(conveyor, shooter),
                    new ThreeBlueHubToSide(drivetrain),
                    new BlueStationToStat2nd(drivetrain), 
                    new BlueTermToHub(drivetrain)
                ),
                new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME),
                new IntakeIntakeCommand(intake).withTimeout(12.0)
            ),
            new Dump(conveyor, shooter)
        );
    }
}
