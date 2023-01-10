package frc.robot.commands.conveyor;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class ConveyorShootCommand extends CommandBase {

    private Conveyor conveyor;
    // private double startTime;

    public ConveyorShootCommand(Conveyor conveyor) {
        this.conveyor = conveyor;

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        // startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        conveyor.shoot();
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.setSpeed(0);
    }

    @Override
    public boolean isFinished() {
        // return (Timer.getFPGATimestamp() - startTime) > 2;
        return false;
    }
}