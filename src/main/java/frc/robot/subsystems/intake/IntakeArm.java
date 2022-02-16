package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.SnailSubsystem;

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
        // Set motor
        intakeArmMotor = new CANSparkMax(INTAKE_ARM_ID, MotorType.kBrushless);
        intakeArmMotor.restoreFactoryDefaults();
        intakeArmMotor.setIdleMode(IdleMode.kBrake);
        intakeArmMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

        // Set State
        state = State.MANUAL;

        // Get Encoder
        primaryEncoder = intakeArmMotor.getEncoder();
        primaryEncoder.setPositionConversionFactor(48.0 * Math.PI * 6);
        primaryEncoder.setVelocityConversionFactor(48.0 * Math.PI * 6 / 60);

        // Get PID Controller and set
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
                // If in manual mode set the speed
                intakeArmMotor.set(speed);
                break;
            // Autonomous
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
    
    // End PID
    public void endPID() {
        state = State.MANUAL;
    }

    // Set PID
    public void setPosition(double setpoint) {
        state = State.PID;
        this.setpoint = setpoint;
    }

    // Set Profiled Motion
    public void setPositionProfiled(double setpoint) {
        this.setpoint = setpoint;
        state = State.PROFILED;
    }

    // Set Manual Control and Speed
    public void manualControl(double speed){
        this.speed = speed;
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {
        // Display Encoder position and setpoint
        SmartDashboard.putNumberArray("Intake Arm Dist PID", new double[] 
        {primaryEncoder.getPosition(), setpoint});
    }
    
    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Intake Armm PID P", INTAKE_ARM_PID[0]);
        SmartDashboard.putNumber("Intake Armm PID I", INTAKE_ARM_PID[1]);
        SmartDashboard.putNumber("Intake Armm PID D", INTAKE_ARM_PID[2]);
        SmartDashboard.putNumber("Intake Armm PID FF", INTAKE_ARM_PID[3]);

        SmartDashboard.putNumber("INTAKE ARM PID TOLERANCE", INTAKE_ARM_PID_TOLERANCE);
        SmartDashboard.putNumber("INTAKE ARM PID MAX OUTPUT", INTAKE_ARM_PID_MAX_OUTPUT);
        SmartDashboard.putNumber("INTAKE ARM PROFILE MAX VEL", INTAKE_ARM_PROFILE_MAX_VEL);
        SmartDashboard.putNumber("INTAKE ARM PROFILE MAX ACC", INTAKE_ARM_PROFILE_MAX_ACC);
        
        SmartDashboard.putNumber("SETPOINT TOP", SETPOINT_TOP);
        SmartDashboard.putNumber("SETPOINT TOP", SETPOINT_TOP);
        
    }

    @Override
    public void tuningPeriodic() {
        // Change the P, I, and D values
        INTAKE_ARM_PID[0] = SmartDashboard.getNumber("Intake Arm PID P", INTAKE_ARM_PID[0]);
        INTAKE_ARM_PID[1] = SmartDashboard.getNumber("Intake Arm PID I", INTAKE_ARM_PID[1]);
        INTAKE_ARM_PID[2] = SmartDashboard.getNumber("Intake Arm PID D", INTAKE_ARM_PID[2]);
        INTAKE_ARM_PID[3] = SmartDashboard.getNumber("Intake Arm PID FF", INTAKE_ARM_PID[3]);
        
        INTAKE_ARM_PID_TOLERANCE = SmartDashboard.getNumber("INTAKE ARM PID TOLERANCE", INTAKE_ARM_PID_TOLERANCE);
        INTAKE_ARM_PID_MAX_OUTPUT = SmartDashboard.getNumber("INTAKE ARM PID MAX OUTPUT", INTAKE_ARM_PID_MAX_OUTPUT);
        
        INTAKE_ARM_PROFILE_MAX_VEL = SmartDashboard.getNumber("INTAKE ARM PROFILE MAX VEL", INTAKE_ARM_PROFILE_MAX_VEL);
        INTAKE_ARM_PROFILE_MAX_ACC = SmartDashboard.getNumber("INTAKE ARM PROFILE MAX ACC", INTAKE_ARM_PROFILE_MAX_ACC);
        
        SETPOINT_TOP = SmartDashboard.getNumber("SETPOINT TOP", SETPOINT_TOP);
        SETPOINT_BOT = SmartDashboard.getNumber("SETPOINT BOT", SETPOINT_BOT);
        

        // Set PID
        if(armPID.getP() != INTAKE_ARM_PID[0])  armPID.setP(INTAKE_ARM_PID[0]);
        if(armPID.getI() != INTAKE_ARM_PID[1]) armPID.setI(INTAKE_ARM_PID[1]);
        if(armPID.getD() != INTAKE_ARM_PID[2]) armPID.setD(INTAKE_ARM_PID[2]);
        if(armPID.getFF() != INTAKE_ARM_PID[3]) armPID.setFF(INTAKE_ARM_PID[3]);
        if(armPID.getOutputMin() != -INTAKE_ARM_PID_MAX_OUTPUT) armPID.setOutputRange(-INTAKE_ARM_PID_MAX_OUTPUT, INTAKE_ARM_PID_MAX_OUTPUT);
        if(armPID.getSmartMotionMaxVelocity(INTAKE_ARM_PID_SLOT_VEL) != INTAKE_ARM_PROFILE_MAX_VEL) armPID.setSmartMotionMaxVelocity(INTAKE_ARM_PROFILE_MAX_VEL, INTAKE_ARM_PID_SLOT_VEL);
        if(armPID.getSmartMotionMaxAccel(INTAKE_ARM_PID_SLOT_ACC) != INTAKE_ARM_PROFILE_MAX_ACC) armPID.setSmartMotionMaxVelocity(INTAKE_ARM_PROFILE_MAX_ACC, INTAKE_ARM_PID_SLOT_ACC);
    }

    /**
    * Returns the state
    */
    public State getState() {
        return state;
    }
}
