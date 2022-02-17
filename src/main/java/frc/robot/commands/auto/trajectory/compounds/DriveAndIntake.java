package frc.robot.commands.auto.trajectory.compounds;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.intake.Intake;

import static frc.robot.Constants.Autonomous.CONVEYOR_DUMP_TIME;

public class DriveAndIntake extends ParallelDeadlineGroup { // written by Steve

    public DriveAndIntake(Drivetrain drivetrain, double dist, Intake intake){
        super(
            new DriveDistanceCommand(drivetrain, dist)
            //new Intake(/*syntax blah blah blah*/)
        );
    }
}