package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SnailSubsystem;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.IntakeArm.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

/**
 * Subsystem to handle the intake arm mechanism
 * 
 * - Utilizes one NEO motor attached to the intake mechanism
 */

public class IntakeArm extends SnailSubsystem {

    private final CANSparkMax intakeArmMotor;
    private double speed;
    private double setpoint;

    private RelativeEncoder primaryEncoder;
    private SparkMaxPIDController armPID;

    public enum State {
        MANUAL,
        PID,
    }
    State state = State.MANUAL;

    public IntakeArm() {
        // Set motor
        intakeArmMotor = new CANSparkMax(INTAKE_ARM_ID, MotorType.kBrushless);
        intakeArmMotor.restoreFactoryDefaults();
        intakeArmMotor.setIdleMode(IdleMode.kBrake);
        intakeArmMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

        // Get Encoder
        primaryEncoder = intakeArmMotor.getEncoder();
        primaryEncoder.setPositionConversionFactor(INTAKE_GEAR_FACTOR); // verify with build
        primaryEncoder.setVelocityConversionFactor(INTAKE_GEAR_FACTOR / 60);

        // Get PID Controller and set
        armPID = intakeArmMotor.getPIDController();
        armPID.setP(INTAKE_ARM_PID[0]);
        armPID.setI(INTAKE_ARM_PID[1]);
        armPID.setD(INTAKE_ARM_PID[2]);
        armPID.setFF(INTAKE_ARM_PID[3]);
        armPID.setOutputRange(-INTAKE_ARM_PID_MAX_OUTPUT, INTAKE_ARM_PID_MAX_OUTPUT);

        speed = 0;
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

    // Set Manual Control and Speed
    public void manualControl(double speed){
        this.speed = speed;
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {
        // Display Encoder position and setpoint
        SmartDashboard.putNumberArray("Intake Arm Dist PID (pos, setpt)", new double[] {primaryEncoder.getPosition(), setpoint});
    }
    
    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Intake Arm PID P", INTAKE_ARM_PID[0]);
        SmartDashboard.putNumber("Intake Arm PID I", INTAKE_ARM_PID[1]);
        SmartDashboard.putNumber("Intake Arm PID D", INTAKE_ARM_PID[2]);
        SmartDashboard.putNumber("Intake Arm PID FF", INTAKE_ARM_PID[3]);

        SmartDashboard.putNumber("Intake Arm PID Tolerance", INTAKE_ARM_PID_TOLERANCE);
        SmartDashboard.putNumber("Intake Arm PID Max Output", INTAKE_ARM_PID_MAX_OUTPUT);
        SmartDashboard.putNumber("Intake Arm Prof Max Vel", INTAKE_ARM_PROFILE_MAX_VEL);
        SmartDashboard.putNumber("Intake Arm Prof Max Accel", INTAKE_ARM_PROFILE_MAX_ACC);
        
        SmartDashboard.putNumber("Intake Setpoint Top", INTAKE_SETPOINT_TOP);
        SmartDashboard.putNumber("Intake Setpoint Bottom", INTAKE_SETPOINT_BOT);
    }

    @Override
    public void tuningPeriodic() {
        // Change the P, I, and D values
        INTAKE_ARM_PID[0] = SmartDashboard.getNumber("Intake Arm PID P", INTAKE_ARM_PID[0]);
        INTAKE_ARM_PID[1] = SmartDashboard.getNumber("Intake Arm PID I", INTAKE_ARM_PID[1]);
        INTAKE_ARM_PID[2] = SmartDashboard.getNumber("Intake Arm PID D", INTAKE_ARM_PID[2]);
        INTAKE_ARM_PID[3] = SmartDashboard.getNumber("Intake Arm PID FF", INTAKE_ARM_PID[3]);
        
        INTAKE_ARM_PID_TOLERANCE = SmartDashboard.getNumber("Intake Arm PID Tolerance", INTAKE_ARM_PID_TOLERANCE);
        INTAKE_ARM_PID_MAX_OUTPUT = SmartDashboard.getNumber("Intake Arm PID Max Output", INTAKE_ARM_PID_MAX_OUTPUT);
        
        INTAKE_ARM_PROFILE_MAX_VEL = SmartDashboard.getNumber("Intake Arm Prof Max Vel", INTAKE_ARM_PROFILE_MAX_VEL);
        INTAKE_ARM_PROFILE_MAX_ACC = SmartDashboard.getNumber("Intake Arm Prof Max Accel", INTAKE_ARM_PROFILE_MAX_ACC);
        
        INTAKE_SETPOINT_TOP = SmartDashboard.getNumber("Intake Setpoint Top", INTAKE_SETPOINT_TOP);
        INTAKE_SETPOINT_BOT = SmartDashboard.getNumber("Intake Setpoint Bottom", INTAKE_SETPOINT_BOT);
        
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
