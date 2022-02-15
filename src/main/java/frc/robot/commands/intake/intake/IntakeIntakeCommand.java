package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeIntakeCommand extends CommandBase {

    private Intake rollerIntake;

    public IntakeIntakeCommand(Intake rollerIntake) {
        this.rollerIntake = rollerIntake;

        addRequirements(rollerIntake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        rollerIntake.intake();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}