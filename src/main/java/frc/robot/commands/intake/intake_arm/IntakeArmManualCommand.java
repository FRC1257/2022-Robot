package frc.robot.commands.intake_arm;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmManualCommand extends CommandBase {

    private IntakeArm intakeArm;
    private DoubleSupplier speedSupplier;

    public IntakeArmManualCommand(IntakeArm intakeArm, DoubleSupplier speedSupplier) {
        this.intakeArm = intakeArm;
        this.speedSupplier = speedSupplier;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intakeArm.manualControl(speedSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}