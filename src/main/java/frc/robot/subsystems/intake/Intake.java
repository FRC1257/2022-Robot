package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Intake.*;
import static frc.robot.Constants.NEO_550_CURRENT_LIMIT;

/**
 * Subsystem to handle the intake mechanism
 * 
 * - Utilizes one NEO 550 motor attached to the intake mechanism
 */

public class Intake extends SnailSubsystem {

    private final CANSparkMax leftIntakeMotor;
    private final CANSparkMax rightIntakeMotor;

    private final CANSparkMax intakeArmMotor;
    private double armSpeed = 0;

    /**
     * NEUTRAL - The cargo is not moved by the intake
     * 
     * INTAKING - The cargo is intaked and given to the conveyor
     * 
     * EJECTING - The cargo is ejected from the intake and taken out of the robot's control
     */
    public enum State {
        NEUTRAL,
        INTAKING,
        EJECTING
    }

    public enum ArmState {
        MANUAL
    }

    State state = State.NEUTRAL;
    ArmState armState = ArmState.MANUAL;

    public Intake() {
        leftIntakeMotor = new CANSparkMax(LEFT_INTAKE_MOTOR_ID, MotorType.kBrushless);
        leftIntakeMotor.restoreFactoryDefaults();
        leftIntakeMotor.setIdleMode(IdleMode.kBrake);
        leftIntakeMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

        rightIntakeMotor = new CANSparkMax(RIGHT_INTAKE_MOTOR_ID, MotorType.kBrushless);
        rightIntakeMotor.restoreFactoryDefaults();
        rightIntakeMotor.setIdleMode(IdleMode.kBrake);
        rightIntakeMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);
        rightIntakeMotor.follow(leftIntakeMotor);

        intakeArmMotor = new CANSparkMax(INTAKE_ARM_ID, MotorType.kBrushless);
        intakeArmMotor.restoreFactoryDefaults();
        intakeArmMotor.setIdleMode(IdleMode.kBrake);
        intakeArmMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

    }
    
    /**
     * Update motor outputs according to the current state
     */
    @Override
    public void update() {
        switch(state) {
            case NEUTRAL: 
                leftIntakeMotor.set(Constants.Intake.INTAKE_NEUTRAL_SPEED);
                break;
            case INTAKING:
                leftIntakeMotor.set(Constants.Intake.INTAKE_INTAKE_SPEED);
                break;
            case EJECTING:
                leftIntakeMotor.set(Constants.Intake.INTAKE_EJECT_SPEED);
                break;
        }
        switch (armState) {
            case MANUAL:
                intakeArmMotor.set(armSpeed);
                break;
        }
    }
    
    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumber("Intake Motor Current", leftIntakeMotor.getOutputCurrent());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Intake Eject Speed", INTAKE_EJECT_SPEED);
        SmartDashboard.putNumber("Intake Intake Speed", INTAKE_INTAKE_SPEED);
        SmartDashboard.putNumber("Intake Neutral Speed", INTAKE_NEUTRAL_SPEED);        
    }

    @Override
    public void tuningPeriodic() {
        INTAKE_EJECT_SPEED = SmartDashboard.getNumber("Intake Eject Speed", INTAKE_EJECT_SPEED);
        INTAKE_INTAKE_SPEED = SmartDashboard.getNumber("Intake Intake Speed", INTAKE_INTAKE_SPEED);
        INTAKE_NEUTRAL_SPEED = SmartDashboard.getNumber("Intake Neutral Speed", INTAKE_NEUTRAL_SPEED);        
    }

    /**
    * Changes state to neutral
    */
    public void neutral() {
        state = State.NEUTRAL;
    }

    /**
    * Changes state to eject
    */
    public void eject() {
        state = State.EJECTING;
    }

    /**
    * Changes state to intake
    */
    public void intake() {
        state = State.INTAKING;
    }

    public void intakeArmManualControl(double speed){
        this.armSpeed = speed;
        armState = ArmState.MANUAL;
    }

    /**
    * Returns the state
    */
    public State getState() {
        return state;
    }
}
