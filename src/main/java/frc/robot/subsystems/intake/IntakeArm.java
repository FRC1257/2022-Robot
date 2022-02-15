package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.IntakeArm.*;
import static frc.robot.Constants.NEO_550_CURRENT_LIMIT;

/**
 * Subsystem to handle the intake arm mechanism
 * 
 * - Utilizes one NEO 550 motor attached to the intake mechanism
 */

public class IntakeArm extends SnailSubsystem {

    private final CANSparkMax intakeArmMotor;
    private double speed = 0;
    private double setpoint;

    private RelativeEncoder primaryEncoder;
    private SparkMaxPIDController armPID;

    public enum State {
        MANUAL,
        PID,
        PROFILED
    }

    State state;

    public IntakeArm() {
        intakeArmMotor = new CANSparkMax(INTAKE_ARM_ID, MotorType.kBrushless);
        intakeArmMotor.restoreFactoryDefaults();
        intakeArmMotor.setIdleMode(IdleMode.kBrake);
        intakeArmMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

        state = State.MANUAL;

        primaryEncoder = intakeArmMotor.getEncoder();
        primaryEncoder.setPositionConversionFactor(48.0 * Math.PI * 6);
        primaryEncoder.setVelocityConversionFactor(48.0 * Math.PI * 6 / 60);

        armPID = intakeArmMotor.getPIDController();
        armPID.setP(INTAKE_ARM_PID[0]);
        armPID.setI(INTAKE_ARM_PID[1]);
        armPID.setD(INTAKE_ARM_PID[2]);
        armPID.setFF(INTAKE_ARM_PID[3]);
        armPID.setOutputRange(-INTAKE_ARM_PID_MAX_OUTPUT, INTAKE_ARM_PID_MAX_OUTPUT);

        armPID.setSmartMotionMaxVelocity(INTAKE_ARM_PROFILE_MAX_VEL, INTAKE_ARM_PID_SLOT_VEL);
        armPID.setSmartMotionMaxAccel(INTAKE_ARM_PROFILE_MAX_ACC,INTAKE_ARM_PID_SLOT_ACC);

    }
    
    /**
     * Update motor outputs according to the current state
     */
    @Override
    public void update() {
        switch(state) {
            case MANUAL: 
                intakeArmMotor.set(speed);
                break;
            
            case PID:
                // send the desired setpoint to the PID controller and specify we want to use position control
                armPID.setReference(setpoint, ControlType.kPosition);

                // check our error and update the state if we finish
                if(Math.abs(primaryEncoder.getPosition() - setpoint) < INTAKE_ARM_PID_TOLERANCE) {
                    state = State.MANUAL;
                }
                
                break;
            
            case PROFILED:
                armPID.setReference(setpoint, ControlType.kSmartMotion);
                // check our error and update the state if we finish
                if(Math.abs(primaryEncoder.getPosition() - setpoint) < INTAKE_ARM_PID_TOLERANCE) {
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
        SmartDashboard.putNumberArray("Intake Arm Dist PID", new double[] 
        {primaryEncoder.getPosition(), setpoint});
    }

    @Override
    public void tuningInit() {

    }

    @Override
    public void tuningPeriodic() {
        /*
        INTAKE_ARM_PID[0] = Shuffleboard.getNumber("Intake Arm PID P", INTAKE_ARM_PID[0]);
        INTAKE_ARM_PID[1] = Shuffleboard.getNumber("Intake Arm PID I", INTAKE_ARM_PID[1]);
        INTAKE_ARM_PID[2] = Shuffleboard.getNumber("Intake Arm PID D", INTAKE_ARM_PID[2]);
        */
        if(armPID.getP() != INTAKE_ARM_PID[0]) armPID.setP(INTAKE_ARM_PID[0]);
        if(armPID.getI() != INTAKE_ARM_PID[1]) armPID.setI(INTAKE_ARM_PID[1]);
        if(armPID.getD() != INTAKE_ARM_PID[2]) armPID.setD(INTAKE_ARM_PID[2]);
    }

    /**
    * Returns the state
    */
    public State getState() {
        return state;
    }
}
