package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.SubsystemConstants;

public class ElevatorSubsystem extends SubsystemBase {
    private final SparkMax right_motor;
    private final SparkMax left_motor;

    private final PIDController pid_controller;

    public ElevatorSubsystem() {
        right_motor = new SparkMax(SubsystemConstants.ELEVATOR_RIGHT_MOTOR_ID, MotorType.kBrushless);
        left_motor = new SparkMax(SubsystemConstants.ELEVATOR_LEFT_MOTOR_ID, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(30).idleMode(IdleMode.kCoast);

        right_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        left_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        pid_controller = new PIDController(SubsystemConstants.ELEVATOR_PID_P, SubsystemConstants.ELEVATOR_PID_I,
                SubsystemConstants.ELEVATOR_PID_D);

        right_motor.getEncoder().setPosition(0);
        left_motor.getEncoder().setPosition(0);
    }

    public void move_up() {
        right_motor.setVoltage(-4);
        left_motor.setVoltage(4);
    }

    public void move_down() {
        right_motor.setVoltage(2.0);
        left_motor.setVoltage(-2.0);
    }
    
    public Command pid_controll(double setpoint) {
        return run(
            () -> {
                double num = calculate_pid(setpoint);

                right_motor.set(-num);
                left_motor.set(num);
            }
        ).until(() -> Math.round(calculate_pid(setpoint)) == 0);
    }

    public void stop() {
        if (get_height() > 0.2) {
            right_motor.setVoltage(-0.38);
            left_motor.setVoltage(0.38);

            return;
        }

        right_motor.setVoltage(0);
        left_motor.setVoltage(0);
    }

    private double get_height() {
        return left_motor.getEncoder().getPosition();
    }

    private double calculate_pid(double setpoint) {
        double output = pid_controller.calculate(get_height(), setpoint);
        output = MathUtil.clamp(output, -1.0, 1.0);

        return output;
    }

    @Override
    public void periodic() {
        stop();
        SmartDashboard.putNumber("Elevator Height", get_height());
    }
}