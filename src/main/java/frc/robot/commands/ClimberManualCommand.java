package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

import java.util.function.DoubleSupplier;

public class ClimberManualCommand extends CommandBase {

    public final Climber climber;
    public final DoubleSupplier speedSupplier;

    public ClimberManualCommand(Climber climber, DoubleSupplier speedSupplier) {
        this.climber = climber;
        this.speedSupplier = speedSupplier;

        addRequirements(climber);

    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        climber.manualControl(speedSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}