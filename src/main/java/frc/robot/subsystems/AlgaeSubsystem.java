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
    private boolean layer1 = false;
    private boolean layer2 = false;

    public AlgaeSubsystem() {
        SparkMaxConfig intake_config = new SparkMaxConfig();

        intake_config.idleMode(IdleMode.kCoast);

        intake_motor_right.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intake_motor_left.configure(intake_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void set_intake_speed() {
        layer1 = !layer1;
        if (layer1) {
            layer2 = !layer2;
        }

        if (layer1 && layer2) {
            intake_motor_left.set(0.2);
            intake_motor_right.set(-0.2);
        } else if ((layer1 && !layer2)) {
            intake_motor_left.set(-0.2);
            intake_motor_right.set(0.2);
        } else {
            intake_motor_left.set(0);
            intake_motor_right.set(0);
        }

    }
}
