// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    SparkMax coralIntake = new SparkMax(0, MotorType.kBrushless);
    SparkMax coralIntakePivot = new SparkMax(1, MotorType.kBrushless);
    SparkMax AlgaeIntakeMaster = new SparkMax(2, MotorType.kBrushless);
    SparkMax AlgaeIntakeSlave = new SparkMax(3, MotorType.kBrushless);
    Encoder throughBore = new Encoder(0, 0);
    PIDController pid = new PIDController(0.02, 0.02, 0.02);

    public Intake() {
        SparkMaxConfig config1 = new SparkMaxConfig();
        SparkMaxConfig config2 = new SparkMaxConfig();
        SparkMaxConfig config3 = new SparkMaxConfig();
        SparkMaxConfig config4 = new SparkMaxConfig();
        config3.follow(3);
        config3.inverted(true);
        coralIntake.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        coralIntakePivot.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        AlgaeIntakeMaster.configure(config3, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        AlgaeIntakeSlave.configure(config4, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
    public Command buttonSettings(boolean button1Pressed, boolean button2Pressed, boolean button3Pressed) {
        if (button1Pressed) {
            coralIntakeOn();
        } else if (button2Pressed) {
            algaeIntake();
        } else if (button3Pressed) {
            coralIntakePivot();
        }
        return null;

    }

    public void coralIntakeOn() {
        coralIntake.setVoltage(1.0);

    }

    public void algaeIntake() {
        AlgaeIntakeMaster.setVoltage(1.0);
    }

    public void coralIntakePivot() {
        coralIntakePivot.set(MathUtil.clamp(pid.calculate(throughBore.getDistance(), 0), 0.01, -0.01));
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