package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.CONVEYOR_PRIMARY_ID;
import static frc.robot.Constants.Conveyor.*;
import static frc.robot.Constants.NEO_CURRENT_LIMIT;

public class Conveyor extends SnailSubsystem {

    private CANSparkMax conveyorMotor;

    public enum State {
        RAISING,
        NEUTRAL,
        LOWERING,
        SHOOTING // reserved for scoring only
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
            case RAISING:
                conveyorMotor.set(CONVEYOR_RAISE_SPEED);
                break;
            case LOWERING:
                conveyorMotor.set(CONVEYOR_LOWER_SPEED);
                break;
            case SHOOTING:
                conveyorMotor.set(CONVEYOR_SHOOT_SPEED);
                break;
        }
    }

    public void neutral() {
        state = State.NEUTRAL;
    }

    public void raise() {
        state = State.RAISING;
    }

    public void shoot() {
        state = State.SHOOTING;
    }

    public void lower() {
        state = State.LOWERING;
    }

    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumber("Conveyor Motor Current", conveyorMotor.getOutputCurrent());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Conveyor Raise Speed", CONVEYOR_RAISE_SPEED);
        SmartDashboard.putNumber("Conveyor Neutral Speed", CONVEYOR_NEUTRAL_SPEED);
        SmartDashboard.putNumber("Conveyor Lower Speed", CONVEYOR_LOWER_SPEED);
        SmartDashboard.putNumber("Conveyor Shoot Speed", CONVEYOR_SHOOT_SPEED);
    }

    @Override
    public void tuningPeriodic() {
        CONVEYOR_RAISE_SPEED = SmartDashboard.getNumber("Conveyor Raise Speed", CONVEYOR_RAISE_SPEED);
        CONVEYOR_NEUTRAL_SPEED = SmartDashboard.getNumber("Conveyor Neutral Speed", CONVEYOR_NEUTRAL_SPEED);
        CONVEYOR_LOWER_SPEED = SmartDashboard.getNumber("Conveyor Lower Speed", CONVEYOR_LOWER_SPEED);
        CONVEYOR_SHOOT_SPEED = SmartDashboard.getNumber("Conveyor Shoot Speed", CONVEYOR_SHOOT_SPEED);
    }

    public State getState() {
        return state;
    }
}