package frc.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.Limelight;

public class changePipeline extends CommandBase {

    private Limelight limelight;
    private int pipeline;

    public changePipeline(Limelight limelight, int pipeline) {
        this.limelight = limelight;
        this.pipeline = pipeline;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        limelight.setPipeline(pipeline);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}