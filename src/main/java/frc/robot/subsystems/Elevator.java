package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
    SparkMax motor1;
    SparkMax motor2;

    PIDController pid = new PIDController(0.02, 0.02, 0.02);

    public Elevator() {
        motor1 = new SparkMax(5, MotorType.kBrushless);
        motor2 = new SparkMax(6, MotorType.kBrushless);

        SparkMaxConfig config1 = new SparkMaxConfig();
        SparkMaxConfig config2 = new SparkMaxConfig();
        
        config1.smartCurrentLimit(15).idleMode(IdleMode.kBrake);
        config2.smartCurrentLimit(15).inverted(true).idleMode(IdleMode.kBrake);

        motor1.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        motor2.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Example command factory method.
     *
     * @return a command
     */
    /**
     * An example method querying a boolean state of the subsystem (for example, a
     * digital sensor).
     *
     * @return value of some boolean subsystem state, such as a digital sensor.
     */

    public Command PIDcontrol_Stage(int setpoint) {
        return runOnce(
                () -> {
                    // motor2.set(pid.calculate(sensor.getRangeInches(), setpoint));
                });

    }

    public Command manualControlUp() {

        return runOnce(
                () -> {
                    motor1.setVoltage(4);
                    motor2.setVoltage(4);
                });
    }

    public Command manualControlDown() {
        return runOnce(
                () -> {
                    motor1.setVoltage(-4);
                    motor2.setVoltage(-4);
                });
    }

    public Command test() {
        return runOnce(
                () -> {
                    System.out.println("Some xbox key was pressed");
                });
    }

    public boolean exampleCondition() {
        return false;
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }
}