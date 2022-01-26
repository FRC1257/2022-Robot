package frc.robot.commands.rollerintake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class ConveyorEjectCommand extends CommandBase {

    private Conveyor conveyor;

    public ConveyorEjectCommand(Conveyor conveyor) {
        this.conveyor = conveyor;

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        conveyor.move();
    }

    @Override
    public void end(boolean interrupted) {
    
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}