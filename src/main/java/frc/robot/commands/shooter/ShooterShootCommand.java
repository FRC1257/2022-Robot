package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterShootCommand extends CommandBase {

    private Shooter shooter;
// double startTime;
    public ShooterShootCommand(Shooter shooter) {
        this.shooter = shooter;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        // startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        shooter.shoot();
    }

    @Override
    public void end(boolean interrupted) {
        shooter.neutral();
    }

    @Override
    public boolean isFinished() {
        // return (Timer.getFPGATimestamp() - startTime) > 2;
        return false;
    }
}