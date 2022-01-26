package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

import java.util.function.DoubleSupplier;

public class ClimberProfiledCommand extends CommandBase {

    private Climber climber;
    private double setPoint;

    public void ClimberManualCommand(Climber climber, double setPoint) {
        this.climber = climber;
        this.setPoint = setPoint;

        addRequirements(climber);

    }

    @Override
    public void initialize() {
        climber.setPositionProfiled(setPoint);
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        climber.endPID();
    }

    @Override
    public boolean isFinished() {
        return climber.getState() != Climber.State.PID;
    }
}