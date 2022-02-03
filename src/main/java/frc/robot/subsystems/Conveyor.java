package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.Conveyor.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class Conveyor extends SnailSubsystem {

    private CANSparkMax conveyorMotor;

    public enum State {
        MOVING,
        NEUTRAL,
        REVERSE
    }

    State state = State.NEUTRAL;

    public Conveyor() {
        conveyorMotor = new CANSparkMax(CONVEYOR_PRIMARY_ID, MotorType.kBrushless);
        conveyorMotor.restoreFactoryDefaults();
        conveyorMotor.setIdleMode(IdleMode.kBrake);
        conveyorMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
    }

    @Override
    public void update() {
        switch(state) {
            case NEUTRAL:
                conveyorMotor.set(CONVEYOR_NEUTRAL_SPEED);
                break;
            case MOVING:
                conveyorMotor.set(CONVEYOR_MOVING_SPEED);
                break;
            case REVERSE:
                conveyorMotor.set(CONVEYOR_REVERSE_SPEED);
                break;
        }
    }

    public void neutral() {
        state = State.NEUTRAL;
    }

    public void move() {
        state = State.MOVING;
    }

    public void reverse() {
        state = State.REVERSE;
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