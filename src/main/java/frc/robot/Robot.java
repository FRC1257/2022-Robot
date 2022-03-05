package frc.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    
    private RobotContainer robotContainer;
    private Command autoCommand;

    // Thread m_visionThread;

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();

        UsbCamera cam = CameraServer.startAutomaticCapture();
        cam.setResolution(640, 480);

        // m_visionThread =
        // new Thread(
        //     () -> {
        //       // Get the UsbCamera from CameraServer
        //       UsbCamera camera = CameraServer.startAutomaticCapture();
        //       // Set the resolution
        //       camera.setResolution(640, 480);

        //       // Get a CvSink. This will capture Mats from the camera
        //       CvSink cvSink = CameraServer.getVideo();
        //       // Setup a CvSource. This will send images back to the Dashboard
        //       CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

        //       // Mats are very memory expensive. Lets reuse this Mat.
        //       Mat mat = new Mat();

        //       // This cannot be 'true'. The program will never exit if it is. This
        //       // lets the robot stop this thread when restarting robot code or
        //       // deploying.
        //       while (!Thread.interrupted()) {
        //         // Tell the CvSink to grab a frame from the camera and put it
        //         // in the source mat.  If there is an error notify the output.
        //         if (cvSink.grabFrame(mat) == 0) {
        //           // Send the output the error.
        //           outputStream.notifyError(cvSink.getError());
        //           // skip the rest of the current iteration
        //           continue;
        //         }
        //         // Put a rectangle on the image
        //         Imgproc.rectangle(
        //             mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
        //         // Give the output stream a new image to display
        //         outputStream.putFrame(mat);
        //       }
        //     });
        // m_visionThread.setDaemon(true);
        // m_visionThread.start();

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