package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.ElectricalLayout.*;
import static frc.robot.Constants.Climber.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class Climber extends SnailSubsystem {

    private CANSparkMax climberMotor;
    private CANSparkMax climberFollowerMotor;

    public enum State {
        MANUAL
    }

    State state = State.MANUAL;
    private double speed = 0;

    public Climber() {
        climberMotor = new CANSparkMax(climber_PRIMARY_ID, MotorType.kBrushless);
        climberMotor.restoreFactoryDefaults();
        climberMotor.setIdleMode(IdleMode.kBrake);
        climberMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);

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
    }
    }

    public void manualControl(double speed){
        this.speed = speed;
        state = State.MANUAL;
    }

    @Override
    public void displayShuffleboard() {

    }

    @Override
    public void tuningInit() {

    }

    @Override
    public void tuningPeriodic() {

    }

    public State getState() {
        return state;
    }
    
}