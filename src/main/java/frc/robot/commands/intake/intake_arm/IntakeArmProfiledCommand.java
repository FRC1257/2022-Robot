package frc.robot.commands.intake.intake_arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeArm;

public class IntakeArmProfiledCommand extends CommandBase {

    private IntakeArm intakeArm;
    private double setPoint;

    public IntakeArmProfiledCommand(IntakeArm intakeArm, double setPoint) {
        // get subsystem and set point
        this.intakeArm = intakeArm;
        this.setPoint = setPoint;

        addRequirements(intakeArm);

    }

    @Override
    public void initialize() {
        // Run Profiled Motion
        intakeArm.setPositionProfiled(setPoint);
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        intakeArm.endPID();
    }

    @Override
    public boolean isFinished() {
        return intakeArm.getState() != IntakeArm.State.PROFILED;
    }
}