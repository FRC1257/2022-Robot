package frc.robot.commands.auto.trajectory.red;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.auto.trajectory.compounds.Dump;
import frc.robot.commands.intake.intake.IntakeEjectCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;

import static frc.robot.Constants.IntakeArm.INTAKE_SETPOINT_BOT;

public class RedAuto2Bot extends SequentialCommandGroup {
    
    public RedAuto2Bot(Drivetrain drivetrain, IntakeArm intakeArm, Conveyor conveyor, Intake intake, Shooter shooter) {
        addCommands(
            new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT), 
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(new RedCornerToStation(drivetrain), new RedStationToHub(drivetrain)),
                new IntakeEjectCommand(intake).withTimeout(10.0)
            ),
            new Dump(conveyor, shooter)
        );
    }
}
