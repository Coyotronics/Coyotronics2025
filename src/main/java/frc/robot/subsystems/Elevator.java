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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.SubsystemConstants;

public class Elevator extends SubsystemBase {
    private final SparkMax right_motor;
    private final SparkMax left_motor;

    private final PIDController pid_controller;

    public Elevator() {
        right_motor = new SparkMax(SubsystemConstants.ELEVATOR_RIGHT_MOTOR_ID, MotorType.kBrushless);
        left_motor = new SparkMax(SubsystemConstants.ELEVATOR_LEFT_MOTOR_ID, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(15).idleMode(IdleMode.kBrake);

        right_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        left_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        pid_controller = new PIDController(SubsystemConstants.ELEVATOR_PID_P, SubsystemConstants.ELEVATOR_PID_I,
                SubsystemConstants.ELEVATOR_PID_D);

        right_motor.getEncoder().setPosition(0);
        left_motor.getEncoder().setPosition(0);
    }

    public void manual_elevator_rise() {
        right_motor.setVoltage(-4.0);
        left_motor.setVoltage(4.0);
    }

    public void move_up() {
        right_motor.setVoltage(-4);
        left_motor.setVoltage(4);
    }

    public void move_down() {
        right_motor.setVoltage(2.0);
        left_motor.setVoltage(-2.0);
    }

    public void pid_control(double setpoint) {
        double num = calculate_pid(setpoint);

        right_motor.set(-num);
        left_motor.set(num);
    }

    public void stop() {
        right_motor.setVoltage(-0.38);
        left_motor.setVoltage(0.38);
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
        SmartDashboard.putNumber("Elevator Height", get_height());

        if (get_height() > 39 && get_height() < 41) {
            SmartDashboard.putBoolean("L2 Ready", true);
        } else {
            SmartDashboard.putBoolean("L2 Ready", false);
        }

        if (get_height() > 67 && get_height() < 69) {
            SmartDashboard.putBoolean("L3 Ready", true);
        } else {
            SmartDashboard.putBoolean("L3 Ready", false);
        }
    }
}