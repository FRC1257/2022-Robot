package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.Conveyor.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class RollerIntake extends SnailSubsystem {

    private CANSparkMax rollerConveyorMotor;

    public enum State {
        MOVING,
        NEUTRAL
    }

    State state = State.NEUTRAL;

    public RollerIntake() {
        rollerConveyorMotor = new CANSparkMax(CONVEYOR_PRIMARY_ID, MotorType.kBrushless);
        rollerConveyorMotor.restoreFactoryDefaults();
        rollerConveyorMotor.setIdleMode(IdleMode.kBrake);
        rollerConveyorMotor.setSmartCurrentLimit(NEO_CURRENT_LIMIT);
    }

    @Override
    public void update() {
        switch(state) {
            case NEUTRAL:
                rollerConveyorMotor.set(CONVEYOR_NEUTRAL_SPEED);
                break;
            case MOVING:
                rollerConveyorMotor.set(CONVEYOR_MOVING_SPEED);
                break;
        }
    }

    public void neutral() {
        state = State.NEUTRAL;
    }

    public void move() {
        state = State.MOVING;
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