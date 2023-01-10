package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.CONVEYOR_BOTTOM_ID;
import static frc.robot.Constants.Conveyor.*;
import static frc.robot.Constants.NEO_550_CURRENT_LIMIT;

public class Conveyor extends SnailSubsystem { // 2 NEO 550s

    private CANSparkMax conveyorBottomMotor; 

    private double speed;

    public enum State {
        MANUAL,
        SHOOTING // reserved for scoring only
    }
    State state = State.MANUAL;

    public Conveyor() {
        conveyorBottomMotor = new CANSparkMax(CONVEYOR_BOTTOM_ID, MotorType.kBrushless);
        conveyorBottomMotor.setInverted(true);
        conveyorBottomMotor.restoreFactoryDefaults();
        conveyorBottomMotor.setIdleMode(IdleMode.kBrake);
        conveyorBottomMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);
    }

    @Override
    public void update() {
        switch(state) {
            case MANUAL:
                conveyorBottomMotor.set(speed);
                break;
            case SHOOTING:
                conveyorBottomMotor.set(CONVEYOR_BOTTOM_SHOOT_SPEED);
                break;
        }

    }

    public void setSpeed(double speed) {
        state = State.MANUAL;
        this.speed = speed;
    }

    public void shoot() {
        state = State.SHOOTING;
    }
    //TODO where the hell are the states


    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumber("Conveyor Bottom Motor Current", conveyorBottomMotor.getOutputCurrent());

        SmartDashboard.putString("Conveyor State", getState().name());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Conveyor Bottom Shoot Speed", CONVEYOR_BOTTOM_SHOOT_SPEED);
    }

    @Override
    public void tuningPeriodic() {
        CONVEYOR_BOTTOM_SHOOT_SPEED = SmartDashboard.getNumber("Conveyor Bottom Shoot Speed", CONVEYOR_BOTTOM_SHOOT_SPEED);
    }

    public State getState() {
        return state;
    }
}