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

public class Elevator extends SubsystemBase {
    private final double MAX_HEIGHT = 88;
    private final double MIN_HEIGHT = 1;

    private final SparkMax right_motor;
    private final SparkMax left_motor;

    private final PIDController pid_controller;

    public Elevator() {
        right_motor = new SparkMax(5, MotorType.kBrushless);
        left_motor = new SparkMax(6, MotorType.kBrushless);

        SparkMaxConfig config = new SparkMaxConfig();
        config.smartCurrentLimit(15).idleMode(IdleMode.kBrake);

        right_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        left_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        pid_controller = new PIDController(0.02, 0, 0);

        right_motor.getEncoder().setPosition(0);
        left_motor.getEncoder().setPosition(0);
    }

    public void move_up() {
        double pid = calculate_pid(MAX_HEIGHT);

        right_motor.set(-pid);
        left_motor.set(pid);
    }

    public void move_down() {
        double pid = calculate_pid(MIN_HEIGHT);

        right_motor.set(-pid);
        left_motor.set(pid);
    }

    public void pid_control(double setpoint) {
        double num = calculate_pid(setpoint);
        
        right_motor.set(-num);
        left_motor.set(num);
    }

    public void stop() {
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
        SmartDashboard.putNumber("Elevator Height", get_height());
    }
}