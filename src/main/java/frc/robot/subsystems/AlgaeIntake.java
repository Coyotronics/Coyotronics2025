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

public class AlgaeIntake extends SubsystemBase {
    private SparkMax intake_motor_right = new SparkMax(37, MotorType.kBrushless);
    private SparkMax intake_motor_left = new SparkMax(62, MotorType.kBrushless);
    private SparkMax pivot_motor = new SparkMax(58, MotorType.kBrushless);

    private IntakeStates intake_state = IntakeStates.IDLE;
    private PivotStates pivot_states = PivotStates.INTAKE;

    public AlgaeIntake() {
        SparkMaxConfig intake_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);

        intake_motor_right.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intake_motor_left.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void set_intake_speed(double speed) {
        intake_motor_right.set(speed);
        intake_motor_left.set(-speed);
    }

    public void intake() {
        switch (intake_state) {
            case IDLE:
                set_intake_speed(-0.4);
                intake_state = IntakeStates.FORWARD;
                SmartDashboard.putString("ALGAE INTAKE","ALGAE INTAKE");
                break;
            
            case FORWARD:
                set_intake_speed(0);
                intake_state = IntakeStates.REVERSE;
                SmartDashboard.putString("ALGAE INTAKE","ALGAE IDLE");
                break;

            case REVERSE:
                set_intake_speed(0.4);
                intake_state = IntakeStates.IDLE;
                SmartDashboard.putString("ALGAE INTAKE","ALGAE OUTTAKE");
                break;
        
            default:
                break;
        }
    }

    public void pivot() {
        switch (pivot_states) {
            case SHOOT:
                while (pivot_motor.getEncoder().getPosition() < 6.2) {
                    pivot_motor.set(0.2);
                }
                pivot_motor.set(0);
                pivot_states = PivotStates.INTAKE;
                SmartDashboard.putString("PIVOT STATE, ALGAE", "UP");
                break;
            
            case INTAKE:
                while (pivot_motor.getEncoder().getPosition() > 5.8) {
                    pivot_motor.set(-0.2);
                }
                pivot_motor.set(0);
                pivot_states = PivotStates.SHOOT;
                SmartDashboard.putString("PIVOT STATE", "DOWN");
                break;
        
            default:
                break;
        }
    }
}
