package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class IntakeTimedCommand extends CommandBase {

    private Intake intake;
    double time;

    public IntakeTimedCommand(Intake intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        intake.eject();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - time) > 7.0;
    }
}