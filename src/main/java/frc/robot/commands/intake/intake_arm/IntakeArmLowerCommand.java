package frc.robot.commands.intake.intake_arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeArm;

public class IntakeArmLowerCommand extends CommandBase {

    private IntakeArm intakeArm;

    public IntakeArmLowerCommand(IntakeArm intakeArm) {
        this.intakeArm = intakeArm;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        intakeArm.lower();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}