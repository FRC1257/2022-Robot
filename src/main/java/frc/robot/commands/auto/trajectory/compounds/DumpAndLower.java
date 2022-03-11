package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.IntakeArm.INTAKE_SETPOINT_BOT;

public class DumpAndLower extends ParallelCommandGroup {
    
    public DumpAndLower(IntakeArm intakeArm, Conveyor conveyor, Shooter shooter) {
        addCommands(
            new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT), 
            new Dump(conveyor, shooter)
        );
    }
}
