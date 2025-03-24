package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.SubsystemConstants;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class AlgaeSubsystem extends SubsystemBase {
    private SparkMax intake_motor_right = new SparkMax(SubsystemConstants.ALGAE_INTAKE_MOTOR_RIGHT_ID,
            MotorType.kBrushless);
    private SparkMax intake_motor_left = new SparkMax(SubsystemConstants.ALGAE_INTAKE_MOTOR_LEFT_ID,
            MotorType.kBrushless);
    private SparkMax pivot_motor = new SparkMax(SubsystemConstants.ALGAE_PIVOT_MOTOR_ID, MotorType.kBrushless);


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
}
