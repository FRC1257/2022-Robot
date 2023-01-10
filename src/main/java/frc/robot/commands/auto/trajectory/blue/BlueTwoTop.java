package frc.robot.commands.auto.trajectory.blue;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.Delay;
import frc.robot.commands.auto.trajectory.compounds.Dump;

import frc.robot.commands.intake.intake.IntakeIntakeCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmLowerCommand;


import static frc.robot.Constants.Autonomous.INTAKE_ARM_LOWER_TIME;

public class BlueTwoTop extends SequentialCommandGroup {
    
    public BlueTwoTop(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new Delay(4.0), 
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new BlueCornerToStation(drivetrain), 
                    new BlueStationToHub(drivetrain)
                ),
                new IntakeArmLowerCommand(intakeArm).withTimeout(INTAKE_ARM_LOWER_TIME),
                new IntakeIntakeCommand(intake).withTimeout(10.0)         
            ),
            new Dump(conveyor, shooter),
            new LeaveHubHangar(drivetrain)
        );
    }
}
