package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import frc.robot.Constants.SubsystemConstants;
import frc.robot.Enums.IntakeStates;
import frc.robot.Enums.PivotStates;

public class AlgaeSubsystem extends SubsystemBase {
    private SparkMax intake_motor_right = new SparkMax(SubsystemConstants.ALGAE_INTAKE_MOTOR_RIGHT_ID,
            MotorType.kBrushless);
    private SparkMax intake_motor_left = new SparkMax(SubsystemConstants.ALGAE_INTAKE_MOTOR_LEFT_ID,
            MotorType.kBrushless);
    private SparkMax pivot_motor = new SparkMax(SubsystemConstants.ALGAE_PIVOT_MOTOR_ID, MotorType.kBrushless);

    private IntakeStates intake_state = IntakeStates.IDLE;
    private PivotStates pivot_states = PivotStates.INTAKE;

    public AlgaeSubsystem() {
        SparkMaxConfig intake_config = new SparkMaxConfig();
        SparkMaxConfig pivot_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);
        pivot_config.idleMode(IdleMode.kBrake);

        intake_motor_right.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intake_motor_left.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        pivot_motor.configure(pivot_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void set_intake_speed(double speed) {
        intake_motor_right.set(speed);
        intake_motor_left.set(-speed);
    }

    public void intake() {
        switch (intake_state) {
            case IDLE:
                set_intake_speed(0);
                intake_state = IntakeStates.REVERSE;
                break;

            case REVERSE:
                set_intake_speed(-0.5);
                intake_state = IntakeStates.STOP_1;
                break;

            case STOP_1:
                set_intake_speed(0);
                intake_state = IntakeStates.FORWARD;
                break;

            case FORWARD:
                set_intake_speed(0.5);
                intake_state = IntakeStates.STOP_2;
                break;

            case STOP_2:
                set_intake_speed(0);
                intake_state = IntakeStates.REVERSE;
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
                SmartDashboard.putString("ALGAE PIVOT STATE", "UP");
                break;

            case INTAKE:
                while (pivot_motor.getEncoder().getPosition() > 5.8) {
                    pivot_motor.set(-0.2);
                }
                pivot_motor.set(0);
                pivot_states = PivotStates.SHOOT;
                SmartDashboard.putString("ALGAE PIVOT STATE", "DOWN");
                break;

            default:
                break;
        }
    }
}
