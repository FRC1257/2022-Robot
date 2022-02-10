package frc.robot.commands.auto.segmented;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.drivetrain.DriveDistanceCommand;
import frc.robot.commands.indexer.IndexerShootCommand;
import frc.robot.commands.shooter.ShooterPIDCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Autonomous.INDEXER_DUMP_TIME;

public class DriveAndIntake extends ParallelDeadlineGroup {

    public DriveAndIntake(Drivetrain drivetrain, Indexer indexer, Intake intake, double dist) {
        addCommands(
            new DriveDistance(drivetrain, dist),
            //new Intake(/*syntax blah blah blah*/)
        );
    }
}