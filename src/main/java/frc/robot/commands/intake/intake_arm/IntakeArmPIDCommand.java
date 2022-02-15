package frc.robot.commands.intake_arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmPIDCommand extends CommandBase {

    private IntakeArm intakeArm;
    private double setPoint;

    public IntakeArmPIDCommand(IntakeArm intakeArm, double setPoint) {
        this.intakeArm = intakeArm;
        this.setPoint = setPoint;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        intakeArm.setPosition(setPoint);
    }

    @Override
    public void execute() {}

    @Override
    public void end(boolean interrupted) {
        intakeArm.endPID();
    }

    @Override
    public boolean isFinished() {
        return intakeArm.getState() != IntakeArm.State.PID;
    }
}
