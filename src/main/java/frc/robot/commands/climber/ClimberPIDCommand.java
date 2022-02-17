package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberPIDCommand extends CommandBase {

    private Climber climber;
    private double setpoint;

    public ClimberPIDCommand(Climber climber, double setpoint) {
        this.climber = climber;
        this.setpoint = setpoint;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setPosition(setpoint);
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