package frc.robot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.*;
//import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkClosedLoopController;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    private final SparkMax motor1;
    private final SparkMax motor2;
    private final RelativeEncoder encoder;
    private final PIDController pidController;

    private static final double kMaxHeight = 100.0; // Example max height in encoder ticks
    private static final double kMinHeight = 0.0;



    private static final double kP = 0.05; // Adjust this for tuning
    private static final double kI = 0.0;  // Usually 0 for simple control
    private static final double kD = 0.005; // Helps prevent overshoot
    private static final double kTolerance = 2.0;  // Deadband to stop jittering, according to delphi (idk man)

    public ElevatorSubsystem(int motor1ID, int motor2ID) {
        motor1 = new SparkMax(motor1ID, MotorType.kBrushless);
        motor2 = new SparkMax(motor2ID, MotorType.kBrushless);
        encoder = motor1.getEncoder();
        pidController = new PIDController(kP, kI, kD);

       /*  motor1.restoreFactoryDefaults();
        motor2.restoreFactoryDefaults(); //I don't think this is needed but Chief Delphi says it's good practice

        motor1.setSmartCurrentLimit(40);
        motor2.setSmartCurrentLimit(40); //This limits power to motors!*/
        //above was the 2024 implementation, here's the 2025 way to do it

        // Create a configuration object
        SparkMaxConfig config1 = new SparkMaxConfig();
        SparkMaxConfig config2 = new SparkMaxConfig();

        // Set the smart current limit
        config1.smartCurrentLimit(40);
        config2.smartCurrentLimit(40);

        // Apply the configuration to the motors with a reset
        motor1.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        config2.inverted(true);
        //Because the other motor is inverted

        motor2.configure(config2, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);

        encoder.setPosition(0); // Reset encoder at startup

        pidController.setTolerance(kTolerance); // Allow some tolerance because I THINK PID's need that. 
    }

    public void setPosition(double targetPosition) {
        targetPosition = Math.max(kMinHeight, Math.min(kMaxHeight, targetPosition)); // Constrain position. Stupidity protection!


        double speed = pidController.calculate(encoder.getPosition(), targetPosition); // PID output. Lit so simple. Returns a percentage of voltage I thinks

        speed = Math.max(-0.5, Math.min(0.5, speed)); // Limit motor speed between 0.5 and -0.5. Not tryna hurt my computer

        if (!pidController.atSetpoint()) { // Move only if not within tolerance. I don't need to add the extra tolerance checks because the PIDcontroller takes care of it
            motor1.set(speed);
            motor2.set(speed);
        } else {
            stop();
        }
    }
    public void manualControl(double speed){
        //This doesn't need a PID because it's open loop.

        if ( (speed > 0 && getPosition() < kMaxHeight) || (speed < 0 && getPosition() > kMinHeight)){
            motor1.set(speed);
            motor2.set(speed);
        }
        else{
            motor1.set(0);
            motor2.set(0);
        }


    }
    public void stop() { //killswtich
        motor1.set(0);
        motor2.set(0);
    }

    public double getPosition() {  //so that I can make kMaxHeight work in the future
        return encoder.getPosition();
    }
}
