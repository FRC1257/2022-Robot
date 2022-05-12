package frc.robot;

import edu.wpi.first.util.net.PortForwarder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    
    private RobotContainer robotContainer;
    private Command autoCommand;


    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();

        // Redirect the Limelight's address through the rio-tether connection (172...)
        // On field, use 10.12.57.11...
        PortForwarder.add(5800, "limelight.local", 5800); // stream
        PortForwarder.add(5801, "limelight.local", 5801); // interface
        PortForwarder.add(5802, "limelight.local", 5802); // 2-5 for actual interface settings
        PortForwarder.add(5803, "limelight.local", 5803);
        PortForwarder.add(5804, "limelight.local", 5804);
        PortForwarder.add(5805, "limelight.local", 5805);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        
        robotContainer.displayShuffleboard();
        if(SmartDashboard.getBoolean("Testing", false)) {
            robotContainer.tuningPeriodic();
        }
    }

    @Override
    public void autonomousInit() {
        autoCommand = robotContainer.getAutoCommand();

        if(autoCommand != null) {
            autoCommand.schedule();
        }
    }

    @Override
    public void teleopInit() {
        if(autoCommand != null) {
            autoCommand.cancel();
        }
    }

    @Override
    public void testInit() {
        robotContainer.tuningInit();
    }
}