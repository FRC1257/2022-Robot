package frc.robot.commands.intake.intake_arm;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeArm;

public class IntakeArmManualCommand extends CommandBase {

    private IntakeArm intakeArm;
    private DoubleSupplier speedSupplier;

    public IntakeArmManualCommand(IntakeArm intakeArm, DoubleSupplier speedSupplier) {
        // Set subsystem and DoubleSupplier
        this.intakeArm = intakeArm;
        this.speedSupplier = speedSupplier;

        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {}

    // Run the Manual Control command and get the speed
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