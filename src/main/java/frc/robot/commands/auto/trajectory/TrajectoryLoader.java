package frc.robot.commands.auto.trajectory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.commands.drivetrain.DriveTrajectoryCommand;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import java.io.File;
import java.util.Hashtable;
import java.io.IOException;
import java.nio.file.Path;


public class TrajectoryLoader {
    Hashtable<String, DriveTrajectoryCommand> trajectories = new Hashtable<>();
    
    public TrajectoryLoader(Drivetrain drivetrain) {
        loadTrajectories(drivetrain);
    }

    // Trajectory Loading Function
    public static Trajectory loadTrajectoryFromFile(String filename) {
        Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(filename);
        Trajectory trajectory = null;

        try {
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException e) {
            DriverStation.reportError("Unable to open trajectory: " + filename, e.getStackTrace());
        }

        return trajectory;
    }

    // Load all trajectories and save the commands in the hashtable
    public void loadTrajectories(Drivetrain drivetrain) {
        // from https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
        // find all files in the trajectories directory
        Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve("paths/");
        File dir = new File(trajectoryPath.normalize().toString());

        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            // iterate through all files in the directory
            for (File child : directoryListing) {
                // only look at files with .
                if (child.getName().contains(".")) {
                    // take the name of the file
                    String[] name = child.getName().split("\\.", 2);
                    // make a new command with the name and the trajectory and add it to the hashtable
                    trajectories.put(name[0], new DriveTrajectoryCommand(drivetrain, loadTrajectoryFromFile("paths/" + child.getName())));
                }    
        }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
        
    }

    public DriveTrajectoryCommand getCommand(String pathName) {
        // Get the value from the String key from the Hashtable
        return trajectories.get(pathName);
    }
}
