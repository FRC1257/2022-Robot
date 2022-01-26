package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

import java.util.function.DoubleSupplier;

public class ClimberPIDCommand extends CommandBase {

    private Climber climber;
    private double setPoint;

    public ClimberPIDCommand(Climber climber, double setPoint) {
        this.climber = climber;
        this.setPoint = setPoint;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setPosition(setPoint);
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