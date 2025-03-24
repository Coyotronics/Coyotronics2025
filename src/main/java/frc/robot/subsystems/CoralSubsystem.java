package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import static edu.wpi.first.wpilibj2.command.Commands.parallel;
import static edu.wpi.first.wpilibj2.command.Commands.waitUntil;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import frc.robot.Constants.SubsystemConstants;

public class CoralSubsystem extends SubsystemBase {
    SparkMax coral_intake_motor = new SparkMax(SubsystemConstants.CORAL_INTAKE_MOTOR_ID, MotorType.kBrushless);
    SparkMax coral_pivot_motor = new SparkMax(SubsystemConstants.CORAL_PIVOT_MOTOR_ID, MotorType.kBrushless);
    AbsoluteEncoder coral_pivot_encoder = coral_pivot_motor.getAbsoluteEncoder();

    public CoralSubsystem() {
        SparkMaxConfig intake_config = new SparkMaxConfig();
        SparkMaxConfig pivot_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);
        pivot_config.idleMode(IdleMode.kBrake);

        coral_intake_motor.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        coral_pivot_motor.configure(pivot_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SmartDashboard.putString("CORAL INTAKE STATE", "IDLE");
        SmartDashboard.putString("CORAL PIVOT STATE", "IDLE");
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Pivot Encoder", get_pivot_position());
    }

    public Command pivot_to_intake() {
        return parallel(
            runOnce(() -> coral_pivot_motor.set(0.2)),
            waitUntil(() -> get_pivot_position() >= 0.4).andThen(() -> coral_pivot_motor.set(0))
        ).onlyIf(() -> get_pivot_position() < 0.4);
    }

    public Command pivot_to_shoot() {
        return parallel(
            runOnce(() -> coral_pivot_motor.set(-0.2)),
            waitUntil(() -> get_pivot_position() <= 0.3).andThen(() -> coral_pivot_motor.set(0))
        ).onlyIf(() -> get_pivot_position() > 0.3);
    }

    public Command coral_intake() {
        return runOnce(() -> coral_intake_motor.set(-0.25));
    }

    public Command coral_outtake() {
        return runOnce(() -> coral_intake_motor.set(0.25));
    }

    public Command coral_stop() {
        return runOnce(() -> coral_intake_motor.set(0));
    }

    private double get_pivot_position() {
        return coral_pivot_encoder.getPosition();
    }
}
