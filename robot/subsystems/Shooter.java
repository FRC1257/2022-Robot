package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.SHOOTER_ID;
import static frc.robot.Constants.Shooter.*;
import static frc.robot.Constants.NEO_550_CURRENT_LIMIT;

public class Shooter extends SnailSubsystem {

    private CANSparkMax shooterMotor; 

    public enum State {
        SHOOTING,
        NEUTRAL,
        BACKING
    }
    State state = State.NEUTRAL;

    public Shooter() {
        shooterMotor = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
        shooterMotor.setInverted(true);
        shooterMotor.restoreFactoryDefaults();
        shooterMotor.setIdleMode(IdleMode.kBrake);
        shooterMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);
    }

    @Override
    public void update() {
        switch(state) {
            case SHOOTING:
                shooterMotor.set(SHOOTER_SHOOT_SPEED);
                break;
            case NEUTRAL:
                shooterMotor.set(SHOOTER_NEUTRAL_SPEED);
                break;
            case BACKING:
                shooterMotor.set(SHOOTER_BACKING_SPEED);
                break;
        }
    }

    public void shoot() {
        state = State.SHOOTING;
    }

    public void neutral() {
        state = State.NEUTRAL;
    } 

    public void back() {
        state = State.BACKING;
    }


    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumber("Shooter Motor Current", shooterMotor.getOutputCurrent());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Shooter Shoot Speed", SHOOTER_SHOOT_SPEED);
    }

    @Override
    public void tuningPeriodic() {
        SHOOTER_SHOOT_SPEED = SmartDashboard.getNumber("Shooter Shoot Speed", SHOOTER_SHOOT_SPEED);
    }

    public State getState() {
        return state;
    }
}