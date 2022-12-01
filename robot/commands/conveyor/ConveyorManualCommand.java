package frc.robot.commands.conveyor;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.util.SnailController;

public class ConveyorManualCommand extends CommandBase {

    private Conveyor conveyor;
    private DoubleSupplier speedSupplier;

    public ConveyorManualCommand(Conveyor conveyor, DoubleSupplier speedSupplier) {
        this.conveyor = conveyor;
        this.speedSupplier = speedSupplier;

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        conveyor.setSpeed(SnailController.applyDeadband(speedSupplier.getAsDouble()));
    }

    @Override
    public void end(boolean interrupted) {
    
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}