
package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import frc.robot.Enums.IntakeStates;
import frc.robot.Enums.PivotStates;

public class CoralIntake extends SubsystemBase {
    SparkMax coral_intake_motor = new SparkMax(36, MotorType.kBrushless);
    SparkMax coral_pivot_motor = new SparkMax(7, MotorType.kBrushless);

    private IntakeStates intake_state = IntakeStates.IDLE;
    private PivotStates pivot_state = PivotStates.INTAKE;

    public CoralIntake() {
        SparkMaxConfig intake_config = new SparkMaxConfig();
        SparkMaxConfig pivot_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);

        pivot_config.idleMode(IdleMode.kBrake);

        coral_intake_motor.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        coral_pivot_motor.configure(pivot_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        coral_pivot_motor.getEncoder().setPosition(0);

        SmartDashboard.putString("INTAKE STATE", "IDLE");
        SmartDashboard.putString("PIVOT STATE", "IDLE");
    }

    public void pivot() {
        switch (pivot_state) {
            case SHOOT:
                while (get_pivot_position() < 6.2) {
                    coral_pivot_motor.set(0.2);
                }
                coral_pivot_motor.set(0);
                pivot_state = PivotStates.INTAKE;
                SmartDashboard.putString("Pivot State", "PIVOT INTAKE");
                break;
            
            case INTAKE:
                while (get_pivot_position() > 5.8) {
                    coral_pivot_motor.set(-0.2);
                }
                coral_pivot_motor.set(0);
                pivot_state = PivotStates.SHOOT;
                SmartDashboard.putString("Pivot State", "PIVOT SHOOT");
                break;
        
            default:
                break;
        }
    }

    private double get_pivot_position() {
        return coral_pivot_motor.getEncoder().getPosition();
    }

    public void intake() {
        switch (intake_state) {
            case IDLE:
                coral_intake_motor.set(0.5);
                intake_state = IntakeStates.FORWARD;
                SmartDashboard.putString("Intake State", "CORAL INTAKE");
                break;
            
            case FORWARD:
                coral_intake_motor.set(0);
                intake_state = IntakeStates.REVERSE;
                SmartDashboard.putString("Intake State", "CORAL IDLE");
                break;
            
            case REVERSE:
                coral_intake_motor.set(-0.5);
                intake_state = IntakeStates.IDLE;
                SmartDashboard.putString("Intake State", "CORAL OUTTAKE");
                break;
            
            default:
                break;
        }
    }
}
