package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.intake.IntakeArm;
import frc.robot.commands.conveyor.ConveyorShootCommand;
import frc.robot.commands.intake.intake_arm.IntakeArmPIDCommand;
import frc.robot.commands.shooter.ShooterShootCommand;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.IntakeArm.INTAKE_SETPOINT_BOT;
import static frc.robot.Constants.Autonomous.CONVEYOR_DUMP_TIME;

public class DumpAndLower extends ParallelCommandGroup {
    
    public DumpAndLower(IntakeArm intakeArm, Conveyor conveyor, Shooter shooter) {
        addCommands(new IntakeArmPIDCommand(intakeArm, INTAKE_SETPOINT_BOT), 
            new ParallelCommandGroup(
                new ConveyorShootCommand(conveyor).withTimeout(CONVEYOR_DUMP_TIME),
                new ShooterShootCommand(shooter).withTimeout(CONVEYOR_DUMP_TIME)
            )
        );
    }
}
