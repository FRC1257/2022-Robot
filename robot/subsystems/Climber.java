package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Climber.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class Climber extends SnailSubsystem {

    private CANSparkMax climberMotor;

    private RelativeEncoder primaryEncoder;
    private SparkMaxPIDController climberPID;

    // DigitalInput limitSwitch;
    
    public enum State {
        MANUAL,
        PID,
        PROFILED
    }
    State state = State.MANUAL;

    private double speed = 0;
    private double setpoint;

    public Climber() {
        climberMotor = new CANSparkMax(CLIMBER_PRIMARY_ID, MotorType.kBrushless);
        climberMotor.restoreFactoryDefaults();
        climberMotor.setIdleMode(IdleMode.kBrake);
        climberMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

        climberMotor.setInverted(true);

        primaryEncoder = climberMotor.getEncoder();
        primaryEncoder.setPositionConversionFactor(CLIMBER_GEAR_FACTOR); 
        primaryEncoder.setVelocityConversionFactor(CLIMBER_GEAR_FACTOR / 60); 
        primaryEncoder.setPosition(0);

        climberPID = climberMotor.getPIDController();
        climberPID.setP(CLIMBER_PID[0]);
        climberPID.setI(CLIMBER_PID[1]);
        climberPID.setD(CLIMBER_PID[2]);
        climberPID.setFF(CLIMBER_PID[3]);
        climberPID.setOutputRange(-CLIMBER_PID_MAX_OUTPUT, CLIMBER_PID_MAX_OUTPUT);

        climberPID.setSmartMotionMaxVelocity(CLIMBER_PROFILE_MAX_VEL, CLIMBER_PID_SLOT_VEL);
        climberPID.setSmartMotionMaxAccel(CLIMBER_PROFILE_MAX_ACC,CLIMBER_PID_SLOT_ACC);

        // limitSwitch = new DigitalInput(CLIMBER_LIMIT_SWITCH_PORT_ID);
    }

    @Override
    public void update() {
        switch(state) {
            case MANUAL:
                // // if (speed > 0 && limitSwitch.get()) {
                //     climberMotor.set(0);
                // } else {
                    climberMotor.set(speed);
                // }
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
                climberPID.setReference(setpoint, ControlType.kSmartMotion);
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

    public void manualControl(double speed) {
        this.speed = speed;
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumberArray("Climber Dist (PID)", new double[] 
            {primaryEncoder.getPosition(), setpoint});

        SmartDashboard.putNumber("Climber Pos", primaryEncoder.getPosition());
        SmartDashboard.putNumber("Climber Vel", primaryEncoder.getVelocity());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Climber PID P", CLIMBER_PID[0]);
        SmartDashboard.putNumber("Climber PID I", CLIMBER_PID[1]);
        SmartDashboard.putNumber("Climber PID D", CLIMBER_PID[2]);
    }

    @Override
    public void tuningPeriodic() {
        CLIMBER_PID[0] = SmartDashboard.getNumber("Climber PID P", CLIMBER_PID[0]);
        CLIMBER_PID[1] = SmartDashboard.getNumber("Climber PID I", CLIMBER_PID[1]);
        CLIMBER_PID[2] = SmartDashboard.getNumber("Climber PID D", CLIMBER_PID[2]);
        
        if(climberPID.getP() != CLIMBER_PID[0]) climberPID.setP(CLIMBER_PID[0]);
        if(climberPID.getI() != CLIMBER_PID[1]) climberPID.setI(CLIMBER_PID[1]);
        if(climberPID.getD() != CLIMBER_PID[2]) climberPID.setD(CLIMBER_PID[2]);
    }

    public State getState() {
        return state;
    }
}
