package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    SparkMax motor1 = new SparkMax(0, MotorType.kBrushless);
    SparkMax motor2 = new SparkMax(1, MotorType.kBrushless);
    PIDController pid = new PIDController(0.02, 0.02, 0.02);

    public Elevator() {
        SparkMaxConfig config1 = new SparkMaxConfig();
        SparkMaxConfig config2 = new SparkMaxConfig();
        config1.follow(1);
        config2.inverted(true);
        motor1.configure(config1, null, null);
        motor2.configure(config2, null, null);

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
                // motor2.set(pid.calculate(sensor.getRangeInches(), setpoint));
                motor1.setVoltage(1);
            }
        );
    }

    public Command manualControlDown() {
        return runOnce(
                () -> {
                    // motor2.set(pid.calculate(sensor.getRangeInches(), setpoint));
                    motor1.setVoltage(-1);
                });
    }

    public boolean exampleCondition() {
        // Query some boolean state, such as a digital sensor.
        return false;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}