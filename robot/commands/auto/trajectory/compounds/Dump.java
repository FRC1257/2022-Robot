package frc.robot.commands.auto.trajectory.compounds;

import frc.robot.commands.conveyor.ConveyorShootCommand;
import frc.robot.commands.shooter.ShooterShootCommand;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Conveyor;

import static frc.robot.Constants.Autonomous.CONVEYOR_DUMP_TIME;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class Dump extends ParallelCommandGroup {
    
    public Dump(Conveyor conveyor, Shooter shooter) {
        addCommands(
            new ConveyorShootCommand(conveyor).withTimeout(CONVEYOR_DUMP_TIME),
            new ShooterShootCommand(shooter).withTimeout(CONVEYOR_DUMP_TIME)
        );
    }
}
