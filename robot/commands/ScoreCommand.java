package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;

public class ScoreCommand extends CommandBase {

    private Shooter shooter;
    private Conveyor conveyor;

    public ScoreCommand(Shooter shooter, Conveyor conveyor) {
        this.shooter = shooter;
        this.conveyor = conveyor;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        shooter.shoot();
        conveyor.shoot();
    }

    @Override
    public void end(boolean interrupted) {
    
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}