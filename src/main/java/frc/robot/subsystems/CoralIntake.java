package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class CoralIntake extends SubsystemBase {
    SparkMax coral_intake_motor = new SparkMax(36, MotorType.kBrushless);
    SparkMax coral_pivot_motor = new SparkMax(7, MotorType.kBrushless);

    public CoralIntake() {
        SparkMaxConfig intake_config = new SparkMaxConfig();
        SparkMaxConfig pivot_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);

        pivot_config.idleMode(IdleMode.kBrake);
        pivot_config.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder).pid(0.02, 0.02, 0.02).outputRange(-0.5, 0.5);

        coral_intake_motor.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        coral_pivot_motor.configure(pivot_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        coral_pivot_motor.getEncoder().setPosition(0);
    }

    public void manual_intake_control(double speed) {
        coral_intake_motor.set(speed);
    }

    public void pid_pivot_control(double setpoint) {
        coral_pivot_motor.getClosedLoopController().setReference(setpoint, ControlType.kPosition);
    }

    public void stop() {
        coral_intake_motor.set(0);
        coral_pivot_motor.set(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Pivot Encoder", coral_pivot_motor.getEncoder().getPosition());
        SmartDashboard.putNumber("Intake Applied", coral_intake_motor.getAppliedOutput()*12);
    }
}
