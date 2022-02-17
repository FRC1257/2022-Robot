package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class ConveyorLowerCommand extends CommandBase {

    private Conveyor conveyor;

    public ConveyorLowerCommand(Conveyor conveyor) {
        this.conveyor = conveyor;

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        conveyor.lower();
    }

    @Override
    public void end(boolean interrupted) {
    
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}