package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Climber.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class Climber extends SnailSubsystem {

    // Create Motors
    private CANSparkMax climberMotor;
    private CANSparkMax climberFollowerMotor;

    // Create encoder and pid controller
    private CANEncoder primaryEncoder;
    private CANPIDController climberPID;

    // Define states
    public enum State {
        MANUAL,
        PID,
        PROFILED
    }

    // Set state and speed
    State state = State.MANUAL;
    private double speed = 0;

    // Constructor
    public Climber() {
        // Set Motor
        climberMotor = new CANSparkMax(climber_PRIMARY_ID, MotorType.kBrushless);
        climberMotor.restoreFactoryDefaults();
        climberMotor.setIdleMode(IdleMode.kBrake);
        climberMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);


        // Set encoder
        primaryEncoder = new CANEncoder(primaryMotor);
        primaryEncoder.setPositionConversionFactor(48.0 * Math.PI * 6);
        primaryEncoder.setVelocityConversionFactor(48.0 * Math.PI * 6 / 60);

        climberPID = new CANPIDController(primaryMotor);
        climberPID.setP(CLIMBER_PID[0]);
        climberPID.setI(CLIMBER_PID[1]);
        climberPID.setD(CLIMBER_PID[2]);
        climberPID.setFF(CLIMBER_PIDF[3]);
        climberPID.setOutputRange(-CLIMBER_PID_MAX_OUTPUT, CLIMBER_PID_MAX_OUTPUT);

        climberPID.setSmartMotionMaxVelocity(CLIMBER_PROFILE_MAX_VEL);
        climberPID.setSmartMotionMaxAccel(CLIMBER_PROFILE_MAX_ACC);

        // Follor set motor
        climberFollowerMotor = new CANSparkMax(climber_FOLLOWER_ID, MotorType.kBrushless);
        climberFollowerMotor.restoreFactoryDefaults();
        climberFollowerMotor.setIdleMode(IdleMode.kBrake);
        climberFollowerMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
        climberFollowerMotor.follow(climberMotor, false); // following
    }

    @Override
    public void update() {
        switch(state) {
            case MANUAL:
                climberMotor.set(speed);
                break;
            case PID:
                // send the desired setpoint to the PID controller and specify we want to use position control
                climberPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(primaryEncoder.getPosition() - setpoint) < CLIMBER_PID_TOLERANCE) {
                    state = State.MANUAL;
                }
                
                break;
            case PROFILED:
                climberPID.setReference(setPoint, ControlType.kSmartMotion)
                // check our error and update the state if we finish
                if(Math.abs(primaryEncoder.getPosition() - setpoint) < CLIMBER_PID_TOLERANCE) {
                    state = State.MANUAL;
                }
                break;
    }
    }

    public void endPID() {
        state = State.MANUAL;
    }

    public void setPosition(double setpoint) {
        state = State.PID;
        this.setpoint = setpoint;
    }

    public void setPositionProfiled(double setpoint) {
        this.setpoint = setpoint;
        state = State.PROFILED;
    }

    public void manualControl(double speed){
        this.speed = speed;
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumberArray("Elevator Dist PID", new double[] 
        {primaryEncoder.getPosition(), setpoint});
    }

    @Override
    public void tuningInit() {

    }

    @Override
    public void tuningPeriodic() {
        ELEVATOR_PID[0] = Shuffleboard.getNumber("Elevator PID P", ELEVATOR_PID[0]);
        ELEVATOR_PID[1] = Shuffleboard.getNumber("Elevator PID I", ELEVATOR_PID[1]);
        ELEVATOR_PID[2] = Shuffleboard.getNumber("Elevator PID D", ELEVATOR_PID[2]);

        if(elevatorPID.getP() != ELEVATOR_PID[0]) elevatorPID.setP(ELEVATOR_PID[0]);
        if(elevatorPID.getI() != ELEVATOR_PID[1]) elevatorPID.setI(ELEVATOR_PID[1]);
        if(elevatorPID.getD() != ELEVATOR_PID[2]) elevatorPID.setD(ELEVATOR_PID[2]);
    }

    public State getState() {
        return state;
    }
    
}