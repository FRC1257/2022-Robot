package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class IntakeNeutralCommand extends CommandBase {

    private Intake rollerIntake;

  

    public IntakeNeutralCommand(Intake rollerIntake) {
        this.rollerIntake = rollerIntake;

        addRequirements(rollerIntake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        rollerIntake.neutral();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}