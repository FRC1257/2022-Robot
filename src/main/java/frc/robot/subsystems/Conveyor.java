package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.ElectricalLayout.CONVEYOR_BOTTOM_ID;
import static frc.robot.Constants.ElectricalLayout.CONVEYOR_TOP_ID;
import static frc.robot.Constants.Conveyor.*;
import static frc.robot.Constants.NEO_550_CURRENT_LIMIT;

public class Conveyor extends SnailSubsystem { // 2 NEO 550s

    private CANSparkMax conveyorBottomMotor; 
    private CANSparkMax conveyorTopMotor;

    private double speed;

    public enum State {
        MANUAL,
        SHOOTING // reserved for scoring only
    }
    State state = State.MANUAL;

    public Conveyor() {
        conveyorBottomMotor = new CANSparkMax(CONVEYOR_BOTTOM_ID, MotorType.kBrushless);
        conveyorBottomMotor.restoreFactoryDefaults();
        conveyorBottomMotor.setIdleMode(IdleMode.kBrake);
        conveyorBottomMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

        conveyorTopMotor = new CANSparkMax(CONVEYOR_TOP_ID, MotorType.kBrushless);
        conveyorTopMotor.restoreFactoryDefaults();
        conveyorTopMotor.setIdleMode(IdleMode.kBrake);
        conveyorTopMotor.setSmartCurrentLimit(NEO_550_CURRENT_LIMIT);

        speed = 0;
    }

    @Override
    public void update() {
        switch(state) {
            case MANUAL:
                conveyorBottomMotor.set(speed);
                conveyorTopMotor.set(CONVEYOR_TOP_NEUTRAL_SPEED);
                break;
            case SHOOTING:
                conveyorBottomMotor.set(CONVEYOR_BOTTOM_SHOOT_SPEED);
                conveyorTopMotor.set(CONVEYOR_TOP_SHOOT_SPEED);
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


    @Override
    public void displayShuffleboard() {
        SmartDashboard.putNumber("Conveyor Bottom Motor Current", conveyorBottomMotor.getOutputCurrent());
        SmartDashboard.putNumber("Conveyor Top Motor Current", conveyorTopMotor.getOutputCurrent());
    }

    @Override
    public void tuningInit() {
        SmartDashboard.putNumber("Conveyor Bottom Shoot Speed", CONVEYOR_BOTTOM_SHOOT_SPEED);
   
        SmartDashboard.putNumber("Conveyor Top Neutral Speed", CONVEYOR_TOP_NEUTRAL_SPEED);
        SmartDashboard.putNumber("Conveyor Top Shoot Speed", CONVEYOR_TOP_SHOOT_SPEED);
    }

    @Override
    public void tuningPeriodic() {
        CONVEYOR_BOTTOM_SHOOT_SPEED = SmartDashboard.getNumber("Conveyor Bottom Shoot Speed", CONVEYOR_BOTTOM_SHOOT_SPEED);
    
        CONVEYOR_TOP_NEUTRAL_SPEED = SmartDashboard.getNumber("Conveyor Top Neutral Speed", CONVEYOR_TOP_NEUTRAL_SPEED);
        CONVEYOR_TOP_SHOOT_SPEED = SmartDashboard.getNumber("Conveyor Top Shoot Speed", CONVEYOR_TOP_SHOOT_SPEED);
    }

    public State getState() {
        return state;
    }
}