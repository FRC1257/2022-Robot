package frc.robot.commands.intake.intake_arm;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeArm;

public class IntakeArmNeutralCommand extends CommandBase {

    private IntakeArm intakeArm;

    public IntakeArmNeutralCommand(IntakeArm intakeArm) {
        this.intakeArm = intakeArm;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        
    }

    // Run the Manual Control command and get the speed
    @Override
    public void execute() {
        intakeArm.neutral();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}